package com.wind.billypos.view.ui.category.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.ui.category.repository.CategoryRepository
import timber.log.Timber

class CategoryViewModel(private val categoryRepository: CategoryRepository): BaseViewModel() {

    val mutableLiveDataCategories = MutableLiveData<List<Category>>()
    val mutableLiveDataUpdateCategory = MutableLiveData<Boolean>()

    private val viewBillyPosMapper = ViewBillyPosMapper()

    fun updateCategory(category: Category){
        addCompositeDisposable(
            categoryRepository.update(category = viewBillyPosMapper.viewCategoryMapper.toLocalModel(category)),
            onSuccess = {
                mutableLiveDataUpdateCategory.value = true
            },
            onError = {
                mutableLiveDataUpdateCategory.value = true
            }
        )
    }

    fun searchCategories(input: String?){
        addCompositeDisposable(
            categoryRepository.searchCategories(input = input).map {
                 viewBillyPosMapper.viewCategoryMapper.toListOfModel(it)
            },
            onSuccess = {
                Timber.e("Lista %s", it)
                mutableLiveDataCategories.value = it
            },
            onError = {
                Timber.e("Error")
                mutableLiveDataCategories.value = listOf()
            }
        )
    }

    fun updateSubCategories(id: Long?) {
        addCompositeDisposable(
            categoryRepository.updateSubCategories(id = id),
            onSuccess = {
                Timber.e("Updejt")
            },
            onError = {
                Timber.e("Updejt error")
            }
        )
    }

}