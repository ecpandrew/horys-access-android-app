package com.example.KLSDinfo.Adapters.SelectionAdapters
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.KLSDinfo.Adapters.ViewHolders.PersonViewHolder
import com.example.KLSDinfo.Adapters.ViewHolders.RoleViewHolder
import com.example.KLSDinfo.Models.Role
import com.example.KLSDinfo.R
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter


class RoleAdapter(groups: List<ExpandableGroup<*>>) :
    ExpandableRecyclerViewAdapter<RoleViewHolder, PersonViewHolder>(groups) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_role, parent, false)
        return RoleViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_person, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindChildViewHolder(holder: PersonViewHolder, flatPosition: Int,
        group: ExpandableGroup<*>, childIndex: Int) {

        val person = (group as Role).items[childIndex]
        holder.setArtistName(person.name)

    }

    override fun onBindGroupViewHolder(
        holder: RoleViewHolder, flatPosition: Int,
        group: ExpandableGroup<*>
    ) {

        holder.setGenreTitle(group)
    }
}