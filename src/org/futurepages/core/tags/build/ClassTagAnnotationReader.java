package org.futurepages.core.tags.build;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.annotations.TagAttributeOverride;
import org.futurepages.annotations.TagAttributeOverrideArray;
import org.futurepages.util.Is;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.util.The;

/**
 *Leitor das Annotations para Tag
 * @author Danilo
 */
public class ClassTagAnnotationReader {
	/**
	 * Lê as informações contidas nas annotations de klass para geração de taglib.
	 * @param klass
	 * @return
	 */
	public TagBean readTag(Class klass) {
		TagBean tag = null;
		klass.getAnnotations();
		if (klass.isAnnotationPresent(Tag.class)) {
			tag = colectTag(klass);
			tag.setAttributes(colectAttributes(klass));
		}
		return tag;
	}

	/**
	 * Coleta as informações da tag.
	 * Não coleta informações sobre seus atributos
	 * @param klass
	 * @return
	 */
	private TagBean colectTag(Class klass) {
		TagBean tagBean = new TagBean();
		if (klass.isAnnotationPresent(Tag.class)) {
			Tag tag = (Tag) klass.getAnnotation(Tag.class);

			if (Is.empty(tag.name())) {
				tagBean.setName(The.uncapitalizedWord(klass.getSimpleName()));
			} else {
				tagBean.setName(tag.name());
			}

			if (Is.empty(tag.displayName())) {
				tagBean.setDisplayName(klass.getSimpleName());
			} else {
				tagBean.setDisplayName(tag.displayName());
			}

			tagBean.setTagClass(klass);
			tagBean.setContentType(tag.bodyContent());
		}

		return tagBean;
	}

	/**
	 * Coleta informações sobre os atributos da tag.
	 * @param klass
	 * @return
	 */
	private Map<String, TagAttributeBean> colectAttributes(Class klass) {

		Map<String, TagAttributeBean> allAttributes = new HashMap<String, TagAttributeBean>();
		Class superKlass = klass.getSuperclass();
		if(!superKlass.equals(Object.class)){
			allAttributes.putAll(colectAttributes(superKlass));
		}

		Field[] subFields = klass.getDeclaredFields();
		allAttributes.putAll(filterAnnotatedFields(subFields));
		applyAttributesOverrided(klass, allAttributes);
		return allAttributes;
	}

	private Map<String, TagAttributeBean>  filterAnnotatedFields(Field[] superFields) {
		Map<String, TagAttributeBean> attributes = new HashMap<String, TagAttributeBean>();
		TagAttributeBean attribute;
		for (Field field : superFields) {
			if ( field.isAnnotationPresent(TagAttribute.class) ) {
				attribute = createTagAttribute(field);
				attributes.put(attribute.getName(), attribute);
			}
		}
		return attributes;
	}


	/**
	 * coleta as informações de sobrescrita da tag.
	 * As informações coletadas sobrescreverão as informações contidas nas super classes;
	 * @param klass
	 * @param existentes
	 */
	private void applyAttributesOverrided(Class klass, Map<String, TagAttributeBean> existentes) {
		if(ReflectionUtil.isSomeAnnotationPresent(klass, TagAttributeOverrideArray.class)){
			TagAttributeOverrideArray tag = (TagAttributeOverrideArray) klass.getAnnotation(TagAttributeOverrideArray.class);
			for (TagAttributeOverride over : tag.value()) {
				String nomeAntiga = over.name();
				if(over.tagAttribute() == null || over.tagAttribute().name().equals("")){
					existentes.remove(nomeAntiga);
				}else{
					TagAttributeBean nova; 
					if(existentes.containsKey(nomeAntiga)){
						TagAttributeBean oldTag = existentes.get(nomeAntiga);
						existentes.remove(nomeAntiga);
						nova = createTagAttribute(over.tagAttribute(), oldTag);
					}else{
						nova = createTagAttribute(over, klass);
					}
					if(nova != null){
						existentes.put(nova.getName(), nova);
					}
				}
			}
		}
	}

	private TagAttributeBean createTagAttribute(TagAttributeOverride over, Class klass) {
		TagAttributeBean tag = null;
		
		Field f = ReflectionUtil.getObjectField(over.name(), klass);
		tag = createTagAttribute(f, over.tagAttribute()); 
		tag = createTagAttribute(over.tagAttribute(),tag);
		
		return tag;
	}

	private TagAttributeBean createTagAttribute(TagAttribute nova, TagAttributeBean oldTag) {
		oldTag.setName(nova.name());
		oldTag.setRequired(nova.required());
		oldTag.setRtexprvalue(nova.rtexprvalue());
		return oldTag;
	}

	private TagAttributeBean createTagAttribute(Field field) {
		TagAttribute tag = field.getAnnotation(TagAttribute.class);
		return createTagAttribute(field, tag);
	}
	
	private TagAttributeBean createTagAttribute(Field field, TagAttribute tag ) {

		TagAttributeBean attribute = new TagAttributeBean();
		attribute.setRequired(tag.required());
		attribute.setRtexprvalue(tag.rtexprvalue());
		attribute.setType(field.getType());

		if (Is.empty(tag.name())) {
			attribute.setName(field.getName());
		} else {
			attribute.setName(tag.name());
		}

		return attribute;
	}
}