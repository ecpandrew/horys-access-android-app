package com.example.klsdinfo.main.ChartFragments

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
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import kotlinx.android.synthetic.main.main_home_layout.*


class LocationHistoryChartFragment : DialogFragment() {

    val TAG: String = "FullScreenDialog"
    lateinit var tool: Toolbar
    lateinit var chart1: HorizontalBarChart

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


        chart1 = view.findViewById(R.id.chart1)




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





        // primeiro converta o map para uma lista, como vc não precisa dos rotulos deve haver algum metodo do Map chamado .values
        // que retorna uma lista com os valores, então dai vc só precisa iterar sobre essa lista de valores
        // e adicionar cada valor em uma BarEntry
        // vc pode fazer essa conversão aqui ou dentro do metodo setChildData


        // Chame o Metodo setChildData passando o map ou a lista de valores



        // Todo()

        // Kotlin -> declaração de variavel ->     var ou val
        //                                         var numero : <Tipo da variavel> = 10

        // Kotlin -> get e set -> aqui não precisamos de getter e setter os metodos já trazem implementado

        val string: String = "Ola"

        // veja o getLength por exemplo, usamos o .lenght tanto para o get quanto para o set
        string.length
//      string.length = 2   O lenght não possui setter mas seria algo assim se fosse um metodo que permitisse



    }



    private fun cancelUpload() {
        dialog?.dismiss()

    }


    private fun setData(data: MutableList<Table4Aux>) {












        val yVals = arrayListOf<BarEntry>()

        val barWidth = 9f
        val spaceForBar = 10f

        for (i in 0 until data.size) {

            yVals.add(BarEntry(i * spaceForBar, data[i].duration.toFloat()))
        }

        val set: BarDataSet

        set = BarDataSet(yVals, "Data Set")

        val data = BarData(set)









        chart1.data = data

    }


    private fun setChildData(data: Map<String, Long>) {











        val yVals = arrayListOf<BarEntry>()

        val barWidth = 9f
        val spaceForBar = 10f

//        for (i in 0 until count) {
//            val x = (Math.random() * range).toFloat()
//            yVals.add(BarEntry(i * spaceForBar, x))
//        }

        val set: BarDataSet

        set = BarDataSet(yVals, "Data Set")

        val data = BarData(set)

















        chart1.data = data

    }


}