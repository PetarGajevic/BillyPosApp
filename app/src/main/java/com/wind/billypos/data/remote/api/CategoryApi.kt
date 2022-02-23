package com.wind.billypos.data.remote.api

import com.wind.billypos.data.remote.model.subcategory.RemoteSubCategory
import com.wind.billypos.data.remote.model.category.RemoteCategory
import com.wind.billypos.data.remote.model.category.RemoteCategoryCreateResponse
import com.wind.billypos.data.remote.model.category.RemoteCategoryUpdateResponse
import com.wind.billypos.data.remote.model.subcategory.RemoteSubCategoryCreateResponse
import com.wind.billypos.data.remote.model.subcategory.RemoteSubCategoryUpdateResponse
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CategoryApi {

    @POST("v1/categories")
    fun sendCategory(@Body category: RemoteCategory): Maybe<RemoteCategoryCreateResponse>

    @PUT("v1/categories/{id}")
    fun updateCategory(
        @Body category: RemoteCategory,
        @Path("id") id: Long?
    ): Maybe<RemoteCategoryUpdateResponse>

    @POST("v1/categories/{categoryId}/subcategories")
    fun sendSubCategory(
        @Body subcategory: RemoteSubCategory,
        @Path("categoryId") categoryId: Long?
    ): Maybe<RemoteSubCategoryCreateResponse>

    @PUT("v1/categories/{categoryId}/subcategories/{subcategoryId}")
    fun updateSubCategory(
        @Body subcategory: RemoteSubCategory,
        @Path("categoryId") categoryId: Long?,
        @Path("subcategoryId") subcategoryId: Long?
    ): Maybe<RemoteSubCategoryUpdateResponse>


}