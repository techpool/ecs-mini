package com.pratilipi.data.type.gae;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pratilipi.common.type.Language;
import com.pratilipi.data.type.AuthorByReadCount;

import java.util.Date;

/**
 * Created by genirahul on 30/6/17.
 */

@SuppressWarnings("serial")
@Cache
@Entity( name = "AUTHOR_BY_READ_COUNT" )
public class AuthorByReadCountEntity implements AuthorByReadCount {

    @Id
    private String AUTHOR_ID_DATE;

    @Index
    private String AUTHOR_ID;

    @Index
    private String USER_ID;

    @Index
    private Boolean HAS_USER_ID;

    private String AUTHOR_NAME;

    private String COVER_IMAGE_URL;

    private String PROFILE_PAGE_URL;

    @Index
    private Language LANGUAGE;

    @Index
    private Long READ_COUNT;

    private Long READ_COUNT_WEB;

    private Long READ_COUNT_ANDROID;

    @Index
    private Date DATE;


    @Override
    public String getId() {
        return this.AUTHOR_ID_DATE;
    }

    public void setId( String id ) {
        this.AUTHOR_ID_DATE = id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Key<T> getKey() {
        return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
    }

    @Override
    public <T> void setKey(Key<T> key) {
        this.AUTHOR_ID_DATE = key.getName();
    }

    @Override
    public String getAuthorId() {
        return AUTHOR_ID;
    }

    @Override
    public void setAuthorId(String AUTHOR_ID) {
        this.AUTHOR_ID = AUTHOR_ID;
    }


    @Override
    public String getUserId() {
        return this.USER_ID;
    }

    @Override
    public void setUserId(String userId) {
        this.USER_ID = userId;
    }

    @Override
    public Boolean hasUserId() {
        return this.HAS_USER_ID;
    }

    @Override
    public void setHasUserId(Boolean hasUserId) {
        this.HAS_USER_ID = hasUserId;
    }

    @Override
    public String getName() {
        return this.AUTHOR_NAME;
    }

    @Override
    public void setName(String name) {
        this.AUTHOR_NAME = name;
    }

    @Override
    public String getCoverImageUrl() {
        return this.COVER_IMAGE_URL;
    }

    @Override
    public void setCoverImageUrl(String coverImageUrl) {
        this.COVER_IMAGE_URL = coverImageUrl;
    }

    @Override
    public String getProfilePageUrl() {
        return this.PROFILE_PAGE_URL;
    }

    @Override
    public void setProfilePageUrl(String profilePageUrl) {
        this.PROFILE_PAGE_URL = profilePageUrl;
    }

    @Override
    public Language getLanguage() {
        return LANGUAGE;
    }

    @Override
    public void setLanguage(Language language) {
        this.LANGUAGE = language;
    }

    @Override
    public Long getReadCount() {
        return this.READ_COUNT;
    }

    @Override
    public void setReadCount(Long readCount) {
        this.READ_COUNT = readCount;
    }

    @Override
    public Long getReadCountWeb() {
        return READ_COUNT_WEB;
    }

    @Override
    public void setReadCountWeb(Long readCountWeb) {
        this.READ_COUNT_WEB = readCountWeb;
    }

    @Override
    public Long getReadCountAndroid() {
        return READ_COUNT_ANDROID;
    }

    @Override
    public void setReadCountAndroid(Long readCountAndroid) {
        this.READ_COUNT_ANDROID = readCountAndroid;
    }

    @Override
    public Date getDate() {
        return this.DATE;
    }

    @Override
    public void setDate(Date date) {
        this.DATE  = date;
    }

}
