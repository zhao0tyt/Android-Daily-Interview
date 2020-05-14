package com.zzq.democollection.ipc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zzq.democollection.R;
import com.zzq.democollection.utils.Utils;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Uri bookUri = BookContentProvider.BOOK_CONTENT_URI;
        ContentValues bookValues = new ContentValues();
        bookValues.put("author", "java开发");//插入数据时，自增的数据不需要赋值
        bookValues.put("price", 25.67);
        bookValues.put("pages", 145);
        bookValues.put("name", "java");
        getContentResolver().insert(bookUri, bookValues);
        getContentResolver().delete(bookUri,"price > ?", new String[]{"100"});
        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"id","author","price","pages","name"},null,null,null);
        while(bookCursor.moveToNext()){
            Books book = new Books();
            book.id = bookCursor.getInt(bookCursor.getColumnIndex("id"));
            book.author = bookCursor.getString(bookCursor.getColumnIndex("author"));
            book.price = bookCursor.getFloat(bookCursor.getColumnIndex("price"));
            book.pages = bookCursor.getInt(bookCursor.getColumnIndex("pages"));
            book.name = bookCursor.getString(bookCursor.getColumnIndex("name"));
            Log.d("zzq",book.toString());
        }
        bookCursor.close();

        Uri userUri = BookContentProvider.USER_CONTENT_URI;
        ContentValues userValues = new ContentValues();
        userValues.put("name", "Lily");
        getContentResolver().update(userUri,userValues,"age = ?",new String[]{"25"});//age为25的地方将name改为Lily
        Cursor userCursor = getContentResolver().query(userUri, new String[]{"id","name","age","isMale"},null,null,null);
        while(userCursor.moveToNext()){
            User user = new User();
            user.id = userCursor.getInt(userCursor.getColumnIndex("id"));
            user.name = userCursor.getString(userCursor.getColumnIndex("name"));
            user.age = userCursor.getInt(userCursor.getColumnIndex("age"));
            user.isMale = userCursor.getInt(userCursor.getColumnIndex("isMale"));
            Log.d("zzq",user.toString());
        }
        userCursor.close();

    }
}
