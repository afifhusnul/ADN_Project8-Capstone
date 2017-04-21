package com.android.nusnafif.capstone.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by NUSNAFIF on 4/11/2017.
 */

public final class TrxContract {

    public static final String CONTENT_AUTHORITY = "com.android.nusnafif.capstone";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TRXRETAIL = "trxretail";

    public TrxContract() {    }

    public static final class trxEntry implements BaseColumns {
        public static final String TABLE_TRXSTORE_NAME = "trx_store";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_STORENAME = "store_name";
        public static final String COLUMN_STOREDEVICE = "device_name";
        public static final String COLUMN_DATE = "date_trx";
        public static final String COLUMN_EMAIL = "subscriber_email";
        public static final String COLUMN_PHONE = "subscriber_phone";
        public static final String COLUMN_REWARDS = "subscriber_rewards";


        // elements for ContentProvider
        public static final Uri CONTENT_URI2 = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TRXRETAIL);
        public static final String CONTENT_ITEM_TYPE2 = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRXRETAIL;
        public static final String CONTENT_DIR_TYPE2 = ContentResolver.CURSOR_DIR_BASE_TYPE   + "/" + CONTENT_AUTHORITY + "/" + PATH_TRXRETAIL;
    }
}
