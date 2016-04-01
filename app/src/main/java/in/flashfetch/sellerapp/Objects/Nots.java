package in.flashfetch.sellerapp.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import in.flashfetch.sellerapp.Helper.DatabaseHelper;

/**
 * Created by kevinselvaprasanna on 3/31/16.
 */
public class Nots {
    public String text,id;
    public long time;
    public static String[] columns = {"id","text","time"};
    public static String TABLE_NAME = "Nots";
    public Nots(String id, String text,long time){
        this.text = text;
        this.id = id;
        this.time = time;
    }

    public static ArrayList<Nots> getArrayList(Cursor c) {
        ArrayList<Nots> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            arrayList.add(parseNot(c));
        }
        return arrayList;
    }

    public static Nots parseNot(Cursor c) {
        Nots not = new Nots(c.getString(0),c.getString(1),c.getLong(2));
        return not;
    }
    public static ArrayList<Nots> getAllNots(Context context){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getAllNots();
    }


}
