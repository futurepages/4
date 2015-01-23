package org.futurepages.emails;

import org.futurepages.exceptions.EmailException;
import org.futurepages.util.StringUtils;

// Revision: 155415

/**
 * This class is used to send simple internet email messages without
 * attachments.
 *
 * @author <a href="mailto:quintonm@bellsouth.net">Quinton McCombs</a>
 * @author <a href="mailto:colin.chalmers@maxware.nl">Colin Chalmers</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:frank.kim@clearink.com">Frank Y. Kim</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:unknown">Regis Koenig</a>
 * @version $Id: SimpleEmail.java,v 1.1 2006/03/02 17:11:41 soliveira Exp $
 */
public class SimpleEmail extends Email {
    
    public SimpleEmail() {
        super();
    }

    /**
     * Set the content of the mail
     *
     * @param msg A String.
     * @return An Email.
     * @throws EmailException see javax.mail.internet.MimeBodyPart
     *  for defintions
     */
    public Email setMsg(String msg) throws EmailException {
        if (StringUtils.isEmpty(msg)) {
            throw new EmailException("Invalid message supplied");
        }

        setContent(msg, TEXT_PLAIN);
        return this;
    }
}