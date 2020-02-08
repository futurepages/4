package org.futurepages.core.migration;

import org.futurepages.util.Is;

public interface VersionedDataModel {
	String getVersion();
	void setVersion(String value);

	default double getVersionNum(){
		if(Is.empty(getVersion())){
			return 0.0d;
		}else{
			return Double.valueOf(getVersion().replace("_","."));
		}
	}
}