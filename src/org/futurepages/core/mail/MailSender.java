package org.futurepages.core.mail;

import org.futurepages.core.config.Apps;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.emails.Email;
import org.futurepages.emails.HtmlEmail;
import org.futurepages.emails.SimpleEmail;
import org.futurepages.exceptions.EmailException;
import org.futurepages.util.Is;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MailSender {

	private static MailSender instance = new MailSender();

	private MailSender() {}



	private SimpleEmail newSimpleEmail(String mail, String subject, String message) throws EmailException {
		SimpleEmail email = new SimpleEmail();
		email.addTo(mail);
		email.setSubject(subject);
		email.setMsg(message);
		email.setSentDate(new Date());
		return email;
	}

	private HtmlEmail newHtmlEmail(String mail, String subject, String message) throws EmailException {
		HtmlEmail email = new HtmlEmail();
		email.addTo(mail);
		email.setSubject(subject);
		email.setMsg(message);
		email.setSentDate(new Date());
		return email;
	}

	private List<Email> criarListaEmails(String[] mailAdresses, String subject, String message, TypeEmail typeEmail) throws EmailException {
		List< Email> emails = new ArrayList<Email>();
		for (String emailAdr : mailAdresses) {
			Email email = newEmail(typeEmail,emailAdr, subject,message);
			emails.add(email);
		}
		return emails;
	}

	private List<Email> criarListaEmailsWithFrom(String nameFrom, String emailReplyTo,String emailAccountFrom, String emailPassword, String[] mailAdresses, String subject, String message, TypeEmail typeEmail) throws EmailException {
		List<Email> emails = new ArrayList<Email>();
		for (String emailAdr : mailAdresses) {
			Email email = newEmail(typeEmail,emailAdr, subject,message);
			if(!Is.empty(emailAccountFrom)){
				email.setAuthentication(emailAccountFrom,!Is.empty(emailPassword)?emailPassword: Apps.get("EMAIL_USER_PASSWORD"));
			}
			if(nameFrom.length()>60){
				nameFrom = nameFrom.substring(0,60)+"...";
			}
			email.setFrom(Apps.get("EMAIL_FROM"), nameFrom);
			if(!Is.empty(emailReplyTo)){
				email.addReplyTo(emailReplyTo,nameFrom);
			}
			emails.add(email);
		}
		return emails;
	}

	private enum TypeEmail {
		SIMPLE_EMAIL,
		HTML_EMAIL;
	}

	public static MailSender getInstance() {
		return instance;
	}

	/*
	 * Envia o e-mail tempo de execução da sessão.
	 * O usuário aguarda a excução para receber uma
	 * resposta do servidor.
	 */
	public void sendHtmlEmailNow(String subject, String message, String... mailAdresses) throws EmailException {
		for (String mail : mailAdresses) {
			HtmlEmail email = newHtmlEmail(mail, subject, message);
			send(email);
		}
	}

	/*
	 * Envia o e-mail tempo de execução da sessão.
	 * O usuário aguarda a excução para receber uma
	 * resposta do servidor.
	 */
	public void sendHtmlEmailNowWithFrom(String nameFrom, String emailReplyTo, String emailAccountFrom, String emailPassword, String subject, String message, String... mailAdresses) throws EmailException {
		for (String mail : mailAdresses) {
			HtmlEmail email = newHtmlEmail(mail, subject, message);
			if(!Is.empty(emailAccountFrom)){
				email.setAuthentication(emailAccountFrom,!Is.empty(emailPassword)?emailPassword: Apps.get("EMAIL_USER_PASSWORD"));
			}
			if(nameFrom.length()>60){
				nameFrom = nameFrom.substring(0,60)+"...";
			}
			email.setFrom(Apps.get("EMAIL_FROM"), nameFrom);
			if(!Is.empty(emailReplyTo)){
				email.addReplyTo(emailReplyTo,nameFrom);
			}
			send(email);
		}
	}

	/*
	 * Envia o e-mail tempo de execução da sessão.
	 * O usuário aguarda a excução para receber uma
	 * resposta do servidor.
	 */
	public void sendSimpleEmailNow(String subject, String message, String... mailAdresses) throws EmailException {
		for (String mail : mailAdresses) {
			SimpleEmail email = newSimpleEmail(mail, subject, message);
			send(email);
		}
	}

	/*
	 * O e-mail é enviado concorrentemente à sessão.
	 * O usuário não espera esta execução para receber
	 * uma resposta do servidor.
	 */
	public void sendHtmlEmail(String subject, String message, String... mailAdresses) {

	try {
			List<Email> emails = criarListaEmails(mailAdresses, subject, message, TypeEmail.HTML_EMAIL);
			ThreadEmail threadEmail = new ThreadEmail(emails);
			Thread thread = new Thread(threadEmail);
			thread.start();
		} catch (EmailException ex) {
			AppLogger.getInstance().execute(ex);
		}

	}

	public void sendHtmlEmailWithFrom(String nameFrom, String emailReplayTo, String emailAccountFrom, String emailPassword, String subject, String message, String... mailAdresses) {

	try {
			List<Email> emails = criarListaEmailsWithFrom(nameFrom, emailReplayTo, emailAccountFrom, emailPassword, mailAdresses, subject, message, TypeEmail.HTML_EMAIL);
			ThreadEmail threadEmail = new ThreadEmail(emails);
			Thread thread = new Thread(threadEmail);
			thread.start();
		} catch (EmailException ex) {
			AppLogger.getInstance().execute(ex);
		}

	}

	/*
	 * O e-mail é enviado concorrentemente à sessão.
	 * O usuário não espera esta execução para receber
	 * uma resposta do servidor.
	 */
	public void sendSimpleEmail(String subject, String message, String... mailAdresses) {

		try {
			List<Email> emails = criarListaEmails(mailAdresses, subject, message, TypeEmail.SIMPLE_EMAIL);
			ThreadEmail threadEmail = new ThreadEmail(emails);
			Thread thread = new Thread(threadEmail);
			thread.start();
		} catch (EmailException ex) {
			AppLogger.getInstance().execute(ex);
		}
	}

	/*
	 * Classe interna à classe EnviadorDeEmails que
	 * usada para enviar e-mail por thread.
	 */
	private class ThreadEmail implements Runnable {

		private Email email;
		
		List< Email> emails ;

		public ThreadEmail(Email email) {
			this.email = email;
		}
		
		public ThreadEmail(List< Email> emails) {
			this.emails = emails;
		}

	
		public ThreadEmail(TypeEmail typeEmail, String subject, String message, String mailAdresses) throws EmailException {
			this.email = newEmail(typeEmail, mailAdresses, subject, message);
		}
		

		@Override
		public void run() {
			
			if (this.email!= null){
				send(this.email);
			}
			
			if (emails != null && emails.size()>0){
				for (Email email : emails) {
					send(email);
				}
			}	
			
		}

	}

	Email newEmail(TypeEmail typeEmail, String mailAdresses, String subject, String message) throws EmailException {
			Email email = (typeEmail == TypeEmail.SIMPLE_EMAIL ? newSimpleEmail(mailAdresses, subject, message) : newHtmlEmail(mailAdresses, subject, message));
			return email;
	}
	
	private void send(Email email){
			try {
				
				email.send();

			} catch (EmailException ex) {
				AppLogger.getInstance().execute(ex);
			}
	}

	public static void main(String[] args) throws Exception {
		MailConfig.initialize();
		HtmlEmail email = new HtmlEmail();
		try {
			email.addTo("person@gmail.com");
			email.setSubject("testing subject");
			email.setMsg("hello <strong>big world!</strong><br/><br/><br/>Good by Fiancé!");
			email.setSentDate(new Date());
			email.send();
		} catch (EmailException ex) {
			AppLogger.getInstance().execute(ex);
		}

	}
}
