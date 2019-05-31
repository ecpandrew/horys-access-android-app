package com.example.KLSDinfo.Historic
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.example.KLSDinfo.Adapters.MultiCheckRoleAdapter
import com.example.KLSDinfo.Fragments.DialogFragments.*
import com.example.KLSDinfo.Models.*
import com.example.KLSDinfo.R
import com.example.KLSDinfo.Models.FakeRequest
import com.example.KLSDinfo.Volley.VolleySingleton
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
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
    lateinit var cardDate: TextView
    lateinit var cardDate2: TextView
    private var calendar: Calendar? = null
    private var calendar2: Calendar? = null
    private var unixTime: Long? = null
    private var unixOneWeekAgo: Long? = null

    companion object {
        fun newInstance(): HSelectionPersonFragment {
            return HSelectionPersonFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.select_person_history_layout, container, false)

        val rv : RecyclerView = view.findViewById(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(context)


        cardDate = view.findViewById(R.id.textView13)
        cardDate2 = view.findViewById(R.id.textView14)
        LL = view.findViewById(R.id.LL)


        // Todo: tratar esse -> !!
        val methodRef : Int? = arguments?.getInt("ref")

        cardDate.setOnClickListener {
            TAG = 0
            pickData()

        }
        cardDate2.setOnClickListener {
            TAG = 1
            pickData()
        }


        mAdapter = MultiCheckRoleAdapter(mutableListOf())

        rv.layoutManager = layoutManager
        rv.adapter = mAdapter


        val btnClear : Button = view.findViewById(R.id.clear_button)

        btnClear.setOnClickListener {
            Toast.makeText(context,"TODO", Toast.LENGTH_SHORT).show()
            mAdapter.clearChoices()
        }


        val btnSend : Button = view.findViewById(R.id.btn_request)

        btnSend.setOnClickListener {


            val seletedElements: ArrayList<Parcelable> = getSelectedElements()
            val bundle = Bundle()
            var date = null
            var date2 = null

            if (calendar == null || calendar2 == null){
                setDefaultUnixTime()
            }else{
                setCustomUnixTime()
            }

            bundle.putParcelableArrayList("resources", seletedElements)
            bundle.putLong("date", unixTime!!)
            bundle.putLong("date2", unixOneWeekAgo!!)

            when(methodRef){
                0 -> {
                    Log.i("debug","go1")
                    val dialog = TableThreeDialog()
                    dialog.arguments = bundle
                    val activity: AppCompatActivity = view.context as AppCompatActivity
                    val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                    dialog.show(transaction, "FullScreenDialog")
                }

                1 -> {
                    Log.i("debug","go2")

                    val dialog = TableFourDialog()
                    dialog.arguments = bundle
                    val activity: AppCompatActivity = view.context as AppCompatActivity
                    val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                    dialog.show(transaction, "FullScreenDialog")
                }
                else -> {
                    Log.i("debug","else")

                }
            }






        }









        val queue= VolleySingleton.getInstance(context).requestQueue
        val url = "http://smartlab.lsdi.ufma.br/semantic/api/persons"
        val url_roles = "http://smartlab.lsdi.ufma.br/semantic/api/roles"


        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                VolleyLog.v("Response:%n %s", response)
                val lista: MutableList<Person2> = FakeRequest().getAllPersons(response)

                val mCheckRoles : MutableList<MultiCheckRole> = getMultiCheckRoles2(listOfRoles, lista)
                mAdapter = MultiCheckRoleAdapter(mCheckRoles)
                rv.layoutManager = layoutManager
                rv.adapter = mAdapter

                initCheckBoxes(mCheckRoles)



            },
            Response.ErrorListener {
                VolleyLog.e("Error: ", it.message)
            })

        // Add the request to the RequestQueue.

        stringRequest.retryPolicy = DefaultRetryPolicy(20 * 1000, 3, 1.0f)



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
            })

        // Add the request to the RequestQueue.

        stringRequest.retryPolicy = DefaultRetryPolicy(20 * 1000, 3, 1.0f)


        queue.add(roleRequest)


        return view
    }

    private fun setCustomUnixTime() {
        var timeZone: TimeZone
        var cals: Date
        var milis: Long

        timeZone = calendar!!.timeZone
        cals = Calendar.getInstance(TimeZone.getDefault()).time
        milis = cals.time
        milis += timeZone.getOffset(milis)
        unixTime = milis/1000


        timeZone = calendar2!!.timeZone
        cals = Calendar.getInstance(TimeZone.getDefault()).time
        milis = cals.time
        milis += timeZone.getOffset(milis)
        unixOneWeekAgo = milis/1000




    }

    private fun setDefaultUnixTime() {

        val calendar = Calendar.getInstance()
        val timeZone: TimeZone = calendar!!.timeZone
        val cals: Date = Calendar.getInstance(TimeZone.getDefault()).time
        var milis: Long = cals.time

        milis += timeZone.getOffset(milis)

        unixTime = milis/1000
        unixOneWeekAgo = (milis/1000)-604800


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

    private fun initCheckBoxes(roles: MutableList<MultiCheckRole>) {
        for (i in 0 until roles.size) {
            val ch = CheckBox(context)
            ch.text = roles[i].name

            ch.setOnClickListener {

                when (ch.isChecked) {
                    true -> {
                        selectAllOfGroup(true, i, roles[i].persons.size)

                    }
                    false -> {
                        selectAllOfGroup(false, i, roles[i].persons.size)

                    }
                }
            }

            LL.addView(ch)
        }
    }

    private fun selectAllOfGroup(status: Boolean, group: Int, child: Int) {

        for (i in 0 until child)
            mAdapter.checkChild(status,group, i)


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


        calendar = Calendar.getInstance()
        calendar = Calendar.getInstance()
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
            cardDate.text = dateString
            calendar!!.set(year,month,day,hour,minute)

        } else {
            cardDate2.text = dateString
            calendar2!!.set(year,month,day,hour,minute)
        }

    }

    override fun onCancel(dialog: DialogInterface?) {
        year = 0
        month = 0
        day = 0
        hour = 0
        minute = 0
        cardDate.text = "yyyy-MM-dd HH:mm"
        cardDate2.text = "yyyy-MM-dd HH:mm"
        calendar = null
        calendar2 = null
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
        print("onStop")
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


}