package com.stockroompro.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

public final class Communicator {

    private Communicator() {

    }

    public static AppServerSpecs getAppServer() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(90, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(90, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(90, TimeUnit.SECONDS);

        return new RestAdapter.Builder()
                .setEndpoint(BuildConfig.HOST + BuildConfig.PATH)
                .setClient(new OkClient(okHttpClient))
                .setConverter(new GsonConverter(Config.CONVERTER))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(AppServerSpecs.class);
    }

    public static Gson getConverter() {
        return Config.CONVERTER;
    }

    private interface Config {
        Gson CONVERTER = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }
}