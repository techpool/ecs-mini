package com.pratilipi.common.type;

public enum I18nGroup {

	NOTIFICATION (
			"notification_and",
			// PRATILIPI_ADD
			"notification_has_published",	// Chetan Bhagat has published a new content : 2 States
			// AUTHOR_FOLLOW
			"notification_has_followed",		// Ram has followed you
			"notification_have_followed",		// Ram & Shayam have followed you
			"notification_others_have_followed"	// Ram, Shyam and 7 others have followed you
			),

	EMAIL (
			"email_author_follow_button",
			"email_author_follow_heading",
			"email_author_follow_subject",
			"email_comment_review_author_button",
			"email_comment_review_author_heading",
			"email_comment_review_author_subject",
			"email_comment_review_reviewer_button",
			"email_comment_review_reviewer_heading",
			"email_comment_review_reviewer_subject",
			"email_pratilipi_published_author_button",
			"email_pratilipi_published_author_heading",
			"email_pratilipi_published_author_subject",
			"email_pratilipi_published_follower_button",
			"email_pratilipi_published_follower_heading",
			"email_pratilipi_published_follower_subject",
			"email_userpratilipi_rating_button",
			"email_userpratilipi_rating_heading",
			"email_userpratilipi_rating_subject",
			"email_userpratilipi_review_button",
			"email_userpratilipi_review_heading",
			"email_userpratilipi_review_subject",
			"email_vote_comment_review_commentor_button",
			"email_vote_comment_review_commentor_heading",
			"email_vote_comment_review_commentor_subject",
			"email_vote_review_author_button",
			"email_vote_review_author_heading",
			"email_vote_review_author_subject",
			"email_vote_review_reviewer_button",
			"email_vote_review_reviewer_heading",
			"email_vote_review_reviewer_subject",
			"email_consolidated_subject"
			)
	;
	
	
	private String[] i18nIds;
	
	
	private I18nGroup( String ...i18nIds ) {
		this.i18nIds = i18nIds;
	}
	
}
