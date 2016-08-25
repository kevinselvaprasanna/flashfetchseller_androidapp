package in.flashfetch.sellerapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import in.flashfetch.sellerapp.Objects.Notification;
import in.flashfetch.sellerapp.Objects.Transactions;

public class DatabaseHelper {

    private static String LOG_TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "FlashFetch";
    private static final int DATABASE_VERSION = 3;

    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub _id INTEGER PRIMARY KEY
           /* db.execSQL(" DROP TABLE IF EXISTS " + Notification.TABLE_NAME);*/
            db.execSQL("CREATE TABLE " + Transactions.TABLE_NAME + "(id VARCHAR PRIMARY KEY,buyer_name VARCHAR, name VARCHAR,url VARCHAR DEFAULT 'http://www.jfgbuofbgabwiewak.com/', imgurl VARCHAR, price VARCHAR, timesent BIGINT, time BIGINT,loc VARCHAR, quoted INT DEFAULT 0, qprice VARCHAR DEFAULT '0', type INT DEFAULT 0, deltype INT DEFAULT 0, comment VARCHAR DEFAULT ' ',bargained INT DEFAULT 0,bgprice VARCHAR DEFAULT ' ',bgexptime BIGINT DEFAULT 1459424183578,cuscon INT DEFAULT 0,selcon INT DEFAULT 0,del INT DEFAULT 0,buyno BIGINT DEFAULT 0)");
            Log.d("dmydb", "DATABSE CREATED");
            db.execSQL("CREATE TABLE " + Notification.TABLE_NAME + "(id VARCHAR PRIMARY KEY,text VARCHAR,time BIGINT)");
            Log.d("dmydb", "DATABSE CREATED");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL(" DROP TABLE IF EXISTS " + Transactions.TABLE_NAME);
            Log.d("dmydb", "DATABSE dropped");
        }
    }

    public DatabaseHelper(Context c) {
        ourContext = c;
    }

    public DatabaseHelper open() {
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public long addNotification(ContentValues cv) {
        open();
        long id = ourDatabase.insert(Notification.TABLE_NAME, null, cv);
        Log.d("dmydb","NOT ADDED");
        close();
        return id;
    }

    public ArrayList<Notification> getAllNotifications() {
        open();
        String[] columns = Notification.columns;
        Cursor c = ourDatabase.query(Notification.TABLE_NAME, columns, null, null, null, null, "time DESC");
        ArrayList<Notification> arrayList = Notification.getArrayList(c);
        close();
        return arrayList;
    }

    public long addTransaction(ContentValues cv) {

        if (getTransaction(cv.getAsString("id")).size() > 0) {
            updateTransaction(cv.getAsString("id"), cv);
            close();
            return 1;
        } else {
            open();
            long id = ourDatabase.insert(Transactions.TABLE_NAME, null, cv);
            Log.d("dmydb", "NOTIFICATION ADDED");
            close();
            return id;
        }
    }

    public boolean deleteTransaction(String id) {
        open();
        Boolean d = ourDatabase.delete(Transactions.TABLE_NAME, "id = ?", new String[]{id}) > 0;
        close();
        return d;
    }

    public void updateTransaction(String id, ContentValues cv) {
        open();
        ourDatabase.update(Transactions.TABLE_NAME, cv, "id" + " = ?", new String[]{id});
        close();
    }

    public ArrayList<Transactions> getAllTransactions() {
        open();
        String[] columns = Transactions.columns;
        Cursor c = ourDatabase.query(Transactions.TABLE_NAME, columns, "quoted = 0", null, null, null, "timesent DESC");
        ArrayList<Transactions> arrayList = Transactions.getArrayList(c);
        close();
        return arrayList;
    }

    public ArrayList<Transactions> getQuotedTransactions() {
        open();
        String[] columns = Transactions.columns;
        Cursor c = ourDatabase.query(Transactions.TABLE_NAME, columns, "quoted = 1", null, null, null, "timesent DESC");
        ArrayList<Transactions> arrayList = Transactions.getArrayList(c);
        close();
        return arrayList;
    }

    public ArrayList<Transactions> getAcceptedTransactions() {
        open();
        String[] columns = Transactions.columns;
        Cursor c = ourDatabase.query(Transactions.TABLE_NAME, columns, "cuscon = 1", null, null, null, "timesent DESC");
        ArrayList<Transactions> arrayList = Transactions.getArrayList(c);
        close();
        return arrayList;
    }

    public ArrayList<Transactions> getTransaction(String id) {
        open();
        String[] columns = Transactions.columns;
        Cursor c = ourDatabase.query(Transactions.TABLE_NAME, columns, "id = ?", new String[]{id}, null, null, null);
        ArrayList<Transactions> arrayList = Transactions.getArrayList(c);
        close();
        return arrayList;
    }
}