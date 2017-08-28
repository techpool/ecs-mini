package com.pratilipi.common.util;

import java.util.ArrayList;
import java.util.List;

import com.pratilipi.common.type.AccessType;
import com.pratilipi.common.type.Language;

public class UserAccessUtil {

	private static final AccessType[] MEMBER_ACCESS = {
			AccessType.PRATILIPI_ADD_REVIEW,
			AccessType.USER_PRATILIPI_REVIEW, AccessType.USER_PRATILIPI_LIBRARY,
			AccessType.USER_AUTHOR_FOLLOWING,
			AccessType.COMMENT_ADD, AccessType.COMMENT_UPDATE,
			AccessType.VOTE };

	private static final AccessType[] ADMIN_ACCESS = {
			AccessType.INIT_UPDATE,
			AccessType.PRATILIPI_LIST, AccessType.PRATILIPI_ADD, AccessType.PRATILIPI_UPDATE,
			AccessType.PRATILIPI_READ_META, AccessType.PRATILIPI_UPDATE_META, AccessType.PRATILIPI_READ_CONTENT,
			AccessType.AUTHOR_LIST, AccessType.AUTHOR_ADD, AccessType.AUTHOR_UPDATE,
			AccessType.EVENT_ADD, AccessType.EVENT_UPDATE,
			AccessType.BLOG_POST_LIST, AccessType.BLOG_POST_ADD, AccessType.BLOG_POST_UPDATE, AccessType.I18N_UPDATE };

	private enum Role {

		GUEST			( null ),
		MEMBER			( null,					MEMBER_ACCESS ),

		ADMIN			( null,					AccessType.USER_ADD, AccessType.USER_UPDATE, AccessType.PRATILIPI_LIST, AccessType.BATCH_PROCESS_ADD, AccessType.BATCH_PROCESS_LIST ),
		ADMIN_BENGALI	( Language.BENGALI,		ADMIN_ACCESS ),
		ADMIN_GUJARATI	( Language.GUJARATI,	ADMIN_ACCESS ),
		ADMIN_HINDI		( Language.HINDI,		ADMIN_ACCESS ),
		ADMIN_KANNADA	( Language.KANNADA,		ADMIN_ACCESS ),
		ADMIN_MALAYALAM	( Language.MALAYALAM,	ADMIN_ACCESS ),
		ADMIN_MARATHI	( Language.MARATHI,		ADMIN_ACCESS ),
		ADMIN_TAMIL		( Language.TAMIL,		ADMIN_ACCESS ),
		ADMIN_TELUGU	( Language.TELUGU,		ADMIN_ACCESS ),

		ADMINISTRATOR	( null,					AccessType.values() );


		private Language language;
		private AccessType[] accessTypes;


		private Role( Language language, AccessType ...accessTypes ) {
			this.language = language;
			this.accessTypes = accessTypes;
		}

		public Language getLanguage() {
			return language;
		}

		public boolean hasAccess( Language language, AccessType accessType ) {
			if( this.language != null && this.language != language )
				return false;
			if( this.accessTypes == null )
				return false;
			for( AccessType at : accessTypes )
				if( accessType == at )
					return true;
			return false;
		}

	}

	public enum AEE {

		MOUMITA ( 6243664397336576L, Role.MEMBER, Role.ADMIN, Role.ADMIN_BENGALI ),
		NIMISHA ( 5644707593977856L, Role.MEMBER, Role.ADMIN, Role.ADMIN_GUJARATI, Role.ADMIN_HINDI ),
		VEENA   ( 4790800105865216L, Role.MEMBER, Role.ADMIN, Role.ADMIN_GUJARATI, Role.ADMIN_HINDI, Role.ADMIN_MARATHI ),
		VRUSHALI( 4900189601005568L, Role.MEMBER, Role.ADMIN, Role.ADMIN_MARATHI, Role.ADMIN_HINDI ),
		BRINDA  ( 6046961763352576L, Role.MEMBER, Role.ADMIN, Role.ADMIN_GUJARATI ),

		KIMAYA  ( 5373377891008512L, Role.MEMBER, Role.ADMIN, Role.ADMIN_MARATHI, Role.ADMIN_HINDI ),
		JITESH  ( 5743817900687360L, Role.MEMBER, Role.ADMIN, Role.ADMIN_BENGALI, Role.ADMIN_GUJARATI, Role.ADMIN_HINDI, Role.ADMIN_MARATHI ),
		SHALLY  ( 5664902681198592L, Role.MEMBER, Role.ADMIN, Role.ADMIN_BENGALI, Role.ADMIN_GUJARATI, Role.ADMIN_HINDI, Role.ADMIN_MARATHI ),

		VAISAKH     ( 5666355716030464L, Role.MEMBER, Role.ADMIN, Role.ADMIN_MALAYALAM ),
		DIPLEEPAN   ( 4900071594262528L, Role.MEMBER, Role.ADMIN, Role.ADMIN_TAMIL ),
		JOHNY       ( 5187684625547264L, Role.MEMBER, Role.ADMIN, Role.ADMIN_TELUGU ),
		ARUNA       ( 5715256422694912L, Role.MEMBER, Role.ADMIN, Role.ADMIN_KANNADA ),
		SANKAR      ( 5991416564023296L, Role.MEMBER, Role.ADMIN, Role.ADMIN_BENGALI, Role.ADMIN_GUJARATI, Role.ADMIN_HINDI, Role.ADMIN_KANNADA, Role.ADMIN_MALAYALAM, Role.ADMIN_MARATHI, Role.ADMIN_TAMIL, Role.ADMIN_TELUGU ),

		DRASTI  ( 4908348089565184L, Role.MEMBER, Role.ADMIN, Role.ADMIN_HINDI, Role.ADMIN_GUJARATI ),
		ANURAG  ( 5013864096727040L, Role.MEMBER, Role.ADMIN, Role.ADMIN_HINDI ),

		RADHIKA ( 5124071978172416L, Role.MEMBER, Role.ADMIN, Role.ADMIN_BENGALI, Role.ADMIN_GUJARATI, Role.ADMIN_HINDI, Role.ADMIN_KANNADA, Role.ADMIN_MALAYALAM, Role.ADMIN_MARATHI, Role.ADMIN_TAMIL, Role.ADMIN_TELUGU ),
		ABHISHEK( 5694768648552448L, Role.MEMBER, Role.ADMIN, Role.ADMIN_BENGALI, Role.ADMIN_GUJARATI, Role.ADMIN_HINDI, Role.ADMIN_KANNADA, Role.ADMIN_MALAYALAM, Role.ADMIN_MARATHI, Role.ADMIN_TAMIL, Role.ADMIN_TELUGU ),
		RAHUL( 5073076857339904L, Role.MEMBER, Role.ADMIN, Role.ADMIN_BENGALI, Role.ADMIN_GUJARATI, Role.ADMIN_HINDI, Role.ADMIN_KANNADA, Role.ADMIN_MALAYALAM, Role.ADMIN_MARATHI, Role.ADMIN_TAMIL, Role.ADMIN_TELUGU ),
		RANJEET ( 6264191547604992L, Role.MEMBER, Role.ADMIN, Role.ADMIN_BENGALI, Role.ADMIN_GUJARATI, Role.ADMIN_HINDI, Role.ADMIN_KANNADA, Role.ADMIN_MALAYALAM, Role.ADMIN_MARATHI, Role.ADMIN_TAMIL, Role.ADMIN_TELUGU ),
		GAURI   ( 6311393362968576L, Role.MEMBER, Role.ADMIN, Role.ADMIN_BENGALI, Role.ADMIN_GUJARATI, Role.ADMIN_HINDI, Role.ADMIN_KANNADA, Role.ADMIN_MALAYALAM, Role.ADMIN_MARATHI, Role.ADMIN_TAMIL, Role.ADMIN_TELUGU ),
		RAGHU   ( 6196244602945536L, Role.MEMBER, Role.ADMINISTRATOR ),
		PRASHANT( 5705241014042624L, Role.MEMBER, Role.ADMINISTRATOR );


		private Long userId;
		private Role[] roles;

		private AEE( Long userId, Role ...roles ) {
			this.userId = userId;
			this.roles = roles;
		}

		public Long getUserId() {
			return userId;
		}

		public Role[] getRoles() {
			return roles;
		}

	}

	private static Role[] getRoles( Long userId ) { // TODO: Migrate this data to DataStore/CloudStore

		if( userId.equals( 0L ) )
			return new Role[] { Role.GUEST };

		for( AEE aEE : AEE.values() )
			if( userId.equals( aEE.getUserId() ) )
				return aEE.roles;

		return new Role[] { Role.MEMBER };

	}

	public static boolean hasUserAccess( Long userId, Language language, AccessType access ) {
		for( Role role : getRoles( userId ) )
			if( role.hasAccess( language, access ) )
				return true;
		return false;
	}

	public static List<Long> getAeeUserIdList( Language language ) {
		List<Long> aeeUserIdList = new ArrayList<>();
		for( AEE aEE : AEE.values() ) {
			for( Role role : aEE.roles ) {
				if( role.getLanguage() == language )
					aeeUserIdList.add( aEE.getUserId() );
			}
		}
		return aeeUserIdList;
	}

}
