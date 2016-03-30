package in.flashfetch.sellerapp.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import in.flashfetch.sellerapp.Helper.DatabaseHelper;
import in.flashfetch.sellerapp.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kevinselvaprasanna on 3/1/16.
 */
public class Notification {
    public String name,imgurl,price,buyer_name,url;
    public long time,qprice;
    public String  id,comment,loc;
    Boolean quoted=false,type,deltype,cuscon,selcon;



   public Notification(JSONObject not) {
        try {
            //this.email = not.getString("email");
            this.price = not.getString("Ser_price");
            this.imgurl = not.getString("Ser_image");
            this.time = not.getLong("Cus_expiry");
            this.id = not.getString("Cus_id");
            this.loc = not.getString("cus_loc");
            this.buyer_name = not.getString("Cus_name");
            this.url = not.getString("Cus_link");
            this.imgurl = not.getString("Ser_image");
            this.name = not.getString("Ser_product");
            //this.email = not.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*public Notification(JSONObject not, Boolean qouted) {
        try {
            //this.email = not.getString("email");
            this.price = not.getString("price");
            this.imgurl = not.getString("imgurl");
            this.time = not.getLong("time");
            this.id = not.getString("id");
            this.qouted = qouted;
            this.qprice = not.getInt("qprice");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    public static String[] columns = {"id","buyer_name", "name", "imgurl", "price", "time","loc","quoted","qprice","type","deltype","comment","cuscon","selcon"};
    public static String TABLE_NAME = "Notifications";

    /*public Notification(String id, String name, String imgurl, String price ,Long time) {
        this.id = id;
        this.name = name;
        this.imgurl = imgurl;
        this.price = price;
        this.time = time;
        this.qouted =false;
    }*/
    public Notification(String id, String buyer_name, String name, String imgurl, String price ,Long time,String loc, Boolean quoted, Long qprice, Boolean type,Boolean deltype,String comment, Boolean cuscon, Boolean selcon) {
        this.id = id;
        this.buyer_name = buyer_name;
        this.name = name;
        this.imgurl = imgurl;
        this.price = price;
        this.time = time;
        this.loc = loc;
        this.quoted = quoted;
        this.qprice = qprice;
        this.type = type;
        this.deltype = deltype;
        this.comment = comment;
        this.cuscon = cuscon;
        this.selcon = selcon;

    }

    public static ArrayList<Notification> getArrayList(Cursor c) {
        ArrayList<Notification> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            arrayList.add(parseNot(c));
        }
        return arrayList;
    }

    public static Notification parseNot(Cursor c) {
        Notification not = new Notification(c.getString(0),c.getString(1), c.getString(2), c.getString(3), c.getString(4),c.getLong(5),c.getString(6),c.getInt(7)==1, c.getLong(8),c.getInt(9)== 1, c.getInt(10)==1,c.getString(11),c.getInt(12)==1, c.getInt(13)==1);
        return not;
    }

    public static void updateNot(Context context, String id, ContentValues cv) {
        DatabaseHelper data = new DatabaseHelper(context);
        data.updateNot(id, cv);
    }

    public static ArrayList<Notification> getAllNotifications(Context context){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getAllNotifications();
    }
    public static ArrayList<Notification> getQNotifications(Context context){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getQNotifications();
    }
    public static ArrayList<Notification> getANotifications(Context context){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getANotifications();
    }
    public static ArrayList<Notification> getNotification(Context context, String id){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getNotification(id);
    }

    public void saveNot(Context mContext) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("imgurl", imgurl);
        cv.put("price",price);
        cv.put("loc",loc);
        cv.put("buyer_name",buyer_name);
        //cv.put("price", Integer.parseInt(data.getString("price")));
        cv.put("time", time);
        cv.put("id", id);
        if (quoted.equals("1")) {
            cv.put("quoted", 1);
            cv.put("qprice", qprice);
            cv.put("type", type);
            cv.put("deltype", deltype);
            cv.put("comment", comment);
            cv.put("cuscon", cuscon);
        } else {
            cv.put("quoted", 0);
        }
        DatabaseHelper dh = new DatabaseHelper(mContext);
        dh.addNot(cv);
    }
}
