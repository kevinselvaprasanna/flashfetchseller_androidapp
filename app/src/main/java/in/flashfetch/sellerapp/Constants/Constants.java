package in.flashfetch.sellerapp.Constants;

import android.text.SpannableString;

import in.flashfetch.sellerapp.Objects.SignUpObject;
import in.flashfetch.sellerapp.Objects.Transactions;

/**
 * Created by kranthikumar_b on 7/6/2016.
 */
public class Constants {

    public static final String PACKAGE_NAME = "in.flashfetch.sellerapp";

    //TODO: remove this
    public static final String DUMMY_EMAIL = "abc@gmail.com";

    public static final String DUMMY_PASSWORD = "123123123";

    public static final SignUpObject DUMMY_SIGN_UP_OBJECT = new SignUpObject("Kranthi","abc@gmail.com","123123123","1234567890","Bhagya Lakshmi Provisional Store","9884","32032032","Chennai",
            "TamilNadu","Ramapuram",true);

    public static final Transactions DUMMY_REQUESTED_TRANSACTION = new Transactions(
            "123",
            "FlashFetch",
            "Yonex Badminton Shuttles and Rackets",
            "http://www.yonex.com/products/badminton/shuttlecocks",
            "http://www.yonex.com/_assets/images/cache/products/categorythumbnail/M-600.jpg",
            "250",
            System.currentTimeMillis(),
            System.currentTimeMillis()+ 1200000,
            "Adyar",
            false,
            "",
            true,
            true,
            "Good Shuttle to Play Similar Available",
            false,
            "",
            0,
            false,
            false,
            3,
            1234567890);

    public static final Transactions DUMMY_PROVIDED_TRANSACTION = new Transactions("123",
            "Vamshi",
            "Yonex Badminton Shuttles and Rackets",
            "http://www.yonex.com/products/badminton/shuttlecocks",
            "http://www.yonex.com/_assets/images/cache/products/categorythumbnail/M-600.jpg",
            "250",
            System.currentTimeMillis(),
            System.currentTimeMillis()+ 120000,
            "Adyar",
            true,
            "230",
            true,
            true,
            "Good Shuttle to Play Similar Available",
            false,
            "",
            0,
            false,
            false,
            1,
            1234567890);

    public static final Transactions DUMMY_ACCEPTED_TRANSACTION = new Transactions("123",
            "Vamshi",
            "Yonex Badminton Shuttles and Rackets",
            "http://www.yonex.com/products/badminton/shuttlecocks",
            "http://www.yonex.com/_assets/images/cache/products/categorythumbnail/M-600.jpg",
            "250",
            System.currentTimeMillis(),
            System.currentTimeMillis()+ 120000,
            "Adyar",
            true,
            "230",
            true,
            true,
            "Good Shuttle to Play Similar Available",
            false,
            "",
            0,
            true,
            true,
            1,
            1234567890);

    public static final int REQUEST_READ_CONTACTS = 0;

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final int SIMILAR_PRODUCT = 100;

    public static final int SAME_PRODUCT = 101;

    public static final int HOME_DELIVERY = 102;

    public static final int SHOP_VISIT = 103;

    public static final boolean IS_FROM_FORGOT_PASSWORD = true;

    public static final boolean IS_FROM_PASSWORD_VERIFICATION = true;

    public static final boolean IS_FROM_REGISTRATION_FLOW = true;

    public static final boolean IS_FROM_LOGIN_FLOW = true;

    public static final boolean IS_FROM_QUOTE_FLOW = true;

    public static final String SELECT_START_DATE = "selectStartDate";

    public static final String IS_ACCESS_ALLOWED = "ACCESS_ALLOWED";

    public static final String SELECT_END_DATE = "selectEndDate";

    public static final String CONTACT_NUMBER = "+919940126973";

    public static final String APP_NAME = "FlashFetch";

    // app links

    public static final String GOOGLE_REFERRAL_URL = "http://goo.gl/xnwC9V";

    public static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=in.flashfetch.sellerapp&hl=en";

    public static final String TWITTER_URL = "https://twitter.com/flashfetch";

    public static final String FACEBOOK_URL = "https://www.facebook.com/Flashfetch-140606842997095/";
}
