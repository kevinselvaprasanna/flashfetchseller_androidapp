package in.flashfetch.sellerapp.Constants;

import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.Objects.SignUpObject;

/**
 * Created by kranthikumar_b on 7/6/2016.
 */
public class Constants {

    public static final SignUpObject DUMMY_SIGN_UP_OBJECT = new SignUpObject("Dummy","abc@gmail.com","123123","4567891230","Bhagya Lakshmi Provisional Store","9884","32032032","Chennai",
            "Tamil Nadu","Ramapuram",true);

    public static final Notification DUMMY_TRANSACTION = new Notification("123",
            "Vamshi",
            "Yonex Badminton Shuttles and Rackets",
            "http://www.yonex.com/products/badminton/shuttlecocks",
            "http://www.yonex.com/_assets/images/cache/products/categorythumbnail/M-600.jpg",
            "250",
            System.currentTimeMillis(),
            System.currentTimeMillis()+ 120000,
            "Adyar",
            false,
            "",
            false,
            true,
            "Good Shuttle to Play Similar Available",
            false,
            "",
            0,
            false,
            false,
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

    public static final String SELECT_END_DATE = "selectEndDate";

    public static final String DUMMY_EMAIL = "abc@gmail.com";

    public static final String DUMMY_PASSWORD = "123123";

    public static final String CONTACT_NUMBER = "+919940126973";

    public static final String APP_NAME = "FlashFetch";

    public static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=in.flashfetch.sellerapp&hl=en";

    public static final String TWITTER_URL = "https://twitter.com/flashfetch";

    public static final String FACEBOOK_URL = "https://www.facebook.com/Flashfetch-140606842997095/";
}
