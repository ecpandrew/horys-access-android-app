package com.example.KLSDinfo.UtilClasses
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.KLSDinfo.Models.MultiCheckRole
import com.example.KLSDinfo.Models.Role
import com.example.KLSDinfo.R
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder


class RoleViewHolder(itemView: View): GroupViewHolder(itemView) {




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
        val rotate = RotateAnimation(360f, 180f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 150
        rotate.fillAfter = true
        arrow.animation = rotate
    }

    private fun animateCollapse() {
        val rotate = RotateAnimation(180f, 360f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 150
        rotate.fillAfter = true
        arrow.animation = rotate
    }

}