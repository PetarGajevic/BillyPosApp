package com.wind.billypos.view.ui.category.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.base.Error
import com.wind.billypos.data.local.model.LocalCategory
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.ui.category.repository.CategoryRepository
import com.wind.billypos.view.ui.category.repository.SubCategoryRepository
import timber.log.Timber

class EditCategoryViewModel(private val categoryRepository: CategoryRepository): BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataCategory = MutableLiveData<Category>()
    val mutableLiveDataSyncCategory = MutableLiveData<String>()

    fun setCategory(category: Category){
        category.code = Error.INITIALIZE
        mutableLiveDataCategory.value = category
    }

    fun updateCategory(category: Category) {
        addCompositeDisposable(
            categoryRepository.updateCategory(
                category = viewBillyPosMapper.viewCategoryMapper.toLocalModel(
                    category
                )
            ).map {
                viewBillyPosMapper.viewCategoryMapper.toModel(it)
            },
            onSuccess = {
                mutableLiveDataCategory.value = it
                Timber.e("On Success %s", it)
            },
            onError = {
                Timber.e("Error inserta")
            }
        )
    }

    fun sendUpdateCategory(category: Category){
        addCompositeDisposable(
            categoryRepository.sendUpdateCategory(category = viewBillyPosMapper.viewCategoryMapper.toLocalModel(category)),
            onSuccess = {
                mutableLiveDataSyncCategory.value = it.status ?: "ERROR"
            },
            onError = {
                mutableLiveDataSyncCategory.value = "ERROR"
            }
        )
    }

}