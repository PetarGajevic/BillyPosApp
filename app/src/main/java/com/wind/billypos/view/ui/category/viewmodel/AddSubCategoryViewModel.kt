package com.wind.billypos.view.ui.category.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.base.Error
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.SubCategory
import com.wind.billypos.view.ui.category.repository.SubCategoryRepository
import timber.log.Timber

class AddSubCategoryViewModel(private val subCategoryRepository: SubCategoryRepository): BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataSubCategory = MutableLiveData<SubCategory>()
    val mutableLiveDataSyncSubCategory = MutableLiveData<String>()

    fun setSubCategory(subcategory: SubCategory){
        subcategory.code = Error.INITIALIZE
        mutableLiveDataSubCategory.value = subcategory
    }

    fun insertSubCategory(subcategory: SubCategory) {
        addCompositeDisposable(
            subCategoryRepository.insertSubCategory(
                subcategory = viewBillyPosMapper.viewSubCategoryMapper.toLocalModel(
                    subcategory
                )
            ).map {
                viewBillyPosMapper.viewSubCategoryMapper.toModel(it)
            },
            onSuccess = {
                mutableLiveDataSubCategory.value = it
                Timber.e("On Success %s", it)
            },
            onError = {
                Timber.e("Error inserta")
            }
        )
    }

    fun sendSubCategory(subcategory: SubCategory){
        addCompositeDisposable(
            subCategoryRepository.sendSubCategory(subcategory = viewBillyPosMapper.viewSubCategoryMapper.toLocalModel(subcategory)),
            onSuccess = {
                Timber.e("SUCCESS SUBCATEGORY %s", it)
                mutableLiveDataSyncSubCategory.value = it.status ?: "ERROR"
            },
            onError = {
                Timber.e("ERROR SUBCATEGORY")
                mutableLiveDataSyncSubCategory.value = "ERROR"
            }
        )
    }
}