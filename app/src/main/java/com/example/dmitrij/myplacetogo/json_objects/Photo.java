package com.example.dmitrij.myplacetogo.json_objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Dmitrij on 19.07.2015.
 */
public class Photo implements Parcelable {

    private final String tableName="photos";
    public String url;
    private String name;
    private byte[]body;
    private final String thisCode = "UTF-8";
    public Photo(){}

    public Photo(String url){
        this.url=url;
        HttpURLConnection connection = null;
        try {
            connection=(HttpURLConnection)new URL("\"127.0.0.1:8080\\MyPlaceToGo\\:android\\getPhoto\\").openConnection();
            String requestParams ="placeId"+ URLEncoder.encode(url, thisCode);
            if (connection != null) {
                OutputStream out = connection.getOutputStream();
                out.write(requestParams.getBytes());
                InputStream in = connection.getInputStream();
                BufferedInputStream bin = new BufferedInputStream(in);
                byte[]body = new byte[1024];
                int val;
                int offcet=0;
                while((val=bin.read(body))!=-1) {
                    bin.read(body,offcet,val);
                    offcet+=val;
                }
                this.body=body;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void savePhoto(SQLiteDatabase db ){

        ContentValues cv = new ContentValues();
        cv.put("url",url);
        cv.put("body",body);
        db.insert(tableName, null, cv);
    }

    public Photo[] getPhotos(SQLiteDatabase db ) {
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        int columnIndexURL = c.getColumnIndex("url");
        int columnIndexBody = c.getColumnIndex("body");
        Photo[] photos = new Photo[c.getCount()];
        for (int i = 0; c.moveToNext(); i++){
            Photo photo = new Photo().setUrl(c.getString(columnIndexURL)).setBody(c.getBlob(columnIndexBody));
            photos[i]=photo;
        }
    return photos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myPlaceToGoDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("db", "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table "+tableName+"("
                    + "id integer primary key autoincrement,"
                    + "url text,"
                    + "body blob" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
}

    public Photo setUrl(String url) {
        this.url = url;
        return this;
    }

    public Photo setBody(byte[] body) {
        this.body = body;
        return this;
    }

}
