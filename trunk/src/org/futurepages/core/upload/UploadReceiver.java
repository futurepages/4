package org.futurepages.core.upload;

import com.vaadin.server.FileResource;
import com.vaadin.ui.Upload;
import org.futurepages.core.resource.UploadedTempResource;
import org.futurepages.util.FileUtil;
import org.futurepages.util.Security;
import org.futurepages.util.The;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class UploadReceiver implements Upload.Receiver {

        private String newFileName;
        private FileResource newFileResource;

        /**
         * return an OutputStream that simply counts lineends
         */
        @Override
        public OutputStream receiveUpload(final String fileName, final String MIMEType) {
            String prefix = The.concat(Thread.currentThread().getId(), "_", String.valueOf((new Date()).getTime()));
            this.newFileName = The.concat(prefix, "_", Security.md5(prefix + fileName).substring(0, 10), ".", FileUtil.extensionFormat(fileName));
            this.newFileResource = new UploadedTempResource(newFileName);
            File file =  newFileResource.getSourceFile();
            final FileOutputStream fos;
            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            return fos;
       }

        public String getNewFileName() {
            return newFileName;
        }

        public FileResource getNewFileResource() {
            return newFileResource;
        }
    }