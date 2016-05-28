package in.flashfetch.sellerapp.Services;


/**
 * Created by kevin selva prasanna on 24-Aug-15.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import in.flashfetch.sellerapp.Empty_1;
import in.flashfetch.sellerapp.Helper.DatabaseHelper;
import in.flashfetch.sellerapp.MainActivity;
import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.Objects.UserProfile;
import in.flashfetch.sellerapp.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class IE_GCMListenerService extends GcmListenerService{

    private static final String TAG = "GcmListenerService";
    String message=" ";
    String data="";
    String type=" ",category,scoreBoardId,title=" ";



    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */
        //JSONObject notjs;
        Log.i("GCM-message",data.toString());
        if(!(data.getString("not")==null)){
            ContentValues cv = new ContentValues();
            cv.put("id", data.getString("id"));
            cv.put("text", data.getString("text"));
            cv.put("time",data.getString("time"));
            DatabaseHelper dh = new DatabaseHelper(this);
            dh.add(cv);
            sendNotification("FlashFetch",data.getString("text"));
        }
        else {
          //try {
            //notjs = new JSONObject(String.valueOf(data));
            //Notification not = new Notification(notjs);
            ContentValues cv = new ContentValues();
            cv.put("name", data.getString("name"));
            cv.put("imgurl", data.getString("imgurl"));
            cv.put("price", data.getString("price"));
            cv.put("loc", data.getString("loc"));
            cv.put("url",data.getString("url"));
            cv.put("buyer_name", data.getString("buyer_name"));
            //cv.put("price", Integer.parseInt(data.getString("price")));
            cv.put("time", Long.parseLong(data.getString("time")));
            cv.put("id", data.getString("id"));
            cv.put("timesent",Long.parseLong(data.getString("timenow")));
            if (data.getString("quoted").equals("1")) {
                cv.put("quoted", 1);
                cv.put("qprice", Long.parseLong(data.getString("qprice")));
                cv.put("type", Integer.parseInt(data.getString("type")));
                cv.put("deltype", Integer.parseInt(data.getString("deltype")));
                cv.put("comment", data.getString("comment"));
                if(data.getString("bargained").equals("1")){
                    cv.put("bargained",1);
                    cv.put("bgprice",data.getString("bgprice"));
                    cv.put("bgexptime",Long.parseLong(data.getString("bgexptime")));
                    long yourmilliseconds =  Long.parseLong(data.getString("bgexptime"));
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                    Date resultdate = new Date(yourmilliseconds);
                    if(data.getString("cuscon").equals("0")){
                        sendNotification("Bargain Request", "You have a bargain request for the deal provided on " + data.getString("name") + ". You have to respond by" + resultdate);
                        cv.put("del",data.getInt("del"));
                        cv.put("buyno",data.getLong("buyno"));
                    }

                }else ;
                cv.put("selcon", Integer.parseInt(data.getString("selcon")));
                if(data.getString("cuscon").equals("1")){
                    cv.put("cuscon", 1);
                    sendNotification("Deal Accepted", "Congratulations! Your deal on product " + data.getString("name") + "is accepted by the buyer.");
                }
            } else {
                cv.put("quoted", 0);
                long yourmilliseconds =  Long.parseLong(data.getString("time"));
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                Date resultdate = new Date(yourmilliseconds);
                sendNotification("Product Request", "You have a new Flashfech request on " + data.getString("name") + ", You have to respond before" + resultdate);
            }
            DatabaseHelper dh = new DatabaseHelper(this);
            dh.addNot(cv);



            //sendNotification(data.getString("name"), data.getString("imgurl"), data.getString("price"), data.getString("id"), data.getString("time"));
    }
    }

  /*  private void app(Bundle data) {
        *//*if(data.getString("text").equals("counter")){
            UserProfile.setCounter(1,IE_GCMListenerService.this);
        }else if(data.getString("text").equals("update")){
            UserProfile.setUpdate(1, IE_GCMListenerService.this);
        }else if(data.getString("text").equals("updatecancel")){
            UserProfile.setUpdate(0, IE_GCMListenerService.this);
        } if(data.getString("text").equals("countercancel")){
            UserProfile.setCounter(0, IE_GCMListenerService.this);
        }else {
            UserProfile.setText(data.getString("text"), IE_GCMListenerService.this);
        }*//*
       sendNotification(data.getString("title"),data.getString("message"));

    }*/
    // [END receive_message]

   /* *//**
     * Create and show a simple notification containing the received GCM message.
     *
     * message GCM message received.
     */
    private void sendNotification(String email, String category, String price,String id, String time){
        PendingIntent pendingIntent;
        Uri defaultSoundUri;
        NotificationCompat.Builder notificationBuilder;
        Intent i = new Intent(IE_GCMListenerService.this, MainActivity.class);
        NotificationManager notificationManager;
        pendingIntent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_ONE_SHOT);



        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.nav)
                .setContentTitle("FlashFetch")
                .setContentText(email + " at price" +  price)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        android.app.Notification not = notificationBuilder.build();
        not.flags = android.app.Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify((int) (Math.random() * 1000), not);


    }
    private void sendNotification(String title, String message){
        PendingIntent pendingIntent;
        Uri defaultSoundUri;
        NotificationCompat.Builder notificationBuilder;
        Intent i = new Intent(IE_GCMListenerService.this,MainActivity.class);
        NotificationManager notificationManager;
        pendingIntent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_ONE_SHOT);




        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.nav_transparent)
            .setColor(Color.RED);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.nav);
        }

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int)(Math.random()*1000) , notificationBuilder.build());


    }

}
