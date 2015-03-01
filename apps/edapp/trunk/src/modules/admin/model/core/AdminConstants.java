package modules.admin.model.core;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;

/**
 *
 * @author leandro
 */
public interface AdminConstants {

	public static final String   SECURITY_KEY        = "kqEZES2uIKMLLSds343edHjnpKW6HwlhR5WcPiq8t0hrz92sAfq";
    public static final String   SUPER_ID            = "*";
	public static final long     MIN_SIZE_PASSWORD   =   6;
	public static final Resource AVATAR_DEFAULT_RES  = new ThemeResource("img/profile-pic-300px.jpg");

;
}