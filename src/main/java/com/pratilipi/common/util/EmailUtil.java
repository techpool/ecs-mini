package com.pratilipi.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.pratilipi.common.exception.UnexpectedServerException;


public class EmailUtil {

	private static final Logger logger = Logger.getLogger( EmailUtil.class.getName() );

	private final static Properties properties = new Properties();
	private final static Session session = Session.getDefaultInstance( properties, null );

	
	public static void sendMail(
			String senderName, String senderEmail,
			String recipientName, String recipientEmail,
			String subject, String body ) throws UnexpectedServerException {

		try {
			Message msg = new MimeMessage( session );
			msg.setFrom( new InternetAddress( senderEmail, senderName ) );
			msg.addRecipient( Message.RecipientType.TO, new InternetAddress( recipientEmail, MimeUtility.encodeText( recipientName, "UTF-8", "B" ) ) );
			// TODO: Uncomment when we migrate to other email service providers
//			msg.addRecipient( Message.RecipientType.BCC, new InternetAddress( "mail-archive@pratilipi.com", "Mail Archive" ) );
			msg.setSubject(  MimeUtility.encodeText( subject, "UTF-8", "B" )  );
			msg.setContent( body, "text/html" );
			Transport.send( msg );
			logger.log( Level.INFO, "Successfully sent mail to " + recipientEmail + "." );
		} catch ( UnsupportedEncodingException | MessagingException e ) {
			logger.log( Level.SEVERE, "Failed to send mail to " + recipientEmail + ".", e );
			throw new UnexpectedServerException();
		}

	}
	
	
	public static void sendMail(
			String senderName, String senderEmail,
			InternetAddress[] recipients, InternetAddress[] cc,
			String subject, String body ) throws UnexpectedServerException {
		try {
			Message msg = new MimeMessage( session );
			msg.setFrom( new InternetAddress( senderEmail, senderName ) );
			msg.addRecipients( Message.RecipientType.TO, recipients);
			msg.addRecipients( Message.RecipientType.CC, cc);
			// TODO: Uncomment when we migrate to other email service providers
//			msg.addRecipient( Message.RecipientType.BCC, new InternetAddress( "mail-archive@pratilipi.com", "Mail Archive" ) );
			msg.setSubject( subject );
			msg.setContent( body, "text/html; charset=UTF-8" );
			logger.log(Level.INFO, "Sending mail");
			Transport.send( msg );
		} catch ( UnsupportedEncodingException | MessagingException e ) {
//			logger.log( Level.SEVERE, "Failed to send mail to " + recipientEmail + ".", e );
			throw new UnexpectedServerException();
		}
	}

}
