package com.example.KLSDinfo.Fragments.DialogFragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.example.KLSDinfo.Models.Person2
import com.example.KLSDinfo.Models.PhysicalSpace
import com.example.KLSDinfo.R
import kotlinx.android.synthetic.main.table_one_layout.view.*




class TableOneDialog : DialogFragment() {



    lateinit var toggleSort: ImageButton
    lateinit var btnOptions: ImageButton
    lateinit var childLinearLayout: LinearLayout
    lateinit var url: String
    var SORT: String = "UP"
    var list_of_items = arrayOf("Details", "LOG")
    val TAG: String = "FullScreenDialog"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.table_one_layout, container, false)
        val tool: Toolbar = view.findViewById(R.id.toolbar)
        tool.setNavigationIcon(R.drawable.ic_close_white_24dp)
        tool.setNavigationOnClickListener {
            cancelUpload()
        }
        childLinearLayout= view.findViewById(R.id.child_linear_layout)

        val bundle: Bundle? = arguments
        if (bundle == null || bundle.isEmpty){
            // Todo: Não há resultados
        }else{
            val listOfPhysicalSpace: List<PhysicalSpace>? = bundle.getParcelableArrayList("resources")
            if (listOfPhysicalSpace == null){
                // Todo: tratar isso dai
            }else{


                for (space in listOfPhysicalSpace ){
                    url = "http://smartlab.lsdi.ufma.br/semantic/api/physical_spaces/{id}/things"

                }




            }
        }












        return view
    }

    private fun cancelUpload() {
        dialog.dismiss()
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog

        if (dialog != null){
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)

        }
    }

}


//        toggleSort.setOnClickListener {
//
//            when(SORT){
//                "UP" -> {
//                    toggleSort.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
//                    SORT = "DOWN"
//                }
//                "DOWN" -> {
//                    toggleSort.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
//                    SORT = "UP"
//                }
//            }
//
//        }
