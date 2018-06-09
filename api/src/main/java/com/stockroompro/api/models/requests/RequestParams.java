package com.stockroompro.api.models.requests;

import android.util.Log;

import com.artjoker.tool.core.Crypto;
import com.stockroompro.api.BuildConfig;
import com.stockroompro.api.Communicator;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by alexsergienko on 25.03.15.
 */
public class RequestParams {
    private static final String TAG = RequestParams.class.getSimpleName();

    public static final long REQUEST_TIME = System.currentTimeMillis() / 1000;
    private static final String TIME = "t";
    private static final String HASH = "h";
    private static final String APP_ID = "app_id";
    private Map<String, Object> params = new LinkedHashMap<>();

    public Map<String, Object> getParams() {
        return params;
    }

//    public void setParams(Map<String, Object> params) {
//        this.params = params;
//    }

    public RequestParams() {
        addParameter(APP_ID, BuildConfig.SERVER_APP_ID);
        addParameter(TIME, REQUEST_TIME);
    }

    public RequestParams addParameter(String name, Object object) {
        params.put(name, object);
        return this;
    }

    //1
    public Map<String, Object> buildJSON(String queryPath) {
        addParameter(HASH, makeHash(String.format("%s%s%s", BuildConfig.HOST, BuildConfig.PATH, queryPath)));
        return params;
    }

    private String makeHash(String url) {
        Log.d(TAG, "url - " + url);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BuildConfig.SERVER_APP_KEY);
        stringBuilder.append(REQUEST_TIME);

        String paramJson = Communicator.getConverter().toJson(params);
        Log.d(TAG, "paramJson - " + paramJson);

        String paramsEncoded = URLEncoder.encode(paramJson);
        Log.d(TAG, "paramEncoded - " + paramsEncoded);

        stringBuilder.append(Crypto.md5(Crypto.encodeToAscii(url + paramsEncoded)));

        return Crypto.md5(stringBuilder.toString());
    }

    public Map<String, Object> buildFiltersJSON(String queryPath) {
        addParameter(HASH, makeHash(String.format("%s%s%s", BuildConfig.HOST, BuildConfig.PATH, queryPath)));

        Map<String, Object> params1 = new LinkedHashMap<>();
        try {
            for (String key : params.keySet()) {
                int i = 0;
                if (key.equals(ParamNames.ADVERT_FILTERS)) {
                    ArrayList<Integer> list = (ArrayList<Integer>) params.get(key);
                    for (Integer integer : list) {
                        String newKey = URLEncoder.encode(String.format(ParamNames.FILTERS_ARRAY_ITEM, i), "UTF-8");
                        params1.put(newKey, integer);
                        i++;
                    }
                } else if (key.equals(ParamNames.ADVERT_PRICE_TYPES)) {
                    ArrayList<Integer> list = (ArrayList<Integer>) params.get(key);
                    for (Integer integer : list) {
                        String newKey = URLEncoder.encode(String.format(ParamNames.ADVERT_PRICE_ARRAY, i), "UTF-8");
                        params1.put(newKey, integer);
                        i++;
                    }
                } else {
                    params1.put(key, params.get(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return params1;
    }

    public String getHashOfJSONWithPathParams(String queryPath, Object... params) {
        for (int i = 0; i < params.length; i += 2) {
            if (queryPath.contains(String.format("{%s}", params[i]))) {
                queryPath = queryPath.replace(String.format("{%s}", params[i]), String.valueOf(params[i + 1]));
            } else
                throw new IllegalArgumentException("Cannot find request parameter in path " + params[i]);
        }

        return makeHash(String.format("%s%s%s", BuildConfig.HOST, BuildConfig.PATH, queryPath));
    }

    public String getHash(String queryPath, Object... params) {
        return makeHash(String.format("%s%s%s", BuildConfig.HOST, BuildConfig.PATH, queryPath));
    }

    public Map<String, Object> buildJSONWithPathParams(String queryPath, Object... params) {
        for (int i = 0; i < params.length; i += 2) {
            if (queryPath.contains(String.format("{%s}", params[i]))) {
                queryPath = queryPath.replace(String.format("{%s}", params[i]), String.valueOf(params[i + 1]));
            } else
                throw new IllegalArgumentException("Cannot find request parameter in path " + params[i]);
        }
        return buildJSON(queryPath);
    }

    public interface ParamNames {
        String DATE = "date";
        String CATEGORY_ID = "category_id";
        String CATEGORY_NAME = "category_name";
        String EMAIL = "email";
        String PASSWORD = "password";
        String OLD_PASSWORD = "old_password";
        String LAST_NAME = "last_name";
        String FIRST_NAME = "first_name";
        String PHONE_NUMBER = "phones";
        String UID = "uid";
        String SERVICE_UID = "service_uid";
        String TOKEN = "token";
        String SERVICE = "service";
        String SERVICE_TOKEN = "service_token";
        String SERVICE_ID = "service_id";
        String ADVERT_CITY_ID = "city_id";
        String LIMIT = "limit";
        String OFFSET = "offset";
        String MESSAGE_ID = "message_id";
        String STATUS = "status";
        String AD_ID = "ad_id";
        String FILE_PATH = "file_path";
        String SEARCH_QUERY_PARAMETER = "q";
        String ONLY_PRICE = "only_price";
        String CURRENCY_ID = "currency_id";

        String PATH = "path";
        String FILTERS_NAMES = "filters_names";

        String ADVERT_TYPE = "type";
        String ADVERT_FILTERS = "f";
        String ADVERT_FREE = "free";
        String ADVERT_EXCHANGE = "exch";
        String MIN = "price_min";
        String MAX = "price_max";
        String ADVERT_TITLE = "title";
        String ADVERT_DESCRIPTION = "description";
        String ADVERT_PRICE_TYPE = "price_type";
        String ADVERT_PRICE_TYPES = "price_types";
        String SEARCH_PRICE = "p";
        String ADVERT_PRICE = "price";
        String ADVERT_CURRENCY = "currency";
        String ADVERT_CURRENCY_ID = "currency_id";
        String ADVERT_COUNTRY_ID = "country_id";
        String ADVERT_REGION_ID = "region_id";
        String ADVERT_USED = "used";
        String ADVERT_BARGAIN = "bargain";
        String ADVERT_OPTIONS = "options";
        String RATE = "rate";
        String QUERY = "q";
        String INTERLOCUTOR_UID = "interlocutor_uid";
        String RECIPIENT_ID = "recipient_id";
        String TEXT = "text";
        String LOGIN_TYPE = "login_type";
        String USER_ID = "user_id";
        String COMMENT_ID = "comment_id";
        String GCM_ID = "reg_id";
        String DELETED_PHOTOS = "delete_photos";

        String COUNTRY_ID = "country_id";
        String REGION_ID = "region_id";
        String CITY_NAME = "city_name";
        String LOCALE = "locale";
        String ADVERT_PHONES = "phones";
        String ACTION = "action";
        String REFRESH_TOKEN = "refresh_token";
        String DEVICE_ID = "device_id";

        String ADVERT_PRICE_ARRAY = "price_types[%d]";
        String FILTERS_ARRAY_ITEM = "f[%d]";
    }

}
