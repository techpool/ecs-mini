package com.pratilipi.api.impl.appproperty;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.impl.author.AuthorListByReadCountApi;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.type.AccessToken;
import com.pratilipi.data.type.AppProperty;
import com.pratilipi.data.util.AppPropertyUtil;
import com.pratilipi.filter.AccessTokenFilter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by genirahul on 12/7/17.
 */

@SuppressWarnings("serial")
@Bind(uri = "/appproperty")
public class AppPropertyApi extends GenericApi {

    private static class PostRequest extends GenericRequest {

        // DATE FORMAT : YYYYMMDD
        String date;

        // Personalized Home filename.
        String filename;

        public String getDate() {
            return this.date;
        }

        public String getFilename() {
            return this.filename;
        }
    }

    private class PostResponse extends GenericResponse {

        boolean isUpdated;

        private PostResponse() {
        }

        private PostResponse(boolean isUpdated) {
            this.isUpdated = isUpdated;
        }

    }

    @Post
    public PostResponse updateTopAuthorDate(PostRequest request)
            throws InsufficientAccessException, UnexpectedServerException {

        AccessToken accessToken = AccessTokenFilter.getAccessToken();

        if (accessToken.getUserId() != 5073076857339904L)
            throw new InsufficientAccessException();
        Logger.getLogger(AuthorListByReadCountApi.class.getSimpleName())
                .log(Level.INFO, "Updating app property");

        String dateStr = request.getDate();
        String filename = request.getFilename();

        if (dateStr != null && filename != null)
            throw new UnexpectedServerException("Both date and filename are persent");

        boolean isUpdated = false;

        if (dateStr != null && !dateStr.isEmpty()) {
            Logger.getLogger(AuthorListByReadCountApi.class.getSimpleName())
                    .log(Level.INFO, "Updating Top author date app property");
            AppProperty appProperty = AppPropertyUtil.createOrUpdateTopAuthorsDate(dateStr);
            if (dateStr.equals(appProperty.getValue()))
                isUpdated = true;
        }


        if (filename != null && !filename.isEmpty()) {
            Logger.getLogger(AuthorListByReadCountApi.class.getSimpleName())
                    .log(Level.INFO, "Updating personalized home filename");
            AppProperty appProperty = AppPropertyUtil.createOrUpdatePersonalizedHomeFilename(filename);
            if (appProperty.getValue().equals(filename))
                isUpdated = true;
        }


        return new PostResponse(isUpdated);
    }
}
