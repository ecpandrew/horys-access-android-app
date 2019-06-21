package com.example.KLSDinfo.Repositories

import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.ViewModel
import com.example.KLSDinfo.Models.MultiCheckRole
import com.example.KLSDinfo.Models.Person2
import com.example.KLSDinfo.Models.Role2
import com.example.KLSDinfo.UtilClasses.MultiCheckRoleAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPersonViewModel(
   semanticRepository: SemanticRepository) : ViewModel(){


    val people = semanticRepository.getAvailablePeople()
    val roles = semanticRepository.getAvailableRoles()

    var mCheckRoles : List<MultiCheckRole> = listOf()

    init {





    }

//    semanticRepository.getAvailableRoles().enqueue(object : Callback<List<Role2>> {
//
//        override fun onResponse(call: Call<List<Role2>>, response: Response<List<Role2>>) {
//
//            semanticRepository.getAvailablePeople().enqueue(object : Callback<List<Person2>> {
//
//                override fun onResponse(call: Call<List<Person2>>, response2: Response<List<Person2>>) {
//
//                    mCheckRoles = semanticRepository.getMultiCheckRoles2(response.body()?: listOf(), response2.body()?: listOf())
//                    items = mCheckRoles
//                    mAdapter = MultiCheckRoleAdapter(mCheckRoles)
//                    rv.adapter = mAdapter
//
//                    rv.layoutManager = layoutManager
//                    rv.setHasFixedSize(true)
//                    rv.adapter = mAdapter
//                    val obj = object: MultiCheckRoleAdapter.OnClickListener{
//                        override fun onClick(view: View, group: ExpandableGroup<*>, pos: Int) {
//                            mAdapter.toggleSelection(pos)
//                            mAdapter.getSelectedItems()
//                            val status: Boolean = (view as CheckBox).isChecked
//                            for(i in 0 until mCheckRoles.size){
//                                if(mCheckRoles[i].name == group.title){
//
//                                    run {
//                                        for(j in 0 until group.itemCount){
//                                            mAdapter.checkChild(status, i, j)
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    mAdapter.setCheckBoxOnClickListener(obj)
//                    alertDialog.dismiss()
//
//
//                }
//
//                override fun onFailure(call: Call<List<Person2>>, t: Throwable) {
//                    TODO()
//                }
//            })
//
//
//
//
//
//        }
//        override fun onFailure(call: Call<List<Role2>>, t: Throwable) {
//            TODO()
//        }
//    })


//    val mCheckRoles = semanticRepository.getMultiCheckRoles2(roles.value?: listOf(), people.value?: listOf())
}