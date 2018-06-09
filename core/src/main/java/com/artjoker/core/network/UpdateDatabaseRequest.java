package com.artjoker.core.network;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.artjoker.core.database.CommonDBQueries;
import com.artjoker.core.database.ContentValuesConverter;

/**
 * Created by alexsergienko on 24.03.15.
 */
public abstract class UpdateDatabaseRequest<T, R extends ContentValuesConverter> extends BaseRequest<T> {
    protected final Uri uri;
    protected Class<R>  rClass;

    public UpdateDatabaseRequest(Context context, Uri uri, Class<R> rClass,Bundle bundle) {
        super(context,bundle );

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
        r.setObjectModel(responseHolder.getData());
        processItem(cr, uri, r);
    }

    protected void processItem(ContentResolver cr, Uri uri, ContentValuesConverter converter) throws Exception {
        if (converter.isValid()) {
            if (cr.update(uri, converter.getContentValues(), converter.getUpdateWhere(), converter.getUpdateArgs()) == 0) {
                cr.insert(uri, converter.getContentValues());
            }
        }
    }
}
