package in.flashfetch.sellerapp.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Helper.DatabaseHelper;

/**
 * Created by kevinselvaprasanna on 3/31/16.
 */
public class Notifications {

    public String text,id;
    public long time;
    public static String[] columns = {"id","text","time"};
    public static String TABLE_NAME = "Notifications";

    public Notifications(String id, String text,long time){
        this.text = text;
        this.id = id;
        this.time = time;
    }

    public static ArrayList<Notifications> getArrayList(Cursor c) {
        ArrayList<Notifications> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            arrayList.add(parseNot(c));
        }
        return arrayList;
    }

    public static Notifications parseNot(Cursor c) {
        Notifications not = new Notifications(c.getString(0),c.getString(1),c.getLong(2));
        return not;
    }
    public static ArrayList<Notifications> getAllNots(Context context){

        DatabaseHelper data = new DatabaseHelper(context);
        return data.getAllNotifications();
    }


}
