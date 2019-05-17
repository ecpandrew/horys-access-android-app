package com.example.KLSDinfo.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.KLSDinfo.Adapters.HistoricMethodsAdapter
import com.example.KLSDinfo.Adapters.RealMethodsAdapter
import com.example.KLSDinfo.Models.Method
import com.example.KLSDinfo.R

class HistoryFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var methodsAdapter: HistoricMethodsAdapter
    private var items: MutableList<Method> = mutableListOf()

    companion object {
        fun newInstance(): HistoryFragment{
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
        items.add(Method("Houve Encontro?","Exibe todos os encontros em que grupo de pessoas(duas ou mais) estiveram em um intervalo de tempo",R.drawable.ic_location_on_black_24dp))
        items.add(Method("Tempo passado com grupos de pessoas","Exibe quanto tempo uma pessoa passou em encontros com um grupo de pessoas(duas ou mais) em um intervalo de tempo",R.drawable.ic_location_on_black_24dp))
        items.add(Method("Verificar Histórico de Pessoas","Exibe todos os locais visitados por pessoa(as) e a duração dos encontros",R.drawable.ic_location_on_black_24dp))
        items.add(Method("Verificar Histórico de Espaço Físico","Exibe todas as pessoas que estiveram no espaço físico selecionado e a duração dos encontros",R.drawable.ic_location_on_black_24dp))


    }

    private fun initComponents(view: View) {

        recyclerView = view.findViewById(R.id.historyRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context,1)
        recyclerView.setHasFixedSize(true)
        addItems()
        methodsAdapter = HistoricMethodsAdapter(context!!, items)
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