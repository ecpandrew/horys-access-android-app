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




    override fun onStart() {
        super.onStart()
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if(user!= null){
            // Todo()

        }else{
            viewModel.fetchUserForChart()
            viewModel.fetchUserForCurrentPosition()
        }

        print("onStart")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        print("onCreateView")

        mView = inflater.inflate(R.layout.main_home_layout, container, false)
        welcome = mView.findViewById(R.id.welcome_textView)
        email = mView.findViewById(R.id.email_textView)
        errorTv = mView.findViewById(R.id.error)
        duration = mView.findViewById(R.id.duration_textView)
        beacons = mView.findViewById(R.id.beacon_textView)
        radioGroup= mView.findViewById(R.id.radioGroup)
        refresh = mView.findViewById(R.id.btn_refresh_position)


        setupUser()

        setupViewModel()

        setupRadioCheckChangeListener()

        refresh.setOnClickListener {
            viewModel.fetchUserForCurrentPosition()
        }








        return mView
    }

    private fun setupUser() {
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        welcome.text = user?.displayName
        email.text = user?.email

        if(user?.photoUrl.toString().isNullOrBlank()){
//            (mView.findViewById(R.id.profile_image) as ImageView).setImageDrawable(null)
        }else{
            Picasso.get().load(user?.photoUrl.toString()).into((mView.findViewById(R.id.profile_image) as ImageView))
        }


    }

    private fun setupChart(map: Map<String,Long>) {

        chart = mView.findViewById(R.id.chart1)
        chart.setDrawBarShadow(false)

        chart.setDrawValueAboveBar(true)

        chart.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)

        // draw shadows for each bar that show the maximum value
        // chart.setDrawBarShadow(true);

        chart.setDrawGridBackground(false)



        val xl = chart.xAxis



        val array = arrayListOf<String>()



        for(i in map.keys){ array.add(i)}

        Log.i("labels", array.toString())

        val obj = object:  IndexAxisValueFormatter() {

            override fun getFormattedValue(value: Float): String {
                val index = Math.round(value)

                if(index < 0 || index > map.size || index != value.toInt()){
                    return ""
                }

                return array[index]

            }

        }

        chart.xAxis.valueFormatter = IndexAxisValueFormatter(array)






        xl.position = XAxis.XAxisPosition.BOTH_SIDED // caso fique estranho mudar para .BOTTOM

        xl.typeface = Typeface.SERIF
        xl.setDrawAxisLine(true)
        xl.setDrawGridLines(true)
        xl.granularity = 1f

        val yl = chart.axisLeft
        yl.typeface = Typeface.SERIF
        yl.setDrawAxisLine(true)
        yl.setDrawGridLines(true)
        yl.axisMinimum = 0f // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        val yr = chart.axisRight
        yr.typeface = Typeface.SERIF
        yr.setDrawAxisLine(true)
        yr.setDrawGridLines(true)
        yr.axisMinimum = 0f // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        chart.setFitBars(true)
        chart.animateY(1000)

        // setting data
//        seekBarY.setProgress(50)
//        seekBarX.setProgress(12)

        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(true)
        l.formSize = 8f
        l.xEntrySpace = 4f


        chart.setDrawValueAboveBar(false)
        chart.legend.isEnabled = false

        setData(map)
//        setData(5,15.toFloat())
    }


    private fun setData(map: Map<String,Long>) {

        val barWidth = .9f
        val spaceForBar = 1f
        val values = arrayListOf<BarEntry>()



        for (i in 0 until map.toList().size){

            values.add(
                BarEntry(
                    i*spaceForBar , map.toList()[i].second.toFloat())

            )

        }

        val set1: BarDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {

            set1 = BarDataSet(values, "")

            set1.setDrawIcons(false)

            val dataSets = arrayListOf<IBarDataSet>()
            dataSets.add(set1)

            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.setValueTypeface(Typeface.SERIF)
            data.barWidth = .8f
//            chart.xAxis.valueFormatter = LabelValueFormatter(data)//IndexAxisValueFormatter(array)//obj//


            chart.data = data

        }

    }

    private fun setupRadioCheckChangeListener() {
        radioGroup.setOnCheckedChangeListener { radioGroup, id ->

            viewModel.error.postValue(Pair(100,"Fetching: user"))
            when(id){
                R.id.radio0 -> {
                    setTimeInterval(86400)
                    viewModel.setDates(unixTime.toString(),unixTimePast.toString())
                    Log.e("debugtime", unixTime.toString())
                    Log.e("debugtime", unixTimePast.toString())
                    viewModel.fetchUserForChart()
                }
                R.id.radio1 -> {
                    setTimeInterval(259200)
                    Log.e("debugtime", unixTime.toString())
                    Log.e("debugtime", unixTimePast.toString())
                    viewModel.setDates(unixTime.toString(),unixTimePast.toString())
                    viewModel.fetchUserForChart()

                }
                R.id.radio2 -> {
                    setTimeInterval(604800)
                    Log.e("debugtime", unixTime.toString())
                    Log.e("debugtime", unixTimePast.toString())
                    viewModel.setDates(unixTime.toString(),unixTimePast.toString())
                    viewModel.fetchUserForChart()


                }
                R.id.radio3 -> {
                    setTimeInterval(2592000)
                    Log.e("debugtime", unixTime.toString())
                    Log.e("debugtime", unixTimePast.toString())
                    viewModel.setDates(unixTime.toString(),unixTimePast.toString())
                    viewModel.fetchUserForChart()

                }
            }

        }
        radioGroup.check(R.id.radio0)

//        setTimeInterval(86400)
    }


    private fun setupViewModel() {
        val repo = SemanticRepository.getInstance(SemanticApiService.create(), AppDatabase.getInstance(context!!)!!)
        val repo2 = DanielServiceRepository.getInstance(DanielApiService.create(), AppDatabase.getInstance(context!!)!!)

        val factory = ViewModelFactory(repo,repo2, activity?.application!!)

        viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)



        viewModel.error.observe(viewLifecycleOwner, Observer {
            val code = it.first

            when(code){


                100 -> {
                    errorTv.text = it.second
                    errorTv.setTextColor(resources.getColor(colorPrimary))

                }

                101 -> {
                    errorTv.text = it.second
                    errorTv.setTextColor(resources.getColor(colorPrimary))

                }


                210 -> {
                    errorTv.text = it.second
                    errorTv.setTextColor(resources.getColor(green))

                }


                200 ->{
                    errorTv.text = it.second
                    errorTv.setTextColor(resources.getColor(green))
                }





                504 ->{
                    errorTv.text = it.second
                    errorTv.setTextColor(resources.getColor(red_500))
                }


                503 ->{
                    errorTv.text = it.second
                    errorTv.setTextColor(resources.getColor(red_500))
                }


                505 -> {
                    errorTv.text = it.second
                    errorTv.setTextColor(resources.getColor(red_500))
                }

            }

        })





        viewModel.currentPosition.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty()){

                var d: String = ""
                var b: String = ""
                for (i in it){
                    b += "${i.physical_space}, "
                    d += "${i.duration}, "
                }

                beacons.text = b.removeSuffix(",")
                duration.text = d.removeSuffix(",")


            }else{
                beacons.text = "no beacon"
                duration.text = "--"
            }
        })


        viewModel.chartData.observe(viewLifecycleOwner, Observer {


            if(it.isNullOrEmpty()){
                // TODO()

//                setupChart(mapOf())


            }else{

                setupChart(it)
                //Todo()
            }



        })
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


