package com.klip.android.broadcastbestpractice.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.klip.android.broadcastbestpractice.R;

/**
 * Created by park
 * on 2017/12/1.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION_1 = 1;
    public static final int DB_VERSION_2 = 2;
    public static final String DB_NAME = "MY_DB.db";
    public static final String TABLE_BOOKS = "books";
    private static final String TABLE_CATEGORIES = "categories";

    private Context mContext;

    public DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder(64);
        sql.append(" create table ");
        sql.append(" if not exists ");
        sql.append(TABLE_BOOKS);
        sql.append(" ( id integer primary key autoincrement, ");
        sql.append(" author text, ");
        sql.append(" price real, ");
        sql.append(" pages integer, ");
        sql.append(" name text ) ");

        StringBuilder sql2 = new StringBuilder(64);
        sql2.append(" create table ");
        sql2.append(" if not exists ");
        sql2.append(TABLE_CATEGORIES);
        sql2.append(" ( id integer primary key autoincrement, ");
        sql2.append(" category_name text, ");
        sql2.append(" category_code integer ) ");

        db.execSQL(sql.toString());
        db.execSQL(sql2.toString());

        Toast.makeText(mContext, mContext.getString(R.string.create_succeed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sql = new StringBuilder(64);
        sql.append(" drop table ");
        sql.append(" if exists ");
        sql.append(TABLE_BOOKS);

        StringBuilder sql2 = new StringBuilder(64);
        sql2.append(" drop table ");
        sql2.append(" if exists ");
        sql2.append(TABLE_CATEGORIES);

        db.execSQL(sql.toString());
        db.execSQL(sql2.toString());
        onCreate(db);
    }
}
