package com.stockroompro.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.artjoker.core.CommonBundleBuilderArgs;
import com.stockroompro.api.models.requests.RequestParams;
import com.stockroompro.models.Comment;
import com.stockroompro.models.contracts.CommentsContract;
import com.stockroompro.models.converters.CommentCursorConverter;

import java.util.AbstractList;
import java.util.ArrayList;

public class CommentsLoader extends AsyncTaskLoader<ArrayList<Comment>> {

    private final Bundle bundle;
    private static final String TAG = CommentsLoader.class.getSimpleName();
    public CommentsLoader(Context context, Bundle bundle) {
        super(context);
        this.bundle = bundle;
    }

    @Override
    public ArrayList<Comment> loadInBackground() {
        ArrayList<Comment> comments = new ArrayList<>();
        try {
            long adId = bundle.getLong(RequestParams.ParamNames.AD_ID);
            StringBuilder appendingSQL = new StringBuilder(CommentsContract.DATE + " DESC");
            appendingSQL.append(" ");
            appendingSQL.append(CommonBundleBuilderArgs.LIMIT);
            appendingSQL.append(" ");
            if (bundle.containsKey(CommonBundleBuilderArgs.LIMIT)
                    && (bundle.getInt(CommonBundleBuilderArgs.LIMIT) != 0)) {
                appendingSQL.append(bundle.getInt(CommonBundleBuilderArgs.LIMIT));
            } else {
                appendingSQL.append(20);
            }
            Cursor c = getContext().getContentResolver().query(CommentsContract.CONTENT_URI, null,
                    CommentsContract.PARENT_ID + " = ? AND " + CommentsContract.ADVERTISEMENT_ID + " = ?",
                    new String[]{"0", String.valueOf(adId)}, appendingSQL.toString());

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                getAnswers(c, comments, adId);
            }
            c.close();
        }catch (Exception e){
            Log.e(TAG, "Cannot process response " , e);
        }
        return comments;
    }

    private void getAnswers(Cursor c, AbstractList<Comment> comments, long adId) {
        CommentCursorConverter converter = new CommentCursorConverter();
        converter.setCursor(c);
        comments.add(converter.getObject());
        Cursor child = getContext().getContentResolver().query(CommentsContract.CONTENT_URI, null,
                CommentsContract.PARENT_ID + " = ? AND " + CommentsContract.ADVERTISEMENT_ID + " = ?",
                new String[]{String.valueOf(c.getLong(c.getColumnIndexOrThrow(CommentsContract.ID))),
                        String.valueOf(adId)}, CommentsContract.DATE);
        for (child.moveToFirst(); !child.isAfterLast(); child.moveToNext()) {
            getAnswers(child, comments, adId);
        }
        child.close();
    }
}
