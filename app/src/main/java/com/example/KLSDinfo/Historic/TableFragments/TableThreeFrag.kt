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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.example.KLSDinfo.Models.FakeRequest
import com.example.KLSDinfo.Models.Person2
import com.example.KLSDinfo.Models.TableThreeResource
import com.example.KLSDinfo.R
import com.example.KLSDinfo.Volley.VolleySingleton
import kotlinx.android.synthetic.main.table_one_layout.view.parent_options
import java.lang.Exception
import java.text.NumberFormat
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.volley.toolbox.HttpHeaderParser
import com.example.KLSDinfo.CustomTable.CustomTableDialog
import com.example.KLSDinfo.Historic.adapters.TableThreeAdapter
import com.example.KLSDinfo.Models.AuxResource3
import java.io.UnsupportedEncodingException
import java.util.ArrayList


class TableThreeFrag : Fragment() {

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

    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: TableThreeAdapter
    lateinit var dividerItemDecoration: DividerItemDecoration


    companion object {
        fun newInstance(): TableThreeFrag {
            return TableThreeFrag()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.table_three_layout, container, false)
        val linearLayoutManager = LinearLayoutManager(context)

        recyclerView = view.findViewById(R.id.rv_resource_3)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        dividerItemDecoration = DividerItemDecoration(recyclerView.context, linearLayoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)


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
                val lista: List<TableThreeResource> = FakeRequest().getTableThreeData(response)
                if (lista.isNotEmpty()) {


                    //Todo: esse trecho está funcionando, porem não da melhor forma possivel
                    Log.i("recebido", lista.toString())

                    generateParentTable(lista)



                    mAdapter = TableThreeAdapter(context!!, generateData(lista))
                    recyclerView.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()
                    //generateChildCard(lista, pessoas)


                }
                alertDialog.dismiss()
            },
            Response.ErrorListener {
                VolleyLog.e("Error: tratar o request que falhou ")
                alertDialog.dismiss()
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
                        Toast.makeText(context,"Not Implemented Yet",Toast.LENGTH_SHORT).show()
                        val bundle = Bundle()
                        var ref ="detail3"
                        bundle.putString("ref", ref)
                        bundle.putParcelableArrayList("resources", lista as ArrayList<out Parcelable>) // ??
                        val dialog = CustomTableDialog()
                        dialog.arguments = bundle
                        val activity: AppCompatActivity = context as AppCompatActivity // ??
                        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                        dialog.show(transaction, "FullScreenDialog")

                    }
                    R.id.action_log -> {
                        val bundle = Bundle()
                        var ref ="log3"
                        bundle.putString("ref", ref)
                        bundle.putParcelableArrayList("resources", lista as ArrayList<out Parcelable>) // ??
                        val dialog = CustomTableDialog()
                        dialog.arguments = bundle
                        val activity: AppCompatActivity = context as AppCompatActivity // ??
                        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                        dialog.show(transaction, "FullScreenDialog")

                    }
                }
                false
            }
            popup.show()
        }

        val table: TableLayout = card.findViewById(R.id.parent_table_layout)
        var row: TableRow = LayoutInflater.from(context).inflate(R.layout.table_four_parent_item, null) as TableRow
        (row.findViewById(R.id.table_item_name) as TextView).text = "Nº Total de Encontros"
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



    private fun generateData(lista: List<TableThreeResource>
                             ): MutableList<AuxResource3>{

        val aux: MutableList<AuxResource3> = mutableListOf()
        val map: MutableMap<String, MutableList<TableThreeResource>> = mutableMapOf()
        for (resource in lista){
            for(person in resource.persons){
                if(!map.containsKey(person.shortName)){
                    map[person.shortName] = mutableListOf(resource)
                }else{
                    val auxi = map[person.shortName]!!
                    auxi.add(resource)
                    map[person.shortName] = auxi
                }
            }
        }

        for (entry in map){
            aux.add(AuxResource3(entry.key, entry.value))
        }
        return aux
    }


    override fun onStop() {
        super.onStop()
        queue.cancelAll(this)

    }


    class VolleyUTF8EncodingStringRequest(
        method: Int, url: String, private val mListener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ) : Request<String>(method, url, errorListener) {

        override fun deliverResponse(response: String) {
            mListener.onResponse(response)
        }

        override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
            var parsed = ""

            val encoding = charset(HttpHeaderParser.parseCharset(response.headers))

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