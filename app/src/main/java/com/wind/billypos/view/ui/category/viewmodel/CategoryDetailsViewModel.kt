package com.wind.billypos.view.ui.category.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.SubCategory
import com.wind.billypos.view.ui.category.repository.CategoryRepository
import timber.log.Timber


class CategoryDetailsViewModel(private val categoryRepository: CategoryRepository) :
    BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataCategory = MutableLiveData<Category>()
    val mutableLiveDataSubCategories = MutableLiveData<List<SubCategory>>()
    val mutableLiveDataSubUpdateSubCategory = MutableLiveData<Boolean>()
    val mutableLiveDataActivateSubCategory = MutableLiveData<Boolean>()
    val mutableLiveDataActivateCategory = MutableLiveData<Boolean>()


    fun getSubcategories(id: Long?) {
        addCompositeDisposable(
            categoryRepository.getSubcategories(id = id).map {
                viewBillyPosMapper.viewSubCategoryMapper.toListOfModel(it)
            },
            onSuccess = {
                Timber.e("Lista %s", it)
                mutableLiveDataSubCategories.value = it
            },
            onError = {
                Timber.e("Lista prazna")
                mutableLiveDataSubCategories.value = listOf()
            }
        )
    }

    fun updateSubCategory(subcategory: SubCategory) {
        addCompositeDisposable(
            categoryRepository.updateSubCategory(
                subcategory = viewBillyPosMapper.viewSubCategoryMapper.toLocalModel(
                    subcategory
                )
            ).map {
                viewBillyPosMapper.viewSubCategoryMapper.toModel(it)
            },
            onSuccess = {
                mutableLiveDataSubUpdateSubCategory.value = true
            },
            onError = {
                mutableLiveDataSubUpdateSubCategory.value = true
                Timber.e("ERROR")
            }
        )
    }

    fun searchSubCategories(input: String?, idCategory: Long?) {
        addCompositeDisposable(
            categoryRepository.searchSubCategories(input = input, idCategory = idCategory).map {
                viewBillyPosMapper.viewSubCategoryMapper.toListOfModel(it)
            },
            onSuccess = {
                mutableLiveDataSubCategories.value = it
            },
            onError = {
                mutableLiveDataSubCategories.value = listOf()
            }
        )
    }

    fun updateSubCategories(id: Long?, isActive: Boolean) {
        addCompositeDisposable(
            categoryRepository.updateSubCategories(id = id, isActive = isActive),
            onSuccess = {
                mutableLiveDataActivateSubCategory.value = true
            },
            onError = {
                mutableLiveDataActivateSubCategory.value = true
            }
        )
    }

    fun activateCategory(id: Long?) {
        addCompositeDisposable(
            categoryRepository.activateCategory(id = id),
            onSuccess = {
                mutableLiveDataActivateCategory.value = true
            },
            onError = {
                mutableLiveDataActivateCategory.value = true
            }
        )
    }

    fun findCategoryById(id: Long?){
        addCompositeDisposable(
            categoryRepository.findCategoryById(id = id).map {
                viewBillyPosMapper.viewCategoryMapper.toModel(it)
            },
            onSuccess = {
                mutableLiveDataCategory.value = it
            },
            onError = {
                Timber.e("ERROR CATEGORY")
            }
        )
    }

}