package org.futurepages.core.resource;

import com.vaadin.server.FileResource;
import org.futurepages.core.config.Apps;
import org.futurepages.util.The;

import java.io.File;

public class UploadedTempResource extends FileResource {

	public UploadedTempResource(String internalPath) {
		super(new File(The.concat(getTempPath(),"/",internalPath)));
	}

	private static String getTempPath() {
		return The.concat(Apps.get("UPLOADS_REAL_PATH"), "/temp");
	}
}