package com.example.klsdinfo.Historic.TableFragments

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.example.klsdinfo.models.FakeRequest
import com.example.klsdinfo.models.Person2
import com.example.klsdinfo.models.TableThreeResource
import com.example.klsdinfo.R
import com.example.klsdinfo.Volley.VolleySingleton
import java.text.NumberFormat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.toolbox.HttpHeaderParser
import com.example.klsdinfo.CustomTable.CustomTableDialog
import com.example.klsdinfo.Historic.adapters.TableThreeAdapter
import com.example.klsdinfo.models.AuxResource3
import com.google.android.material.button.MaterialButton
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
        recyclerView.layoutManager = GridLayoutManager(context, 2)
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
            Log.i("recebido", persons.toString())
            Log.i("recebido", "${bundle.getLong("date")} and ${bundle.getLong("date2")}")


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

        val card: CardView = view!!.findViewById(R.id.tableThreeCardView)

        (card.findViewById(R.id.btn_detail) as MaterialButton).setOnClickListener{
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

        (card.findViewById(R.id.btn_log) as MaterialButton).setOnClickListener {
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
        (card.findViewById(R.id.nplacesTV4) as TextView).text = ("Physical Spaces Found: ${mean/60}")
        (card.findViewById(R.id.durationTV4) as TextView).text = ("Total Time Elapsed: ${totalDuration/60} min")
        card.visibility = View.VISIBLE



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