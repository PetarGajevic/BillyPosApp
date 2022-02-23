package com.wind.billypos.utils

import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.model.TransactionHead
import timber.log.Timber


fun sameTaxes(items: List<TransactionBody>): List<TransactionHead.SameTax> {
    val sameTaxes = mutableListOf<TransactionHead.SameTax>()
    items.map { item ->
        Timber.e("Same taxes Items: %s" , item)
        if (!containsVatRate(item.vatRate!!, sameTaxes)) {
            sameTaxes += TransactionHead.SameTax(
                1,
                item.total, //item.unitPrice?.times(item.quantity!!),
                item.vatAmount,
                item.vatRate
            )
        } else {
            sameTaxes.find { it.vatRate == item.vatRate }?.let {
                it.numOfItems = it.numOfItems?.inc()
                it.priceBefVAT = it.priceBefVAT?.plus(item.total!!) //it.priceBefVAT?.plus(item.unitPrice?.times(item.quantity!!)!!)
                it.vatAmt = it.vatAmt?.plus(item.vatAmount!!)
            }
        }
    }
    return sameTaxes
}

fun containsVatRate(vatRate: Double, sameTax: List<TransactionHead.SameTax>): Boolean {
    sameTax.map {
        if(vatRate == it.vatRate) {
            return true
        }
    }
    return false
}