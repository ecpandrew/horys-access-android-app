package com.example.KLSDinfo.Adapters
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.KLSDinfo.R
class SimpleExpandableAdapter(
    private val context: Context,
    private val listTitle: List<String>,
    private val listItem: Map<String, List<String>>) : BaseExpandableListAdapter() {



    override fun getGroup(groupPosition: Int): Any {
        return listTitle[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val title: String = getGroup(groupPosition).toString()
        var cv = convertView
        if (cv == null) {
            cv = LayoutInflater.from(context).inflate(R.layout.expandable_group, null)
        }
        val textTitle: TextView = cv!!.findViewById(R.id.expListTitle)
        textTitle.setTypeface(null, Typeface.BOLD)
        textTitle.text = title
        return cv
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listItem.getValue(listTitle[groupPosition]).size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        Log.i("debug", "group: $groupPosition")
        Log.i("debug", "child: $childPosition")
        return listItem.getValue(listTitle[groupPosition])[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val title: String = getChild(groupPosition,childPosition).toString()
        var cv = convertView
        if (cv == null){
            cv = LayoutInflater.from(context).inflate(R.layout.expandable_list_item, null)
        }
        val textTitle: TextView = cv!!.findViewById(R.id.expandableListItem)
        textTitle.text = title
        return cv
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return listTitle.size
    }


}