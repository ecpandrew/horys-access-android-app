package com.example.klsdinfo.main.TableFragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.example.klsdinfo.R
import com.example.klsdinfo.data.CheckPhysicalSpacesViewModel
import com.example.klsdinfo.data.DanielApiService
import com.example.klsdinfo.data.DanielServiceRepository
import com.example.klsdinfo.data.ViewModelFactory
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.main.adapters.TableOneAdapter


class CheckPhysicalSpacesFragment : Fragment(), LifecycleOwner {




    lateinit var url: String
    private lateinit var queue: RequestQueue
    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: TableOneAdapter
    lateinit var noResults: TextView
    lateinit var mView : View
    lateinit var viewModel: CheckPhysicalSpacesViewModel
    lateinit var progressBar: ProgressBar

    companion object {
        fun newInstance(): CheckPhysicalSpacesFragment {
            return CheckPhysicalSpacesFragment()
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
            mAdapter = TableOneAdapter(context!!, it)
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

        viewModel = ViewModelProviders.of(this, factory).get(CheckPhysicalSpacesViewModel::class.java)

    }
    private fun init(inflater: LayoutInflater, container: ViewGroup?) {
        mView = inflater.inflate(R.layout.table_one_layout, container, false)
        val linearLayoutManager = LinearLayoutManager(context)
        noResults = mView.findViewById(R.id.no_result)
        recyclerView = mView.findViewById(R.id.rv_resource_1)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        progressBar = mView.findViewById(R.id.progress_bar)

    }


    override fun onStart() {
        super.onStart()
        viewModel.fetchPeople()
    }


}
