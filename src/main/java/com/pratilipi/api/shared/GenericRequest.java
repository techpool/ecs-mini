package com.pratilipi.api.shared;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.common.exception.UnexpectedServerException;

public class GenericRequest {

	private static final Logger logger =
			Logger.getLogger( GenericRequest.class.getName() );


	protected static final String REGEX_NUMBER = "[0-9]+";
	protected static final String REGEX_PASSWORD = "\\S{6,128}";
	protected static final String REGEX_URI = "(/[A-Za-z0-9-]+)+";
	protected static final String REGEX_EMAIL = "[A-Za-z0-9]+([._+-][A-Za-z0-9]+)*@[A-Za-z0-9]+([.-][A-Za-z0-9]+)*\\.[A-Za-z]{2,4}";
	protected static final String REGEX_PHONE = "[0-9]{10}";

	public static final String ERR_INSUFFICIENT_ARGS = "One or more required arguments are missing.";
	
	public static final String ERR_NAME_REQUIRED = "Please enter your name.";
	
	public static final String ERR_USER_ID_REQUIRED	= "User id is required.";
	public static final String ERR_USER_ID_INVALID	= "Invalid user id.";
	
	public static final String ERR_EMAIL_REQUIRED			= "Email is required.";
	public static final String ERR_EMAIL_INVALID			= "Invalid email.";
	public static final String ERR_PHONE_REQUIRED			= "Phone is required.";
	public static final String ERR_PHONE_INVALID			= "Invalid phone number.";
	public static final String ERR_EMAIL_NOT_REGISTERED		= "Email not registered.";
	public static final String ERR_EMAIL_REGISTERED_WITH_FACEBOOK = "You have registered with us via Facebook. Kindly login with Facebook.";
	public static final String ERR_EMAIL_REGISTERED_WITH_GOOGLE = "You have registered with us via Google. Kindly login with Google.";
	public static final String ERR_EMAIL_REGISTERED_ALREADY = "Email is already registered.";
	
	public static final String ERR_PASSWORD_REQUIRED  = "Enter password.";
	public static final String ERR_PASSWORD_INVALID	  = "Password must contain at least 6 characters, no spaces.";
	public static final String ERR_PASSWORD_INCORRECT = "Incorrect password !";

	public static final String ERR_PASSWORD2_REQUIRED = "Re-enter password.";
	public static final String ERR_PASSWORD2_MISMATCH = "Must be same as password.";

	public static final String ERR_ACCOUNT_BLOCKED	   = "Your account is blocked ! Kindly mail us at contact@pratilipi.com ."; 
	public static final String ERR_INVALID_CREDENTIALS = "Invalid Credentials!";
	
	public static final String ERR_VERIFICATION_TOKEN_INVALID_OR_EXPIRED = "Verification token is invalid or expired.";

	public static final String ERR_LANGUAGE_REQUIRED = "Language is required.";

	public static final String ERR_PRATILIPI_ID_REQUIRED			= "pratilipiId is required.";
	public static final String ERR_PRATILIPI_TYPE_REQUIRED			= "Type is required.";
	public static final String ERR_PRATILIPI_CONTENT_TYPE_REQUIRED	= "Content type is required.";
	public static final String ERR_PRATILIPI_STATE_REQUIRED			= "State is required.";
	public static final String ERR_PRATILIPI_STATE_INVALID			= "Invalid State.";

	public static final String ERR_PRATILIPI_CHAPTER_NO_REQUIRED			= "chapterNo is required.";
	public static final String ERR_PRATILIPI_PAGE_NO_REQUIRED				= "pageNo is required.";

	public static final String ERR_AUTHOR_ID_REQUIRED	 = "Author id is required.";
	public static final String ERR_AUTHOR_STATE_REQUIRED = "State is required.";
	public static final String ERR_AUTHOR_STATE_INVALID	 = "Invalid State.";

	public static final String ERR_BLOG_ID_REQUIRED			 = "Blog id is required.";
	
	public static final String ERR_BLOG_POST_STATE_REQUIRED = "State is required.";

	public static final String ERR_COMMENT_PARENT_TYPE_REQUIRED	= "Parent type is required.";
	public static final String ERR_COMMENT_PARENT_ID_REQUIRED	= "Parent id is required.";
	public static final String ERR_COMMENT_CONTENT_REQUIRED		= "Content is required.";
	
	public static final String ERR_MAILING_LIST_SUBSCRIBED_ALREDY = "Email/Phone already subscribed to this mailing list.";

	
	public GenericRequest() {}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final JsonObject validate() throws UnexpectedServerException {
		JsonObject errorMessages = new JsonObject();
		for( Field field : this.getClass().getDeclaredFields() ) {
			Validate validate = field.getAnnotation( Validate.class );
			if( validate != null ) {
				
				field.setAccessible( true );
				try {

					Object value = field.get( this );
					if( value == null || ( field.getType() == String.class && ( (String) value ).trim().isEmpty() ) ) {

						if( validate.required() ) {
							errorMessages.addProperty( field.getName(), validate.requiredErrMsg().isEmpty()
									? "'" + field.getName() + "' is missing."
									: validate.requiredErrMsg() );
						}
					
					} else if( value != null ) {
						
						if( field.getType() == String.class ) {
							if( ! validate.regEx().isEmpty() && ! ( (String) value ).matches( validate.regEx() ) )
								errorMessages.addProperty( field.getName(), validate.regExErrMsg().isEmpty()
										? "'" + field.getName() + "' value is invalid."
										: validate.regExErrMsg() );
	
						} else if( field.getType() == Integer.class ) {
							if( (Integer) value < validate.minInt() )
								errorMessages.addProperty( field.getName(), "'" + field.getName() + "' must be greater than or equal to " + validate.minInt() );
						
						} else if( field.getType() == Long.class ) {
							if( (Long) value < validate.minLong() )
								errorMessages.addProperty( field.getName(), "'" + field.getName() + "' must be greater than or equal to " + validate.minLong() );
						
						} else if( field.getType() == List.class && GenericRequest.class.isAssignableFrom( (Class) ( (ParameterizedType) field.getGenericType() ).getActualTypeArguments()[0] ) ) {
							JsonArray errorMessagesArray = new JsonArray();
							for( GenericRequest request : (List<GenericRequest>) value ) {
								JsonObject nestedErrorMessages = request.validate();
								if( nestedErrorMessages.entrySet().size() > 0 )
									errorMessagesArray.add( nestedErrorMessages );
							}
							if( errorMessagesArray.size() > 0 )
								errorMessages.add( field.getName(), errorMessagesArray );
						}
						
					}
					
				} catch( IllegalAccessException e ) {
					logger.log( Level.SEVERE, "Unexpected exception occured !", e );
					throw new UnexpectedServerException();
				}
				
			}
		}
		return errorMessages;
	}

}
