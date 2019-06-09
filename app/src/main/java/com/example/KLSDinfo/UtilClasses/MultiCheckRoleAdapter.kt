package com.example.KLSDinfo.UtilClasses
import android.util.Log
import android.util.SparseBooleanArray
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.KLSDinfo.Historic.adapters.PhysicalSpaceAdapter
import com.example.KLSDinfo.Models.MultiCheckRole
import com.example.KLSDinfo.Models.Person2
import com.example.KLSDinfo.R
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup




class MultiCheckRoleAdapter(
    groups: MutableList <MultiCheckRole>) :
    CheckableChildRecyclerViewAdapter<RoleViewHolder, MultiCheckPersonViewHolder>(groups) {

    private var onClickLister: OnClickListener? = null
    private val selectedItems: SparseBooleanArray = SparseBooleanArray()
    private var current_selected_idx = -1
    private val checkedArr: BooleanArray? = BooleanArray(groups.size)

    init {
        checkedArr!!.fill(false)
    }


    fun setCheckBoxOnClickListener(ocl: OnClickListener){
        this.onClickLister = ocl

    }


    override fun onCreateCheckChildViewHolder(parent: ViewGroup, viewType: Int): MultiCheckPersonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.expandable_list_item_check, parent, false)

        return MultiCheckPersonViewHolder(view)
    }

    override fun onBindCheckChildViewHolder(holder: MultiCheckPersonViewHolder, position: Int, group: CheckedExpandableGroup, childIndex: Int
    ) {
        val person = group.items[childIndex] as Person2
        holder.setArtistName(person.shortName)


    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_role, parent, false)
        return RoleViewHolder(view)
    }



    override fun onBindGroupViewHolder(
        holder: RoleViewHolder, flatPosition: Int,
        group: ExpandableGroup<*>
    ) {

        holder.setGenreTitle(group)



        holder.checkRole.setOnClickListener{
            if(onClickLister == null){
                return@setOnClickListener
            }else{
                onClickLister!!.onClick(it, group, holder.adapterPosition)
            }
        }



        toggleCheckedIcon(holder, flatPosition )


    }


    private fun toggleCheckedIcon(holder: RoleViewHolder, position: Int) {


        if (selectedItems.get(position, false)) {
            holder.checkRole.isChecked = true
//            holder.lyt_image.visibility = View.GONE
//            holder.lyt_checked.visibility = View.VISIBLE

            if (current_selected_idx == position) resetCurrentIndex()
        } else {
            holder.checkRole.isChecked = false
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

    fun getSelectedItems(): List<Int> {
        val items = ArrayList<Int>(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        Log.i("items", items.toString())
        return items
    }

    fun getBoolArray(): SparseBooleanArray {
        return selectedItems
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
    interface OnClickListener {


        fun onClick(view: View, group: ExpandableGroup<*>, pos: Int)
    }

}
