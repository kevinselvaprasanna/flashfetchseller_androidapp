package in.flashfetch.sellerapp.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import in.flashfetch.sellerapp.Helper.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kevinselvaprasanna on 3/1/16.
 */
public class Notification {
    public String name,imgurl,price;
    public long time,qprice;
    public String  id,comment;
    Boolean qouted=false,type,deltype,cuscon,selcon;



    public Notification(JSONObject not) {
        try {
            //this.email = not.getString("email");
            this.price = not.getString("price");
            this.imgurl = not.getString("imgurl");
            this.time = not.getLong("time");
            this.id = not.getString("id");
            //this.email = not.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Notification(JSONObject not, Boolean qouted) {
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
    }

    public static String[] columns = {"id", "name", "imgurl", "price", "time","qouted","qprice","type","deltype","comment","cuscon","selcon"};
    public static String TABLE_NAME = "Notifications";

    /*public Notification(String id, String name, String imgurl, String price ,Long time) {
        this.id = id;
        this.name = name;
        this.imgurl = imgurl;
        this.price = price;
        this.time = time;
        this.qouted =false;
    }*/
    public Notification(String id, String name, String imgurl, String price ,Long time, Boolean quoted, Long qprice, Boolean type,Boolean deltype,String comment, Boolean cuscon, Boolean selcon) {
        this.id = id;
        this.name = name;
        this.imgurl = imgurl;
        this.price = price;
        this.time = time;
        this.qouted = quoted;
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
        Notification not = new Notification(c.getString(0), c.getString(1), c.getString(2), c.getString(3),c.getLong(4),c.getInt(5) >0, c.getLong(6),c.getInt(7) > 0, c.getInt(8)>0,c.getString(9),c.getInt(10) >0, c.getInt(11)>0);
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
}
