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
import com.example.KLSDinfo.R
import kotlinx.android.synthetic.main.table_one_layout.view.*




class TableOneDialog : DialogFragment() {



    lateinit var toggleSort: ImageButton
    lateinit var btnOptions: ImageButton
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
        btnOptions = view.findViewById(R.id.parent_options)
        toggleSort = view.findViewById(R.id.parent_sort_duration)

        toggleSort.setOnClickListener {

            when(SORT){
                "UP" -> {
                    toggleSort.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
                    SORT = "DOWN"
                }
                "DOWN" -> {
                    toggleSort.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
                    SORT = "UP"
                }
            }

        }

        btnOptions.setOnClickListener {


            val popup = PopupMenu(context, it.parent_options)

            popup.inflate(R.menu.menu_card)

            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.action_details -> {
                            Toast.makeText(context,"Detail", Toast.LENGTH_LONG).show()
                        }
                        R.id.action_log -> {
                            Toast.makeText(context,"Log", Toast.LENGTH_LONG).show()
                        }
                    }
                    return false
                }
            })
            popup.show()
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