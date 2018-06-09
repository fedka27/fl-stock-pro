package com.artjoker.core.network;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.artjoker.core.database.ContentValuesConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexsergienko on 24.03.15.
 */
public abstract class ButchUpdateArrayDatabaseRequest<T, R extends ContentValuesConverter> extends UpdateDatabaseRequest<T, R> {

    public ButchUpdateArrayDatabaseRequest(Context context, Uri uri, Class<R> rClass, Bundle bundle) {
        super(context, uri, rClass, bundle);
    }

    @Override
    public void processResponse(ResponseHolder<T> responseHolder) throws Exception {
        ContentResolver cr = getContext().getContentResolver();
        R r = rClass.newInstance();
        if (responseHolder.getData() instanceof ResponseItemHolder) {
            processItems(cr, uri, ((ResponseItemHolder) responseHolder.getData()).getItems(), r);
        } else if (responseHolder.getData() instanceof List) {
            processItems(cr, uri, ((List) responseHolder.getData()), r);
        }
    }

    protected <TT> void processItems(ContentResolver cr, Uri uri, List<TT> objects, ContentValuesConverter converter) throws Exception {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>(objects.size());
        for (TT obj : objects) {
            converter.setObjectModel(obj);
            if (converter.isValid()) {
                operations.add(
                        ContentProviderOperation.newUpdate(uri).withSelection(converter.getUpdateWhere(),
                                converter.getUpdateArgs()).withValues(converter.getContentValues()).build());
            }
        }
        cr.applyBatch(uri.getAuthority(), operations);
    }

    protected <TT, RR extends ContentValuesConverter> void processItems(ContentResolver cr, Uri uri, List<TT> objects, Class<RR> converterClass) throws Exception {
        ContentValuesConverter converter = converterClass.newInstance();
        processItems(cr, uri, objects, converter);
    }
}
