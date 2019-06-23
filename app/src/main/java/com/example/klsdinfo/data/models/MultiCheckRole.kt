package com.example.klsdinfo.data.models

import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup


data class MultiCheckRole(val name: String, val persons: List<Person2>, val iconResId: Int) : MultiCheckExpandableGroup(name, persons){



    fun getCheckedGroup(): MultiCheckExpandableGroup{

        return MultiCheckExpandableGroup(name, persons)
    }
    fun getChildCount(): Int{
        return persons.size
    }

}