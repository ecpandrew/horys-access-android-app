package com.example.KLSDinfo.Repositories

object InjectorUtils {


    private fun getSemanticRepository(): SemanticRepository{
        return SemanticRepository.getInstance(SemanticApiService.create())
    }


    fun provideListPersonViewModelFactory() : ListPersonViewModel{
        return ListPersonViewModel(getSemanticRepository())
    }


}