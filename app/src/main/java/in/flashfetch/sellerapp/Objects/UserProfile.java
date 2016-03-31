package in.flashfetch.sellerapp.Objects;

import android.content.Context;
import android.content.SharedPreferences;

import in.flashfetch.sellerapp.SignUpActivity;


public class UserProfile {

    public static String name,email,token,text;
    public static int category;

    public static void setName(String name, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.commit();
    }

    public static void setEmail(String email,Context context){
        SharedPreferences preferences = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email",email);
        editor.commit();
    }
    public static void setToken(String token, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public static void setShopName(String shopName, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("shopname", shopName);
        editor.commit();
    }

    public static void setCategory(int category, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("category", category);
        editor.commit();
    }

   /* public static void setText(String text, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("text", text);
        editor.commit();
    }

    public static void setCounter(int counter, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("counter", counter);
        editor.commit();
    }

    public static void setUpdate(int update, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("update", update);
        editor.commit();
    }*/


    public static String getName(Context context) {
        SharedPreferences pref = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        return pref.getString("name", "");
    }

    public static String getEmail(Context context) {
        SharedPreferences pref = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        return pref.getString("email", "");
    }

    public static String getToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        return pref.getString("token", "");
    }

    public static String getShopName(Context context) {
        SharedPreferences pref = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        return pref.getString("shopname", "");
    }

    public static int getCategory(Context context) {
        SharedPreferences pref = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        return pref.getInt("category", 1);
    }

  /*  public static String getText(Context context) {
        SharedPreferences pref = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        return pref.getString("text", "Thank you for registering with us. Buyers coming soon");
    }

    public static int getCounter(Context context) {
        SharedPreferences pref = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        return pref.getInt("counter", 0);
    }

    public static int getUpdate(Context context) {
        SharedPreferences pref = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        return pref.getInt("update", 0);
    }*/
    public static int clear(Context context){
        setName("", context);
        setEmail("", context);
        setToken("", context);
        setCategory(1,context);
        return 1;
    }


}


