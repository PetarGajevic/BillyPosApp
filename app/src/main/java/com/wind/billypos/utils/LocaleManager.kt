package com.wind.billypos.utils

import com.wind.billypos.utils.PreferenceHelper.lang
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LocaleManager{


    fun setLocale(context: Context): Context {
        var lang = PreferenceHelper.customPreference(context).lang

        var contextFun = context

        var locale = Locale(lang)
        Locale.setDefault(locale)

        var resources = context.resources
        var configuration = Configuration(resources.configuration)

        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(locale)
            contextFun = context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.getDisplayMetrics())
        }

        return contextFun
    }

    fun setNewLocale(context: Context, lang: String): Context {
        PreferenceHelper.customPreference(context).lang = lang

        var contextFun = context

        var locale = Locale(lang)
        Locale.setDefault(locale)

        var resources = context.resources
        var configuration = Configuration(resources.configuration)

        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(locale)
            contextFun = context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.getDisplayMetrics())
        }

        return contextFun
    }
}