package com.wind.billypos.view.ui.category.repository

import com.wind.billypos.data.dao.CategoryDao
import com.wind.billypos.data.local.model.LocalCategory
import com.wind.billypos.data.local.model.LocalSubCategory
import com.wind.billypos.data.remote.api.CategoryApi
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.category.RemoteCategoryCreateResponse
import com.wind.billypos.data.remote.model.category.RemoteCategoryUpdateResponse
import com.wind.billypos.data.remote.model.subcategory.RemoteSubCategoryCreateResponse
import com.wind.billypos.data.remote.model.subcategory.RemoteSubCategoryUpdateResponse
import io.reactivex.Maybe

class SubCategoryRepository(
    private val categoryDao: CategoryDao,
    private val categoryApi: CategoryApi
) {

    private val billyPosMapper = BillyPosMapper()

    fun insertSubCategory(subcategory: LocalSubCategory): Maybe<LocalSubCategory> =
        categoryDao.insert(subcategory = subcategory).flatMap {
            findSubCategoryById(id = it)
        }

    fun updateSubCategory(subcategory: LocalSubCategory): Maybe<LocalSubCategory> =
        categoryDao.update(subcategory = subcategory).flatMap {
            findSubCategoryById(id = subcategory.id!!)
        }

    fun sendSubCategory(subcategory: LocalSubCategory): Maybe<RemoteSubCategoryCreateResponse> =
        categoryApi.sendSubCategory(
            subcategory = billyPosMapper.subCategoryMapper.toRemoteModel(subcategory),
            categoryId = subcategory.id_category
        )

    fun sendUpdateSubCategory(subcategory: LocalSubCategory): Maybe<RemoteSubCategoryUpdateResponse> =
        categoryApi.updateSubCategory(
            subcategory = billyPosMapper.subCategoryMapper.toRemoteModel(subcategory),
            categoryId = subcategory.id_category,
            subcategoryId = subcategory.id
        )

    private fun findSubCategoryById(id: Long): Maybe<LocalSubCategory> =
        categoryDao.findSubCategoryById(id = id)


}