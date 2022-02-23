package com.wind.billypos.view.model

import com.wind.billypos.base.Error


data class TransactionBodyWithItem(
    val body: TransactionBody? = null,
    val item: Item? = null
): Error()