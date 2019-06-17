package com.example.KLSDinfo.RealTime.TableFragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.example.KLSDinfo.Historic.TableFragments.TableFiveFrag
import com.example.KLSDinfo.Models.*
import com.example.KLSDinfo.R
import com.example.KLSDinfo.RealTime.adapters.TableOneAdapter
import com.example.KLSDinfo.Volley.VolleySingleton


class TableOnefrag : Fragment() {



    lateinit var toggleSort: ImageButton
    lateinit var btnOptions: ImageButton
    lateinit var childLinearLayout: LinearLayout
    lateinit var url: String
    var SORT: String = "UP"
    var list_of_items = arrayOf("Details", "LOG")
    val TAG: String = "FullScreenDialog"
    private lateinit var queue: RequestQueue
    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog

    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: TableOneAdapter

    companion object {
        fun newInstance(): TableOnefrag {
            return TableOnefrag()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.table_one_layout, container, false)
        val linearLayoutManager = LinearLayoutManager(context)

        queue = VolleySingleton.getInstance(context).requestQueue
        recyclerView = view.findViewById(R.id.rv_resource_1)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)


        val bundle: Bundle? = arguments
        if (bundle == null || bundle.isEmpty){
            // Todo: Não há resultados
        }else{
            val listOfPhysicalSpace: List<PhysicalSpace>? = bundle.getParcelableArrayList("resources")
            if (listOfPhysicalSpace == null){
                // Todo: tratar isso dai
            }else{

                var id = ""
                for(place in listOfPhysicalSpace){
                    id += "${place.holder.id}/"
                }

                url = "http://smartlab.lsdi.ufma.br/service/physical_spaces/${id}persons"
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
        val stringRequest = TableFiveFrag.VolleyUTF8EncodingStringRequest(
            Request.Method.GET,
            url,
            Response.Listener { response ->
                // Display the first 500 characters of the response string.

                Log.i("recebido1", response.toString())
                val lista: List<TableOneResource> = FakeRequest().getTableOneData(response)
                if (lista.isNotEmpty()) {



                    val childMap: MutableMap<String, MutableList<TableOneResource>> = mutableMapOf()




                    //Todo: esse trecho está funcionando, porem não da melhor forma possivel

                    for (resource in lista) {
                        if (!childMap.containsKey(resource.physical_space)) {
                            childMap[resource.physical_space] = mutableListOf(resource)
                        } else {
                            val aux: MutableList<TableOneResource>? = childMap[resource.physical_space]
                            aux?.add(resource)
                            childMap[resource.physical_space] = aux!!
                        }
                    }



                    mAdapter = TableOneAdapter(context!!, childMap)
                    recyclerView.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()



                    Log.i("recebido1", lista.toString())
                    Log.i("recebido1", childMap.toString())


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



    private fun generateData(childMap: MutableMap<String, MutableList<TableOneResource>>): MutableList<AuxResource1> {
        val lista: MutableList<AuxResource1> = mutableListOf()
        for (entry in childMap){
            lista.add(AuxResource1(entry.key, entry.value))
        }
        return lista
    }
    override fun onStop() {
        super.onStop()
        queue.cancelAll(this)
        VolleyLog.e("Error: ", "Request Cancelado")

    }



}
