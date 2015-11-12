package org.futurepages.core.quartz;

import java.io.File;
import java.text.ParseException;
import java.util.Collection;
import org.futurepages.exceptions.NotModuleException;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * //TODO comment...
 * @author leandro
 */
public class QuartzManager {

	private static SchedulerFactory schedulerFactory;
	
	public static void initialize(File[] modules) throws SchedulerException, ClassNotFoundException, ParseException, NotModuleException {
		schedulerFactory = new StdSchedulerFactory();
		(new QuartzJobsRegister(modules)).register();
		schedulerFactory.getScheduler().start();
	}
	
	public static SchedulerFactory getSchedulerFactory(){
		return schedulerFactory;
	}

	public static void shutdown() throws SchedulerException {
		System.out.println("Killing Quartz's Jobs...");
		Collection<Scheduler> schedulers = schedulerFactory.getAllSchedulers();
		for(Scheduler scheduler : schedulers){
			scheduler.shutdown();
		}
		System.out.println("Quartz's Jobs successful killed.");
	}
}
