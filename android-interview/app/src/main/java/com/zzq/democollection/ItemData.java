package com.zzq.democollection;

import java.util.ArrayList;
import java.util.List;

public class ItemData {
    public static final String IPC = "Android实现IPC进程间通信";
    private List<String> data = new ArrayList<>();

    public ItemData(){
        addData();
    }


    public void addData(){
        data.add(IPC);
    }

    public List<String> getData(){
        return data;
    }
}
