package com.example.klsdinfo.main.MainFragments

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.sort.SortState
import com.example.klsdinfo.R
import com.example.klsdinfo.data.models.*
import com.example.klsdinfo.main.adapters.MyTableViewAdapter
import java.text.SimpleDateFormat
import java.util.*


class CustomTableFragment : DialogFragment() {

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


                    "detail1" -> generateDetailTable1(lista, view, "$title's Detail")

                    "detail2" ->  generateDetailTable2(lista, view, "$title's Detail")





                    "log3" -> generateLogTable3(lista, view, "$title's Log")
                    "detail3" -> generateDetailTable3(lista, view, "$title's Log")
                    "child_detail3" -> generateChildDetailTable3(lista, view, "$title's Log")


                    "log4","child_log4" -> generateLogTable4(lista, view,"$title's Log")
                    "detail4" -> generateDetailTable4(lista, view, "$title's Detail")
                    "child_detail4" -> generateChildDetailTable4(lista, view, "$title's Detail")


                    "log5", "child_log5" -> generateLogTable5(lista,view,"$title's Log")
                    "detail5" -> generateDetailTable5(lista, view, "$title's Detail")
                    "child_detail5" -> generateChildDetailTable5(lista, view, "$title's Detail")


                }
            }
        }


        return view
    }

    private fun generateDetailTable1(lista: ArrayList<Parcelable>, view: View, title: String) {
        val mRowHeaderList: MutableList<RowHeader> = mutableListOf()
        val mColumnHeaderList: MutableList<ColumnHeader> = mutableListOf()
        mColumnHeaderList.add(ColumnHeader("0", "Name"))
        mColumnHeaderList.add(ColumnHeader("2", "Total Duration (h)"))
        mColumnHeaderList.add(ColumnHeader("3", "Total Duration (min)"))
        mColumnHeaderList.add(ColumnHeader("4", "Total Duration (s)"))

        val mCellList: MutableList<List<Cell>> = mutableListOf()
        var id = 0

        //Todo: protect the aplication against the !! operator
        for(element in lista){
            val resource : TableOneResource? = element as? TableOneResource
            if(resource != null){
                mRowHeaderList.add(RowHeader(id.toString(), id.toString()))
                val cell: MutableList<Cell> = mutableListOf()
                cell.add(Cell(id.toString(), element.shortName))
                cell.add(Cell(id.toString(), element.duration / 3600))
                cell.add(Cell(id.toString(), element.duration / 60))
                cell.add(Cell(id.toString(), element.duration))
                mCellList.add(cell)
            }
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)
        tool.title = title
        adapter = MyTableViewAdapter(context)
        tableView.adapter = adapter
        adapter.setAllItems(mColumnHeaderList,mRowHeaderList, mCellList)
    }

    private fun generateDetailTable2(lista: ArrayList<Parcelable>, view: View, title: String) {

        val mRowHeaderList: MutableList<RowHeader> = mutableListOf()
        val mColumnHeaderList: MutableList<ColumnHeader> = mutableListOf()
        mColumnHeaderList.add(ColumnHeader("0", "Name"))
        mColumnHeaderList.add(ColumnHeader("1", "Physical Space"))
        mColumnHeaderList.add(ColumnHeader("2", "Total Duration (h)"))
        mColumnHeaderList.add(ColumnHeader("3", "Total Duration (min)"))
        mColumnHeaderList.add(ColumnHeader("4", "Total Duration (s)"))

        val mCellList: MutableList<List<Cell>> = mutableListOf()
        var id = 0

        //Todo: protect the aplication against the !! operator
        for(element in lista){
            val resource : TableTwoResource? = element as? TableTwoResource
            if(resource != null){
                mRowHeaderList.add(RowHeader(id.toString(), id.toString()))
                val cell: MutableList<Cell> = mutableListOf()
                cell.add(Cell(id.toString(), element.shortName))
                cell.add(Cell(id.toString(), element.physical_space))
                cell.add(Cell(id.toString(), element.duration / 3600))
                cell.add(Cell(id.toString(), element.duration / 60))
                cell.add(Cell(id.toString(), element.duration))
                mCellList.add(cell)
            }
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)
        tool.title = title
        adapter = MyTableViewAdapter(context)
        tableView.adapter = adapter
        adapter.setAllItems(mColumnHeaderList,mRowHeaderList, mCellList)




    }


    private fun generateChildDetailTable3(lista: ArrayList<Parcelable>, view: View, title: String?){


        val mRowHeaderList: MutableList<RowHeader> = mutableListOf()
        val mColumnHeaderList: MutableList<ColumnHeader> = mutableListOf()
        mColumnHeaderList.add(ColumnHeader("0", "Persons"))
        mColumnHeaderList.add(ColumnHeader("1", "Nº of Rendezvous"))
        mColumnHeaderList.add(ColumnHeader("2", "Total Duration (h)"))
        mColumnHeaderList.add(ColumnHeader("3", "Total Duration (min)"))
        mColumnHeaderList.add(ColumnHeader("4", "Total Duration (s)"))

        val mCellList: MutableList<List<Cell>> = mutableListOf()
        var id = 0


        val groupSet = mutableSetOf<List<String>>()
        for (element in lista){
            val resource : TableThreeResource? = element as TableThreeResource
            if(resource != null) groupSet.add(resource.getPersonsList())
        }
        for(element in groupSet){
            var duration: Long = 0
            var count: Long = 0
            for (resource3 in lista){
                val resource3 : TableThreeResource? = resource3 as TableThreeResource
                if(resource3 != null){
                    if(resource3.getPersonsList().containsAll(element)){
                        duration += resource3.getDuration()
                        count += 1
                    }
                }
            }
            mRowHeaderList.add(RowHeader(id.toString(), id.toString()))
            val cell: MutableList<Cell> = mutableListOf()
            cell.add(
                Cell(
                    id.toString(),
                    element.toString().substring(1, element.toString().length - 1)
                )
            )
            cell.add(Cell(id.toString(), count))
            cell.add(Cell(id.toString(), duration / 3600))
            cell.add(Cell(id.toString(), duration / 60))
            cell.add(Cell(id.toString(), duration))
            mCellList.add(cell)
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)
        if(title != null) tool.title = title else tool.title = "Undefined Title"
        adapter = MyTableViewAdapter(context)
        tableView.adapter = adapter
        adapter.setAllItems(mColumnHeaderList,mRowHeaderList, mCellList)


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


        val groupSet = mutableSetOf<List<String>>()
        for (element in lista){
            val resource : TableThreeResource? = element as TableThreeResource
            if(resource != null) groupSet.add(resource.getPersonsList())
        }

        for(element in groupSet){
            var duration: Long = 0
            var count: Long = 0
            for (resource3 in lista){
                val resource3 : TableThreeResource? = resource3 as TableThreeResource
                if(resource3 != null){
                    if(resource3.getPersonsList().containsAll(element)){
                        duration += resource3.getDuration()
                        count += 1
                    }
                }
            }
            mRowHeaderList.add(RowHeader(id.toString(), id.toString()))
            val cell: MutableList<Cell> = mutableListOf()
            cell.add(
                Cell(
                    id.toString(),
                    element.toString().substring(1, element.toString().length - 1)
                )
            )
            cell.add(Cell(id.toString(), count))
            cell.add(Cell(id.toString(), duration / 3600))
            cell.add(Cell(id.toString(), duration / 60))
            cell.add(Cell(id.toString(), duration))
            mCellList.add(cell)
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)
        if(title != null) tool.title = title else tool.title = "Undefined Title"
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
                mRowHeaderList.add(RowHeader(id.toString(), id.toString()))
                val cell: MutableList<Cell> = mutableListOf()
                cell.add(Cell(id.toString(), element.physical_space))
                cell.add(Cell(id.toString(), element.getPersons()))
                cell.add(Cell(id.toString(), element.getDuration() / 3600))
                cell.add(Cell(id.toString(), element.getDuration() / 60))
                cell.add(Cell(id.toString(), element.getDuration()))

                cell.add(Cell(id.toString(), getDateTime(element.arrive)))
                cell.add(Cell(id.toString(), getDateTime(element.depart)))
                mCellList.add(cell)


            }
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)
        if(title != null) tool.title = title else tool.title = "Undefined Title"
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
                mRowHeaderList.add(RowHeader(id.toString(), id.toString()))
                val cell: MutableList<Cell> = mutableListOf()
                cell.add(Cell(id.toString(), element.shortName))
                cell.add(Cell(id.toString(), element.physical_space))
                cell.add(Cell(id.toString(), element.getDuration() / 3600))
                cell.add(Cell(id.toString(), element.getDuration() / 60))
                cell.add(Cell(id.toString(), element.getDuration()))
                cell.add(Cell(id.toString(), getDateTime(element.arrive)))
                cell.add(Cell(id.toString(), getDateTime(element.depart)))
                mCellList.add(cell)
            }
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)

        if(title != null) tool.title = title else tool.title = "Complete Log"
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
                mRowHeaderList.add(RowHeader(id.toString(), id.toString()))
                val cell: MutableList<Cell> = mutableListOf()
                cell.add(Cell(id.toString(), element.name))
                cell.add(Cell(id.toString(), element.count_places))
                cell.add(Cell(id.toString(), element.duration / 3600))
                cell.add(Cell(id.toString(), element.duration / 60))
                cell.add(Cell(id.toString(), element.duration))
                mCellList.add(cell)
            }
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)

        if(title != null) tool.title = title else tool.title = "Complete Details"
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
            mRowHeaderList.add(RowHeader(id.toString(), id.toString()))
            val cell: MutableList<Cell> = mutableListOf()
            cell.add(Cell(id.toString(), element.key))
            cell.add(Cell(id.toString(), countMap[element.key]))
            cell.add(Cell(id.toString(), element.value / 3600))
            cell.add(Cell(id.toString(), element.value / 60))
            cell.add(Cell(id.toString(), element.value))
            mCellList.add(cell)
            id+=1
    }
        tableView = view.findViewById(R.id.content_container)

        if(title != null) tool.title = title else tool.title = "Complete Details"
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
                mRowHeaderList.add(RowHeader(id.toString(), id.toString()))
                val cell: MutableList<Cell> = mutableListOf()
                cell.add(Cell(id.toString(), element.physical_space))
                cell.add(Cell(id.toString(), element.shortName))
                cell.add(Cell(id.toString(), element.getDuration() / 3600))
                cell.add(Cell(id.toString(), element.getDuration() / 60))
                cell.add(Cell(id.toString(), element.getDuration()))
                cell.add(Cell(id.toString(), getDateTime(element.arrive)))
                cell.add(Cell(id.toString(), getDateTime(element.depart)))
                mCellList.add(cell)
            }
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)

        if(title != null) tool.title = title else tool.title = "Complete Log"
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
                mRowHeaderList.add(RowHeader(id.toString(), id.toString()))
                val cell: MutableList<Cell> = mutableListOf()
                cell.add(Cell(id.toString(), element.name))
                cell.add(Cell(id.toString(), element.count_places))
                cell.add(Cell(id.toString(), element.duration / 3600))
                cell.add(Cell(id.toString(), element.duration / 60))
                cell.add(Cell(id.toString(), element.duration))
                mCellList.add(cell)
            }
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)

        if(title != null) tool.title = title else tool.title = "Complete Details"
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
            mRowHeaderList.add(RowHeader(id.toString(), id.toString()))
            val cell: MutableList<Cell> = mutableListOf()
            cell.add(Cell(id.toString(), element.key))
            cell.add(Cell(id.toString(), countMap[element.key]))
            cell.add(Cell(id.toString(), element.value / 3600))
            cell.add(Cell(id.toString(), element.value / 60))
            cell.add(Cell(id.toString(), element.value))
            mCellList.add(cell)
            id+=1
        }
        tableView = view.findViewById(R.id.content_container)

        if(title != null) tool.title = title else tool.title = "Complete Details"
        adapter = MyTableViewAdapter(context)
        tableView.adapter = adapter
        adapter.setAllItems(mColumnHeaderList,mRowHeaderList, mCellList)


    }



    private fun cancelUpload() {
        dialog?.dismiss()
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