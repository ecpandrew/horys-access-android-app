package com.example.KLSDinfo.Historic.MainFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.KLSDinfo.Historic.adapters.HistoryMethodsAdapter
import com.example.KLSDinfo.Models.Method
import com.example.KLSDinfo.R

class HistoryFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var methodsAdapter: HistoryMethodsAdapter
    private var items: MutableList<Method> = mutableListOf()

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        print("onCreateView")
        val view: View = inflater.inflate(R.layout.main_history_layout, container, false)
        initComponents(view)
        return view
    }


    private fun addItems(){
        items.clear()
        items.add(Method("Group History","Given a group and a time interval this section display how many encounters the group had and the time each person spent with the group.",R.drawable.ic_hierarchical_structure))
        items.add(Method("People History","Given a group and a time interval this section display the places where each person was and for how long they have been there.",R.drawable.ic_analysis))
        items.add(Method("Physical Space History","Given a physical space and a time interval this section displays the people who visited that physical space and/or the physical spaces within it.",R.drawable.ic_enterprise))
    }

    private fun initComponents(view: View) {
        recyclerView = view.findViewById(R.id.historyRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context,1)
        recyclerView.setHasFixedSize(true)
        addItems()
        methodsAdapter = HistoryMethodsAdapter(context!!, items)
        recyclerView.adapter = methodsAdapter
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

    fun print(msg: String){
        Log.d("Lifecycle", "HistoryFragment: $msg")
    }



}