package com.wind.billypos.utils

import android.os.Environment

object Utils {
    /* Checks if external storage is available to at least read */
    val isExternalStorageReadable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
        }

    class CONSTANTS {
        object ALERTS {
            const val PERMISSION_NEEDED =
                "Ju nuk mund të hyni nëse pajisja juaj nuk identifikohet! Ju lutem jepni lejen për të vazhduar"
            const val PERMISSION_PHONE_NEEDED =
                "Ju duhet t`i jepni aplikacionit të drejtat e nevojshme për të vazhduar!"
            const val NOT_CONNECTED =
                "Ju nuk keni akses në internet! Ju lutem provoni pasi të jeni lidhur në rrjet"
            const val PERMISSION_LOCATION_NEEDED =
                "Ju duhet jepni të drejtën për të lexuar vendodhjen tuaj aktuale për të vazhduar!"
            const val STORAGE_PERMISSION_NEEDED =
                "Ju lutem jepni të drejtën për të përdorur kamerën për të vazhduar!"
        }

        object INTENT_REQUESTS {
            const val APP_LOGIN = 10023
        }

        object RECYCLER_VIEW_TYPE {
            const val ITEMS_LIST = 100
            const val ITEMS_GRID = 1001
            const val ITEMS_PRINT = 1002
            const val TYPE_DATE = 2000
            const val TYPE_SALE = 2001
            const val ITEMS_GRID_COUNT = 3
            const val ITEMS_GRID_COUNT_TABLET = 6
            const val CUSTOMERS_LIST = 400
            const val CUSTOMERS_GRID = 4001
        }

        object FRAGMENT_PARAMS {
            const val TCR_NUMBER = "TCRNUMBER"
            const val VALID_FROM = "VALID_FROM"
            const val VALID_TO = "VALID_TO"
            const val TRANSACTION_ID = "TRANSACTION_ID"
            const val TRANSACTION_ITEM_ID = "TRANSACTION_ITEM_ID"
            const val ITEM_ID = "ITEM_ID"
            const val CASH_BALANCE_ID = "CASH_BALANCE_ID"
            const val CUSTOMER_ACTION = "CUSTOMER_ACTION"
            const val CUSTOMER_ACTION_CHOOSE = "CHOOSE"
            const val CUSTOMER_ID = "CUSTOMER_ID"
            const val ENTITY_ID = "ENTITY_ID"
            const val IS_SUMMARY_INVOICE = "IS_FROM_ORDER"
            const val ENTITY_ORDER_ID = "ENTITY_ORDER_ID"
        }

        object FRAGMENT_TAGS {
            const val TAG_POS_FRAGMENT = "posFragment"
            const val TAG_CHECKOUT_FRAGMENT = "checkoutFragment"
            const val TAG_OVERVIEW_FRAGMENT = "overviewFragment"
        }

        object ACTION_TAGS {
            const val CASH_BALANCE_ACTION = 100
        }
    }
}