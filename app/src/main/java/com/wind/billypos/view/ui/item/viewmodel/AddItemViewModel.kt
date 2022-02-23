package com.wind.billypos.view.ui.item.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.base.Error
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.CashBalance
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.Item
import com.wind.billypos.view.model.SubCategory
import com.wind.billypos.view.ui.item.repository.ItemRepository
import org.koin.android.ext.koin.ERROR_MSG
import timber.log.Timber

class AddItemViewModel(private val itemRepository: ItemRepository) : BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataSubCategories = MutableLiveData<List<SubCategory>>()
    val mutableLiveDataCategories = MutableLiveData<List<Category>>()
    val mutableLiveDataItem = MutableLiveData<Item>()
    val mutableLiveDataSync = MutableLiveData<Boolean>()


    fun setItem(item: Item){
        item.code = Error.INITIALIZE
        mutableLiveDataItem.value = item
    }

    fun getItem() = mutableLiveDataItem.value

    fun getSubcategories(category: Category) {
        addCompositeDisposable(
            itemRepository.getSubCategoriesByCatId(categoryId = category.id).map {
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

    fun insertItem(item: Item) {
        addCompositeDisposable(
            itemRepository.insertItem(item = viewBillyPosMapper.viewItemMapper.toLocalModel(item))
                .map {
                 viewBillyPosMapper.viewItemMapper.toModel(it)
                },
            onSuccess = {
                it.code = Error.SUCCESS
                mutableLiveDataItem.value = it
            },
            onError = {
                Timber.e("On Error item")
            }
        )
    }

    fun updateItem(item: Item?){
        Timber.e("Item %s", item)
        addCompositeDisposable(
            itemRepository.updateItem(item = viewBillyPosMapper.viewItemMapper.toLocalModel(item)),
            onSuccess = {

            },
            onError = {
                Timber.e("fail updejt")
            }
        )
    }

    fun sendItem(item: Item){
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
}