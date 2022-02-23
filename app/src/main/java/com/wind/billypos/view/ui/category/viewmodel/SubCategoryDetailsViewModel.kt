package com.wind.billypos.view.ui.category.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.base.Error
import com.wind.billypos.data.local.model.LocalSubCategory
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.SubCategory
import com.wind.billypos.view.ui.category.repository.SubCategoryRepository
import timber.log.Timber

class SubCategoryDetailsViewModel(private val subCategoryRepository: SubCategoryRepository) :
    BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataSubCategory = MutableLiveData<SubCategory>()
    val mutableLiveDataSyncSubCategory = MutableLiveData<String>()

    fun setSubCategory(subcategory: SubCategory){
        subcategory.code = Error.INITIALIZE
        mutableLiveDataSubCategory.value = subcategory
    }

    fun updateSubCategory(subCategory: SubCategory) {
        addCompositeDisposable(
            subCategoryRepository.updateSubCategory(
                subcategory = viewBillyPosMapper.viewSubCategoryMapper.toLocalModel(
                    subCategory
                )
            ).map {
                viewBillyPosMapper.viewSubCategoryMapper.toModel(it)
            },
            onSuccess = {
                mutableLiveDataSubCategory.value = it
            },
            onError = {
                Timber.e("ERROR")
            }
        )
    }

    fun sendUpdateSubCategory(subcategory: SubCategory){
        addCompositeDisposable(
            subCategoryRepository.sendUpdateSubCategory(subcategory = viewBillyPosMapper.viewSubCategoryMapper.toLocalModel(subcategory)),
            onSuccess = {
                mutableLiveDataSyncSubCategory.value = it.status ?: "ERROR"
            },
            onError = {
                mutableLiveDataSyncSubCategory.value = "ERROR"
            }
        )
    }

}