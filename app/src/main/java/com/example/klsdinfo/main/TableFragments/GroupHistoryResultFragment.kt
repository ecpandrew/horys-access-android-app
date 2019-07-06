package com.example.klsdinfo.main.TableFragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.example.klsdinfo.R
import com.example.klsdinfo.data.*
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.models.*
import com.example.klsdinfo.main.MainFragments.CustomTableFragment
import com.example.klsdinfo.main.adapters.TableThreeAdapter
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.*


class GroupHistoryResultFragment : Fragment(), LifecycleOwner {

    val TAG: String = "FullScreenDialog"
    lateinit var id: String
    lateinit var url: String
    private lateinit var dateStr: String
    private lateinit var dateStr2: String
    lateinit var linear: LinearLayout
    lateinit var parentLinear: LinearLayout
    private lateinit var queue: RequestQueue

    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: TableThreeAdapter
    lateinit var data: List<TableThreeResource>
    lateinit var viewModel : GroupHistoryViewModel
    lateinit var mView: View
    lateinit var noResults: TextView
    companion object {
        fun newInstance(): GroupHistoryResultFragment {
            return GroupHistoryResultFragment()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchData()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        init(inflater, container)
        createViewModel()
        subscribeToModel()
        return mView
    }

    private fun init(inflater : LayoutInflater, container: ViewGroup?) {
        mView = inflater.inflate(R.layout.table_three_layout, container, false)
        noResults = mView.findViewById(R.id.no_result)
        data = listOf()
        recyclerView = mView.findViewById(R.id.rv_resource_3)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)
        progressBar = mView.findViewById(R.id.progress_bar)
    }

    private fun subscribeToModel() {

        viewModel.getResources().observe(viewLifecycleOwner, Observer {
            Log.i("retrofit", "table three list = $it")

            generateParentTable(it)



        })

        viewModel.getAdapterData().observe(viewLifecycleOwner, Observer {
            mAdapter = TableThreeAdapter(context!!, it)
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

        viewModel = ViewModelProviders.of(this, factory).get(GroupHistoryViewModel::class.java)
    }


    private fun generateParentTable(lista: List<TableThreeResource>) {

        if(lista.isNullOrEmpty()){
            noResults.visibility = View.VISIBLE
        }else{

            noResults.visibility = View.GONE



            val card: CardView = view!!.findViewById(R.id.tableThreeCardView)

            (card.findViewById(R.id.btn_detail) as MaterialButton).setOnClickListener{
                Toast.makeText(context,"Not Implemented Yet",Toast.LENGTH_SHORT).show()



                val bundle = Bundle()
                var ref ="detail3"
                bundle.putString("ref", ref)
                bundle.putParcelableArrayList("resources", lista as ArrayList<out Parcelable>) // ??

                val dialog = CustomTableFragment()

                dialog.arguments = bundle
                val activity: AppCompatActivity = context as AppCompatActivity // ??
                val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                dialog.show(transaction, "FullScreenDialog")
            }

            (card.findViewById(R.id.btn_log) as MaterialButton).setOnClickListener {
                val bundle = Bundle()
                var ref ="log3"
                bundle.putString("ref", ref)
                bundle.putParcelableArrayList("resources", lista as ArrayList<out Parcelable>) // ??
                val dialog = CustomTableFragment()
                dialog.arguments = bundle
                val activity: AppCompatActivity = context as AppCompatActivity // ??
                val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                dialog.show(transaction, "FullScreenDialog")
            }


            val nf = NumberFormat.getInstance() // get instance
            nf.maximumFractionDigits = 2 // set decimal places

            // Seconds
            var totalDuration: Long = 0
            for (resource in lista){
                totalDuration += resource.getDuration()
                 }
            val mean = (totalDuration/lista.size)

            val m = nf.format(mean.toFloat()/3600)
            val tD = nf.format(totalDuration.toFloat()/3600)


            (card.findViewById(R.id.nameTV4) as TextView).text = ("Group History")
            (card.findViewById(R.id.descriptionTV4) as TextView).text = ("Encounters: ${lista.size}")
            (card.findViewById(R.id.nplacesTV4) as TextView).visibility = View.GONE//.text = ("Physical Spaces Found: ${mean/60}")
            (card.findViewById(R.id.durationTV4) as TextView).text = ("Total Time Elapsed: ${totalDuration/60} min")
            card.visibility = View.VISIBLE
            }

    }


}


