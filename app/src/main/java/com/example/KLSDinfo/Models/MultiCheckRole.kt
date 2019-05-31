package com.example.KLSDinfo.Models

import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup


data class MultiCheckRole(val name: String, val persons: List<Person2>, val iconResId: Int) : MultiCheckExpandableGroup(name, persons){



    fun getCheckedGroup(): MultiCheckExpandableGroup{

        return MultiCheckExpandableGroup(name, persons)
    }

}