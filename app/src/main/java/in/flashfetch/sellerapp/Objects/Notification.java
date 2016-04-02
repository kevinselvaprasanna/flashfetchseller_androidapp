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
    public String name,imgurl,price,buyer_name,url,bgprice,qprice;
    public long time,timesent,bgexptime;
    public String  id,comment,loc;
    public Boolean quoted=false,type,deltype,bargained=false,cuscon,selcon;



   public Notification(JSONObject not) {
        try {
            //this.email = not.getString("email");
            this.price = not.getString("Ser_price");
            this.imgurl = not.getString("Ser_image");
            this.time = not.getLong("Cus_expiry");
            this.timesent = not.getLong("time");
            this.id = not.getString("Cus_id");
            this.loc = not.getString("cus_loc");
            this.buyer_name = not.getString("Cus_name");
            this.url = not.getString("Cus_link");
            this.imgurl = not.getString("Ser_image");
            this.name = not.getString("Ser_product");

            if(not.getInt("qouted") > 0 ){
                this.quoted = true;
                this.qprice = not.getString("qprice");
                this.type = not.getInt("type") > 0;
                this.deltype = not.getInt("deltype") > 0;
                this.comment = not.getString("Sel_comments");
                if(not.getInt("bargained") >0 ){
                    this.bargained = true;
                    this.bgprice = not.getString("bgprice");
                    this.bgexptime = not.getLong("bgexptime");
                }
                this.cuscon = not.getInt("cuscon")>0;
                this.selcon = not.getInt("selcon")>0;

            }
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

    public static String[] columns = {"id","buyer_name", "name", "url", "imgurl", "price","timesent", "time","loc","quoted","qprice","type","deltype","comment","bargained","bgprice","bgexptime","cuscon","selcon"};
    public static String TABLE_NAME = "Notifications";

    /*public Notification(String id, String name, String imgurl, String price ,Long time) {
        this.id = id;
        this.name = name;
        this.imgurl = imgurl;
        this.price = price;
        this.time = time;
        this.qouted =false;
    }*/
    public Notification(String id, String buyer_name, String name, String url, String imgurl, String price ,Long timesent,Long time,String loc, Boolean quoted, String qprice, Boolean type,Boolean deltype,String comment,Boolean bargained, String bgprice,long bgexptime, Boolean cuscon, Boolean selcon) {
        this.id = id;
        this.buyer_name = buyer_name;
        this.name = name;
        this.imgurl = imgurl;
        this.price = price;
        this.time = time;
        this.timesent = timesent;
        this.loc = loc;
        this.url = url;
        this.quoted = quoted;
        this.qprice = qprice;
        this.type = type;
        this.deltype = deltype;
        this.comment = comment;
        this.bargained = bargained;
        this.bgprice = bgprice;
        this.bgexptime = bgexptime;
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
        Notification not = new Notification(c.getString(0),c.getString(1), c.getString(2),c.getString(3), c.getString(4), c.getString(5),c.getLong(6),c.getLong(7),c.getString(8),c.getInt(9)==1, c.getString(10),c.getInt(11)== 1, c.getInt(12)==1,c.getString(13),c.getInt(14)==1,c.getString(15),c.getLong(16), c.getInt(17)==1,c.getInt(18)==1);
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
        cv.put("url",url);
        cv.put("buyer_name",buyer_name);
        cv.put("timesent",timesent);
        //cv.put("price", Integer.parseInt(data.getString("price")));
        cv.put("time", time);
        cv.put("id", id);
        if (quoted.equals("1")) {
            cv.put("quoted", 1);
            cv.put("qprice", qprice);
            cv.put("type", type);
            cv.put("deltype", deltype);
            if(bargained){
                cv.put("bargained",bargained);
                cv.put("bgprice",bgprice);
                cv.put("bgexptime",bgexptime);
            }
            cv.put("comment", comment);
            cv.put("cuscon", cuscon);
        } else {
            cv.put("quoted", 0);
        }
        DatabaseHelper dh = new DatabaseHelper(mContext);
        dh.addNot(cv);
    }
}
