package com.wind.billypos.base

import androidx.room.Ignore

/**
 * Created by Sinan Dizdarevic on 2019-05-31.
 */
abstract class Error {
    @Ignore var errorMessage: String? = ""
    @Ignore var message: String = ""
    @Ignore var code: Int = NO_STATUS

    fun hasChanges() = code != 0
    fun hasHttpError() = code in 400..499
    fun hasServerError() = code >= 500
    fun hasNetworkError() = code == -1
    fun isUnauthorized() = code == 401
    fun isLocked() = code == 423
    fun isVerified() = code == 409
    fun hasError() = code < 0
    fun isSuccess() = code in 100..400

    fun resetStatusCode() {
        code = NO_STATUS
    }

    companion object {

        const val NETWORK_ERROR = -1
        const val NO_STATUS = 0
        const val INITIALIZE = 1
        const val SUCCESS = 200
        const val ERROR = -100
    }
}