package com.example.klsdinfo.main.MainFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klsdinfo.R
import com.example.klsdinfo.data.models.Method
import com.example.klsdinfo.main.adapters.HistoryMethodsAdapter
import com.example.klsdinfo.main.adapters.SecurityMethodsAdapter

class SecurityFragment : Fragment(){


    private lateinit var recyclerView: RecyclerView
    private lateinit var methodsAdapter: SecurityMethodsAdapter
    private var items: MutableList<Method> = mutableListOf()


    private lateinit var ctx: Context

    companion object {
        fun newInstance(context: Context): SecurityFragment {
            val instance : SecurityFragment = SecurityFragment()
            instance.ctx = context;
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        print("onCreateView")
        val view: View = inflater.inflate(R.layout.main_security_layout, container, false)
        initComponents(view)
        return view
    }


    private fun initComponents(view: View) {
        recyclerView = view.findViewById(R.id.securityRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context,1)
        recyclerView.setHasFixedSize(true)
        addItems()
        methodsAdapter = SecurityMethodsAdapter(ctx, items)
        recyclerView.adapter = methodsAdapter
    }


    private fun addItems(){
        items.clear()
//        items.add(
//            Method(
//                "Group History",
//                "Given a group and a time interval this section display how many encounters the group had and the time each person spent with the group.",
//                R.drawable.ic_hierarchical_structure
//            )
//        )
        items.add(
            Method(
                "Generate CSR",
                "Generate the CSR of user.",
                R.drawable.ic_analysis
            )
        )
        items.add(
            Method(
                "Import Client/CA Certificates",
                "Import certificates.",
                R.drawable.ic_enterprise
            )
        )

        items.add(
            Method(
                "ACL",
                "Access Control List.",
                R.drawable.ic_enterprise
            )
        )
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

    fun print(msg: String){
        Log.d("Lifecycle", "HistoryFragment: $msg")
    }

}