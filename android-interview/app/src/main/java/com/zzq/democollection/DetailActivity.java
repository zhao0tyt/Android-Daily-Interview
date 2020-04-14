package com.zzq.democollection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.zzq.democollection.ipc.IpcFragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        if (intent != null) {
            String item = intent.getStringExtra(BaseAdapter.ITEM_KEY);
            if (item.equals(ItemData.IPC)) {
                addFragment(IpcFragment.newInstance());
            }

        }
    }

    public void addFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl,fragment)
                .commit();
    }
}
