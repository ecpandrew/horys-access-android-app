package com.example.KLSDinfo.CustomTable

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.evrencoskun.tableview.TableView
import com.example.KLSDinfo.Models.*
import com.example.KLSDinfo.R
import kotlinx.android.synthetic.main.app_bar_main.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList


class CustomTableDialog : DialogFragment() {

    val TAG: String = "FullScreenDialog"
    lateinit var tableView: TableView
    lateinit var adapter: MyTableViewAdapter
    lateinit var tool: Toolbar


    override fun getTheme(): Int {
        return R.style.FullScreenDialogStyle
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.table_three_dialog_layout, container, false)
        tool = view.findViewById(R.id.toolbar)



        tool.setNavigationIcon(R.drawable.ic_close_white_24dp)
        tool.setNavigationOnClickListener {
            cancelUpload()
        }


        val lista: ArrayList<Parcelable>? = arguments?.getParcelableArrayList<Parcelable>("resources")
        val ref: String? = arguments?.getString("ref")
        val title: String? = arguments?.getString("person")

        Log.i("debug", "custom table: $lista" )

        when{
            lista == null ->{
            }

            lista.isEmpty() -> {

            }

            else -> {

                when (ref) {
                    "log3" -> generateLogTable3(lista, view, title)
                    "detail3" -> Toast.makeText(context,"Not Implemented Yet", Toast.LENGTH_SHORT).show()


                    "log4","child_log4" -> generateLogTable4(lista, view, title)
                    "detail4" -> generateDetailTable4(lista, view, title)
                    "child_detail4" -> generateChildDetailTable4(lista, view, title)


                    "log5", "child_log5" -> generateLogTable5(lista,view,title)
                    "detail5" -> generateDetailTable5(lista, view, title)
                    "child_detail5" -> generateChildDetailTable5(lista, view, title)


                }
            }
        }


        return view
    }


    private fun generateDetailTable3(lista: ArrayList<Parcelable>, view: View, title: String?){


        val mRowHeaderList: MutableList<RowHeader> = mutableListOf()
        val mColumnHeaderList: MutableList<ColumnHeader> = mutableListOf()
        mColumnHeaderList.add(ColumnHeader("0", "Persons"))
        mColumnHeaderList.add(ColumnHeader("1", "Nº of Rendezvous"))
        mColumnHeaderList.add(ColumnHeader("2", "Total Duration (h)"))
        mColumnHeaderList.add(ColumnHeader("3", "Total Duration (min)"))
        mColumnHeaderList.add(ColumnHeader("4", "Total Duration (s)"))

        val mCellList: MutableList<List<Cell>> = mutableListOf()
        var id = 0





        val countMap: MutableMap<String,Long> = mutableMapOf()
        val durationMap: MutableMap<String,Long> = mutableMapOf()

        for (element in lista){
            val resource : TableThreeResource? = element as? TableThreeResource
            if(resource != null){

                if(!countMap.containsKey(resource.getPersons())){
                    countMap[resource.getPersons()] = 1
                    durationMap[resource.getPersons()] = resource.getDuration()
                }else{
                    countMap[resource.getPersons()]!!.plus(1)
                    durationMap[resource.getPersons()]!!.plus(resource.getDuration())
                }
            }
        }

        //Todo: protect the aplication against the !! operator
        for(element in lista){
            val resource : TableThreeResource? = element as? TableThreeResource
            if(resource != null){
                mRowHeaderList.add(RowHeader(id.toString(),id.toString()))
                val cell: MutableList<Cell> = mutableListOf()
                cell.add(Cell(id.toString(),element.getPersons()))
                cell.add(Cell(id.toString(),countMap[element.getPersons()]))
                cell.add(Cell(id.toString(),durationMap[element.getPersons()]!!/3600))
                cell.add(Cell(id.toString(),durationMap[element.getPersons()]!!/60))
                cell.add(Cell(id.toString(),durationMap[element.getPersons()]))

                mCellList.add(cell)


            }
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)

        if(title != null) tool.title = "$title's Log" else tool.title = "Complete Details"
        adapter = MyTableViewAdapter(context)
        tableView.adapter = adapter
        adapter.setAllItems(mColumnHeaderList,mRowHeaderList, mCellList)


    }


    private fun generateLogTable3(lista: ArrayList<Parcelable>, view: View, title: String?) {

        val mRowHeaderList: MutableList<RowHeader> = mutableListOf()
        val mColumnHeaderList: MutableList<ColumnHeader> = mutableListOf()
        mColumnHeaderList.add(ColumnHeader("0", "Physical Space"))
        mColumnHeaderList.add(ColumnHeader("1", "Persons"))
        mColumnHeaderList.add(ColumnHeader("2", "Total Duration (h)"))
        mColumnHeaderList.add(ColumnHeader("3", "Total Duration (min)"))
        mColumnHeaderList.add(ColumnHeader("4", "Total Duration (s)"))
        mColumnHeaderList.add(ColumnHeader("5", "Arrive"))
        mColumnHeaderList.add(ColumnHeader("6", "Depart"))

        val mCellList: MutableList<List<Cell>> = mutableListOf()
        var id = 0


        for(element in lista){
            val resource : TableThreeResource? = element as? TableThreeResource
            if(resource != null){
                mRowHeaderList.add(RowHeader(id.toString(),id.toString()))
                val cell: MutableList<Cell> = mutableListOf()
                cell.add(Cell(id.toString(),element.physical_space))
                cell.add(Cell(id.toString(),element.getPersons()))
                cell.add(Cell(id.toString(),element.getDuration()/3600))
                cell.add(Cell(id.toString(),element.getDuration()/60))
                cell.add(Cell(id.toString(),element.getDuration()))

                cell.add(Cell(id.toString(),getDateTime(element.arrive)))
                cell.add(Cell(id.toString(),getDateTime(element.depart)))
                mCellList.add(cell)


            }
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)

        if(title != null) tool.title = "$title's Log" else tool.title = "Complete Log"
        adapter = MyTableViewAdapter(context)
        tableView.adapter = adapter
        adapter.setAllItems(mColumnHeaderList,mRowHeaderList, mCellList)


    }




    private fun generateLogTable4(lista: ArrayList<Parcelable>, view: View, title: String?) {

        val mRowHeaderList: MutableList<RowHeader> = mutableListOf()
        val mColumnHeaderList: MutableList<ColumnHeader> = mutableListOf()
        mColumnHeaderList.add(ColumnHeader("0", "Person"))
        mColumnHeaderList.add(ColumnHeader("1", "Physical Space"))
        mColumnHeaderList.add(ColumnHeader("2", "Total Duration (h)"))
        mColumnHeaderList.add(ColumnHeader("3", "Total Duration (min)"))
        mColumnHeaderList.add(ColumnHeader("4", "Total Duration (s)"))
        mColumnHeaderList.add(ColumnHeader("5", "Arrive"))
        mColumnHeaderList.add(ColumnHeader("6", "Depart"))

        val mCellList: MutableList<List<Cell>> = mutableListOf()
        var id = 0


        for(element in lista){
            val resource : TableFourResource? = element as? TableFourResource
            if(resource != null){
                mRowHeaderList.add(RowHeader(id.toString(),id.toString()))
                val cell: MutableList<Cell> = mutableListOf()
                cell.add(Cell(id.toString(),element.shortName))
                cell.add(Cell(id.toString(),element.physical_space))
                cell.add(Cell(id.toString(),element.getDuration()/3600))
                cell.add(Cell(id.toString(),element.getDuration()/60))
                cell.add(Cell(id.toString(),element.getDuration()))
                cell.add(Cell(id.toString(),getDateTime(element.arrive)))
                cell.add(Cell(id.toString(),getDateTime(element.depart)))
                mCellList.add(cell)
            }
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)

        if(title != null) tool.title = "$title's Log" else tool.title = "Complete Log"
        adapter = MyTableViewAdapter(context)
        tableView.adapter = adapter
        adapter.setAllItems(mColumnHeaderList,mRowHeaderList, mCellList)


    }



    private fun generateDetailTable4(lista: ArrayList<Parcelable>, view: View, title: String?) {

        val mRowHeaderList: MutableList<RowHeader> = mutableListOf()
        val mColumnHeaderList: MutableList<ColumnHeader> = mutableListOf()
        mColumnHeaderList.add(ColumnHeader("0", "Person"))
        mColumnHeaderList.add(ColumnHeader("1", "Places Found"))
        mColumnHeaderList.add(ColumnHeader("2", "Total Duration (h)"))
        mColumnHeaderList.add(ColumnHeader("3", "Total Duration (min)"))
        mColumnHeaderList.add(ColumnHeader("4", "Total Duration (s)"))

        val mCellList: MutableList<List<Cell>> = mutableListOf()
        var id = 0


        for(element in lista){
            val resource : Table4Aux? = element as? Table4Aux
            if(resource != null){
                mRowHeaderList.add(RowHeader(id.toString(),id.toString()))
                val cell: MutableList<Cell> = mutableListOf()
                cell.add(Cell(id.toString(),element.person))
                cell.add(Cell(id.toString(),element.count_places))
                cell.add(Cell(id.toString(),element.duration/3600))
                cell.add(Cell(id.toString(),element.duration/60))
                cell.add(Cell(id.toString(),element.duration))
                mCellList.add(cell)
            }
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)

        if(title != null) tool.title = "$title's Log" else tool.title = "Complete Details"
        adapter = MyTableViewAdapter(context)
        tableView.adapter = adapter
        adapter.setAllItems(mColumnHeaderList,mRowHeaderList, mCellList)


    }




    private fun generateChildDetailTable4(lista: ArrayList<Parcelable>, view: View, title: String?) {

        val mRowHeaderList: MutableList<RowHeader> = mutableListOf()
        val mColumnHeaderList: MutableList<ColumnHeader> = mutableListOf()
        mColumnHeaderList.add(ColumnHeader("0", "Physical Space"))
        mColumnHeaderList.add(ColumnHeader("1", "Nº of rendezvous"))
        mColumnHeaderList.add(ColumnHeader("2", "Total Duration (h)"))
        mColumnHeaderList.add(ColumnHeader("3", "Total Duration (min)"))
        mColumnHeaderList.add(ColumnHeader("4", "Total Duration (s)"))


        val countMap: MutableMap<String, Long> = mutableMapOf()
        val durationMap: MutableMap<String, Long> = mutableMapOf()

        for (element in lista){

            val item = element as TableFourResource
            if(!countMap.containsKey(item.physical_space)){
                countMap[item.physical_space] = 1
                durationMap[item.physical_space] = item.getDuration()
            }else{
                countMap[item.physical_space] = countMap[item.physical_space]!!.plus(1)
                durationMap[item.physical_space] = durationMap[item.physical_space]!!.plus(item.getDuration())
            }


        }

        val mCellList: MutableList<List<Cell>> = mutableListOf()
        var id = 0


        for(element in durationMap){
//            val resource : Table4Aux? = element as? Table4Aux
            mRowHeaderList.add(RowHeader(id.toString(),id.toString()))
            val cell: MutableList<Cell> = mutableListOf()
            cell.add(Cell(id.toString(),element.key))
            cell.add(Cell(id.toString(),countMap[element.key]))
            cell.add(Cell(id.toString(),element.value/3600))
            cell.add(Cell(id.toString(),element.value/60))
            cell.add(Cell(id.toString(),element.value))
            mCellList.add(cell)
            id+=1
    }
        tableView = view.findViewById(R.id.content_container)

        if(title != null) tool.title = "$title's Log" else tool.title = "Complete Details"
        adapter = MyTableViewAdapter(context)
        tableView.adapter = adapter
        adapter.setAllItems(mColumnHeaderList,mRowHeaderList, mCellList)


    }


    private fun generateLogTable5(lista: ArrayList<Parcelable>, view: View, title: String?) {

        val mRowHeaderList: MutableList<RowHeader> = mutableListOf()
        val mColumnHeaderList: MutableList<ColumnHeader> = mutableListOf()
        mColumnHeaderList.add(ColumnHeader("0", "Physical Space"))
        mColumnHeaderList.add(ColumnHeader("1", "Person"))
        mColumnHeaderList.add(ColumnHeader("2", "Total Duration (h)"))
        mColumnHeaderList.add(ColumnHeader("3", "Total Duration (min)"))
        mColumnHeaderList.add(ColumnHeader("4", "Total Duration (s)"))
        mColumnHeaderList.add(ColumnHeader("5", "Arrive"))
        mColumnHeaderList.add(ColumnHeader("6", "Depart"))

        val mCellList: MutableList<List<Cell>> = mutableListOf()
        var id = 0


        for(element in lista){
            val resource : TableFiveResource? = element as? TableFiveResource
            if(resource != null){
                mRowHeaderList.add(RowHeader(id.toString(),id.toString()))
                val cell: MutableList<Cell> = mutableListOf()
                cell.add(Cell(id.toString(),element.physical_space))
                cell.add(Cell(id.toString(),element.shortName))
                cell.add(Cell(id.toString(),element.getDuration()/3600))
                cell.add(Cell(id.toString(),element.getDuration()/60))
                cell.add(Cell(id.toString(),element.getDuration()))
                cell.add(Cell(id.toString(),getDateTime(element.arrive)))
                cell.add(Cell(id.toString(),getDateTime(element.depart)))
                mCellList.add(cell)
            }
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)

        if(title != null) tool.title = "$title's Log" else tool.title = "Complete Log"
        adapter = MyTableViewAdapter(context)
        tableView.adapter = adapter
        adapter.setAllItems(mColumnHeaderList,mRowHeaderList, mCellList)


    }


    private fun generateDetailTable5(lista: ArrayList<Parcelable>, view: View, title: String?) {

        val mRowHeaderList: MutableList<RowHeader> = mutableListOf()
        val mColumnHeaderList: MutableList<ColumnHeader> = mutableListOf()
        mColumnHeaderList.add(ColumnHeader("0", "Physical Space"))
        mColumnHeaderList.add(ColumnHeader("1", "Persons Found"))
        mColumnHeaderList.add(ColumnHeader("2", "Total Duration (h)"))
        mColumnHeaderList.add(ColumnHeader("3", "Total Duration (min)"))
        mColumnHeaderList.add(ColumnHeader("4", "Total Duration (s)"))

        val mCellList: MutableList<List<Cell>> = mutableListOf()
        var id = 0


        for(element in lista){
            val resource : Table4Aux? = element as? Table4Aux
            if(resource != null){
                mRowHeaderList.add(RowHeader(id.toString(),id.toString()))
                val cell: MutableList<Cell> = mutableListOf()
                cell.add(Cell(id.toString(),element.person))
                cell.add(Cell(id.toString(),element.count_places))
                cell.add(Cell(id.toString(),element.duration/3600))
                cell.add(Cell(id.toString(),element.duration/60))
                cell.add(Cell(id.toString(),element.duration))
                mCellList.add(cell)
            }
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)

        if(title != null) tool.title = "$title's Log" else tool.title = "Complete Details"
        adapter = MyTableViewAdapter(context)
        tableView.adapter = adapter
        adapter.setAllItems(mColumnHeaderList,mRowHeaderList, mCellList)


    }



    private fun generateChildDetailTable5(lista: ArrayList<Parcelable>, view: View, title: String?) {

        Log.i("teste", lista.toString())
        val mRowHeaderList: MutableList<RowHeader> = mutableListOf()
        val mColumnHeaderList: MutableList<ColumnHeader> = mutableListOf()
        mColumnHeaderList.add(ColumnHeader("0", "Person"))
        mColumnHeaderList.add(ColumnHeader("1", "Nº of rendezvous"))
        mColumnHeaderList.add(ColumnHeader("2", "Total Duration (h)"))
        mColumnHeaderList.add(ColumnHeader("3", "Total Duration (min)"))
        mColumnHeaderList.add(ColumnHeader("4", "Total Duration (s)"))


        val countMap: MutableMap<String, Long> = mutableMapOf()
        val durationMap: MutableMap<String, Long> = mutableMapOf()

        for (element in lista){

            val item = element as TableFiveResource
            if(!countMap.containsKey(item.shortName)){
                countMap[item.shortName] = 1
                durationMap[item.shortName] = item.getDuration()
            }else{
                countMap[item.shortName] = countMap[item.shortName]!!.plus(1)
                durationMap[item.shortName] = durationMap[item.shortName]!!.plus(item.getDuration())
            }


        }

        val mCellList: MutableList<List<Cell>> = mutableListOf()
        var id = 0


        for(element in durationMap){
//            val resource : Table4Aux? = element as? Table4Aux
            mRowHeaderList.add(RowHeader(id.toString(),id.toString()))
            val cell: MutableList<Cell> = mutableListOf()
            cell.add(Cell(id.toString(),element.key))
            cell.add(Cell(id.toString(),countMap[element.key]))
            cell.add(Cell(id.toString(),element.value/3600))
            cell.add(Cell(id.toString(),element.value/60))
            cell.add(Cell(id.toString(),element.value))
            mCellList.add(cell)
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)

        if(title != null) tool.title = "$title's Log" else tool.title = "Complete Details"
        adapter = MyTableViewAdapter(context)
        tableView.adapter = adapter
        adapter.setAllItems(mColumnHeaderList,mRowHeaderList, mCellList)


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

    private fun getDateTime(s: Long): String? {

        val sdf = SimpleDateFormat("dd-MM-yyyy  HH:mm:ss")
        val date = Date(s * 1000)
        return sdf.format(date)
    }
}