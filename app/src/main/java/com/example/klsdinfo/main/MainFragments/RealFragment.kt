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
import com.example.klsdinfo.main.adapters.RMethodsAdapter

class RealFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var methodsAdapter: RMethodsAdapter
    private var items: MutableList<Method> = mutableListOf()

    companion object {
        fun newInstance(): RealFragment {
            return RealFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        print("onCreateView")
        val view: View = inflater.inflate(R.layout.main_real_time_layout, container, false)
        initComponents(view)
        return view
    }




    private fun addItems(){

        items.clear()

        items.add(
            Method(
                "Check Physical Space",
                "Given a physical space this section displays all persons in encounters with the location or the locations within it.",
                R.drawable.ic_notification
            )
        )
        items.add(
            Method(
                "Find People",
                "Given a group this section displays the location of each person",
                R.drawable.ic_recruitment
            )
        )

    }

    private fun initComponents(view: View) {

        recyclerView = view.findViewById(R.id.realTimeRecyclerView)

        recyclerView.layoutManager = GridLayoutManager(context,1)

        recyclerView.setHasFixedSize(true)

        addItems()
        methodsAdapter = RMethodsAdapter(context!!, items)
        recyclerView.adapter = methodsAdapter



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

    private fun print(msg: String){
        Log.d("Lifecycle", "RealFragment: $msg")
    }



}


