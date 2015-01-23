package org.futurepages.core.tags.build;

/**
 * Atributo de uma Tag jsp
 * @author Danilo
 */
public class TagAttributeBean implements Comparable<TagAttributeBean>{

    private String name;
    private boolean required;
    private boolean rtexprvalue;
    private Class type;

    public TagAttributeBean(){
    }

    public TagAttributeBean(String name, boolean required, boolean rtexprvalue, Class type) {
        this.name = name;
        this.required = required;
        this.rtexprvalue = rtexprvalue;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isRtexprvalue() {
        return rtexprvalue;
    }

    public void setRtexprvalue(boolean rtexprvalue) {
        this.rtexprvalue = rtexprvalue;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

	@Override
	public int compareTo(TagAttributeBean other) {
		return this.getName().compareTo(other.getName());
	}
    
    
}
