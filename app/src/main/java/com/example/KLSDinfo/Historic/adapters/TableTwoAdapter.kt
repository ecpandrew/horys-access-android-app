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
import com.example.KLSDinfo.UtilClasses.FullscreenDialogFragment
import com.example.KLSDinfo.Models.TableTwoResource
import com.example.KLSDinfo.R
import kotlinx.android.synthetic.main.table_three_rv_item.view.*
import java.text.NumberFormat
import java.util.ArrayList

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
        val s: String = nf.format(count.toFloat() / 3600)


        holder.nameTV.text = keys[position]
        holder.numberRendzTV.text = ("""Nº de pessoas encontradas: """ + src.size)
        holder.durationTV.text = ("Duração total: $s (h)")


        holder.optionsIB.setOnClickListener {
            val popup = PopupMenu(context, it.main_options)
            popup.inflate(R.menu.menu_card)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_details -> {
                        val bundle = Bundle()
                        bundle.putString("name", keys[position])
                        bundle.putParcelableArrayList("resources", src as ArrayList<out Parcelable>) // ??
                        val dialog = FullscreenDialogFragment()
                        dialog.arguments = bundle
                        val activity: AppCompatActivity = context as AppCompatActivity // ??
                        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                        dialog.show(transaction, "FullScreenDialog")

                    }
                    R.id.action_log -> {
                        val bundle = Bundle()
                        bundle.putString("name", keys[position])
                        bundle.putParcelableArrayList("resources", src as ArrayList<out Parcelable>) // ??
                        val dialog = FullscreenDialogFragment()
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




    class ResourceTwoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val nameTV: TextView = itemView.findViewById(R.id.main_title)
        val numberRendzTV: TextView = itemView.findViewById(R.id.main_rating)
        val durationTV: TextView = itemView.findViewById(R.id.main_duration)
        val optionsIB: ImageButton = itemView.findViewById(R.id.main_options)
    }








}

