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
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.futurepages.apps.simple.SimpleUI;
import org.futurepages.apps.simple.SimpleWindow;
import org.futurepages.components.CalendarDateField;
import org.futurepages.components.FileUploadField;
import org.futurepages.components.ImageUploadField;
import org.futurepages.core.locale.Txt;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.core.services.EntityForServices;
import org.futurepages.core.view.annotations.FieldCustom;
import org.futurepages.core.view.annotations.FieldDelete;
import org.futurepages.core.view.annotations.FieldDependency;
import org.futurepages.core.view.annotations.FieldFile;
import org.futurepages.core.view.annotations.FieldHTML;
import org.futurepages.core.view.annotations.FieldImage;
import org.futurepages.core.view.annotations.FieldPassword;
import org.futurepages.core.view.annotations.FieldStartGroup;
import org.futurepages.core.view.annotations.FieldStartGroupIcon;
import org.futurepages.core.view.annotations.FieldUpdate;
import org.futurepages.core.view.annotations.ForView;
import org.futurepages.core.view.annotations.PreSelectDependency;
import org.futurepages.core.view.types.EntityGenderType;
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
import java.util.List;
import java.util.Map;

public class ViewMaker extends HQLProvider{

	private static final Map<Class, Class<? extends Component>> mapComponents;

	static {
		mapComponents = new LinkedHashMap<>();
		mapComponents.put(boolean.class,         CheckBox.class);
		mapComponents.put(Boolean.class,         CheckBox.class);
		mapComponents.put(Calendar.class,        CalendarDateField.class);
		mapComponents.put(Date.class,            CalendarDateField.class);
		mapComponents.put(Temporal.class,        CalendarDateField.class);
		mapComponents.put(FieldPassword.class,   PasswordField.class);
		mapComponents.put(ManyToOne.class,       ComboBox.class);
		mapComponents.put(OneToOne.class,        ComboBox.class);
		mapComponents.put(FieldDependency.class, ComboBox.class);
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

	public ViewMaker(SimpleView simpleView,@ForView EntityForServices entity) {
		this.simpleView = simpleView;
		this.entity = entity;
		this.entityClass = entity.getClass();
		this.moduleId = ModuleUtil.moduleId(entityClass);
	}

	public void updateForm() {
		updateForm(ValoTheme.FORMLAYOUT_LIGHT);
	}

	public void updateForm(OnSuccessUpdateListener listener) {
		updateForm(ValoTheme.FORMLAYOUT_LIGHT, listener);
	}

	public void updateForm(String formStyle, OnSuccessUpdateListener listener) {
		updateFields(formStyle);
		simpleView.addFooter(updateFooterButton(listener));
	}

	public void updateForm(String formStyle) {
		updateFields(formStyle);
		simpleView.addFooter(updateFooterButton(null));
	}

	public BeanFieldGroup updateFields(String formStyle) {

		entity = (EntityForServices) entity.services().retrieve(entity);
		fieldGroup = new BeanFieldGroup<>(entityClass);
		AbstractOrderedLayout mainLayout = null;

		int customExpandRatio = 0;

		for (Field field : entityClass.getDeclaredFields()) {
			if (field.isAnnotationPresent(FieldUpdate.class)) {

				mainLayout = getLayoutToPut(mainLayout, field, formStyle, customExpandRatio);

				ViewElement ve = (new ViewElement(field));
				ve.adjustViewElementTo(mainLayout, fieldGroup);

				customExpandRatio = prepareNextLayoutToPut(field, ve.component, mainLayout);
			}
		}
		fieldGroup.setItemDataSource(entity);
		return fieldGroup;
	}

	private int prepareNextLayoutToPut(Field field, Component component, AbstractOrderedLayout mainLayout) {

		FieldCustom custom = field.getAnnotation(FieldCustom.class);
		if(custom!=null){
			String floatLeft = custom.floatLeft();
			if (!Is.empty(floatLeft)) {
				String[] floatLeftRatios = floatLeft.split("\\:");
				if (floatLeftRatios.length != 2) {
					throw new RuntimeException("@FieldCustom.floatLeft must be in 'N:N' format.");
				}try{
					mainLayout.setExpandRatio(component, Integer.parseInt(floatLeftRatios[0]));

				}catch(Exception ex){
					throw new RuntimeException("@FieldCustom.floatLeft('N:N'). N must be integer greater than zero.",ex);
				}
				return Integer.parseInt(floatLeftRatios[1]);
			}
		}
		return 0;
	}

	private AbstractOrderedLayout getLayoutToPut(AbstractOrderedLayout mainLayout, Field field, String formStyle, int customExpandRatio) {

		FieldStartGroup startGroup = field.getAnnotation(FieldStartGroup.class);
		FieldCustom custom = field.getAnnotation(FieldCustom.class);

		if(startGroup!=null || mainLayout==null){
			HorizontalLayout hLayout;
			hLayout = new HorizontalLayout();
			if (startGroup != null) {
				hLayout.setCaption(proccessLabel(startGroup.label()));
				hLayout.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
				hLayout.setMargin(true);
				FieldStartGroupIcon fsgi = field.getAnnotation(FieldStartGroupIcon.class);
				if (fsgi != null) {
					hLayout.setIcon(fsgi.value());
				}
				simpleView.addTab(hLayout); //TODO depois fazer para o caso do fieldset tambem.
			} else { //mainLayout is null
				simpleView.addComponent(hLayout);
			}
			AbstractOrderedLayout vLayout;
			if(custom!=null && !Is.empty(custom.floatLeft())){
				vLayout = new VerticalLayout();
			}else{
				vLayout = new FormLayout();
			}
			if (!Is.empty(formStyle)) {
				vLayout.setStyleName(formStyle);
			}
			hLayout.addComponent(vLayout);
			hLayout.setExpandRatio(vLayout, 1);
			mainLayout = vLayout;
		}
		if(customExpandRatio>0){
			FormLayout vLayout = new FormLayout();
			if (!Is.empty(formStyle)) {
				vLayout.setStyleName(formStyle);
			}
			vLayout.setMargin(false);
			mainLayout.addComponent(vLayout);
			mainLayout.setExpandRatio(vLayout, customExpandRatio);
			mainLayout = vLayout;
		}else{
			if (custom != null) {
				if (!Is.empty(custom.floatLeft())) {
					HorizontalLayout hLayout = new HorizontalLayout();
			        hLayout.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
					mainLayout.addComponent(hLayout);
					mainLayout = hLayout;
				}
			}
		}
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
		EntityGenderType gender = ((ForView)entityClass.getAnnotation(ForView.class)).gender();

		return updateFooterButton(String.format(Txt.get("viewmaker.entity_successfully_updated["+gender.name().toLowerCase()+"]"), proccessEntityLabel()), listener);
	}

	private String proccessEntityLabel() {
		String entityLabel = ((ForView)entityClass.getAnnotation(ForView.class)).label();
		if(!Is.empty(entityLabel)){
			return proccessLabel(entityLabel);
		}else{
			return Txt.get(moduleId+"."+The.camelCaseToLowerUnderscore(entityClass.getSimpleName()));
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
                SimpleUI.getCurrent().notifyErrors(new UserException(Txt.get("viewmaker.form_was_not_properly_filled")));
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
				FieldCustom custom = classField.getAnnotation(FieldCustom.class);
				String defaultTxtPath = defaultTxtPath(classField);
				String fieldLabel = (custom!=null && !Is.empty(custom.label())) ? proccessLabel(custom.label()) : Txt.get(defaultTxtPath);
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
							component = new ImageUploadField((String) ReflectionUtil.getField(entity, classField.getName()), (Resource) ReflectionUtil.getField(entity, fi.noImage()), (Resource) ReflectionUtil.getField(entity, fi.image()),getTxtWithSufix(defaultTxtPath,"prompt"));

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
				if(viewField.isVisible()){
					viewField.setCaption(fieldLabel);
				}
				if (viewField instanceof AbstractTextField) {
					((AbstractTextField) viewField).setNullRepresentation("");
				}
				if(viewField instanceof RichTextArea){
					((RichTextArea) viewField).setNullRepresentation("");
				}

				viewField.setId(classField.getName());
				if (viewField instanceof FieldEvents.BlurNotifier) {
					((FieldEvents.BlurNotifier) viewField).addBlurListener(event -> {
						Collection<Validator> validators = viewField.getValidators();
						if (validators == null || validators.isEmpty()) {
							viewField.addValidator(new BeanValidator(entityClass, viewField.getId())); //TODO review it in a big tree object when we have repeated attribute names.
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

			FieldUpdate fieldUpdate = classField.getAnnotation(FieldUpdate.class);
			if (fieldUpdate!=null && fieldUpdate.readOnly()) {
				viewField.setValue(ReflectionUtil.getField(entity,classField.getName()).toString());
				viewField.setRequired(false);
				viewField.setEnabled(false);
				viewField.setReadOnly(true);
			}else{
				if(classField.isAnnotationPresent(NotEmpty.class)){
					viewField.setRequired(true);
				}

				FieldDependency dependency = classField.getAnnotation(FieldDependency.class);
				if (dependency != null) {

					ComboBox mainCB = (ComboBox) viewField;
					if (!Is.empty(dependency.showAttr())) {
						mainCB.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
						mainCB.setItemCaptionPropertyId(dependency.showAttr());
					}

					Class<? extends Serializable> dependencyEntity = (Class<? extends Serializable>) classField.getType();
					if (dependency.pre().length == 1) {
						PreSelectDependency preSelect = dependency.pre()[0];
						String preSelectTxtPath = defaultTxtPath(classField,preSelect.groupBy());
						ComboBox preCB = new ComboBox(Txt.get(preSelectTxtPath));
						if (!Is.empty(preSelect.showAttr())) {
							preCB.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
							preCB.setItemCaptionPropertyId(preSelect.showAttr());
						}

						preCB.setInputPrompt(getTxtWithSufix(preSelectTxtPath, "prompt"));
						Class preEntity;
						Field preField;
						try {
							preField = dependencyEntity.getDeclaredField(preSelect.groupBy());
							preField.setAccessible(true);
							preEntity = preField.getType();
						} catch (NoSuchFieldException e) {
							throw new RuntimeException("Invalid @PreSelectDependency.groupBy=\""+preSelect.groupBy()+"\"",e);
						}
						List preList = entity.services().dao().list(hql(preEntity,fieldNotDeletedClause(preEntity) , preSelect.orderBy()));

						preCB.setContainerDataSource(new ListContainer(preList));

						String idName = entity.services().dao().getIdName(preEntity);

						preCB.addValueChangeListener(event -> {
							if (preCB.getValue() != null) {
								mainCB.setValue(null);
								List groupedList = entity.services().dao().list(hql(dependencyEntity,ands(field(preSelect.groupBy(),idName).equalsTo(entity.services().dao().getIdValue(preCB.getValue()).toString()),fieldNotDeletedClause(dependencyEntity)),dependency.orderBy()));
								mainCB.setContainerDataSource(new ListContainer(groupedList));
								mainCB.setEnabled(true);
								mainCB.setReadOnly(false);
								mainCB.setTextInputAllowed(true);
								mainCB.setInputPrompt(getDefaultTxt(classField, "prompt"));
							} else {
								mainCB.setValue(null);
								mainCB.setEnabled(false);
								mainCB.setReadOnly(true);
								mainCB.setTextInputAllowed(false);
							}
						});
						classField.setAccessible(true);
						try {
							Object depValue = classField.get(entity);
							if (depValue != null) {
								Object preObj = preField.get(depValue);
								preCB.setValue(preObj);
								List groupedList = entity.services().dao().list(hql(dependencyEntity, ands(field(preSelect.groupBy(), idName).equalsTo(entity.services().dao().getIdValue(preObj).toString()), fieldNotDeletedClause(dependencyEntity)),dependency.orderBy()));
								mainCB.setContainerDataSource(new ListContainer(groupedList));
								mainCB.setEnabled(true);
								mainCB.setReadOnly(false);
							} else {
								mainCB.setEnabled(false);
								mainCB.setReadOnly(true);
							}
						} catch (IllegalAccessException e) {
							throw new RuntimeException(e);
						}
						preCB.setSizeFull();
						mainLayout.addComponent(preCB);

					} else if (dependency.pre().length > 1) {
						throw new RuntimeException("@FieldDependency.pre() should have only one element.");
					} else {
						mainCB.setContainerDataSource(new ListContainer(entity.services().dao().list(hql(dependencyEntity, fieldNotDeletedClause(dependencyEntity), dependency.orderBy()))));
					}
				}
			}

			component.setSizeFull();
			mainLayout.addComponent(component);

			if(viewField.isEnabled() && !viewField.isReadOnly()){
				fieldGroup.bind(viewField, classField.getName());
			}
		}
	}

	private String fieldNotDeletedClause(Class entityClass) {
		FieldDelete fieldDelete;
		for(Field classField : entityClass.getDeclaredFields()){
			fieldDelete = classField.getAnnotation(FieldDelete.class);
			if(fieldDelete!=null){
				return field(classField.getName()).is(!fieldDelete.deleted());
			}
		}
		return null;
	}

	private String getDefaultTxt(Field classField,String sufix) {
		return getTxtWithSufix(defaultTxtPath(classField),sufix);
	}

	private String defaultTxtPath(Field classField) {
		return The.concat(moduleId, ".", entity.getClass().getSimpleName().toLowerCase(), ".", The.camelCaseToLowerUnderscore(classField.getName()));
	}
	private String defaultTxtPath(Field classField, String sufix) {
		return defaultTxtPath(classField)+(sufix!=null?"["+sufix+"]":"");
	}

	private String getTxtWithSufix(String pathName,String sufix) {
		return Txt.get(pathName+(sufix!=null?"["+sufix+"]":""));
	}

	private String getDefaultTxt(Field classField) {
		return getDefaultTxt(classField, null);
	}

	public static interface OnSuccessUpdateListener {
		public void execute(EntityForServices entity);
	}
}

//@FieldShow(formatter=MoneyFormatter.class,formatterParam="##.##")
//@FieldExplore(
//	  formatAsShow=false,
//	  formatter=MoneyFormatter.class,
//	  formatterParam="##.##",
//	  mainFilter= MainFilterType.AND_CONTAINS,
//      advancedFilter= AdvancedFilterType.CHOOSE_CONTAINS,
//	  sortable=true,
//	  hidden=false,
//      sortPriority=1
//)
//@FieldCreate
//@FieldDelete
//
//@FieldIcon(FontAwesome.ALIGN_JUSTIFY)
//@FieldStartGroup(FIELDSET)
//@FieldAutoNow(create=true,update=true)
