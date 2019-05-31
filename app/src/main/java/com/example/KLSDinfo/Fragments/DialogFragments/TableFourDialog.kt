package com.example.KLSDinfo.Fragments.DialogFragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.example.KLSDinfo.Models.Person2
import com.example.KLSDinfo.R
import com.example.KLSDinfo.Models.FakeRequest
import com.example.KLSDinfo.Models.TableFourResource
import com.example.KLSDinfo.Volley.VolleySingleton
import kotlinx.android.synthetic.main.table_four_card_child.view.*
import kotlinx.android.synthetic.main.table_one_layout.view.*
import java.text.NumberFormat


class TableFourDialog : DialogFragment() {

    val TAG: String = "FullScreenDialog"
    lateinit var map : MutableMap<String, Long>
    lateinit var id: String
    lateinit var url: String
    lateinit var linear: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.table_four_layout, container, false)


        val tool: Toolbar = view.findViewById(R.id.toolbar)


        tool.setNavigationIcon(R.drawable.ic_close_white_24dp)
        tool.setNavigationOnClickListener {
            cancelUpload()
        }

        linear = view.findViewById(R.id.table_four_child_linear_Layout)

        val bundle: Bundle? = arguments

        if (bundle == null || bundle.isEmpty){
            // Todo: Não há resultados

        }else {
            val persons: List<Person2>? = bundle.getParcelableArrayList("resources")
            val unix: Long = bundle.getLong("date")
            val unixPast: Long = bundle.getLong("date2")
            if (persons == null || unix == null || unixPast == null){
                // Todo: tratar isso dai
            }else{
                Log.i("recebido", persons.toString())
                Log.i("recebido", "${bundle.getLong("date")} and ${bundle.getLong("date2")}")
                id = ""
                for (person in persons){
                    id += "${person.holder.id}/"
                }
                Log.i("recebido", id)


                map = mutableMapOf()
                for (person in persons){
                    map[person.shortName] = 0
                }

                url = "http://smartlab.lsdi.ufma.br/service/persons/${id}physical_spaces/${unixPast+10800}/${unix+10800}"
                makeRequest(url)


            }
        }




        return view
    }





    private fun makeRequest(url: String) {
        val queue= VolleySingleton.getInstance(context).requestQueue
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Log.i("Response", response)
                val lista: List<TableFourResource> = FakeRequest().getTableFourData(response)
                if(lista.isNotEmpty()){
//                    generateTable(lista, role)
                    // count places

                    Log.i("recebido", lista.toString())
                    val map : MutableMap<String, Long> = mutableMapOf()
                    val childMap: MutableMap<String, MutableList<TableFourResource>> = mutableMapOf()
                    for (resource in lista){
                        if(!map.containsKey(resource.shortName)){
                            map[resource.shortName] = resource.getDuration()
                        }else{
                            map[resource.shortName] = resource.getDuration() + map[resource.shortName]!!

                        }

                        if(!childMap.containsKey(resource.physical_space)){
                            childMap[resource.physical_space] = mutableListOf(resource)
                        }else{
                            val aux : MutableList<TableFourResource> = childMap[resource.physical_space]!!
                            aux.add(resource)
                            childMap[resource.physical_space] = aux
                        }
                    }

                    val childAux: MutableMap<String, MutableMap<String, Long>> = mutableMapOf()

                    for (entry in childMap){

                        if(!childAux.containsKey(entry.key)){
                            childAux[entry.key] = mutableMapOf()
                        }else{

                        }

                        val childDuration: MutableMap<String, Long> = mutableMapOf()
                        for (resource in entry.value){
                            if(!childDuration.containsKey(resource.shortName)){
                                childDuration[resource.shortName] = resource.getDuration()
                            }else{
                                childDuration[resource.shortName] = resource.getDuration() + childDuration[resource.shortName]!!

                            }
                        }
                       childAux[entry.key] = childDuration
                    }


                    Log.i("recebido", map.toString())

                    Log.i("recebido", childMap.toString())
                    Log.i("recebido", childAux.toString())


                    for (element in childAux){
                        generateTableChild(element.key, element.value)

                    }




                }




            },
            Response.ErrorListener {
                VolleyLog.e("Error: ", it.message)
            })

        // Add the request to the RequestQueue.

        stringRequest.retryPolicy = DefaultRetryPolicy(20 * 1000, 3, 1.0f)
        queue.add(stringRequest)

    }



    private fun generateTableParent(map: MutableMap<String, Long>,value: MutableMap<String, Long>){




    }

    private fun generateTableChild(
        title: String,
        value: MutableMap<String, Long>
    ) {

        val card: CardView = LayoutInflater.from(context).inflate(R.layout.table_four_card_child, null) as CardView
        (card.findViewById(R.id.child_title) as TextView).text = title
        (card.findViewById(R.id.child_pagination_txt) as TextView).text = "${value.size} results"

        (card.findViewById(R.id.child_options) as ImageButton).setOnClickListener {
            val popup = PopupMenu(context, it.child_options)
            popup.inflate(R.menu.menu_card)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_details -> {
                        Toast.makeText(context,"Expand Table - $title", Toast.LENGTH_LONG).show()
                    }
                    R.id.action_log -> {
                        Toast.makeText(context,"Log - $title", Toast.LENGTH_LONG).show()
                    }
                }
                false
            }
            popup.show()
        }
        for(item in value){
            val nf = NumberFormat.getInstance() // get instance
            val table: TableLayout = card.findViewById(R.id.child_table_layout)
            val row: TableRow = LayoutInflater.from(context).inflate(R.layout.table_four_child_item, null) as TableRow

            (row.findViewById(R.id.table_item_name) as TextView).text = item.key

            nf.maximumFractionDigits = 2 // set decimal places

            val s = nf.format(item.value.toFloat()/3600)

            (row.findViewById(R.id.table_item_duration) as TextView).text = s

            val view: View = View(context).also {
                it.setBackgroundColor(resources.getColor(R.color.grey))
                it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1)

            }

            table.addView(row)
            table.addView(view)
        }
        linear.addView(card)


    }


    private fun cancelUpload() {
        dialog.dismiss()
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog

        if (dialog != null){
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)

        }
    }

}