package com.android.nusnafif.capstone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nusnafif.capstone.data.MasterAdapter;
import com.android.nusnafif.capstone.data.RetailContract;
import com.android.nusnafif.capstone.data.RetailDBHelper;
import com.android.nusnafif.capstone.data.TrxRetail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onurciner.toastox.ToastOX;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.nusnafif.capstone.data.RetailContract.storeEntry.TABLE_STORE_NAME;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //--Implement AdMob
    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;

    //-- Firebase
    DatabaseReference databaseRetail;
    private Uri currentUri;


    /*---Butterknife---*/

    @BindView(R.id.img)
    ImageView imgMain;
    @BindView(R.id.imgTouch)
    ImageView imgTouch;

    @BindView(R.id.activity_main)
    ConstraintLayout activityMain;

    @BindView(R.id.layoutRecord)
    LinearLayout layoutRecord;
    @BindView(R.id.textStore)
    TextView storeNameInfo;
    @BindView(R.id.textDevice)
    TextView storeDeviceInfo;

    @BindView(R.id.storeInfo)
    TextView storeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //--- Set Full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //--- Set screen as LANDSCAPE ONLY
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //-- Implement Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //-- Get temp Parameter from Layout
        ButterKnife.bind(this);

        //Animation on Touch
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking_animation);
        imgTouch.startAnimation(startAnimation);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Glide.with(MainActivity.this).load(R.drawable.flash)
                .centerCrop()
                .crossFade()
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgMain);


        //-- Firebase
        databaseRetail = FirebaseDatabase.getInstance().getReference("trx_retail");

        //--- Show Store record
        getStoreSetup();

        final String storeNm = storeNameInfo.getText().toString().trim();
        final String storeDevice = storeDeviceInfo.getText().toString().trim();

        //-- Implement AdMob
        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(MainActivity.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
            // Call displayInterstitial() function
                displayInterstitial();
            }
        });

    }

    public void displayInterstitial() {
    // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    private Cursor getStoreSetup() {
        Cursor cursor = getContentResolver().query(RetailContract.storeEntry.CONTENT_URI1, null, null, null, null);

        storeInfo.setText("Records : " + cursor.getCount() + " store.\n\n");
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                storeNameInfo.setText(cursor.getString(1));
                storeDeviceInfo.setText(cursor.getString(2));
                cursor.moveToNext();
            }
            cursor.close();
        }

        if (cursor.getCount() > 0) {
            disappearInput();
            final String storeNm = storeNameInfo.getText().toString().trim();
            final String storeDevice = storeDeviceInfo.getText().toString().trim();
            final String actionIntent = getString(R.string.action_intent_main);
            final String emailIntent = "";
            final String phoneIntent = "";
            final String rewardIntent = "";

            activityMain.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //-- Send data to FireBase
                    saveFireBase(storeNm, storeDevice);
                    //-- Goto Video
                    Intent VideoIntent = new Intent(MainActivity.this, VideoActivity.class);
                    VideoIntent.putExtra("storeNm", storeNm);
                    VideoIntent.putExtra("storeDv", storeDevice);
                    ToastOX.info(getApplicationContext(), storeNm + " | " + storeDevice);
                    startActivity(VideoIntent);
                    return true;
                }
            });

        } else {
            // Setup device
            setupDevice();
        }
        return cursor;
    }


    public void disappearInput() {
        //Dont display input
        storeNameInfo.setVisibility(View.GONE);
        storeDeviceInfo.setVisibility(View.GONE);
        layoutRecord.setVisibility(View.GONE);
        storeInfo.setVisibility(View.GONE);
    }

    public void setupDevice() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_setup, null);

        final EditText editStore = (EditText) mView.findViewById(R.id.editStore);
        final EditText editDevice = (EditText) mView.findViewById(R.id.editDevice);
        Button btnSave = (Button) mView.findViewById(R.id.btnSaveStore);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setIcon(android.R.drawable.sym_def_app_icon);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editStore.getText().toString().isEmpty() && !editDevice.getText().toString().isEmpty()) {

                    // create a ContentValues object where column names are keys,
                    ContentValues values = new ContentValues();
                    values.put(RetailContract.storeEntry.COLUMN_STORENAME, editStore.getText().toString().trim());
                    values.put(RetailContract.storeEntry.COLUMN_STOREDEVICE, editDevice.getText().toString().trim());
                    values.put(RetailContract.storeEntry.COLUMN_CREATESTORE, getDateTime());

                    if (currentUri == null) {
                        Uri newRowId = getContentResolver().insert(RetailContract.storeEntry.CONTENT_URI1, values);
                        ToastOX.ok(getApplicationContext(), getString(R.string.string_main_savedata) + newRowId);
                        //-- Close apps & Exit to HOME
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    } else {
                        ToastOX.error(getApplicationContext(), getString(R.string.string_main_savedataerror));
                    }
                    dialog.dismiss();
                } else {
                    return;
                }
            }
        });
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void saveFireBase(String store, String device) {
        /*--- Push Data to firebase : trx_retail */
        String id = databaseRetail.push().getKey();
        String action = getString(R.string.action_intent_main);
        String email = "";
        String phone = "";
        String rewards = "";
        // Creating object
        TrxRetail tblRetail = new TrxRetail(id, getDateTime(), store, device, action, email, phone, rewards);
        // Save data
        databaseRetail.child(id).setValue(tblRetail);
    }

}
