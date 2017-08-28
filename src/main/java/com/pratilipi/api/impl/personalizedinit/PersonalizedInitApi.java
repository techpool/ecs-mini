package com.pratilipi.api.impl.personalizedinit;

import com.google.gson.JsonArray;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.util.PersonalizedInitUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Rahul Ranjan on 3/8/17.
 */


@Bind (uri = "/personalized/init")
public class PersonalizedInitApi extends GenericApi{

    private static final Logger logger = Logger.getLogger(PersonalizedInitApi.class.getSimpleName());

    private static class Request extends GenericRequest {

        @Validate (required = true)
        Language language;

        @Validate (required = true)
        String appVersion;

        String androidVersion;

        public Language getLanguage() {
            return language;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public String getAndroidVersion() {
            return androidVersion;
        }
    }

    private static class Response extends GenericResponse {

        JsonArray response;

        public Response(JsonArray jsonArray) {
            this.response = jsonArray;
        }
    }

    @Get
    public Response get(Request request) throws UnexpectedServerException {

        logger.log(Level.INFO, "Language : " + request.getLanguage());
        logger.log(Level.INFO, "AppVersion : " + request.getAppVersion());

        JsonArray responseArray = PersonalizedInitUtil.prepareResponse(
                request.getLanguage(),
                request.getAppVersion(),
                request.getAndroidVersion()
        );

        return new Response(responseArray);
    }

}
