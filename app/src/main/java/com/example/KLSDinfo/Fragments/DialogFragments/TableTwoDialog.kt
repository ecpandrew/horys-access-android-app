package com.example.KLSDinfo.Fragments.DialogFragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.example.KLSDinfo.Adapters.TableThreeAdapter
import com.example.KLSDinfo.Adapters.TableTwoAdapter
import com.example.KLSDinfo.Models.Person2
import com.example.KLSDinfo.Models.TableTwoResource
import com.example.KLSDinfo.R
import com.example.KLSDinfo.R.color.grey
import com.example.KLSDinfo.Models.FakeRequest
import com.example.KLSDinfo.Volley.VolleySingleton
import kotlinx.android.synthetic.main.table_one_layout.view.*
import java.text.NumberFormat


class TableTwoDialog : Fragment() {

    lateinit var cardview: CardView
    lateinit var textview: TextView
    lateinit var linear: LinearLayout
    lateinit var map : MutableMap<String, MutableList<Person2>>
    lateinit var mapResults : MutableMap<String, List<TableTwoResource>>
    lateinit var listResults: MutableList<MutableMap<String, List<TableTwoResource>>>
    lateinit var url: String
    private lateinit var queue: RequestQueue
    private lateinit var parentMap: MutableMap<String,List<TableTwoResource>>
    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog

    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: TableTwoAdapter
    lateinit var dividerItemDecoration: DividerItemDecoration


    companion object {
        fun newInstance(): TableTwoDialog {
            return TableTwoDialog()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.table_two_layout, container, false)
        val linearLayoutManager = LinearLayoutManager(context)


        recyclerView = view.findViewById(R.id.rv_resource_2)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        dividerItemDecoration = DividerItemDecoration(recyclerView.context, linearLayoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)









        linear = view.findViewById(R.id.parent_linear_layout)
        parentMap = mutableMapOf()
        mapResults = mutableMapOf()
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






//                map = mutableMapOf()
//                var ids : String = "/"
//                for(element in persons){
//                    ids += "${element.holder.id}/"
//                    for (role in element.roles!!){
//                        map[role.name] = mutableListOf()
//                    }
//                }
//                url = "http://smartlab.lsdi.ufma.br/service/persons/${ids}physical_spaces/"
//
//                makeRequest(url)
//
//
////
//                for(element in persons){
//                    for (role in element.roles!!){
//                        val list: MutableList<Person2>? = map[role.name]
//                        list!!.add(element)
//                        map[role.name] = list
//                    }
//                }


                //Todo: Esta funcionando, porem não é eficiente
//                progress = AlertDialog.Builder(context)
//                progress.setCancelable(false)
//                progress.setView(R.layout.loading_dialog_layout)
//                alertDialog = progress.create()
//                alertDialog.show()
//                var id: String? = null
//                for(entry in map){
//                    id = "/"
//                    for(person in entry.value){
//                        id += "${person.holder.id}/"
//                    }
//                    url = "http://smartlab.lsdi.ufma.br/service/persons/${id}physical_spaces/"
////                    makeRequest(url, entry.key)
//
//                }
            }
        }

//        alertDialog.dismiss()

        return view
    }


    private fun makeRequest(url: String, role: String) {
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
//                Log.i("Response", response)
                val r: List<TableTwoResource> = FakeRequest().getTableTwoData(response)

                if(r.isNotEmpty()){
                    //generateTable(lista, role)
//                    Log.i("Response", "lista $lista")
                    mapResults[role] = r

                    mAdapter = TableTwoAdapter(context!!, mapResults)
                    recyclerView.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()



                    Log.i("Response", "map $mapResults")

                    // setar o adapter


                }
//


            },
            Response.ErrorListener {
                VolleyLog.e("Error: ", it.message)
            })



        // Add the request to the RequestQueue.

        stringRequest.retryPolicy = DefaultRetryPolicy(20 * 1000, 3, 1.0f)
        stringRequest.tag = this
        queue.add(stringRequest)

    }



    private fun generateTable(src: List<TableTwoResource>, role: String) {





        val card: CardView = LayoutInflater.from(context).inflate(R.layout.table_two_card, null) as CardView
        (card.findViewById(R.id.parent_title) as TextView).text = role

        (card.findViewById(R.id.parent_options) as ImageButton).setOnClickListener {
            val popup = PopupMenu(context, it.parent_options)
            popup.inflate(R.menu.menu_card)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_details -> {
                        Toast.makeText(context,"Expand Table - $role", Toast.LENGTH_LONG).show()
                    }
                    R.id.action_log -> {
                        Toast.makeText(context,"Log - $role", Toast.LENGTH_LONG).show()
                    }
                }
                false
            }
            popup.show()
        }
        for(item in src){
            val nf = NumberFormat.getInstance() // get instance
            val table: TableLayout = card.findViewById(R.id.parent_table_layout)
            val row: TableRow = LayoutInflater.from(context).inflate(R.layout.table_two_item, null) as TableRow

            (row.findViewById(R.id.table_item_name) as TextView).text = item.shortName
            (row.findViewById(R.id.table_item_physical) as TextView).text = item.physical_space
            nf.maximumFractionDigits = 2 // set decimal places

            val s = nf.format(item.duration.toFloat()/3600)

            (row.findViewById(R.id.table_item_duration) as TextView).text = "$s"

            val view: View = View(context).also {
                it.setBackgroundColor(resources.getColor(grey))
                it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,1)

            }

            table.addView(row)
            table.addView(view)
        }
            linear.addView(card)


    }


    override fun onStop() {
        super.onStop()
        queue.cancelAll(this)
        VolleyLog.e("Error: ", "Request Cancelado")

    }

}