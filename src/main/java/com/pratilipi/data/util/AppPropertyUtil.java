package com.pratilipi.data.util;

import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.AppProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by genirahul on 12/7/17.
 */
public class AppPropertyUtil {

    private static final Logger logger =
            Logger.getLogger(AppPropertyUtil.class.getName());

    public static AppProperty createOrUpdateAppProperty(String id, String value) {

        DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

        logger.log(Level.INFO, "App property Id : " + id);
        AppProperty appProperty = dataAccessor.getAppProperty(id);

        if (appProperty == null) {
            logger.log(Level.INFO, "App property not present");
            appProperty = dataAccessor.newAppProperty(id);
        }
        logger.log(Level.INFO, "App property value : " + value);
        appProperty.setValue(value);
        appProperty = dataAccessor.createOrUpdateAppProperty(appProperty);
        return  appProperty;
    }

    public static AppProperty createOrUpdateTopAuthorsDate(String date) {
        return createOrUpdateAppProperty(AppProperty.TOP_AUTHORS_DATE, date);
    }

    public static AppProperty createOrUpdatePersonalizedHomeFilename(String filename) {
        return createOrUpdateAppProperty(AppProperty.PERSONALIZED_HOME_FILENAME, filename);
    }

    public static Date getTopAuthorLoadDate() {
        AppProperty appProperty = DataAccessorFactory.getDataAccessor().getAppProperty(AppProperty.TOP_AUTHORS_DATE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        if (appProperty == null) {
            // IF AppProperty is not present return day before yesterday's date.
            // Same is present in AuthorDataUtil.getAuthorListByReadCount function, line 886
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -2);
            return  cal.getTime();
        } else {
            String dateString = appProperty.getValue();
            try {
                return  dateFormat.parse(dateString);
            } catch (ParseException e) {
                logger.log(Level.SEVERE, "Error while Top authors date from app property table.");
                e.printStackTrace();
                return null;
            }
        }

    }

    public static String getPersonalizedHomeFilename() {
        AppProperty appProperty = DataAccessorFactory
                .getDataAccessor()
                .getAppProperty(AppProperty.PERSONALIZED_HOME_FILENAME);

        return  appProperty == null ? "personalized.home." : appProperty.getValue().toString();
    }
}
