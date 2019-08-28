package com.example.klsdinfo.main.ResultFragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.example.klsdinfo.R
import com.example.klsdinfo.data.*
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.TableTwoResource
import com.example.klsdinfo.main.adapters.TableTwoAdapter


class FindPeopleFragment : Fragment(), LifecycleOwner {

    lateinit var cardview: CardView
    lateinit var textview: TextView
    lateinit var map : MutableMap<String, MutableList<Person2>>
    lateinit var mapResults : MutableMap<String, List<TableTwoResource>>
    lateinit var url: String
    private lateinit var queue: RequestQueue
    private lateinit var parentMap: MutableMap<String,List<TableTwoResource>>
    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: TableTwoAdapter
    lateinit var noResults: TextView
    lateinit var mView : View
    lateinit var progressBar: ProgressBar
    lateinit var viewModel: FindPeopleViewModel


    companion object {
        fun newInstance(): FindPeopleFragment {
            return FindPeopleFragment()
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

        viewModel.adaterData.observe(viewLifecycleOwner, Observer {
            mAdapter = TableTwoAdapter(context!!, it)
            recyclerView.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
        })

        viewModel.loadingProgress.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    progressBar.visibility = View.VISIBLE
                }
                false -> {
                    progressBar.visibility = View.GONE
                }
            }
        })
    }


    private fun createViewModel() {
        val repo = DanielServiceRepository.getInstance(DanielApiService.create(), AppDatabase.getInstance(activity?.applicationContext!!)!!)
        val factory = ViewModelFactory(null,repo, activity?.application!!)

        viewModel = ViewModelProviders.of(this, factory).get(FindPeopleViewModel::class.java)

    }




    private fun init(inflater: LayoutInflater, container: ViewGroup?) {
        mView = inflater.inflate(R.layout.table_two_layout, container, false)
        noResults = mView.findViewById(R.id.no_result)
        recyclerView = mView.findViewById(R.id.rv_resource_2)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        parentMap = mutableMapOf()
        mapResults = mutableMapOf()
        progressBar = mView.findViewById(R.id.progress_bar)



    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchData()
    }


}