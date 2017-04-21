package com.android.nusnafif.capstone.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by NUSNAFIF on 4/11/2017.
 */

public final class RetailContract {

    public static final String CONTENT_AUTHORITY = "com.android.nusnafif.capstone";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RETAIL = "retail";

    public RetailContract() {    }

    public static final class storeEntry implements BaseColumns {

        public static final String TABLE_STORE_NAME = "t_store";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_STORENAME = "store_name";
        public static final String COLUMN_STOREDEVICE = "device_name";
        public static final String COLUMN_CREATESTORE = "create_at";

        public static final  String[] projection = {
                COLUMN_ID,
                COLUMN_STORENAME,
                COLUMN_STOREDEVICE,
                COLUMN_CREATESTORE
        };


        // elements for ContentProvider
        public static final Uri CONTENT_URI1 = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RETAIL);
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RETAIL;
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE   + "/" + CONTENT_AUTHORITY + "/" + PATH_RETAIL;

    }

}
