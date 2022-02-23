package com.wind.billypos.data.remote.model

import com.google.gson.annotations.SerializedName

abstract class RemoteError {
    @SerializedName("errorMessage")
    var errorMessage: String? = ""

    @SerializedName("statusCode")
    val status: Int = 0

    @SerializedName("message")
    val message: String = ""
}