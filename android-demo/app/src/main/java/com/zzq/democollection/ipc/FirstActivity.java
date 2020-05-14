package com.zzq.democollection.ipc;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

//@RuntimePermissions
public class FirstActivity extends AppCompatActivity {
    public static  String FILE_PATH;
    public static final String FILE_NAME = "file.txt";
    private Context context;
    String ipcStyle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        context = this;
        FILE_PATH = context.getExternalFilesDir(null).getAbsolutePath();
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
                        bundle.putString("flag", IpcFragment.BUNDLE);
                        bundle.putString("extra", "ipc bundle");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (ipcStyle.equals(IpcFragment.SHARE_FILE)) {
                        Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("flag", IpcFragment.SHARE_FILE);
                        bundle.putString("file_path", FILE_PATH);
                        bundle.putString("file_name", FILE_NAME);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }

            }
        });
        WriteToFile();
//        FirstActivityPermissionsDispatcher.WriteToFileWithPermissionCheck(this);
    }

    // 单个权限
    // @NeedsPermission(Manifest.permission.CAMERA)
    // 多个权限
//    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE})
     void WriteToFile() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("zzq",FILE_PATH);
                File dir = new File(FILE_PATH);
                if (!dir.exists()) {
                  boolean success = dir.mkdir();
                    Log.d("zzq", "create dir "+success);
                }

                File cacheFile = new File(FILE_PATH, FILE_NAME);
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
                    objectOutputStream.writeObject("share file");

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
