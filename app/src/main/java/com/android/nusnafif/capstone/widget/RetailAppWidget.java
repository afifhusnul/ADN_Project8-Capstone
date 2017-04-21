package com.android.nusnafif.capstone.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nusnafif.capstone.MainActivity;
import com.android.nusnafif.capstone.R;
import com.android.nusnafif.capstone.data.RetailContract;
import com.android.nusnafif.capstone.data.TrxContract;
import com.android.nusnafif.capstone.data.TrxRetail;
import com.google.firebase.database.DataSnapshot;
import com.onurciner.toastox.ToastOX;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Implementation of App Widget functionality.
 */
public class RetailAppWidget extends AppWidgetProvider {

    private static final String LOG_TAG = RetailAppWidget.class.getSimpleName();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        ComponentName widget = new ComponentName(context, RetailAppWidget.class);
        int[] allIds = appWidgetManager.getAppWidgetIds(widget);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.retail_app_widget);

        for (int widgetID : allIds) {

            //Make call to get data from db (Device setup)
            CursorLoader loader = new CursorLoader(context, RetailContract.storeEntry.CONTENT_URI1,
                    null, null, null, null);
            Cursor data = loader.loadInBackground();

            if (data != null) {
                data.moveToFirst();
                for (int i = 0; i < data.getCount(); i++) {
                    views.setTextViewText(R.id.storeInfoWidget, data.getString(1));
                    data.moveToNext();
                }
                data.close();
            }

            //Create intent to launch Activity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            //Get layout for app widget and attach on click listener

            views.setOnClickPendingIntent(R.id.widgetRetail, pendingIntent);

            //AppWidgetManager to show widget
            appWidgetManager.updateAppWidget(widgetID, views);
        }
    }

    /*static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        *//*final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.retail_app_widget);*//*

        *//*public void onDataChange(Context context){*//*
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.retail_app_widget);
        CursorLoader loader = new CursorLoader(context, RetailContract.storeEntry.CONTENT_URI1,
                null, null, null, null);
        Cursor data = loader.loadInBackground();

        if (data != null) {
            data.moveToFirst();
            for (int i = 0; i < data.getCount(); i++) {
                views.setTextViewText(R.id.storeInfoWidget, data.getString(1));
                data.moveToNext();
            }
            data.close();
        }

        //Create intent to launch Activity
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //Get layout for app widget and attach on click listener

        views.setOnClickPendingIntent(R.id.widgetRetail, pendingIntent);


    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }*/
}

