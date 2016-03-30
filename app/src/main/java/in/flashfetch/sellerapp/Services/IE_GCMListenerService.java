package in.flashfetch.sellerapp.Services;


/**
 * Created by kevin selva prasanna on 24-Aug-15.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import in.flashfetch.sellerapp.Empty_1;
import in.flashfetch.sellerapp.Helper.DatabaseHelper;
import in.flashfetch.sellerapp.MainActivity;
import in.flashfetch.sellerapp.Objects.UserProfile;
import in.flashfetch.sellerapp.R;

import org.json.JSONObject;


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
        if(!(data.getString("app")==null)){
            sendNotification(data.getString("title"),data.getString("message"));
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
            cv.put("buyer_name", data.getString("buyer_name"));
            //cv.put("price", Integer.parseInt(data.getString("price")));
            cv.put("time", Long.parseLong(data.getString("time")));
            cv.put("id", data.getString("id"));
            if (data.getString("quoted").equals("1")) {
                cv.put("quoted", 1);
                cv.put("qprice", Long.parseLong(data.getString("type")));
                cv.put("type", Integer.parseInt(data.getString("type")));
                cv.put("deltype", Integer.parseInt(data.getString("deltype")));
                cv.put("comment", data.getString("comment"));
                cv.put("cuscon", Integer.parseInt(data.getString("cuscon")));
            } else {
                cv.put("quoted", 0);
            }
            DatabaseHelper dh = new DatabaseHelper(this);
            dh.addNot(cv);



            sendNotification(data.getString("name"), data.getString("imgurl"), data.getString("price"), data.getString("id"), data.getString("time"));
    }
    }

    private void app(Bundle data) {
        /*if(data.getString("text").equals("counter")){
            UserProfile.setCounter(1,IE_GCMListenerService.this);
        }else if(data.getString("text").equals("update")){
            UserProfile.setUpdate(1, IE_GCMListenerService.this);
        }else if(data.getString("text").equals("updatecancel")){
            UserProfile.setUpdate(0, IE_GCMListenerService.this);
        } if(data.getString("text").equals("countercancel")){
            UserProfile.setCounter(0, IE_GCMListenerService.this);
        }else {
            UserProfile.setText(data.getString("text"), IE_GCMListenerService.this);
        }*/
       sendNotification(data.getString("title"),data.getString("message"));

    }
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
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FlashFetch")
                .setContentText(email + " at price" +  price)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) (Math.random() * 1000), notificationBuilder.build());


    }
    private void sendNotification(String title, String message){
        PendingIntent pendingIntent;
        Uri defaultSoundUri;
        NotificationCompat.Builder notificationBuilder;
        Intent i = new Intent(IE_GCMListenerService.this,Empty_1.class);
        NotificationManager notificationManager;
        pendingIntent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_ONE_SHOT);



        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int)(Math.random()*1000) , notificationBuilder.build());


    }

}
