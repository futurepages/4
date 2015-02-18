package org.futurepages.core.resource;

import com.vaadin.server.FileResource;
import org.futurepages.core.config.Apps;
import org.futurepages.core.services.EntityForServices;
import org.futurepages.util.ModuleUtil;
import org.futurepages.util.The;

import java.io.File;

public class UploadedResource extends FileResource {

	public UploadedResource(String internalPath) {
		super(new File(The.concat(Apps.get("UPLOADS_REAL_PATH"), "/", internalPath)));
	}
	public UploadedResource(EntityForServices entity, String internalPath) {
		super(new File(The.concat(Apps.get("UPLOADS_REAL_PATH"), "/", ModuleUtil.moduleId(entity.getClass()), "/",  internalPath)));
	}
}