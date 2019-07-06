package com.example.klsdinfo.main.MainFragments

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.klsdinfo.R
import com.example.klsdinfo.data.*
import com.example.klsdinfo.data.database.AppDatabase
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

class HomeFragment : Fragment(){



    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    lateinit var viewModel: HomeViewModel
    lateinit var  welcome : TextView
    private var unixTime: Long? = null
    private var unixTimePast: Long? = null

    var delta: Long = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        print("onCreateView")
//        val view: View = inflater.inflate(R.layout.guide, container, false)
        val view : View = inflater.inflate(R.layout.main_home_layout, container, false)
        val mRoot: LinearLayout = view.findViewById(R.id.mainLayout)
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        welcome = view.findViewById(R.id.welcome_textView)
        val radioGroup : RadioGroup = view.findViewById(R.id.radioGroup)

        setupViewModel()





        radioGroup.setOnCheckedChangeListener { radioGroup, id ->
            val radio : RadioButton = view.findViewById(id)
            Snackbar.make(container as View,"Time Interval: last ${radio.text}",Snackbar.LENGTH_LONG).show()

            when(id){
                R.id.radio0 -> {
                    setTimeInterval(86400)
                    viewModel.setDates(unixTime.toString(),unixTimePast.toString())
                    Log.e("timestamp", unixTime.toString())
                    Log.e("timestamp", unixTimePast.toString())
                    viewModel.setDates(unixTime.toString(),unixTimePast.toString())
                    viewModel.fetchUser()

                }
                R.id.radio1 -> {
                    setTimeInterval(259200)
                    Log.e("timestamp", unixTime.toString())
                    Log.e("timestamp", unixTimePast.toString())
                    viewModel.setDates(unixTime.toString(),unixTimePast.toString())
                    viewModel.fetchUser()

                }
                R.id.radio2 -> {
                    setTimeInterval(604800)
                    Log.e("timestamp", unixTime.toString())
                    Log.e("timestamp", unixTimePast.toString())
                    viewModel.setDates(unixTime.toString(),unixTimePast.toString())
                    viewModel.fetchUser()


                }
                R.id.radio3 -> {
                    setTimeInterval(2592000)
                    Log.e("timestamp", unixTime.toString())
                    Log.e("timestamp", unixTimePast.toString())
                    viewModel.setDates(unixTime.toString(),unixTimePast.toString())
                    viewModel.fetchUser()

                }
            }

        }

        radioGroup.check(R.id.radio0)
        setTimeInterval(86400)





        return view
    }


    private fun setupViewModel() {
        val repo = SemanticRepository.getInstance(SemanticApiService.create(), AppDatabase.getInstance(context!!)!!)
        val repo2 = DanielServiceRepository.getInstance(DanielApiService.create(), AppDatabase.getInstance(context!!)!!)

        val factory = ViewModelFactory(repo,repo2, activity?.application!!)

        viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)





        viewModel.user.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                welcome.text = (it.shortName)
            }else{
                welcome.text = "No user Found, please relog!"
            }
        })


        viewModel.listResource.observe(viewLifecycleOwner, Observer {
          Log.i("data", it.toString())
        })
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
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if(user==null){

        }else{
            viewModel.fetchUser()
        }

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


    fun setTimeInterval(delta: Long){
        val calendar2 = Calendar.getInstance()

        val timeZone: TimeZone = calendar2!!.timeZone

        val cals: Date = Calendar.getInstance().time//TimeZone.getDefault()).time

        var milis: Long = cals.time

        milis += timeZone.getOffset(milis)

        unixTime = milis/1000
        unixTimePast = unixTime!! - delta

    }


}


