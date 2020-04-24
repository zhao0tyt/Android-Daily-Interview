package com.zzq.democollection.ipc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zzq.democollection.R;

import java.util.List;
import java.util.Random;

public class BookActivity extends AppCompatActivity {

    private IMyAidlInterface iMyAidlInterface;
    private TextView mTextView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        mTextView = findViewById(R.id.tv_book);
        mButton = findViewById(R.id.bt_book);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                String name = "编程"+random.nextInt(10);
                try {
                    iMyAidlInterface.addBook(name);
                    List<Book> lastBookList = iMyAidlInterface.getBookList();
                    mTextView.setText(lastBookList.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Intent intent = new Intent(getApplicationContext(), MyAIDLService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            iMyAidlInterface = null;
        }
    };
}
