package com.artjoker.core.network;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.artjoker.core.database.CommonDBQueries;
import com.artjoker.core.database.ContentValuesConverter;

import java.util.List;

/**
 * Created by alexsergienko on 24.03.15.
 */
public abstract class UpdateArrayDatabaseRequest<T, R extends ContentValuesConverter> extends BaseRequest<T> {
    private final Uri uri;
    private Class<R> rClass;

    public UpdateArrayDatabaseRequest(Context context, Uri uri, Class<R> rClass) {
        super(context);
        this.uri = uri;
        this.rClass = rClass;
    }

    @Override
    public ResponseHolder<T> makeRequest() throws Exception {
        return makeRequest(CommonDBQueries.getMaxUpdateDateUnix(getContext(), uri));
    }


    public abstract ResponseHolder<T> makeRequest(long date) throws Exception;

    @Override
    public void processResponse(ResponseHolder<T> responseHolder) throws Exception {
        ContentResolver cr = getContext().getContentResolver();
        R r = rClass.newInstance();
        if (responseHolder.getData() instanceof List) {
            for (Object obj : (List) responseHolder.getData()) {
                r.setObjectModel(obj);
                if (r.isValid()) {
                    if (cr.update(uri, r.getContentValues(), r.getUpdateWhere(), r.getUpdateArgs()) == 0) {
                        cr.insert(uri, r.getContentValues());
                    }
                }
            }

        }
    }

}
