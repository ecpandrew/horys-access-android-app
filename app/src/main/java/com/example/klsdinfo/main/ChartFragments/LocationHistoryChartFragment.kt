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

class LocationHistoryChartFragment : DialogFragment() {

    val TAG: String = "FullScreenDialog"
    lateinit var tool: Toolbar


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

                    }

                }


            }
        }


        return view
    }



    private fun generateMainChart(lista: ArrayList<Parcelable>) {
        for(element in lista){
            val resource : Table4Aux? = element as? Table4Aux
            if(resource != null){
                // Todo()
                Log.i("debug", resource.toString())
            }
        }

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


        // Todo()
        Log.i("debug", durationMap.toString())


    }



    private fun cancelUpload() {
        dialog?.dismiss()
    }


}