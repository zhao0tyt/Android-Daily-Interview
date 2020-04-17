package com.zzq.democollection.ipc;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zzq.democollection.R;
import com.zzq.democollection.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView textView =findViewById(R.id.tv_process);
        String process = Utils.getCurProcessName(this);
        textView.setText("当前进程为："+process);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String ipcStyle = bundle.getString("extra");
            Toast.makeText(this, "接收到first activity传递过来的值：" + ipcStyle, Toast.LENGTH_LONG).show();
        }
        Button button2 = findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SecondActivity.this,ThirdActivity.class);
                startActivity(intent);
            }
        });
        readFromFile();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
     void readFromFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File cacheFile = new File(FirstActivity.CACHE_PATH, FirstActivity.FILENAME);
                if(!cacheFile.exists()) {
                    Log.d("zzq","cacheFile not exists");
                    return;
                }
                ObjectInputStream objectInputStream = null;
                try {
                    Log.d("zzq","try try try");
                    objectInputStream = new ObjectInputStream(new FileInputStream(cacheFile));
                    String text = (String) objectInputStream.readObject();
                    if (text != null) {
                        Log.d("zzq", "text ="+text);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
