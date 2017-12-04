package com.klip.android.broadcastbestpractice;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.klip.android.broadcastbestpractice.db.DBHelper;

/**
 * Created by park
 * on 2017/12/2.
 */

public class DatabaseProvider extends ContentProvider {
    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORIES_DIR = 2;
    public static final int CATEGORIES_ITEM = 3;
    public static final String AUTHORITY = "com.klip.android.broadcastbestpractice.provider";
    private static UriMatcher uriMatcher;

    private DBHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "books", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "books/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "categories", CATEGORIES_DIR);
        uriMatcher.addURI(AUTHORITY, "categories/#", CATEGORIES_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext(), DBHelper.DB_NAME, DBHelper.DB_VERSION_3);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                cursor = db.query("books", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                String book_id = uri.getPathSegments().get(1);
                cursor = db.query("books", projection, "id = ?", new String[]{book_id}, null, null, sortOrder);
                break;
            case CATEGORIES_DIR:
                cursor = db.query("categories", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORIES_ITEM:
                String category_id = uri.getPathSegments().get(1);
                cursor = db.query("categories", projection, "id = ?", new String[]{category_id}, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.klip.android.broadcastbestpractice.provider.books";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.klip.android.broadcastbestpractice.provider.books";
            case CATEGORIES_DIR:
                return "vnd.android.cursor.dir/vnd.com.klip.android.broadcastbestpractice.provider.categories";
            case CATEGORIES_ITEM:
                return "vnd.android.cursor.item/vnd.com.klip.android.broadcastbestpractice.provider.categories";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert("books", null, values);
                returnUri = Uri.parse("content://" + AUTHORITY + "/books/" + newBookId);
                break;
            case CATEGORIES_DIR:
            case CATEGORIES_ITEM:
                long newCategoryId = db.insert("books", null, values);
                returnUri = Uri.parse("content://" + AUTHORITY + "/categories/" + newCategoryId);
                break;
            default:
                break;
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteRow = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                deleteRow = db.delete("books", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String book_id = uri.getPathSegments().get(1);
                deleteRow = db.delete("books", "id = ?", new String[]{book_id});
                break;
            case CATEGORIES_DIR:
                deleteRow = db.delete("categories", selection, selectionArgs);
                break;
            case CATEGORIES_ITEM:
                String category_id = uri.getPathSegments().get(1);
                deleteRow = db.delete("categories", "id = ?", new String[]{category_id});
                break;
            default:
                break;
        }
        return deleteRow;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateRow = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                updateRow = db.update("books", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String book_id = uri.getPathSegments().get(1);
                updateRow = db.update("books", values, "id = ?", new String[]{book_id});
                break;
            case CATEGORIES_DIR:
                updateRow = db.update("categories", values, selection, selectionArgs);
                break;
            case CATEGORIES_ITEM:
                String category_id = uri.getPathSegments().get(1);
                updateRow = db.update("categories", values, "id = ?", new String[]{category_id});
                break;
            default:
                break;
        }
        return updateRow;
    }
}
