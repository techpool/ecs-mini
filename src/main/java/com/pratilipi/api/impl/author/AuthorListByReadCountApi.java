package com.pratilipi.api.impl.author;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.client.AuthorByReadCountData;
import com.pratilipi.data.type.AccessToken;
import com.pratilipi.data.util.AppPropertyUtil;
import com.pratilipi.data.util.AuthorDataUtil;
import com.pratilipi.filter.AccessTokenFilter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by genirahul on 1/7/17.
 */

@SuppressWarnings( "serial" )
@Bind( uri = "/author/list/readcount" )
public class AuthorListByReadCountApi extends GenericApi {

    public static class GetRequest extends GenericRequest {

        Language language;

        Integer resultCount;

        String cursor;

        public Integer getResultCount() {
            return this.resultCount;
        }

        public Language getLanguage() {
            return this.language;
        }

        public String getCursor() {
            return this.cursor;
        }
    }

    public class GetResponse extends GenericResponse {

        List<AuthorByReadCountData> authorDataList;

        // in dd/mm/yyyy format
        String date;

        String cursor;

        private GetResponse() {}

        private GetResponse(List<AuthorByReadCountData> authorDataList, String date, String cursor) {
            this.authorDataList = authorDataList;
            this.date = date;
            this.cursor = cursor;
        }
    }

    public static class PostRequest extends GenericRequest {

        Long pratilipiId;

        Long webReadCount;

        Long androidReadCount;

//        Boolean isDeletePost;

        String date;

        public Long getPratilipiId() {
            return pratilipiId;
        }

        public Long getWebReadCount() {
            return webReadCount;
        }

        public Long getAndroidReadCount() {
            return androidReadCount;
        }

//        public Boolean isDeletePost() {
//            return this.isDeletePost;
//        }

        public String getDate() { return this.date; }
    }

    public class PostResponse extends GenericResponse {
        Boolean isSuccess;

        private PostResponse() {}

        public PostResponse(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }
    }

    @Get
    public GetResponse get(GetRequest request) throws  UnexpectedServerException {

        DataListCursorTuple<AuthorByReadCountData> dataListCursorTuple = AuthorDataUtil.getAuthorListByReadCount(
                request.getLanguage(),
                request.getResultCount(),
                request.getCursor()
        );

        if (dataListCursorTuple == null) {
            Logger.getLogger(AuthorListByReadCountApi.class.getSimpleName())
                    .log(Level.INFO, "DB returned null cursor tuple");
            throw new UnexpectedServerException();
        }

        Date date = AppPropertyUtil.getTopAuthorLoadDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Logger.getLogger(AuthorListByReadCountApi.class.getSimpleName())
                .log(Level.INFO, "Date : " + dateFormat.format(date));

        return new GetResponse(
                dataListCursorTuple.getDataList(),
                dateFormat.format(date),
                dataListCursorTuple.getCursor()
        );
    }

    @Post
    public PostResponse post(PostRequest request) throws InsufficientAccessException {

        AccessToken accessToken = AccessTokenFilter.getAccessToken();

        if (accessToken.getUserId() != 5073076857339904L && accessToken.getUserId() != 5686960458825728L)
            throw  new InsufficientAccessException();
        Logger.getLogger(AuthorListByReadCountApi.class.getSimpleName())
                    .log(Level.INFO, "Updating author by read count");

        boolean isSuccess;
//        boolean isDeletePost = request.isDeletePost() != null ? request.isDeletePost() : false;
        if (false) {
            isSuccess = AuthorDataUtil.deleteAuthorsByReadCount(null);
        } else {
            isSuccess = AuthorDataUtil.updateAuthorByReadCount(
                    request.getPratilipiId(), request.getWebReadCount(), request.getAndroidReadCount(), request.getDate()
            );
        }

        return new PostResponse(isSuccess);
    }

}
