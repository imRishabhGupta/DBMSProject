package com.project.dbms.user.dbmsproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 24-04-2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "login_manager";

    //Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_DATA="data";

    //Table Columns names
    private static final String DATA="data";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String LOCK="lock";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + EMAIL + " TEXT PRIMARY KEY," + USERNAME + " TEXT,"
                + PASSWORD + " TEXT" + ")";

        //1-unlock
        //2-read lock
        //3-write lock

        String CREATE_DATA_TABLE="CREATE TABLE "+TABLE_DATA+"("+DATA+" INTEGER NOT NULL, "+LOCK+" INTEGER NOT NULL, CHECK ("+LOCK+" > 0 AND lock < 4))";
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_DATA_TABLE);
        ContentValues values= new ContentValues();
        values.put(DATA,100000);
        values.put(LOCK,1);
        db.insert(TABLE_DATA,null,values);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    void addUser(User user){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(USERNAME,user.getUsername());
        values.put(PASSWORD,user.getPassword());
        values.put(EMAIL,user.getEmail());
        db.insert(TABLE_USERS, null, values);
        System.out.println("added");
        db.close();
    }

    // Getting single contact
    User getUser(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] {EMAIL,
                        USERNAME, PASSWORD}, EMAIL + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if(cursor.getCount()==0)
            return  null;
        User user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2));

        // return user
        return user;
    }

    // Getting All Contacts
    public void getAllUsers() {
        List<User> users = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User(cursor.getString(0),cursor.getString(1),cursor.getString(2));
                System.out.println(user.getEmail()+" "+user.getUsername()+" "+user.getPassword());
                users.add(user);
            } while (cursor.moveToNext());
        }
    }

    public void changeLock(int lock){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOCK,lock);
        db.update(TABLE_DATA,values,null,null);
    }

    public void changeValue(int value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int i=Integer.parseInt(getValue())-value;
        values.put(DATA,i);
        db.update(TABLE_DATA,values,null,null);
    }

    public int getLock(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_DATA,new String[]{LOCK},null,null,null,null,null);
        if (cursor != null)
            cursor.moveToFirst();
        return Integer.parseInt(cursor.getString(0));
    }

    public String getValue(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_DATA,new String[]{DATA},null,null,null,null,null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor.getString(0);
    }
}
