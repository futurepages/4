package modules.admin.model.entities.enums;

public enum LogType {
    ALL,
    CREATE,
    READ,
    UPDATE,
    DELETE,
    LOGIN,
    FILE_CHANGE,
    OTHER,
    SYSTEM,
	INATIVATE;

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
