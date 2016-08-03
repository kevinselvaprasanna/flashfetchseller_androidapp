package in.flashfetch.sellerapp.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import in.flashfetch.sellerapp.Helper.DatabaseHelper;

public class Transactions {

    public String productName, imageURL, productPrice, buyerName, productURL, bargainPrice, quotedPrice, productId, comment, buyerLocation;
    public long time, timeSent, bargainExpTime, buyerNumber;
    public boolean quoted=false, type, delivery, bargained=false, customerConfirmation, sellerConfirmation;
    public int deliveryType;

    public static String[] columns = {"id","buyer_name", "name", "url", "imgurl", "price","timesent", "time","loc","quoted","qprice","type","deltype","comment","bargained","bgprice","bgexptime","cuscon","selcon","del","buyno"};
    public static String TABLE_NAME = "Transactions";

    public Transactions(JSONObject transaction) {
        try {
            this.productPrice = transaction.getString("Ser_price");
            this.imageURL = transaction.getString("Ser_image");
            this.time = transaction.getLong("Cus_expiry");
            this.timeSent = transaction.getLong("time");
            this.productId = transaction.getString("Cus_id");
            this.buyerLocation = transaction.getString("cus_loc");
            this.buyerName = transaction.getString("Cus_name");
            this.productURL = transaction.getString("Cus_link");
            this.imageURL = transaction.getString("Ser_image");
            this.productName = transaction.getString("Ser_product");

            if(transaction.getInt("quoted") > 0 ){

                this.quoted = true;
                this.quotedPrice = transaction.getString("qprice");
                this.type = transaction.getInt("type") > 0;
                this.delivery = transaction.getInt("deltype") > 0;
                this.comment = transaction.getString("Sel_comments");
                this.customerConfirmation = transaction.getInt("cuscon") > 0;
                this.sellerConfirmation = transaction.getInt("selcon") > 0;

                if(transaction.getInt("bargained") > 0){
                    this.bargained = true;
                    this.bargainPrice = transaction.getString("bgprice");
                    this.bargainExpTime = transaction.getLong("bgexptime");
                }

                if(transaction.getInt("cuscon") > 0){
                    this.deliveryType = transaction.getInt("del");
                    this.buyerNumber = transaction.getInt("buyno");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Transactions(String id, String buyer_name, String name, String url, String imgurl, String price ,Long timesent,Long time,String loc, Boolean quoted, String qprice, Boolean type,Boolean deltype,String comment,Boolean bargained, String bgprice,long bgexptime, Boolean cuscon, Boolean selcon,int del, long buyno) {
        this.productId = id;
        this.buyerName = buyer_name;
        this.productName = name;
        this.productURL = url;
        this.imageURL = imgurl;
        this.productPrice = price;
        this.timeSent = timesent;
        this.time = time;
        this.buyerLocation = loc;
        this.quoted = quoted;
        this.quotedPrice = qprice;
        this.type = type;
        this.delivery = deltype;
        this.comment = comment;
        this.bargained = bargained;
        this.bargainPrice = bgprice;
        this.bargainExpTime = bgexptime;
        this.customerConfirmation = cuscon;
        this.sellerConfirmation = selcon;
        this.deliveryType = del;
        this.buyerNumber = buyno;
    }

    public void saveTransaction(Context mContext) {

        ContentValues cv = new ContentValues();

        cv.put("name", productName);
        cv.put("imgurl", imageURL);
        cv.put("price",productPrice);
        cv.put("loc",buyerLocation);
        cv.put("url",productURL);
        cv.put("buyer_name",buyerName);
        cv.put("timesent",timeSent);
        cv.put("time", time);
        cv.put("id", productId);

        if (quoted) {
            cv.put("quoted", 1);
            cv.put("qprice", quotedPrice);
            cv.put("type", type);
            cv.put("deltype", delivery);

            if(bargained){
                cv.put("bargained",bargained);
                cv.put("bgprice",bargainPrice);
                cv.put("bgexptime",bargainExpTime);
            }

            cv.put("comment", comment);
            cv.put("cuscon", customerConfirmation);
            cv.put("del",deliveryType);
        } else {
            cv.put("quoted", 0);
        }

        DatabaseHelper dh = new DatabaseHelper(mContext);
        dh.addTransaction(cv);
    }

    public static ArrayList<Transactions> getArrayList(Cursor c) {

        ArrayList<Transactions> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            arrayList.add(parseTransaction(c));
        }
        return arrayList;
    }

    public static Transactions parseTransaction(Cursor c) {
        Transactions transaction = new Transactions(c.getString(0),c.getString(1), c.getString(2),c.getString(3), c.getString(4), c.getString(5),c.getLong(6),c.getLong(7),c.getString(8),c.getInt(9)==1, c.getString(10),c.getInt(11)== 1, c.getInt(12)==1,c.getString(13),c.getInt(14)==1,c.getString(15),c.getLong(16), c.getInt(17)==1,c.getInt(18)==1,c.getInt(19),c.getLong(20));
        return transaction;
    }

    public static void updateTransaction(Context context, String id, ContentValues cv) {
        DatabaseHelper data = new DatabaseHelper(context);
        data.updateTransaction(id, cv);
    }

    public static ArrayList<Transactions> getTransaction(Context context, String id){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getTransaction(id);
    }

    public static ArrayList<Transactions> getQuotedTransactions(Context context){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getQuotedTransactions();
    }

    public static ArrayList<Transactions> getAcceptedTransactions(Context context){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getAcceptedTransactions();
    }

    public static ArrayList<Transactions> getRequestedTransactions(Context context){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getAllTransactions();
    }
}
