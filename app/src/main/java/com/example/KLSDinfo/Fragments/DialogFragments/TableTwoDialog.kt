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
import com.example.KLSDinfo.Models.TableTwoResource
import com.example.KLSDinfo.R
import com.example.KLSDinfo.R.color.grey
import com.example.KLSDinfo.Requests.FakeRequest
import com.example.KLSDinfo.Volley.VolleySingleton


class TableTwoDialog : DialogFragment() {

    val TAG: String = "FullScreenDialog"
    lateinit var cardview: CardView
    lateinit var layoutparams: LinearLayout.LayoutParams
    lateinit var textview: TextView
    lateinit var table: TableLayout
    lateinit var listTables: MutableList<TableLayout>
    lateinit var linear: LinearLayout
    lateinit var map : MutableMap<String, MutableList<Person2>>
    lateinit var id: String
    lateinit var url: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.table_two_layout, container, false)

//        val recyclerView : RecyclerView = view.findViewById(R.id.table_two_rv)
//        recyclerView.layoutManager = GridLayoutManager(context,1)
//        recyclerView.setHasFixedSize(true)

        linear = view.findViewById(R.id.parent_linear_layout)


        val tool: Toolbar = view.findViewById(R.id.toolbar)


        tool.setNavigationIcon(R.drawable.ic_close_white_24dp)
        tool.setNavigationOnClickListener {
            cancelUpload()
        }

        val bundle: Bundle? = arguments

        if (bundle == null || bundle.isEmpty){
            // Todo: Não há resultados

        }else{

            val persons: List<Person2>? = bundle.getParcelableArrayList("resources")
            if (persons == null){
                // Todo: tratar isso dai
            }else{
                // A lista veio

                map = mutableMapOf()
                var ids : String = "/"
                for(element in persons){
                    ids += "${element.holder.id}/"
                    for (role in element.roles!!){
                        map[role.name] = mutableListOf()
                    }
                }
                for(element in persons){
                    for (role in element.roles!!){
                        val list: MutableList<Person2>? = map[role.name]
                        list!!.add(element)
                        map[role.name] = list
                    }
                }

                for(entry in map){
                    id = "/"
                    for(person in entry.value){
                        id += "${person.holder.id}/"
                    }
                    url = "http://smartlab.lsdi.ufma.br/service/persons/${id}physical_spaces/"
                    makeRequest(url, entry.key)

                }
            }
        }

        return view
    }


    private fun makeRequest(url: String, role: String) {
        val queue= VolleySingleton.getInstance(context).requestQueue
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Log.i("Response", response)
                val lista: List<TableTwoResource> = FakeRequest().getTableTwoData(response)

                if(lista.isNotEmpty()){
                    generateTable(lista, role)
                }




            },
            Response.ErrorListener {
                VolleyLog.e("Error: ", it.message)
            })

        // Add the request to the RequestQueue.

        stringRequest.retryPolicy = DefaultRetryPolicy(20 * 1000, 3, 1.0f)
        queue.add(stringRequest)

    }




    private fun addRow(element: TableTwoResource) {

        val row: TableRow = LayoutInflater.from(context).inflate(R.layout.table_two_item, null) as TableRow



        (row.findViewById(R.id.table_item_name) as TextView).text = element.shortName
        (row.findViewById(R.id.table_item_physical) as TextView).text = element.physical_space

        (row.findViewById(R.id.table_item_duration) as TextView).text = "${element.duration/60}"

        val view: View = View(context).also {
            it.setBackgroundColor(resources.getColor(grey))
            it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,1)

        }

        table.addView(row)
        table.addView(view)

    }

    private fun generateTable(src: List<TableTwoResource>, role: String) {

        val card: CardView = LayoutInflater.from(context).inflate(R.layout.table_two_card, null) as CardView
            (card.findViewById(R.id.parent_title) as TextView).text = role
            (card.findViewById(R.id.parent_options) as ImageButton).setOnClickListener {
                Toast.makeText(context, role,Toast.LENGTH_SHORT).show()
            }
        for(item in src){
            val table: TableLayout = card.findViewById(R.id.parent_table_layout)
            val row: TableRow = LayoutInflater.from(context).inflate(R.layout.table_two_item, null) as TableRow

            (row.findViewById(R.id.table_item_name) as TextView).text = item.shortName
            (row.findViewById(R.id.table_item_physical) as TextView).text = item.physical_space

            (row.findViewById(R.id.table_item_duration) as TextView).text = "${item.duration/60}"

            val view: View = View(context).also {
                it.setBackgroundColor(resources.getColor(grey))
                it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,1)

            }

            table.addView(row)
            table.addView(view)
        }
            linear.addView(card)


    }


    private fun initTables(map: MutableMap<String, MutableList<Person2>>, element: TableTwoResource, list: List<TableTwoResource> ) {

        map.map { entry ->

            val card: CardView = LayoutInflater.from(context).inflate(R.layout.table_two_card, null) as CardView
            (card.findViewById(R.id.parent_title) as TextView).text = entry.key
            (card.findViewById(R.id.parent_options) as ImageButton).setOnClickListener {
                Toast.makeText(context, entry.key,Toast.LENGTH_SHORT).show()
            }

            val table: TableLayout = card.findViewById(R.id.parent_table_layout)




            val row: TableRow = LayoutInflater.from(context).inflate(R.layout.table_two_item, null) as TableRow

            (row.findViewById(R.id.table_item_name) as TextView).text = element.shortName
            (row.findViewById(R.id.table_item_physical) as TextView).text = element.physical_space

            (row.findViewById(R.id.table_item_duration) as TextView).text = "${element.duration/60}"

            val view: View = View(context).also {
                it.setBackgroundColor(resources.getColor(grey))
                it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,1)

            }

            table.addView(row)
            table.addView(view)

            linear.addView(card)
        }
        Log.i("debug","Recebido: $map")


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