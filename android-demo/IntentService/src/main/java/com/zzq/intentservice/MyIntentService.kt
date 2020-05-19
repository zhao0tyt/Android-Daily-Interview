package com.zzq.intentservice

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import android.os.Message
import android.util.Log
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class MyIntentService : IntentService("MyIntentService"){
    
    companion object{
        private var updateUI : UpdateUI? = null
        val DOWNLOAD_URL : String = "download_url"
        val INDEX_FLAG : String = "index_flag"
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d("zhaozhiqiang", "onHandleIntent")
        //在子线程中进行网络请求
        val bitmap = downloadUrlBitmap(intent!!.getStringExtra(DOWNLOAD_URL))
        val msg1 = Message()
        msg1.what = intent!!.getIntExtra(INDEX_FLAG, 0)
        msg1.obj = bitmap
        //通知主线程去更新UI
        Log.d("zhaozhiqiang", "updateUI="+updateUI)
        if (updateUI != null) {
            Log.d("zhaozhiqiang", "updateUI != null")
            updateUI!!.update(msg1)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("zhaozhiqiang", "onCreate")
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        Log.d("zhaozhiqiang", "onStart")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    fun setUpdateUI(updateUIInterface : UpdateUI){
        Log.d("zhaozhiqiang", "setUpdateUI")
        updateUI = updateUIInterface
        Log.d("zhaozhiqiang", "updateUI="+updateUI)
    }

    private fun downloadUrlBitmap(urlString: String?): Bitmap? {
        var urlConnection: HttpURLConnection? = null
        var `in`: BufferedInputStream? = null
        var bitmap: Bitmap? = null
        try {
            val url = URL(urlString!!)
            urlConnection = url.openConnection() as HttpURLConnection
            `in` = BufferedInputStream(urlConnection.inputStream, 8 * 1024)
            bitmap = BitmapFactory.decodeStream(`in`)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            urlConnection?.disconnect()
            try {
                `in`?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return bitmap
    }

    open interface UpdateUI {
        fun update(message: Message)
    }
}