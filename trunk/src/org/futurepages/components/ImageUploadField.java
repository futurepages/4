package org.futurepages.components;

import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
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

public class ImageUploadField extends CustomComponent implements Property<String>, HasField {


	private final VerticalLayout root;
    private final Image image;
	private final Property<String> imageProperty;

	/**
	 * Default component that uses Txt default keys: change_image and remove_image
	 * and the value mounts a default invisible textField.
	 */
	public ImageUploadField(String value, Resource noImageRes, Resource imageRes){
		this(makeImageProperty(value), noImageRes, imageRes);
	}

	/**
	 * Default component that uses input caption and Txt default key: remove_image
	 */
	public ImageUploadField(String imageValue, Resource noImageRes, Resource imageRes, String caption){
		this(makeImageProperty(imageValue), imageRes, noImageRes, caption, Txt.get("remove_image"), Integer.parseInt(Apps.get("MAX_UPLOAD_MB_SIZE")), 300, 300);
	}


	public ImageUploadField(Property imageProperty, Resource noImageRes, Resource imageRes){
		this(imageProperty, imageRes, noImageRes, Txt.get("change_image"), Txt.get("remove_image"), Integer.parseInt(Apps.get("MAX_UPLOAD_MB_SIZE")), 300, 300);
	}

	public ImageUploadField(Property imgProperty, Resource imageRes, Resource noImageRes, String uploadButtonCaption, String removeButtonDescription, int imageSizeInMB, int outputWidth, int outputHeight){
		this.imageProperty = imgProperty;

		// initializing components...
		root = new VerticalLayout();
        image = new Image(null, imageRes);
        final Uploader uploader;
        final Button removeImageButton = new Button("");

		uploader = new Uploader(uploadButtonCaption, imageSizeInMB, Uploader.AllowedTypes.IMAGES,
        event -> {
            File file = event.getReceiver().getNewFileResource().getSourceFile();
                try {
	                ImageUtil.resizeCropping(file, outputWidth, outputHeight, file.getAbsolutePath(), false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
	        image.setSource(event.getReceiver().getNewFileResource());
	        this.imageProperty.setValue(event.getReceiver().getNewFileName());
        });
		uploader.setStartListener(() -> removeImageButton.setVisible(false));
		uploader.setFinishListener(() -> {
			if (!Is.empty(this.imageProperty.getValue())) removeImageButton.setVisible(true);
		});

		removeImageButton.setDescription(removeButtonDescription);
		removeImageButton.setIcon(FontAwesome.TIMES);
		removeImageButton.setStyleName(ValoTheme.BUTTON_TINY);

         if(Is.empty(this.imageProperty.getValue())){
           removeImageButton.setVisible(false);
         }

        removeImageButton.addClickListener(event -> {
	        image.setSource(noImageRes);
	        this.imageProperty.setValue("");
	        removeImageButton.setVisible(false);
        });

		//configuring components (sizes, positions, margins, styles, etc)...
		root.setSpacing(true);
		root.setVisible(true);
		root.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		image.setWidth(outputWidth / 2, Unit.PIXELS);
		image.setHeight(outputHeight / 2, Unit.PIXELS);
		removeImageButton.setStyleName("removeImage " + ValoTheme.BUTTON_TINY + " " + ValoTheme.BUTTON_ICON_ONLY);


		//adding and ordering...
        root.addComponent(image);
		root.addComponent(uploader);
        root.addComponent(removeImageButton);
		this.setWidth((outputWidth / 2), Unit.PIXELS);
        root.setWidth(image.getWidth(), Unit.PIXELS);
		uploader.setWidth(image.getWidth(), Unit.PIXELS);
		setCompositionRoot(root);
	}

	public VerticalLayout getRoot() {
		return root;
	}

	public Image getImage() {
		return image;
	}

	@Override
	public String getValue() {
		return this.imageProperty.getValue();
	}

	@Override
	public void setValue(String newValue) throws ReadOnlyException {
		this.imageProperty.setValue(newValue);
	}

	@Override
	public Class getType() {
		return String.class;
	}

	private static Property makeImageProperty(String value) {
		TextField tfImageProperty = new TextField();
		tfImageProperty.setVisible(false);
		tfImageProperty.setValue(value);
		return tfImageProperty;
	}

	@Override
	public AbstractField getField() {
		if(this.imageProperty instanceof AbstractField){
			return (AbstractField) this.imageProperty;
		}
		return null;
	}
}