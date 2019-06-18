package com.example.KLSDinfo.Historic.MainFragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.KLSDinfo.ErrorFragment
import com.example.KLSDinfo.Historic.TableFragments.TableFiveFrag
import com.example.KLSDinfo.Historic.adapters.PhysicalSpaceAdapter
import com.example.KLSDinfo.Models.FakeRequest
import com.example.KLSDinfo.Models.Location
import com.example.KLSDinfo.Models.PhysicalSpace
import com.example.KLSDinfo.R
import com.example.KLSDinfo.UtilClasses.Tools
import com.example.KLSDinfo.Volley.VolleySingleton
import com.google.android.material.button.MaterialButton
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.text.SimpleDateFormat
import java.util.*

class HSelectionLocationFragment: Fragment() , DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener{

    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: PhysicalSpaceAdapter
    private lateinit var actionModeCallback: ActionModeCallback
    private var actionMode: ActionMode? = null
    lateinit var pilha: Stack<List<PhysicalSpace>>
    lateinit var back: MaterialButton
    lateinit var get: MaterialButton




    private var unixTime: Long? = null
    private var unixTimePast: Long? = null
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

//    lateinit var dateTxt2: TextView
//    lateinit var dateTxt: TextView

    lateinit var cardDate: CardView
    lateinit var dayTv: TextView
    lateinit var monthTv: TextView
    lateinit var yearTv: TextView
    lateinit var timeTv: TextView
    lateinit var calendar: Calendar
    private lateinit var dateStr: String

    lateinit var cardDate2: CardView
    lateinit var dayTv2: TextView
    lateinit var monthTv2: TextView
    lateinit var yearTv2: TextView
    lateinit var timeTv2: TextView
    lateinit var calendar2: Calendar
    private lateinit var dateStr2: String


    companion object {
        fun newInstance(): HSelectionLocationFragment {
            return HSelectionLocationFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.select_location_history_layout, container, false)

        initComponents(view)



        initDateComponents(view)
        setDefaultTime()

        return view
    }

    private fun setDefaultTime(){
        var m: String = ""
        val calendar2 = Calendar.getInstance()
        val timeZone: TimeZone = calendar2!!.timeZone
        val cals: Date = Calendar.getInstance(TimeZone.getDefault()).time
        var milis: Long = cals.time
        milis += timeZone.getOffset(milis)

        unixTime = milis/1000
        unixTimePast = (milis/1000)-604800

        dayTv2.text = calendar2.get(Calendar.DAY_OF_MONTH).toString()
        monthTv2.text = getMonth(calendar2.get(Calendar.MONTH))//calendar.getDisplayName(Calendar.MONTH, Calendar.LONG,Locale.getDefault()).substring(0,3)//(calendar.get(Calendar.MONTH)+1).toString()
        yearTv2.text = calendar2.get(Calendar.YEAR).toString()
        // Todo: arrumar a string dos minutos

        m = if (calendar2.get(Calendar.MINUTE) < 10) "0${calendar2.get(Calendar.MINUTE)}" else "${calendar2.get(Calendar.MINUTE)}"
        val time2 = "${calendar2.get(Calendar.HOUR_OF_DAY)}:$m"
        timeTv2.text = time2

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = unixTimePast!! * 1000

        dayTv.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
        monthTv.text = getMonth(calendar.get(Calendar.MONTH))//calendar2.getDisplayName(Calendar.MONTH, Calendar.LONG,Locale.getDefault()).substring(0,3)//(calendar2.get(Calendar.MONTH)+1).toString()
        yearTv.text = calendar.get(Calendar.YEAR).toString()

        m = if (calendar.get(Calendar.MINUTE) < 10) "0${calendar.get(Calendar.MINUTE)}" else "${calendar.get(Calendar.MINUTE)}"
        val time = "${calendar.get(Calendar.HOUR_OF_DAY)}:$m"
        timeTv.text = time


    }

    private fun initDateComponents(view: View) {
        cardDate = view.findViewById(R.id.date_card_view)
        dayTv = view.findViewById(R.id.dayTV)
        monthTv = view.findViewById(R.id.monthTV)
        yearTv = view.findViewById(R.id.yearTV)
        timeTv = view.findViewById(R.id.timeTV)

        cardDate2 = view.findViewById(R.id.date_card_view2)
        dayTv2 = view.findViewById(R.id.dayTV2)
        monthTv2 = view.findViewById(R.id.monthTV2)
        yearTv2 = view.findViewById(R.id.yearTV2)
        timeTv2 = view.findViewById(R.id.timeTV2)

        cardDate.setOnClickListener {
            TAG = 0
            pickData()

        }
        cardDate2.setOnClickListener {
            TAG = 1
            pickData()
        }

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

//        cardDate = view.findViewById(R.id.date_card_view)
//        cardDate2 = view.findViewById(R.id.date_card_view2)





        get = view.findViewById(R.id.buttonGet)
        back = view.findViewById(R.id.backToParent)



//
//        dateTxt = view.findViewById(R.id.dayTV)
//        dateTxt2 = view.findViewById(R.id.data2)



        get.setOnClickListener {
            val selectedItemPositions = mAdapter.getSelectedItems()
            val selectedLocations = ArrayList<Parcelable>()

            for (i in selectedItemPositions){
                selectedLocations.add(pilha.peek()[i])
            }

            if (unixTime == null || unixTimePast == null){
                setDefaultTime()
            }else{
//                setCustomUnixTime()
            }


            val bundle = Bundle()
            Log.i("debugh", "Enviado: $selectedLocations")
            bundle.putLong("date", unixTime!!)
            bundle.putLong("date2", unixTimePast!!)
            bundle.putParcelableArrayList("resources", selectedLocations)
            val dialog = TableFiveFrag()
            Log.i("debugh","go1")
            dialog.arguments = bundle
            navigateToFragment(dialog,true)

        }



        validateBackParentButton(false)


        val obj = object: PhysicalSpaceAdapter.OnClickListener {
            override fun onItemLongClick(view: View, obj: PhysicalSpace, pos: Int) {
//                enableActionMode(pos)

            }

            override fun onCheckBoxClick(view: View, obj: PhysicalSpace, pos: Int) {
                toggleSelection(pos)
            }

            override fun onItemClick(view: View, obj: PhysicalSpace, pos: Int) {
                if (mAdapter.getSelectedItemCount() > 0) run {
//                    enableActionMode(pos)
                    Log.i("onclick", "onClick if")


                }else{
                    Toast.makeText(context,"onClick: ${obj.name}", Toast.LENGTH_LONG).show()
                    if(obj.children != null){
                        pilha.push(obj.children)
                        mAdapter.setItems(obj.children)
                        mAdapter.notifyDataSetChanged()
                        validateBackParentButton(true)


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
                Log.i("debug","Response is: request failed}")
                alertDialog.dismiss()
                navigateToFragment(ErrorFragment(), true)

            })
        stringRequest.tag = this
        // Add the request to the RequestQueue.
        progress = AlertDialog.Builder(context)
        progress.setView(R.layout.loading_dialog_layout)
        alertDialog.setCancelable(true)
        alertDialog.setOnCancelListener {
            navigateToFragment(ErrorFragment(), true)
        }
        alertDialog = progress.create()
        alertDialog.show()
        queue.add(stringRequest)
    }

    private fun validateBackParentButton(b: Boolean) {

        if(b){
            back.setTextColor(resources.getColor(R.color.colorPrimary))
            back.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorWhite))
            back.setOnClickListener {

                when(pilha.size){
                    1 -> {
                        Toast.makeText(context,"There are no parent nodes", Toast.LENGTH_LONG).show()
                        validateBackParentButton(false)

                    }
                    2 -> {
                        pilha.pop()
                        mAdapter.clearSelections()
                        mAdapter.setItems(pilha.peek())
                        mAdapter.notifyDataSetChanged()
                        validateBackParentButton(false)
                    }
                    else -> {
                        pilha.pop()
                        mAdapter.clearSelections()
                        mAdapter.setItems(pilha.peek())
                        mAdapter.notifyDataSetChanged()

                    }
                }

            }
            back.isClickable = true
        }else{
            back.setTextColor(resources.getColor(R.color.grey2))
            back.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.grey))
            back.setOnClickListener(null)
            back.isClickable = false
        }



    }


    fun navigateToFragment(fragToGo: Fragment, addToBackStack: Boolean = false){
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fragment_container, fragToGo)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if(addToBackStack){
            transaction.addToBackStack(null) // Todo: verificar o ciclo de vida dos fragmentos
        }
        transaction.commit()
    }

    private fun enableActionMode(position: Int) {
        if (actionMode == null) {
            actionMode = this@HSelectionLocationFragment.activity!!.startActionMode(actionModeCallback)
        }
//        toggleSelection(position)
    }

    private fun toggleSelection(position: Int) {
        mAdapter.toggleSelection(position)
//        val count = mAdapter.getSelectedItemCount()
//
//        if (count == 0) {
//            actionMode!!.finish()
//        } else {
//            actionMode!!.title = count.toString()
//            actionMode!!.invalidate()
//        }
    }








    // Todo: possivelmente isso não esta sendo relevante
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


        if (TAG == 1) {

            dayTv2.text = (if (day < 10) "0$day" else "$day")
            monthTv2.text = getMonth(month)//(if (month + 1 < 10) "0" + (month + 1) else "${month + 1}")
            yearTv2.text = year.toString()

            val h = (if (hour < 10) "0$hour" else "$hour")
            val m =   if (minute < 10) "0$minute" else "$minute"
            val time = "$h:$m"
            timeTv2.text = time

            unixTime = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString).time/1000




        } else {
            dayTv.text = (if (day < 10) "0$day" else "$day")
            monthTv.text = getMonth(month)// (if (month + 1 < 10) "0" + (month + 1) else "${month + 1}")
            yearTv.text = year.toString()

            val h = (if (hour < 10) "0$hour" else "$hour")
            val m =   if (minute < 10) "0$minute" else "$minute"
            val time = "$h:$m"
            timeTv.text = time

            unixTimePast = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString).time/1000



        }
    }





    override fun onCancel(dialog: DialogInterface?) {
        year = 0
        month = 0
        day = 0
        hour = 0
        minute = 0
        setDefaultTime()
    }


    private fun getMonth(m:Int): String{
        when(m){
            0 -> return "JAN"
            1 -> return "FEV"
            2 -> return "MAR"
            3 -> return "ABR"
            4 -> return "MAI"
            5 -> return "JUN"
            6 -> return "JUL"
            7 -> return "AGO"
            8 -> return "SET"
            9 -> return "OUT"
            10 -> return "NOV"
            11 -> return "DEC"
            else -> {
                return "error"
            }
        }
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

