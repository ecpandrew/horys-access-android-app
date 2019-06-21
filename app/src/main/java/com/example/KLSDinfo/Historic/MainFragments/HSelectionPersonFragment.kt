package com.example.KLSDinfo.Historic.MainFragments
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.util.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.example.KLSDinfo.ErrorFragment
import com.example.KLSDinfo.Historic.TableFragments.TableFourFrag
import com.example.KLSDinfo.Historic.TableFragments.TableThreeFrag
import com.example.KLSDinfo.UtilClasses.MultiCheckRoleAdapter
import com.example.KLSDinfo.Models.*
import com.example.KLSDinfo.R
import com.example.KLSDinfo.Models.FakeRequest
import com.example.KLSDinfo.Volley.VolleySingleton
import com.google.android.material.button.MaterialButton
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.text.SimpleDateFormat
import java.util.*




open class HSelectionPersonFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener  {


    lateinit var mAdapter: MultiCheckRoleAdapter
    lateinit var items: MutableList<MultiCheckRole>
    lateinit var dateTxt: TextView
    lateinit var dateTxt2: TextView
    lateinit var  LL : LinearLayout

    private var TAG: Int = 0
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var hour: Int = 0
    private var minute: Int = 0
    lateinit var listOfPersons: List<Person2>
    lateinit var listOfRoles: List<Role2>
    private var calendar: Calendar? = null
    private var calendar2: Calendar? = null
    private var unixTime: Long? = null
    private var unixTimePast: Long? = null
    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    private lateinit var queue: RequestQueue
    lateinit var mCheckRoles : MutableList<MultiCheckRole>

    lateinit var cardDate: CardView
    lateinit var dayTv: TextView
    lateinit var monthTv: TextView
    lateinit var yearTv: TextView
    lateinit var timeTv: TextView
    lateinit var dateStr: String
    lateinit var dateStr2: String


    lateinit var cardDate2: CardView
    lateinit var dayTv2: TextView
    lateinit var monthTv2: TextView
    lateinit var yearTv2: TextView
    lateinit var timeTv2: TextView


    lateinit var clear: MaterialButton
    lateinit var get: MaterialButton
    lateinit var rv: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    companion object {
        fun newInstance(): HSelectionPersonFragment {
            return HSelectionPersonFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.select_person_history_layout, container, false)
        initComponents(view)
        initDateComponents(view)
        setDefaultTime()
        return view
    }

    private fun initComponents(view: View) {
        rv = view.findViewById(R.id.recycler_view) as RecyclerView
        layoutManager = LinearLayoutManager(context)
        mAdapter = MultiCheckRoleAdapter(mutableListOf())
        rv.layoutManager = layoutManager
        rv.adapter = mAdapter
        get = view.findViewById(R.id.buttonGet)
        clear = view.findViewById(R.id.buttonClear)
        get.setOnClickListener {
            val seletedElements: ArrayList<Parcelable> = getSelectedElements()
            val bundle = Bundle()
            var date = null
            var date2 = null

            if (unixTime == null || unixTimePast == null){
                setDefaultUnixTime()
            }else{
//                setCustomUnixTime()
            }

            if(arguments == null){
                // Todo: Do nothing
            }else{
                val methodRef : Int? = arguments?.getInt("ref")
                if(methodRef == null){
                    // Todo: do nothing
                }else{
                    bundle.putParcelableArrayList("resources", seletedElements)
                    bundle.putLong("date", unixTime!!)
                    bundle.putLong("date2", unixTimePast!!)
                    when(methodRef){
                        0 -> {
                            Log.i("debug","go1")
                            val dialog = TableThreeFrag()
                            dialog.arguments = bundle
                            navigateToFragment(dialog,true)
                        }
                        1 -> {
                            Log.i("debug","go2")
                            val dialog = TableFourFrag()
                            dialog.arguments = bundle
                            navigateToFragment(dialog,true)
                        }
                        else -> {
                            Log.i("debug","else")
                        }
                    }
                }
            }

        }
        clear.setOnClickListener {
            setDefaultTime()
        }
        queue = VolleySingleton.getInstance(context).requestQueue
        val url = "http://smartlab.lsdi.ufma.br/semantic/api/persons"
        val url_roles = "http://smartlab.lsdi.ufma.br/semantic/api/roles"
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                VolleyLog.v("Response:%n %s", response)
                val lista: MutableList<Person2> = FakeRequest().getAllPersons(response)
                mCheckRoles = getMultiCheckRoles2(listOfRoles, lista)
                mAdapter = MultiCheckRoleAdapter(mCheckRoles)
                rv.layoutManager = layoutManager
                rv.setHasFixedSize(true)
                rv.adapter = mAdapter
                val obj = object: MultiCheckRoleAdapter.OnClickListener{
                    override fun onClick(view: View, group: ExpandableGroup<*> , pos: Int) {
                        mAdapter.toggleSelection(pos)
                        mAdapter.getSelectedItems()
                        val status: Boolean = (view as CheckBox).isChecked
                        for(i in 0 until mCheckRoles.size){
                            if(mCheckRoles[i].name == group.title){

                                run {
                                    for(j in 0 until group.itemCount){
                                        mAdapter.checkChild(status, i, j)
                                    }
                                }
                            }
                        }
                    }
                }
                mAdapter.setCheckBoxOnClickListener(obj)
                alertDialog.dismiss()
            },
            Response.ErrorListener {
                VolleyLog.e("Error: ", it.message)
                alertDialog.dismiss()
                navigateToFragment(ErrorFragment(), true)

            })
        // Add the request to the RequestQueue.
        val roleRequest = StringRequest(
            Request.Method.GET,
            url_roles,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                VolleyLog.v("Response:%n %s", response)
                listOfRoles = FakeRequest().getAllRoles(response)
                queue.add(stringRequest)
            },
            Response.ErrorListener {
                VolleyLog.e("Error: ", it.message)
                alertDialog.dismiss()
                navigateToFragment(ErrorFragment(), true)

            })
        // Add the request to the RequestQueue.

        stringRequest.retryPolicy = DefaultRetryPolicy(15 * 1000, 3, 1.0f)
        roleRequest.retryPolicy = DefaultRetryPolicy(15 * 1000, 3, 1.0f)
        stringRequest.tag = this
        roleRequest.tag = this
        progress = AlertDialog.Builder(context)
        progress.setView(R.layout.loading_dialog_layout)
        alertDialog = progress.create()
        alertDialog.setCancelable(true)
        alertDialog.setOnCancelListener {
            navigateToFragment(ErrorFragment(), true)
        }
        alertDialog.show()
        queue.add(roleRequest)
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



    private fun clearDate() {
//        cardDate.text = "yyyy-MM-dd HH:mm"
//        cardDate2.text = "yyyy-MM-dd HH:mm"
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




    private fun setDefaultUnixTime() {

        val calendar = Calendar.getInstance()
        val timeZone: TimeZone = calendar!!.timeZone
        val cals: Date = Calendar.getInstance(TimeZone.getDefault()).time
        var milis: Long = cals.time

        milis += timeZone.getOffset(milis)

        unixTime = milis/1000
        unixTimePast = (milis/1000)-604800


    }

    private fun getSelectedElements(): ArrayList<Parcelable> {

        val persons: ArrayList<Parcelable> = ArrayList()
        val roles: ArrayList<MultiCheckRole> = mAdapter.groups as ArrayList<MultiCheckRole>

        for(i in 0 until roles.size){
            for (j in 0 until roles[i].selectedChildren.size){
                when(roles[i].selectedChildren[j]){
                    true -> { persons.add(items[i].persons[j])

                    }
                }
            }

        }
        Log.i("debug", "Enviado: $persons")
        return persons

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mAdapter.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        mAdapter.onRestoreInstanceState(savedInstanceState)

    }
    private fun getMultiCheckRoles2(listRoles: List<Role2>,lista: MutableList<Person2>): MutableList<MultiCheckRole> {
        val map : MutableMap<String,MutableList<Person2>> = mutableMapOf()
        listRoles.map {
            map[it.name] = mutableListOf()
        }
        for (person in lista) {
            for (role in person.roles!!) {
                val list = map[role.name]
                list!!.add(person)
                map[role.name] = list
            }
        }


        val multiRoles: MutableList<MultiCheckRole> = mutableListOf()




        map.map {
            multiRoles.add(MultiCheckRole(it.key, it.value, R.mipmap.ic_aluno))
        }

        items = multiRoles.filterNot {
            it.persons.isEmpty()
        } as MutableList<MultiCheckRole>

        return items



    }



    private fun selectAllOfGroup(
        group: ExpandableGroup<*>,
        boolArray: SparseBooleanArray
    ) {

        for(i in 0 until boolArray.size){

            if(boolArray[i]){
                mAdapter.checkChild(true, i, group.itemCount)
            }else{
                mAdapter.checkChild(true, i, group.itemCount)
            }
        }


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
        clearDate()
        calendar = null
        calendar2 = null
    }
    private fun navigateToFragment(fragToGo: Fragment, addToBackStack: Boolean = false){
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fragment_container, fragToGo)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if(addToBackStack){
            transaction.addToBackStack(null) // Todo: verificar o ciclo de vida dos fragmentos
        }
        transaction.commit()
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
        Log.d("Lifecycle", "Historic: Person Selection Fragment: $msg")
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

}