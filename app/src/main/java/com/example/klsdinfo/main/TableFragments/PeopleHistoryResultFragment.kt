package com.example.klsdinfo.main.TableFragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
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
import com.example.klsdinfo.data.DanielApiService
import com.example.klsdinfo.data.DanielServiceRepository
import com.example.klsdinfo.data.PeopleHistoryViewModel
import com.example.klsdinfo.data.ViewModelFactory
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.models.*
import com.example.klsdinfo.main.ChartFragments.PeopleHistoryChartFragment
import com.example.klsdinfo.main.MainFragments.CustomTableFragment
import com.example.klsdinfo.main.adapters.TableFourAdapter


class PeopleHistoryResultFragment : Fragment(), LifecycleOwner {

    private lateinit var dateStr: String
    private lateinit var dateStr2: String
    val TAG: String = "FullScreenDialog"
    lateinit var map : MutableMap<String, Long>
    lateinit var id: String
    lateinit var url: String
    lateinit var linear: LinearLayout
    lateinit var parentLinear: LinearLayout
    private lateinit var queue: RequestQueue
    lateinit var progress: AlertDialog.Builder
    lateinit var progressBar: ProgressBar

    lateinit var alertDialog: AlertDialog
    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: TableFourAdapter
    lateinit var noResults: TextView
    lateinit var mView: View
    lateinit var viewModel : PeopleHistoryViewModel



    companion object {
        fun newInstance(): PeopleHistoryResultFragment {
            return PeopleHistoryResultFragment()
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
            mAdapter = TableFourAdapter(context!!, it)
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

        viewModel = ViewModelProviders.of(this, factory).get(PeopleHistoryViewModel::class.java)

    }


    private fun init(inflater : LayoutInflater, container: ViewGroup?) {
        mView = inflater.inflate(R.layout.table_four_layout, container, false)
        progressBar = mView.findViewById(R.id.progress_bar)
        noResults = mView.findViewById(R.id.no_result)
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
        lista: List<TableFourResource>

    ) {

        val map: MutableMap<String, Long> = mutableMapOf()
        val childMap: MutableMap<String, MutableList<TableFourResource>> = mutableMapOf()
        val childAux: MutableMap<String, MutableMap<String, Long>> = mutableMapOf()
        for (resource in lista) {
            if (!map.containsKey(resource.shortName)) {
                map[resource.shortName] = resource.getDuration()
            } else {
                map[resource.shortName] = resource.getDuration() + map[resource.shortName]!!
            }
            if (!childMap.containsKey(resource.physical_space)) {
                childMap[resource.physical_space] = mutableListOf(resource)
            } else {
                val aux: MutableList<TableFourResource> = childMap[resource.physical_space]!!
                aux.add(resource)
                childMap[resource.physical_space] = aux
            }
        }
        for (entry in childMap) {

            if (!childAux.containsKey(entry.key)) {
                childAux[entry.key] = mutableMapOf()
            } else {

            }

            val childDuration: MutableMap<String, Long> = mutableMapOf()
            for (resource in entry.value) {
                if (!childDuration.containsKey(resource.shortName)) {
                    childDuration[resource.shortName] = resource.getDuration()
                } else {
                    childDuration[resource.shortName] =
                        resource.getDuration() + childDuration[resource.shortName]!!

                }
            }
            childAux[entry.key] = childDuration
        }

        val countMap: MutableMap<String, Int> = mutableMapOf()

        for (entry in childAux) {
            for (element in entry.value) {

                if (!countMap.containsKey(element.key)) {
                    countMap[element.key] = 1
                } else {
                    countMap[element.key] = countMap[element.key]!! + 1
                }
            }
        }
        Log.i("recebido4", "map $map")
        Log.i("recebido4", "child map$childMap")
        Log.i("recebido4", "aux map$childAux")
        Log.i("recebido4", "count map$countMap")

        val map2: MutableMap<String, MutableList<TableFourResource>> = mutableMapOf()
        for (element in lista) {
            if (!map2.containsKey(element.shortName)) {
                map2[element.shortName] = mutableListOf(element)
            } else {
                val i: MutableList<TableFourResource> = map2[element.shortName]!!
                i.add(element)
                map2[element.shortName] = i
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
            var ref ="detail4"
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
            var ref ="main_chart"
            bundle.putString("ref", ref)
            bundle.putParcelableArrayList("resources", x as ArrayList<out Parcelable>) // ??
            val dialog = PeopleHistoryChartFragment()
            dialog.arguments = bundle
            val activity: AppCompatActivity = context as AppCompatActivity // ??
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog")
        }





        (card.findViewById(R.id.btn_log) as Button).setOnClickListener {

            val bundle = Bundle()
            var ref ="log4"
            bundle.putString("ref", ref)
            bundle.putParcelableArrayList("resources", lista as ArrayList<out Parcelable>) // ??
            val dialog = CustomTableFragment()
            dialog.arguments = bundle
            val activity: AppCompatActivity = context as AppCompatActivity // ??
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog")

        }



        var count: Long = 0
        for (entry in childAux){
            for(element in entry.value){
                count += element.value
            }
        }


        (card.findViewById(R.id.nameTV4) as TextView).text = ("Person History")
        (card.findViewById(R.id.descriptionTV4) as TextView).text = ("People Found: ${map.size}")
        (card.findViewById(R.id.nplacesTV4) as TextView).text = ("Physical Spaces Found: ${childAux.size}")
        (card.findViewById(R.id.durationTV4) as TextView).text = ("Total Time Elapsed: ${count/60} min")
        card.visibility = View.VISIBLE

    }








}