package com.example.KLSDinfo.RealTime.MainFragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.example.KLSDinfo.UtilClasses.PhysicalSpaceAdapter
import com.example.KLSDinfo.Models.Location
import com.example.KLSDinfo.Models.PhysicalSpace
import com.example.KLSDinfo.R
import com.example.KLSDinfo.Models.FakeRequest
import com.example.KLSDinfo.RealTime.TableFragments.TableOneDialog
import com.example.KLSDinfo.UtilClasses.Tools
import com.example.KLSDinfo.Volley.VolleySingleton
import java.util.*

class RSelectionLocationFragment: Fragment() {


    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: PhysicalSpaceAdapter
    lateinit var pilha: Stack<List<PhysicalSpace>>
    private lateinit var actionModeCallback: ActionModeCallback
    private var actionMode: ActionMode? = null
    lateinit var back: Button
    lateinit var get: Button
    lateinit var url: String

    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog

    var listPhysicalSpaces: List<PhysicalSpace> = listOf()
    lateinit var rq: RequestQueue


    var params: Map<String, String>? = null

    companion object {
        fun newInstance(): RSelectionLocationFragment {
            return RSelectionLocationFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.select_location_real_layout, container, false)


        url = "http://smartlab.lsdi.ufma.br/semantic/api/physical_spaces/roots"

        initComponents(view)





        print("onCreateView")
        return view
    }



    //Todo: Organizar esse metodo initComponents

    private fun initComponents(view: View) {

        recyclerView = view.findViewById(R.id.selectionRealRecyclerView)
        recyclerView.visibility = View.GONE
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        pilha = Stack()
//        pilha.push(listPhysicalSpaces)

        mAdapter = PhysicalSpaceAdapter(context!!, listPhysicalSpaces)
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
            val dialog = TableOneDialog()
            dialog.arguments = bundle
            navigateToFragment(dialog, true)

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


        val queue= VolleySingleton.getInstance(context).requestQueue
        val url = "http://smartlab.lsdi.ufma.br/semantic/api/physical_spaces/roots"

        progress = AlertDialog.Builder(context)
        progress.setCancelable(false)
        progress.setView(R.layout.loading_dialog_layout)
        alertDialog = progress.create()
        alertDialog.show()

        //Todo: Organizar esse metodo initComponents
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                VolleyLog.v("Response:%n %s", response)
                val lista: List<PhysicalSpace> = FakeRequest().getAllPhysicalSpaces(response)
                pilha.push(lista)
                mAdapter.setItems(lista)
                mAdapter.notifyDataSetChanged()
                recyclerView.visibility = View.VISIBLE

                alertDialog.dismiss()

            },
            Response.ErrorListener {
                VolleyLog.e("Error: ", it.message)
                alertDialog.dismiss()
            })

        // Add the request to the RequestQueue.


        stringRequest.retryPolicy = DefaultRetryPolicy(20 * 1000, 3, 1.0f)
        stringRequest.tag = this

        queue.add(stringRequest)
    }



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

    fun navigateToFragment(fragToGo: Fragment, addToBackStack: Boolean = false){
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fragment_container, fragToGo)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if(addToBackStack){
            transaction.addToBackStack(null) // Todo: verificar o ciclo de vida dos fragmentos
        }
        transaction.commit()
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

