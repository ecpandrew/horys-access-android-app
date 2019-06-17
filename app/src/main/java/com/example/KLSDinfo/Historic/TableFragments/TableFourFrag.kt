package com.example.KLSDinfo.Historic.TableFragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.example.KLSDinfo.CustomTable.CustomTableDialog
import com.example.KLSDinfo.Historic.adapters.TableFourAdapter
import com.example.KLSDinfo.Models.*
import com.example.KLSDinfo.R
import com.example.KLSDinfo.Volley.VolleySingleton
import java.io.UnsupportedEncodingException
import java.lang.Exception


class TableFourFrag : Fragment() {

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
    lateinit var alertDialog: AlertDialog
    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: TableFourAdapter
    lateinit var dividerItemDecoration: DividerItemDecoration



    companion object {
        fun newInstance(): TableFourFrag {
            return TableFourFrag()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.table_four_layout, container, false)
        val linearLayoutManager = LinearLayoutManager(context)

        recyclerView = view.findViewById(R.id.tableFourRV)
        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.setHasFixedSize(true)
//        dividerItemDecoration = DividerItemDecoration(recyclerView.context, linearLayoutManager.orientation)
//        recyclerView.addItemDecoration(dividerItemDecoration)

        queue = VolleySingleton.getInstance(context).requestQueue

        val bundle: Bundle? = arguments

        if (bundle == null || bundle.isEmpty){
            // Todo: Não há resultados

        }else {
            //todo: tratar os unix para o caso de vir null
            val persons: List<Person2>? = bundle.getParcelableArrayList("resources")
            val unix: Long = bundle.getLong("date")
            val unixPast: Long = bundle.getLong("date2")

            try{
                dateStr = bundle.getString("dateStr")!!
                dateStr2 = bundle.getString("dateStr2")!!

            }catch (e:Exception){}


            if (persons == null){
                // Todo: tratar isso dai
            }else{
                Log.i("recebido", persons.toString())
                Log.i("recebido", "${bundle.getLong("date")} and ${bundle.getLong("date2")}")
                id = ""
                map = mutableMapOf()

                for (person in persons){
                    id += "${person.holder.id}/"
                    map[person.shortName] = 0
                }
                Log.i("recebido", id)

                url = "http://smartlab.lsdi.ufma.br/service/persons/${id}physical_spaces/${unixPast+10800}/${unix+10800}"

                progress = AlertDialog.Builder(context)
                progress.setCancelable(false)
                progress.setView(R.layout.loading_dialog_layout)
                alertDialog = progress.create()
                alertDialog.show()
                makeRequest(url)


            }
        }



        return view
    }



    private fun makeRequest(url: String) {
        val stringRequest = VolleyUTF8EncodingStringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Log.i("Response", response)
                val lista: List<TableFourResource> = FakeRequest().getTableFourData(response)
                if (lista.isNotEmpty()) {


                    //Todo: esse trecho está funcionando, porem não da melhor forma possivel
                    Log.i("recebido", lista.toString())
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


//                    val lista: MutableList<Map<String, Long>> = mutableListOf()

//                    for (element in childAux){
//                        lista.add(element.value)
//
//                    }


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


                    Log.i("recebido4", "map2: $map2")




                    generateParentTable(map, childAux, lista, countMap)
                    mAdapter = TableFourAdapter(context!!, generateData(map2))
                    recyclerView.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()



                }
                alertDialog.dismiss()


            },
            Response.ErrorListener {
                VolleyLog.e("Error: " + it.message)
                alertDialog.dismiss()

                //Todo: Tratar o caso do request falhar
            })

        // Add the request to the RequestQueue.

        stringRequest.retryPolicy = DefaultRetryPolicy(10 * 1000, 3, 1.0f)
        stringRequest.tag = this


        queue.add(stringRequest)

    }



    private fun generateData(childMap: MutableMap<String, MutableList<TableFourResource>>): MutableList<AuxResource4> {
        val lista: MutableList<AuxResource4> = mutableListOf()
        for (entry in childMap){
            lista.add(AuxResource4(entry.key, entry.value))
        }
        return lista
    }
    
    // Todo: esse trecho nao é null safe. Corrigir
    private fun generateParentTable(
        map: MutableMap<String, Long>,
        childAux: MutableMap<String, MutableMap<String, Long>>,
        lista: List<TableFourResource>,
        countMap: MutableMap<String, Int>

    ) {

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
            val dialog = CustomTableDialog()
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
            val dialog = CustomTableDialog()
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