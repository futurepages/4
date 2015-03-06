package org.futurepages.core.view;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.futurepages.apps.simple.SimpleUI;
import org.futurepages.apps.simple.SimpleWindow;
import org.futurepages.components.CalendarDateField;
import org.futurepages.components.FileUploadField;
import org.futurepages.components.ImageUploadField;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.services.EntityForServices;
import org.futurepages.core.view.annotations.FieldDependency;
import org.futurepages.core.view.annotations.FieldFile;
import org.futurepages.core.view.annotations.FieldHTML;
import org.futurepages.core.view.annotations.FieldImage;
import org.futurepages.core.view.annotations.FieldPassword;
import org.futurepages.core.view.annotations.FieldStartGroup;
import org.futurepages.core.view.annotations.FieldStartGroupIcon;
import org.futurepages.core.view.annotations.FieldUpdate;
import org.futurepages.core.view.annotations.ForView;
import org.futurepages.exceptions.UserException;
import org.futurepages.util.Is;
import org.futurepages.util.ModuleUtil;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.util.The;
import org.hibernate.validator.constraints.NotEmpty;
import org.vaadin.maddon.ListContainer;

import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ViewMaker {

	private static final Map<Class, Class<? extends Component>> mapComponents;

	static {
		mapComponents = new LinkedHashMap<>();
		mapComponents.put(Calendar.class,        CalendarDateField.class);
		mapComponents.put(Date.class,            CalendarDateField.class);
		mapComponents.put(Temporal.class,        CalendarDateField.class);
		mapComponents.put(FieldPassword.class,   PasswordField.class);
		mapComponents.put(ManyToOne.class,       ComboBox.class); //pode ser radioButton tb
		mapComponents.put(OneToOne.class,        ComboBox.class); //pode ser radioButton tb
		mapComponents.put(FieldDependency.class, ComboBox.class); //pode ser radioButton tb
		mapComponents.put(FieldImage.class,      ImageUploadField.class);
		mapComponents.put(FieldFile.class,       FileUploadField.class);
		mapComponents.put(FieldHTML.class,       RichTextArea.class);
		mapComponents.put(Lob.class,             TextArea.class);

		//TODO:
		//OneToMany, Cascade
		//OneToMany, NotCascade
		//ManyToMany, Cascade
		//ManyToMany, NotCascade
		//MaskedField
	}
	private EntityForServices entity;
	private Class entityClass;
	private String moduleId;
	private BeanFieldGroup fieldGroup;
	private SimpleView simpleView;

	public ViewMaker(SimpleView simpleView, @ForView EntityForServices entity) {
		this.simpleView = simpleView;
		this.entity = entity;
		this.entityClass = entity.getClass();
		this.moduleId = ModuleUtil.moduleId(entityClass);
	}

	public void updateForm() {
		updateForm(ValoTheme.FORMLAYOUT_LIGHT);
	}

	public void updateForm(OnSuccessUpdateListener listener) {
		updateForm(ValoTheme.FORMLAYOUT_LIGHT);
		simpleView.addFooter(updateFooterButton(listener));
	}

	public void updateForm(String formStyle, OnSuccessUpdateListener listener) {
		updateForm(formStyle);
		simpleView.addFooter(updateFooterButton(listener));
	}


	public void updateForm(String formStyle) {
		updateFields(formStyle);
		simpleView.addFooter(updateFooterButton(null));
	}

	public BeanFieldGroup updateFields(String formStyle) {

		entity = (EntityForServices) entity.services().retrieve(entity);

		fieldGroup = new BeanFieldGroup<>(entityClass);

		Layout mainLayout = null;

		for (Field field : entityClass.getDeclaredFields()) {

			if (field.isAnnotationPresent(FieldUpdate.class)) {

				mainLayout = getLayoutToPut(mainLayout, field, formStyle);

				(new ViewElement(field)).adjustViewElementTo(mainLayout,fieldGroup);
			}

		}
		fieldGroup.setItemDataSource(entity);

		return fieldGroup;
	}

	private Layout getLayoutToPut(Layout mainLayout, Field field, String formStyle) {
		FieldStartGroup startGroup = field.getAnnotation(FieldStartGroup.class);
		if (startGroup != null) {
			HorizontalLayout hLayout = new HorizontalLayout();
			hLayout.setCaption(proccessLabel(startGroup.label()));
			hLayout.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
			hLayout.setSpacing(true);
			hLayout.setMargin(true);
			FieldStartGroupIcon fsgi = field.getAnnotation(FieldStartGroupIcon.class);
			if (fsgi != null) {
				hLayout.setIcon(fsgi.value());
			}
			simpleView.addTab(hLayout); //TODO depois fazer para o caso do fieldset tambem.
			FormLayout vLayout = new FormLayout();
			if (!Is.empty(formStyle)) {
				vLayout.setStyleName(formStyle);
			}
			hLayout.addComponent(vLayout);
			hLayout.setExpandRatio(vLayout, 1);
			mainLayout = vLayout;
		} else if (mainLayout == null) {
			HorizontalLayout hLayout = new HorizontalLayout();
			simpleView.addComponent(hLayout);
			FormLayout vLayout = new FormLayout();
			if (!Is.empty(formStyle)) {
				vLayout.setStyleName(formStyle);
			}
			hLayout.addComponent(vLayout);
			hLayout.setExpandRatio(vLayout, 1);
			mainLayout = vLayout;
		}
		//int expandFlag = 0;
		//if(field.isAnnotationPresent(FieldCustom.class) || expandFlag>0){
		//FieldCustom fc = field.getAnnotation(FieldCustom.class);
		//if(!Is.empty(fc.floatRatio())){
		//	 if(expandFlag>0){
		//		 throw new RuntimeException("You cannot put floatRatio inside another. See field "+field.getName());
		//	 }else{
		//		 String[] floatRatio = fc.floatRatio().split("\\:");
		//		 int ratio1 = Integer.parseInt(floatRatio[0]);
		//		 int ratio2 = Integer.parseInt(floatRatio[1]);
		////							 ((HorizontalLayout)theLayout).setExpandRatio();
		//	 }
		//}
		//} else if(expandFlag>0){
		////					((HorizontalLayout)theLayout).setExpandRatio();
		//}
		//FormLayout vLayout = new FormLayout();
		//if(!Is.empty(formStyle)){
		//	vLayout.setStyleName(formStyle);
		//}
		//hLayout.addComponent(vLayout);
		return mainLayout;
	}

	private String proccessLabel(String label) {
		if (label.startsWith("Txt:")) {
			label = label.substring(4);
			if (label.startsWith("$this.")) {
				label = Txt.get(The.concat(moduleId, ".", entityClass.getSimpleName().toLowerCase(), ".", label.substring(6)));
			}
		}
		return label;
	}

	private Class<? extends Component> getComponent(Field field) {
		Class componentType = null;
		for (Class type : mapComponents.keySet()) {
			if (field.getType()==type || field.isAnnotationPresent(type)) {
				componentType = mapComponents.get(type);
				break;
			}
		}
		return componentType;
	}

	public Layout updateFooterButton(OnSuccessUpdateListener listener){
		String genderSufix = ((ForView)entityClass.getAnnotation(ForView.class)).genderSufix();
		return updateFooterButton(String.format(Txt.get("entity_successfully_updated"), proccessEntityLabel(),genderSufix), listener);
	}

	private String proccessEntityLabel() {
		String entityLabel = ((ForView)entityClass.getAnnotation(ForView.class)).label();
		if(!Is.empty(entityLabel)){
			return proccessLabel(entityLabel);
		}else{
			return Txt.get("$."+The.camelCaseToLowerUnderscore(entityClass.getSimpleName()));
		}
	}

	public Layout updateFooterButton(String successMsg, OnSuccessUpdateListener updateListener){
		HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

		Button button = updateButton(successMsg, updateListener);
        footer.addComponent(button);
        footer.setComponentAlignment(button, Alignment.TOP_RIGHT);
        return footer;
	}

	public Button updateButton(String successMsg, OnSuccessUpdateListener updateListener) {

		Button ok = new Button(Txt.get("save"));
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(event -> {
            try {
                fieldGroup.commit();
                entity.services().update(entity);
                entity.services().dao().flush(); //flush is here because of a possible eviction before the transaction commit
                SimpleUI.getCurrent().notifySuccess(successMsg);
	            if(simpleView instanceof SimpleWindow){
		            ((SimpleWindow)simpleView).close();
	            }
	            if(updateListener!=null){
		            updateListener.execute(entity);
	            }
            } catch (UserException e) {
                SimpleUI.getCurrent().notifyErrors(e);
            } catch (FieldGroup.CommitException e1) {
                SimpleUI.getCurrent().notifyErrors(new UserException("O formulário não foi devidamente preenchido."));
                //throw new RuntimeException(e1);
            }
        });
        ok.focus();
        return ok;
	}

	private class ViewElement {

		private Field classField;
		private AbstractField viewField;
		private Component component;

		public ViewElement(Field classField) {
			Component component;
			AbstractField viewField;
			try {
				Class<? extends Component> componentClass = getComponent(classField);
				if (componentClass == null) {
					viewField = TextField.class.newInstance();
					component = viewField;
				} else {
					if (AbstractField.class.isAssignableFrom(componentClass)) {
						viewField = (AbstractField) componentClass.newInstance();
						component = viewField;
					} else if (HasField.class.isAssignableFrom(componentClass)) {
						if (componentClass == ImageUploadField.class) {
							FieldImage fi = classField.getAnnotation(FieldImage.class);
							if (classField.getType() != String.class) {
								throw new RuntimeException("Only Strings attributes could be @FieldImage");
							}
							component = new ImageUploadField((String) ReflectionUtil.getField(entity, classField.getName()), (Resource) ReflectionUtil.getField(entity, fi.noImage()), (Resource) ReflectionUtil.getField(entity, fi.image()));

						} else if (componentClass == FileUploadField.class) {
							//todo nao foi testado o componente ainda...
							FieldFile ff = classField.getAnnotation(FieldFile.class);
							if (classField.getType() != String.class) {
								throw new RuntimeException("Only Strings attributes could be @FieldFile");
							}
							component = new FileUploadField((String) classField.get(entity), (Resource) ReflectionUtil.getField(entity, ff.file()));
						} else {
							component = componentClass.newInstance();
						}
						viewField = ((HasField) component).getField();
					} else {
						throw new RuntimeException(componentClass + " must be AbstractField or HasField");
					}
				}
				viewField.setCaption(Txt.get(The.concat(moduleId, ".", entity.getClass().getSimpleName().toLowerCase(), ".", The.camelCaseToLowerUnderscore(classField.getName()))));
				if (viewField instanceof AbstractTextField) {
					((AbstractTextField) viewField).setNullRepresentation("");
				}

				if (viewField instanceof FieldEvents.BlurNotifier) {
					((FieldEvents.BlurNotifier) viewField).addBlurListener(event -> {
						Collection<Validator> validators = viewField.getValidators();
						if (validators == null || validators.isEmpty()) {
							viewField.addValidator(new BeanValidator(entityClass, classField.getName()));
						}
					});
				}
				this.classField = classField;
				this.component = component;
				this.viewField = viewField;
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		private void adjustViewElementTo(Layout mainLayout, FieldGroup fieldGroup) {

			Temporal temporal = classField.getDeclaredAnnotation(Temporal.class);
			if (temporal != null && viewField instanceof CalendarDateField) {
				if (classField.getDeclaredAnnotation(Temporal.class).value() == TemporalType.DATE) {
					((CalendarDateField) viewField).setResolution(Resolution.DAY);
				} else {
					((CalendarDateField) viewField).setResolution(Resolution.MINUTE);
				}
			}

			if (classField.getAnnotation(FieldUpdate.class).readOnly()) {

//				Label label = new Label(ReflectionUtil.getField(entity,classField.getName()).toString());
//				label.setCaption(viewField.getCaption());
//				component = label;
//				viewField = null;
				viewField.setValue(ReflectionUtil.getField(entity,classField.getName()).toString());
				viewField.setRequired(false);
				viewField.setEnabled(false);
				viewField.setReadOnly(true);
			}else{
				if(classField.isAnnotationPresent(NotEmpty.class)){
					viewField.setRequired(true);
				}

				FieldDependency dependency = classField.getAnnotation(FieldDependency.class);
				if(dependency!=null){
					ComboBox comboBox = (ComboBox) viewField;
					//TODO contemplar ordenação e listagem indireta
					Class<? extends Serializable> classX = (Class<? extends Serializable>) classField.getType();
					comboBox.setContainerDataSource(new ListContainer(Dao.getInstance().list(classX)));
				}
			}

			mainLayout.addComponent(component);

			if(viewField.isEnabled() && !viewField.isReadOnly()){
				fieldGroup.bind(viewField, classField.getName());
			}
		}
	}

	public static interface OnSuccessUpdateListener {
		public void execute(EntityForServices entity);
	}
}
