package com.zzq.democollection.ipc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class BookDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "my_provider.db";
    public static final String BOOK_TABLE_NAME = "book";
    public static final String USER_TABLE_NAME = "user";
    private static final int DB_VERSION = 1;

    private static final String CREATOR_BOOK =
            "create table if not exists " + BOOK_TABLE_NAME
                    + "(id integer primary key autoincrement, "
                    + "author text, "
                    + "price real, "
                    + "pages integer, "
                    + "name text)";
    private static final String CREATOR_USER =
            "create table if not exists "+ USER_TABLE_NAME
                    + "(id integer primary key autoincrement, "
                    + "name text, "
                    + "age integer, "
                    + "isMale integer)";

    public BookDatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATOR_BOOK);
        db.execSQL(CREATOR_USER);
        Log.d("zzq","Create SQLiteDatabase success");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists book");
        db.execSQL("drop table if exists user");
        onCreate(db);
    }
}
