package com.example.klsdinfo.main.TableFragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.example.klsdinfo.R
import com.example.klsdinfo.Volley.VolleySingleton
import com.example.klsdinfo.data.*
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.models.FakeRequest
import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.TableTwoResource
import com.example.klsdinfo.main.adapters.TableOneAdapter
import com.example.klsdinfo.main.adapters.TableTwoAdapter
import java.io.UnsupportedEncodingException


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

        val bundle: Bundle? = arguments
        queue = VolleySingleton.getInstance(context).requestQueue

        if (bundle == null || bundle.isEmpty){
            // Todo: Não há resultados

        }else{
            val persons: List<Person2>? = bundle.getParcelableArrayList("resources")
            if (persons == null){
                // Todo: tratar isso dai
            }else{
                Log.i("Response", "lista $persons")

                // classificar por role
                map = mutableMapOf()

                for(person in persons){
                    if(person.roles != null){
                        for(role in person.roles){
                            if(!map.containsKey(role.name)){
                                map[role.name] = mutableListOf(person)
                            }else{
                                val aux = map[role.name]
                                aux!!.add(person)
                                map[role.name] = aux
                            }
                        }
                    }
                }

                Log.i("Response", "map $map")


                for (entry in map){
                    var id = "/"
                    for(person in entry.value){
                        id+="${person.holder.id}/"
                    }
                    url = "http://smartlab.lsdi.ufma.br/service/persons/${id}physical_spaces/"

                    makeRequest(url, entry.key)
                }
                Log.i("Response", "map $mapResults")
            }
        }


        return mView
    }


    private fun subscribeToModel() {


        viewModel.adaterData.observe(viewLifecycleOwner, Observer {
//            mAdapter = TableOneAdapter(context!!, it)
//            recyclerView.adapter = mAdapter
//            mAdapter.notifyDataSetChanged()
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

    // Todo: eliminar a necessidade de varios requests
    private fun makeRequest(url: String, role: String) {
        val stringRequest = VolleyUTF8EncodingStringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                val r: List<TableTwoResource> = FakeRequest()
                    .getTableTwoData(response)
                if (r.isNotEmpty()) {
                    noResults.visibility = View.GONE

                    mapResults[role] = r

                    mAdapter = TableTwoAdapter(context!!, mapResults)
                    recyclerView.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()


                }else{
                    noResults.visibility = View.VISIBLE

                }

            },
            Response.ErrorListener {
                VolleyLog.e("Error: ", it.message)
                noResults.text = it.message
                noResults.visibility = View.VISIBLE


            })
        stringRequest.retryPolicy = DefaultRetryPolicy(20 * 1000, 3, 1.0f)
        stringRequest.tag = this
        queue.add(stringRequest)
    }
    override fun onStop() {
        super.onStop()
        queue.cancelAll(this)
        VolleyLog.e("Error: ", "Request Cancelado")
    }
    class VolleyUTF8EncodingStringRequest(
        method: Int, url: String, private val mListener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ) : Request<String>(method, url, errorListener) {

        override fun deliverResponse(response: String) {
            mListener.onResponse(response)
        }

        override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
            var parsed: String

            val encoding = charset(HttpHeaderParser.parseCharset(response.headers))

            // TODO: colcar o return dentro do try
            return try {
                parsed = String(response.data, encoding)
                val bytes = parsed.toByteArray(encoding)
                parsed = String(bytes, charset("UTF-8"))

                Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response))
            } catch (e: UnsupportedEncodingException) {
                Response.error(ParseError(e))
            }
        }
    }


}