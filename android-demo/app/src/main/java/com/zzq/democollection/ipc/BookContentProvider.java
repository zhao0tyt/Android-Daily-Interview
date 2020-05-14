package com.zzq.democollection.ipc;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BookContentProvider extends ContentProvider {
    private Context mContext;
    private SQLiteDatabase mDb;

    public static final String AUTHORITY = "com.zzq.democollection.ipc.bookcontentprovider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BookDatabaseHelper.BOOK_TABLE_NAME);
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BookDatabaseHelper.USER_TABLE_NAME);

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, BookDatabaseHelper.BOOK_TABLE_NAME, BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, BookDatabaseHelper.USER_TABLE_NAME, USER_URI_CODE);
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        initProviderData(mContext);
        Log.d("zzq", "onCreate, current Thread:" + Thread.currentThread().getName());
        return false;
    }

    private void initProviderData(Context context) {
        mDb = new BookDatabaseHelper(context).getWritableDatabase();
        mDb.execSQL("delete from " + BookDatabaseHelper.BOOK_TABLE_NAME);
        mDb.execSQL("delete from " + BookDatabaseHelper.USER_TABLE_NAME);
        mDb.execSQL("insert into book values(3,'Android', 105.39,666, 'Android开发');");//初始化的时候自增的数据需要赋值
        mDb.execSQL("insert into book values(1,'Ios', 50.46, 333, 'Ios开发');");
        mDb.execSQL("insert into user values(1,'Claire', 25, 0);");
        mDb.execSQL("insert into user values(4,'Tom', 23, 1);");
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = BookDatabaseHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = BookDatabaseHelper.USER_TABLE_NAME;
                break;
        }
        return tableName;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        String table = getTableName(uri);
        if (table == null) {
            try {
                throw new IllegalAccessException("unSupport uri = " + uri);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return mDb.query(table, strings, s, strings1, null, s1, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d("zzq", "getType, current Thread:" + Thread.currentThread().getName());
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.d("zzq", "insert, current Thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (null == table) {
            try {
                throw new IllegalAccessException("unSupport uri = " + uri);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        long row = mDb.insert(table, null, contentValues);

        if (row > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, row);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Log.d("zzq", "delete, current Thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (null == table) {
            try {
                throw new IllegalAccessException("unSupport uri = " + uri);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        int count = mDb.delete(table, s, strings);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        Log.d("zzq", "update, current Thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (null == table) {
            try {
                throw new IllegalAccessException("unSupport uri = " + uri);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        int row = mDb.update(table, contentValues, s, strings);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }
}
