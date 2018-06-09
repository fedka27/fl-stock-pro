package com.artjoker.tool.core;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;

public final class TypefaceProducer {
    private static final SparseArray<Typeface> CACHE = new SparseArray<Typeface>();

    private TypefaceProducer() {

    }

    public static TypefaceProducer getInstance() {
        return TypefaceProducerHolder.INSTANCE;
    }

    public final Typeface get(final Context context, final int key) {
        Typeface typeface = CACHE.get(key);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), buildPath(getFilename(key)));
            CACHE.put(key, typeface);
        }
        return typeface;
    }

    private String getFilename(int key) {
        switch (key) {
            case Key.LIGHT:
                return Path.LIGHT;
            case Key.REGULAR:
                return Path.REGULAR;
            case Key.BLACK:
                return Path.BLACK;
            default:
                return null;

        }
    }

    private String buildPath(final String filename) {
        return Path.DIR + filename + Path.EXTENSION;
    }

    private interface Path {
        String DIR = "fonts/";
        String EXTENSION = ".ttf";
        String LIGHT = "Lato-Light";
        String REGULAR = "Lato-Regular";
        String BLACK = "Lato-Black";
    }

    public interface Key {
        int LIGHT = 0x7003;
        int REGULAR = 0x7005;
        int BLACK = 0x7004;
    }

    private static class TypefaceProducerHolder {
        private static final TypefaceProducer INSTANCE = new TypefaceProducer();
    }

}