package in.flashfetch.sellerapp.Network;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Constants.Constants;
import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Helper.DatabaseHelper;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.MainActivity;
import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.SignUpObject;
import in.flashfetch.sellerapp.Objects.UserProfile;
import in.flashfetch.sellerapp.Returns;

/**
 * Created by KRANTHI on 03-07-2016.
 */
public class ServiceManager {

    public static void callSignUpService(Context context, SignUpObject signUpObject, final UIListener uiListener) {

        if(signUpObject.isDummyObject()){
            UserProfile.setName(signUpObject.getName(), context);
            UserProfile.setEmail(signUpObject.getEmail(), context);
            UserProfile.setPhone(signUpObject.getPhone(), context);
            UserProfile.setPassword(signUpObject.getPassword(),context);
            UserProfile.setShopId(signUpObject.getShopId(), context);
            UserProfile.setShopPhone(signUpObject.getPhone(), context);
            UserProfile.setAddress1(signUpObject.getShopAddress1(), context);
            UserProfile.setAddress2(signUpObject.getShopAddress2(), context);
            UserProfile.setShopName(signUpObject.getShopName(), context);
            UserProfile.setLocation(signUpObject.getShopLocation(),context);

            uiListener.onSuccess();
        }

        SignUpTask signUpTask = new SignUpTask(context,signUpObject,uiListener);
        if(signUpTask != null){
            return;
        }
        signUpTask.execute();
    }

    public static class SignUpTask extends AsyncTask<String, Void, Void> {

        private JSONObject response;
        private String name, email, password, phone, shopName, shopId, shopTelephone, address1, address2, shopLocation;
        private Context context;
        private UIListener uiListener;

        public SignUpTask(Context context, SignUpObject signUpObject, final UIListener uiListener){

            this.context = context;
            this.uiListener = uiListener;
            this.name = signUpObject.getName();
            this.email = signUpObject.getEmail();
            this.password = signUpObject.getPassword();
            this.phone = signUpObject.getPhone();
            this.shopName = signUpObject.getShopName();
            this.shopId = signUpObject.getShopId();
            this.shopTelephone = signUpObject.getShopTelephone();
            this.address1 = signUpObject.getShopAddress1();
            this.address2 = signUpObject.getShopAddress2();
            this.shopLocation = signUpObject.getShopLocation();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            ArrayList<PostParam> instiPostParams = new ArrayList<>();
            instiPostParams.add(new PostParam("name", name));
            instiPostParams.add(new PostParam("email", email));
            instiPostParams.add(new PostParam("password", password));
            instiPostParams.add(new PostParam("mobile",phone));
            instiPostParams.add(new PostParam("shopName",shopName));
            instiPostParams.add(new PostParam("address1",address1));
            instiPostParams.add(new PostParam("address2",address2));
            instiPostParams.add(new PostParam("sid",shopId));
            instiPostParams.add(new PostParam("office",shopTelephone));
            instiPostParams.add(new PostParam("sel_loc",shopLocation));


            response = PostRequest.execute(URLConstants.URLSignup, instiPostParams, null);
            Log.d("RESPONSE", response.toString());

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            //tvv.setText(response.toString());
            super.onPostExecute(aVoid);
            try {
                if(response.getJSONObject("data").getInt("result") == 1) {
                    UserProfile.setToken(response.getJSONObject("data").getString("token"), context);
                    uiListener.onSuccess();

                }else if(response.getJSONObject("data").getInt("result") == 0) {
                    uiListener.onFailure();
                }
                else{
                    uiListener.onConnectionError();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }


        }

    }

    public static void callLoginService(Context context, String email, String password, final UIListener uiListener) {

        if(email.equals(Constants.DUMMY_EMAIL) && password.equals(Constants.DUMMY_PASSWORD)){
            UserProfile.setEmail(Constants.DUMMY_SIGN_UP_OBJECT.getEmail(), context);
            UserProfile.setPassword(Constants.DUMMY_SIGN_UP_OBJECT.getPassword(),context);
            UserProfile.setName(Constants.DUMMY_SIGN_UP_OBJECT.getName(),context);
            UserProfile.setPhone(Constants.DUMMY_SIGN_UP_OBJECT.getPhone(),context);
            UserProfile.setShopId(Constants.DUMMY_SIGN_UP_OBJECT.getShopId(),context);
            UserProfile.setShopName(Constants.DUMMY_SIGN_UP_OBJECT.getShopName(),context);
            UserProfile.setShopPhone(Constants.DUMMY_SIGN_UP_OBJECT.getShopTelephone(),context);
            UserProfile.setAddress1(Constants.DUMMY_SIGN_UP_OBJECT.getShopAddress1(),context);
            UserProfile.setAddress2(Constants.DUMMY_SIGN_UP_OBJECT.getShopAddress1(),context);
            UserProfile.setLocation(Constants.DUMMY_SIGN_UP_OBJECT.getShopLocation(),context);
            uiListener.onSuccess();
        }

        UserLoginTask userLoginTask = new UserLoginTask(context,email,password,uiListener);
        if(userLoginTask != null){
            return;
        }
        userLoginTask.execute();
    }

    public static class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject response;
        private Context context;
        private String mEmail;
        private String mPassword;
        private UIListener uiListener;

        public UserLoginTask(Context context,String email, String password, final UIListener uiListener) {
            this.context = context;
            mEmail = email;
            mPassword = password;
            this.uiListener = uiListener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            ArrayList<PostParam> postParams = new ArrayList<>();
            postParams.add(new PostParam("email", mEmail));
            postParams.add(new PostParam("pass",mPassword));

            response = PostRequest.execute(URLConstants.URLLogin, postParams, null);
            Log.d("RESPONSE", response.toString());

            try {
                if(response.getJSONObject("data").getInt("result") == 1) {

                    JSONObject trans = new JSONObject();
                    JSONObject translist = response.getJSONObject("data").getJSONObject("Transactions");
                    Notification not;

                    for (int i=0; i<response.getJSONObject("data").getInt("length"); i++){
                        trans = translist.getJSONObject(String.valueOf(i));
                        not = new Notification(trans);
//                        Log.d(LOG_TAG, "Category of event is " + event.getCate)
                        not.saveNot(context);
                    }

                    UserProfile.setEmail(mEmail, context);
                    UserProfile.setAccess(response.getJSONObject("data").getBoolean("accesscode"),context);
                    UserProfile.setCategory(response.getJSONObject("data").getInt("cat"), context);
                    UserProfile.setToken(response.getJSONObject("data").getString("token"), context);
                    UserProfile.setName(response.getJSONObject("data").getString("user"), context);
                    UserProfile.setPhone(response.getJSONObject("data").getString("mobile"), context);
                    UserProfile.setPassword(response.getJSONObject("data").getString("password"), context);
                    UserProfile.setShopId(response.getJSONObject("data").getString("shopid"), context);
                    UserProfile.setShopPhone(response.getJSONObject("data").getString("office_no"), context);
                    UserProfile.setAddress1(response.getJSONObject("data").getString("Address1"), context);
                    UserProfile.setAddress2(response.getJSONObject("data").getString("Address2"), context);
                    UserProfile.setShopName(response.getJSONObject("data").getString("shopName"), context);
                    UserProfile.setLocation(response.getJSONObject("data").getString("sel_loc"), context);

                    return true;

                }
                else if(response.getJSONObject("data").getInt("result") == 0) {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            try {
                if (response.getJSONObject("data").getInt("result")==1) {
                    uiListener.onSuccess();
                } else if(response.getJSONObject("data").getInt("result") == 0){
                    uiListener.onFailure();
                }else {
                    uiListener.onConnectionError();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }

        @Override
        protected void onCancelled() {
            uiListener.onCancelled();
        }
    }

    public static void callForgotPasswordService(Context context, String email, final UIListener uiListener) {

        ForgotPasswordTask forgotPasswordTask = new ForgotPasswordTask(context,email,uiListener);
        if(forgotPasswordTask != null){
            return;
        }
        forgotPasswordTask.execute();
    }

    public static class ForgotPasswordTask extends AsyncTask<Void, Void, Void>{

        private JSONObject response;
        private String email;
        private Context context;
        private UIListener uiListener;

        public ForgotPasswordTask(Context context, String email, final UIListener uiListener){
            this.context = context;
            this.email = email;
            this.uiListener = uiListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<PostParam> postParams = new ArrayList<>();
            postParams.add(new PostParam("email",email));

            response = PostRequest.execute(URLConstants.URL_FORGOT_PASSWORD,postParams,null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try{
                if(response.getJSONObject("data").getInt("result") == 1){
                    uiListener.onSuccess();
                }else if(response.getJSONObject("data").getInt("result") == 0){
                    uiListener.onFailure();
                }else{
                    uiListener.onConnectionError();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            uiListener.onCancelled();
        }
    }

    public static void callPasswordVerificationService(Context context, String email, String verificationCode, final UIListener uiListener) {

        PasswordVerificationTask passwordVerificationTask = new PasswordVerificationTask(context,email,verificationCode,uiListener);
        if(passwordVerificationTask != null){
            return;
        }
        passwordVerificationTask.execute();
    }

    public static class PasswordVerificationTask extends AsyncTask<Void,Void,Void>{

        private JSONObject response;
        private String email,verificationCode;
        private UIListener uiListener;

        public PasswordVerificationTask(Context context, String email, String verificationCode, final UIListener uiListener){
            this.email = email;
            this.verificationCode = verificationCode;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<PostParam> postParams = new ArrayList<>();
            postParams.add(new PostParam("email",email));
            postParams.add(new PostParam("code",verificationCode));

            response = PostRequest.execute(URLConstants.URL_PASSWORD_VERIFICATION,postParams,null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try{
                if(response.getJSONObject("data").getInt("result") == 1){
                    uiListener.onSuccess();
                }else if(response.getJSONObject("data").getInt("result") == 0){
                    uiListener.onFailure();
                }else{
                    uiListener.onConnectionError();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            uiListener.onCancelled();
        }
    }

    public static void callPasswordChangeService(Context context, String email, String password, final UIListener uiListener) {

        ChangePasswordTask changePasswordTask = new ChangePasswordTask(context,email,password,uiListener);
        if(changePasswordTask != null){
            return;
        }
        changePasswordTask.execute();
    }

    public static class ChangePasswordTask extends AsyncTask<Void,Void,Void> {

        private JSONObject response;
        private String email;
        private String password;
        private UIListener uiListener;

        public ChangePasswordTask(Context context, String email, String password, final UIListener uiListener){
            this.email = email;
            this.password = password;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<PostParam> postParams = new ArrayList<>();
            postParams.add(new PostParam("email",email));
            postParams.add(new PostParam("password",password));

            response = PostRequest.execute(URLConstants.URL_PASSWORD_CHANGE,postParams,null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try{
                if(response.getJSONObject("data").getInt("result") == 1){
                    uiListener.onSuccess();
                }else{
                    uiListener.onFailure();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            uiListener.onConnectionError();
        }
    }

    public static void callAccessCodeVerificationService(Context context, String token, String email, String accessCode, final UIListener uiListener) {

        AccessVerifyTask accessVerifyTask = new AccessVerifyTask(context,token,email,accessCode,uiListener);
        if(accessVerifyTask != null){
            return;
        }
        accessVerifyTask.execute();
    }

    public static class AccessVerifyTask extends AsyncTask<Void,Void,Void> {

        private JSONObject response;
        private String token,email,accessCode;
        private UIListener uiListener;

        public AccessVerifyTask(Context context, String token, String email, String accessCode, final UIListener uiListener){
            this.token = token;
            this.email = email;
            this.accessCode = accessCode;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<PostParam> postParams = new ArrayList<>();
            postParams.add(new PostParam("token",token));
            postParams.add(new PostParam("email",email));
            postParams.add(new PostParam("accessCode",accessCode));

            response = PostRequest.execute(URLConstants.URL_ACCESS_CODE_VERIFICATION,postParams,null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try{
                if(response.getJSONObject("data").getInt("result") == 1){
                    uiListener.onSuccess();
                }else{
                    uiListener.onFailure();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            uiListener.onCancelled();
        }
    }

    public static void callRequestAdsService(Context context, String ad, String startDate, String endDate, final UIListener uiListener) {

        RequestAdTask requestAdTask = new RequestAdTask(context,ad,startDate,endDate,uiListener);
        if(requestAdTask != null){
            return;
        }
        requestAdTask.execute();
    }

    public static class RequestAdTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject response;
        private String ad,startDate,endDate;
        private UIListener uiListener;
        private Context context;

        public RequestAdTask(Context context, String ad, String startDate, String endDate, final UIListener uiListener){
            this.ad = ad;
            this.uiListener = uiListener;
            this.context = context;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ArrayList<PostParam> PostParams = new ArrayList<PostParam>();
            PostParams.add(new PostParam("email", UserProfile.getEmail(context)));
            PostParams.add(new PostParam("token", UserProfile.getToken(context)));
            PostParams.add(new PostParam("adv",ad));
            PostParams.add(new PostParam("startDate",startDate));
            PostParams.add(new PostParam("endDate",endDate));

            response = PostRequest.execute(URLConstants.URLAd, PostParams, null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            try{
                if(response.getJSONObject("data").getInt("result") == 1){
                    uiListener.onSuccess();
                }else{
                    uiListener.onFailure();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            uiListener.onCancelled();
        }
    }

    public static void callFeedBackService(Context context, String feedBack, final UIListener uiListener) {

        FeedBackTask feedBackTask = new FeedBackTask(context,feedBack,uiListener);
        if(feedBackTask != null){
            return;
        }
        feedBackTask.execute();
    }

    public static class FeedBackTask extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        private JSONObject response;
        private String feedBack;
        private UIListener uiListener;

        public FeedBackTask(Context context, String feedBack, final UIListener uiListener){
            this.context = context;
            this.feedBack = feedBack;
            this.uiListener = uiListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            ArrayList<PostParam> PostParams = new ArrayList<PostParam>();
            PostParams.add(new PostParam("email",UserProfile.getEmail(context)));
            PostParams.add(new PostParam("token",UserProfile.getToken(context)));
            PostParams.add(new PostParam("feed",feedBack));

            response = PostRequest.execute(URLConstants.URLFeedback, PostParams, null);
            Log.d("RESPONSE", response.toString());


            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            try{
                if(response.getJSONObject("data").getInt("result") == 1){
                    uiListener.onSuccess();
                }else{
                    uiListener.onFailure();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            uiListener.onCancelled();
        }
    }

    public static void callReturnPolicyService(Context context, int returnsAccepted, final UIListener uiListener) {

        ReturnsPolicyTask returnsPolicyTask = new ReturnsPolicyTask(context,returnsAccepted,uiListener);
        if(returnsPolicyTask != null){
            return;
        }
        returnsPolicyTask.execute();
    }

    public static class ReturnsPolicyTask extends AsyncTask<Void, Void, Void> {

        private JSONObject response;
        private int returnsAccepted;
        private Context context;
        private UIListener uiListener;

        public ReturnsPolicyTask(Context context, int returnsAccepted, final UIListener uiListener){
            this.context = context;
            this.returnsAccepted = returnsAccepted;
            this.uiListener = uiListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<PostParam> requestParams = new ArrayList<PostParam>();

            requestParams.add(new PostParam("email", UserProfile.getEmail(context)));
            requestParams.add(new PostParam("token", UserProfile.getToken(context)));
            requestParams.add(new PostParam("returnAccepted", returnsAccepted+""));

            response = PostRequest.execute(URLConstants.URL_RETURNS, requestParams, null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //tvv.setText(ResponseJSON.toString());
            super.onPostExecute(aVoid);
            try {
                if (response.getJSONObject("data").getInt("result") == 1) {
                    uiListener.onSuccess();

                }else {
                    uiListener.onFailure();

                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }
    }

    public static void callReferralCodeService(Context context, String shopId, final UIListener uiListener) {

        ReferralCodeTask referralCodeTask = new ReferralCodeTask(context,shopId,uiListener);
        if(referralCodeTask != null){
            return;
        }
        referralCodeTask.execute();
    }

    public static class ReferralCodeTask extends AsyncTask<Void, Void, Void> {

        private JSONObject response;
        private String shopId;
        private Context context;
        private UIListener uiListener;

        public ReferralCodeTask(Context context, String shopId, final UIListener uiListener){
            this.context = context;
            this.uiListener = uiListener;
            this.shopId = shopId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<PostParam> requestParams = new ArrayList<PostParam>();

            requestParams.add(new PostParam("email", UserProfile.getEmail(context)));
            requestParams.add(new PostParam("token", UserProfile.getToken(context)));
            requestParams.add(new PostParam("shopId", shopId));

            response = PostRequest.execute(URLConstants.URL_FETCH_REFERRAL_CODE, requestParams, null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //tvv.setText(ResponseJSON.toString());
            super.onPostExecute(aVoid);
            try {
                if (response.getJSONObject("data").getInt("result") == 1) {
                    uiListener.onSuccess();
                    UserProfile.setReferralCode(response.getJSONObject("data").getString("referralcode"),context);
                }else {
                    uiListener.onFailure();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }
    }
}
