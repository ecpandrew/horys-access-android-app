package com.example.KLSDinfo.Adapters.SelectionAdapters
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.KLSDinfo.Adapters.ViewHolders.MultiCheckPersonViewHolder
import com.example.KLSDinfo.Adapters.ViewHolders.RoleViewHolder
import com.example.KLSDinfo.Models.MultiCheckRole
import com.example.KLSDinfo.Models.Person
import com.example.KLSDinfo.R
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup


class MultiCheckRoleAdapter(groups: List <MultiCheckRole>) :
    CheckableChildRecyclerViewAdapter<RoleViewHolder, MultiCheckPersonViewHolder>(groups) {

    override fun onCreateCheckChildViewHolder(parent: ViewGroup, viewType: Int): MultiCheckPersonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_multicheck_person, parent, false)
        return MultiCheckPersonViewHolder(view)
    }

    override fun onBindCheckChildViewHolder(
        holder: MultiCheckPersonViewHolder, position: Int,
        group: CheckedExpandableGroup, childIndex: Int
    ) {
        val person = group.items[childIndex] as Person
        holder.setArtistName(person.name)
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
    }


}