package com.zzq.democollection.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyAIDLService extends Service {

    private final String TAG = this.getClass().getSimpleName();
    List<Book> myBookList = null;

    private IBinder binder = new IMyAidlInterface.Stub() {
        @Override
        public void addBook(String name) throws RemoteException {
            myBookList.add(new Book(name));
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return myBookList;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        myBookList = new ArrayList<>();
        Log.d(TAG,"MyAIDLService onBind");
        return binder;
    }
}
