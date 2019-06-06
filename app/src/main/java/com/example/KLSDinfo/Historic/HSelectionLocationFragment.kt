package com.example.KLSDinfo.Historic

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.KLSDinfo.UtilClasses.PhysicalSpaceAdapter
import com.example.KLSDinfo.Models.Location
import com.example.KLSDinfo.Models.PhysicalSpace
import com.example.KLSDinfo.R
import com.example.KLSDinfo.Models.FakeRequest
import com.example.KLSDinfo.UtilClasses.Tools
import com.example.KLSDinfo.Volley.VolleySingleton
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.util.*

class HSelectionLocationFragment: Fragment() , DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener{

    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: PhysicalSpaceAdapter
    private lateinit var actionModeCallback: ActionModeCallback
    private var actionMode: ActionMode? = null
    lateinit var pilha: Stack<List<PhysicalSpace>>
    lateinit var back: Button
    lateinit var get: Button


    lateinit var dateTxt: TextView
    lateinit var dateTxt2: TextView

    private var TAG: Int = 0
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var hour: Int = 0
    private var minute: Int = 0

    var listPhysicalSpaces: List<PhysicalSpace> = listOf()
    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    private lateinit var queue: RequestQueue



    companion object {
        fun newInstance(): HSelectionLocationFragment {
            return HSelectionLocationFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.select_location_history_layout, container, false)

        initComponents(view)



        return view
    }



    private fun initComponents(view: View) {


        recyclerView = view.findViewById(R.id.selectionHistoryRecyclerView)
        recyclerView.visibility = View.GONE
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        pilha = Stack()
//        pilha.push(FakeRequest().getAllPhysicalSpaces(null))
        queue = VolleySingleton.getInstance(context).requestQueue


        mAdapter = PhysicalSpaceAdapter(context!!, listPhysicalSpaces)
        recyclerView.adapter = mAdapter



        val cardDate: CardView = view.findViewById(R.id.card_view3)
        val cardDate2: CardView = view.findViewById(R.id.card_view4)
        val btnGet: Button = view.findViewById(R.id.buttonGet)
        back = view.findViewById(R.id.backToParent)
        dateTxt = view.findViewById(R.id.data1)
        dateTxt2 = view.findViewById(R.id.data2)



        btnGet.setOnClickListener {
            val selectedItemPositions = mAdapter.getSelectedItems()
            val selectedLocations = ArrayList<Parcelable>()

            for (i in selectedItemPositions){
                selectedLocations.add(pilha.peek()[i])
            }

            val bundle = Bundle()


            Log.i("debug", "Enviado: $selectedLocations")

            bundle.putParcelableArrayList("resources", selectedLocations)
            val dialog = TableFiveDialog()
            dialog.arguments = bundle
            val activity: AppCompatActivity = view.context as AppCompatActivity
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog")

        }


        cardDate.setOnClickListener {
            TAG = 0
            pickData()

        }
        cardDate2.setOnClickListener {
            TAG = 1
            pickData()
        }

        back.setOnClickListener {


            when(pilha.size){
                1 -> {Toast.makeText(context,"There are no parent nodes", Toast.LENGTH_LONG).show()}
                else -> {
                    pilha.pop()
                    mAdapter.clearSelections()
                    mAdapter.setItems(pilha.peek())
                    mAdapter.notifyDataSetChanged()

                }
            }

        }
        val obj = object: PhysicalSpaceAdapter.OnClickListener {
            override fun onItemLongClick(view: View, obj: PhysicalSpace, pos: Int) {
                enableActionMode(pos)

            }

            override fun onItemClick(view: View, obj: PhysicalSpace, pos: Int) {
                if (mAdapter.getSelectedItemCount() > 0) run {
                    enableActionMode(pos)
                    Log.i("onclick", "onClick if")


                }else{
                    Toast.makeText(context,"onClick: ${obj.name}", Toast.LENGTH_LONG).show()
                    if(obj.children != null){
                        pilha.push(obj.children)
                        mAdapter.setItems(obj.children)
                        mAdapter.notifyDataSetChanged()

                    }

                }
            }
        }


        mAdapter.setOnClickListener(obj)
        actionModeCallback = ActionModeCallback()

        val url = "http://smartlab.lsdi.ufma.br/semantic/api/physical_spaces/roots"

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Log.i("debug","Response is: $response")

                val lista: List<PhysicalSpace> = FakeRequest().getAllPhysicalSpaces(response)
                pilha.push(lista)
                mAdapter.setItems(lista)
                mAdapter.notifyDataSetChanged()
                recyclerView.visibility = View.VISIBLE
                alertDialog.dismiss()

            },
            Response.ErrorListener {
                Log.i("debug","Response is: reqeust failled}")
                alertDialog.dismiss()
            })
        stringRequest.tag = this
        // Add the request to the RequestQueue.
        progress = AlertDialog.Builder(context)
        progress.setCancelable(false)
        progress.setView(R.layout.loading_dialog_layout)
        alertDialog = progress.create()
        alertDialog.show()
        queue.add(stringRequest)
    }




    private fun enableActionMode(position: Int) {
        if (actionMode == null) {
            actionMode = this@HSelectionLocationFragment.activity!!.startActionMode(actionModeCallback)
        }
        toggleSelection(position)
    }

    private fun toggleSelection(position: Int) {
        mAdapter.toggleSelection(position)
        val count = mAdapter.getSelectedItemCount()

        if (count == 0) {
            actionMode!!.finish()
        } else {
            actionMode!!.title = count.toString()
            actionMode!!.invalidate()
        }
    }








    private inner class ActionModeCallback : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            Tools.setSystemBarColor(activity, R.color.colorDarkBlue2)// comentar isso;
            mode.menuInflater.inflate(R.menu.menu_search, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            val id = item.itemId
            if (id == R.id.action_search) {


                mode.finish()
                return true
            }
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            mAdapter!!.clearSelections()
            actionMode = null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_search_setting, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            //finish();
            Log.i("debug", "if")
        } else {
            Log.i("debug", "else")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteInboxes() {
        val selectedItemPositions = mAdapter.getSelectedItems()
        val selectedLocations = ArrayList<Location>()

        // TODO: implementar ação de escolha da seleção


    }



    fun pickData(){
        initDateTimeData()
        val cDefault = Calendar.getInstance()
        cDefault.set(year, month, day)

        val datePickerDialog = DatePickerDialog.newInstance(
            this,
            cDefault.get(Calendar.YEAR),
            cDefault.get(Calendar.MONTH),
            cDefault.get(Calendar.DAY_OF_MONTH)
        )

        val cMin = Calendar.getInstance()
        val cMax = Calendar.getInstance()

        cMin.set(cMin.get(Calendar.YEAR) - 1, 0, 1)
        cMax.set(cMax.get(Calendar.YEAR), cMax.get(Calendar.MONTH), cMax.get(Calendar.DAY_OF_MONTH))

        //Todo: arrumar os limites
        datePickerDialog.minDate = cMin // Ideal a data queo horys foi ligado
        datePickerDialog.maxDate = cMax // HOJE


        datePickerDialog.setOnCancelListener(this)
        datePickerDialog.show(fragmentManager!!, "datePickerDialog")
        datePickerDialog.setTitle("Escolher Data")
    }












    fun initDateTimeData(){
        when(year){
            0 -> {
                val c: Calendar = Calendar.getInstance()
                year = c.get(Calendar.YEAR)
                month = c.get(Calendar.MONTH)
                day = c.get(Calendar.DAY_OF_MONTH)
                hour = c.get(Calendar.HOUR_OF_DAY)
                minute = c.get(Calendar.MINUTE)
            }
        }
    }


    override fun onDateSet(view: DatePickerDialog?, year_: Int, monthOfYear: Int, dayOfMonth: Int) {
        val tDefault = Calendar.getInstance()
        tDefault.set(year, month, day, hour, minute)

        year = year_
        month = monthOfYear
        day = dayOfMonth

        val timePickerDialog = TimePickerDialog.newInstance(
            this,
            tDefault.get(Calendar.HOUR_OF_DAY),
            tDefault.get(Calendar.MINUTE),
            false
        )


        timePickerDialog.setOnCancelListener(this)
        timePickerDialog.show(fragmentManager!!, "timePickerDialog")
        timePickerDialog.title = "Escolher Horario"
        timePickerDialog.isThemeDark = true
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute_: Int, second: Int) {
        hour = hourOfDay
        minute = minute_
        val dateString = year.toString() + "-" +
                (if (month + 1 < 10) "0" + (month + 1) else month + 1) + "-" +
                (if (day < 10) "0$day" else day) +
                " " +
                (if (hour < 10) "0$hour" else hour) + ":" +
                if (minute < 10) "0$minute" else minute

        if (TAG == 0) {
            dateTxt.text = dateString

        } else {
            dateTxt2.text = dateString
        }


    }

    override fun onCancel(dialog: DialogInterface?) {
        year = 0
        month = 0
        day = 0
        hour = 0
        minute = 0
        dateTxt.text = "yyyy-MM-dd HH:mm"
        dateTxt2.text = "yyyy-MM-dd HH:mm"
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        print("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        print("onCreate")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        print("onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        print("onStart")

    }

    override fun onResume() {
        super.onResume()
        print("onResume")
    }

    override fun onPause() {
        super.onPause()
        print("onPause")
    }

    override fun onStop() {
        super.onStop()
        queue.cancelAll(this)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        print("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        print("onDestroy")
    }

    private fun print(msg: String){
        Log.d("Lifecycle", "Historic: Location Selection Fragment: $msg")
    }

}

