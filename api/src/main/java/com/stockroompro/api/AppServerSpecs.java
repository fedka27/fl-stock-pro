package com.stockroompro.api;


import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.ResponseItemHolder;
import com.stockroompro.api.models.requests.BodyDelete;
import com.stockroompro.api.models.responses.RangeOfPrices;
import com.stockroompro.api.models.responses.user.UserAuthorization;
import com.stockroompro.api.models.responses.user.UserData;
import com.stockroompro.api.models.responses.user.UserRegistration;
import com.stockroompro.models.AdvertPhoto;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.Category;
import com.stockroompro.models.Comment;
import com.stockroompro.models.ExpiryDate;
import com.stockroompro.models.FilterValues;
import com.stockroompro.models.Filters;
import com.stockroompro.models.Message;
import com.stockroompro.models.NewAdvert;
import com.stockroompro.models.NotificationItem;
import com.stockroompro.models.Settings;
import com.stockroompro.models.location.City;
import com.stockroompro.models.location.Country;
import com.stockroompro.models.location.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

public interface AppServerSpecs {

    String CATEGORY_ID = "CATEGORY_ID";
    String USER_ID = "USER_ID";
    String ADVERTISEMENT_ID = "ADVERTISEMENT_ID";
    String INTERLOCUTOR_ID = "INTERLOCUTOR_ID";
    String MESSAGE_ID = "MESSAGE_ID";
    String ADD_ID = "AD_ID";
    String COMMENT_ID = "COMMENT_ID";
    String LIMIT = "limit";
    String OFFSET = "offset";
    String COUNTRY_ID = "COUNTRY_ID";
    String REGION_ID = "REGION_ID";

    String CATEGORIES_PATH = "/v1/categories";
    String SUB_CATEGORIES_PATH = "/v1/categories/{CATEGORY_ID}/children";
    String REGISTRATION_PATH = "/v1/user";
    String AUTHORIZATION_PATH = "/v1/auth";
    String LOG_OUT_PATH = "/v1/user/{USER_ID}/exit";
    String PASSWORD_RECOVERY_PATH = "/v1/auth/password_recovery";
    String SYNC_WITH_SOCIAL_PATH = "/v1/auth/sync";
    String USER_PATH = "/v1/user/{USER_ID}";
    String ADVERTISEMENT_PATH = "/v1/ads";
    String ADVERTISEMENT_DETAILS_PATH = "/v1/ads/{ADVERTISEMENT_ID}";

    String MESSAGES_BY_USER_PATH = "/v1/user/{USER_ID}/interlocutors";
    String CHANGE_MESSAGE_STATUS_PATH = "/v1/user/{USER_ID}/messages/{MESSAGE_ID}";

    String MESSAGE_BY_ADVERT_PATH = "/v1/user/{USER_ID}/interlocutor/{INTERLOCUTOR_ID}/dialogs";

    String FAVOURITES_PATH = "/v1/user/{USER_ID}/favorites";
    String FAVOURITES_ADDING_PATH = "/v1/user/{USER_ID}/favorites";
    String FAVOURITES_REMOVING_PATH = "/v1/user/{USER_ID}/favorites/{AD_ID}";

    String EDIT_PROFILE_PHOTO_PATH = "/v1/user/{USER_ID}/photo";
    String FILTERS_ALL = "/v1/options";
    String FILTERS_BY_CATEGORY_ID = "/v1/categories/{CATEGORY_ID}/options";
    String ADD_ADVERTISEMENT_PHOTOS = "/v1/ad/{ADVERTISEMENT_ID}/photo";
    String EDIT_RATING_PATH = "/v1/user/{USER_ID}/rating";
    String USER_ADVERTISEMENT_PATH = "/v1/user/{USER_ID}/ads";
    String MESSAGES_PATH = "/v1/messages";
    String CHAT_PATH = "/v1/messages/ad/{ADVERTISEMENT_ID}";
    String MESSAGE_STATUS_PATH = "/v1/messages/{MESSAGE_ID}";
    String COMMENTS_PATH = "/v1/ads/{AD_ID}/comments";
    String LIKE_COMMENTS_PATH = "/v1/comment/{COMMENT_ID}";
    String COUNTRY_PATH = "/v1/countries";
    String REGION_PATH = "/v1/countries/{COUNTRY_ID}/regions";
    String CITY_PATH = "/v1/countries/{COUNTRY_ID}/regions/{REGION_ID}/cities";
    String SEARCH_CITY_PATH = "/v1/cities";
    String SUBSCRIBE_GCM_PATH = "/v1/gcm/sign_on";
    String UNSUBSCRIBE_GCM_PATH = "/v1/gcm/sign_off";
    String NOTIFICATION_PATH = "/v1/notices";
    String DELETE_ADVERT_PATH = "/v1/user/{USER_ID}/ads/{ADVERTISEMENT_ID}";
    String SETTINGS_PATH = "/v1/settings";
    String EXTEND_ADVERT_PATH = "/v1/user/{USER_ID}/ads/{ADVERTISEMENT_ID}";
    String SEARCH_USERS_PATH = "/v1/users";


    @GET(CATEGORIES_PATH)
    ResponseHolder<ArrayList<Category>> getCategories(@QueryMap Map<String, Object> params);

    @GET(SUB_CATEGORIES_PATH)
    ResponseHolder<ArrayList<Category>> getSubCategories(@Path(CATEGORY_ID) long id, @QueryMap Map<String, Object> params);

    @POST(REGISTRATION_PATH)
    ResponseHolder<UserRegistration> registration(@Body Map<String, Object> params);


    @POST(AUTHORIZATION_PATH)
    ResponseHolder<UserAuthorization> authorization(@Body Map<String, Object> params);

    @POST(LOG_OUT_PATH)
    ResponseHolder logOutSocial(@Path(USER_ID) long userId, @Body Map<String, Object> params);

    @POST(PASSWORD_RECOVERY_PATH)
    ResponseHolder<UserRegistration> passwordRecovery(@Body Map<String, Object> params);

    @POST(SYNC_WITH_SOCIAL_PATH)
    ResponseHolder syncWithSocial(@Body Map<String, Object> params);

    @GET(USER_PATH)
    ResponseHolder<UserData> getPersonalData(@Path(USER_ID) long id, @QueryMap Map<String, Object> params);

    @GET(USER_PATH)
    ResponseHolder<com.stockroompro.models.UserData> getUserData(@Path(USER_ID) long id, @QueryMap Map<String, Object> params);

    @BodyDelete(USER_PATH)
    ResponseHolder deleteUser(@Path(USER_ID) long id, @Body Map<String, Object> params);

    @PUT(USER_PATH)
    ResponseHolder<UserData> editUser(@Path(USER_ID) long id, @Body Map<String, Object> params);

    @GET(ADVERTISEMENT_PATH)
    ResponseHolder<ResponseItemHolder<Advertisement>> getAdvertsByCategoryIdAndText(@QueryMap Map<String, Object> params);

    @GET(SEARCH_USERS_PATH)
    ResponseHolder<ResponseItemHolder<UserData>> searchUsers(@QueryMap Map<String, Object> params);

    @GET(ADVERTISEMENT_PATH)
    ResponseHolder<RangeOfPrices> getAdvertsPricesRange(@QueryMap Map<String, Object> params);

    @GET(ADVERTISEMENT_PATH)
    ResponseHolder<ResponseItemHolder<Advertisement>> getAdvertsFromSearch(@QueryMap Map<String, Object> params);

    @GET(ADVERTISEMENT_DETAILS_PATH)
    ResponseHolder<Advertisement> getAdvertisementDetails(@Path(ADVERTISEMENT_ID) long id, @QueryMap Map<String, Object> params);

    @PUT(ADVERTISEMENT_DETAILS_PATH)
    ResponseHolder<NewAdvert> editAdvertisement(@Path(ADVERTISEMENT_ID) long id, @Body Map<String, Object> params);

    @GET(MESSAGES_BY_USER_PATH)
    ResponseHolder<ResponseItemHolder<Message>> getMessagesByUsers(@Path(USER_ID) long id, @QueryMap Map<String, Object> params);

    @GET(MESSAGE_BY_ADVERT_PATH)
    ResponseHolder<ResponseItemHolder<Message>> getMessagesByAdvert(@Path(USER_ID) long id, @Path(INTERLOCUTOR_ID) long interlocutorId, @QueryMap Map<String, Object> params);

    @POST(CHANGE_MESSAGE_STATUS_PATH)
    ResponseHolder changeMessageStatus(@Path(USER_ID) long userId, @Path(MESSAGE_ID) long messageId, @Body Map<String, Object> params);

    @GET(FAVOURITES_PATH)
    ResponseHolder<ResponseItemHolder<Advertisement>> getFavourites(@Path(USER_ID) long userId, @QueryMap Map<String, Object> params);

    @POST(FAVOURITES_ADDING_PATH)
    ResponseHolder addFavourite(@Path(USER_ID) long userId, @Body Map<String, Object> params);

    @BodyDelete(FAVOURITES_REMOVING_PATH)
    ResponseHolder removeFavourite(@Path(USER_ID) long userId, @Path(ADD_ID) long advertId, @Body Map<String, Object> params);

    @BodyDelete(EDIT_PROFILE_PHOTO_PATH)
    ResponseHolder deleteProfilePhoto(@Path(USER_ID) long id, @Body Map<String, Object> params);

    @GET(COMMENTS_PATH)
    ResponseHolder<ResponseItemHolder<Comment>> getComments(@Path(ADD_ID) long addId, @QueryMap Map<String, Object> params);

    @POST(COMMENTS_PATH)
    ResponseHolder addComment(@Path(ADD_ID) long addId, @Body Map<String, Object> params);

    @POST(LIKE_COMMENTS_PATH)
    ResponseHolder setLike(@Path(COMMENT_ID) long commentId, @Body Map<String, Object> params);

    @GET(FILTERS_ALL)
    ResponseHolder<ArrayList<Filters>> getFilters(@QueryMap Map<String, Object> params);

    @GET(FILTERS_BY_CATEGORY_ID)
    ResponseHolder<ArrayList<FilterValues>> getFilters(@Path(CATEGORY_ID) long categoryId, @QueryMap Map<String, Object> params);

    @POST(ADVERTISEMENT_PATH)
    ResponseHolder<NewAdvert> addAdvertisement(@Body Map<String, Object> params);

    @PUT(EDIT_PROFILE_PHOTO_PATH)
    @Multipart
    ResponseHolder editProfilePhoto(@Path(USER_ID) long id,
                                    @Part("img") TypedFile photo,
                                    @Part("app_id") TypedString appId,
                                    @Part("t") long time,
                                    @Part("token") TypedString token,
                                    @Part("h") TypedString hash);

    @PUT(ADD_ADVERTISEMENT_PHOTOS)
    @Multipart
    ResponseHolder<AdvertPhoto> sendPhotos(@Path(ADVERTISEMENT_ID) long addId,
                                           @PartMap HashMap<String, TypedFile> photos,
                                           @Part("app_id") TypedString appId,
                                           @Part("t") long time,
                                           @Part("token") TypedString token,
                                           @Part("delete_photos") ArrayList<String> deletePhotos,
                                           @Part("h") TypedString hash) throws RetrofitError;

    @POST(EDIT_RATING_PATH)
    ResponseHolder setRating(@Path(USER_ID) long userId, @Body Map<String, Object> params);

    @GET(USER_ADVERTISEMENT_PATH)
    ResponseHolder<ResponseItemHolder<Advertisement>> getAdvertsByUserId(@Path(USER_ID) long userId, @QueryMap Map<String, Object> params);

    @POST(MESSAGES_PATH)
    ResponseHolder sendMessage(@Body Map<String, Object> params);

    @GET(CHAT_PATH)
    ResponseHolder<ResponseItemHolder<Message>> getChat(@Path(ADVERTISEMENT_ID) long id, @QueryMap Map<String, Object> params);

    @PUT(MESSAGE_STATUS_PATH)
    ResponseHolder setMessageStatus(@Path(MESSAGE_ID) long id, @Body Map<String, Object> params);

    @POST(SUBSCRIBE_GCM_PATH)
    ResponseHolder subscribeGCM(@Body Map<String, Object> params);

    @POST(UNSUBSCRIBE_GCM_PATH)
    ResponseHolder unsubscribeGCM(@Body Map<String, Object> params);

    @GET(COUNTRY_PATH)
    ResponseHolder<ArrayList<Country>> getCountries(@QueryMap Map<String, Object> params);

    @GET(REGION_PATH)
    ResponseHolder<ArrayList<Region>> getRegions(@Path(COUNTRY_ID) long countryId, @QueryMap Map<String, Object> params);

    @GET(CITY_PATH)
    ResponseHolder<ArrayList<City>> getCities(@Path(COUNTRY_ID) long countryId, @Path(REGION_ID) long regionId, @QueryMap Map<String, Object> params);

    @GET(SEARCH_CITY_PATH)
    ResponseHolder<ResponseItemHolder<City>> getSearchCities(@QueryMap Map<String, Object> params);

    @GET(NOTIFICATION_PATH)
    ResponseHolder<ArrayList<NotificationItem>> getNotifications(@QueryMap Map<String, Object> params);

    @BodyDelete(DELETE_ADVERT_PATH)
    ResponseHolder deleteAdvert(@Path(USER_ID) long userId, @Path(ADVERTISEMENT_ID) long adId, @Body Map<String, Object> params);
  
    @GET(SETTINGS_PATH)
    ResponseHolder<Settings> getSettings(@QueryMap Map<String, Object> params);

    @POST(EXTEND_ADVERT_PATH)
    ResponseHolder<ExpiryDate> extendAdvert(@Path(USER_ID) long id, @Path(ADVERTISEMENT_ID) long adId, @Body Map<String, Object> params);
}