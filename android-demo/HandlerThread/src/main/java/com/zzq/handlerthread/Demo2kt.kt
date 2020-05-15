package com.zzq.handlerthread

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class Demo2kt : AppCompatActivity() {

    private val url = arrayOf(
            "https://img-blog.csdn.net/20160903083245762",
            "https://img-blog.csdn.net/20160903083252184",
            "https://img-blog.csdn.net/20160903083257871",
            "https://img-blog.csdn.net/20160903083311972",
            "https://img-blog.csdn.net/20160903083319668",
            "https://img-blog.csdn.net/20160903083326871")
    private var imageView: ImageView? = null
    private var handlerThread: HandlerThread? = null
    private val mUiHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val bitmap = msg.obj as Bitmap
            imageView!!.setImageBitmap(bitmap)
            super.handleMessage(msg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo2)
        imageView = findViewById(R.id.image)

        //创建异步HandlerThread
        handlerThread = HandlerThread("downloadImage")
        handlerThread!!.start()

        val childHandler = Handler(handlerThread!!.looper, ChildCallback())

        for (i in url.indices) {
            childHandler.sendEmptyMessageDelayed(i, (i * 1000).toLong())
        }
    }

    internal inner class ChildCallback : Handler.Callback {

        override fun handleMessage(message: Message): Boolean {
            val bitmap = downloadUrlBitmap(url[message.what])
            val msg = Message()
            msg.what = message.what
            msg.obj = bitmap
            //通知主线程去更新UI
            mUiHandler.sendMessage(msg)
            return false
        }
    }

    private fun downloadUrlBitmap(urlString: String): Bitmap? {
        var urlConnection: HttpURLConnection? = null
        var `in`: BufferedInputStream? = null
        var bitmap: Bitmap? = null
        try {
            val url = URL(urlString)
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
}


