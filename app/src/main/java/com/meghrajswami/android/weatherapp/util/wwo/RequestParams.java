package com.meghrajswami.android.weatherapp.util.wwo;

import com.meghrajswami.android.weatherapp.Config;

import java.lang.reflect.Field;
import java.net.URLEncoder;

/**
 * Query Parameters object for World weather online's Local weather api
 * service
 */
public class RequestParams {
    //required
    private String q;
    private String extra;
    //required
    private String num_of_days;
    private String date;
    private String fx;
    //default "yes"
    private String cc;
    //default "no"
    private String includeLocation;
    //default "xml"
    private String format;
    private String show_comments;
    private String callback;
    //required
    private String key;

    public RequestParams(String key) {
        this.num_of_days = Integer.toString(Config.PARAM_NUM_OF_DAYS);
        this.fx = Config.PARAM_FX;
        this.show_comments = Config.PARAM_SHOW_COMMENTS;
        this.format = Config.PARAM_FORMAT;
        this.key = key;
    }

    public RequestParams setQ(String q) {
        this.q = q;
        return this;
    }

    public RequestParams setExtra(String extra) {
        this.extra = extra;
        return this;
    }

    public RequestParams setNumOfDays(String num_of_days) {
        this.num_of_days = num_of_days;
        return this;
    }

    public RequestParams setDate(String date) {
        this.date = date;
        return this;
    }

    public RequestParams setFx(String fx) {
        this.fx = fx;
        return this;
    }

    public RequestParams setCc(String cc) {
        this.cc = cc;
        return this;
    }

    public RequestParams setIncludeLocation(String includeLocation) {
        this.includeLocation = includeLocation;
        return this;
    }

    public RequestParams setFormat(String format) {
        this.format = format;
        return this;
    }

    public RequestParams setShowComments(String showComments) {
        this.show_comments = showComments;
        return this;
    }

    public RequestParams setCallback(String callback) {
        this.callback = callback;
        return this;
    }

    public RequestParams setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * build encoded query string with character set UTF-8 for use in url
     * prefixed with '?'
     *
     * @return query string
     */
    public String getQueryString() {
        Class cls = getClass();
        String query = null;
        Field[] fields = cls.getDeclaredFields();
        try {
            for (Field field : fields) {
                Object f = field.get(this);
                if (f != null) {
                    if (query == null)
                        query = "?";
                    else
                        query += "&";
                    query += URLEncoder.encode(field.getName(), "UTF-8") + "="
                            + URLEncoder.encode((String) f, "UTF-8");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return query;
    }
}