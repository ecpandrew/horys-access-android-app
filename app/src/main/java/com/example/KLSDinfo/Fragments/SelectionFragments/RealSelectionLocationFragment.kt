package com.example.KLSDinfo.Fragments.SelectionFragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.KLSDinfo.Adapters.SelectionAdapters.RealSelectLocationAdapter
import com.example.KLSDinfo.Models.Location
import com.example.KLSDinfo.R
import com.example.KLSDinfo.UtilClasses.Tools
import java.util.ArrayList

class RealSelectionLocationFragment: Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: RealSelectLocationAdapter
    lateinit var toolbar: Toolbar
    private val items: MutableList<Location> = ArrayList<Location>()
    private lateinit var actionModeCallback: ActionModeCallback
    private var actionMode: ActionMode? = null


    companion object {
        fun newInstance(): RealSelectionLocationFragment {
            return RealSelectionLocationFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.selection_real_layout, container, false)

        initComponents(view)



        return view
    }


    private fun getLocations(): List<Location>{
        val items: MutableList<Location> = mutableListOf()

        // LSDI child nodes
        var item: Location = Location("Server Room","Room that server is in",null,null)
        var item2: Location = Location("Laboratory","Lab",null,null)
        var item3: Location = Location("Reunion Room","Lab",null,null)
        var lsdiChilds: MutableList<Location> = mutableListOf()
        lsdiChilds.add(item)
        lsdiChilds.add(item2)
        lsdiChilds.add(item3)

        // LSDI Node
        var lsdi: Location = Location("LSDI", "Laboratorio de sistemas Distribuidos e Inteligentes",null,lsdiChilds)

        items.add(lsdi)




        // Another child nodes
        val i : Location  = Location("Parents's Child","child",null,null)
        val i2 : Location   = Location("Parents's Child 2","child 2",null,null)
        val i3 : Location   = Location("Parents's Child 3","child 3",null,null)
        var parentChilds: MutableList<Location> = mutableListOf()

        parentChilds.add(i)
        parentChilds.add(i2)
        parentChilds.add(i3)


        // LSDI Node
        var parent: Location = Location("Parent", "Parents",null,parentChilds)

        items.add(parent)

        return items

    }

    private fun initComponents(view: View) {

        recyclerView = view.findViewById(R.id.selectionRealRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        mAdapter = RealSelectLocationAdapter(context!!,getLocations())

        recyclerView.adapter = mAdapter


        val obj = object: RealSelectLocationAdapter.OnClickListener {
            override fun onItemLongClick(view: View, obj: Location, pos: Int) {
                enableActionMode(pos)

            }

            override fun onItemClick(view: View, obj: Location, pos: Int) {
                if (mAdapter.getSelectedItemCount() > 0) run {
                    enableActionMode(pos)
                    Log.i("onclick", "onClick if")


                }else{
                    Toast.makeText(context,"onClick: ${obj.name}", Toast.LENGTH_LONG).show()
                    if(obj.childs != null){
                        mAdapter.setItems(obj.childs)
                        mAdapter.notifyDataSetChanged()

                    }

                }
            }
        }


        mAdapter.setOnClickListener(obj)
        actionModeCallback = ActionModeCallback()
    }




    private fun enableActionMode(position: Int) {
        if (actionMode == null) {
            actionMode = this@RealSelectionLocationFragment.activity!!.startActionMode(actionModeCallback)
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

}

