package com.wind.billypos.view.ui.item.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Item
import com.wind.billypos.view.ui.item.repository.ItemRepository

class ItemViewModel(private val itemRepository: ItemRepository): BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataItems = MutableLiveData<List<Item>>()

    fun getItems(){
        addCompositeDisposable(
            itemRepository.getItems().map {
                viewBillyPosMapper.viewItemMapper.toListOfModel(it)
            },
            onSuccess = {
                mutableLiveDataItems.value = it
            }
        )
    }


    fun searchItems(input: String?){
        addCompositeDisposable(
            itemRepository.searchItems(input = input).map {
                viewBillyPosMapper.viewItemMapper.toListOfModel(it)
            },
            onSuccess = {
                mutableLiveDataItems.value = it
            }
        )
    }

}