package com.android.nusnafif.capstone;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.nusnafif.capstone.Utils.SendMail;
import com.android.nusnafif.capstone.data.RetailContract;
import com.android.nusnafif.capstone.data.RetailDBHelper;
import com.android.nusnafif.capstone.data.TrxContract;
import com.android.nusnafif.capstone.data.TrxRetail;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onurciner.toastox.ToastOX;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Spot1Activity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    float mX;
    float mY;

    @BindView(R.id.frame)
    ConstraintLayout mainLayout;

    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.circle1a)
    ImageView c1a;
    @BindView(R.id.circle1b)
    ImageView c1b;
    @BindView(R.id.circleMenuA)
    ImageView circleMenuA;
    @BindView(R.id.circleMenuB)
    ImageView circleMenuB;
    @BindView(R.id.circleTop20A)
    ImageView circleTopA;
    @BindView(R.id.circleTop20B)
    ImageView circleTopB;
    @BindView(R.id.circleSearchA)
    ImageView circleSearchA;
    @BindView(R.id.circleSearchB)
    ImageView circleSearchB;

    @BindView(R.id.circleHulk)
    ImageView circleHulk;
    @BindView(R.id.circleCaptain)
    ImageView circleCaptain;

    @BindView(R.id.spot5)
    ImageView Spot;

    @BindView(R.id.btnEnd)
    Button btnEnd;

    @BindView(R.id.textPos)
    TextView textPos;

    String storeNm;
    String storeDv;

    //--- database reference object
    DatabaseReference databaseRetail;
    private Uri currentUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_spot1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        //Animation on Touch
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking_animation);
        Spot.startAnimation(startAnimation);

        Spot.setX(920);
        Spot.setY(8);

        //--Set circle button GONE
        disappear();

        /*--- For Firebase config ---*/
        databaseRetail = FirebaseDatabase.getInstance().getReference("trx_retail");

        //--- Get value from Previous intent
        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        storeNm = bundle.getString("storeNm");
        storeDv = bundle.getString("storeDv");



        img1.setOnTouchListener(handleTouch);
        img2.setOnTouchListener(handleTouch);
        setupVoucher();
    }

    private void disappear() {
        c1a.setVisibility(View.GONE);
        c1b.setVisibility(View.GONE);
        circleMenuA.setVisibility(View.GONE);
        circleMenuB.setVisibility(View.GONE);
        circleTopA.setVisibility(View.GONE);
        circleTopB.setVisibility(View.GONE);
        circleSearchA.setVisibility(View.GONE);
        circleSearchB.setVisibility(View.GONE);
        circleHulk.setVisibility(View.GONE);
        circleCaptain.setVisibility(View.GONE);
        btnEnd.setVisibility(View.GONE);
    }


    public void setupVoucher() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Spot1Activity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_msisdn, null);

        final EditText editEmail = (EditText) mView.findViewById(R.id.editEmail);
        final EditText editNumber = (EditText) mView.findViewById(R.id.editNumber);
        final String action = getString(R.string.action_intent_spot);
        final String rewards = getString(R.string.string_spot_rewards);
        final String message = getString(R.string.string_spot_rewards_msg);
        Button btnVoucher = (Button) mView.findViewById(R.id.btnSaveDetail);
        Button btnCancelVoucher = (Button) mView.findViewById(R.id.btnCancel);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setIcon(android.R.drawable.sym_def_app_icon);
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setTitle(R.string.dialog_setup_title);
        dialog.show();
        btnVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editEmail.getText().toString().isEmpty() && !editNumber.getText().toString().isEmpty()) {
                    if (!isValidEmail(editEmail.getText().toString())) {
                        /*--- Send error alert once got error ---*/
                        editEmail.setError(getString(R.string.string_spot_error_invalid_email));
                        return;
                    }

                    // create a ContentValues object where column names are keys,
                    ContentValues values = new ContentValues();
                    values.put(TrxContract.trxEntry.COLUMN_DATE, getDateTime());
                    values.put(TrxContract.trxEntry.COLUMN_STORENAME, storeNm);
                    values.put(TrxContract.trxEntry.COLUMN_STOREDEVICE, storeDv);
                    values.put(TrxContract.trxEntry.COLUMN_EMAIL, editEmail.getText().toString().trim());
                    values.put(TrxContract.trxEntry.COLUMN_PHONE, editNumber.getText().toString().trim());
                    values.put(TrxContract.trxEntry.COLUMN_REWARDS, rewards);

                    if (currentUri == null) {
                        Uri newRowId = getContentResolver().insert(TrxContract.trxEntry.CONTENT_URI2, values);
                        ToastOX.ok(getApplicationContext(), getString(R.string.string_spot_savedata) + newRowId);
                    } else {
                        ToastOX.error(getApplicationContext(), getString(R.string.string_spot_savedata_error));
                    }
                    dialog.dismiss();
                    //Send Data to Firebase
                    saveFireBase(editEmail.getText().toString().trim(), editNumber.getText().toString().trim(), rewards);

                } else {
                    return;
                }
            }
        });
        btnCancelVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    // Save data to firebase
    private void saveFireBase(String userEmail, String userPhone, String rewards) {
        /*--- Push Data to firebase : trx_retail */
        String id = databaseRetail.push().getKey();
        String actionIntent = getString(R.string.action_intent_spot);
        // Creating object
        TrxRetail tblRetail = new TrxRetail(id, getDateTime(), storeNm, storeDv, actionIntent, userEmail, userPhone, rewards);
        // Save data
        databaseRetail.child(id).setValue(tblRetail);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void submitEmail(String toEmail, String subjectEmail, String emailMessage) {

        String fromEmail = getString(R.string.string_video_fromemail);
        //---Creating SendMail object
        SendMail sm = new SendMail(this, fromEmail, toEmail, subjectEmail, emailMessage);
        //---Executing sendmail to send email
        sm.execute();
    }


    /*--- Disable back button , enable only once END button visible--*/
    /*--- No custom Back button to follow the guideline for this task
    *- App supports standard system Back button navigation and does not make use of any custom, on-screen "Back button" prompts.
    *- All dialogs are dismissible using the Back button.
    --*/


    @Override
    public void onBackPressed() {
        /*AlertDialog.Builder dBuilder = new AlertDialog.Builder(Spot1Activity.this);
        dBuilder.setIcon(R.mipmap.ic_launcher);
        dBuilder.setTitle(R.string.spot_dialog_leave);
        dBuilder.setMessage(R.string.spot_dialog_msg_leave);
        //dBuilder.setCancelable(false);
        dBuilder.setPositiveButton(getString(R.string.spot_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {*/
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            /*}
        });
        dBuilder.setNegativeButton(getString(R.string.spot_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //-- End session go back to MainActivity
                //submitEnd();
            }
        });
        AlertDialog alertDialog = dBuilder.create();
        alertDialog.show();*/
    }


    @OnClick(R.id.btnEnd)
    public void submitEnd() {

        AlertDialog.Builder dBuilder = new AlertDialog.Builder(Spot1Activity.this);
        dBuilder.setIcon(R.mipmap.ic_launcher);
        dBuilder.setTitle(R.string.dialog_title);
        dBuilder.setMessage(R.string.dialog_msg_spot1);
        //dBuilder.setCancelable(true);
        dBuilder.setPositiveButton(getString(R.string.spot_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        dBuilder.setNegativeButton(getString(R.string.spot_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //-- End session go back to MainActivity
                //submitEnd();
            }
        });
        AlertDialog alertDialog = dBuilder.create();
        alertDialog.show();
    }

    //--- GetPosition & add circle into it
    public View.OnTouchListener handleTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i(LOG_TAG, "touched down");
                    // Getting X coordinate
                    mX = event.getX();
                    // Getting Y Coordinate
                    mY = event.getY();
                    //---Give position
                    textPos.setText(getString(R.string.spot_activity_x) + mX + getString(R.string.spot_activity_comma) + getString(R.string.spot_activity_y) + mY);
                    if (mX >= 155 && mX <= 230 && mY >= 2 && mY <= 50) { //-- 3g&4g spot
                        if (!c1a.isShown() && !c1b.isShown()) {
                            c1a.setVisibility(View.VISIBLE);
                            c1b.setVisibility(View.VISIBLE);
                            c1a.setX(390);
                            c1a.setY(45);
                            c1b.setX(1470);
                            c1b.setY(45);
                        }

                        ToastOX.info(getApplicationContext(), getString(R.string.string_spot_lte));


                    } else if (mX >= 34 && mX <= 150 && mY >= 210 && mY <= 285) { //--Top20 spot
                        if (!circleTopA.isShown() && !circleTopB.isShown()) {
                            circleTopA.setVisibility(View.VISIBLE);
                            circleTopB.setVisibility(View.VISIBLE);
                            circleTopA.setX(340);
                            circleTopA.setY(280);
                            circleTopB.setX(1470);
                            circleTopB.setY(280);
                        }
                        ToastOX.info(getApplicationContext(), getString(R.string.string_spot_topmovie));


                    } else if (mX >= 350 && mX <= 675 && mY >= 175 && mY <= 280) { //--Almost famous spot
                        if (!circleMenuA.isShown() && !circleMenuB.isShown()) {
                            circleMenuA.setVisibility(View.VISIBLE);
                            circleMenuB.setVisibility(View.VISIBLE);
                            circleMenuA.setX(681);
                            circleMenuA.setY(280);
                            circleMenuB.setX(1850);
                            circleMenuB.setY(280);
                        }
                        ToastOX.info(getApplicationContext(), getString(R.string.string_spot_sortmovie));

                    } else if (mX >= 600 && mX <= 700 && mY >= 59 && mY <= 130) { //--Search button spot
                        if (!circleSearchA.isShown() && !circleSearchB.isShown()) {
                            circleSearchA.setVisibility(View.VISIBLE);
                            circleSearchB.setVisibility(View.VISIBLE);
                            circleSearchA.setX(839);
                            circleSearchA.setY(140);
                            circleSearchB.setX(1940);
                            circleSearchB.setY(140);
                        }
                        ToastOX.info(getApplicationContext(), getString(R.string.string_spot_searchbtn));

                    } else if (mX >= 42 && mX <= 280 && mY >= 749 && mY <= 810) { //--Hulk spot
                        if (!circleHulk.isShown() && !circleCaptain.isShown()) {
                            circleHulk.setVisibility(View.VISIBLE);
                            circleCaptain.setVisibility(View.VISIBLE);
                            circleHulk.setX(330);
                            circleHulk.setY(830);
                            circleCaptain.setX(1470);
                            circleCaptain.setY(830);
                        }
                        ToastOX.info(getApplicationContext(), getString(R.string.string_spot_hulk));

                    }
            }
            circleEnable();
            return true;
        }

    };

    public boolean circleEnable() {
        if (c1a.isShown() && circleTopA.isShown() && circleMenuA.isShown() && circleSearchA.isShown() && circleHulk.isShown()) {
            btnEnd.setVisibility(View.VISIBLE);
            btnEnd.setY(110);
        } //else {
        //}
        return true;
    }

    /*
    * Validate idEmail, Min 5 Char, Max 25 Char
    */
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


}
