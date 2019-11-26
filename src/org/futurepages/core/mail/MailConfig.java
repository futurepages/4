package org.futurepages.core.mail;

import org.futurepages.core.config.Apps;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.emails.Email;

/**
 * Configuração dos valores padrão dos parâmetros de Configuração do Email.
 * 
 * @author leandro
 */
public class MailConfig {

    private static MailConfig INSTANCE;

    public static MailConfig getInstance(){
        if(INSTANCE==null){
            INSTANCE = new MailConfig();
        }
        return INSTANCE;
    }

    public final String  EMAIL_HOST_NAME;
    public final String  EMAIL_DEFAULT_PORT;
    public final boolean EMAIL_SSL_CONNECTION;
    public final String  EMAIL_USER_NAME;
    public final String  EMAIL_USER_PASSWORD;
    public final String  EMAIL_FROM;
    public final String  EMAIL_FROM_NAME;
    public final String  EMAIL_CHARSET;
    

    private MailConfig() {
        try{
            EMAIL_HOST_NAME      = Apps.get("EMAIL_HOST_NAME");
            EMAIL_DEFAULT_PORT   = Apps.get("EMAIL_DEFAULT_PORT");
            EMAIL_SSL_CONNECTION = Apps.get("EMAIL_SSL_CONNECTION").equals("true");
            EMAIL_USER_NAME      = Apps.get("EMAIL_USER_NAME");
            EMAIL_USER_PASSWORD  = Apps.get("EMAIL_USER_PASSWORD");
            EMAIL_FROM           = Apps.get("EMAIL_FROM");
            EMAIL_FROM_NAME      = Apps.get("EMAIL_FROM_NAME");
            EMAIL_CHARSET = (String) ReflectionUtil.staticField(Email.class, Apps.get("EMAIL_CHARSET"));
        }
        catch(Exception ex){
            throw new RuntimeException("Erro ao configurar serviço de email. ("+ex.getMessage()+")");
        }
    }

    public static void initialize() {
        MailConfig.getInstance();
    }
}