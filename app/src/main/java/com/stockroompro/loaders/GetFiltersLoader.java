package com.stockroompro.loaders;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.MergeCursor;

import com.artjoker.database.SecureDatabaseProvider;
import com.stockroompro.models.columns.FiltersColumns;
import com.stockroompro.models.contracts.CategoryFiltersContract;
import com.stockroompro.models.contracts.FilterValuesContract;

/**
 * Created by bagach.alexandr on 27.04.15.
 */
public class GetFiltersLoader extends CursorLoader {

    private long categoryId;

    public GetFiltersLoader(Context context, long categoryId) {
        super(context);
        this.categoryId = categoryId;
    }

    @Override
    public Cursor loadInBackground() {
        Cursor categoryFiltersCursor = getCategoryFilters();
        Cursor[] cursors = new Cursor[categoryFiltersCursor.getCount() + 1];
        cursors[0] = categoryFiltersCursor;
        int i = 1;
        if (categoryFiltersCursor.moveToFirst()) {
            do {
                long filterId = categoryFiltersCursor.getLong(1);
                Cursor cursor = getFiltersValues(filterId);
                cursors[i] = cursor;
                i++;
            } while (categoryFiltersCursor.moveToNext());
        }

        if (cursors.length > 0) {
            return new MergeCursor(cursors);
        } else return null;
    }

    private Cursor getCategoryFilters() {
        return getContext().getContentResolver().query(CategoryFiltersContract.CONTENT_URI,
                Config.CATEGORY_FILTER_PROJECTION, Config.CATEGORY_FILTER_SELECTION, buildSelectionArgs(categoryId), null);
    }

    private Cursor getFiltersValues(long id) {
        return getContext().getContentResolver().query(FilterValuesContract.CONTENT_URI,
                Config.FILTER_VALUES_PROJECTION, Config.FILTER_VALUES_SELECTION, buildSelectionArgs(id), null);
    }

    private String[] buildSelectionArgs(long id) {
        return new String[]{
                String.valueOf(id),
        };
    }

    private interface Config {
        String[] CATEGORY_FILTER_PROJECTION = new String[]{FiltersColumns._ID, FiltersColumns.ID + " AS parent_id", FiltersColumns.NAME + " AS parent_name"};
        String CATEGORY_FILTER_SELECTION = FiltersColumns.CATEGORY_ID + " = ?";

        String[] FILTER_VALUES_PROJECTION = new String[]{FiltersColumns._ID, FiltersColumns.ID, FiltersColumns.NAME, FiltersColumns.PARENT_ID};
        String FILTER_VALUES_SELECTION = FiltersColumns.PARENT_ID + " = ?";
    }
}
