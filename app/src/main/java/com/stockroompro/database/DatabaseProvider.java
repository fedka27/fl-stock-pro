package com.stockroompro.database;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.artjoker.core.BackgroundUtils;
import com.artjoker.core.network.ButchUpdateArrayDatabaseRequest;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.ResponseItemHolder;
import com.artjoker.core.network.StatusCode;
import com.artjoker.database.SecureDatabaseProvider;
import com.artjoker.database.SelectionBuilder;
import com.artjoker.tool.core.Notification;
import com.stockroompro.BasicApplication;
import com.stockroompro.R;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.models.requests.AdvertisementRequest;
import com.stockroompro.api.models.requests.RequestParams;
import com.stockroompro.fragments.MessagesByUserFragment;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.columns.AdvertisementColumns;
import com.stockroompro.models.columns.CityColumns;
import com.stockroompro.models.columns.MessageByAdvertColumns;
import com.stockroompro.models.columns.UserDataColumns;
import com.stockroompro.models.contracts.AdvertFiltersContract;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.contracts.CategoryContract;
import com.stockroompro.models.contracts.CategoryFiltersContract;
import com.stockroompro.models.contracts.ChatMessageContract;
import com.stockroompro.models.contracts.CityContract;
import com.stockroompro.models.contracts.CommentsContract;
import com.stockroompro.models.contracts.CountryContract;
import com.stockroompro.models.contracts.FilterValuesContract;
import com.stockroompro.models.contracts.MessageByAdvertContract;
import com.stockroompro.models.contracts.MessageByUserContract;
import com.stockroompro.models.contracts.NotificationContract;
import com.stockroompro.models.contracts.PhotosContract;
import com.stockroompro.models.contracts.RegionContract;
import com.stockroompro.models.contracts.UserDataContract;
import com.stockroompro.models.converters.CityContentValuesConverter;
import com.stockroompro.models.location.City;
import com.stockroompro.utils.ContentProviderConfig;

import java.net.UnknownHostException;
import java.util.ArrayList;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_CITY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.LIMIT;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.REGION_ID;

public final class DatabaseProvider extends SecureDatabaseProvider {
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, CategoryContract.TABLE_NAME, Config.CATEGORY_DIR_ID, Config.CATEGORY_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, AdvertisementContract.TABLE_NAME, Config.ADVERTISEMENT_DIR_ID, Config.ADVERTISEMENT_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, CommentsContract.TABLE_NAME, Config.COMMENTS_DIR_ID, Config.COMMENTS_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, PhotosContract.TABLE_NAME, Config.PHOTOS_DIR_ID, Config.PHOTOS_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, NotificationContract.TABLE_NAME, Config.NOTIFICATION_DIR_ID, Config.NOTIFICATION_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, MessageByUserContract.TABLE_NAME, Config.MESSAGE_BY_USER_DIR_ID, Config.MESSAGE_BY_USER_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, MessageByAdvertContract.TABLE_NAME, Config.MESSAGE_BY_ADVERT_DIR_ID, Config.MESSAGE_BY_ADVERT_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, ChatMessageContract.TABLE_NAME, Config.MESSAGE_CHAT_DIR_ID, Config.MESSAGE_CHAT_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, CategoryFiltersContract.TABLE_NAME, Config.CATEGORY_FILTERS_DIR_ID, Config.CATEGORY_FILTERS_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, FilterValuesContract.TABLE_NAME, Config.FILTER_VALUES_DIR_ID, Config.FILTER_VALUES_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, UserDataContract.TABLE_NAME, Config.USER_DATA_DIR_ID, Config.USER_DATA_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, CountryContract.TABLE_NAME, Config.COUNTRY_DIR_ID, Config.COUNTRY_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, RegionContract.TABLE_NAME, Config.REGION_DIR_ID, Config.REGION_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, CityContract.TABLE_NAME, Config.CITY_DIR_ID, Config.CITY_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, AdvertFiltersContract.TABLE_NAME, Config.ADVERTISEMENT_FILTERS_DIR_ID, Config.ADVERTISEMENT_FILTERS_ITEM_ID);

        URI_MATCHER.addURI(ContentProviderConfig.AUTHORITY_CONTENT, CategoryContract.SEARCH_BY_ADVERT_SUGGEST_URI, Config.SEARCH_BY_ADVERT_ITEM_DIR);
        URI_MATCHER.addURI(ContentProviderConfig.AUTHORITY_CONTENT, CategoryContract.SEARCH_BY_USERS_SUGGEST_URI, Config.SEARCH_BY_USERS_ITEM_DIR);
        URI_MATCHER.addURI(ContentProviderConfig.AUTHORITY_CONTENT, CityContract.SEARCH_CITY_URI, Config.SEARCH_CITY_DIR);
        URI_MATCHER.addURI(ContentProviderConfig.AUTHORITY_CONTENT, MessageByUserContract.SEARCH_USER_URI, Config.SEARCH_USER_DIR);
        URI_MATCHER.addURI(ContentProviderConfig.AUTHORITY_CONTENT, MessageByAdvertContract.SEARCH_MESSAGES_URI, Config.SEARCH_MESSAGES_DIR);
        URI_MATCHER.addURI(ContentProviderConfig.AUTHORITY_CONTENT, AdvertisementContract.SEARCH_ADVERT_URI, Config.SEARCH_ADVERT_DIR);
    }

    @Override
    protected SQLiteOpenHelper getNewDatabaseHelper() {
        return new DatabaseHelper(getContext());
    }

    @Override
    protected SelectionBuilder getSimpleSelectionBuilder(Uri uri, String selection, String[] selectionArgs) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (URI_MATCHER.match(uri)) {
            case Config.CATEGORY_DIR_ID:
                appendSelection(builder, CategoryContract.TABLE_NAME, selection, selectionArgs);
                break;

            case Config.ADVERTISEMENT_DIR_ID:
                appendSelection(builder, AdvertisementContract.TABLE_NAME, selection, selectionArgs);
                break;

            case Config.COMMENTS_DIR_ID:
                appendSelection(builder, CommentsContract.TABLE_NAME, selection, selectionArgs);
                break;

            case Config.PHOTOS_DIR_ID:
                appendSelection(builder, PhotosContract.TABLE_NAME, selection, selectionArgs);
                break;

            case Config.MESSAGE_BY_USER_DIR_ID:
                appendSelection(builder, MessageByUserContract.TABLE_NAME, selection, selectionArgs);
                break;

            case Config.MESSAGE_BY_ADVERT_DIR_ID:
                appendSelection(builder, MessageByAdvertContract.TABLE_NAME, selection, selectionArgs);
                break;

            case Config.MESSAGE_CHAT_DIR_ID:
                appendSelection(builder, ChatMessageContract.TABLE_NAME, selection, selectionArgs);
                break;

            case Config.NOTIFICATION_DIR_ID:
                appendSelection(builder, NotificationContract.TABLE_NAME, selection, selectionArgs);
                break;

            case Config.CATEGORY_FILTERS_DIR_ID:
                appendSelection(builder, CategoryFiltersContract.TABLE_NAME, selection, selectionArgs);
                break;

            case Config.FILTER_VALUES_DIR_ID:
                appendSelection(builder, FilterValuesContract.TABLE_NAME, selection, selectionArgs);
                break;

            case Config.USER_DATA_DIR_ID:
                appendSelection(builder, UserDataContract.TABLE_NAME, selection, selectionArgs);
                break;

            case Config.COUNTRY_DIR_ID:
                appendSelection(builder, CountryContract.TABLE_NAME, selection, selectionArgs);
                break;

            case Config.REGION_DIR_ID:
                appendSelection(builder, RegionContract.TABLE_NAME, selection, selectionArgs);
                break;

            case Config.CITY_DIR_ID:
                appendSelection(builder, CityContract.TABLE_NAME, selection, selectionArgs);
                break;

            case Config.ADVERTISEMENT_FILTERS_DIR_ID:
                appendSelection(builder, AdvertFiltersContract.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("unknown uri: " + uri);
        }
        return builder;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase database = getInstance();
        Uri contentUri;
        String tableName;
        switch (URI_MATCHER.match(uri)) {
            case Config.CATEGORY_DIR_ID:
                contentUri = CategoryContract.CONTENT_URI;
                tableName = CategoryContract.TABLE_NAME;
                break;

            case Config.ADVERTISEMENT_DIR_ID:
                contentUri = AdvertisementContract.CONTENT_URI;
                tableName = AdvertisementContract.TABLE_NAME;
                break;

            case Config.COMMENTS_DIR_ID:
                contentUri = CommentsContract.CONTENT_URI;
                tableName = CommentsContract.TABLE_NAME;
                break;

            case Config.PHOTOS_DIR_ID:
                contentUri = PhotosContract.CONTENT_URI;
                tableName = PhotosContract.TABLE_NAME;
                break;

            case Config.NOTIFICATION_DIR_ID:
                contentUri = NotificationContract.CONTENT_URI;
                tableName = NotificationContract.TABLE_NAME;
                break;

            case Config.MESSAGE_BY_USER_DIR_ID:
                contentUri = MessageByUserContract.CONTENT_URI;
                tableName = MessageByUserContract.TABLE_NAME;
                break;

            case Config.MESSAGE_BY_ADVERT_DIR_ID:
                contentUri = MessageByAdvertContract.CONTENT_URI;
                tableName = MessageByAdvertContract.TABLE_NAME;
                break;

            case Config.MESSAGE_CHAT_DIR_ID:
                contentUri = ChatMessageContract.CONTENT_URI;
                tableName = ChatMessageContract.TABLE_NAME;
                break;

            case Config.CATEGORY_FILTERS_DIR_ID:
                contentUri = CategoryFiltersContract.CONTENT_URI;
                tableName = CategoryFiltersContract.TABLE_NAME;
                break;

            case Config.FILTER_VALUES_DIR_ID:
                contentUri = FilterValuesContract.CONTENT_URI;
                tableName = FilterValuesContract.TABLE_NAME;
                break;

            case Config.USER_DATA_DIR_ID:
                contentUri = UserDataContract.CONTENT_URI;
                tableName = UserDataContract.TABLE_NAME;
                break;

            case Config.COUNTRY_DIR_ID:
                contentUri = CountryContract.CONTENT_URI;
                tableName = CountryContract.TABLE_NAME;
                break;

            case Config.REGION_DIR_ID:
                contentUri = RegionContract.CONTENT_URI;
                tableName = RegionContract.TABLE_NAME;
                break;

            case Config.CITY_DIR_ID:
                contentUri = CityContract.CONTENT_URI;
                tableName = CityContract.TABLE_NAME;
                break;

            case Config.ADVERTISEMENT_FILTERS_DIR_ID:
                contentUri = AdvertFiltersContract.CONTENT_URI;
                tableName = AdvertFiltersContract.TABLE_NAME;
                break;

            default:
                throw new IllegalArgumentException("unknown Uri: " + uri);
        }
        final Uri insertedUri = buildInsertUri(database, contentUri, tableName, contentValues);
        getContext().getContentResolver().notifyChange(insertedUri, null);
        return insertedUri;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        switch (URI_MATCHER.match(uri)) {
            case Config.CATEGORY_DIR_ID:

                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);
                }
                break;

            case Config.CATEGORY_ITEM_ID:
                break;

            case Config.ADVERTISEMENT_DIR_ID:

                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);
                }
                break;

            case Config.ADVERTISEMENT_ITEM_ID:
                break;

            case Config.COMMENTS_DIR_ID:

                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);
                }
                break;

            case Config.COMMENTS_ITEM_ID:
                break;

            case Config.PHOTOS_DIR_ID:

                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);
                }
                break;

            case Config.PHOTOS_ITEM_ID:
                break;

            case Config.NOTIFICATION_DIR_ID:

                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);
                }
                break;

            case Config.NOTIFICATION_ITEM_ID:
                break;

            case Config.MESSAGE_BY_USER_DIR_ID:
                break;

            case Config.MESSAGE_BY_ADVERT_DIR_ID:
                break;

            case Config.CATEGORY_FILTERS_DIR_ID:

                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);
                }
                break;

            case Config.CATEGORY_FILTERS_ITEM_ID:
                break;

            case Config.FILTER_VALUES_ITEM_ID:

                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);
                }
                break;

            case Config.FILTER_VALUES_DIR_ID:
                break;

            case Config.USER_DATA_DIR_ID:

                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);
                }
                break;

            case Config.USER_DATA_ITEM_ID:
                break;
            case Config.MESSAGE_CHAT_DIR_ID:

                break;
            case Config.MESSAGE_CHAT_ITEM_ID:

                break;

            case Config.COUNTRY_DIR_ID:

                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);
                }
                break;

            case Config.COUNTRY_ITEM_ID:
                break;

            case Config.REGION_DIR_ID:

                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);
                }
                break;

            case Config.REGION_ITEM_ID:
                break;

            case Config.CITY_DIR_ID:

                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);
                }
                break;

            case Config.CITY_ITEM_ID:
                break;
            case Config.ADVERTISEMENT_FILTERS_DIR_ID:

                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);
                }
                break;

            case Config.ADVERTISEMENT_FILTERS_ITEM_ID:
                break;

            default:
                throw new IllegalArgumentException("unknown uri: " + uri);
        }
        final int count = insertOrUpdate(uri, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = getInstance();
        final int count = getSimpleSelectionBuilder(uri, selection, selectionArgs).delete(database);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case Config.CATEGORY_DIR_ID:
                return CategoryContract.CONTENT_TYPE;
            case Config.CATEGORY_ITEM_ID:
                return CategoryContract.CONTENT_ITEM_TYPE;

            case Config.ADVERTISEMENT_DIR_ID:
                return AdvertisementContract.CONTENT_TYPE;
            case Config.ADVERTISEMENT_ITEM_ID:
                return AdvertisementContract.CONTENT_ITEM_TYPE;

            case Config.COMMENTS_DIR_ID:
                return CommentsContract.CONTENT_TYPE;
            case Config.COMMENTS_ITEM_ID:
                return CommentsContract.CONTENT_ITEM_TYPE;

            case Config.PHOTOS_DIR_ID:
                return PhotosContract.CONTENT_TYPE;
            case Config.PHOTOS_ITEM_ID:
                return PhotosContract.CONTENT_ITEM_TYPE;

            case Config.NOTIFICATION_DIR_ID:
                return NotificationContract.CONTENT_TYPE;
            case Config.NOTIFICATION_ITEM_ID:
                return NotificationContract.CONTENT_ITEM_TYPE;

            case Config.MESSAGE_CHAT_ITEM_ID:
                return ChatMessageContract.CONTENT_ITEM_TYPE;
            case Config.MESSAGE_CHAT_DIR_ID:
                return ChatMessageContract.CONTENT_TYPE;

            case Config.MESSAGE_BY_USER_ITEM_ID:
                return MessageByUserContract.CONTENT_ITEM_TYPE;
            case Config.MESSAGE_BY_USER_DIR_ID:
                return MessageByUserContract.CONTENT_TYPE;

            case Config.MESSAGE_BY_ADVERT_ITEM_ID:
                return MessageByAdvertContract.CONTENT_ITEM_TYPE;
            case Config.MESSAGE_BY_ADVERT_DIR_ID:
                return MessageByAdvertContract.CONTENT_TYPE;

            case Config.CATEGORY_FILTERS_ITEM_ID:
                return CategoryFiltersContract.CONTENT_ITEM_TYPE;
            case Config.CATEGORY_FILTERS_DIR_ID:
                return CategoryFiltersContract.CONTENT_TYPE;

            case Config.FILTER_VALUES_ITEM_ID:
                return CategoryFiltersContract.CONTENT_ITEM_TYPE;
            case Config.FILTER_VALUES_DIR_ID:
                return CategoryFiltersContract.CONTENT_TYPE;

            case Config.USER_DATA_ITEM_ID:
                return UserDataContract.CONTENT_ITEM_TYPE;
            case Config.USER_DATA_DIR_ID:
                return UserDataContract.CONTENT_TYPE;

            case Config.ADVERTISEMENT_FILTERS_DIR_ID:
                return AdvertFiltersContract.CONTENT_TYPE;
            case Config.ADVERTISEMENT_FILTERS_ITEM_ID:
                return AdvertFiltersContract.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("unknown Uri: " + uri);
        }
    }

    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        final SQLiteDatabase database = getInstance();

        switch (URI_MATCHER.match(uri)) {
            case Config.SEARCH_BY_ADVERT_ITEM_DIR:
                return searchAdvertisement(selectionArgs);

            case Config.SEARCH_BY_USERS_ITEM_DIR:
                return searchUsers(selectionArgs);

            case Config.SEARCH_CITY_DIR:
                return searchCities(selectionArgs);

            case Config.SEARCH_USER_DIR:
                return searchUsersByMessage(selectionArgs);

            case Config.SEARCH_MESSAGES_DIR:
                return searchMessages(selectionArgs);

            case Config.SEARCH_ADVERT_DIR:
                return searchAdvert(selectionArgs);

            default:
                final Cursor cursor = getSimpleSelectionBuilder(uri, selection, selectionArgs).query(database, projection, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
        }
    }

    private Cursor searchUsers(String[] selectionArgs) {
        if (selectionArgs != null) {
            String queryString = selectionArgs[0];
            Long countryId = Long.valueOf(selectionArgs[1]);
            Long regionId = Long.valueOf(selectionArgs[2]);
            Long cityId = Long.valueOf(selectionArgs[3]);
            if (!queryString.isEmpty()) {
                try {
                    BackgroundUtils.requestsCountChanged(getContext(), true);
                    String[] args = new String[]{UserDataColumns._ID,
                            UserDataColumns.ID,
                            UserDataColumns.FIRST_NAME,
                            UserDataColumns.LAST_NAME,
                            UserDataColumns.PICTURE_URL};
                    RequestParams params = new RequestParams();
                    params.addParameter(RequestParams.ParamNames.SEARCH_QUERY_PARAMETER, queryString);
                    params.addParameter(LIMIT, 100);
                    if (countryId != 0) {
                        params.addParameter(COUNTRY_ID, countryId);
                    }
                    if (regionId != 0) {
                        params.addParameter(REGION_ID, regionId);
                    }
                    if (cityId != 0) {
                        params.addParameter(ADVERT_CITY_ID, cityId);
                    }
                    ResponseHolder<ResponseItemHolder<com.stockroompro.api.models.responses.user.UserData>> response = Communicator.getAppServer().searchUsers(
                            params.buildJSON(AppServerSpecs.SEARCH_USERS_PATH));
                    if (response != null && response.getStatusCode() == StatusCode.RESPONSE_SUCCESS) {

                        MatrixCursor matrixCursor = new MatrixCursor(args);
                        for (int i = 0; i < response.getData().getAmount(); i++) {
                            com.stockroompro.api.models.responses.user.UserData userData = response.getData().getItems().get(i);
                            matrixCursor.newRow()
                                    .add(i)
                                    .add(userData.getId())
                                    .add(userData.getFirstName())
                                    .add(userData.getLastName())
                                    .add(userData.getPictureUrl());
                        }
                        return matrixCursor;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    BackgroundUtils.requestsCountChanged(getContext(), false);
                }
            }
            return null;
        } else return null;
    }

    private Cursor searchAdvertisement(final String[] selectionArgs) {
        if (selectionArgs != null) {
            String queryString = selectionArgs[0];
            long countryId = Long.valueOf(selectionArgs[1]);
            long regionId = Long.valueOf(selectionArgs[2]);
            long cityId = Long.valueOf(selectionArgs[3]);
            long categoryId = Long.valueOf(selectionArgs[4]);
            if (!queryString.isEmpty()) {
                try {
                    BackgroundUtils.requestsCountChanged(getContext(), true);
                    String[] args = new String[]{AdvertisementColumns._ID, AdvertisementColumns.ID,
                            AdvertisementColumns.USER_ID, AdvertisementColumns.TITLE,
                            AdvertisementColumns.PRICE, AdvertisementColumns.CURRENCY_ID,
                            AdvertisementColumns.COUNTRY_NAME, AdvertisementColumns.CITY_NAME,
                            AdvertisementColumns.PHOTO_URL, AdvertisementColumns.TYPE};
                    RequestParams params = new RequestParams();
                    params.addParameter(RequestParams.ParamNames.SEARCH_QUERY_PARAMETER, queryString);
                    if (categoryId != 0) {
                        params.addParameter(CATEGORY_ID, categoryId);
                    }
                    if (countryId != 0) {
                        params.addParameter(COUNTRY_ID, countryId);
                    }
                    if (regionId != 0) {
                        params.addParameter(REGION_ID, regionId);
                    }
                    if (cityId != 0) {
                        params.addParameter(ADVERT_CITY_ID, cityId);
                    }
                    ResponseHolder<ResponseItemHolder<Advertisement>> response = Communicator.getAppServer().getAdvertsByCategoryIdAndText(
                            params.buildJSON(AppServerSpecs.ADVERTISEMENT_PATH));
                    if (response != null && response.getStatusCode() == StatusCode.RESPONSE_SUCCESS) {
                        Bundle bundle = new Bundle();
                        bundle.putLong(CATEGORY_ID, categoryId);
                        AdvertisementRequest request = new AdvertisementRequest(getContext(), bundle);
                        request.processResponse(response);

                        MatrixCursor matrixCursor = new MatrixCursor(args);
                        for (int i = 0; i < response.getData().getAmount(); i++) {
                            Advertisement advertisement = response.getData().getItems().get(i);
                            matrixCursor.newRow()
                                    .add(i)
                                    .add(advertisement.getId())
                                    .add(advertisement.getUserId())
                                    .add(advertisement.getTitle())
                                    .add(advertisement.getPrice())
                                    .add(advertisement.getCurrencyId())
                                    .add(advertisement.getCountryName())
                                    .add(advertisement.getCityName())
                                    .add(advertisement.getPhotos().size() > 0 ? advertisement.getPhotos().get(0) : "null")
                                    .add(advertisement.getType());
                        }
                        return matrixCursor;
                    }
                } catch (UnknownHostException err) {
                    err.printStackTrace();
                    Notification.getInstance().show(getContext(), R.string.error_internet_connection);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    BackgroundUtils.requestsCountChanged(getContext(), false);
                }
            }
            if (categoryId == 0) {
                return query(AdvertisementContract.CONTENT_URI, null,
                        Config.SELECTION_SEARCH_ADVERTISEMENTS_BY_TITLE, new String[]{"%" + queryString + "%"}, null);
            } else {
                return query(AdvertisementContract.CONTENT_URI, null,
                        buildSearchAdvertSelection(countryId, regionId, cityId),
                        buildSearchAdvertSelectionArgs(queryString, categoryId, countryId, regionId, cityId), null);
            }
        } else return null;
    }

    private String buildSearchAdvertSelection(long countryId, long regionId, long cityId) {
        StringBuilder builder = new StringBuilder();
        builder.append(AdvertisementColumns.CATEGORY_ID);
        builder.append(" = ?");
        if (countryId != 0) {
            builder.append(" AND " );
            builder.append(AdvertisementColumns.COUNTRY_ID);
            builder.append(" = ?");
        }
        if (regionId != 0) {
            builder.append(" AND " );
            builder.append(AdvertisementColumns.REGION_ID);
            builder.append(" = ?");
        }
        if (cityId != 0) {
            builder.append(" AND " );
            builder.append( AdvertisementColumns.CITY_ID);
            builder.append(" = ?");
        }

        return builder.toString();
    }

    private String[] buildSearchAdvertSelectionArgs(String query, long categoryId, long countryId, long regionId, long cityId) {
        ArrayList<String> list = new ArrayList<>();
        list.add(String.valueOf(categoryId));
        if (countryId != 0) {
            list.add(String.valueOf(countryId));
        }
        if (regionId != 0) {
            list.add(String.valueOf(regionId));
        }
        if (cityId != 0) {
            list.add(String.valueOf(cityId));
        }

        return list.toArray(new String[]{});
    }

    private Cursor searchCities(final String[] selectionArgs) {
        if (selectionArgs != null) {
            String queryString = selectionArgs[0];
            Long categoryId = Long.valueOf(selectionArgs[1]);
            String[] args = new String[]{CityColumns._ID, CityColumns.ID, CityColumns.NAME, CityColumns.REGION_ID, CityColumns.PARENT_ID, CityColumns.CREATED_AT, CityColumns.UPDATED_AT};
            if (!queryString.isEmpty()) {
                try {
                    BackgroundUtils.requestsCountChanged(getContext(), true);
                    RequestParams params = new RequestParams();
                    params.addParameter(RequestParams.ParamNames.SEARCH_QUERY_PARAMETER, queryString);
                    if (categoryId != 0) {
                        params.addParameter(CATEGORY_ID, categoryId);
                    }
                    ResponseHolder<ResponseItemHolder<City>> response = Communicator.getAppServer().getSearchCities(params.buildJSON(AppServerSpecs.SEARCH_CITY_PATH));
                    if (response != null && response.getStatusCode() == StatusCode.RESPONSE_SUCCESS) {
                        ButchUpdateArrayDatabaseRequest<ResponseItemHolder<City>, CityContentValuesConverter> request = new ButchUpdateArrayDatabaseRequest<ResponseItemHolder<City>,
                                CityContentValuesConverter>(getContext(), CityContract.CONTENT_URI, CityContentValuesConverter.class, null) {
                            @Override
                            public ResponseHolder makeRequest(long date) throws Exception {
                                return null;
                            }

                            @Override
                            public int getRequestName() {
                                return 0;
                            }
                        };
                        request.processResponse(response);
                        MatrixCursor matrixCursor = new MatrixCursor(args);
                        for (int i = 0; i < response.getData().getItems().size(); i++) {
                            City city = response.getData().getItems().get(i);
                            matrixCursor.newRow()
                                    .add(i)
                                    .add(city.getId())
                                    .add(city.getName())
                                    .add(0)
                                    .add(0)
                                    .add(0)
                                    .add(0);
                        }
                        return matrixCursor;
                    }
                } catch (UnknownHostException err) {
                    err.printStackTrace();
                    Notification.getInstance().show(getContext(), R.string.error_internet_connection);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    BackgroundUtils.requestsCountChanged(getContext(), false);
                }
            }
            Cursor cursor;
            if (categoryId == 0) {
                cursor = query(CityContract.CONTENT_URI, null, null, null, null);
            } else {
                cursor = query(AdvertisementContract.CONTENT_URI,
                        new String[]{
                                "DISTINCT " + AdvertisementColumns.CITY_NAME + " as " + CityColumns.NAME,
                                " (SELECT " + CityColumns._ID + " FROM " + CityContract.TABLE_NAME + " WHERE " + CityColumns.ID + "=" + AdvertisementColumns.CITY_ID + ") as " + CityColumns._ID,
                                AdvertisementColumns.CITY_ID + " as " + CityColumns.ID,
                                AdvertisementColumns.CATEGORY_ID},
                        Config.SELECTION_SEARCH_CITY_BY_NAME_AND_CATEGORY,
                        new String[]{String.valueOf(categoryId)},
                        null);
            }
            if (cursor != null) {
                MatrixCursor matrixCursor = new MatrixCursor(args);
                int i = 0;
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    if (cursor.getString(cursor.getColumnIndex(CityContract.NAME)).toLowerCase().contains(queryString.toLowerCase())) {
                        matrixCursor.newRow()
                                .add(i++)
                                .add(cursor.getLong(cursor.getColumnIndex(CityContract.ID)))
                                .add(cursor.getString(cursor.getColumnIndex(CityContract.NAME)))
                                .add(0)
                                .add(0)
                                .add(0)
                                .add(0);
                    }
                }
                cursor.close();
                return matrixCursor;
            }
        }
        return null;
    }

    private Cursor searchUsersByMessage(String[] selectionArgs) {
        String[] args = new String[]{MessageByUserContract._ID,
                MessageByUserContract.ID,
                MessageByUserContract.TITLE,
                MessageByUserContract.SENDER_ID,
                MessageByUserContract.USER_ID,
                MessageByUserContract.AD_ID,
                MessageByUserContract.INTERLOCATOR_ID,
                MessageByUserContract.INTERLOCATOR_NAME,
                MessageByUserContract.RECIPIENT_ID,
                MessageByUserContract.NEW,
                MessageByUserContract.PRICE,
                MessageByUserContract.TYPE,
                MessageByUserContract.MESSAGE_TEXT,
                MessageByUserContract.USER_NAME,
                MessageByUserContract.DATE,
                MessageByUserContract.PARENT_ID,
                MessageByUserContract.CREATED_AT,
                MessageByUserContract.UPDATED_AT,
                MessageByUserContract.STATUS};
        Cursor cursor = null;
        String searchText = selectionArgs[0];
        long userId = Long.valueOf(selectionArgs[1]);

        cursor = query(MessageByUserContract.CONTENT_URI,
                null,
                MessageByUserContract.USER_ID + " = ? ",
                new String[]{String.valueOf(userId)},
                MessageByUserContract.DATE + " DESC");

        if (cursor != null) {
            MatrixCursor matrixCursor = new MatrixCursor(args);
            int i = 0;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex(MessageByUserContract.INTERLOCATOR_NAME)).toLowerCase().contains(searchText.toLowerCase())) {
                    matrixCursor.newRow()
                            .add(i++)
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByUserContract.ID)))
                            .add(cursor.getString(cursor.getColumnIndex(MessageByUserContract.TITLE)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByUserContract.SENDER_ID)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByUserContract.USER_ID)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByUserContract.AD_ID)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByUserContract.INTERLOCATOR_ID)))
                            .add(cursor.getString(cursor.getColumnIndex(MessageByUserContract.INTERLOCATOR_NAME)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByUserContract.RECIPIENT_ID)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByUserContract.NEW)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByUserContract.PRICE)))
                            .add(cursor.getString(cursor.getColumnIndex(MessageByUserContract.TYPE)))
                            .add(cursor.getString(cursor.getColumnIndex(MessageByUserContract.MESSAGE_TEXT)))
                            .add(cursor.getString(cursor.getColumnIndex(MessageByUserContract.USER_NAME)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByUserContract.DATE)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByUserContract.PARENT_ID)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByUserContract.CREATED_AT)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByUserContract.UPDATED_AT)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByUserContract.STATUS)));
                }
            }
            cursor.close();
            return matrixCursor;
        }
        return null;
    }

    private Cursor searchMessages(String[] selectionArgs) {
        String[] args = new String[]{MessageByUserContract._ID,
                MessageByAdvertContract.ID,
                MessageByAdvertContract.SENDER_ID,
                MessageByAdvertContract.USER_ID,
                MessageByAdvertContract.SENDER_NAME,
                MessageByAdvertContract.AD_ID,
                MessageByAdvertContract.RECIPIENT_ID,
                MessageByAdvertContract.INTERLOCATOR_NAME,
                MessageByAdvertContract.INTERLOCATOR_ID,
                MessageByAdvertContract.NEW,
                MessageByAdvertContract.TITLE,
                MessageByAdvertContract.TYPE,
                MessageByAdvertContract.MESSAGE_TEXT,
                MessageByAdvertContract.DATE,
                MessageByAdvertContract.STATUS,
                MessageByAdvertContract.PARENT_ID,
                MessageByAdvertContract.PRICE,
                MessageByAdvertContract.IMAGE,
                MessageByAdvertContract.CURRENCY_ID,
                MessageByAdvertContract.CREATED_AT,
                MessageByAdvertContract.UPDATED_AT};
        Cursor cursor = null;
        String searchText = selectionArgs[0];
        long userId = Long.valueOf(selectionArgs[1]);
        long myId = Long.valueOf(selectionArgs[2]);

        cursor = query(MessageByAdvertContract.CONTENT_URI,
                null,
                MessageByAdvertColumns.INTERLOCATOR_ID + " = ? AND " + MessageByAdvertColumns.USER_ID + " = ? ",
                new String[]{String.valueOf(userId), String.valueOf(myId)},
                MessageByAdvertColumns.DATE + " DESC");

        if (cursor != null) {
            MatrixCursor matrixCursor = new MatrixCursor(args);
            int i = 0;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex(MessageByAdvertContract.TITLE)).toLowerCase().contains(searchText.toLowerCase())) {
                    matrixCursor.newRow()
                            .add(i++)
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByAdvertContract.ID)))
                            .add(cursor.getString(cursor.getColumnIndex(MessageByAdvertContract.SENDER_ID)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByAdvertContract.USER_ID)))
                            .add(cursor.getString(cursor.getColumnIndex(MessageByAdvertContract.SENDER_NAME)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByAdvertContract.AD_ID)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByAdvertContract.RECIPIENT_ID)))
                            .add(cursor.getString(cursor.getColumnIndex(MessageByAdvertContract.INTERLOCATOR_NAME)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByAdvertContract.INTERLOCATOR_ID)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByAdvertContract.NEW)))
                            .add(cursor.getString(cursor.getColumnIndex(MessageByAdvertContract.TITLE)))
                            .add(cursor.getString(cursor.getColumnIndex(MessageByAdvertContract.TYPE)))
                            .add(cursor.getString(cursor.getColumnIndex(MessageByAdvertContract.MESSAGE_TEXT)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByAdvertContract.DATE)))
                            .add(cursor.getInt(cursor.getColumnIndex(MessageByAdvertContract.STATUS)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByAdvertContract.PARENT_ID)))
                            .add(cursor.getString(cursor.getColumnIndex(MessageByAdvertContract.PRICE)))
                            .add(cursor.getString(cursor.getColumnIndex(MessageByAdvertContract.IMAGE)))
                            .add(cursor.getInt(cursor.getColumnIndex(MessageByAdvertContract.CURRENCY_ID)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByAdvertContract.CREATED_AT)))
                            .add(cursor.getLong(cursor.getColumnIndex(MessageByAdvertContract.UPDATED_AT)));
                }
            }
            cursor.close();
            return matrixCursor;
        }
        return null;
    }

    private Cursor searchAdvert(String[] selectionArgs) {
        String[] args = new String[]{AdvertisementColumns._ID,
                AdvertisementColumns.ID,
                AdvertisementColumns.TITLE,
                AdvertisementColumns.DESCRIPTION,
                AdvertisementColumns.USER_ID,
                AdvertisementColumns.CATEGORY_ID,
                AdvertisementColumns.CATEGORY_NAME,
                AdvertisementColumns.PRICE,
                AdvertisementColumns.CURRENCY_ID,
                AdvertisementColumns.PRICE_TYPE,
                AdvertisementColumns.COUNTRY_ID,
                AdvertisementColumns.REGION_ID,
                AdvertisementColumns.CITY_ID,
                AdvertisementColumns.COUNTRY_NAME,
                AdvertisementColumns.REGION_NAME,
                AdvertisementColumns.CITY_NAME,
                AdvertisementColumns.USED,
                AdvertisementColumns.BARGAIN,
                AdvertisementColumns.ACTIVE,
                AdvertisementColumns.APPROVED,
                AdvertisementColumns.EXPIRED,
                AdvertisementColumns.RENEWAL_DATE,
                AdvertisementColumns.EXPIRY_DATE,
                AdvertisementColumns.PHOTO_URL,
                AdvertisementColumns.FAVOURITE,
                AdvertisementColumns.TYPE,
                AdvertisementColumns.PHONE,
                AdvertisementColumns.FILTERS_NAMES,
                AdvertisementColumns.PATH,
                AdvertisementColumns.CREATED_AT,
                AdvertisementColumns.UPDATED_AT};

        Cursor cursor = null;
        String searchText = selectionArgs[0];
        long userId = Long.valueOf(selectionArgs[1]);

        cursor = query(AdvertisementContract.CONTENT_URI,
                null,
                AdvertisementContract.USER_ID + " = ? ",
                new String[]{String.valueOf(userId)},
                AdvertisementContract.CREATED_AT + " DESC");

        if (cursor != null) {
            MatrixCursor matrixCursor = new MatrixCursor(args);
            int i = 0;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex(AdvertisementContract.TITLE)).toLowerCase().contains(searchText.toLowerCase())) {
                    matrixCursor.newRow()
                            .add(i++)
                            .add(cursor.getLong(cursor.getColumnIndex(AdvertisementColumns.ID)))
                            .add(cursor.getString(cursor.getColumnIndex(AdvertisementColumns.TITLE)))
                            .add(cursor.getString(cursor.getColumnIndex(AdvertisementColumns.DESCRIPTION)))
                            .add(cursor.getLong(cursor.getColumnIndex(AdvertisementColumns.USER_ID)))
                            .add(cursor.getLong(cursor.getColumnIndex(AdvertisementColumns.CATEGORY_ID)))
                            .add(cursor.getString(cursor.getColumnIndex(AdvertisementColumns.CATEGORY_NAME)))
                            .add(cursor.getFloat(cursor.getColumnIndex(AdvertisementColumns.PRICE)))
                            .add(cursor.getInt(cursor.getColumnIndex(AdvertisementColumns.CURRENCY_ID)))
                            .add(cursor.getInt(cursor.getColumnIndex(AdvertisementColumns.PRICE_TYPE)))
                            .add(cursor.getLong(cursor.getColumnIndex(AdvertisementColumns.COUNTRY_ID)))
                            .add(cursor.getLong(cursor.getColumnIndex(AdvertisementColumns.REGION_ID)))
                            .add(cursor.getLong(cursor.getColumnIndex(AdvertisementColumns.CITY_ID)))
                            .add(cursor.getString(cursor.getColumnIndex(AdvertisementColumns.COUNTRY_NAME)))
                            .add(cursor.getString(cursor.getColumnIndex(AdvertisementColumns.REGION_NAME)))
                            .add(cursor.getString(cursor.getColumnIndex(AdvertisementColumns.CITY_NAME)))
                            .add(cursor.getInt(cursor.getColumnIndex(AdvertisementColumns.USED)))
                            .add(cursor.getInt(cursor.getColumnIndex(AdvertisementColumns.BARGAIN)))
                            .add(cursor.getInt(cursor.getColumnIndex(AdvertisementColumns.ACTIVE)))
                            .add(cursor.getInt(cursor.getColumnIndex(AdvertisementColumns.APPROVED)))
                            .add(cursor.getInt(cursor.getColumnIndex(AdvertisementColumns.EXPIRED)))
                            .add(cursor.getLong(cursor.getColumnIndex(AdvertisementColumns.RENEWAL_DATE)))
                            .add(cursor.getLong(cursor.getColumnIndex(AdvertisementColumns.EXPIRY_DATE)))
                            .add(cursor.getString(cursor.getColumnIndex(AdvertisementColumns.PHOTO_URL)))
                            .add(cursor.getInt(cursor.getColumnIndex(AdvertisementColumns.FAVOURITE)))
                            .add(cursor.getLong(cursor.getColumnIndex(AdvertisementColumns.TYPE)))
                            .add(cursor.getString(cursor.getColumnIndex(AdvertisementColumns.PHONE)))
                            .add(cursor.getString(cursor.getColumnIndex(AdvertisementColumns.FILTERS_NAMES)))
                            .add(cursor.getLong(cursor.getColumnIndex(AdvertisementColumns.PATH)))
                            .add(cursor.getLong(cursor.getColumnIndex(AdvertisementColumns.CREATED_AT)))
                            .add(cursor.getLong(cursor.getColumnIndex(AdvertisementColumns.UPDATED_AT)));
                }
            }
            cursor.close();
            return matrixCursor;
        }
        return null;
    }

    private interface Config extends SecureConfig {

        String SELECTION_SEARCH_CITY_BY_NAME = "";
        String SELECTION_SEARCH_CITY_BY_NAME_AND_CATEGORY = AdvertisementColumns.CATEGORY_ID + "=?";
        String SELECTION_SEARCH_ADVERTISEMENTS_BY_TITLE = AdvertisementColumns.TITLE + " LIKE ? ";
        String SELECTION_SEARCH_ADVERTISEMENTS_BY_TITLE_AND_CATEGORY = AdvertisementColumns.TITLE + " LIKE ? AND " + AdvertisementColumns.CATEGORY_ID + "=?";

        int SEARCH_BY_ADVERT_ITEM_DIR = 0x1001;
        int SEARCH_CITY_DIR = 0x1101;
        int SEARCH_USER_DIR = 0x2101;
        int SEARCH_MESSAGES_DIR = 0x3101;
        int SEARCH_ADVERT_DIR = 0x4101;
        int SEARCH_BY_USERS_ITEM_DIR = 0x1111;

        int CATEGORY_DIR_ID = 0x2000;
        int CATEGORY_ITEM_ID = 0x2001;
        int ADVERTISEMENT_DIR_ID = 0x4000;
        int ADVERTISEMENT_ITEM_ID = 0x4001;
        int COMMENTS_DIR_ID = 0x6000;
        int COMMENTS_ITEM_ID = 0x6001;
        int PHOTOS_DIR_ID = 0x7000;
        int PHOTOS_ITEM_ID = 0x7001;
        int NOTIFICATION_DIR_ID = 0x8000;
        int NOTIFICATION_ITEM_ID = 0x8001;
        int MESSAGE_BY_USER_DIR_ID = 0x9000;
        int MESSAGE_BY_USER_ITEM_ID = 0x9001;
        int MESSAGE_BY_ADVERT_DIR_ID = 0x9100;
        int MESSAGE_BY_ADVERT_ITEM_ID = 0x9101;
        int MESSAGE_CHAT_DIR_ID = 0x9200;
        int MESSAGE_CHAT_ITEM_ID = 0x9201;
        int CATEGORY_FILTERS_DIR_ID = 0x10000;
        int CATEGORY_FILTERS_ITEM_ID = 0x10001;
        int FILTER_VALUES_DIR_ID = 0x20000;
        int FILTER_VALUES_ITEM_ID = 0x20001;
        int USER_DATA_DIR_ID = 0x30000;
        int USER_DATA_ITEM_ID = 0x30001;

        int FILTER_VALUE_DIR = 0x4100;

        int COUNTRY_DIR_ID = 0x5100;
        int COUNTRY_ITEM_ID = 0x5101;
        int REGION_DIR_ID = 0x6100;
        int REGION_ITEM_ID = 0x6101;
        int CITY_DIR_ID = 0x7100;
        int CITY_ITEM_ID = 0x7101;
        int ADVERTISEMENT_FILTERS_DIR_ID = 0x8100;
        int ADVERTISEMENT_FILTERS_ITEM_ID = 0x8101;
    }

}