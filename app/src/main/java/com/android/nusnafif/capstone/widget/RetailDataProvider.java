package com.android.nusnafif.capstone.widget;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.nusnafif.capstone.R;
import com.android.nusnafif.capstone.data.RetailContract;
import com.android.nusnafif.capstone.data.TrxContract;

import static android.R.attr.data;

/**
 * Created by NUSNAFIF on 4/14/2017.
 */

public class RetailDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = RetailDataProvider.class.getSimpleName();

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}