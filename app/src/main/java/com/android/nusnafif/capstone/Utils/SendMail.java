package com.android.nusnafif.capstone.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.nusnafif.capstone.R;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by NUSNAFIF on 3/22/2017.
 */

public class SendMail extends AsyncTask<Void, Void, Void> {

    public static final String EMAIL =""; //your-gmail-username
    public static final String PASSWORD =""; //your-gmail-password

    //Declaring Variables
    private Context context;
    private Session session;

    //Information to send email
    private String fromEmail;
    private String email;
    private String subject;
    private String message;

    //Progress dialog to show while sending email
    private ProgressDialog progressDialog;

    //Class Constructor
    public SendMail(Context context, String fromEmail, String email, String subject, String message) {
        //Initializing variables
        this.context = context;
        this.fromEmail = fromEmail;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        if (new NetworkUtil(context).isConnected()) {
//            //Showing progress dialog while sending email
//            progressDialog = ProgressDialog.show(context, "Sending message", "Please wait...", false, false);
//        } else {
//            Toast.makeText(context, "Internet connection error!!!", Toast.LENGTH_LONG).show();
//            return;
//        }
//        progressDialog = ProgressDialog.show(context, "Sending message", "Please wait...", false, false);
        progressDialog = ProgressDialog.show(context, context.getResources().getString(R.string.sendmail_sendingmail), context.getResources().getString(R.string.sendmail_wait), false, false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context, R.string.sendmail_msg_sent, Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put(R.string.sendmail_smtp_host, R.string.sendmail_smtp_host_value);
        props.put(R.string.sendmail_smtp_port_socket, R.string.sendmail_smtp_port_socket_value);
        props.put(R.string.sendmail_smtp_socket_class, R.string.sendmail_smtp_socket_class_value);
        props.put(R.string.sendmail_smtp_auth, R.string.sendmail_smtp_auth_value);
        props.put(R.string.sendmail_smtp_port, R.string.sendmail_smtp_port_value);

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        /*return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);*/
                        return new PasswordAuthentication(EMAIL, PASSWORD);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(EMAIL));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mm.setSubject(subject);
            //Adding message
            mm.setText(message);

            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
