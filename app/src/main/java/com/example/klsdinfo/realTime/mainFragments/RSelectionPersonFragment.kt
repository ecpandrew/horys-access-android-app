package com.example.klsdinfo.realTime.mainFragments
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klsdinfo.ErrorFragment
import com.example.klsdinfo.utilClasses.MultiCheckRoleAdapter
import com.example.klsdinfo.models.*
import com.example.klsdinfo.R
import com.example.klsdinfo.realTime.TableFragments.TableTwoFrag
import com.example.klsdinfo.repositories.ListPersonViewModel
import com.example.klsdinfo.repositories.SemanticApiService
import com.example.klsdinfo.repositories.SemanticRepository
import com.example.klsdinfo.repositories.SemanticViewModelFactory
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


open class RSelectionPersonFragment : Fragment(), LifecycleOwner {

    lateinit var mAdapter: MultiCheckRoleAdapter
    lateinit var items: List<MultiCheckRole>
    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var mCheckRoles : List<MultiCheckRole>
    lateinit var rv: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var progressBar: ProgressBar
    lateinit var viewModel: ListPersonViewModel


    companion object {
        fun newInstance(): RSelectionPersonFragment {
            return RSelectionPersonFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.select_person_real_layout, container, false)
        setup(view)
        setupViewModel()
        print("onCreateView")
        return view
    }

    private fun setupViewModel() {
        val repo = SemanticRepository.getInstance(SemanticApiService.create())
        val factory = SemanticViewModelFactory(repo, activity?.application!! )

        viewModel = ViewModelProviders.of(this, factory).get(ListPersonViewModel::class.java)
        viewModel.mPeople.observe(viewLifecycleOwner, Observer {
            val data = configData(it)
            mCheckRoles = data
            items = data
            updateAdapter(data)
            Log.i("observe", it.toString())
        })
    }

    private fun updateAdapter(it: List<MultiCheckRole>){
        mAdapter = MultiCheckRoleAdapter(it)
        rv.adapter = mAdapter
        rv.layoutManager = layoutManager
        rv.setHasFixedSize(true)
        rv.adapter = mAdapter
        myAdapterSetClickListener()
        progressBar.visibility = View.GONE

    }

    private fun setup(view: View) {
        rv = view.findViewById(R.id.recycler_view)
        progressBar = view.findViewById(R.id.progress_bar)
        layoutManager = LinearLayoutManager(context)
        items = listOf()
        mAdapter = MultiCheckRoleAdapter(listOf())
        rv.adapter = mAdapter
        rv.layoutManager = layoutManager
        rv.setHasFixedSize(true)
        rv.adapter = mAdapter
        myAdapterSetClickListener()
        val btnClear : Button = view.findViewById(R.id.buttonClear)
        btnClear.setOnClickListener {
            mAdapter.clearChoices()
        }
        // Todo: esse envio deve ocorrer por meio de armazenamento local
        val btnSend : Button = view.findViewById(R.id.buttonGet)
        btnSend.setOnClickListener {
            val seletedElements: ArrayList<Parcelable> = getSelectedElements()
            val bundle = Bundle()
            bundle.putParcelableArrayList("resources", seletedElements)
            val dialog = TableTwoFrag()
            dialog.arguments = bundle
            navigateToFragment(dialog, true)
        }
    }

    private fun myAdapterSetClickListener() {
        val obj = object: MultiCheckRoleAdapter.OnClickListener{
            override fun onClick(view: View, group: ExpandableGroup<*>, pos: Int) {
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
    }



    fun configData(mPeople: List<Person2>) : List<MultiCheckRole>{
        val roles = mutableSetOf<String>()
        val listRoles = mutableListOf<Role2>()
        for(person in mPeople){
            if(person.roles == null){
                roles.add("No Role")
            }else{
                for (role in person.roles){
                    roles.add(role.name)
                }
            }
        }
        for(s in roles){
            listRoles.add(Role2(s))
        }
        return getMultiCheckRoles(listRoles, mPeople)
    }


    fun getMultiCheckRoles(listRoles: List<Role2>,lista: List<Person2>): List<MultiCheckRole> {
        lateinit var items: List<MultiCheckRole>
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

    fun navigateToFragment(fragToGo: Fragment, addToBackStack: Boolean = false){
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fragment_container, fragToGo)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if(addToBackStack){
            transaction.addToBackStack(null) // Todo: verificar o ciclo de vida dos fragmentos
        }
        transaction.commit()
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
        viewModel.load()
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
        Log.d("Lifecycle", "Real Time: Person Selection Fragment: $msg")
    }



}