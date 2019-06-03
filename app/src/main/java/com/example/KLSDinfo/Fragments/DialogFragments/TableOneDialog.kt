package com.example.KLSDinfo.Fragments.DialogFragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.VolleyLog
import com.example.KLSDinfo.Models.Person2
import com.example.KLSDinfo.Models.PhysicalSpace
import com.example.KLSDinfo.R
import com.example.KLSDinfo.Volley.VolleySingleton
import kotlinx.android.synthetic.main.table_one_layout.view.*




class TableOneDialog : Fragment() {



    lateinit var toggleSort: ImageButton
    lateinit var btnOptions: ImageButton
    lateinit var childLinearLayout: LinearLayout
    lateinit var url: String
    var SORT: String = "UP"
    var list_of_items = arrayOf("Details", "LOG")
    val TAG: String = "FullScreenDialog"
    private lateinit var queue: RequestQueue
    lateinit var progress: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog


    companion object {
        fun newInstance(): TableOneDialog {
            return TableOneDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.table_one_layout, container, false)

        queue = VolleySingleton.getInstance(context).requestQueue


        childLinearLayout= view.findViewById(R.id.child_linear_layout)

        val bundle: Bundle? = arguments
        if (bundle == null || bundle.isEmpty){
            // Todo: Não há resultados
        }else{
            val listOfPhysicalSpace: List<PhysicalSpace>? = bundle.getParcelableArrayList("resources")
            if (listOfPhysicalSpace == null){
                // Todo: tratar isso dai
            }else{


                progress = AlertDialog.Builder(context)
                progress.setCancelable(false)
                progress.setView(R.layout.loading_dialog_layout)
                alertDialog = progress.create()
                alertDialog.show()




                for (space in listOfPhysicalSpace ){
                    url = "http://smartlab.lsdi.ufma.br/semantic/api/physical_spaces/{id}/things"

                }




            }
        }












        return view
    }

    override fun onStop() {
        super.onStop()
        queue.cancelAll(this)
        VolleyLog.e("Error: ", "Request Cancelado")

    }



}
