package com.example.KLSDinfo.Historic.adapters

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.KLSDinfo.CustomTable.CustomTableDialog
import com.example.KLSDinfo.Models.AuxResource3
import com.example.KLSDinfo.R
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.table_three_rv_item.view.*
import java.text.NumberFormat
import java.util.*

class TableThreeAdapter(
    private val context: Context,
    private val items: MutableList<AuxResource3>) : RecyclerView.Adapter<TableThreeAdapter.ResourceThreeViewHolder>()

{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceThreeViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.table_three_rv_item, parent, false)
        return ResourceThreeViewHolder(itemView)

    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ResourceThreeViewHolder, position: Int) {
        val src: AuxResource3 = items[position]

        var count: Long = 0
        for (element in src.resources){
            count += element.getDuration()
        }
        val nf = NumberFormat.getInstance() // get instance
        nf.maximumFractionDigits = 2 // set decimal places
        val s: String = nf.format(count.toFloat() / 60)


        holder.nameTV.text = src.nome
        holder.numberRendzTV.text = ("""Encounters: """ + src.resources.size).trimIndent()
        holder.durationTV.text = ("Time spent with group: $s min")


        holder.btnDetail.setOnClickListener {
            val bundle = Bundle()
            var ref ="child_detail3"
            bundle.putString("ref", ref)
            bundle.putString("person", src.nome)
            bundle.putParcelableArrayList("resources", src.resources as ArrayList<out Parcelable>) // ??
            val dialog = CustomTableDialog()
            dialog.arguments = bundle
            val activity: AppCompatActivity = context as AppCompatActivity // ??
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog")
        }

        holder.btnLog.setOnClickListener {
            val bundle = Bundle()
            var ref ="log3"
            bundle.putString("ref", ref)
            bundle.putString("person", src.nome)
            bundle.putParcelableArrayList("resources", src.resources as ArrayList<out Parcelable>) // ??
            val dialog = CustomTableDialog()
            dialog.arguments = bundle
            val activity: AppCompatActivity = context as AppCompatActivity // ??
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog") }

//
    }




    class ResourceThreeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val nameTV: TextView = itemView.findViewById(R.id.main_title)
        val numberRendzTV: TextView = itemView.findViewById(R.id.main_rating)
        val durationTV: TextView = itemView.findViewById(R.id.main_duration)

        val btnDetail: MaterialButton = itemView.findViewById(R.id.btn_detail)
        val btnLog: MaterialButton = itemView.findViewById(R.id.btn_log)
    }








}

