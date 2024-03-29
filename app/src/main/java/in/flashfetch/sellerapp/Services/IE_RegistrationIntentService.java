package in.flashfetch.sellerapp.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Network.PostRequest;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.UserProfile;

import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kevin selva prasanna on 24-Aug-15.
 */
public class IE_RegistrationIntentService extends IntentService {
    private static final String TAG = "RegistrationIntentService";
    private static final String[] TOPICS = {"global"};
    private String Old_ID,token;
    private Boolean tokenSentToServer;

    public IE_RegistrationIntentService() {
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Old_ID = sharedPreferences.getString("token", "");

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // [START register_for_gcm]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                // [START get_token]
                token ="no  token";
                InstanceID instanceID = InstanceID.getInstance(this);
                /*String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);*/
                token = instanceID.getToken("475795801819", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                if((!token.equals(Old_ID)))
                    sharedPreferences.edit().putBoolean("sentTokenToServer", false).apply();
                // [END get_token]
                Log.i(TAG, "GCM Registration Token:" + token);

                // TODO: Implement this method to send any registration to your app's servers.

                tokenSentToServer = sharedPreferences.getBoolean("sentTokenToServer", false);
//                if((!token.equals(Old_ID))||(!tokenSentToServer))
                    sendRegistrationToServer(token);

                // Subscribe to topic channels
                subscribeTopics(token);

                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.


                // [END register_for_gcm]
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean("sentTokenToServer", false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        /*Intent registrationComplete = new Intent("registrationComplete");
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);*/
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * token The new token.
     */

    private void sendRegistrationToServer(String freshtoken) {

        ArrayList<PostParam> PostParams = new ArrayList<PostParam>();

        PostParams.add(new PostParam("email", UserProfile.getEmail(IE_RegistrationIntentService.this)));
        PostParams.add(new PostParam("gcmid",freshtoken));
        PostParams.add(new PostParam("token",UserProfile.getToken(IE_RegistrationIntentService.this)));

        JSONObject ResponseJSON = PostRequest.execute(URLConstants.URLGCM_Register, PostParams, null);
        Log.d("RESPONSE",ResponseJSON.toString());
    }


    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
