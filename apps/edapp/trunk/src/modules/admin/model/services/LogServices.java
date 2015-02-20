package modules.admin.model.services;

public class LogServices {

//	public static Log saveLog(Object objeto, LogType logType, User agent, String ipHost, String logContent, String obs, boolean withId, String ...fieldsNames) {
//		try {
//			Log log = new Log();
//
//			log.setDateTime(Calendar.getInstance());
//			log.setClassName(objeto.getClass().getName());
//			log.setLogType(logType);
//			log.setAgent(agent);
//			log.setIpHost(ipHost);
//			log.setLogContent(logContent);
//			log.setObs(obs);
//
//			if (fieldsNames != null && fieldsNames.length >= 0) {
//				log.generateLogContent(objeto, withId, fieldsNames);
//			}
//
//			return Dao.save(log);
//		} catch (Exception ex) {
//			DefaultExceptionLogger.getInstance().execute(ex);
//		}
//
//		return null;
//	}
//
//	public static Log saveLog(Object objeto, LogType logType, User agent, String ipHost) {
//		try {
//			return Dao.save(new Log(objeto, logType, agent, ipHost));
//		} catch (Exception ex) {
//			DefaultExceptionLogger.getInstance().execute(ex);
//		}
//
//		return null;
//	}
//
//	public static Log saveLog(LogType logType, User agent, String ipHost, String obs) {
//		try {
//			Log log = new Log();
//			log.setAgent(agent);
//			log.setLogType(logType);
//			log.setObs(obs);
//			log.setIpHost(ipHost);
//
//			return Dao.save(log);
//		} catch (Exception ex) {
//			DefaultExceptionLogger.getInstance().execute(ex);
//		}
//
//		return null;
//	}
//
//	public static Log saveLog(User agent, User alvo, LogType logType, String obs) {
//		try {
//			Log log = new Log();
//			log.setLogType(logType);
//			log.setAgent(agent);
//			log.setIdName("login");
//			log.setIdValue(alvo.getLogin());
//			log.setObs(obs);
//			log.setDateTime(Calendar.getInstance());
//
//			return Dao.save(log);
//		} catch (Exception ex) {
//			DefaultExceptionLogger.getInstance().execute(ex);
//		}
//		return null;
//	}
//
//	public static Log saveLog(LogType logType, String logContent, String obs) {
//		try {
//			Log log = new Log();
//			log.setLogType(logType);
//			log.setLogContent(logContent);
//			log.setObs(obs);
//			log.setDateTime(Calendar.getInstance());
//
//			return Dao.save(log);
//		} catch (Exception ex) {
//			DefaultExceptionLogger.getInstance().execute(ex);
//		}
//
//		return null;
//	}
}
