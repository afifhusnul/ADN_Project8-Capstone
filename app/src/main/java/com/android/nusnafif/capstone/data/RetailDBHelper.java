package com.android.nusnafif.capstone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.android.nusnafif.capstone.data.RetailContract.storeEntry;
import static com.android.nusnafif.capstone.data.TrxContract.trxEntry;

/**
 * Created by NUSNAFIF on 4/10/2017.
 */

public class RetailDBHelper extends SQLiteOpenHelper {


    /**
     * Inventory Database name
     */
    private static final String DB_NAME = "retail.db";
    /**
     * Inventory Database version
     */
    private static final int DB_VERSION = 1;

    /**
     * Create a new instance of the {@link RetailDBHelper}
     *
     * @param context application context
     */
    public RetailDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_STORE_TABLE = "CREATE TABLE " + storeEntry.TABLE_STORE_NAME + " ("
                + storeEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + storeEntry.COLUMN_STORENAME + " TEXT NOT NULL, "
                + storeEntry.COLUMN_STOREDEVICE+ " TEXT, "
                + storeEntry.COLUMN_CREATESTORE+ " INTEGER NOT NULL DEFAULT 0 )";

        String SQL_CREATE_STORETRX_TABLE = "CREATE TABLE " + trxEntry.TABLE_TRXSTORE_NAME + " ("
                + trxEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + trxEntry.COLUMN_STORENAME + " TEXT NOT NULL, "
                + trxEntry.COLUMN_STOREDEVICE+ " TEXT NOT NULL, "
                + trxEntry.COLUMN_DATE + " INTEGER NOT NULL, "
                + trxEntry.COLUMN_EMAIL + " TEXT NOT NULL, "
                + trxEntry.COLUMN_PHONE + " TEXT NOT NULL, "
                + trxEntry.COLUMN_REWARDS + " TEXT NOT NULL); ";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_STORE_TABLE);
        db.execSQL(SQL_CREATE_STORETRX_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + storeEntry.TABLE_STORE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + trxEntry.TABLE_TRXSTORE_NAME);
        onCreate(db);
    }
}
