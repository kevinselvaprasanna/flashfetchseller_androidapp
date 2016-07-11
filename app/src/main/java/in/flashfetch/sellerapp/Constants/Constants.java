package in.flashfetch.sellerapp.Constants;

import in.flashfetch.sellerapp.Objects.SignUpObject;

/**
 * Created by kranthikumar_b on 7/6/2016.
 */
public class Constants {

    public static final boolean IS_FROM_FORGOT_PASSWORD = true;

    public static final boolean IS_FROM_PASSWORD_VERIFICATION = true;

    public static final boolean IS_FROM_REGISTRATION_FLOW = true;

    public static final boolean IS_FROM_LOGIN_FLOW = true;

    public static final String SELECT_START_DATE = "selectStartDate";

    public static final String SELECT_END_DATE = "selectEndDate";

    public static final SignUpObject DUMMY_SIGN_UP_OBJECT = new SignUpObject("Kranthi","abc@gmail.com","123123","9885554006","Bhagya Lakshmi Provisional Store","9884","32032032","Chennai",
            "Tamil Nadu","Ramapuram",true);

    public static final String DUMMY_EMAIL = "abc@gmail.com";

    public static final String DUMMY_PASSWORD = "123123";
}
