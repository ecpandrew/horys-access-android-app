package com.example.klsdinfo.main.ChartFragments

import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.example.klsdinfo.R
import com.example.klsdinfo.data.models.Table4Aux
import com.example.klsdinfo.data.models.TableFiveResource
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.main_home_layout.*


class LocationHistoryChartFragment : DialogFragment() {

    val TAG: String = "FullScreenDialog"
    lateinit var tool: Toolbar
    lateinit var chart: HorizontalBarChart

    override fun getTheme(): Int {
        return R.style.FullScreenDialogStyle
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.chart_history_location, container, false)
        tool = view.findViewById(R.id.toolbar)


        chart = view.findViewById(R.id.chart1)




        tool.setNavigationIcon(R.drawable.ic_close_white_24dp)
        tool.setNavigationOnClickListener {
            cancelUpload()
        }


        val lista: ArrayList<Parcelable>? = arguments?.getParcelableArrayList<Parcelable>("resources")


        val ref: String? = arguments?.getString("ref")

        when{
            lista == null ->{

            }

            lista.isEmpty() -> {

            }

            else -> {

                when (ref) {


                    "main_chart" -> {

                        generateMainChart(lista)

                    }
                    "child_chart" ->  {

                        generateChildChart(lista)

                        // Vc deve completar esse metodo generateChildChart()



                    }

                }


            }
        }


        return view




    }



    private fun generateMainChart(lista: ArrayList<Parcelable>) {

        val array: MutableList<Table4Aux> = mutableListOf()

        for(element in lista){
            val resource : Table4Aux? = element as? Table4Aux
            if(resource != null){
                array.add(resource)
            }
        }
        Log.i("debug", array.toString())
        // Todo()
        setData(array)
    }





    private fun generateChildChart(lista: ArrayList<Parcelable>) {
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


        Log.i("debug", durationMap.toString())

        setChildData(durationMap)

    }



    private fun cancelUpload() {
        dialog?.dismiss()

    }


    private fun setData(map: MutableList<Table4Aux>) {



        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.description.isEnabled = false
        chart.setMaxVisibleValueCount(60)
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)
        val xl = chart.xAxis
        val array = arrayListOf<String>()
        for(i in map){ array.add(i.name)}
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










        val barWidth = .9f
        val spaceForBar = 1f
        val values = arrayListOf<BarEntry>()

        for (i in 0 until map.toList().size){

            values.add(
                BarEntry(
                    i*spaceForBar , map.toList()[i].duration.toFloat())

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
            data.barWidth = .4f
//            chart.xAxis.valueFormatter = LabelValueFormatter(data)//IndexAxisValueFormatter(array)//obj//


            chart.data = data
        }

//        val yVals = arrayListOf<BarEntry>()
//
//        val barWidth = 9f
//        val spaceForBar = 10f
//
//        for (i in 0 until data.size) {
//
//            yVals.add(BarEntry(i * spaceForBar, data[i].duration.toFloat()))
//        }
//
//        val set: BarDataSet
//
//        set = BarDataSet(yVals, "Data Set")
//
//        val data = BarData(set)
//
//
//
//
//
//
//
//
//
//        chart.data = data

    }


    private fun setChildData(map: Map<String, Long>) {
        val title: String? = arguments?.getString("person")
        tool.title = "$title's chart"


        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.description.isEnabled = false
        chart.setMaxVisibleValueCount(60)
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)
        val xl = chart.xAxis
        val array = arrayListOf<String>()
        for(i in map.keys){ array.add(i)}
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
            data.barWidth = .4f
//            chart.xAxis.valueFormatter = LabelValueFormatter(data)//IndexAxisValueFormatter(array)//obj//


            chart.data = data

        }


//
//        val yVals = arrayListOf<BarEntry>()
//
//        val barWidth = 9f
//        val spaceForBar = 10f
//        var count = 0f
//
//        for ((name, duration) in data) {
//
//
//            yVals.add(BarEntry(count * spaceForBar, duration.toFloat()))
//            count++
//        }
//
//        val set: BarDataSet
//
//        set = BarDataSet(yVals, "Data Set")
//
//        val data = BarData(set)
//
//
//        chart1.data = data


    }



    private fun setupChart() {


        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.description.isEnabled = false
        chart.setMaxVisibleValueCount(60)
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)
        val xl = chart.xAxis
        val array = arrayListOf<String>()
//        for(i in map.keys){ array.add(i)}
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

    }


}