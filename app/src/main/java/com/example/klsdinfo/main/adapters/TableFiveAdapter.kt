package com.example.klsdinfo.main.adapters

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.klsdinfo.R
import com.example.klsdinfo.data.models.AuxResource5
import com.example.klsdinfo.main.MainFragments.CustomTableFragment
import com.google.android.material.button.MaterialButton
import java.util.*

class TableFiveAdapter(
    private val context: Context,
    private val items: MutableList<AuxResource5>
) : RecyclerView.Adapter<TableFiveAdapter.ResourceFourViewHolder>()

{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceFourViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.table_three_rv_item, parent, false)
        return ResourceFourViewHolder(itemView)

    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ResourceFourViewHolder, position: Int) {
        val src: AuxResource5 = items[position]


        holder.nameTV.text = src.name
        holder.numberRendzTV.text = ("People Found: ${src.getPersonCount()}")
        holder.durationTV.text = ("Time Elapsed: ${src.getDuration()/60} min")


        holder.btnDetail.setOnClickListener {
            val bundle = Bundle()
            var ref ="child_detail5"
            bundle.putString("ref", ref)
            bundle.putString("person", src.name)
            bundle.putParcelableArrayList("resources", src.resources as ArrayList<out Parcelable>) // ??
            val dialog = CustomTableFragment()
            dialog.arguments = bundle
            val activity: AppCompatActivity = context as AppCompatActivity // ??
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog")
        }

        holder.btnLog.setOnClickListener {
            val bundle = Bundle()
            var ref ="child_log5"
            bundle.putString("ref", ref)
            bundle.putString("person", src.name)
            bundle.putParcelableArrayList("resources", src.resources as ArrayList<out Parcelable>) // ??
            val dialog = CustomTableFragment()
            dialog.arguments = bundle
            val activity: AppCompatActivity = context as AppCompatActivity // ??
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog")
        }

    }




    class ResourceFourViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val nameTV: TextView = itemView.findViewById(R.id.main_title)
        val numberRendzTV: TextView = itemView.findViewById(R.id.main_rating)
        val durationTV: TextView = itemView.findViewById(R.id.main_duration)
        val btnDetail: MaterialButton = itemView.findViewById(R.id.btn_detail)
        val btnLog: MaterialButton = itemView.findViewById(R.id.btn_log)
    }







}

