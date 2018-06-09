package com.stockroompro.api;

import com.artjoker.core.network.RequestHolder;
import com.artjoker.tool.core.SystemHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import retrofit.mime.TypedOutput;

public final class ServerTypedOutput<T> implements TypedOutput {
    private final byte[] bodyBytes;
    private final String mimeType;

    public ServerTypedOutput(final T data) {
        this(data, "UTF-8");
    }

    public ServerTypedOutput(final T data, final String encoding) {
        final RequestHolder<T> requestHolder = new RequestHolder<T>(data);
        this.bodyBytes = buildBody(requestHolder, requestHolder.getClass(), encoding);
        this.mimeType = "application/x-www-form-urlencoded; charset=" + encoding;
    }

    public static <T> ServerTypedOutput<T> build(final T data) {
        return new ServerTypedOutput<T>(data);
    }

    @Override
    public String fileName() {
        return null;
    }

    @Override
    public String mimeType() {
        return mimeType;
    }

    @Override
    public long length() {
        return bodyExist() ? bodyBytes.length : 0;
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        if (bodyExist()) {
            out.write(bodyBytes);
        }
    }

    private byte[] buildBody(final RequestHolder<T> requestHolder, final Type type, final String encoding) {
        try {
            return Communicator.getConverter().toJson(requestHolder, type).toString().getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addKeyValuePair(final StringBuilder builder, final String key, final String value) {
        checkThenAddAnd(builder);
        builder.append(key).append("=").append(value);
    }

    private void checkThenAddAnd(final StringBuilder builder) {
        if (SystemHelper.getInstance().notEmpty(builder.length())) {
            builder.append("&");
        }
    }

    private boolean bodyExist() {
        return (bodyBytes != null);
    }

}