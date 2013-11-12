package com.util;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASENAME = "result.db"; //数据库名称
    private static final int DATABASEVERSION = 1;//数据库版本

    public DBOpenHelper(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
        		"create table if not exists result(" +
        		"id integer primary key AUTOINCREMENT," + 
        		"statuses_id varchar(255)," +		
        		"image_urls varchar(255)," + 
        		"screen_name varchar(255)," + 
        		"statuses_text varchar(255)," + 
        	    "retweeted_statuses varchar(255)," + 
        		"pic_url1 varchar(255)," + 
        		"pic_url2 varchar(255)," + 
        		"reposts_comments varchar(255)" +
        		")"
        );//执行有更改的sql语句
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS person");
        onCreate(db);
    }

}

