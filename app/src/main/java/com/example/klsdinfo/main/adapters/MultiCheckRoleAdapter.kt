package com.example.klsdinfo.main.adapters
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.*
import com.example.klsdinfo.R
import com.example.klsdinfo.data.models.MultiCheckRole
import com.example.klsdinfo.data.models.Person2
import com.example.klsdinfo.data.models.Role
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder


class MultiCheckRoleAdapter(
    private var group: List <MultiCheckRole>) :
    CheckableChildRecyclerViewAdapter<MultiCheckRoleAdapter.RoleViewHolder, MultiCheckRoleAdapter.MultiCheckPersonViewHolder>(group) {

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expandable_list_item_check, parent, false)


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


    inner class MultiCheckPersonViewHolder(itemView: View) : CheckableChildViewHolder(itemView){


        private val childCheckedTextView: CheckedTextView = itemView.findViewById(R.id.list_item_multicheck_artist_name)

        override fun getCheckable(): Checkable {
            return childCheckedTextView
        }

        fun setArtistName(artistName: String) {
            childCheckedTextView.text = artistName
        }
    }

    inner class RoleViewHolder(itemView: View): GroupViewHolder(itemView) {




        val checkRole: CheckBox = itemView.findViewById(R.id.groupCheckBox)
        private val genreName: TextView = itemView.findViewById(R.id.list_item_genre_name)
        private val arrow: ImageView = itemView.findViewById(R.id.list_item_genre_arrow)


        fun setGenreTitle(role: ExpandableGroup<*>) {
            if (role is Role) {
                genreName.text = role.title
//            icon.setBackgroundResource(role.iconResId)
            }
            if (role is MultiCheckRole) {
                genreName.text = role.title
//            icon.setBackgroundResource(role.iconResId)
            }

        }





        override fun expand() {
            animateExpand()
        }

        override fun collapse() {
            animateCollapse()
        }

        private fun animateExpand() {
            val rotate = RotateAnimation(360f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotate.duration = 150
            rotate.fillAfter = true
            arrow.animation = rotate
        }

        private fun animateCollapse() {
            val rotate = RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotate.duration = 150
            rotate.fillAfter = true
            arrow.animation = rotate
        }

    }


}
