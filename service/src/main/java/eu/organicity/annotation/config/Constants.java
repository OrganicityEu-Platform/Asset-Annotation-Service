package eu.organicity.annotation.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Constants {
    public static final String LOGIN_TEMPLATE = "login";
    public static final String INDEX_TEMPLATE = "swagger-ui.html";


    public static String epochToISODatetime(Long date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
        df.setTimeZone(tz);
        return df.format(new Date(date));
    }
}
