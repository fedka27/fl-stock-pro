package com.artjoker.core.network;

/**
 * Created by alexsergienko on 16.03.15.
 */
public interface ServerErrorsDefiner {

    String getErrorMessageByCode(int errorCode);
}
