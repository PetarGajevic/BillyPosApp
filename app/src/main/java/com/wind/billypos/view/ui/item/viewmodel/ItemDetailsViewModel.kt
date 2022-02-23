package com.wind.billypos.view.ui.item.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.Item
import com.wind.billypos.view.ui.item.repository.ItemRepository
import timber.log.Timber

class ItemDetailsViewModel(private val itemRepository: ItemRepository) : BaseViewModel() {


    val mutableLiveDataCategoryName = MutableLiveData<String>()
    val mutableLiveDataSubCategoryName = MutableLiveData<String>()
    val mutableLiveDataItem = MutableLiveData<Item>()
    val mutableLiveDataIsItemActive = MutableLiveData<Boolean>()

    private val viewBillyPosMapper = ViewBillyPosMapper()

    fun getCategoryByItemId(idCategory: Long?){
        addCompositeDisposable(
            itemRepository.getCategoryByItemId(idCategory = idCategory),
            onSuccess = {
                Timber.e("CATEGORY NAME %s", it)
                mutableLiveDataCategoryName.value = it
            },
            onError = {
                Timber.e("CATEGORY NAME ERROR")
            }
        )
    }


    fun getSubCategoryByItemId(idSubCategory: Long?){
        addCompositeDisposable(
            itemRepository.getSubCategoryByItemId(idSubCategory = idSubCategory),
            onSuccess = {
                Timber.e("SUBCATEGORY NAME %s", it)
                mutableLiveDataSubCategoryName.value = it
            },
            onError = {
                Timber.e("SUBCATEGORY NAME ERROR")
            }
        )
    }

    fun updateItem(item: Item){
        addCompositeDisposable(
            itemRepository.update(item = viewBillyPosMapper.viewItemMapper.toLocalModel(item)).map{
                 viewBillyPosMapper.viewItemMapper.toModel(it)
            },
            onSuccess = {
                mutableLiveDataItem.value = it
                mutableLiveDataIsItemActive.value = it.isActive!!
            }
        )
    }


}