package com.example.klsdinfo.repositories

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.klsdinfo.models.MultiCheckRole
import com.example.klsdinfo.models.Person2
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ListPersonViewModel(
   private val semanticRepository: SemanticRepository, application: Application) : AndroidViewModel(application), LifecycleObserver{


    val mPeople = MutableLiveData<List<Person2>>().apply { value = emptyList() }
    val loadingVisibility = MutableLiveData<Boolean>().apply { value = false }





    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun load(){
        loadingVisibility.postValue(true)
        semanticRepository.getAvailablePeople({
                 mPeople.postValue(it)
                 loadingVisibility.value = true
            Log.e("debug", it.toString())

        }, {
          mPeople.postValue(listOf())
        })
    }



    fun getBtn(){

    }

    fun clearBtn(){

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