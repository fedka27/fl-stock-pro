package com.artjoker.core.network;

import com.artjoker.tool.core.Crypto;
import com.google.gson.annotations.Expose;

public abstract class SignatureRequestHolder<T> extends RequestHolder<T> {
    @Expose
    private final String signature;

    public SignatureRequestHolder(final T data) {
        super(data);
        this.signature = Crypto.md5(buildSignatureContent(data));
    }

    public final String getSignature() {
        return signature;
    }

    protected abstract String buildSignatureContent(final T data);

}