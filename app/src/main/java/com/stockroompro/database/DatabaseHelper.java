package com.stockroompro.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stockroompro.models.contracts.AdvertFiltersContract;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.contracts.CategoryContract;
import com.stockroompro.models.contracts.CategoryFiltersContract;
import com.stockroompro.models.contracts.ChatMessageContract;
import com.stockroompro.models.contracts.CityContract;
import com.stockroompro.models.contracts.CommentsContract;
import com.stockroompro.models.contracts.CountryContract;
import com.stockroompro.models.contracts.FilterValuesContract;
import com.stockroompro.models.contracts.MessageByAdvertContract;
import com.stockroompro.models.contracts.MessageByUserContract;
import com.stockroompro.models.contracts.NotificationContract;
import com.stockroompro.models.contracts.PhotosContract;
import com.stockroompro.models.contracts.RegionContract;
import com.stockroompro.models.contracts.UserDataContract;


final class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(Context context) {
        super(context, DatabaseConfig.NAME, null, DatabaseConfig.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w(TAG, "database create - version " + DatabaseConfig.VERSION);
        db.execSQL(CategoryContract.CREATE_TABLE);
        db.execSQL(AdvertisementContract.CREATE_TABLE);
        db.execSQL(CommentsContract.CREATE_TABLE);
        db.execSQL(PhotosContract.CREATE_TABLE);
        db.execSQL(NotificationContract.CREATE_TABLE);
        db.execSQL(MessageByUserContract.CREATE_TABLE);
        db.execSQL(MessageByAdvertContract.CREATE_TABLE);
        db.execSQL(ChatMessageContract.CREATE_TABLE);
        db.execSQL(CategoryFiltersContract.CREATE_TABLE);
        db.execSQL(FilterValuesContract.CREATE_TABLE);
        db.execSQL(UserDataContract.CREATE_TABLE);
        db.execSQL(CountryContract.CREATE_TABLE);
        db.execSQL(RegionContract.CREATE_TABLE);
        db.execSQL(CityContract.CREATE_TABLE);
        db.execSQL(AdvertFiltersContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "database upgrade - version from " + oldVersion + " to " + newVersion);
    }

}