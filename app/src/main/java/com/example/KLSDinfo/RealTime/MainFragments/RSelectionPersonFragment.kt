package com.example.KLSDinfo.RealTime.MainFragments
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.example.KLSDinfo.ErrorFragment
import com.example.KLSDinfo.UtilClasses.MultiCheckRoleAdapter
import com.example.KLSDinfo.Models.*
import com.example.KLSDinfo.R
import com.example.KLSDinfo.Models.FakeRequest
import com.example.KLSDinfo.RealTime.TableFragments.TableTwoFrag
import com.example.KLSDinfo.Repositories.InjectorUtils
import com.example.KLSDinfo.Repositories.ListPersonViewModel
import com.example.KLSDinfo.Repositories.SemanticApiService
import com.example.KLSDinfo.Repositories.SemanticRepository
import com.example.KLSDinfo.Volley.VolleySingleton
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class RSelectionPersonFragment : Fragment() {

    lateinit var mAdapter: MultiCheckRoleAdapter
    lateinit var items: List<MultiCheckRole>
    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var queue: RequestQueue
    lateinit var mCheckRoles : List<MultiCheckRole>

    lateinit var rv: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var semanticRepository: SemanticRepository




    companion object {
        fun newInstance(): RSelectionPersonFragment {
            return RSelectionPersonFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.select_person_real_layout, container, false)
        setup(view)
        print("onCreateView")
        setupUi()
        return view
    }



    // Todo:  The application may be doing too much work on its main thread. fix it
    private fun setupUi() {
        alertDialog.show()

        semanticRepository.getAvailableRoles().enqueue(object : Callback<List<Role2>> {

            override fun onResponse(call: Call<List<Role2>>, response: Response<List<Role2>>) {

                semanticRepository.getAvailablePeople().enqueue(object : Callback<List<Person2>> {

                    override fun onResponse(call: Call<List<Person2>>, response2: Response<List<Person2>>) {

                        mCheckRoles = semanticRepository.getMultiCheckRoles2(response.body()?: listOf(), response2.body()?: listOf())
                        items = mCheckRoles
                        mAdapter = MultiCheckRoleAdapter(mCheckRoles)
                        rv.adapter = mAdapter
                        rv.layoutManager = layoutManager
                        rv.setHasFixedSize(true)
                        rv.adapter = mAdapter
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
                        alertDialog.dismiss()
                    }

                    override fun onFailure(call: Call<List<Person2>>, t: Throwable) {
                        TODO()
                    }
                })





            }
            override fun onFailure(call: Call<List<Role2>>, t: Throwable) {
                TODO()
            }
        })












    }

    private fun setup(view: View) {
        rv = view.findViewById(R.id.recycler_view)
        layoutManager = LinearLayoutManager(context)
        semanticRepository = SemanticRepository.getInstance(SemanticApiService.create())

        progress = AlertDialog.Builder(context)
        progress.setCancelable(false)
        progress.setView(R.layout.loading_dialog_layout)
        alertDialog = progress.create()
        alertDialog.setCancelable(true)
        alertDialog.setOnCancelListener {
            navigateToFragment(ErrorFragment(), true)
        }


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
//        queue.cancelAll(this)
//        VolleyLog.e("Error: ", "Request Cancelado")

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