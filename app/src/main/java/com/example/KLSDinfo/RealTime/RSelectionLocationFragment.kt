package com.example.KLSDinfo.RealTime

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.KLSDinfo.Adapters.PhysicalSpaceAdapter
import com.example.KLSDinfo.Fragments.DialogFragments.FullscreenDialogFragment
import com.example.KLSDinfo.Models.Location
import com.example.KLSDinfo.Models.MultiCheckRole
import com.example.KLSDinfo.Models.PhysicalSpace
import com.example.KLSDinfo.R
import com.example.KLSDinfo.Requests.FakeRequest
import com.example.KLSDinfo.UtilClasses.Tools
import com.google.gson.GsonBuilder
import java.util.*

class RSelectionLocationFragment: Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: PhysicalSpaceAdapter
    lateinit var pilha: Stack<List<PhysicalSpace>>
    private lateinit var actionModeCallback: ActionModeCallback
    private var actionMode: ActionMode? = null
    lateinit var back: Button
    lateinit var get: Button


    companion object {
        fun newInstance(): RSelectionLocationFragment {
            return RSelectionLocationFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.select_location_real_layout, container, false)

        initComponents(view)

        print("onCreateView")
        return view
    }



    private fun initComponents(view: View) {

        recyclerView = view.findViewById(R.id.selectionRealRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        pilha = Stack()
        pilha.push(FakeRequest().getAllPhysicalSpaces())
        mAdapter = PhysicalSpaceAdapter(context!!, FakeRequest().getAllPhysicalSpaces())

        Log.i("debug", GsonBuilder().setPrettyPrinting().create().toJson(FakeRequest().getAllPhysicalSpaces()))

        recyclerView.adapter = mAdapter

        back = view.findViewById(R.id.backToParent)
        get = view.findViewById(R.id.getInfo)


        get.setOnClickListener {

            val selectedItemPositions = mAdapter.getSelectedItems()
            val selectedLocations = ArrayList<Parcelable>()

            for (i in selectedItemPositions){
                selectedLocations.add(pilha.peek()[i])
            }

            val bundle = Bundle()


            Log.i("debug", "Enviado: $selectedLocations")

            bundle.putParcelableArrayList("resources", selectedLocations)
            val dialog = FullscreenDialogFragment()
            dialog.arguments = bundle
            val activity: AppCompatActivity = view.context as AppCompatActivity
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog")
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
    }


//    private fun getSelectedElements(): MutableList<Parcelable> {
//
//        val locations: MutableList<Parcelable> = mutableListOf()
//        val locations: List<PhysicalSpace> = mAdapter.groups as List<MultiCheckRole>
//
//        for(i in 0 until roles.size){
//            for (j in 0 until roles[i].selectedChildren.size){
//                when(roles[i].selectedChildren[j]){
//                    true -> { persons.add(items[i].persons[j])
//
//                    }
//                }
//            }
//
//        }
//        Log.i("debug", "Enviado: $persons")
//        return persons
//
//    }


    private fun enableActionMode(position: Int) {
        if (actionMode == null) {
            actionMode = this@RSelectionLocationFragment.activity!!.startActionMode(actionModeCallback)
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
        Log.d("Lifecycle", "Real Time: Location Selection Fragment: $msg")
    }


}

