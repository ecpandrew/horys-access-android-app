package com.example.KLSDinfo.Home

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.KLSDinfo.R
import com.example.KLSDinfo.Tarefa
import com.example.KLSDinfo.TarefaInterface

class HomeFragment : Fragment(), TarefaInterface{

    override fun depoisDownload(bitmap: Bitmap?) {
        img.setImageBitmap(bitmap)
    }


    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    lateinit var img : ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        print("onCreateView")
        val view: View = inflater.inflate(R.layout.main_home_layout, container, false)
        img = view.findViewById(R.id.imagemTeste)
        val btn: Button = view.findViewById(R.id.buttonRequest)

        btn.setOnClickListener {
            baixarImagem()
        }

        return view
    }


    fun baixarImagem(){
        val tarefa = Tarefa(context!!, this)
        tarefa.execute("http://www.thiengo.com.br/img/system/logo/thiengo-80-80.png")
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        print("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        print("onCreate")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        print("onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        print("onStart")

    }

    override fun onResume() {
        super.onResume()
        print("onResume")
    }

    override fun onPause() {
        super.onPause()
        print("onPause")
    }

    override fun onStop() {
        super.onStop()
        print("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        print("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        print("onDestroy")
    }

    fun print(msg: String){
        Log.d("Lifecycle", "HomeFragment: $msg")
    }



}


