
package modules.admin.model.entities.enums;

public enum ParamValueType {

    SIMPLE_TEXT(String.class),
    LONG_TEXT(String.class),
    HTML(String.class),
    INT(Integer.class),
    DOUBLE(Double.class),
    BOOLEAN(Boolean.class);

	private Class associatedType;

	private ParamValueType(Class associatedType){
		this.associatedType = associatedType;
	}

	public Class getAssociatedType() {
		return associatedType;
	}

    @Override
    public String toString() {
        return this.name();
    }

    public String getName() {
        return this.name();
    }

    public int getOrdinal() {
        return this.ordinal();
    }
}
