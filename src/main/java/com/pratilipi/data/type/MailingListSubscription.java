package com.pratilipi.data.type;

import java.util.Date;

import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.MailingList;

public interface MailingListSubscription extends GenericOfyType {

	MailingList getMailingList();
	
	void setMailingList( MailingList mailingList );
	
	Long getUserId();

	void setUserId( Long userId );

	String getEmail();

	void setEmail( String email );

	String getPhone();

	void setPhone( String phone );

	Language getLanguage();

	void setLanguage( Language language );
	
	String getComment();

	void setComment( String comment );

	Date getSubscriptionDate();
	
	void setSubscriptionDate( Date date );
	
}
