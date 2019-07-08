package com.example.klsdinfo.main.MainFragments

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
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
import com.example.klsdinfo.data.models.Person2
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.EntryXComparator
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlin.collections.ArrayList


class HomeFragment3 : Fragment(){



    companion object {
        fun newInstance(): HomeFragment3 {
            return HomeFragment3()
        }
    }

    lateinit var viewModel: HomeViewModel
    lateinit var  welcome : TextView
    private var unixTime: Long? = null
    private var unixTimePast: Long? = null
    lateinit var radioGroup: RadioGroup
    lateinit var mView: View
    var delta: Long = 0
    lateinit var ID: String

    lateinit var chart : HorizontalBarChart
    val tvY: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        print("onCreateView")

        mView = inflater.inflate(R.layout.main_home_layout, container, false)
        welcome = mView.findViewById(R.id.welcome_textView)
        radioGroup= mView.findViewById(R.id.radioGroup)

        setupViewModel()

        setupRadioCheckChangeListener(container)









        return mView
    }

    private fun setupChart(map: Map<String,Long>) {

        chart = mView.findViewById(R.id.chart1)
//        var total : Long = 0
//        val percentages = arrayListOf<Long>()
//        for(i in map){
//            total += i.value
//        }
//        for (i in map){
//            percentages.add(i.value/total)
//        }
//
//
//
//
//
//        val yvalues: ArrayList<BarEntry> = arrayListOf()
//
//        for (i in 0 until percentages.size){
//            yvalues.add(BarEntry(i.toFloat(), percentages[i].toFloat()))
//        }
//
//
//        val values = arrayListOf<BarEntry>()
//
//
//
//        for (i in 0 until map.toList().size){
//
//            values.add(
//                BarEntry(
//                    i.toFloat() , map.toList()[i].second.toFloat())
//
//            )
//
//        }
//
//
//
//        val dataSet : BarDataSet = BarDataSet(values, "tenses")
//
//        dataSet.setDrawIcons(true)
//
//        val data: BarData = BarData(dataSet)
//        data.setValueFormatter(PercentFormatter())
//
//        chart.data = data
//
//
///////////////////////////////////////////////////////////
//
//
//        val xVals = arrayListOf<String>()
//        for(i in map.keys){ xVals.add(i)}
//
//        chart.xAxis.valueFormatter = IndexAxisValueFormatter(xVals)
//
//        chart.axisLeft.axisMaximum = 100f
//
//        chart.animateXY(1000,1000)
//
//
//
//        chart.setDrawValueAboveBar(false)
//
//        chart.axisLeft.isEnabled = false
//        chart.axisRight.isEnabled = false
//
//
//        chart.description.isEnabled = false
//        chart.legend.isEnabled = false
//
//        dataSet.colors = ColorTemplate.VORDIPLOM_COLORS.toList()
//        data.setValueTextSize(13f)
//        data.setValueTextColor(Color.DKGRAY)
//
//        chart.invalidate()
    /////////////////////////////////////////


//        chart.setOnChartValueSelectedListener(this)
//         chart.setHighlightEnabled(false)

        chart.setDrawBarShadow(false)

        chart.setDrawValueAboveBar(true)

        chart.getDescription().isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)

        // draw shadows for each bar that show the maximum value
        // chart.setDrawBarShadow(true);

        chart.setDrawGridBackground(false)



        val xl = chart.getXAxis()

        val array = arrayListOf<String>()



        for(i in map.keys){ array.add(i)}


        val obj = object:  IndexAxisValueFormatter() {

            override fun getFormattedValue(value: Float): String {
                val index = Math.round(value)

                if(index < 0 || index >= map.size || index != value as Int){
                    return ""
                }

                return array[index]

            }

        }

        chart.xAxis.valueFormatter = obj//IndexAxisValueFormatter(array)

//        xl.position = XAxisPosition.BOTTOM

        xl.typeface = Typeface.SERIF
        xl.setDrawAxisLine(true)
        xl.setDrawGridLines(true)
        xl.granularity = 10f

        val yl = chart.getAxisLeft()
        yl.typeface = Typeface.SERIF
        yl.setDrawAxisLine(true)
        yl.setDrawGridLines(true)
        yl.axisMinimum = 0f // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        val yr = chart.getAxisRight()
        yr.typeface = Typeface.SERIF
        yr.setDrawAxisLine(true)
        yr.setDrawGridLines(true)
        yr.axisMinimum = 0f // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        chart.setFitBars(true)
        chart.animateY(2000)

        // setting data
//        seekBarY.setProgress(50)
//        seekBarX.setProgress(12)

        val l = chart.getLegend()
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

        val barWidth = 4f
        val spaceForBar = 6f
        val values = arrayListOf<BarEntry>()



        for (i in 0 until map.toList().size){

            values.add(
                BarEntry(
                    i*spaceForBar , map.toList()[i].second.toFloat())

            )

        }

        val set1: BarDataSet

        if (false){//chart.data != null && chart.data.dataSetCount > 0) {
//            set1 = chart.data.getDataSetByIndex(0) as BarDataSet
//            set1.values = values
//            chart.data.notifyDataChanged()
//            chart.notifyDataSetChanged()
        } else {

            set1 = BarDataSet(values, "")

            set1.setDrawIcons(false)

            val dataSets = arrayListOf<IBarDataSet>()
            dataSets.add(set1)

            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.setValueTypeface(Typeface.SERIF)
            data.setBarWidth(barWidth)

            chart.data = data

        }

    }

    private fun setupRadioCheckChangeListener(container: ViewGroup?) {
        radioGroup.setOnCheckedChangeListener { radioGroup, id ->
            val radio : RadioButton = mView.findViewById(id)
            Snackbar.make(container as View,"Time Interval: last ${radio.text}",Snackbar.LENGTH_LONG).show()

            when(id){
                R.id.radio0 -> {
                    setTimeInterval(86400)
                    viewModel.setDates(unixTime.toString(),unixTimePast.toString())
                    Log.e("debugtime", unixTime.toString())
                    Log.e("debugtime", unixTimePast.toString())
                    viewModel.fetchUser()
                }
                R.id.radio1 -> {
                    setTimeInterval(259200)
                    Log.e("debugtime", unixTime.toString())
                    Log.e("debugtime", unixTimePast.toString())
                    viewModel.setDates(unixTime.toString(),unixTimePast.toString())
                    viewModel.fetchUser()

                }
                R.id.radio2 -> {
                    setTimeInterval(604800)
                    Log.e("debugtime", unixTime.toString())
                    Log.e("debugtime", unixTimePast.toString())
                    viewModel.setDates(unixTime.toString(),unixTimePast.toString())
                    viewModel.fetchUser()


                }
                R.id.radio3 -> {
                    setTimeInterval(2592000)
                    Log.e("debugtime", unixTime.toString())
                    Log.e("debugtime", unixTimePast.toString())
                    viewModel.setDates(unixTime.toString(),unixTimePast.toString())
                    viewModel.fetchUser()

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





        viewModel.user.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                welcome.text = (it.shortName)
                ID = it.holder.id.toString()
            }else{
                welcome.text = "No user Found, please relog!"
                ID = ""

            }
        })


        viewModel.chartData.observe(viewLifecycleOwner, Observer {

          Log.i("data", it.toString())

            setupChart(it)
        })
    }


    override fun onStart() {
        super.onStart()
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if(user!= null){

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


