package com.pratilipi.data.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.AccessType;
import com.pratilipi.common.type.EmailState;
import com.pratilipi.common.type.EmailType;
import com.pratilipi.common.type.I18nGroup;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.util.EmailUtil;
import com.pratilipi.common.util.FreeMarkerUtil;
import com.pratilipi.common.util.HtmlUtil;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DataListIterator;
import com.pratilipi.data.client.AuthorData;
import com.pratilipi.data.client.CommentData;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.client.UserPratilipiData;
import com.pratilipi.data.client.VoteData;
import com.pratilipi.data.type.AuditLog;
import com.pratilipi.data.type.Email;
import com.pratilipi.data.type.User;
import com.pratilipi.data.type.UserAuthor;
import com.pratilipi.data.type.Vote;
import com.pratilipi.email.EmailTemplateUtil;
import com.pratilipi.filter.AccessTokenFilter;


public class EmailDataUtil {

	@SuppressWarnings("unused")
	private static final Logger logger =
			Logger.getLogger( EmailDataUtil.class.getName() );

	@SuppressWarnings("deprecation")
	private static String _getDomainName( Language language ) {
		return "https://" + ( language == null ? Language.ENGLISH.getHostName() : language.getHostName() );
	}

	private static String _getDateFormat( Date date ) {
		DateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
		dateFormat.setTimeZone( TimeZone.getTimeZone( "IST" ) );
		return dateFormat.format( date );
	}

	private static boolean _isToday( Date date ) {
		Long time = new Date().getTime();
		time = time - time % TimeUnit.DAYS.toMillis( 1 ); // 00:00 AM GMT
		time = time - TimeUnit.MINUTES.toMillis( 330 ); // 00:00 AM IST
		return date.getTime() > time;
	}

	private static String _getContentSnippet( Email email ) throws UnexpectedServerException {
		return _getContentSnippet( email, null );
	}

	private static void _updateUserEntity( Long userId ) {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		User user = dataAccessor.getUser( userId );
		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				AccessType.USER_UPDATE,
				user );

		user.setLastEmailedDate( new Date() );
		user.setLastUpdated( new Date() );
		user = dataAccessor.createOrUpdateUser( user, auditLog );

	}


	@SuppressWarnings("unchecked")
	private static String _getContentSnippet( Email email, Language language ) throws UnexpectedServerException {

		Object[] emailData = null;

		if( email.getType() == EmailType.PRATILIPI_PUBLISHED_AUTHOR
				|| email.getType() == EmailType.PRATILIPI_PUBLISHED_FOLLOWER )
			emailData = _createDataModelForPratilipiPublishedEmail( email.getPrimaryContentIdLong() );

		else if( email.getType() == EmailType.AUTHOR_FOLLOW )
			emailData = _createDataModelForAuthorFollowEmail( email.getPrimaryContentId() );

		else if( email.getType() == EmailType.USER_PRATILIPI_RATING || email.getType() == EmailType.USER_PRATILIPI_REVIEW )
			emailData = _createDataModelForUserPratilipiEmail( email.getPrimaryContentId() );

		else if( email.getType() == EmailType.COMMENT_REVIEW_REVIEWER
					|| email.getType() == EmailType.COMMENT_REVIEW_AUTHOR )
			emailData = _createDataModelForCommentReviewEmail( email.getPrimaryContentIdLong() );

		else if( email.getType() == EmailType.VOTE_REVIEW_REVIEWER )
			emailData = _createDataModelForVoteReviewEmail( email.getPrimaryContentId() );

		else if( email.getType() == EmailType.VOTE_COMMENT_REVIEW_COMMENTOR )
			emailData = _createDataModelForVoteCommentEmail( email.getPrimaryContentId() );

		String template = EmailTemplateUtil.getEmailTemplate( email.getType(),
				language != null ? language : (Language) emailData[1] );

		return FreeMarkerUtil.processString( ( Map<String, Object> ) emailData[0], template );

	}

	private static void _sendConsolidatedEmail( Long userId, List<Email> emailList ) throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		UserData user = UserDataUtil.createUserData( dataAccessor.getUser( userId ) );
		// Dropping mails if user doesn't have name or language
		if( user.getDisplayName() == null || user.getLanguage() == null ) {
			for( Email email : emailList ) {
				email.setState( EmailState.DROPPED );
				email.setLastUpdated( new Date() );
			}
			emailList = dataAccessor.createOrUpdateEmailList( emailList );
			return;
		}

		String consolidatedContent = new String();

		for( Email email : emailList ) {

			if( email.getState() != EmailState.IN_PROGRESS )
				continue;

			if( ! _isToday( email.getScheduledDate() ) && email.getScheduledDate().before( new Date() ) ) {
				email.setState( EmailState.DROPPED );
				email.setLastUpdated( new Date() );
				continue;
			}

			consolidatedContent = consolidatedContent + _getContentSnippet( email, user.getLanguage() );

			email.setState( EmailState.SENT );
			email.setLastUpdated( new Date() );

		}

		if( consolidatedContent.isEmpty() )  { // No IN_PROGRESS emails
			dataAccessor.createOrUpdateEmailList( emailList );
			return;
		}


		String content = EmailTemplateUtil.getEmailBody( consolidatedContent, user.getLanguage() );

		Pattern senderNamePattern = Pattern.compile( "<!-- SENDER_NAME:(.+?) -->" );
		Pattern senderEmailPattern = Pattern.compile( "<!-- SENDER_EMAIL:(.+?) -->" );

		String senderName = null;
		String senderEmail = null;
		String subject = dataAccessor.getI18nStrings( I18nGroup.EMAIL, user.getLanguage() ).get( "email_consolidated_subject" );

		Matcher m = null;
		if( ( m = senderNamePattern.matcher( content ) ).find() )
			senderName = m.group( 1 ).trim();
		if( ( m = senderEmailPattern.matcher( content ) ).find() )
			senderEmail = m.group( 1 ).trim();

		EmailUtil.sendMail( senderName, senderEmail,
				user.getDisplayName(), user.getEmail(),
				subject,
				content );

		dataAccessor.createOrUpdateEmailList( emailList );

		_updateUserEntity( userId );

	}

	public static void sendEmail( Long emailId ) throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Email email = dataAccessor.getEmail( emailId );

		if( email.getState() != EmailState.IN_PROGRESS )
			return;

		UserData user = UserDataUtil.createUserData( dataAccessor.getUser( email.getUserId() ) );
		if( user.getDisplayName() == null || user.getLanguage() == null
				|| ( ! _isToday( email.getScheduledDate() ) && email.getScheduledDate().before( new Date() ) ) ) {
			email.setState( EmailState.DROPPED );
			email.setLastUpdated( new Date() );
			email = dataAccessor.createOrUpdateEmail( email );
			return;
		}

		String content = _getContentSnippet( email, user.getLanguage() );

		Pattern senderNamePattern = Pattern.compile( "<!-- SENDER_NAME:(.+?) -->" );
		Pattern senderEmailPattern = Pattern.compile( "<!-- SENDER_EMAIL:(.+?) -->" );
		Pattern subjectPattern = Pattern.compile( "<div subject .+?>(.+?)<\\/div>" );

		String senderName = null;
		String senderEmail = null;
		String subject = null;

		Matcher m = null;
		if( ( m = senderNamePattern.matcher( content ) ).find() )
			senderName = m.group( 1 ).trim();
		if( ( m = senderEmailPattern.matcher( content ) ).find() )
			senderEmail = m.group( 1 ).trim();
		if( ( m = subjectPattern.matcher( content ) ).find() )
			subject = m.group( 1 ).trim();

		EmailUtil.sendMail( senderName, senderEmail,
				user.getDisplayName(), user.getEmail(),
				subject,
				EmailTemplateUtil.getEmailBody( content,
									user.getLanguage() != null ? user.getLanguage() : Language.ENGLISH ) );

		email.setState( EmailState.SENT );
		email.setLastUpdated( new Date() );
		dataAccessor.createOrUpdateEmail( email );

		_updateUserEntity( email.getUserId() );

	}

	public static void sendEmailsToUser( Long userId ) throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		DataListIterator<Email> it = dataAccessor.getEmailListIteratorForStatePending( userId, true );
		List<Email> emailList = new ArrayList<>();
		while( it.hasNext() )
			emailList.add( it.next() );

		_sendConsolidatedEmail( userId, emailList );

	}


	private static Object[] _createDataModelForPratilipiPublishedEmail( Long pratilipiId )
			throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		PratilipiData pratilipi = PratilipiDataUtil.createPratilipiData( dataAccessor.getPratilipi( pratilipiId ) );

		Map<String, Object> dataModel = new HashMap<>();
		dataModel.put( "pratilipi_title", pratilipi.getTitle() != null ? pratilipi.getTitle() : pratilipi.getTitleEn() );
		dataModel.put( "pratilipi_cover_image_url", pratilipi.getCoverImageUrl( 150 ) );
		dataModel.put( "pratilipi_listing_date", _getDateFormat( pratilipi.getListingDate() ) );
		dataModel.put( "pratilipi_summary", HtmlUtil.truncateText( pratilipi.getSummary(), 250 ) );
		dataModel.put( "pratilipi_page_url", _getDomainName( pratilipi.getLanguage() ) + pratilipi.getPageUrl() );
		dataModel.put( "author_name", pratilipi.getAuthor().getName() != null ? pratilipi.getAuthor().getName() : pratilipi.getAuthor().getNameEn() );
		dataModel.put( "author_page_url", _getDomainName( pratilipi.getLanguage() ) + pratilipi.getAuthor().getPageUrl() );

		return new Object[] { dataModel, pratilipi.getLanguage() };

	}

	private static Object[] _createDataModelForAuthorFollowEmail( String userAuthorId )
			throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		UserAuthor userAuthor = dataAccessor.getUserAuthor( userAuthorId );
		UserData user = UserDataUtil.createUserData( dataAccessor.getUser( userAuthor.getUserId() ) );
		AuthorData follower = AuthorDataUtil.createAuthorData( dataAccessor.getAuthorByUserId( userAuthor.getUserId() ) );

		Map<String, Object> dataModel = new HashMap<>();
		dataModel.put( "follower_name", follower.getName() != null ?  follower.getName() : follower.getNameEn() );
		dataModel.put( "follower_page_url", _getDomainName( follower.getLanguage() ) + follower.getPageUrl() );
		dataModel.put( "follower_profile_image_url", follower.getProfileImageUrl( 50 ) );

		if( follower.getFollowCount() > 0 )
			dataModel.put( "follower_followers_count", follower.getFollowCount() );

		return new Object[] { dataModel, user.getLanguage() };

	}

	private static Object[] _createDataModelForUserPratilipiEmail( String userPratilipiId )
			throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		UserPratilipiData userPratilipi = UserPratilipiDataUtil.createUserPratilipiData( dataAccessor.getUserPratilipi( userPratilipiId ) );
		PratilipiData pratilipi = PratilipiDataUtil.createPratilipiData( dataAccessor.getPratilipi( userPratilipi.getPratilipiId() ) );

		UserData reviewer = UserDataUtil.createUserData( dataAccessor.getUser( userPratilipi.getUserId() ) );

		Map<String, Object> dataModel = new HashMap<>();

		dataModel.put( "pratilipi_title", pratilipi.getTitle() != null ? pratilipi.getTitle() : pratilipi.getTitleEn() );
		dataModel.put( "pratilipi_page_url", _getDomainName( pratilipi.getLanguage() ) + pratilipi.getPageUrl() );

		dataModel.put( "reviewer_name", reviewer.getAuthor().getName() != null
												? reviewer.getAuthor().getName()
												: reviewer.getAuthor().getNameEn() );
		dataModel.put( "reviewer_page_url", _getDomainName( pratilipi.getLanguage() ) + reviewer.getProfilePageUrl() );
		dataModel.put( "reviewer_image_url", reviewer.getAuthor().getProfileImageUrl( 100 ) );
		dataModel.put( "review_creation_date", _getDateFormat( userPratilipi.getReviewDate() ) );

		if( userPratilipi.getRating() != null )
			dataModel.put( "rating", userPratilipi.getRating() );

		if( userPratilipi.getReview() != null )
			dataModel.put( "review_review", HtmlUtil.truncateText( userPratilipi.getReview(), 250 ) );

		if( userPratilipi.getCommentCount() != null )
			dataModel.put( "review_comment_count", userPratilipi.getCommentCount().toString() );

		return new Object[] { dataModel, pratilipi.getLanguage() };

	}

	private static Object[] _createDataModelForCommentReviewEmail( Long commentId )
			throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		CommentData comment = CommentDataUtil.createCommentData( dataAccessor.getComment( commentId ) );
		comment.setUser( UserDataUtil.createUserData( dataAccessor.getUser( comment.getUserId() ) ) );
		UserPratilipiData review = UserPratilipiDataUtil.createUserPratilipiData( dataAccessor.getUserPratilipi( comment.getParentId() ) );
		PratilipiData pratilipi = PratilipiDataUtil.createPratilipiData( dataAccessor.getPratilipi( review.getPratilipiId() ) );

		Map<String, Object> dataModel = new HashMap<>();

		dataModel.put( "pratilipi_title", pratilipi.getTitle() != null ? pratilipi.getTitle() : pratilipi.getTitleEn() );
		dataModel.put( "pratilipi_page_url", _getDomainName( pratilipi.getLanguage() ) + pratilipi.getPageUrl() );

		dataModel.put( "reviewer_name", review.getUser().getAuthor().getName() != null ?
								review.getUser().getAuthor().getName() : review.getUser().getAuthor().getNameEn() );
		dataModel.put( "reviewer_page_url", _getDomainName( review.getUser().getAuthor().getLanguage() ) + review.getUser().getProfilePageUrl() );
		dataModel.put( "reviewer_image_url", review.getUser().getAuthor().getProfileImageUrl( 64 ) );
		dataModel.put( "review_date", _getDateFormat( review.getReviewDate() ) );
		dataModel.put( "review_review", HtmlUtil.truncateText( review.getReview(), 250 ) );

		dataModel.put( "commentor_name", comment.getUser().getAuthor().getName() != null
										? comment.getUser().getAuthor().getName()
										: comment.getUser().getAuthor().getNameEn() );
		dataModel.put( "commentor_page_url", _getDomainName( comment.getUser().getAuthor().getLanguage() ) + comment.getUser().getProfilePageUrl() );
		dataModel.put( "commentor_image_url", comment.getUser().getAuthor().getProfileImageUrl( 50 ) );
		dataModel.put( "comment_date", _getDateFormat( comment.getCreationDate() ) );
		dataModel.put( "comment_comment", HtmlUtil.truncateText( comment.getContent(), 200 ) );

		return new Object[] { dataModel, pratilipi.getLanguage() };

	}

	private static Object[] _createDataModelForVoteReviewEmail( String voteId )
			throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		Vote vote = dataAccessor.getVote( voteId );

		UserPratilipiData userPratilipi = UserPratilipiDataUtil.createUserPratilipiData( dataAccessor.getUserPratilipi( vote.getParentId() ) );
		PratilipiData pratilipi = PratilipiDataUtil.createPratilipiData( dataAccessor.getPratilipi( userPratilipi.getPratilipiId() ) );
		UserData voter = UserDataUtil.createUserData( dataAccessor.getUser( vote.getUserId() ) );

		Map<String, Object> dataModel = new HashMap<>();
		dataModel.put( "pratilipi_title", pratilipi.getTitle() != null ? pratilipi.getTitle() : pratilipi.getTitleEn() );
		dataModel.put( "pratilipi_page_url", _getDomainName( pratilipi.getLanguage() ) + pratilipi.getPageUrl() );
		dataModel.put( "reviewer_page_url", _getDomainName( userPratilipi.getUser().getLanguage() ) + userPratilipi.getUser().getProfilePageUrl() );
		dataModel.put( "reviewer_image_url", userPratilipi.getUser().getAuthor().getProfileImageUrl( 64 ) );
		dataModel.put( "reviewer_name", userPratilipi.getUser().getAuthor().getName() != null
												? userPratilipi.getUser().getAuthor().getName()
												: userPratilipi.getUser().getAuthor().getNameEn() );
		dataModel.put( "review_creation_date", _getDateFormat( userPratilipi.getReviewDate() ) );
		dataModel.put( "review_review", HtmlUtil.toPlainText( userPratilipi.getReview() ) );

		if( userPratilipi.getRating() != null )
			dataModel.put( "rating", userPratilipi.getRating() );

		dataModel.put( "voter_name", voter.getAuthor().getName() != null
				? voter.getAuthor().getName()
				: voter.getAuthor().getNameEn() );

		return new Object[] { dataModel, pratilipi.getLanguage() };

	}

	private static Object[] _createDataModelForVoteCommentEmail( String voteId )
			throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		VoteData vote = VoteDataUtil.createVoteData( dataAccessor.getVote( voteId ) );
		vote.setUser( UserDataUtil.createUserData( dataAccessor.getUser( vote.getUserId() ) ) );
		CommentData comment = CommentDataUtil.createCommentData( dataAccessor.getComment( vote.getParentIdLong() ) );
		comment.setUser( UserDataUtil.createUserData( dataAccessor.getUser( comment.getUserId() ) ) );
		UserPratilipiData review = UserPratilipiDataUtil.createUserPratilipiData( dataAccessor.getUserPratilipi( comment.getParentId() ) );
		PratilipiData pratilipi = PratilipiDataUtil.createPratilipiData( dataAccessor.getPratilipi( review.getPratilipiId() ) );

		Map<String, Object> dataModel = new HashMap<>();

		dataModel.put( "voter_name", vote.getUser().getAuthor().getName() != null
									? vote.getUser().getAuthor().getName()
									: vote.getUser().getAuthor().getNameEn() );

		dataModel.put( "pratilipi_title", pratilipi.getTitle() != null ? pratilipi.getTitle() : pratilipi.getTitleEn() );
		dataModel.put( "pratilipi_page_url", _getDomainName( pratilipi.getLanguage() ) + pratilipi.getPageUrl() );

		dataModel.put( "reviewer_name", review.getUser().getAuthor().getName() != null ?
								review.getUser().getAuthor().getName() : review.getUser().getAuthor().getNameEn() );
		dataModel.put( "reviewer_page_url", _getDomainName( review.getUser().getAuthor().getLanguage() ) + review.getUser().getProfilePageUrl() );
		dataModel.put( "reviewer_image_url", review.getUser().getAuthor().getProfileImageUrl( 64 ) );
		dataModel.put( "review_date", _getDateFormat( review.getReviewDate() ) );
		dataModel.put( "review_review", HtmlUtil.truncateText( review.getReview(), 250 ) );

		dataModel.put( "commentor_name", comment.getUser().getAuthor().getName() != null
										? comment.getUser().getAuthor().getName()
										: comment.getUser().getAuthor().getNameEn() );
		dataModel.put( "commentor_page_url", _getDomainName( comment.getUser().getAuthor().getLanguage() ) + comment.getUser().getProfilePageUrl() );
		dataModel.put( "commentor_image_url", comment.getUser().getAuthor().getProfileImageUrl( 50 ) );
		dataModel.put( "comment_date", _getDateFormat( comment.getCreationDate() ) );
		dataModel.put( "comment_comment", HtmlUtil.truncateText( comment.getContent(), 200 ) );

		return new Object[] { dataModel, pratilipi.getLanguage() };

	}

}
