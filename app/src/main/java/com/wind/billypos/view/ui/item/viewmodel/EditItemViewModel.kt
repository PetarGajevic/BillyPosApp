package com.wind.billypos.view.ui.item.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.base.Error
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.Item
import com.wind.billypos.view.model.SubCategory
import com.wind.billypos.view.ui.item.repository.ItemRepository
import timber.log.Timber

class EditItemViewModel(private val itemRepository: ItemRepository): BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataSubCategories = MutableLiveData<List<SubCategory>>()
    val mutableLiveDataCategories = MutableLiveData<List<Category>>()
    val mutableLiveDataItemCategory = MutableLiveData<Category?>()
    val mutableLiveDataItemSubCategory = MutableLiveData<SubCategory?>()
    val mutableLiveDataItem = MutableLiveData<Item>()
    val mutableLiveDataSync = MutableLiveData<Boolean>()

    fun setItem(item: Item){
        item.code = Error.INITIALIZE
        mutableLiveDataItem.value = item
    }

    fun getItem() = mutableLiveDataItem.value

    fun updateItem(item: Item?) {
        Timber.e("Item %s", item)
        addCompositeDisposable(
            itemRepository.updateItem(item = viewBillyPosMapper.viewItemMapper.toLocalModel(item)),
            onSuccess = {
                item?.code = Error.SUCCESS
                mutableLiveDataItem.value = item!!
            },
            onError = {
                Timber.e("On Error item")
            }
        )
    }

    fun sendItem(item: Item?){
        addCompositeDisposable(
            itemRepository.sendItem(item = viewBillyPosMapper.viewItemMapper.toLocalModel(item)),
            onSuccess = {
                Timber.e("Remote item response %s", it)
                mutableLiveDataSync.value = it.status == "OK"
            },
            onError = {
                mutableLiveDataSync.value = false
            }
        )
    }

    fun getCategories() {
        addCompositeDisposable(
            itemRepository.getCategories().map {
                viewBillyPosMapper.viewCategoryMapper.toListOfModel(it)
            },
            onSuccess = {
                mutableLiveDataCategories.value = it
            },
            onError = {
                mutableLiveDataCategories.value = listOf()
            }
        )
    }

    fun getSubcategories(category: Category) {
        addCompositeDisposable(
            itemRepository.getSubCategoriesByCatId(categoryId = category.id).map {
                viewBillyPosMapper.viewSubCategoryMapper.toListOfModel(it)
            },
            onSuccess = {
                Timber.e("Lista kategorije %s", it)
                mutableLiveDataSubCategories.value = it
            },
            onError = {
                mutableLiveDataSubCategories.value = listOf()
            }
        )
    }


    fun getSubcategoriesByCategoryId(categoryId: Long?) {
        addCompositeDisposable(
            itemRepository.getSubCategoriesByCatId(categoryId = categoryId).map {
                viewBillyPosMapper.viewSubCategoryMapper.toListOfModel(it)
            },
            onSuccess = {
                Timber.e("Lista Sub kategorije %s", it)
                mutableLiveDataSubCategories.value = it
            },
            onError = {
                mutableLiveDataSubCategories.value = listOf()
            }
        )
    }

    fun getItemCategory(id: Long?){
        addCompositeDisposable(
            itemRepository.getItemCategory(id = id).map {
               viewBillyPosMapper.viewCategoryMapper.toModel(it)
            },
            onSuccess = {
                mutableLiveDataItemCategory.value = it
            },
            onError = {
                mutableLiveDataItemCategory.value = null
            }
        )
    }

    fun getItemSubCategory(id: Long?){
        addCompositeDisposable(
            itemRepository.getItemSubCategory(id = id).map {
                viewBillyPosMapper.viewSubCategoryMapper.toModel(it)
            },
            onSuccess = {
                mutableLiveDataItemSubCategory.value = it
            },
            onError = {
                mutableLiveDataItemSubCategory.value = null
            }
        )
    }

}