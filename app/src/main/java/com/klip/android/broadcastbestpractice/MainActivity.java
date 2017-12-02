package com.klip.android.broadcastbestpractice;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.klip.android.broadcastbestpractice.db.DBHelper;

import org.w3c.dom.Text;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;

    private Button button;
    private Button create_database;
    private Button add_data;
    private Button update_data;
    private Button delete_data;
    private Button query_data;
    private Button make_call;
    private TextView query_result;

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化的时候就会创建表
        dbHelper = new DBHelper(this, DBHelper.DB_NAME, DBHelper.DB_VERSION_3);
        db = dbHelper.getWritableDatabase();
        query_result = (TextView) findViewById(R.id.query_result);
        make_call = (Button) findViewById(R.id.make_call);
        make_call.setOnClickListener(makeCallListener);
        button = (Button) findViewById(R.id.forced_offline);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("ab");
                sendBroadcast(intent);
            }
        });

        create_database = (Button) findViewById(R.id.create_database);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        add_data = (Button) findViewById(R.id.add_data);
        add_data.setOnClickListener(addDataListener);

        update_data = (Button) findViewById(R.id.update_data);
        update_data.setOnClickListener(updateDataListener);

        delete_data = (Button) findViewById(R.id.delete_data);
        delete_data.setOnClickListener(deleteDataListener);

        query_data = (Button) findViewById(R.id.query_data);
        query_data.setOnClickListener(queryDataListener);

    }

    private View.OnClickListener addDataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContentValues values = new ContentValues();
            values.put("name", "The Da Vinci Code");
            values.put("author", "Dan Brown");
            values.put("pages", "454");
            values.put("price", "16.96");
            db.insert(DBHelper.TABLE_BOOKS, null, values);

            values.clear();
            values.put("name", "The Lost Symbol");
            values.put("author", "Dan Brown");
            values.put("pages", "510");
            values.put("price", "19.95");
            db.insert(DBHelper.TABLE_BOOKS, null, values);
        }
    };
    private View.OnClickListener updateDataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            db.execSQL("update books set price = ? where name = ?", new String[]{"20.01", "The Da Vinci Code"});
        }
    };
    private View.OnClickListener deleteDataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            db.execSQL("delete from books");
        }
    };
    private View.OnClickListener queryDataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            query_result.setText("");
            Cursor cursor = db.rawQuery("select * from books", null);
            if (cursor.moveToFirst()) {
                StringBuilder content = new StringBuilder();
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String author = cursor.getString(cursor.getColumnIndex("author"));
                    String price = cursor.getString(cursor.getColumnIndex("price"));
                    String pages = cursor.getString(cursor.getColumnIndex("pages"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    content.append(id);
                    content.append(author);
                    content.append(price);
                    content.append(pages);
                    content.append(name);
                    content.append("\n");
                } while (cursor.moveToNext());
                query_result.setText(content.toString());
            } else {
                query_result.setText(getString(R.string.has_no_data));
            }

            cursor.close();
        }
    };
    private View.OnClickListener makeCallListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
            } else {
                call();
            }
        }
    };


    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CALL_PHONE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.denied_permission), Toast.LENGTH_LONG).show();
                }
        }
    }
}
