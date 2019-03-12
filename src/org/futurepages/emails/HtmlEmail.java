package org.futurepages.emails;


import org.futurepages.util.Is;
import org.futurepages.util.RandomStringUtils;
import org.futurepages.exceptions.EmailException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import org.futurepages.core.config.Apps;
import org.futurepages.util.The;
import org.futurepages.util.html.HtmlStripper;

// Revision: 191951

/**
 * An HTML multipart email.
 *
 * <p>This class is used to send HTML formatted email.  A text message
 * can also be set for HTML unaware email clients, such as text-based
 * email clients.
 *
 * <p>This class also inherits from MultiPartEmail, so it is easy to
 * add attachents to the email.
 *
 * <p>To send an email in HTML, one should create a HtmlEmail, then
 * use the setFrom, addTo, etc. methods.  The HTML content can be set
 * with the setHtmlMsg method.  The alternate text content can be set
 * with setTextMsg.
 *
 * <p>Either the text or HTML can be omitted, in which case the "main"
 * part of the multipart becomes whichever is supplied rather than a
 * multipart/alternative.
 *
 * @author <a href="mailto:unknown">Regis Koenig</a>
 * @author <a href="mailto:sean@informage.net">Sean Legassick</a>
 * @version $Id: HtmlEmail.java,v 1.1 2006/03/02 17:11:41 soliveira Exp $
 */
public class HtmlEmail extends MultiPartEmail {

    /** Defintion of the length of generated CID's */
    public static final int CID_LENGTH = 10;

    /**
     * Text part of the message.  This will be used as alternative text if
     * the email client does not support HTML messages.
     */
    protected String text;

    /** Html part of the message */
    protected String html;

    /** Embeded images */
    protected List<MimeBodyPart> inlineImages = new ArrayList<MimeBodyPart>();
    
    public HtmlEmail() {
        super();
    }

    /**
     * @return List<InternetAddress> the list of destinataries or null if not exist
     */
    public List<InternetAddress> getToList(){
    	return new ArrayList<InternetAddress>(super.toList);
    }
    
    /**
     * @return List<InternetAddress> the list of cc or null if not exist
     */
    public List<InternetAddress> getCcList(){
    	return new ArrayList<InternetAddress>(super.ccList);
    }
    
    /**
     * @return List<InternetAddress> the list of bcc or null if not exist
     */
    public List<InternetAddress> getBccList(){
    	return new ArrayList<InternetAddress>(super.bccList);
    }
    
    /**
     * @return List<InternetAddress> the list of reply or null if not exist
     */
    public List<InternetAddress> getReplyList(){
    	return new ArrayList<InternetAddress>(super.replyList);
    }
    
    
    /**
     * Set the text content.
     *
     * @param aText A String.
     * @return An HtmlEmail.
     * @throws EmailException see javax.mail.internet.MimeBodyPart
     *  for defintions
     */
    public HtmlEmail setTextMsg(String aText) throws EmailException {
        if (Is.empty(aText)) {
            throw new EmailException("Invalid message supplied");
        }

        this.text = aText;
        return this;
    }

    /**
     * Set the HTML content.
     *
     * @param aHtml A String.
     * @return An HtmlEmail.
     * @throws EmailException see javax.mail.internet.MimeBodyPart
     *  for defintions
     */
    public HtmlEmail setHtmlMsg(String aHtml) throws EmailException {
        if (Is.empty(aHtml)) {
            throw new EmailException("Invalid message supplied");
        }

        this.html = aHtml;
        return this;
    }

    /**
     * Set the message.
     *
     * <p>This method overrides the MultiPartEmail setMsg() method in
     * order to send an HTML message instead of a full text message in
     * the mail body. The message is formatted in HTML for the HTML
     * part of the message, it is let as is in the alternate text
     * part.
     *
     * @param msg A String.
     * @return An Email.
     * @throws EmailException see javax.mail.internet.MimeBodyPart
     *  for defintions
     */
	@Override
    public Email setMsg(String msg) throws EmailException {
        if (Is.empty(msg)) {
            throw new EmailException("Invalid message supplied");
        }
		setTextMsg(new HtmlStripper(msg).text());
        setHtmlMsg(The.concat("<html><body>",msg,"</body></html>"));
		return this;
    }

    /**
     * Embeds an URL in the HTML.
     *
     * <p>This method allows to embed a file located by an URL into
     * the mail body.  It allows, for instance, to add inline images
     * to the email.  Inline files may be referenced with a
     * <code>cid:xxxxxx</code> URL, where xxxxxx is the Content-ID
     * returned by the embed function.
     *
     * <p>Example of use:<br><code><pre>
     * HtmlEmail he = new HtmlEmail();
     * he.setHtmlMsg("&lt;html&gt;&lt;img src=cid:" +
     *  embed("file:/my/image.gif","image.gif") +
     *  "&gt;&lt;/html&gt;");
     * // code to set the others email fields (not shown)
     * </pre></code>
     *
     * @param url The URL of the file.
     * @param name The name that will be set in the filename header
     * field.
     * @return A String with the Content-ID of the file.
     * @throws EmailException when URL suplpied is invalid
     *  also see javax.mail.internet.MimeBodyPart for defintions
     */
    public String embed(URL url, String name) throws EmailException {
        // verify that the URL is valid
        try {
            InputStream is = url.openStream();

            is.close();
        } catch (IOException e) {
            throw new EmailException("Invalid URL");
        }

        MimeBodyPart mbp = new MimeBodyPart();

        try {
            mbp.setDataHandler(new DataHandler(new URLDataSource(url)));
            mbp.setFileName(name);
            mbp.setDisposition("inline");
            String cid = RandomStringUtils.randomAlphabetic(HtmlEmail.CID_LENGTH).toLowerCase();

            mbp.addHeader("Content-ID", "<" + cid + ">");
            this.inlineImages.add(mbp);
            return cid;
        } catch (MessagingException me) {
            throw new EmailException(me);
        }
    }

    /**
     * Does the work of actually sending the email.
     *
     * @exception EmailException if there was an error.
     */
	@Override
    public void send() throws EmailException {
		if (Apps.get("EMAIL_ACTIVE").equals("true")) {
			try {
				// if the email has attachments then the base type is mixed,
				// otherwise it should be related
				if (this.isBoolHasAttachments()) {
					this.buildAttachments();
				} else {
					this.buildNoAttachments();
				}

			} catch (MessagingException me) {
				throw new EmailException(me);
			}
			super.send();
		} else {
			System.out.println("### EMAIL_ACTIVE = FALSE #########################################");
			System.out.println("... email not sent, see its content below.........................");
			for(InternetAddress ia : this.toList){
				System.out.print("TO: <"+ia.getAddress()+"> ");
			}
			for(InternetAddress ia : this.ccList){
				System.out.print("CC: <"+ia.getAddress()+"> ");
			}
			for(InternetAddress ia : this.bccList){
				System.out.print("BCC: <"+ia.getAddress()+"> ");
			}
			System.out.println();
			System.out.println("Subject: "+this.subject);
			System.out.println();
			System.out.println("<HTML MSG>---------------------------------------------------------");
			System.out.println(this.html);
//			System.out.println("<TEXT MSG>---------------------------------------------------------");
//			System.out.println(this.text);
			System.out.println("##################################################################");
		}
    }

    /**
     * @throws EmailException EmailException
     * @throws MessagingException MessagingException
     */
    private void buildAttachments() throws MessagingException, EmailException {
        MimeMultipart container = this.getContainer();
        MimeMultipart subContainer = null;
        MimeMultipart subContainerHTML = new MimeMultipart("related");
        BodyPart msgHtml = null;
        BodyPart msgText = null;

        container.setSubType("mixed");
        subContainer = new MimeMultipart("alternative");

        if (!Is.empty(this.text)) {
            msgText = new MimeBodyPart();
            subContainer.addBodyPart(msgText);

            if (!Is.empty(this.charset)) {
                msgText.setContent(
                    this.text,
                    Email.TEXT_PLAIN + "; charset=" + this.charset);
            } else {
                msgText.setContent(this.text, Email.TEXT_PLAIN);
            }
        }

        if (!Is.empty(this.html)) {
            if (this.inlineImages.size() > 0) {
                msgHtml = new MimeBodyPart();
                subContainerHTML.addBodyPart(msgHtml);
            } else {
                msgHtml = new MimeBodyPart();
                subContainer.addBodyPart(msgHtml);
            }

            if (!Is.empty(this.charset)) {
                msgHtml.setContent(
                    this.html,
                    Email.TEXT_HTML + "; charset=" + this.charset);
            } else {
                msgHtml.setContent(this.html, Email.TEXT_HTML);
            }

            Iterator<MimeBodyPart> iter = this.inlineImages.iterator();

            while (iter.hasNext()) {
                subContainerHTML.addBodyPart(iter.next());
            }
        }

        // add sub containers to message
        this.addPart(subContainer, 0);

        if (this.inlineImages.size() > 0) {
            // add sub container to message
            this.addPart(subContainerHTML, 1);
        }
    }

    /**
     * @throws EmailException EmailException
     * @throws MessagingException MessagingException
     */
    private void buildNoAttachments() throws MessagingException, EmailException {
        MimeMultipart container = this.getContainer();
        MimeMultipart subContainerHTML = new MimeMultipart("related");

        container.setSubType("alternative");

        BodyPart msgText = null;
        BodyPart msgHtml = null;

        if (!Is.empty(this.text)) {
            msgText = this.getPrimaryBodyPart();
            if (!Is.empty(this.charset)) {
                msgText.setContent(
                    this.text,
                    Email.TEXT_PLAIN + "; charset=" + this.charset);
            } else {
                msgText.setContent(this.text, Email.TEXT_PLAIN);
            }
        }

        if (!Is.empty(this.html)) {
            // if the txt part of the message was null, then the html part
            // will become the primary body part
            if (msgText == null) {
                msgHtml = getPrimaryBodyPart();
            } else {
                if (this.inlineImages.size() > 0) {
                    msgHtml = new MimeBodyPart();
                    subContainerHTML.addBodyPart(msgHtml);
                } else {
                    msgHtml = new MimeBodyPart();
                    container.addBodyPart(msgHtml, 1);
                }
            }

            if (!Is.empty(this.charset)) {
                msgHtml.setContent(
                    this.html,
                    Email.TEXT_HTML + "; charset=" + this.charset);
            } else {
                msgHtml.setContent(this.html, Email.TEXT_HTML);
            }

            Iterator<MimeBodyPart> iter = this.inlineImages.iterator();

            while (iter.hasNext()) {
                subContainerHTML.addBodyPart(iter.next());
            }

            if (this.inlineImages.size() > 0) {
                // add sub container to message
                this.addPart(subContainerHTML);
            }
        }
    }
}