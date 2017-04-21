package com.android.nusnafif.capstone.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.android.nusnafif.capstone.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NUSNAFIF on 4/12/2017.
 */

public class MasterAdapter extends CursorAdapter {

    @BindView(R.id.textStore) TextView storeInfo;
    @BindView(R.id.textDevice) TextView storeDevice;
    @BindView(R.id.storeInfo) TextView recordStore;


    public MasterAdapter(Context context, Cursor c){
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //return null;
        return LayoutInflater.from(context).inflate( R.layout.activity_main,parent,false );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ButterKnife.bind(view);
        String rowStore = cursor.getString(cursor.getColumnIndex(RetailContract.storeEntry.COLUMN_STORENAME));
        String rowDevice = cursor.getString(cursor.getColumnIndex(RetailContract.storeEntry.COLUMN_STOREDEVICE));
        storeInfo.setText(rowStore);
        storeDevice.setText(rowDevice);
    }
}
