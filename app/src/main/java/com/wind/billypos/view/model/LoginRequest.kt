package com.wind.billypos.view.model

import com.wind.billypos.base.Error

class LoginRequest(
    val username: String? = null,
    val password: String? = null,
    val deviceId: String? = null
    ): Error(){

    fun isEmailInvalid() = false //username.isNullOrEmpty()
    fun isPasswordInvalid() = false // password.isNullOrEmpty() || password.length < 2
    fun areDataCorrect() = true // password?.length!! >= 2 && password.isNotEmpty() && !isEmailInvalid()

}