package in.flashfetch.sellerapp.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.flashfetch.sellerapp.CommonUtils.Toasts;
import in.flashfetch.sellerapp.Constants.Constants;
import in.flashfetch.sellerapp.Constants.URLConstants;
import in.flashfetch.sellerapp.Helper.DatabaseHelper;
import in.flashfetch.sellerapp.Interfaces.UIListener;
import in.flashfetch.sellerapp.Objects.PostParam;
import in.flashfetch.sellerapp.Objects.QuoteObject;
import in.flashfetch.sellerapp.Objects.ShopInfoObject;
import in.flashfetch.sellerapp.Objects.SignUpObject;
import in.flashfetch.sellerapp.Objects.Transactions;
import in.flashfetch.sellerapp.Objects.UserProfile;
import in.flashfetch.sellerapp.R;
import in.flashfetch.sellerapp.Returns;
import in.flashfetch.sellerapp.ShopDetailsActivity;

/**
 * Created by KRANTHI on 03-07-2016.
 */
public class ServiceManager {

    public static void callSignUpService(Context context, SignUpObject signUpObject, final UIListener uiListener) {

        SignUpTask signUpTask = new SignUpTask(context,signUpObject,uiListener);
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
            instiPostParams.add(new PostParam("pass", password));
            instiPostParams.add(new PostParam("mobile",phone));
            instiPostParams.add(new PostParam("shopname",shopName));
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

//        if(email.equals(Constants.DUMMY_EMAIL) && password.equals(Constants.DUMMY_PASSWORD)){
//            UserProfile.setEmail(Constants.DUMMY_SIGN_UP_OBJECT.getEmail(), context);
//            UserProfile.setPassword(Constants.DUMMY_SIGN_UP_OBJECT.getPassword(),context);
//            UserProfile.setCategory(20,context);
//            UserProfile.setName(Constants.DUMMY_SIGN_UP_OBJECT.getName(),context);
//            UserProfile.setPhone(Constants.DUMMY_SIGN_UP_OBJECT.getPhone(),context);
//            UserProfile.setShopId(Constants.DUMMY_SIGN_UP_OBJECT.getShopId(),context);
//            UserProfile.setShopName(Constants.DUMMY_SIGN_UP_OBJECT.getShopName(),context);
//            UserProfile.setShopPhone(Constants.DUMMY_SIGN_UP_OBJECT.getShopTelephone(),context);
//            UserProfile.setAddress1(Constants.DUMMY_SIGN_UP_OBJECT.getShopAddress1(),context);
//            UserProfile.setAddress2(Constants.DUMMY_SIGN_UP_OBJECT.getShopAddress1(),context);
//            UserProfile.setLocation(Constants.DUMMY_SIGN_UP_OBJECT.getShopLocation(),context);
//            UserProfile.setAccess(true,context);
//            uiListener.onSuccess();
//            return;
//        }

        UserLoginTask userLoginTask = new UserLoginTask(context,email,password,uiListener);
        userLoginTask.execute();
    }

    public static class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject response;
        private Context context;
        private String mEmail;
        private String mPassword;
        private UIListener uiListener;

        public UserLoginTask(Context context, String email, String password, final UIListener uiListener) {
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

                    JSONObject transactionsList = response.getJSONObject("data").getJSONObject("Transactions");
                    Transactions transactions;

                    for (int i=0; i<response.getJSONObject("data").getInt("length"); i++){
                        transactions = new Transactions(transactionsList.getJSONObject(String.valueOf(i)));
                        transactions.saveTransaction(context);
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
                if (response.getJSONObject("data").getInt("result") == 1) {
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
            requestParams.add(new PostParam("returnpolicy", returnsAccepted+""));

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
                    UserProfile.setReferralCode(response.getJSONObject("data").getString("referralcode"),context);
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

    public static void callCategoryService(Context context, int categoryProduct, final UIListener uiListener) {

        CategoryTask categoryTask = new CategoryTask(context,categoryProduct,uiListener);
        categoryTask.execute();
    }



    public static class CategoryTask extends AsyncTask<String, Void, Void> {

        private JSONObject response;
        private Context context;
        private int categoryProduct;
        private final UIListener uiListener;

        public CategoryTask(Context context, int categoryProduct, final UIListener uiListener){

            this.context = context;
            this.categoryProduct = categoryProduct;
            this.uiListener = uiListener;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            ArrayList<PostParam> instiPostParams = new ArrayList<PostParam>();

            instiPostParams.add(new PostParam("token",UserProfile.getToken(context)));
            instiPostParams.add(new PostParam("email",UserProfile.getEmail(context)));
            instiPostParams.add(new PostParam("category",String.valueOf(categoryProduct)));


            response = PostRequest.execute(URLConstants.URLCategory, instiPostParams, null);
            Log.d("RESPONSE",response.toString());

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if(response.getJSONObject("data").getInt("result") == 1) {
                    super.onPostExecute(aVoid);
                    UserProfile.setCategory(categoryProduct,context);
                    uiListener.onSuccess();
                }else {
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

    public static void getProfile(Context context, final UIListener uiListener) {

        GetProfileTask getProfileTask = new GetProfileTask(context,uiListener);
        getProfileTask.execute();
    }

    public static class GetProfileTask extends AsyncTask<String, Void, Void> {

        private JSONObject response;
        private Context context;
        private final UIListener uiListener;

        public GetProfileTask(Context context,  final UIListener uiListener){

            this.context = context;
            this.uiListener = uiListener;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            ArrayList<PostParam> instiPostParams = new ArrayList<PostParam>();
            instiPostParams.add(new PostParam("token",UserProfile.getToken(context)));
            instiPostParams.add(new PostParam("email",UserProfile.getEmail(context)));
            response = PostRequest.execute(URLConstants.URLCategory, instiPostParams, null);
            Log.d("RESPONSE",response.toString());

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if(response.getJSONObject("data").getInt("result") == 1) {
                    super.onPostExecute(aVoid);
                    uiListener.onSuccess();
                }else {
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

    public static void callRewardsService(Context context, String referralCode, final UIListener uiListener) {

        RewardsTask rewardsTask = new RewardsTask(context,referralCode,uiListener);
        rewardsTask.execute();
    }

    public static class RewardsTask extends AsyncTask<Void, Void, Void> {

        private JSONObject response;
        private String referralCode;
        private Context context;
        private UIListener uiListener;

        public RewardsTask(Context context, String referralCode, final UIListener uiListener){
            this.context = context;
            this.uiListener = uiListener;
            this.referralCode = referralCode;
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
            requestParams.add(new PostParam("shopId", referralCode));

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
                    UserProfile.setReferralCode(response.getJSONObject("data").getString("referralcode"),context);
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

    public static void callUpdateAccountInfoService(Context context, String newPhoneNumber, final UIListener uiListener) {

        UpdateAccountInfoTask updateAccountInfoTask = new UpdateAccountInfoTask(context,newPhoneNumber,uiListener);
        updateAccountInfoTask.execute();
    }

    public static class UpdateAccountInfoTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject response;
        private Context context;
        private UIListener uiListener;
        private String newPhoneNumber;

        public UpdateAccountInfoTask(Context context, String newPhoneNumber, final UIListener uiListener){
            this.context = context;
            this.uiListener = uiListener;
            this.newPhoneNumber = newPhoneNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            ArrayList<PostParam> PostParams = new ArrayList<PostParam>();

            PostParams.add(new PostParam("email", UserProfile.getEmail(context)));
            PostParams.add(new PostParam("token",UserProfile.getToken(context)));
            PostParams.add(new PostParam("name",UserProfile.getName(context)));
            PostParams.add(new PostParam("mobile",newPhoneNumber));
            PostParams.add(new PostParam("pass",UserProfile.getPassword(context)));
            PostParams.add(new PostParam("shopName",UserProfile.getShopName(context)));
            PostParams.add(new PostParam("address1",UserProfile.getAddress1(context)));
            PostParams.add(new PostParam("address2",UserProfile.getAddress2(context)));
            PostParams.add(new PostParam("sid",UserProfile.getShopId(context)));
            PostParams.add(new PostParam("shopPhone",UserProfile.getShopPhone(context)));
            PostParams.add(new PostParam("sel_loc",UserProfile.getLocation(context)));

            response = PostRequest.execute(URLConstants.URLUpdate, PostParams, null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

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

    public static void callUpdateShopInfoService(Context context, ShopInfoObject shopInfoObject, final UIListener uiListener) {

        UpdateShopInfoTask updateShopInfoTask = new UpdateShopInfoTask(context,shopInfoObject,uiListener);
        updateShopInfoTask.execute();
    }

    public static class UpdateShopInfoTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject response;
        private Context context;
        private UIListener uiListener;
        private ShopInfoObject shopInfoObject;

        public UpdateShopInfoTask(Context context, ShopInfoObject shopInfoObject, final UIListener uiListener){
            this.context = context;
            this.uiListener = uiListener;
            this.shopInfoObject = shopInfoObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            ArrayList<PostParam> PostParams = new ArrayList<PostParam>();

            PostParams.add(new PostParam("email", UserProfile.getEmail(context)));
            PostParams.add(new PostParam("token",UserProfile.getToken(context)));
            PostParams.add(new PostParam("name",UserProfile.getName(context)));
            PostParams.add(new PostParam("mobile",UserProfile.getPhone(context)));
            PostParams.add(new PostParam("pass",UserProfile.getPassword(context)));
            PostParams.add(new PostParam("sel_loc",UserProfile.getLocation(context)));
            PostParams.add(new PostParam("shopName",shopInfoObject.getShopName()));
            PostParams.add(new PostParam("address1",shopInfoObject.getShopAddress1()));
            PostParams.add(new PostParam("address2",shopInfoObject.getShopAddress2()));
            PostParams.add(new PostParam("sid",shopInfoObject.getShopId()));
            PostParams.add(new PostParam("office",shopInfoObject.getShopTelephone()));

            response = PostRequest.execute(URLConstants.URLUpdate, PostParams, null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            try {
                if(response.getJSONObject("data").getInt("result") == 1){
                    uiListener.onSuccess();
                }else{
                    uiListener.onFailure();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onFailure();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            uiListener.onCancelled();
        }
    }

    public static void callItemDeclineService(Context context, String productId, final UIListener uiListener) {

        ItemDeclineTask itemDeclineTask = new ItemDeclineTask(context,productId,uiListener);
        itemDeclineTask.execute();
    }

    public static class ItemDeclineTask extends AsyncTask<Void, Void, Void> {

        private JSONObject response;
        private String id;
        private final UIListener uiListener;
        private Context context;

        public ItemDeclineTask(Context context, String productId, final UIListener uiListener) {
            this.context = context;
            this.id = productId;
            this.uiListener = uiListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<PostParam> iPostParams = new ArrayList<PostParam>();

            iPostParams.add(new PostParam("email", UserProfile.getEmail(context)));
            iPostParams.add(new PostParam("token",UserProfile.getToken(context)));
            iPostParams.add(new PostParam("decline_id",id));

            DatabaseHelper dh = new DatabaseHelper(context);
            dh.deleteTransaction(id);

            response = PostRequest.execute(URLConstants.URLDecline, iPostParams, null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //TODO: Service Response

        }
    }

    public static void callQuoteService(Context context, QuoteObject quoteObject, final UIListener uiListener) {

        QuoteTask quoteTask = new QuoteTask(context,quoteObject,uiListener);
        quoteTask.execute();
    }

    public static class QuoteTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject response;
        private QuoteObject quoteObject;
        private Context context;
        private UIListener uiListener;

        public QuoteTask(Context context, QuoteObject quoteObject, final UIListener uiListener){
            this.context = context;
            this.quoteObject = quoteObject;
            this.uiListener = uiListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            ArrayList<PostParam> instiPostParams = new ArrayList<PostParam>();

            instiPostParams.add(new PostParam("email", UserProfile.getEmail(context)));
            instiPostParams.add(new PostParam("token",UserProfile.getToken(context)));
            instiPostParams.add(new PostParam("type",String.valueOf(quoteObject.getProductType())));
            instiPostParams.add(new PostParam("deltype",String.valueOf(quoteObject.getDeliveryType())));
            instiPostParams.add(new PostParam("comment",quoteObject.getComments()));
            instiPostParams.add(new PostParam("qprice",quoteObject.getQuotedPrice()));
            instiPostParams.add(new PostParam("id",quoteObject.getProductId()));

            response = PostRequest.execute(URLConstants.URL_Quote, instiPostParams, null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            try {
                if(response.getJSONObject("data").getInt("result")==1){
                    uiListener.onSuccess();
                }else {
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

    public static void callItemAcceptService(Context context, String productId, final UIListener uiListener) {

        AcceptTask acceptTask = new AcceptTask(context,productId,uiListener);
        acceptTask.execute();
    }

    public static class AcceptTask extends AsyncTask<Void, Void, Void> {

        private JSONObject response;
        private Context context;
        private UIListener uiListener;
        private String productId;

        public AcceptTask(Context context, String productId, final UIListener uiListener) {
            this.context = context;
            this.productId = productId;
            this.uiListener = uiListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<PostParam> iPostParams = new ArrayList<PostParam>();

            iPostParams.add(new PostParam("email", UserProfile.getEmail(context)));
            iPostParams.add(new PostParam("token", UserProfile.getToken(context)));
            iPostParams.add(new PostParam("Cus_id", productId));

            response = PostRequest.execute(URLConstants.URLAccept, iPostParams, null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                if (response.getInt("status") == 200) {
                    uiListener.onSuccess();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onFailure();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            uiListener.onCancelled();
        }
    }

}
