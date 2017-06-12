package org.futurepages.core.persistence;

import org.futurepages.core.exception.AppLogger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.jndi.JndiException;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HibernateManager {

	public static final String DEFAULT = "default";
	private boolean running = false;

	private Map<String, Configurations>       configurations;
	private Map<String, GenericDao>           genericDaos    = new HashMap<String,GenericDao>();
	private Map<String, SessionFactory>       factories      = new HashMap<String, SessionFactory>();
	private Map<String, ThreadLocal<Session>> sessionsTL     = new HashMap<String, ThreadLocal<Session>>();

	private static HibernateManager INSTANCE;

	/**
	 * Inicialização Estática da Conexão do Hibernate com o(s) Banco(s) de Dados.
	 */
	private HibernateManager() {
		initResources();
	}

	private synchronized void initResources() {
		HibernateConfigurationFactory hiberCfgFac = HibernateConfigurationFactory.getInstance();
		try {
			configurations = hiberCfgFac.getApplicationConfigurations();
			if (!configurations.isEmpty()) {
				for (String schemaId : configurations.keySet()) {
					log("registering '" + schemaId + "' schema.");
					Configuration config;
					config = configurations.get(schemaId).getEntitiesConfig();

					try{
						ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
						factories.put(schemaId, config.buildSessionFactory(serviceRegistry));
					} catch(JndiException ex){
						// caso esteja desenvolvendo localmente com mysql e precise usar o pool de conexoes:
							// crie um pool casando o nome com o nome do banco de dados, usuário 'root' e senha ''
						if(ex.getMessage().startsWith("Error parsing JNDI name [java:/comp/env/jdbc/")){
							String databaseName = ex.getMessage().replace("Error parsing JNDI name [java:/comp/env/jdbc/","").replace("]","");
							config.getProperties().remove("hibernate.connection.datasource");
							config.getProperties().setProperty("hibernate.connection.url","jdbc:mysql://localhost:3306/"+databaseName);
							config.getProperties().setProperty("hibernate.connection.username","root");
							config.getProperties().setProperty("hibernate.connection.password","");
							ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
							factories.put(schemaId, config.buildSessionFactory(serviceRegistry));
						} else {
							throw ex;
						}
					}
					sessionsTL.put(schemaId, new ThreadLocal<Session>());
					genericDaos.put(schemaId, GenericDao.newInstance(schemaId));
				}
				running = true;
			}
		} catch (Exception e) {
			log("Hibernate couldn´t be started: " + e.getMessage());
			AppLogger.getInstance().execute(e);
		}
	}

	public static HibernateManager getInstance(){
		if(INSTANCE==null){
			INSTANCE = new HibernateManager();
		}
		return INSTANCE;
	}

	public SessionFactory getSessionFactory() {
		return factories.get(DEFAULT);
	}

	public SessionFactory getSessionFactory(String sessionFactoryKey) {
		return factories.get(sessionFactoryKey);
	}

	public Configurations getConfigurations(String configurationKey) {
		return configurations.get(configurationKey);
	}

	public Configurations getConfigurations() {
		return getConfigurations(DEFAULT);
	}

	public Map<String, Configurations> getConfigurationsMap() {
		return configurations;
	}

	Session getSession() {
		return getSession(DEFAULT);
	}

	Session getSession(String schemaId) {
		Session session = getSessionTL(schemaId).get();

		if (session != null && session.isOpen()) {
				return session;
		}else{
			session = getSessionFactory(schemaId).withOptions().autoJoinTransactions(true).openSession();
			getSessionTL(schemaId).set(session);
			return session;
		}
	}

	void setSessionFactory(String schemaId, SessionFactory sessionFactory) {
		factories.put(schemaId, sessionFactory);
	}

	ThreadLocal<Session> getSessionTL(String schemaId) {
		return sessionsTL.get(schemaId);
	}

	public static boolean isRunning() {
		return getInstance().running;
	}

	public static void shutdown() {
		getInstance().doShutdown();
	}

	public void doShutdown() {
		try {
			log("Killing sessions...");
			for (ThreadLocal<Session> sTL : sessionsTL.values()) {
				if (sTL != null && sTL.get() != null) {//verifica se a sessao esta aberta se estiver fecha ela
					if(sTL.get().isOpen()){
						sTL.get().close();
					}
					sTL.remove();
				}
			}

			for (SessionFactory sf : factories.values()) {
				if (sf != null && !sf.isClosed()) { //se a fabrica de sessao não estiver fechada fecha ela
					sf.close();
				}
			}
			sessionsTL.clear();
			genericDaos.clear();
			sessionsTL.clear();

			log("Hibernate Sessions killed.");
		} catch (Exception ex) {
			log("Impossible to Kill hibernate-sessions:");
			AppLogger.getInstance().execute(ex);
		}

		// This manually deregisters JDBC drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                log("Deregistering jdbc-driver: "+  driver);
            } catch (SQLException e) {
                log("Error deregistering driver: "+e);
            }

        }
	}

	private void log(String msg) {
		System.out.println("   >> hibernate: "+msg);
	}

	GenericDao getGenericDao(String schemaId) {
		return genericDaos.get(schemaId);
	}

	GenericDao getDefaultGenericDao(){
		return genericDaos.get(DEFAULT);
	}

	public void closeSessions() {
		for(String schemaId : configurations.keySet()){
			ThreadLocal<Session> sessionTL =  getSessionTL(schemaId);
			Session session =sessionTL.get();
			if(session!=null && session.isOpen()){
				session.close();
			}
			sessionTL.remove();
		}
	}
}