package com.zzq.democollection.ipc;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zzq.democollection.R;
import com.zzq.democollection.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class FirstActivity extends AppCompatActivity {
//    public static final String CACHE_PATH = Environment.getExternalStorageDirectory() + "/IPCCache";
    public static final String CACHE_PATH = "data/data/com.zzq.democollection/cache";
    public static final String FILENAME = "file.txt";
    String ipcStyle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Intent intent = getIntent();
        if (intent != null) {
            ipcStyle = intent.getStringExtra("ipc");
        }

        Button button1 = findViewById(R.id.button_1);
        TextView textView = findViewById(R.id.tv_process);
        String process = Utils.getCurProcessName(this);
        textView.setText("当前进程为：" + process);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ipcStyle != null) {
                    if (ipcStyle.equals(IpcFragment.BUNDLE)) {
                        Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("extra", "ipc bundle");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (ipcStyle.equals(IpcFragment.SHARE_FILE)) {
                        Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                        startActivity(intent);
                    }
                }

            }
        });
//        String fileName = "file.txt";
//        File dir = new File(CACHE_PATH);
//        if (!dir.exists()) {
//            Log.d("zzq", "not exist");
//            Log.d("zzq", "CACHE_PATH" + CACHE_PATH);
//            dir.mkdirs();
//        }
//        File cacheFile = new File(CACHE_PATH, fileName);
//        if (!cacheFile.exists()) {
//            try {
//                cacheFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        FirstActivityPermissionsDispatcher.WriteToFileWithPermissionCheck(this);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
     void WriteToFile() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("zzq","WriteToFile run ");
                File dir = new File(CACHE_PATH);
                if (!dir.exists()) {
                    dir.mkdir();
                }

                File cacheFile = new File(CACHE_PATH,FILENAME);
                Log.d("zzq","filename = "+cacheFile.getName());
                ObjectOutputStream objectOutputStream = null;
                try {
                    boolean isExists = cacheFile.exists();
                    Log.d("zzq","cacheFile exists = "+isExists);
                    if (!cacheFile.exists()) {
                        boolean success = cacheFile.createNewFile();
                        Log.d("zzq","createNewFile = "+success);
                    }
                    objectOutputStream = new ObjectOutputStream(new FileOutputStream(cacheFile));
                    objectOutputStream.writeObject("aaaaaaaa");

                } catch (IOException e) {
                    Log.d("zzq","IOException ");
                    e.printStackTrace();
                } finally {
                    try {
                        objectOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
