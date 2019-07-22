package com.example.klsdinfo.main.TableFragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.example.klsdinfo.R
import com.example.klsdinfo.data.*
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.models.*
import com.example.klsdinfo.main.ChartFragments.LocationHistoryChartFragment
import com.example.klsdinfo.main.MainFragments.CustomTableFragment
import com.example.klsdinfo.main.adapters.TableFiveAdapter


class LocationHistoryResultFragment : Fragment(), LifecycleOwner {

    private lateinit var dateStr: String
    private lateinit var dateStr2: String
    val TAG: String = "FullScreenDialog"
    lateinit var map : MutableMap<String, Long>
    lateinit var id: String
    lateinit var url: String
    private lateinit var queue: RequestQueue
    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: TableFiveAdapter
    lateinit var noResults: TextView
    lateinit var mView: View
    lateinit var viewModel: LocationHistoryViewModel
    lateinit var progressBar: ProgressBar



    companion object {
        fun newInstance(): LocationHistoryResultFragment {
            return LocationHistoryResultFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)



        init(inflater, container)

        createViewModel()
        subscribeToModel()

        return mView
    }



    private fun subscribeToModel() {


        viewModel.getResources().observe(viewLifecycleOwner, Observer {
            Log.i("retrofit", "table three list = $it")

            generateParentTable(it)

        })

        viewModel.getAdapterData().observe(viewLifecycleOwner, Observer {
            mAdapter = TableFiveAdapter(context!!, it)
            recyclerView.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
        })

        viewModel.getProgress().observe(viewLifecycleOwner, Observer {
            when(it){
                true -> progressBar.visibility = View.VISIBLE
                false -> progressBar.visibility = View.GONE
            }
        })
    }

    private fun createViewModel() {
        val repo = DanielServiceRepository.getInstance(DanielApiService.create(), AppDatabase.getInstance(activity?.applicationContext!!)!!)
        val factory = ViewModelFactory(null,repo, activity?.application!!)

        viewModel = ViewModelProviders.of(this, factory).get(LocationHistoryViewModel::class.java)

    }


    private fun init(inflater: LayoutInflater, container: ViewGroup?) {
        mView = inflater.inflate(R.layout.table_five_layout, container, false)
        noResults = mView.findViewById(R.id.no_result)
        progressBar = mView.findViewById(R.id.progress_bar)

        recyclerView = mView.findViewById(R.id.tableFourRV)
        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.setHasFixedSize(true)
    }


    override fun onStart() {
        super.onStart()
        viewModel.fetchData()
    }


    // Todo: esse trecho nao é null safe. Corrigir
    private fun generateParentTable(
        lista: List<TableFiveResource>
    ) {

        val map: MutableMap<String, Long> = mutableMapOf()
        val childMap: MutableMap<String, MutableList<TableFiveResource>> = mutableMapOf()
        val childAux: MutableMap<String, MutableMap<String, Long>> = mutableMapOf()

        for (resource in lista) {
            if(!map.containsKey(resource.physical_space)){
                map[resource.physical_space] = resource.getDuration()
            }else{
                map[resource.physical_space] = resource.getDuration() + map[resource.physical_space]!!
            }

            if (!childMap.containsKey(resource.shortName)) {
                childMap[resource.shortName] = mutableListOf(resource)
            } else {
                val aux: MutableList<TableFiveResource> = childMap[resource.shortName]!!
                aux.add(resource)
                childMap[resource.shortName] = aux
            }
        }

        for (entry in childMap) {

            if (!childAux.containsKey(entry.key)) {
                childAux[entry.key] = mutableMapOf()
            } else {

            }

            val childDuration: MutableMap<String, Long> = mutableMapOf()
            for (resource in entry.value) {
                if (!childDuration.containsKey(resource.physical_space)) {
                    childDuration[resource.physical_space] = resource.getDuration()
                } else {
                    childDuration[resource.physical_space] =
                        resource.getDuration() + childDuration[resource.physical_space]!!

                }
            }
            childAux[entry.key] = childDuration
        }

        val countMap: MutableMap<String, Int> = mutableMapOf()

        for (entry in childAux){
            for (element in entry.value){

                if(!countMap.containsKey(element.key)){
                    countMap[element.key] = 1
                }else{
                    countMap[element.key] = countMap[element.key]!! + 1
                }
            }
        }


        val x: MutableList<Table4Aux> = mutableListOf()

        for (element in map){

            x.add(Table4Aux(element.key, countMap[element.key]!!, map[element.key]!!))
        }


        val card: CardView = view!!.findViewById(R.id.tableFourCardView)

        (card.findViewById(R.id.btn_detail) as Button).setOnClickListener {
            // Todo: details
            val bundle = Bundle()
            var ref ="detail5"
            bundle.putString("ref", ref)
            bundle.putParcelableArrayList("resources", x as ArrayList<out Parcelable>) // ??
            val dialog = CustomTableFragment()
            dialog.arguments = bundle
            val activity: AppCompatActivity = context as AppCompatActivity // ??
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog")

        }



        (card.findViewById(R.id.btn_chart) as Button).setOnClickListener {
            // Todo: details

            val bundle = Bundle()


            val ref ="main_chart"


            bundle.putString("ref", ref)

            bundle.putParcelableArrayList("resources", x as ArrayList<out Parcelable>) // ??

            val dialog = LocationHistoryChartFragment()

            dialog.arguments = bundle
            val activity: AppCompatActivity = context as AppCompatActivity // ??
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog")

        }



        (card.findViewById(R.id.btn_log) as Button).setOnClickListener {

            val bundle = Bundle()
            var ref ="log5"
            bundle.putString("ref", ref)
            bundle.putParcelableArrayList("resources", lista as ArrayList<out Parcelable>) // ??
            val dialog = CustomTableFragment()
            dialog.arguments = bundle
            val activity: AppCompatActivity = context as AppCompatActivity // ??
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog")

        }




        var count: Long = 0
        for (entry in map){
            count += entry.value
        }


        (card.findViewById(R.id.nameTV4) as TextView).text = ("Physical Space History")
        (card.findViewById(R.id.descriptionTV4) as TextView).text =("Physical Spaces Found: ${map.size}")
        (card.findViewById(R.id.nplacesTV4) as TextView).text = ("Total Time Elapsed: ${count/60}m")
        card.visibility = View.VISIBLE
    }









}