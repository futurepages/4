package org.futurepages.core.resource;

import com.vaadin.server.FileResource;
import org.futurepages.core.config.Apps;

import java.io.File;

public class UploadedResource extends FileResource {

	public UploadedResource(String internalPath) {
		super(new File(Apps.get("UPLOADS_REAL_PATH")+"/"+internalPath));
	}
}
