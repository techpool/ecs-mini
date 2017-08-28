package com.pratilipi.common.type;

/**
 * Created by Rahul Ranjan on 2/8/17.
 */
public enum WidgetType {

    AUTHOR_CONTENTS,        // Content list of a author. Click event takes to author profile.
    CATEGORY_BANNER,        // Banner for a category. Click event takes to content list of that category
    CURATED_BANNER,         // Banner for a curated list. Click event takes to a curated content list fetched from a file
    CURATED_CONTENTS,       // Curated content list.
    EVENT_BANNER,
    EVENT_LAUNCHED,         // Event launch widget (shown only to authors)
    FACEBOOK_CONNECT,
    INVITE_FRIENDS,
    NOTIFICATION_SETTINGS,
    RATE_EXPERIENCE,
    RECOMMENDED_CONTENTS,
    SUBSCRIBE_YOUTUBE,
    SURVEY_AUDIO,
    TOP_FOLLOWED,           // list of top followed authors.
    UPDATE_APP,
    WRITE_NEW,

}
