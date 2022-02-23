package com.wind.billypos.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

fun Fragment.getNavigationResult(key: String = "result") =
    NavHostFragment.findNavController(this).currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
        key
    )

fun Fragment.setNavigationResult(result: String, key: String = "result") {
    NavHostFragment.findNavController(this).previousBackStackEntry?.savedStateHandle?.set(
        key,
        result
    )
}