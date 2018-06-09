package com.stockroompro.utils;

import android.os.Bundle;
import android.util.Log;

import com.stockroompro.StockRoomBundleBuilderArgs;
import com.stockroompro.api.models.requests.RequestParams;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.columns.AdvertFiltersColumns;
import com.stockroompro.models.columns.AdvertisementColumns;
import com.stockroompro.models.contracts.AdvertFiltersContract;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.contracts.FilterValuesContract;

import java.util.ArrayList;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_CITY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_TYPE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_USED;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.LIMIT;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.OFFSET;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.QUERY;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.REGION_ID;

/**
 * Created by Александр on 17.09.2016.
 */
public class AdvertUtils {

    private static final String PRICE_LOW = "PRICE_LOW";
    private static final String PRICE_HIGH = "PRICE_HIGH";
    private static final String FILTER_NEW = "FILTER_NEW";
    private static final String FILTER_USED = "FILTER_USED";
    private static final String FILTER_BUY = "FILTER_BUY";

    /**
     * Type of advertisement
     */
    private static final String FILTER_SELL = "FILTER_SELL";

    /**
     * Type of prices
     */
    private static final String FILTER_SALE = "FILTER_SALE";
    private static final String FILTER_FREE = "FILTER_FREE";
    private static final String FILTER_EXCHANGE = "FILTER_EXCHANGE";
    private static final String FILTER_CITY = "FILTER_CITY";
    private static final String FILTER_MORE_FIELDS = "FILTER_MORE_FIELDS";

    public static String getOrder(Bundle bundle) {
        StringBuilder appendingSQL = new StringBuilder();
        if (bundle != null) {
            if (bundle.getBoolean(StockRoomBundleBuilderArgs.ORDER_BY_LAST_ADDED)) {
                appendingSQL.append(AdvertisementContract.CREATED_AT);
                appendingSQL.append(" DESC");
            } else if (bundle.getBoolean(StockRoomBundleBuilderArgs.ORDER_BY_PRICE_ASC)) {
                appendingSQL.append(AdvertisementContract.INDEPENDENT_PRICE);
                appendingSQL.append(" ASC");
            } else if (bundle.getBoolean(StockRoomBundleBuilderArgs.ORDER_BY_PRICE_DESC)) {
                appendingSQL.append(AdvertisementContract.INDEPENDENT_PRICE);
                appendingSQL.append(" DESC");
            } else if (bundle.getBoolean(StockRoomBundleBuilderArgs.ORDER_BY_RATING)) {
                appendingSQL.append(AdvertisementContract.CREATED_AT);
                appendingSQL.append(" DESC, ");
                appendingSQL.append(AdvertisementContract.PRICE);
                appendingSQL.append(" ASC");
            } else {
                appendingSQL.append(AdvertisementContract.CREATED_AT);
                appendingSQL.append(" DESC");
            }
        } else {
            appendingSQL.append(AdvertisementContract.CREATED_AT);
            appendingSQL.append(" DESC");
        }
        return appendingSQL.toString();
    }

    public static String[] buildSelectionArgs(Bundle args) {
        ArrayList<String> list = new ArrayList<>();
        list.add(String.valueOf(args.getLong(RequestParams.ParamNames.CATEGORY_ID)));

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_NEW, false)) {
            list.add(String.valueOf(AdvertisementContract.TYPE_NEW));
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_USED, false)) {
            list.add(String.valueOf(AdvertisementContract.TYPE_USED));
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_BUY, false)) {
            list.add(String.valueOf(Advertisement.ADVERT_TYPE_BUY));
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_SELL, false)) {
            list.add(String.valueOf(Advertisement.ADVERT_TYPE_SELL));
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_EXCHANGE, false)) {
            list.add(String.valueOf(Advertisement.PRICE_TYPE_EXCHANGE));
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_FREE, false)) {
            list.add(String.valueOf(Advertisement.PRICE_TYPE_FREE));
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_SALE, false)) {
            list.add(String.valueOf(Advertisement.PRICE_TYPE_SELL));
        }

        if (args.getString(QUERY, null) != null) {
            list.add(String.valueOf("%" + args.getString(QUERY) + "%"));
        }

        if (args.getLong(COUNTRY_ID, -1l) != -1l) {
            list.add(String.valueOf(args.getLong(COUNTRY_ID)));
        }

        if (args.getLong(REGION_ID, -1l) != -1l) {
            list.add(String.valueOf(args.getLong(REGION_ID)));
        }

        if (args.getLong(ADVERT_CITY_ID, -1l) != -1l) {
            list.add(String.valueOf(args.getLong(ADVERT_CITY_ID)));
        }

        return list.toArray(new String[list.size()]);
    }

    public static String buildSelection(Bundle args) {
        StringBuilder appendingSQL = new StringBuilder();
        appendingSQL.append(AdvertisementColumns.CATEGORY_ID);
        appendingSQL.append("=?");

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_NEW, false)) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementColumns.USED);
            appendingSQL.append("=?");
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_USED, false)) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementColumns.USED);
            appendingSQL.append("=?");
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_BUY, false)) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementColumns.TYPE);
            appendingSQL.append("=?");
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_SELL, false)) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementColumns.TYPE);
            appendingSQL.append("=?");
        }

        StringBuilder sqlExchangeSaleFree = new StringBuilder();

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_EXCHANGE, false)
                || args.getBoolean(StockRoomBundleBuilderArgs.FILTER_FREE, false)
                || args.getBoolean(StockRoomBundleBuilderArgs.FILTER_SALE, false)) {
            if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_EXCHANGE, false)) {
                sqlExchangeSaleFree.append(" AND ( ");
                sqlExchangeSaleFree.append(AdvertisementColumns.PRICE_TYPE);
                sqlExchangeSaleFree.append("=?");
            }

            if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_FREE, false)) {
                if (sqlExchangeSaleFree.length() == 0) {
                    sqlExchangeSaleFree.append(" AND ( ");
                } else {
                    sqlExchangeSaleFree.append(" OR ");
                }
                sqlExchangeSaleFree.append(AdvertisementColumns.PRICE_TYPE);
                sqlExchangeSaleFree.append("=?");
            }

            if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_SALE, false)) {
                if (sqlExchangeSaleFree.length() == 0) {
                    sqlExchangeSaleFree.append(" AND ( ");
                } else {
                    sqlExchangeSaleFree.append(" OR ");
                }
                sqlExchangeSaleFree.append(AdvertisementColumns.PRICE_TYPE);
                sqlExchangeSaleFree.append("=?");
            }
            sqlExchangeSaleFree.append(" )");
            appendingSQL.append(sqlExchangeSaleFree);
        }

        if (!args.getBoolean(StockRoomBundleBuilderArgs.FILTER_FREE, false)) {
            if (args.getDouble(StockRoomBundleBuilderArgs.PRICE_LOW, -1d) != -1d) {
                appendingSQL.append(" AND ");
                appendingSQL.append(AdvertisementContract.INDEPENDENT_PRICE);
                appendingSQL.append(">=");
                appendingSQL.append(args.getDouble(StockRoomBundleBuilderArgs.PRICE_LOW, -1d));
            }

            if (args.getDouble(StockRoomBundleBuilderArgs.PRICE_HIGH, -1d) != -1d) {
                appendingSQL.append(" AND ");
                appendingSQL.append(AdvertisementContract.INDEPENDENT_PRICE);
                appendingSQL.append("<=");
                appendingSQL.append(args.getDouble(StockRoomBundleBuilderArgs.PRICE_HIGH, -1d));
            }
        }

        if (args.getString(QUERY, null) != null) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementContract.TITLE);
            appendingSQL.append(" LIKE ? ");
            appendingSQL.append(" OR ");
            appendingSQL.append(AdvertisementContract.DESCRIPTION);
            appendingSQL.append(" LIKE ?");
        }

        if (args.getLong(COUNTRY_ID, -1l) != -1l) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementContract.COUNTRY_ID);
            appendingSQL.append(" = ? ");
        }

        if (args.getLong(REGION_ID, -1l) != -1l) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementContract.REGION_ID);
            appendingSQL.append(" = ? ");
        }

        if (args.getLong(ADVERT_CITY_ID, -1l) != -1l) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementContract.CITY_ID);
            appendingSQL.append(" = ? ");
        }

        ArrayList<Integer> moreFilters = args.getIntegerArrayList(StockRoomBundleBuilderArgs.FILTER_MORE_FIELDS);
        if (moreFilters != null && moreFilters.size() > 0) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementColumns.ID);
            appendingSQL.append(" in (SELECT ");
            appendingSQL.append(AdvertFiltersColumns.ADVERT_ID);
            appendingSQL.append(" from ");
            appendingSQL.append(AdvertFiltersContract.TABLE_NAME);
            appendingSQL.append(" where ");
            appendingSQL.append(AdvertFiltersContract.ID);
            appendingSQL.append(" in (SELECT ");
            appendingSQL.append(buildSelectionString(moreFilters));
            appendingSQL.append(")");
        }
        return appendingSQL.toString();
    }

    private static String buildSelectionString(ArrayList<Integer> filters) {
        StringBuilder builder = new StringBuilder();
        builder.append(FilterValuesContract._ID);
        builder.append(" from ");
        builder.append(FilterValuesContract.TABLE_NAME);
        builder.append(" where ");
        builder.append(FilterValuesContract.ID);
        builder.append(" in (");
        for (int i = 0; i < filters.size(); i++) {
            builder.append(filters.get(i));
            if (i < filters.size() - 1) {
                builder.append(",");
            } else {
                builder.append("))");
            }
        }
        return builder.toString();
    }

    public static RequestParams createParams(Bundle bundle) {
        RequestParams params = new RequestParams();

        if (bundle.getInt(LIMIT, -1) != -1) {
            params.addParameter(LIMIT, bundle.getInt(LIMIT));
        }

        if (bundle.getInt(OFFSET, -1) != -1) {
            params.addParameter(OFFSET, bundle.getInt(OFFSET));
        }

        if (bundle.getLong(CATEGORY_ID, -1L) != -1L) {
            params.addParameter(CATEGORY_ID, bundle.getLong(CATEGORY_ID));
        }

        if (bundle.getLong(COUNTRY_ID, 0) != 0) {
            params.addParameter(COUNTRY_ID, bundle.getLong(COUNTRY_ID, 0));
        }

        if (bundle.getLong(REGION_ID, 0) != 0) {
            params.addParameter(REGION_ID, bundle.getLong(REGION_ID, 0));
        }

        if (bundle.getLong(ADVERT_CITY_ID, 0) != 0) {
            params.addParameter(ADVERT_CITY_ID, bundle.getLong(ADVERT_CITY_ID, 0));
        }

        if (bundle.getBoolean(FILTER_NEW, false)) {
            params.addParameter(ADVERT_USED, AdvertisementContract.TYPE_NEW);
        } else if (bundle.getBoolean(FILTER_USED, false)) {
            params.addParameter(ADVERT_USED, AdvertisementContract.TYPE_USED);
        }

        if (bundle.getBoolean(FILTER_BUY, false)) {
            params.addParameter(ADVERT_TYPE, Advertisement.ADVERT_TYPE_BUY);
        } else if (bundle.getBoolean(FILTER_SELL, false)) {
            params.addParameter(ADVERT_TYPE, Advertisement.ADVERT_TYPE_SELL);
        }

        ArrayList<Integer> priceTypes = new ArrayList<>(1);
        if (bundle.getBoolean(FILTER_EXCHANGE, false)) {
            priceTypes.add(Advertisement.PRICE_TYPE_EXCHANGE);
        }

        if (bundle.getBoolean(FILTER_FREE, false)) {
            priceTypes.add(Advertisement.PRICE_TYPE_FREE);
        }

        if (bundle.getBoolean(FILTER_SALE, false)) {
            priceTypes.add(Advertisement.PRICE_TYPE_SELL);
        }

        if (priceTypes.size() > 0 && priceTypes.size() < 3/*Max count of price types*/) {
            params.addParameter(RequestParams.ParamNames.ADVERT_PRICE_TYPES, priceTypes);
        }

        if (bundle.getDouble(PRICE_LOW, -1d) != -1d && bundle.getDouble(PRICE_HIGH, -1d) != -1d) {
            params.addParameter(RequestParams.ParamNames.MIN, (int) bundle.getDouble(PRICE_LOW, -1d));
            params.addParameter(RequestParams.ParamNames.MAX, (int) bundle.getDouble(PRICE_HIGH, -1d));
        }

        if (bundle.getString(QUERY, null) != null) {
            params.addParameter(QUERY, bundle.getString(QUERY));
        }

        ArrayList<Integer> moreFilters = bundle.getIntegerArrayList(FILTER_MORE_FIELDS);
        if (moreFilters != null && moreFilters.size() > 0) {
            for (int i = 0; i < moreFilters.size(); i++) {
                params.addParameter(RequestParams.ParamNames.ADVERT_FILTERS, moreFilters);
            }
        }
        return params;
    }

    public static String selection(ArrayList<Long> arrayList) {
        StringBuilder builder = new StringBuilder();
        builder.append(AdvertisementContract.ID);
        builder.append(" IN (");
        for (int i = 0; i < arrayList.size(); i++) {
            builder.append("?");
            if (i == arrayList.size() - 1) {
                builder.append(")");
            } else {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    public static String[] buildSelectionArgs(ArrayList<Long> arrayList) {
        String[] selectionArgs = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            selectionArgs[i] = String.valueOf(arrayList.get(i));
        }
        return selectionArgs;
    }

}
