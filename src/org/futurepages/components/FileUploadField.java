package org.futurepages.components;

import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Link;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.futurepages.core.config.Apps;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.upload.Uploader;
import org.futurepages.core.view.HasField;
import org.futurepages.util.ImageUtil;
import org.futurepages.util.Is;

import java.io.File;
import java.io.IOException;

public class FileUploadField extends CustomComponent implements Property<String>, HasField {


	private final VerticalLayout root;
    private final Link fileLink;
	private final Property<String> fileProperty;

	/**
	 * Default component that uses Txt default keys: change_image and remove_image
	 * and the value mounts a default invisible textField.
	 */
	public FileUploadField(String value, Resource fileRes){
		this(makeFileProperty(value), fileRes);
	}

	/**
	 * Default component that uses Txt default keys: change_image and remove_image
	 */
	public FileUploadField(Property imageProperty, Resource fileRes){
		this(imageProperty,fileRes, Txt.get("change_file"), Txt.get("remove_file"), Integer.parseInt(Apps.get("MAX_UPLOAD_MB_SIZE")));
	}

	public FileUploadField(Property imgProperty, Resource fileRes, String uploadButtonCaption, String removeButtonDescription, int imageSizeInMB){
		this.fileProperty = imgProperty;

		// initializing components...
		root = new VerticalLayout();
        fileLink = new Link(null,fileRes);
        final Uploader uploader;
        final Button removeFileButton = new Button("");

		uploader = new Uploader(uploadButtonCaption, imageSizeInMB, Uploader.AllowedTypes.IMAGES,
        event -> {
            //File file = event.getReceiver().getNewFileResource().getSourceFile();
	        fileLink.setResource(event.getReceiver().getNewFileResource());
	        fileProperty.setValue(event.getReceiver().getNewFileName());
        });
		uploader.setStartListener(() -> removeFileButton.setVisible(false));
		uploader.setFinishListener(() -> {
			if (!Is.empty(fileProperty.getValue())) removeFileButton.setVisible(true);
		});

		removeFileButton.setDescription(removeButtonDescription);
		removeFileButton.setIcon(FontAwesome.TIMES);
		removeFileButton.setStyleName(ValoTheme.BUTTON_TINY);

         if(Is.empty(this.fileProperty.getValue())){
           removeFileButton.setVisible(false);
         }

        removeFileButton.addClickListener(event -> {
	        fileLink.setResource(null);
	        this.fileProperty.setValue("");
	        removeFileButton.setVisible(false);
        });

		//configuring components (sizes, positions, margins, styles, etc)...
		root.setSizeUndefined();
        root.setSpacing(true);
		root.setVisible(true);
		root.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		removeFileButton.setStyleName("removeFile " + ValoTheme.BUTTON_TINY + " " + ValoTheme.BUTTON_ICON_ONLY);


		//adding and ordering...
        root.addComponent(fileLink);
		root.addComponent(uploader);
        root.addComponent(removeFileButton);

        setCompositionRoot(root);
	}

	public VerticalLayout getRoot() {
		return root;
	}

	public Link getFileLink() {
		return fileLink;
	}

	@Override
	public String getValue() {
		return this.fileProperty.getValue();
	}

	@Override
	public void setValue(String newValue) throws ReadOnlyException {
		this.fileProperty.setValue(newValue);
	}

	@Override
	public Class getType() {
		return String.class;
	}

	private static Property makeFileProperty(String value) {
		TextField tfImageProperty = new TextField();
		tfImageProperty.setVisible(false);
		tfImageProperty.setValue(value);
		return tfImageProperty;
	}

	@Override
	public AbstractField getField() {
		if(this.fileProperty instanceof AbstractField){
			return (AbstractField) this.fileProperty;
		}
		return null;
	}
}