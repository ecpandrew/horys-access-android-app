package com.example.klsdinfo.main.MainFragments

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.klsdinfo.R
import com.example.klsdinfo.R.color.*
import com.example.klsdinfo.data.*
import com.example.klsdinfo.data.database.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.squareup.picasso.Picasso

class HomeFragment : Fragment(){



    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    lateinit var viewModel: HomeViewModel
    lateinit var  welcome : TextView
    lateinit var email: TextView
    lateinit var duration: TextView
    lateinit var beacons: TextView
    lateinit var errorTv : TextView
    lateinit var refresh : Button
    private var unixTime: Long? = null
    private var unixTimePast: Long? = null
    lateinit var radioGroup: RadioGroup
    lateinit var mView: View
    lateinit var chart : HorizontalBarChart
    lateinit var listView : ListView
    lateinit var adapter : ArrayAdapter<String>



    override fun onStart() {
        super.onStart()
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if(user!= null){
            // Todo()

        }else{
//            viewModel.fetchUserForChart()
            viewModel.fetchUserForCurrentPosition()
        }

        print("onStart")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        print("onCreateView")

        mView = inflater.inflate(R.layout.main_home_layout, container, false)
        welcome = mView.findViewById(R.id.welcome_textView)
        email = mView.findViewById(R.id.email_textView)
//        errorTv = mView.findViewById(R.id.error)
        duration = mView.findViewById(R.id.duration_textView)
        beacons = mView.findViewById(R.id.beacon_textView)
//        radioGroup= mView.findViewById(R.id.radioGroup)
        refresh = mView.findViewById(R.id.btn_refresh_position)

        listView = mView.findViewById(R.id.list_view)


        setupUser()

        setupViewModel()

//        setupRadioCheckChangeListener()

        refresh.setOnClickListener {
            viewModel.fetchUserForCurrentPosition()
        }








        return mView
    }

    private fun setupUser() {
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        welcome.text = user?.displayName ?: "No user found"
        email.text = user?.email ?: "No user found"

        if(user?.photoUrl.toString().isNullOrBlank()){
//            (mView.findViewById(R.id.profile_image) as ImageView).setImageDrawable(null)
        }else{
            Picasso.get().load(user?.photoUrl.toString()).into((mView.findViewById(R.id.profile_image) as ImageView))
        }


    }






    private fun setupViewModel() {

        val repo = SemanticRepository.getInstance(SemanticApiService.create(), AppDatabase.getInstance(context!!)!!)
        val repo2 = DanielServiceRepository.getInstance(DanielApiService.create(), AppDatabase.getInstance(context!!)!!)

        val factory = ViewModelFactory(repo,repo2, activity?.application!!)

        viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)






//        viewModel.listData.observe(viewLifecycleOwner, Observer {
//            if(!it.isNullOrEmpty()){
//                adapter = ArrayAdapter(context!!, R.layout.list_item_name, it)
//                listView.adapter = adapter
//                adapter.notifyDataSetChanged()
//            }else{
//            }
//        })


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
//        val calendar2 = Calendar.getInstance()
//
//        val timeZone: TimeZone = calendar2!!.timeZone
//
//        //val cals: Date = Calendar.getInstance().time//TimeZone.getDefault()).time
//        val cals: Date = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time
//
//        var milis: Long = cals.time
//
//        milis += timeZone.getOffset(milis)

        unixTime = System.currentTimeMillis()/1000
            //milis/1000
        unixTimePast = unixTime!! - delta

    }


}


