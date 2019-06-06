package com.example.KLSDinfo.UtilClasses

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.KLSDinfo.Models.PhysicalSpace
import com.example.KLSDinfo.R
import com.mikhaellopez.circularimageview.CircularImageView

class PhysicalSpaceAdapter(
    private val context:Context,
    private var items: List<PhysicalSpace>,
    private val selectedItems: SparseBooleanArray = SparseBooleanArray()):
    RecyclerView.Adapter<PhysicalSpaceAdapter.SelectViewHolder>() {

    private var current_selected_idx = -1
    private var onClickLister: OnClickListener? = null




    fun setOnClickListener(ocl: OnClickListener){
        this.onClickLister = ocl
    }

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

        holder.from.text = location.name
        holder.email.text = "no data"
        holder.message.text = "no data"
        holder.date.text = "No date."
        holder.lyt_parent.isActivated = selectedItems.get(position, false)

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
        displayImage(holder, location)



    }


    private fun displayImage(holder: SelectViewHolder, location: PhysicalSpace) {
            holder.image.setImageResource(R.mipmap.ic_building)
    }

    private fun toggleCheckedIcon(holder: SelectViewHolder, position: Int) {

        if (selectedItems.get(position, false)) {
            holder.lyt_image.visibility = View.GONE
            holder.lyt_checked.visibility = View.VISIBLE
            if (current_selected_idx == position) resetCurrentIndex()
        } else {
            holder.lyt_checked.visibility = View.GONE
            holder.lyt_image.visibility = View.VISIBLE
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

    fun createLocation(position: Int): PhysicalSpace {

        return items[position]
    }

    class SelectViewHolder(view: View): RecyclerView.ViewHolder(view){
        val from: TextView = view.findViewById(R.id.from)
        val email: TextView = view.findViewById(R.id.email)
        val message: TextView = view.findViewById(R.id.message)
        val date: TextView = view.findViewById(R.id.date)
        val image: CircularImageView = view.findViewById(R.id.image)
        val lyt_checked: RelativeLayout = view.findViewById(R.id.lyt_checked)
        val lyt_image: RelativeLayout = view.findViewById(R.id.lyt_image)
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
    }



}