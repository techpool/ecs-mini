package com.pratilipi.common.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.type.SourceType;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.util.PratilipiDataUtil;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Rahul Ranjan on 3/8/17.
 */
public class PersonalizedInitUtil {

    private static final Logger logger = Logger.getLogger(PersonalizedInitUtil.class.getSimpleName());

    private static final String BACKGROUND_COLOR = "backgroundColor";
    private static final String BORDER_COLOR = "borderColor";
    private static final String DESTINATION_PARAMS = "destinationParams";
    private static final String DESTINATION_TYPE = "destinationType";
    private static final String EXPERIMENT_ID = "experimentId";
    private static final String IMAGE_URL = "imageUrl";
    private static final String MESSAGE = "message";
    private static final String NEGATIVE_BUTTON = "negativeButton";
    private static final String POSITION = "position";
    private static final String POSITIVE_BUTTON = "positiveButton";
    private static final String SUBTITLE = "subtitle";
    private static final String TEXT = "text";
    private static final String TEXT_COLOR = "textColor";
    private static final String TEXT_SIZE = "textSize";
    private static final String TEMPLATE_TYPE = "template";
    private static final String TITLE = "title";

    public static JsonArray prepareResponse(Language language, String appVersion, String androidVersion)
        throws UnexpectedServerException{

        logger.log(Level.INFO, "Preparing json response. ");
        DateTime startTime = new DateTime();
        logger.log(Level.INFO, "Start time : " + startTime);

        JsonArray jsonArray = new JsonArray();

        List<String> widgetList = DataAccessorFactory.getDataAccessor()
                                        .getPersonalizedHomeWidgetList(language);
        for (String widget : widgetList) {
            String[] properties = widget.split("~!", -1);
            logger.log(Level.INFO, "Widget properties length : " + properties.length);
            // Line doesn't contain all required parameters.
            if (properties.length < 31)
                continue;
            logger.log(Level.INFO, "Position : " + properties[0]);
            JsonObject widgetObj = new JsonObject();
            widgetObj.addProperty(POSITION, properties[0]);
            widgetObj.addProperty(TEMPLATE_TYPE, properties[2]);
            if (properties[3] != null && !properties[3].isEmpty())
                widgetObj.addProperty(BACKGROUND_COLOR, properties[3]);
            widgetObj.addProperty(EXPERIMENT_ID, properties[4]);

            // Title
            if (properties[5] != null && !properties[5].isEmpty()) {
                JsonObject titleObj =
                        prepareTextElementJson(
                                properties[5],  // Title Text
                                properties[6],    // Title text size
                                properties[7],  // Title text color
                                properties[8]   // Title image url mostly used for AUTHOR_CONTENT widget type
                        );
                widgetObj.add(TITLE, titleObj);
            }

            // Subtitle
            if (properties[9] != null && !properties[9].isEmpty()) {
                JsonObject subtitleObj =
                        prepareTextElementJson(
                                properties[9],  // Subtitle Text
                                properties[10],    // Subtitle text size
                                properties[11],  // Subtitle text color
                                null    // Subtitle image url
                        );
                widgetObj.add(SUBTITLE, subtitleObj);
            }

            // Click event of the widget
            if (properties[12] != null && !properties[12].isEmpty()) {
                widgetObj.addProperty(DESTINATION_TYPE, properties[12]);
                widgetObj.addProperty(DESTINATION_PARAMS, properties[13]);
            }

            // Content list
            if (properties[14] != null && !properties[14].isEmpty()) {
                if (properties[14].equals(SourceType.CURATED_CONTENT_LIST.toString())) {
                    JsonArray contents =
                            prepareCurratedContentListJson(language, properties[15], Integer.parseInt(properties[16]));
                    widgetObj.add("pratilipiList", contents);
                }
            }

            if (properties[17] != null && !properties[17].isEmpty()) {
                widgetObj.addProperty(IMAGE_URL, properties[17]);
            }

            if (properties[18] != null && !properties[18].isEmpty()) {
                widgetObj.addProperty(MESSAGE, properties[18]);
            }

            // Button one
            if (properties[19] != null && !properties[19].isEmpty()) {
                JsonObject button1Obj =
                        prepareElementJson(
                                properties[19],     // Button Title
                                properties[20],    // Button text size
                                properties[21],     // Button text color
                                properties[22],     // Button Background color
                                properties[23],     // Button Border color
                                properties[24]      // Button image url
                        );
                widgetObj.add(POSITIVE_BUTTON, button1Obj);
            }

            // Button two
            if (properties[25] != null && !properties[25].isEmpty()) {
                JsonObject buttonObj =
                        prepareElementJson(
                                properties[25],     // Button Title
                                properties[26],    // Button text size
                                properties[27],     // Button text color
                                properties[28],     // Button Background color
                                properties[29],     // Button Border color
                                properties[30]      // Button image url
                        );
                widgetObj.add(NEGATIVE_BUTTON, buttonObj);
            }

            jsonArray.add(widgetObj);
        }

        DateTime endTime = new DateTime();
        logger.log(Level.INFO, "End Time : " + endTime);
        logger.log(Level.INFO, "Time Taken (in Millis) : " + (endTime.getMillis()-startTime.getMillis()));
        System.out.print("Time Taken (in Millis) : " + (endTime.getMillis()-startTime.getMillis()));
        return jsonArray;
    }

    private static JsonObject prepareTextElementJson(
            String text, @Nullable String textSize, @Nullable String textColor, @Nullable String imageUrl) {

        return prepareElementJson(text, textSize, textColor, null, null, imageUrl);
    }

    private static JsonObject prepareElementJson(
            String text, @Nullable String textSize, @Nullable String textColor,
            @Nullable String bgColor, @Nullable String borderColor, @Nullable String imageUrl) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(TEXT, text);
        if (textColor != null && !textColor.isEmpty())
            jsonObject.addProperty(TEXT_COLOR, textColor);
        if (textSize != null && !textSize.isEmpty())
            jsonObject.addProperty(TEXT_SIZE, textSize);
        if (bgColor != null && !bgColor.isEmpty())
            jsonObject.addProperty(BACKGROUND_COLOR, bgColor);
        if (borderColor != null && !borderColor.isEmpty())
            jsonObject.addProperty(BORDER_COLOR, borderColor);
        if (imageUrl != null && !imageUrl.isEmpty())
            jsonObject.addProperty(IMAGE_URL, imageUrl);

        return jsonObject;
    }

    private static JsonArray prepareCurratedContentListJson(
            Language language, String listName, @Nullable Integer resultCount) throws UnexpectedServerException {
        JsonArray jsonArray = new JsonArray();

        PratilipiFilter pratilipiFilter = new PratilipiFilter();
        pratilipiFilter.setLanguage(language);
        pratilipiFilter.setListName(listName);
        pratilipiFilter.setState(PratilipiState.PUBLISHED);

        DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
        List<Long> pratilipiIdList =
                dataAccessor.getPratilipiIdList(pratilipiFilter, null, null, resultCount)
                        .getDataList();

        List<PratilipiData> pratilipiDataList =
                PratilipiDataUtil.createPratilipiDataList(pratilipiIdList, true);

        for (PratilipiData pratilipiData : pratilipiDataList) {
            jsonArray.add(new Gson().toJsonTree(pratilipiData));
        }

        return jsonArray;
    }
}
