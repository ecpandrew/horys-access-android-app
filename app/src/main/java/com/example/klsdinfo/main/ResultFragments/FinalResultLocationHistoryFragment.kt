package com.example.klsdinfo.main.ResultFragments

import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import com.example.klsdinfo.R
import com.example.klsdinfo.data.models.TableFiveResource
import com.example.klsdinfo.data.models.TableFourResource
import com.example.klsdinfo.main.MainFragments.CustomTableFragment
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import java.util.ArrayList

class FinalResultLocationHistoryFragment : Fragment(), LifecycleOwner {

    companion object {
        fun newInstance(): FinalResultLocationHistoryFragment {
            return FinalResultLocationHistoryFragment()
        }
    }

    lateinit var btnLog : Button
    lateinit var btnDetail : Button
    lateinit var tool: Toolbar
    lateinit var myView : View
    lateinit var titleTV : TextView
    lateinit var mainTitle : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.final_result_location_history, container, false)
        btnDetail = myView.findViewById(R.id.btn_detail)
        btnLog = myView.findViewById(R.id.btn_log)
        titleTV = myView.findViewById(R.id.textViewTitle)
//        tool = myView.findViewById(R.id.toolbar)





        val lista: ArrayList<Parcelable>? = arguments?.getParcelableArrayList<Parcelable>("resources")
        val ref: String? = arguments?.getString("ref")
        val title: String? = arguments?.getString("person")

        mainTitle = title?:""
        titleTV.text = "$title: (person x hours)"

        when{
            lista == null ->{
            }

            lista.isEmpty() -> {

            }

            else -> {

                when (ref) {

                    "location_history_child_data" ->{

                        setUpChart(lista)
                        setUpButtons(lista)

                    }


                }
            }
        }

        return myView
    }

    private fun setUpButtons(lista: ArrayList<Parcelable>) {


        btnLog.setOnClickListener {
            val bundle = Bundle()
            var ref ="child_log5"
            bundle.putString("ref", ref)
            bundle.putString("person", mainTitle)
            bundle.putParcelableArrayList("resources", lista) // ??
            val dialog = CustomTableFragment()
            dialog.arguments = bundle
            val activity: AppCompatActivity = context as AppCompatActivity // ??
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog")

        }

        btnDetail.setOnClickListener {
            val bundle = Bundle()
            var ref ="child_detail5"
            bundle.putString("person", mainTitle)
            bundle.putString("ref", ref)
            bundle.putParcelableArrayList("resources", lista) // ??
            val dialog = CustomTableFragment()
            dialog.arguments = bundle
            val activity: AppCompatActivity = context as AppCompatActivity // ??
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog")

        }

    }

    private fun setUpChart(lista: ArrayList<Parcelable>) {

        val chart = myView.findViewById<HorizontalBarChart>(R.id.chart1)


//        val countMap: MutableMap<String, Long> = mutableMapOf()
//        val map: MutableMap<String, Long> = mutableMapOf()
//
//        for (element in lista){
//
//            val item = element as TableFourResource
//            if(!countMap.containsKey(item.physical_space)){
////                countMap[item.shortName] = 1
//                map[item.physical_space] = item.getDuration()
//            }else{
////                countMap[item.shortName] = countMap[item.shortName]!!.plus(1)
//                map[item.physical_space] = map[item.physical_space]!!.plus(item.getDuration())
//            }
//        }

        val countMap: MutableMap<String, Long> = mutableMapOf()
        val durationMap: MutableMap<String, Long> = mutableMapOf()

        for (element in lista){

            val item = element as TableFiveResource
            if(!countMap.containsKey(item.shortName)){
                countMap[item.shortName] = 1
                durationMap[item.shortName] = item.getDuration()
            }else{
                countMap[item.shortName] = countMap[item.shortName]!!.plus(1)
                durationMap[item.shortName] = durationMap[item.shortName]!!.plus(item.getDuration())
            }


        }


        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.description.isEnabled = false
        chart.setMaxVisibleValueCount(60)
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)
        val xl = chart.xAxis
        val array = arrayListOf<String>()
        for(i in durationMap.keys){ array.add(i)}
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(array)
        xl.position = XAxis.XAxisPosition.BOTTOM // caso fique estranho mudar para .BOTTOM
        xl.typeface = Typeface.SERIF
        xl.setDrawAxisLine(true)
        xl.setDrawGridLines(true)
        xl.granularity = 1f
        val yl = chart.axisLeft
        yl.typeface = Typeface.SERIF
        yl.setDrawAxisLine(true)
        yl.setDrawGridLines(true)
        yl.axisMinimum = 0f // this replaces setStartAtZero(true)
        val yr = chart.axisRight
        yr.typeface = Typeface.SERIF
        yr.setDrawAxisLine(true)
        yr.setDrawGridLines(true)
        yr.axisMinimum = 0f // this replaces setStartAtZero(true)
        chart.setFitBars(true)
        chart.animateY(1000)
        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(true)
        l.formSize = 8f
        l.xEntrySpace = 4f
        chart.setDrawValueAboveBar(false)
        chart.legend.isEnabled = false





        val barWidth = .4f
        val spaceForBar = 1f
        val values = arrayListOf<BarEntry>()

        for (i in 0 until durationMap.toList().size){

            values.add(
                BarEntry(
                    i*spaceForBar , durationMap.toList()[i].second.toFloat()/3600)

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
            data.barWidth = barWidth
//            chart.xAxis.valueFormatter = LabelValueFormatter(data)//IndexAxisValueFormatter(array)//obj//


            chart.data = data

        }


    }

}

