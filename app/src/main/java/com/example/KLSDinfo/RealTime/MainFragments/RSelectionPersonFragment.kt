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
import com.example.KLSDinfo.Volley.VolleySingleton
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


open class RSelectionPersonFragment : Fragment() {

    lateinit var mAdapter: MultiCheckRoleAdapter
    lateinit var items: MutableList<MultiCheckRole>
    lateinit var listOfRoles: List<Role2>
    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var queue: RequestQueue
    lateinit var mCheckRoles : MutableList<MultiCheckRole>


    companion object {
        fun newInstance(): RSelectionPersonFragment {
            return RSelectionPersonFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.select_person_real_layout, container, false)

        val rv : RecyclerView = view.findViewById(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(context)


        mAdapter = MultiCheckRoleAdapter(mutableListOf())

        rv.layoutManager = layoutManager
        rv.adapter = mAdapter



        val btnClear : Button = view.findViewById(R.id.buttonClear)
        btnClear.setOnClickListener {
            mAdapter.clearChoices()
        }



        val btnSend : Button = view.findViewById(R.id.buttonGet)

        btnSend.setOnClickListener {


            val seletedElements: ArrayList<Parcelable> = getSelectedElements()

            val bundle = Bundle()

            bundle.putParcelableArrayList("resources", seletedElements)


            val dialog = TableTwoFrag()
            dialog.arguments = bundle
            navigateToFragment(dialog, true)



        }
        print("onCreateView")


        // Todo; Arrumar o request Aninhado

        queue = VolleySingleton.getInstance(context).requestQueue
        val url = "http://smartlab.lsdi.ufma.br/semantic/api/persons"
        val url_roles = "http://smartlab.lsdi.ufma.br/semantic/api/roles"

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                VolleyLog.v("Response:%n %s", response)
                val lista: MutableList<Person2> = FakeRequest().getAllPersons(response)
                mCheckRoles = getMultiCheckRoles2(listOfRoles, lista)
                mAdapter = MultiCheckRoleAdapter(mCheckRoles)
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


            },
            Response.ErrorListener {
                VolleyLog.e("Error: ", it.message)
                alertDialog.dismiss()
                navigateToFragment(ErrorFragment(), true)

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
                navigateToFragment(ErrorFragment(), true)

            })

        // Add the request to the RequestQueue.
        // Todo: Eliminar a necessiade de 2 requests
        stringRequest.retryPolicy = DefaultRetryPolicy(10 * 1000, 3, 1.0f)
        roleRequest.retryPolicy = DefaultRetryPolicy(5 * 1000, 3, 1.0f)
        stringRequest.tag = this
        roleRequest.tag = this

        progress = AlertDialog.Builder(context)
        progress.setCancelable(false)
        progress.setView(R.layout.loading_dialog_layout)
        alertDialog = progress.create()
        alertDialog.setCancelable(true)
        alertDialog.setOnCancelListener {
            navigateToFragment(ErrorFragment(), true)
        }
        alertDialog.show()
        queue.add(roleRequest)


        return view
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
        VolleyLog.e("Error: ", "Request Cancelado")

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