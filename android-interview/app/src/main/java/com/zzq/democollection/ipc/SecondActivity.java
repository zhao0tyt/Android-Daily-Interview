package com.zzq.democollection.ipc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zzq.democollection.R;
import com.zzq.democollection.utils.Utils;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView textView =findViewById(R.id.tv_process);
        String process = Utils.getCurProcessName(this);
        textView.setText("当前进程为："+process);
        Bundle bundle = getIntent().getExtras();
        String ipcStyle = bundle.getString("extra");
        Toast.makeText(this, "接收到first activity传递过来的值："+ipcStyle,Toast.LENGTH_LONG).show();
        Button button2 = findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SecondActivity.this,ThirdActivity.class);
                startActivity(intent);
            }
        });

    }
}
