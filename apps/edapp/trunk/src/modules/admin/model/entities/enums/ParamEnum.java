package modules.admin.model.entities.enums;

/**
 *
 * @author TJPI
 */
public enum ParamEnum {

	EXPIRED_PASS_IN_DAYS("expiredPasswordInDays");
	
	private String id;
	
	private ParamEnum(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
		
}
