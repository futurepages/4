package org.futurepages.components;

import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.futurepages.core.config.Apps;
import org.futurepages.core.locale.Txt;
import org.futurepages.util.ImageUtil;
import org.futurepages.util.Is;

import java.io.File;
import java.io.IOException;

public class ImageUploadField extends CustomComponent {


	private final VerticalLayout root;
    private final Image image;

	/**
	 * Default component that uses Txt default keys: change_image and remove_image
	 */
	public ImageUploadField(Property imageProperty, Resource noImageRes, Resource imageRes){
		this(imageProperty,imageRes, noImageRes,Txt.get("change_image"),Txt.get("remove_image"), Integer.parseInt(Apps.get("MAX_UPLOAD_MB_SIZE")), 300,300);
	}

	public ImageUploadField(Property imageProperty, Resource imageRes, Resource noImageRes, String uploadButtonCaption, String removeButtonDescription, int imageSizeInMB, int outputWidth, int outputHeight){

		// initializing components...
		root = new VerticalLayout();
        image = new Image(null, imageRes);
        final UploadField uploadField;
        final Button removeImageButton = new Button("");

		uploadField = new UploadField(uploadButtonCaption, imageSizeInMB, UploadField.AllowedTypes.IMAGES,
        event -> {
            File file = event.getReceiver().getNewFileResource().getSourceFile();
                try {
	                ImageUtil.resizeCropping(file, outputWidth, outputHeight, file.getAbsolutePath(), false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
	        image.setSource(event.getReceiver().getNewFileResource());
	        imageProperty.setValue(event.getReceiver().getNewFileName());
        });
		uploadField.setStartListener(() -> removeImageButton.setVisible(false));
		uploadField.setFinishListener(() -> { if (!Is.empty(imageProperty.getValue())) removeImageButton.setVisible(true); });

		removeImageButton.setDescription(removeButtonDescription);
		removeImageButton.setIcon(FontAwesome.TIMES);
		removeImageButton.setStyleName(ValoTheme.BUTTON_TINY);

         if(Is.empty(imageProperty.getValue())){
           removeImageButton.setVisible(false);
         }

        removeImageButton.addClickListener(event -> {
		    image.setSource(noImageRes);
	        imageProperty.setValue("");
	        removeImageButton.setVisible(false);
        });

		//configuring components (sizes, positions, margins, styles, etc)...
		root.setSizeUndefined();
        root.setSpacing(true);
		root.setVisible(true);
		root.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		image.setWidth(outputWidth / 2, Unit.PIXELS);
		image.setHeight(outputHeight / 2, Unit.PIXELS);
		removeImageButton.setStyleName("removeImage " + ValoTheme.BUTTON_TINY + " " + ValoTheme.BUTTON_ICON_ONLY);


		//adding and ordering...
        root.addComponent(image);
		root.addComponent(uploadField);
        root.addComponent(removeImageButton);

        setCompositionRoot(root);
	}

	public VerticalLayout getRoot() {
		return root;
	}

	public Image getImage() {
		return image;
	}
}