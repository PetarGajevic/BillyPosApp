package com.wind.billypos.utils

import timber.log.Timber
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class DoublePrice {

    fun formatPrice(double: Double?): String{
        val otherSymbols = DecimalFormatSymbols()
        otherSymbols.decimalSeparator = '.'
        val decimalFormat = DecimalFormat("0.00", otherSymbols)
        decimalFormat.roundingMode = RoundingMode.HALF_UP
        return decimalFormat.format(double)
    }

}