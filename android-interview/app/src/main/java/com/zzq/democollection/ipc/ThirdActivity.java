package com.zzq.democollection.ipc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        TextView textView =findViewById(R.id.tv_process);
        String process = Utils.getCurProcessName(this);
        textView.setText("当前进程为："+process);
        Button button3 = findViewById(R.id.button_3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivity.this, MyService.class);
                startService(intent);
            }
        });
    }
}
