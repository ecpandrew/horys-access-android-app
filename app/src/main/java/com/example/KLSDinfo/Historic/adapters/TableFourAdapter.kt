package com.example.KLSDinfo.Historic.adapters

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.KLSDinfo.CustomTable.CustomTableDialog
import com.example.KLSDinfo.Models.AuxResource4
import com.example.KLSDinfo.R
import kotlinx.android.synthetic.main.table_three_rv_item.view.*
import java.util.ArrayList

class TableFourAdapter(
    private val context: Context,
    private val items: MutableList<AuxResource4>) : RecyclerView.Adapter<TableFourAdapter.ResourceFourViewHolder>()

{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceFourViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.table_three_rv_item, parent, false)
        return ResourceFourViewHolder(itemView)

    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ResourceFourViewHolder, position: Int) {
        val src: AuxResource4 = items[position]


//        var count: Long = 0
//        for (element in src.resources){
//            count += element.getDuration()
//        }
//        val nf = NumberFormat.getInstance() // get instance
//        nf.maximumFractionDigits = 2 // set decimal places
//        val s: String = nf.format(count.toFloat() / 3600)


        holder.nameTV.text = src.name
        holder.numberRendzTV.text = ("Places Visited: ${src.getplacesCount()}")
        holder.durationTV.text = ("Total Time Elapsed: ${src.getDuration()}")


        holder.optionsIB.setOnClickListener {
            val popup = PopupMenu(context, it.main_options)
            popup.inflate(R.menu.menu_card)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_details -> {
                        val bundle = Bundle()
                        var ref ="child_detail4"
                        bundle.putString("ref", ref)
                        bundle.putString("person", src.name)
                        bundle.putParcelableArrayList("resources", src.resources as ArrayList<out Parcelable>) // ??
                        val dialog = CustomTableDialog()
                        dialog.arguments = bundle
                        val activity: AppCompatActivity = context as AppCompatActivity // ??
                        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                        dialog.show(transaction, "FullScreenDialog")



                    }
                    R.id.action_log -> {
                        val bundle = Bundle()
                        var ref ="child_log4"
                        bundle.putString("ref", ref)
                        bundle.putString("person", src.name)
                        bundle.putParcelableArrayList("resources", src.resources as ArrayList<out Parcelable>) // ??
                        val dialog = CustomTableDialog()
                        dialog.arguments = bundle
                        val activity: AppCompatActivity = context as AppCompatActivity // ??
                        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                        dialog.show(transaction, "FullScreenDialog")
                    }
                }
                false
            }
            popup.show()
        }



    }




    class ResourceFourViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val nameTV: TextView = itemView.findViewById(R.id.main_title)
        val numberRendzTV: TextView = itemView.findViewById(R.id.main_rating)
        val durationTV: TextView = itemView.findViewById(R.id.main_duration)
        val optionsIB: ImageButton = itemView.findViewById(R.id.main_options)
    }







}

