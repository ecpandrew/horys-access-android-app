package com.example.KLSDinfo.RealTime.adapters

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
import com.example.KLSDinfo.CustomTable.CustomTableDialog
import com.example.KLSDinfo.Models.TableTwoResource
import com.example.KLSDinfo.R
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.*

class TableTwoAdapter(
    private val context: Context,
    private val items: MutableMap<String, List<TableTwoResource>>,
    private val keys: MutableList<String> = items.keys.toMutableList()) : RecyclerView.Adapter<TableTwoAdapter.ResourceTwoViewHolder>()

{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceTwoViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.table_two_rv_item, parent, false)
        return ResourceTwoViewHolder(itemView)

    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ResourceTwoViewHolder, position: Int) {
        val src:List<TableTwoResource> = items[keys[position]]!!

        var count: Long = 0
        for (element in src){
            count += element.duration
        }

        val nf = NumberFormat.getInstance() // get instance
        nf.maximumFractionDigits = 2 // set decimal places
        val s: String = nf.format(count.toFloat() / 60)


        holder.nameTV.text = keys[position]
        holder.numberRendzTV.text = ("""People Found """ + src.size)
        holder.durationTV.text = ("Time Elapsed: $s min")


        holder.buttonDetail.setOnClickListener {
            val bundle = Bundle()
            var ref ="detail2"
            bundle.putString("ref", ref)
            bundle.putString("person", keys[position])
            bundle.putString("name", keys[position])
            bundle.putParcelableArrayList("resources", src as ArrayList<out Parcelable>) // ??
            val dialog = CustomTableDialog()
            dialog.arguments = bundle
            val activity: AppCompatActivity = context as AppCompatActivity // ??
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog")
        }

//        holder.buttonLog.setOnClickListener {
//            val bundle = Bundle()
//            var ref ="child_log2"
//            bundle.putString("ref", ref)
//            bundle.putString("person", keys[position])
//            bundle.putString("name", keys[position])
//            bundle.putString("name", keys[position])
//            bundle.putParcelableArrayList("resources", src as ArrayList<out Parcelable>) // ??
//            val dialog = CustomTableDialog()
//            dialog.arguments = bundle
//            val activity: AppCompatActivity = context as AppCompatActivity // ??
//            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
//            dialog.show(transaction, "FullScreenDialog")
//        }

    }




    class ResourceTwoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val nameTV: TextView = itemView.findViewById(R.id.main_title)
        val numberRendzTV: TextView = itemView.findViewById(R.id.main_rating)
        val durationTV: TextView = itemView.findViewById(R.id.main_duration)
        val buttonDetail: MaterialButton = itemView.findViewById(R.id.btn_detail)
//        val buttonLog: MaterialButton = itemView.findViewById(R.id.btn_log)

    }








}

