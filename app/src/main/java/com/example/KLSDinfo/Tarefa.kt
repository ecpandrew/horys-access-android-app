package com.example.KLSDinfo

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class Tarefa(private val context: Context, ti: TarefaInterface) : AsyncTask<String, String, Bitmap>() {


    private val ti: TarefaInterface = ti
    lateinit var progress: AlertDialog.Builder
    lateinit var dialog: AlertDialog
    var img: Bitmap? = null


    override fun onPreExecute() {
        progress = AlertDialog.Builder(context)
        progress.setCancelable(false)
        progress.setView(R.layout.loading_dialog_layout)
        dialog = progress.create()
        dialog.show()

    }

    override fun doInBackground(vararg params: String?): Bitmap? {

        try {
            Log.i("request", "1 -> ${params[0]}")
            val ulr = URL(params[0])
            val connection: HttpURLConnection = ulr.openConnection() as HttpURLConnection
            val input: InputStream = connection.inputStream
            Thread.sleep(1000)


            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = false
            options.inSampleSize = 2


            img = BitmapFactory.decodeStream(input, null, options)


        }catch (e: IOException){}
        Log.i("request", "1 -> ${img==null}")

        return img
    }



    override fun onProgressUpdate(vararg values: String?) {
        // TODO
    }

    override fun onPostExecute(result: Bitmap?) {
        ti.depoisDownload(result)
        dialog.dismiss()
    }
}