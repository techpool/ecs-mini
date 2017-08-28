package com.pratilipi.data.util;

import java.util.Date;
import java.util.List;

import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.type.AccessType;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.MailingList;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.AuditLog;
import com.pratilipi.data.type.MailingListSubscription;
import com.pratilipi.filter.AccessTokenFilter;

public class MailingListSubscriptionDataUtil {
	
	public static void subscribe( MailingList mailingList, Long userId,
			String email, String phone, Language language, String comment )
			throws InvalidArgumentException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		MailingListSubscription mailingListSubscription = null;
		
		if( email != null ) {
			
			List<MailingListSubscription> mailingListSubscriptionList = dataAccessor.getMailingListSubscriptionList( mailingList, email, null );
			for( MailingListSubscription subs : mailingListSubscriptionList ) {
				if( language != null && subs.getLanguage() != null && language != subs.getLanguage() )
					continue;
				if( phone != null && subs.getPhone() != null && ! phone.equals( subs.getPhone() ) )
					continue;
				if( comment != null && subs.getComment() != null && ! comment.equals( subs.getComment() ) )
					continue;
				mailingListSubscription = subs;
				break;
			}
		
		} else if( phone != null ) {
		
			List<MailingListSubscription> mailingListSubscriptionList = dataAccessor.getMailingListSubscriptionList( mailingList, null, phone );
			for( MailingListSubscription subs : mailingListSubscriptionList ) {
				if( language != null && subs.getLanguage() != null && language != subs.getLanguage() )
					continue;
				if( comment != null && subs.getComment() != null && ! comment.equals( subs.getComment() ) )
					continue;
				mailingListSubscription = subs;
				break;
			}
			
		} else {
			
			throw new InvalidArgumentException( "Either email or phone must be provided." );

		}
		
		
		if( mailingListSubscription == null )
			mailingListSubscription = dataAccessor.newMailingListSubscription();
		
		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				AccessType.MAILING_LIST_SUBSCRIPTION_ADD,
				mailingListSubscription );
		
		
		boolean bool = true;

		if( mailingListSubscription.getMailingList() == null ) // New entry
			mailingListSubscription.setMailingList( mailingList );
		
		if( userId != null && mailingListSubscription.getUserId() == null )
			mailingListSubscription.setUserId( userId );
		
		if( email != null && mailingListSubscription.getEmail() == null ) {
			mailingListSubscription.setEmail( email );
			bool = false;
		}
		
		if( phone != null && mailingListSubscription.getPhone() == null ) {
			mailingListSubscription.setPhone( phone );
			bool = false;
		}
		
		if( language != null && mailingListSubscription.getLanguage() == null ) {
			mailingListSubscription.setLanguage( language );
			bool = false;
		}
		
		if( comment != null && mailingListSubscription.getComment() == null ) {
			mailingListSubscription.setComment( comment );
			bool = false;
		}
		
		if( mailingListSubscription.getSubscriptionDate() == null ) // New entry
			mailingListSubscription.setSubscriptionDate( new Date() );
		
		if( bool )
			throw new InvalidArgumentException( GenericRequest.ERR_MAILING_LIST_SUBSCRIBED_ALREDY );

		
		dataAccessor.createOrUpdateMailingListSubscription( mailingListSubscription, auditLog );

	}

}