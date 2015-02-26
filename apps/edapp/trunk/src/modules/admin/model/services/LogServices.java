package modules.admin.model.services;

import modules.admin.model.entities.Log;
import modules.admin.model.entities.User;
import modules.admin.model.entities.enums.LogType;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.core.persistence.Dao;

import java.util.Calendar;

public class LogServices {

	public static Log saveLog(Object objeto, LogType logType, User agent, String ipHost, String logContent, String obs, boolean withId, String ...fieldsNames) {
		try {
			Log log = new Log();

			log.setDateTime(Calendar.getInstance());
			log.setClassName(objeto.getClass().getName());
			log.setLogType(logType);
			log.setAgent(agent);
			log.setIpHost(ipHost);
			log.setLogContent(logContent);
			log.setObs(obs);

			if (fieldsNames != null && fieldsNames.length >= 0) {
				log.generateLogContent(objeto, withId, fieldsNames);
			}

			return Dao.getInstance().save(log);
		} catch (Exception ex) {
			AppLogger.getInstance().execute(ex);
		}

		return null;
	}

	public static Log saveLog(Object objeto, LogType logType, User agent, String ipHost) {
		try {
			return Dao.getInstance().save(new Log(objeto, logType, agent, ipHost));
		} catch (Exception ex) {
			AppLogger.getInstance().execute(ex);
		}

		return null;
	}

	public static Log saveLog(LogType logType, User agent, String ipHost, String obs) {
		try {
			Log log = new Log();
			log.setAgent(agent);
			log.setLogType(logType);
			log.setObs(obs);
			log.setIpHost(ipHost);

			return Dao.getInstance().save(log);
		} catch (Exception ex) {
			AppLogger.getInstance().execute(ex);
		}

		return null;
	}

	public static Log saveLog(User agent, User alvo, LogType logType, String obs) {
		try {
			Log log = new Log();
			log.setLogType(logType);
			log.setAgent(agent);
			log.setIdName("login");
			log.setIdValue(alvo.getLogin());
			log.setObs(obs);
			log.setDateTime(Calendar.getInstance());

			return Dao.getInstance().save(log);
		} catch (Exception ex) {
			AppLogger.getInstance().execute(ex);
		}
		return null;
	}

	public static Log saveLog(LogType logType, String logContent, String obs) {
		try {
			Log log = new Log();
			log.setLogType(logType);
			log.setLogContent(logContent);
			log.setObs(obs);
			log.setDateTime(Calendar.getInstance());

			return Dao.getInstance().save(log);
		} catch (Exception ex) {
			AppLogger.getInstance().execute(ex);
		}

		return null;
	}
}
