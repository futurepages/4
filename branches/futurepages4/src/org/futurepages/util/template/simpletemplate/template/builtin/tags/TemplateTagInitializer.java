package org.futurepages.util.template.simpletemplate.template.builtin.tags;

/**
 *
 * @author thiago
 */
public class TemplateTagInitializer {
	
	static {
		instance = new TemplateTagInitializer();
	}
	
	private static final TemplateTagInitializer instance;
	
	public static TemplateTagInitializer getInstance() {
		return instance;
	}

	public static TemplateTagInitializer instance() {
		return instance;
	}
	
	private TemplateTagInitializer() {
		initBuildin();
	}
	
	private void initBuildin() {
		TemplateTag.addBuiltinTag(new IfTemplateTag());
		TemplateTag.addBuiltinTag(new ForEachTemplateTag());
		TemplateTag.addBuiltinTag(new SetTemplateTag());
		TemplateTag.addBuiltinTag(new ValueFormatterTemplateTag());
	}
	
	public void addTag(TemplateTag tag) {
		if (tag != null) {
			TemplateTag.addCustomTag(tag);
		}
	}
}
