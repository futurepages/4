package org.futurepages.core.migration;

import org.futurepages.util.Is;

public interface VersionedDataModel {

	String getVersion();
	void addVersion(String newVersion, String oldVersion, String log, int success, int fail);
	void registerNoChanges(String oldVer);

	default double getVersionNum(){
		if(Is.empty(getVersion())){
			return 0.0d;
		}else{
			return Double.valueOf(getVersion().replace("_","."));
		}
	}

	boolean installed();
}