package com.example.klsdinfo.main.MainFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
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

    lateinit var img : ImageView
    lateinit var nomeTv: TextView
    lateinit var emailTv: TextView
    lateinit var group: Group

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        print("onCreateView")
        val view: View = inflater.inflate(R.layout.main_home_layout, container, false)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            setupProfile(user, view)
        } else {
            group = view.findViewById(R.id.group)
            group.visibility = View.GONE
        }

        return view
    }

    private fun setupProfile(user: FirebaseUser, view: View) {
        (view.findViewById(R.id.username) as TextView).text = user.displayName
        (view.findViewById(R.id.useremail) as TextView).text = user.email
        Picasso.get().load(user.photoUrl.toString()).into((view.findViewById(R.id.userimg) as ImageView))
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

