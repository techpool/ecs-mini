package com.pratilipi.data.type;

import com.pratilipi.common.type.Language;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by genirahul on 30/6/17.
 */
public interface AuthorByReadCount extends GenericOfyType, Serializable {

    String getId();

    String getAuthorId();

    void setAuthorId(String authorId);

    String getUserId();

    void setUserId(String userId);

    Boolean hasUserId();

    void setHasUserId(Boolean hasUserId);

    String getName();

    void setName(String name);

    String getCoverImageUrl();

    void setCoverImageUrl(String coverImageUrl);

    String getProfilePageUrl();

    void setProfilePageUrl(String profilePageUrl);

    Language getLanguage();

    void setLanguage(Language language);

    Long getReadCount();

    void setReadCount(Long readCount);

    Long getReadCountWeb();

    void setReadCountWeb(Long readCountWeb);

    Long getReadCountAndroid();

    void setReadCountAndroid(Long readCountAndroid);

    Date getDate();

    void setDate(Date date);
}
