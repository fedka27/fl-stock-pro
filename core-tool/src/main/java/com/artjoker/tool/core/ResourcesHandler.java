package com.artjoker.tool.core;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

public final class ResourcesHandler {

    private ResourcesHandler() {

    }

    public static Drawable[] getDrawableArray(final Resources resources, final int arrayResourceId) {
        TypedArray typedArray = resources.obtainTypedArray(arrayResourceId);
        int count = typedArray.length();
        Drawable[] drawables = new Drawable[count];
        for (int i = 0; i < count; i++) {
            int resourceId = typedArray.getResourceId(i, 0);
            if (resourceId > 0) {
                drawables[i] = resources.getDrawable(resourceId);
            }
        }
        typedArray.recycle();
        return drawables;
    }

    public static String[] getStringArray(final Resources resources, final int arrayResourceId) {
        TypedArray typedArray = resources.obtainTypedArray(arrayResourceId);
        int count = typedArray.length();
        String[] strings = new String[count];
        for (int i = 0; i < count; i++) {
            strings[i] = typedArray.getString(i);
        }
        typedArray.recycle();
        return strings;
    }

    public static int[] getIdArray(final Resources resources, final int arrayResourceId) {
        TypedArray typedArray = resources.obtainTypedArray(arrayResourceId);
        int count = typedArray.length();
        int[] resourceIds = new int[count];
        for (int i = 0; i < count; i++) {
            int resourceId = typedArray.getResourceId(i, 0);
            if (resourceId > 0) {
                resourceIds[i] = resourceId;
            }
        }
        typedArray.recycle();
        return resourceIds;
    }

    public static int[] getColorArray(final Resources resources, final int arrayResourceId) {
        TypedArray typedArray = resources.obtainTypedArray(arrayResourceId);
        int count = typedArray.length();
        int[] colors = new int[count];
        for (int i = 0; i < count; i++) {
            int resourceId = typedArray.getResourceId(i, 0);
            if (resourceId > 0) {
                colors[i] = resources.getColor(resourceId);
            }
        }
        typedArray.recycle();
        return colors;
    }

}