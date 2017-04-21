package com.android.nusnafif.capstone;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.android.nusnafif.capstone.Utils.SendMail;
import com.android.nusnafif.capstone.data.TrxRetail;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onurciner.toastox.ToastOX;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends Activity implements SurfaceHolder.Callback {

    //our database reference object
    DatabaseReference databaseRetail;

    List<TrxRetail> retailList;

    MediaPlayer mediaPlayer;
    MediaController mediaController;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean pausing = false;
    //Get data bundle from Prev intent
    String storeNm;
    String storeDv;
    String userEmail;
    String userPhone;
    /*-- For save Video state --*/
    int lastOrientation = 0;

    @BindView(R.id.videoView)
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        //final int mPos = savedInstanceState.getInt("pos"); // get position, also check if value exists, refer to documentation for more info
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        /*--- For Firebase config ---*/
        databaseRetail = FirebaseDatabase.getInstance().getReference("trx_retail");

        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        storeNm = bundle.getString("storeNm");
        storeDv = bundle.getString("storeDv");


        getWindow().setFormat(PixelFormat.UNKNOWN);

        mediaController = new MediaController(this);

        String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.the_flash;
        Uri uri = Uri.parse(uriPath);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.requestFocus();
        videoView.start();

        /*--- Video finish palying? */
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.seekTo(0);
                delay(1000);
                AlertDialog.Builder dBuilder = new AlertDialog.Builder(VideoActivity.this);
                dBuilder.setIcon(R.mipmap.ic_launcher);
                dBuilder.setTitle(R.string.dialog_title);
                dBuilder.setMessage(R.string.dialog_msg_video);
                dBuilder.setCancelable(false);
                dBuilder.setPositiveButton(R.string.video_activity_dialog_continue, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent spotIntent = new Intent(getApplicationContext(), Spot1Activity.class);
                        spotIntent.putExtra("storeNm", storeNm);
                        spotIntent.putExtra("storeDv", storeDv);
                        ToastOX.info(getApplicationContext(), storeNm + " | " + storeDv);
                        saveFireBase(storeNm, storeDv);
                        startActivity(spotIntent);
//                        setupVoucher();
                    }
                });
                dBuilder.setNegativeButton(R.string.video_activity_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //-- End session go back to MainActivity
                        submitEnd();
                    }
                });
                AlertDialog alertDialog = dBuilder.create();
                alertDialog.show();
            }
        });
    }

    // Save data to firebase
    private void saveFireBase(String store, String device) {
        /*--- Push Data to firebase : trx_retail */
        String id = databaseRetail.push().getKey();
        String actionIntent = getString(R.string.action_intent_video);
        String email = "";
        String phone = "";
        String rewards = "";
        // Creating object
        TrxRetail tblRetail = new TrxRetail(id, getDateTime(), storeNm, storeDv, actionIntent, email, phone, rewards);
        // Save data
        databaseRetail.child(id).setValue(tblRetail);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }




    public void submitEmail(String toEmail, String subjectEmail, String emailMessage) {

        String fromEmail = getString(R.string.string_video_fromemail);
        //---Creating SendMail object
        SendMail sm = new SendMail(this, fromEmail, toEmail, subjectEmail, emailMessage);
        //---Executing sendmail to send email
        sm.execute();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }



    public void delay(int seconds) {
        final int milliseconds = seconds * 1000;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Waiting ...."); //add your code here
                    }
                }, milliseconds);
            }
        });
    }

    //--- Disable back button --
    @Override
    public void onBackPressed() {

    }

    /*--- Handle change orientation ---*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks if orientation changed
        if (lastOrientation != newConfig.orientation) {
            lastOrientation = newConfig.orientation;
            // redraw your controls here
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("pos", videoView.getCurrentPosition()); // save it here
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getInt("put", videoView.getCurrentPosition());
    }


    public void submitEnd() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
