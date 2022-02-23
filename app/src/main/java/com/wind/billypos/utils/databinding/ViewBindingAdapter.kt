package com.wind.billypos.utils.databinding

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

object ViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("errorText")
    fun setErrorMessage(view: TextInputLayout, errorText: String?) {
        view.error = if (errorText.isNullOrEmpty()) "" else errorText
    }


}