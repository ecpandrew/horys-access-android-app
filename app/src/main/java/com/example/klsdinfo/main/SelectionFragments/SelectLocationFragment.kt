package com.example.klsdinfo.main.SelectionFragments

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klsdinfo.R
import com.example.klsdinfo.data.SelectLocationViewModel
import com.example.klsdinfo.data.SemanticApiService
import com.example.klsdinfo.data.SemanticRepository
import com.example.klsdinfo.data.ViewModelFactory
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.models.Location
import com.example.klsdinfo.data.models.PhysicalSpace
import com.example.klsdinfo.main.TableFragments.TableOnefrag
import com.example.klsdinfo.main.adapters.PhysicalSpaceAdapter
import java.util.*

class SelectLocationFragment: Fragment() {


    lateinit var rv: RecyclerView
    lateinit var mAdapter: PhysicalSpaceAdapter
    lateinit var pilha: Stack<List<PhysicalSpace>>
    lateinit var back: Button
    lateinit var get: Button
    lateinit var layoutManager: LinearLayoutManager
    lateinit var viewModel : SelectLocationViewModel
    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var progressBar: ProgressBar


    var listPhysicalSpaces: List<PhysicalSpace> = listOf()



    companion object {
        fun newInstance(): SelectLocationFragment {
            return SelectLocationFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.select_location_real_layout, container, false)

        setupViewAndClickListeners(view)


        setupViewModel()




        print("onCreateView")
        return view
    }

    private fun setupViewAndClickListeners(view: View) {
        rv = view.findViewById(R.id.selectionRealRecyclerView)
        progressBar = view.findViewById(R.id.progress_bar)
        layoutManager = LinearLayoutManager(context)
        rv.layoutManager = layoutManager
        rv.setHasFixedSize(true)
        pilha = Stack()
        mAdapter = PhysicalSpaceAdapter(context!!, listOf())
        rv.adapter = mAdapter
        back = view.findViewById(R.id.buttonClear)
        get = view.findViewById(R.id.buttonGet)
        get.setOnClickListener {
            val selectedItemPositions = mAdapter.getSelectedItems()
            val selectedLocations = ArrayList<Parcelable>()
            for (i in selectedItemPositions){
                selectedLocations.add(pilha.peek()[i])
            }
            val bundle = Bundle()
            Log.i("debug", "Enviado: $selectedLocations")
            bundle.putParcelableArrayList("resources", selectedLocations)
            val dialog = TableOnefrag()
            dialog.arguments = bundle
            navigateToFragment(dialog, true)
        }

        val obj = object: PhysicalSpaceAdapter.OnClickListener {
            override fun onItemLongClick(view: View, obj: PhysicalSpace, pos: Int) {}
            override fun onCheckBoxClick(view: View, obj: PhysicalSpace, pos: Int) { mAdapter.toggleSelection(pos) }
            override fun onItemClick(view: View, obj: PhysicalSpace, pos: Int) {
                if (mAdapter.getSelectedItemCount() > 0) run {

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
        validateBackParentButton(false)



    }


    private fun setupViewModel(){
        val repo = SemanticRepository.getInstance(SemanticApiService.create(), AppDatabase.getInstance(activity?.applicationContext!!)!!)
        val factory = ViewModelFactory(repo, null,activity?.application!!)
        viewModel = ViewModelProviders.of(this, factory).get(SelectLocationViewModel::class.java)

        viewModel.loadingProgress.observe(this, androidx.lifecycle.Observer {
            when(it){
                true -> progressBar.visibility = View.VISIBLE
                false -> progressBar.visibility = View.INVISIBLE
            }
        })

        viewModel.mPhysicalSpaces.observe(this, androidx.lifecycle.Observer {
            listPhysicalSpaces = it
            mAdapter.setItems(it)
            pilha.push(it)
            mAdapter.notifyDataSetChanged()

        })


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


    private fun deleteInboxes() {
        val selectedItemPositions = mAdapter.getSelectedItems()
        val selectedLocations = ArrayList<Location>()

        // TODO: implementar ação de escolha da seleção


    }
    override fun onAttach(context: Context) {
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
        viewModel.fetchPhysicalSpaces()
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

