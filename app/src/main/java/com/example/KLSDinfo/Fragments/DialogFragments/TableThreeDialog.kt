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
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.example.KLSDinfo.Models.FakeRequest
import com.example.KLSDinfo.Models.Person2
import com.example.KLSDinfo.Models.TableFourResource
import com.example.KLSDinfo.Models.TableThreeResource
import com.example.KLSDinfo.R
import com.example.KLSDinfo.Volley.VolleySingleton
import kotlinx.android.synthetic.main.table_one_layout.view.parent_options
import kotlinx.android.synthetic.main.table_three_layout.view.*
import java.lang.Exception
import java.text.NumberFormat


class TableThreeDialog : DialogFragment() {

    val TAG: String = "FullScreenDialog"
    lateinit var id: String
    lateinit var url: String
    private lateinit var dateStr: String
    private lateinit var dateStr2: String
    lateinit var linear: LinearLayout
    lateinit var parentLinear: LinearLayout
    private lateinit var queue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.table_three_layout, container, false)


        val tool: Toolbar = view.findViewById(R.id.toolbar)


        tool.setNavigationIcon(R.drawable.ic_close_white_24dp)
        tool.setNavigationOnClickListener {
            cancelUpload()
        }
        queue = VolleySingleton.getInstance(context).requestQueue






        val bundle: Bundle? = arguments

        if (bundle == null || bundle.isEmpty){
            // Todo: Não há resultados

        }else {
            //todo: tratar os unix para o caso de vir null
            val persons: List<Person2>? = bundle.getParcelableArrayList("resources")
            val unix: Long = bundle.getLong("date")
            val unixPast: Long = bundle.getLong("date2")
            Log.i("recebido", persons.toString())
            Log.i("recebido", "${bundle.getLong("date")} and ${bundle.getLong("date2")}")
            try{
                dateStr = bundle.getString("dateStr")!!
                dateStr2 = bundle.getString("dateStr2")!!
            }catch (e: Exception){}

            if (persons == null){
                // Todo: tratar isso dai
            }else{
                Log.i("recebido", persons.toString())
                Log.i("recebido", "${bundle.getLong("date")} and ${bundle.getLong("date2")}")
                id = ""
                val pessoas = mutableListOf<String>()
                for (person in persons){
                    id += "${person.holder.id}/"
                    pessoas.add(person.shortName)
                }
                Log.i("recebido", id)


                url = "http://smartlab.lsdi.ufma.br/service/persons/${id}rendezvous/${unixPast+10800}/${unix+10800}"
                // todo: Arrumar o caso do timeout
                makeRequest(url, pessoas)


            }

        }


        return view
    }



    private fun makeRequest(url: String, pessoas: MutableList<String>) {
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Log.i("Response", response)
                val lista: List<TableThreeResource> = FakeRequest().getTableThreeData(response)
                if(lista.isNotEmpty()){


                    //Todo: esse trecho está funcionando, porem não da melhor forma possivel
                    Log.i("recebido", lista.toString())

                    generateParentTable(lista)

                    generateChildCard(lista, pessoas)




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


    private fun generateParentTable(lista: List<TableThreeResource>) {

        val card: CardView = view!!.findViewById(R.id.parent_card)
        (card.findViewById(R.id.parent_title) as TextView).text = "Encontros do Grupo Selecionado"
        (card.findViewById(R.id.parent_pagination_txt) as TextView).text = "1-1 of 1"
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
        (row.findViewById(R.id.table_item_name) as TextView).text = "Nº de Encontros"
        (row.findViewById(R.id.table_item_count) as TextView).text = "Tempo Médio (h)"
        (row.findViewById(R.id.table_item_duration) as TextView).text = "Duração Total (h)"
        var view: View = View(context).also {
            it.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey))
            it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1)
        }

        table.addView(row)
        table.addView(view)

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



        row = LayoutInflater.from(context).inflate(R.layout.table_four_parent_item, null) as TableRow
        (row.findViewById(R.id.table_item_name) as TextView).text = "${lista.size}"
        (row.findViewById(R.id.table_item_count) as TextView).text = m
        (row.findViewById(R.id.table_item_duration) as TextView).text = tD
        table.addView(row)
        view = View(context).also {
            it.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey))
            it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1)
        }
        table.addView(view)

    }




    private fun generateChildCard(
        lista: List<TableThreeResource>,
        pessoas: MutableList<String>
    ) {

        val card: CardView = view!!.findViewById(R.id.parent_card2)
        (card.findViewById(R.id.parent_title2) as TextView).text = "Informações Individuais"



        (card.findViewById(R.id.parent_options2) as ImageButton).setOnClickListener {
            val popup = PopupMenu(context, it.parent_options2)
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

        val table: TableLayout = card.findViewById(R.id.parent_table_layout2)
        var row: TableRow = LayoutInflater.from(context).inflate(R.layout.table_four_parent_item, null) as TableRow
        (row.findViewById(R.id.table_item_name) as TextView).text = "Grupo Selecionado"
        (row.findViewById(R.id.table_item_count) as TextView).text = "Nº de Encontros"
        (row.findViewById(R.id.table_item_duration) as TextView).text = "Duração com o Grupo (h)"
        var view: View = View(context).also {
            it.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey))
            it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1)
        }

        table.addView(row)
        table.addView(view)



        // Compute

        val map: MutableMap<String, MutableList<TableThreeResource>> = mutableMapOf()
        for (resource in lista){
            for(person in resource.persons){
                if(!map.containsKey(person.shortName)){
                    map[person.shortName] = mutableListOf(resource)
                }else{
                    val aux : MutableList<TableThreeResource> = map[person.shortName]!!
                    aux.add(resource)
                    map[person.shortName] = aux
                }

            }
        }

        val mapDuration: MutableMap<String, Long> = mutableMapOf()
        for (element in map){

            var dur: Long = 0
            element.value.forEach {
                dur+= it.getDuration()
            }

            mapDuration[element.key] = dur

        }






        var count = 0
        for (element in mapDuration){
            if(count == 6) break
            val nf = NumberFormat.getInstance() // get instance
            nf.maximumFractionDigits = 2 // set decimal places

            row = LayoutInflater.from(context).inflate(R.layout.table_four_parent_item, null) as TableRow
            (row.findViewById(R.id.table_item_name) as TextView).text = element.key
            (row.findViewById(R.id.table_item_count) as TextView).text = "${map[element.key]!!.size}/${lista.size}"
            (row.findViewById(R.id.table_item_duration) as TextView).text = nf.format(element.value.toFloat()/3600)
            table.addView(row)
            view = View(context).also {
                it.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey))
                it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1)
            }
            table.addView(view)




        // Pagination
            (card.findViewById(R.id.parent_pagination_txt2) as TextView).text = "1-6 of ${mapDuration.size}"
            count++

            Log.i("recebido", "map: ${map.toString()}")
            Log.i("recebido", "mapDuration: ${mapDuration.toString()}")

        }

    }
    private fun cancelUpload() {
        dialog.dismiss()
        queue.cancelAll(this)

    }

    override fun onStop() {
        super.onStop()
        queue.cancelAll(this)

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