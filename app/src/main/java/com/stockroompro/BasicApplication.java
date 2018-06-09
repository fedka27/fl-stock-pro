package com.stockroompro;

import android.app.ActivityManager;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

import io.fabric.sdk.android.Fabric;

public final class BasicApplication extends MultiDexApplication {
    private static final String IMAGES = "images";
    public static final String TAG = "Application";
    private static final int BYTE_FACTOR = 1024;
    private static final int DISK_CACHE_LIMIT = 15;
    private static final int POOL_SIZE = 5;
    private static final int PRIORITY = Thread.MIN_PRIORITY + 3;
    private static final int TIME_FACTOR = 1000;
    private static final int RESPONSE_TIMEOUT = 20 * TIME_FACTOR * TIME_FACTOR;
    private static final int CONNECTION_TIMEOUT = 5 * TIME_FACTOR * TIME_FACTOR;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: 36");
        ImageLoader.getInstance().init(getConfig());
        Log.e(TAG, "onCreate: 38");
        Fresco.initialize(this);
        Log.e(TAG, "onCreate: 40");
        Fabric fabric = new Fabric.Builder(this).debuggable(true).kits(new Crashlytics()).build();
        Log.e(TAG, "onCreate: 42");
        Fabric.with(fabric);
        Log.e(TAG, "onCreate: 44");
    }

    private DisplayImageOptions getDisplayImageOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_image)
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    private ImageLoaderConfiguration getConfig() {
        File cacheDir = initCacheDir();
        return new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(POOL_SIZE)
                .threadPriority(PRIORITY)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(getMemoryCacheSize()))
                .diskCache(new LruDiscCache(cacheDir, new Md5FileNameGenerator(), getDirectoryCacheSize()))
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), CONNECTION_TIMEOUT, RESPONSE_TIMEOUT))
                .defaultDisplayImageOptions(getDisplayImageOptions())
                .build();
    }

    private File initCacheDir() {
        File cacheDir = new File(getFilesDir(), IMAGES);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    private int getMemoryCacheSize() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        return (am.getMemoryClass() * BYTE_FACTOR * BYTE_FACTOR) / 2;
    }

    private long getDirectoryCacheSize() {
        return DISK_CACHE_LIMIT * BYTE_FACTOR * BYTE_FACTOR;
    }

    public enum TrackerName {
        GLOBAL,
        APP
    }
}