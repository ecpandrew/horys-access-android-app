package com.example.klsdinfo.utilClasses
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.klsdinfo.models.Role
import com.example.klsdinfo.R
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