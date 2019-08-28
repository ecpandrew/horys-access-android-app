package com.example.klsdinfo.main.adapters

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.klsdinfo.R
import com.example.klsdinfo.data.models.AuxResource3
import com.example.klsdinfo.main.MainFragments.CustomTableFragment
import com.google.android.material.button.MaterialButton
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



        holder.cardView.setOnClickListener {
            val bundle = Bundle()
            val ref ="group_data"
            bundle.putString("ref", ref)
            bundle.putString("person", src.nome)
            bundle.putParcelableArrayList("resources", src.resources as ArrayList<out Parcelable>) // ??
            val frag = CustomTableFragment()
            frag.arguments = bundle
            navigateToFragment(frag,true)
        }



        holder.btnDetail.setOnClickListener {
            val bundle = Bundle()
            var ref ="child_detail3"
            bundle.putString("ref", ref)
            bundle.putString("person", src.nome)
            bundle.putParcelableArrayList("resources", src.resources as ArrayList<out Parcelable>) // ??
            val dialog = CustomTableFragment()
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
            val dialog = CustomTableFragment()
            dialog.arguments = bundle
            val activity: AppCompatActivity = context as AppCompatActivity // ??
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog") }

//
    }

    fun navigateToFragment(fragToGo: Fragment, addToBackStack: Boolean = false){
        val activity: AppCompatActivity = context as AppCompatActivity // ??

        val transaction = activity.supportFragmentManager.beginTransaction()

        transaction.replace(R.id.fragment_container, fragToGo)

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        if(addToBackStack){
            transaction.addToBackStack(null) // Todo: verificar o ciclo de vida dos fragmentos
        }
        transaction.commit()
    }


    class ResourceThreeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val nameTV: TextView = itemView.findViewById(R.id.main_title)
        val numberRendzTV: TextView = itemView.findViewById(R.id.main_rating)
        val durationTV: TextView = itemView.findViewById(R.id.main_duration)

        val btnDetail: MaterialButton = itemView.findViewById(R.id.btn_detail)
        val btnLog: MaterialButton = itemView.findViewById(R.id.btn_log)
        val cardView : CardView = itemView.findViewById(R.id.cardView)
    }









}

