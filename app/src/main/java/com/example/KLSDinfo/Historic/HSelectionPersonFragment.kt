package com.example.KLSDinfo.Historic
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.example.KLSDinfo.UtilClasses.MultiCheckRoleAdapter
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
    private var unixTimePast: Long? = null
    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    private lateinit var queue: RequestQueue

    companion object {
        fun newInstance(): HSelectionPersonFragment {
            return HSelectionPersonFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.select_person_history_layout, container, false)
        val rv : RecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        val layoutManager = LinearLayoutManager(context)


        cardDate = view.findViewById(R.id.textView13)
        cardDate2 = view.findViewById(R.id.textView14)
        clearDate()

        queue = VolleySingleton.getInstance(context).requestQueue

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
            clearDate()
//            mAdapter.clearChoices()
        }


        val btnSend : Button = view.findViewById(R.id.btn_request)

        btnSend.setOnClickListener {


            val seletedElements: ArrayList<Parcelable> = getSelectedElements()
            val bundle = Bundle()
            var date = null
            var date2 = null

            if (unixTime == null || unixTimePast == null){
                setDefaultUnixTime()
            }else{
//                setCustomUnixTime()
            }

            bundle.putParcelableArrayList("resources", seletedElements)
            bundle.putLong("date", unixTime!!)
            bundle.putLong("date2", unixTimePast!!)
            bundle.putString("dateStr", cardDate.text.toString())
            bundle.putString("dateStr2", cardDate2.text.toString())

            when(methodRef){
                0 -> {
                    Log.i("debug","go1")
                    val dialog = TableThreeFrag()
                    dialog.arguments = bundle
                    navigateToFragment(dialog,true)
                }

                1 -> {
                    Log.i("debug","go2")

                    val dialog = TableFourDialog()
                    dialog.arguments = bundle
                    navigateToFragment(dialog,true)

                }
                else -> {
                    Log.i("debug","else")

                }
            }






        }



        val url = "http://smartlab.lsdi.ufma.br/semantic/api/persons"
        val url_roles = "http://smartlab.lsdi.ufma.br/semantic/api/roles"


        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                VolleyLog.v("Response:%n %s", response)
                val lista: MutableList<Person2> = FakeRequest().getAllPersons(response)

                Log.i("response", listOfRoles.toString())
                val mCheckRoles : MutableList<MultiCheckRole> = getMultiCheckRoles2(listOfRoles, lista)
                mAdapter = MultiCheckRoleAdapter(mCheckRoles)
                rv.layoutManager = layoutManager
                rv.adapter = mAdapter

                initCheckBoxes(mCheckRoles)
                alertDialog.dismiss()


            },
            Response.ErrorListener {
                VolleyLog.e("Error: ", it.message)
                alertDialog.dismiss()
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

            })

        // Add the request to the RequestQueue.

        stringRequest.retryPolicy = DefaultRetryPolicy(15 * 1000, 3, 1.0f)
        roleRequest.retryPolicy = DefaultRetryPolicy(15 * 1000, 3, 1.0f)
        stringRequest.tag = this
        roleRequest.tag = this

        progress = AlertDialog.Builder(context)
        progress.setCancelable(false)
        progress.setView(R.layout.loading_dialog_layout)
        alertDialog = progress.create()

        alertDialog.show()
        queue.add(roleRequest)

        return view
    }

    private fun clearDate() {
        cardDate.text = "yyyy-MM-dd HH:mm"
        cardDate2.text = "yyyy-MM-dd HH:mm"
    }

    private fun setCustomUnixTime() {


        val timeZone: TimeZone = calendar!!.timeZone
        val cals: Date = Calendar.getInstance(TimeZone.getDefault()).time
        var milis: Long = cals.time
        milis += timeZone.getOffset(milis)
        unixTime = milis/1000


        val timeZone2: TimeZone = calendar2!!.timeZone
        val cals2: Date = Calendar.getInstance(TimeZone.getDefault()).time
        var milis2: Long = cals2.time
        milis2 += timeZone2.getOffset(milis2)
        unixTimePast = milis2/1000




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


        //Todo: Verificar esse getOffset zuando o fuso horario..
        if (TAG == 0) {

            val calendar: Calendar = Calendar.getInstance()
            calendar.set(year,month,day,hour,minute)
            cardDate.text = dateString

            val timeZone: TimeZone = calendar.timeZone
            val cals: Date = calendar.time
            var milis: Long = cals.time
            milis += timeZone.getOffset(milis)
            unixTime = milis/1000


        } else {
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(year,month,day,hour,minute)
            cardDate2.text = dateString
            val timeZone: TimeZone = calendar.timeZone
            val cals: Date = calendar.time
            var milis: Long = cals.time
            milis += timeZone.getOffset(milis)
            unixTimePast = milis/1000

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

    fun navigateToFragment(fragToGo: Fragment, addToBackStack: Boolean = false){
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


}