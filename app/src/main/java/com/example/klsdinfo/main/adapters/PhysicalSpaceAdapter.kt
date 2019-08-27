package com.example.klsdinfo.main.adapters

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.klsdinfo.R
import com.example.klsdinfo.data.models.PhysicalSpace

class PhysicalSpaceAdapter(
    private val context:Context,
    private var items: List<PhysicalSpace>,
    private val selectedItems: SparseBooleanArray = SparseBooleanArray()):
    RecyclerView.Adapter<PhysicalSpaceAdapter.SelectViewHolder>() {

    private var current_selected_idx = -1

    private var onClickLister: OnClickListener? = null

//    private var onCheckBoxClickListener: OnClickListener? = null





    fun setOnClickListener(ocl: OnClickListener){
        this.onClickLister = ocl
    }

//    fun setOnCheckBoxListener(ocl:OnClickListener){
//        this.onCheckBoxClickListener = ocl
//    }

    fun setItems(i: List<PhysicalSpace>){
        this.items = i
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item,parent,false)
        return SelectViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
        val location: PhysicalSpace = items[position]

        holder.name.text = location.name
        if (location.children == null) holder.nChilds.text = ("0 children found")
        else holder.nChilds.text = ("${location.children.size} children found")


//        holder.lyt_parent.isActivated = selectedItems.get(position, false)



        holder.checkBox.setOnClickListener {
            if(onClickLister == null){
                return@setOnClickListener
            }else{
                onClickLister!!.onCheckBoxClick(it, location, position)
            }
        }




        holder.lyt_parent.setOnClickListener {
            if(onClickLister == null){
                return@setOnClickListener
            }else{
                onClickLister!!.onItemClick(it, location, position)
            }
        }


        holder.lyt_parent.setOnLongClickListener {

            if(onClickLister == null){
                return@setOnLongClickListener false
            }else{
                onClickLister!!.onItemLongClick(it, location, position)
                return@setOnLongClickListener true
            }
        }

        toggleCheckedIcon(holder, position)
//        displayImage(holder, location)



    }

//
//    private fun displayImage(holder: SelectViewHolder, location: PhysicalSpace) {
//            holder.image.setImageResource(R.mipmap.ic_building)
//    }

    private fun toggleCheckedIcon(holder: SelectViewHolder, position: Int) {


        if (selectedItems.get(position, false)) {
            holder.checkBox.isChecked = true
//            holder.lyt_image.visibility = View.GONE
//            holder.lyt_checked.visibility = View.VISIBLE

            if (current_selected_idx == position) resetCurrentIndex()
        } else {
            holder.checkBox.isChecked = false
//            holder.lyt_checked.visibility = View.GONE
//            holder.lyt_image.visibility = View.VISIBLE
            if (current_selected_idx == position) resetCurrentIndex()
        }

    }

    private fun resetCurrentIndex() {
        current_selected_idx = -1
    }

    internal fun getSelectedItemCount(): Int {
        return selectedItems.size()
    }
    internal fun clearSelections() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    internal fun toggleSelection(pos: Int) {
        current_selected_idx = pos
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos)
        } else {
            selectedItems.put(pos, true)
        }
        notifyItemChanged(pos)
    }



    class SelectViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView = view.findViewById(R.id.physical_space_name)
        val nChilds: TextView = view.findViewById(R.id.childsTV)
        val checkBox: CheckBox = view.findViewById(R.id.physical_space_select)
        val lyt_parent: View = view.findViewById(R.id.lyt_parent)


    }


    fun getSelectedItems(): List<Int> {
        val items = ArrayList<Int>(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        return items
    }
    interface OnClickListener {
        fun onItemClick(view: View, obj: PhysicalSpace, pos: Int)

        fun onItemLongClick(view: View, obj: PhysicalSpace, pos: Int)

        fun onCheckBoxClick(view: View, obj: PhysicalSpace, pos: Int)
    }



}