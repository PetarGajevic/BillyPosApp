package com.wind.billypos.view.model

import java.io.Serializable
import java.util.*

data class Synchronization(
    var isItems: Boolean,
    var isCustomers: Boolean,
    var isCategories: Boolean,
    var isTransactions: Boolean,
    var isCashbalance: Boolean,
    var lastSync: Date,
    var isEntityPoint: Boolean) :
    Serializable {

    fun isSyncRunning(): Boolean {
        return isItems || isCustomers || isCategories || isTransactions || isCashbalance || isEntityPoint
    }

    fun isSyncingEverything(sync: Boolean){
        isItems = sync
        isCustomers= sync
        isCategories = sync
        isTransactions = sync
        isCashbalance = sync
        isCashbalance = sync
    }
}