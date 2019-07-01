package com.example.klsdinfo.main.MainFragments

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klsdinfo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso

class HomeFragment : Fragment(){



    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

//    lateinit var img : ImageView
//    lateinit var nomeTv: TextView
//    lateinit var emailTv: TextView
//    lateinit var group: Group
//    lateinit var rv : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        print("onCreateView")
        val view: View = inflater.inflate(R.layout.guide, container, false)


//        val user = FirebaseAuth.getInstance().currentUser
//        rv = view.findViewById(R.id.rv)
//        emailTv = view.findViewById(R.id.emailTV)
//
//        val listOfDevices = mutableListOf(
//            Device("andre","andre@gmail.com"),
//            Device("alysson","alysson@gmail.com"),
//            Device("daniel","daniel@gmail.com")
//            )
//
//        val adapter = DeviceAdapter(context!!, listOfDevices)
//
//
//        val obj = object : DeviceAdapter.OnClickListener{
//            override fun onItemClick(view: View, obj: Device, pos: Int) {
//                emailTv.text = obj.email
//            }
//            override fun onItemLongClick(view: View, obj: Device, pos: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        }
//        rv.layoutManager = LinearLayoutManager(context)
//        rv.setHasFixedSize(true)
//        rv.adapter = adapter
//        adapter.setOnClickListener(obj)
//        adapter.notifyDataSetChanged()

        return view
    }



    override fun onAttach(context: Context) {
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


