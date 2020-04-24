package com.zzq.democollection.ipc;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

//@RuntimePermissions
public class SecondActivity extends AppCompatActivity {
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        context = this;
        TextView textView =findViewById(R.id.tv_process);
        String process = Utils.getCurProcessName(this);
        textView.setText("当前进程为："+process);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String flag = bundle.getString("flag");
            if (flag.equals(IpcFragment.BUNDLE)) {
                String ipcStyle = bundle.getString("extra");
                Toast.makeText(this, "接收到first activity传递过来的值："
                        + ipcStyle, Toast.LENGTH_LONG).show();
            } else if (flag.equals(IpcFragment.SHARE_FILE)) {
                final String filePath = bundle.getString("file_path");
                final String fileName = bundle.getString("file_name");
                readFromFile(filePath, fileName);
            }
        }
        Button button2 = findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SecondActivity.this,ThirdActivity.class);
                startActivity(intent);
            }
        });
    }

    // 单个权限
    // @NeedsPermission(Manifest.permission.CAMERA)
    // 多个权限
//    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE})
     void readFromFile(final String filePath, final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File cacheFile = new File(filePath, fileName);
                if(!cacheFile.exists()) {
                    throw new RuntimeException("cacheFile not exists");
                }
                ObjectInputStream objectInputStream = null;
                try {
                    objectInputStream = new ObjectInputStream(new FileInputStream(cacheFile));
                    final String text = (String) objectInputStream.readObject();
                    if (text != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "接收到first activity传递过来的值："
                                        + text, Toast.LENGTH_LONG).show();
                            }
                        });
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
