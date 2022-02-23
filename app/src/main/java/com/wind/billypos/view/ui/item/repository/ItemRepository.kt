package com.wind.billypos.view.ui.item.repository

import com.wind.billypos.data.dao.ItemDao
import com.wind.billypos.data.local.model.LocalCategory
import com.wind.billypos.data.local.model.LocalItem
import com.wind.billypos.data.local.model.LocalSubCategory
import com.wind.billypos.data.remote.api.ItemApi
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.item.RemoteItemResponse
import io.reactivex.Maybe

class ItemRepository(private val itemDao: ItemDao, private val itemApi: ItemApi) {

    private val billyPosMapper = BillyPosMapper()

    fun getItems(): Maybe<List<LocalItem>> = itemDao.getItems()

    fun searchItems(input: String?): Maybe<List<LocalItem>> = itemDao.searchItems(input = input)

    fun getSubCategoriesByCatId(categoryId: Long?): Maybe<List<LocalSubCategory>> =
        itemDao.getSubCategoriesByCatId(categoryId = categoryId)

    fun getCategoryByItemId(idCategory: Long?): Maybe<String> =
        itemDao.getCategoryByItemId(idCategory = idCategory)

    fun getSubCategoryByItemId(idSubCategory: Long?): Maybe<String> =
        itemDao.getSubCategoryByItemId(idSubCategory = idSubCategory)

    fun getCategories(): Maybe<List<LocalCategory>> = itemDao.getCategories()

    fun getCategories(id: Long?): Maybe<List<LocalCategory>> = itemDao.getCategories()

    fun getItemCategory(id: Long?): Maybe<LocalCategory> = itemDao.getItemCategory(id = id)

    fun getItemSubCategory(id: Long?): Maybe<LocalSubCategory> = itemDao.getItemSubCategory(id = id)

    fun insertItem(item: LocalItem): Maybe<LocalItem> =
        itemDao.insert(item = item).flatMap {
            findItemById(id = it)
        }

    fun updateItem(item: LocalItem): Maybe<Int> = itemDao.update(item = item)

    fun sendItem(item: LocalItem): Maybe<RemoteItemResponse> =
        itemApi.sendItem(item = billyPosMapper.itemMapper.toRemoteModel(item))


    private fun findItemById(id: Long): Maybe<LocalItem> = itemDao.findItemById(id = id)

    fun update(item: LocalItem): Maybe<LocalItem> = itemDao.update(item = item).flatMap {
        findItemById(id = item.id)
    }
}