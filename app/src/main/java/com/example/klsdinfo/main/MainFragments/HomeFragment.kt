package com.example.klsdinfo.main.MainFragments

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.klsdinfo.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeFragment : Fragment(){



    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    lateinit var view2: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        print("onCreateView")
//        val view: View = inflater.inflate(R.layout.guide, container, false)
        val view : View = inflater.inflate(R.layout.main_home_layout, container, false)
        val mRoot: LinearLayout = view.findViewById(R.id.mainLayout)
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val welcome : TextView = view.findViewById(R.id.welcome_textView)
        val radioGroup : RadioGroup = view.findViewById(R.id.radioGroup)

        if(user!=null) welcome.text = ("Welcome, ${user.displayName}")



        radioGroup.check(R.id.radio0)


        radioGroup.setOnCheckedChangeListener { radioGroup, id ->
            val radio : RadioButton = view.findViewById(id)
            Snackbar.make(container as View,"Time Interval: last ${radio.text}",Snackbar.LENGTH_LONG).show()

        }





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


