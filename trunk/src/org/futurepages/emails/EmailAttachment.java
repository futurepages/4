package org.futurepages.emails;


import java.net.URL;


// Revision: 155415

/**
 * This class models an email attachment.  Used by MultiPartEmail.
 *
 * @author <a href="mailto:frank.kim@clearink.com">Frank Y. Kim</a>
 * @version $Id: EmailAttachment.java,v 1.1 2006/03/02 17:11:41 soliveira Exp $
 */
public class EmailAttachment {

    /** Defintion of the part being an attachment */
    public static final String ATTACHMENT = javax.mail.Part.ATTACHMENT;

    /** Defintion of the part being inline */
    public static final String INLINE = javax.mail.Part.INLINE;

    /** The name of this attachment. */
    private String name = "";

    /** The description of this attachment. */
    private String description = "";

    /** The path to this attachment (ie c:/path/to/file.jpg). */
    private String path = "";

    /** The HttpURI where the file can be got. */
    private URL url;

    /** The disposition. */
    private String disposition = EmailAttachment.ATTACHMENT;

    /**
     * Get the description.
     *
     * @return A String.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the name.
     *
     * @return A String.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the path.
     *
     * @return A String.
     */
    public String getPath() {
        return path;
    }

    /**
     * Get the URL.
     *
     * @return A URL.
     */
    public URL getURL() {
        return url;
    }

    /**
     * Get the disposition.
     *
     * @return A String.
     */
    public String getDisposition() {
        return disposition;
    }

    /**
     * Set the description.
     *
     * @param desc A String.
     */
    public void setDescription(String desc) {
        this.description = desc;
    }

    /**
     * Set the name.
     *
     * @param aName A String.
     */
    public void setName(String aName) {
        this.name = aName;
    }

    /**
     * Set the path to the attachment.  The path can be absolute or relative
     * and should include the filename.
     * <p>
     * Example: /home/user/images/image.jpg<br>
     * Example: images/image.jpg
     *
     * @param aPath A String.
     */
    public void setPath(String aPath) {
        this.path = aPath;
    }

    /**
     * Set the URL.
     *
     * @param aUrl A URL.
     */
    public void setURL(URL aUrl) {
        this.url = aUrl;
    }

    /**
     * Set the disposition.
     *
     * @param aDisposition A String.
     */
    public void setDisposition(String aDisposition) {
        this.disposition = aDisposition;
    }
}
