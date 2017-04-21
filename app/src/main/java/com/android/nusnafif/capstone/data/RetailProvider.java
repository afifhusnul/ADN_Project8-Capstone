package com.android.nusnafif.capstone.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;



import static com.android.nusnafif.capstone.data.RetailContract.storeEntry.CONTENT_URI1;
import static com.android.nusnafif.capstone.data.TrxContract.trxEntry.CONTENT_URI2;

/**
 * Created by NUSNAFIF on 4/10/2017.
 */

public class RetailProvider extends ContentProvider {

    private static final String LOG_TAG = RetailProvider.class.getSimpleName();


    // URI Matcher
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Database helper object
    private RetailDBHelper dbHelper;


    private static final int storeTable = 100;
    private static final int storeItem = 101;
    private static final int storeTrxTable = 102;
    private static final int storeTrxItem = 103;

    static {
        //-- Store Master
        uriMatcher.addURI(RetailContract.CONTENT_AUTHORITY, RetailContract.PATH_RETAIL, storeTable);
        uriMatcher.addURI(RetailContract.CONTENT_AUTHORITY, RetailContract.PATH_RETAIL + "/#", storeItem);

        //-- Trx Store
        uriMatcher.addURI(TrxContract.CONTENT_AUTHORITY, TrxContract.PATH_TRXRETAIL, storeTrxTable);
        uriMatcher.addURI(TrxContract.CONTENT_AUTHORITY, TrxContract.PATH_TRXRETAIL + "/#", storeTrxItem);
    }


    @Override
    public boolean onCreate() {
        dbHelper = new RetailDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int uriMatch = uriMatcher.match(uri);
        switch(uriMatch){
            case storeTable:
                return RetailContract.storeEntry.CONTENT_DIR_TYPE;
            case storeItem:
                return RetailContract.storeEntry.CONTENT_ITEM_TYPE;
            case storeTrxTable:
                return TrxContract.trxEntry.CONTENT_DIR_TYPE2;
            case storeTrxItem:
                return TrxContract.trxEntry.CONTENT_ITEM_TYPE2;
            default:
                throw new IllegalArgumentException("Cannot parse uri: " + uri + " with match " + uriMatch);
        }
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = null;
        int uriMatch = uriMatcher.match(uri);

        switch(uriMatch) {
            case storeTable:
                result = db.query(RetailContract.storeEntry.TABLE_STORE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case  storeItem:
                selection = RetailContract.storeEntry.COLUMN_ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                result = db.query(RetailContract.storeEntry.TABLE_STORE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case storeTrxTable:
                result = db.query(TrxContract.trxEntry.TABLE_TRXSTORE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case  storeTrxItem:
                selection = TrxContract.trxEntry.COLUMN_ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                result = db.query(TrxContract.trxEntry.TABLE_TRXSTORE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                // no match found
                throw new IllegalArgumentException("Cannot query for Uri: " + uri);
        }
        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        long id = -1;

        switch (match){
            case storeTable:
                return insertStore(uri, values, database);
            case storeTrxTable:
                return insertTrxStore(uri, values, database);
            default:
                throw new IllegalStateException("Cannot query unknown URI" + uri);
        }
    }

    private Uri insertTrxStore(Uri uri, ContentValues values, SQLiteDatabase database) {
        long id;
        id =  database.insert(TrxContract.trxEntry.TABLE_TRXSTORE_NAME,null, values);

        if(id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for" + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertStore(Uri uri, ContentValues values, SQLiteDatabase database) {

        long id;
        id =  database.insert(RetailContract.storeEntry.TABLE_STORE_NAME,null, values);

        if(id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for" + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
