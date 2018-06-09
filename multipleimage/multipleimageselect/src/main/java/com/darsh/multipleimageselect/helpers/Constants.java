package com.darsh.multipleimageselect.helpers;

/**
 * Created by Darshan on 5/26/2015.
 */
public class Constants {
    public static final int GET_PHOTO_FROM_GALLERY = 2000;

    public static final String INTENT_EXTRA_IMAGES = "images";
    public static final String INTENT_EXTRA_LIMIT = "limit";
    public static final String INTENT_EXTRA_PHOTO_SIZE = "size";
    public static final int DEFAULT_LIMIT = 10;
    public static final long DEFAULT_PHOTO_SIZE = 2 * 1024 * 1024;

    //Maximum number of images that can be selected at a time
    public static int limit;
    public static long size;

    //String to display when number of selected images limit is exceeded
    public static String toastDisplayLimitExceed = String.format("Можно выбрать только %d фотографий", limit);
}
