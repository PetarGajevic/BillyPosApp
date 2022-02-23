package com.wind.billypos.utils.extensions

import androidx.annotation.NonNull
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.navigateSafe(
    @NonNull directions: NavDirections
) {
    val action = currentDestination?.getAction(directions.actionId) ?: graph.getAction(directions.actionId)
    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(directions.actionId, directions.arguments)
    }
}