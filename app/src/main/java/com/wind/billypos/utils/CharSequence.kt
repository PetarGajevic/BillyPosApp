package com.wind.billypos.utils

fun CharSequence.clearStringToDouble(): Double =
    this.replace("[,.]".toRegex(), "").toDouble()