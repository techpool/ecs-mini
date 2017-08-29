package com.pratilipi.api.impl.contact;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.internet.InternetAddress;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.util.EmailUtil;


@SuppressWarnings("serial")
@Bind( uri= "/contact/email" )
public class ConversationEmailApi extends GenericApi {
	
	public static class PostRequest extends GenericRequest {
		
		private String body;
		private String subject;
		private ArrayList<String> receivers;
		private ArrayList<String> cc;

		
		public String getBody() {
			return body;
		}
		
		public String getSubject() {
			return subject;
		}
		
		public ArrayList<String> getReceievers() {
			return receivers;
		}
		
		public ArrayList<String> getCc() {
			return cc;
		}
	}

	
	@Post
	public GenericResponse post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {
		
		try {
			// CONVERTING LIST TO ARRAY
			InternetAddress[] receivers = createInternetAddressArray(request.getReceievers());
			InternetAddress[] ccs = createInternetAddressArray(request.getCc());
			
			EmailUtil.sendMail(
					"Team Pratilipi", 
					"contact@pratilipi.com", 
					receivers, 
					ccs, 
					request.getSubject(), 
					request.getBody()
			);
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger(ConversationEmailApi.class.getSimpleName())
					.log(Level.SEVERE, "Error while create internetaddess array for conversation email");
			e.printStackTrace();
		}
		
		return new GenericResponse();
		
	}
	
	private InternetAddress[] createInternetAddressArray(List<String> list) throws UnsupportedEncodingException {
		InternetAddress[] array = new InternetAddress[list.size()];
		for(int i = 0; i < list.size(); ++i)
				array[i] = new InternetAddress(list.get(i), list.get(i));
		return array;
	}

}
