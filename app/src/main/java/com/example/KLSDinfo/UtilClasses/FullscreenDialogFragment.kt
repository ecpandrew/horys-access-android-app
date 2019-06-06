package com.example.KLSDinfo.UtilClasses

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.example.KLSDinfo.R




class FullscreenDialogFragment : DialogFragment() {

    val TAG: String = "FullScreenDialog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fullscreen_layout, container, false)
        val tool: Toolbar = view.findViewById(R.id.toolbar)


        tool.setNavigationIcon(R.drawable.ic_close_white_24dp)
        tool.setNavigationOnClickListener {
            cancelUpload()
        }

        val txt: TextView = view.findViewById(R.id.resource_TV)



        val lista: ArrayList<Parcelable>? = arguments?.getParcelableArrayList<Parcelable>("resources")

        Log.i("debug", "Recebido: $lista" )


        when{
            lista == null ->{
                txt.text = "No Results Came"
            }

            lista.isEmpty() -> {
                txt.text = "No Results Came"

            }
            else -> {
                txt.text = lista.toList().toString()

            }
        }





        return view
    }

    private fun cancelUpload() {
        dialog.dismiss()
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog

        if (dialog != null){
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)

        }
    }

}