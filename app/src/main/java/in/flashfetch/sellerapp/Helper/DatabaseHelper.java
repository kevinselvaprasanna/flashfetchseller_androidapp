package in.flashfetch.sellerapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import in.flashfetch.sellerapp.Objects.Notification;

import java.util.ArrayList;


public class DatabaseHelper {

    private static String LOG_TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "FlashFetch";
    private static final int DATABASE_VERSION = 2;

    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;


    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME,null,DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub _id INTEGER PRIMARY KEY
            db.execSQL(" DROP TABLE IF EXISTS " + Notification.TABLE_NAME);
            db.execSQL("CREATE TABLE " + Notification.TABLE_NAME + "(id VARCHAR PRIMARY KEY, name VARCHAR, imgurl VARCHAR, price VARCHAR, time BIGINT, qouted INT DEFAULT 0, qprice INT DEFAULT 0, type INT DEFAULT 0, deltype INT DEFAULT 0, comment VARCHAR DEFAULT ' ',cuscon INT DEFAULT 0,selcon INT DEFAULT 0)");
                    Log.d("dmydb", "DATABSE CREATED");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL(" DROP TABLE IF EXISTS " + Notification.TABLE_NAME  );
            Log.d("dmydb", "DATABSE dropped");
        }

    }

    public DatabaseHelper(Context c){
        ourContext = c;
    }

    public DatabaseHelper open(){
        ourHelper = new DbHelper (ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        ourHelper.close();
    }

    public long addNot(ContentValues cv) {
        open();
        long id = ourDatabase.insert(Notification.TABLE_NAME, null, cv);
        Log.d("dmydb","NOTIFICATION ADDED");
        close();
        return id;
    }

    public void updateNot(String id,ContentValues cv){
        open();
        ourDatabase.update(Notification.TABLE_NAME, cv, "id" + " = ?", new String[]{id});
        close();

    }
    public ArrayList<Notification> getAllNotifications () {
        open();
        String[] columns = Notification.columns;
        Cursor c = ourDatabase.query(Notification.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<Notification> arrayList = Notification.getArrayList(c);
        close();
        return arrayList;
        /*Notification not1 = new Notification("1232","dell laptop","",(long)1234,(long)32472367);
        Notification not2 = new Notification("1232","dell laptop","",(long)1234,(long)32472367,true,(long)234,true,true,"sgsg",false,false);
        ArrayList<Notification> arrayList = {not1,not2}*/
    }

    public ArrayList<Notification> getQNotifications () {
        open();
        String[] columns = Notification.columns;
        Cursor c = ourDatabase.query(Notification.TABLE_NAME, columns, "qouted = 1" , null, null, null, null);
        ArrayList<Notification> arrayList = Notification.getArrayList(c);
        close();
        return arrayList;
    }








}