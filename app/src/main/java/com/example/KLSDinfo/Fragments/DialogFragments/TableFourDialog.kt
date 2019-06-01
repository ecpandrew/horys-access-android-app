package com.example.KLSDinfo.Fragments.DialogFragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.example.KLSDinfo.Models.Person2
import com.example.KLSDinfo.R
import com.example.KLSDinfo.Models.FakeRequest
import com.example.KLSDinfo.Models.TableFourResource
import com.example.KLSDinfo.Volley.VolleySingleton
import kotlinx.android.synthetic.main.table_four_card_child.view.*
import kotlinx.android.synthetic.main.table_one_layout.view.*
import java.lang.Exception
import java.text.NumberFormat


class TableFourDialog : DialogFragment() {

    private lateinit var dateStr: String
    private lateinit var dateStr2: String
    val TAG: String = "FullScreenDialog"
    lateinit var map : MutableMap<String, Long>
    lateinit var id: String
    lateinit var url: String
    lateinit var linear: LinearLayout
    lateinit var parentLinear: LinearLayout
    private lateinit var queue: RequestQueue

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
        queue = VolleySingleton.getInstance(context).requestQueue


        linear = view.findViewById(R.id.table_four_child_linear_Layout)
        parentLinear = view.findViewById(R.id.table_four_parent_linear_Layout)


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
                makeRequest(url)


            }
        }




        return view
    }





    private fun makeRequest(url: String) {
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Log.i("Response", response)
                val lista: List<TableFourResource> = FakeRequest().getTableFourData(response)
                if(lista.isNotEmpty()){


                    //Todo: esse trecho está funcionando, porem não da melhor forma possivel
                    Log.i("recebido", lista.toString())
                    val map : MutableMap<String, Long> = mutableMapOf()
                    val childMap: MutableMap<String, MutableList<TableFourResource>> = mutableMapOf()
                    val childAux: MutableMap<String, MutableMap<String, Long>> = mutableMapOf()

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

                    Log.i("recebido", "map $map")
                    Log.i("recebido", "child map$childMap")
                    Log.i("recebido", "aux map$childAux")
                    Log.i("recebido", "count map$countMap")



                    generateParentTable(map, countMap)


//                    generateParentTable(map)

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
        stringRequest.tag = this
        queue.add(stringRequest)

    }

    private fun generateParentTable(map: MutableMap<String, Long>, countMap: MutableMap<String, Int>) {

        val card: CardView = LayoutInflater.from(context).inflate(R.layout.table_four_card_parent, null) as CardView
        card.elevation = 2f
//        card.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.grey))
        (card.findViewById(R.id.parent_title) as TextView).text = "General Info"
        (card.findViewById(R.id.parent_pagination_txt) as TextView).text = "3 results of ${map.size}"


        if(dateStr == "yyyy-MM-dd HH:mm" || dateStr2 == "yyyy-MM-dd HH:mm"){
            (card.findViewById(R.id.parent_footer) as TextView).text = "Last Week"
            (card.findViewById(R.id.parent_footer2) as TextView).visibility = View.GONE

        }else{
            (card.findViewById(R.id.parent_footer) as TextView).text = dateStr
            (card.findViewById(R.id.parent_footer2) as TextView).text = dateStr2

        }

        (card.findViewById(R.id.parent_options) as ImageButton).setOnClickListener {
            val popup = PopupMenu(context, it.parent_options)
            popup.inflate(R.menu.menu_card)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_details -> {
                        Toast.makeText(context,"Expand Table - General Info", Toast.LENGTH_LONG).show()
                    }
                    R.id.action_log -> {
                        Toast.makeText(context,"Log - General Info", Toast.LENGTH_LONG).show()
                    }
                }
                false
            }
            popup.show()
        }
        val table: TableLayout = card.findViewById(R.id.parent_table_layout)
        var row: TableRow = LayoutInflater.from(context).inflate(R.layout.table_four_parent_item, null) as TableRow
        (row.findViewById(R.id.table_item_name) as TextView).text = "Person"
        (row.findViewById(R.id.table_item_count) as TextView).text = "Nº of Places"
        (row.findViewById(R.id.table_item_duration) as TextView).text = "Time Elapsed (h)"
        var view: View = View(context).also {
            it.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey))
            it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1)
        }

        table.addView(row)
        table.addView(view)

        var count = 0
        for(item in map){
            if(count==3) break
            val nf = NumberFormat.getInstance() // get instance
            row = LayoutInflater.from(context).inflate(R.layout.table_four_parent_item, null) as TableRow

            (row.findViewById(R.id.table_item_name) as TextView).text = item.key
            (row.findViewById(R.id.table_item_count) as TextView).text = countMap[item.key].toString()

            nf.maximumFractionDigits = 2 // set decimal places

            val s = nf.format(item.value.toFloat()/3600)

            (row.findViewById(R.id.table_item_duration) as TextView).text = s

            var view: View = View(context).also {
                it.setBackgroundColor(resources.getColor(R.color.grey))
                it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1)
            }
            table.addView(row)
            table.addView(view)
            count++
        }
        parentLinear.addView(card)



    }


    private fun generateTableChild(
        title: String,
        value: MutableMap<String, Long>
    ) {

        val card: CardView = LayoutInflater.from(context).inflate(R.layout.table_four_card_child, null) as CardView
//        card.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.grey))


        (card.findViewById(R.id.child_title) as TextView).text = title
        (card.findViewById(R.id.child_pagination_txt) as TextView).text = "${value.size} results of ${value.size} "

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
                it.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey))
                it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2)

            }

            table.addView(row)
            table.addView(view)
        }
        val view: View = View(context).also {
            it.setBackgroundColor(ContextCompat.getColor(context!!, R.color.transparent))
            it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,6)

        }
        linear.addView(view)
        linear.addView(card)


    }


    private fun cancelUpload() {
        dialog.dismiss()
        queue.cancelAll(this)
        VolleyLog.e("Error: ", "Request Cancelado")

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

    override fun onStop() {
        super.onStop()
        queue.cancelAll(this)
        VolleyLog.e("Error: ", "Request Cancelado")

    }

}