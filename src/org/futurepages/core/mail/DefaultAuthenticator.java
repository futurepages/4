package org.futurepages.core.mail;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


// Revision: 155415

/**
 * This is a very simple authentication object that can be used for any
 * transport needing basic userName and password type authentication.
 *
 * @author <a href="mailto:quintonm@bellsouth.net">Quinton McCombs</a>
 * @version $Id: DefaultAuthenticator.java,v 1.1 2006/03/02 17:11:41 soliveira Exp $
 */
public class DefaultAuthenticator extends Authenticator {

    /** Stores the login information for authentication */
    private PasswordAuthentication authentication;

    /**
     * Default constructor
     *
     * @param userName user name to use when authentication is requested
     * @param password password to use when authentication is requested
     */
    public DefaultAuthenticator(String userName, String password) {
        this.authentication = new PasswordAuthentication(userName, password);
    }

    /**
     * Gets the authentication object that will be used to login to the mail
     * server.
     *
     * @return A <code>PasswordAuthentication</code> object containing the
     *         login information.
     */
	@Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return this.authentication;
    }
}
