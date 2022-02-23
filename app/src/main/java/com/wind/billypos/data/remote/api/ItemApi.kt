package com.wind.billypos.data.remote.api

import com.wind.billypos.data.remote.model.item.RemoteItem
import com.wind.billypos.data.remote.model.item.RemoteItemResponse
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.POST

interface ItemApi {

    @POST("v1/item")
    fun sendItem(@Body item: RemoteItem): Maybe<RemoteItemResponse>

}