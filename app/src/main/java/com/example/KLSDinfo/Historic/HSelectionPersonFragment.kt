package com.example.KLSDinfo.Historic
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.KLSDinfo.Adapters.MultiCheckRoleAdapter
import com.example.KLSDinfo.Fragments.DialogFragments.*
import com.example.KLSDinfo.Models.MultiCheckRole
import com.example.KLSDinfo.Models.Person
import com.example.KLSDinfo.Models.Role
import com.example.KLSDinfo.R
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

    companion object {
        fun newInstance(): HSelectionPersonFragment {
            return HSelectionPersonFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.select_person_history_layout, container, false)

        val rv : RecyclerView = view.findViewById(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(context)

        val cardDate: CardView = view.findViewById(R.id.card_view3)
        val cardDate2: CardView = view.findViewById(R.id.card_view4)
        LL = view.findViewById(R.id.LL)

        dateTxt = view.findViewById(R.id.data1)
        dateTxt2 = view.findViewById(R.id.data2)

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




        mAdapter = MultiCheckRoleAdapter(getMultiCheckRoles())

        rv.layoutManager = layoutManager
        rv.adapter = mAdapter


        val btnClear : Button = view.findViewById(R.id.clear_button)
        btnClear.setOnClickListener {
            mAdapter.clearChoices()
        }

        val check = view.findViewById(R.id.check_first_child) as Button
        check.setOnClickListener {
            mAdapter.checkChild(true, 0, 0)
        }
        initCheckBoxes()

        val btnSend : Button = view.findViewById(R.id.btn_request)

        btnSend.setOnClickListener {


            val seletedElements: MutableList<Parcelable> = getSelectedElements()

            val bundle = Bundle()

            bundle.putParcelableArray("resources", seletedElements.toTypedArray())



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

        return view
    }

    private fun getSelectedElements(): MutableList<Parcelable> {

        val persons: MutableList<Parcelable> = mutableListOf()
        val roles: List<MultiCheckRole> = mAdapter.groups as List<MultiCheckRole>

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



    fun getMultiCheckRoles() : MutableList<MultiCheckRole>{



        val professors: MutableList<Person> = mutableListOf()
        professors.add(Person("Francisco Silva", true))
        professors.add(Person("Alex Barradas", true))
        professors.add(Person("Davi", true))

        val graduacao: MutableList<Person> = mutableListOf()
        graduacao.add(Person("André Luiz", false))
        graduacao.add(Person("Alysson Cirilo", true))
        graduacao.add(Person("Daniel CP", false))

        val master: MutableList<Person> = mutableListOf()
        master.add(Person("Aluno Mestrado 1", false))
        master.add(Person("Aluno Mestrado 2", true))
        master.add(Person("Aluno Mestrado 3", true))



        val professor = MultiCheckRole("Professores",professors, R.mipmap.ic_prof)
        val student = MultiCheckRole("Alunos de Graduação", graduacao, R.mipmap.ic_aluno)
        val masters = MultiCheckRole("Alunos de Mestrado", master, R.mipmap.ic_master)

        val roles: MutableList<MultiCheckRole> = mutableListOf()
        roles.add(professor)
        roles.add(masters)
        roles.add(student)
        items = roles
        return roles
    }



    private fun initCheckBoxes() {
        val roles: MutableList<MultiCheckRole> = getMultiCheckRoles()
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