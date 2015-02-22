package org.futurepages.core.upload;

import com.vaadin.event.FieldEvents;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.UploadException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.futurepages.apps.simple.SimpleUI;
import org.futurepages.core.locale.Txt;
import org.futurepages.exceptions.UserException;
import org.futurepages.util.Is;

public class UploadField extends CustomComponent {

	private String caption;
	private int maxFileSizeMB;
	private UploadSuccessListener successListener;
	private UploadStartListener startListener;
	private UploadFinishListener finishListener;
	private AllowedTypes allowedTypes;

	public UploadField(String caption, int maxFileSizeMB, AllowedTypes allowedTypes, UploadSuccessListener successListener) {
		this.caption = caption;
		this.maxFileSizeMB = maxFileSizeMB;
		this.allowedTypes = allowedTypes;
		this.successListener = successListener;
		setCompositionRoot(build());
	}

	private Component build() {
		final VerticalLayout root = new VerticalLayout();
		final HorizontalLayout uploadingContainer = new HorizontalLayout();
		final ProgressBar progressBar = new ProgressBar();
		final Upload upload = new Upload(null, new UploadReceiver());
		final Button fakeUpload = new Button(caption);

		root.addComponent(uploadingContainer);
			uploadingContainer.setVisible(false);
			uploadingContainer.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
			uploadingContainer.addComponent(progressBar);
				progressBar.setVisible(true);
				progressBar.setValue(0.0f);
				progressBar.setIndeterminate(false);

		root.addComponent(fakeUpload);
			fakeUpload.setVisible(false);
			fakeUpload.setStyleName(ValoTheme.BUTTON_DANGER);
			fakeUpload.addFocusListener(fakeUploadFocusListener(upload, fakeUpload));

		root.addComponent(upload);
			upload.setImmediate(true);
			upload.setButtonCaption(caption);
			upload.addFailedListener(failedListener(upload, fakeUpload));
			upload.addSucceededListener(successUploadListener(successListener));
			upload.addStartedListener(startListener(uploadingContainer, upload, allowedTypes));
			upload.addProgressListener(progressListener(progressBar));
			upload.addFinishedListener(finishedListener(uploadingContainer));

		return root;
	}

	private Upload.FinishedListener finishedListener(HorizontalLayout uploadingContainer) {
		return event -> {
			if(finishListener!=null){
					finishListener.execute();
				}
			uploadingContainer.setVisible(false);
			if (SimpleUI.getCurrent().getPollInterval() > -1) {
				SimpleUI.getCurrent().setPollInterval(-1);
			}
		};
	}

	private Upload.ProgressListener progressListener(ProgressBar progressBar) {
		return (readBytes, contentLength) -> {
			progressBar.setValue(readBytes / (float) contentLength);
		};
	}

	private Upload.StartedListener startListener(HorizontalLayout uploadingContainer, Upload upload, AllowedTypes allowedTypes) {
		return event -> {
			if (Is.empty(upload.getDescription())) {
				if (!allowedFile(event.getMIMEType(),allowedTypes)) {
					upload.setDescription(String.format(Txt.get("upload.file_type_not_allowed"),allowedTypes.getInfoExt(), event.getMIMEType()));
					String errorMsg = upload.getDescription();
					upload.setVisible(false);
					throw new UserException(errorMsg);
				} else if (((event.getContentLength() / 1024f / 1024f) > maxFileSizeMB)) {
					upload.setDescription(String.format(Txt.get("upload.file_size_not_allowed"),maxFileSizeMB));
					String errorMsg = upload.getDescription();
					upload.setVisible(false);
					throw new UserException(errorMsg);
				} else {
					SimpleUI.getCurrent().setPollInterval(500);
					if(startListener!=null){
						startListener.execute();
					}
					uploadingContainer.setVisible(true);
				}
			} else {
				upload.interruptUpload();
			}
		};
	}

	private boolean allowedFile(String fileInputType, AllowedTypes allowedTypes) {
		for(String MIMEType : allowedTypes.getMimeTypes()){
			if(fileInputType.equals(MIMEType)){
				return true;
			}
		}
		return false;
	}

	private Upload.SucceededListener successUploadListener(UploadSuccessListener usl) {
		return event -> usl.uploadSucceeded(new UploadSuccessEvent(event));
	}

	private FieldEvents.FocusListener fakeUploadFocusListener(Upload upload, Button fakeUpload) {
		return event -> {
			fakeUpload.setVisible(false);
			upload.setVisible(true);
			upload.setDescription("");
		};
	}

	public Upload.FailedListener failedListener(final Upload upload, final Button fakeUpload) {
		return event -> upload.setErrorHandler(new DefaultErrorHandler() {

			@Override
			public void error(com.vaadin.server.ErrorEvent ev) {
				Throwable originalCause = ev.getThrowable();
				while (originalCause.getCause() != null) {
					originalCause = originalCause.getCause();
				}
				if (originalCause instanceof UserException) {
					if (!Is.empty(event.getUpload().getDescription())) {
						fakeUpload.setVisible(true);
						fakeUpload.setDescription(event.getUpload().getDescription());
						SimpleUI.getCurrent().notifyErrors((UserException) originalCause);
						fakeUpload.focus();
					}
				} else {
					if (!(originalCause instanceof UploadException)) {
						event.getUpload().setVisible(false);
						fakeUpload.setVisible(true);
						fakeUpload.setDescription("");
						SimpleUI.getCurrent().notifyFailure(originalCause);
					}
				}
			}
		});
	}

	public static interface UploadSuccessListener {
		/**
		 * Upload successfull..
		 *
		 * @param event the Upload successfull event.
		 */
		public void uploadSucceeded(UploadSuccessEvent event);
	}

	public static class UploadSuccessEvent extends Upload.SucceededEvent {

		public UploadSuccessEvent(Upload.SucceededEvent event) {
			super((Upload) event.getSource(), event.getFilename(), event.getMIMEType(), event.getLength());
		}

		public UploadReceiver getReceiver(){
			return (UploadReceiver) getUpload().getReceiver();
		}
	}

	public static enum AllowedTypes {
		IMAGES("*.jpg,*.png",    "image/png","image/jpg","image/jpeg","image/pjpeg");

		private String infoExt;
		private String[] mimeTypes;

		private AllowedTypes(String infoExt, String... mimeTypes){
			this.infoExt = infoExt;
			this.mimeTypes = mimeTypes;
		}

		public String getInfoExt() {
			return infoExt;
		}

		public String[] getMimeTypes() {
			return mimeTypes;
		}
	}

	public void setFinishListener(UploadFinishListener finishListener) {
		this.finishListener = finishListener;
	}

	public void setStartListener(UploadStartListener startListener) {
		this.startListener = startListener;
	}

	public static interface UploadStartListener {
		public void execute();
	}

	public static interface UploadFinishListener {
		public void execute();
	}
}