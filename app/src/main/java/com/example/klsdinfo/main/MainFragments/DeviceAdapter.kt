package com.example.klsdinfo.main.MainFragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.klsdinfo.R

class DeviceAdapter(
    private val context: Context,
    private val items: MutableList<Device>) : RecyclerView.Adapter<DeviceAdapter.AdapterViewHolder>()

{
    private var onClickLister: OnClickListener? = null



    fun setOnClickListener(ocl : OnClickListener){
        this.onClickLister = ocl

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.device_item, parent, false)
        return AdapterViewHolder(itemView)

    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val device: Device = items[position]




        holder.deviceEmail.text = device.email

        holder.linearLayout.setOnClickListener {
            if(onClickLister == null){
                return@setOnClickListener
            }else{
                onClickLister!!.onItemClick(it, device, position)
            }
        }

        holder.linearLayout.setOnLongClickListener {
            if(onClickLister == null){
                return@setOnLongClickListener false
            }else{
                onClickLister!!.onItemLongClick(it, device, position)
                return@setOnLongClickListener true
            }
        }


    }




    class AdapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val linearLayout: LinearLayout = itemView.findViewById(R.id.linear_layout)
        val deviceEmail: TextView = itemView.findViewById(R.id.email)

    }


    interface OnClickListener {

        fun onItemClick(view: View, obj: Device, pos: Int)

        fun onItemLongClick(view: View, obj: Device, pos: Int)

    }




}

