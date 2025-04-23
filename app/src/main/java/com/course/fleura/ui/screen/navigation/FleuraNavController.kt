package com.course.fleura.ui.screen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

object MainDestinations {
    const val DASHBOARD_ROUTE = "dashboard"
    const val ONBOARDING_ROUTE = "onboarding"
    const val WELCOME_ROUTE = "welcome"
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"
    const val USERNAME_ROUTE = "username"
    const val ORDER_HISTORY_ROUTE = "orderHistory"
    const val SNACK_DETAIL_ROUTE = "snack"
    const val SNACK_ID_KEY = "snackId"
    const val STORE_ID_KEY = "storeId"
    const val FLOWER_ID_KEY = "flowerId"
    const val ORIGIN = "origin"
    const val OTP_ROUTE = "otp"
    const val NEW_PASSWORD_ROUTE = "newPassword"
}

object DetailDestinations {
    const val DETAIL_PROFILE_ROUTE = "profileDetail"
    const val DETAIL_GENERAL_ROUTE = "generalOrder"
    const val DETAIL_ADD_ADDRESS = "addAddress"
    const val DETAIL_MERCHANT = "merchant"
    const val DETAIL_FLOWER = "flowerDetail"
    const val DETAIL_CONFIRM_ORDER = "confirmOrder"
    const val DETAIL_TRANSFER_ORDER = "orderDetail"
    const val DETAIL_CASH_ORDER = "cashOrder"
    const val DETAIL_CART = "detailCart"
}

object QueryKeys {
    const val EMAIL = "email"
}

@Composable
fun rememberFleuraNavController(
    navController: NavHostController = rememberNavController()
): FleuraNavController = remember(navController) {
    FleuraNavController(navController)
}

@Stable
class FleuraNavController(
    val navController: NavHostController,
) {

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != navController.currentDestination?.route) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigateToNonBottomBarRoute(route: String, isPopBackStack: Boolean = false) {
        if (route != navController.currentDestination?.route) {
            if (isPopBackStack) {
                navController.popBackStack()
            }
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    fun navigateToSnackDetail(snackId: Long, origin: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.SNACK_DETAIL_ROUTE}/$snackId?origin=$origin")
        }
    }

    fun navigateToStoreDetail(id: String, origin: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${DetailDestinations.DETAIL_MERCHANT}/$id?origin=$origin")
        }
    }

    fun navigateToFlowerDetail(id: String, origin: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${DetailDestinations.DETAIL_FLOWER}/$id?origin=$origin")
        }
    }

    fun navigateToDetailProfile(id: Long, location: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${DetailDestinations.DETAIL_PROFILE_ROUTE}/$id/$location")
        }
    }

//    fun navigateToGeneral(origin: String, from: NavBackStackEntry,) {
//        if (from.lifecycleIsResumed()) {
//            navController.navigate("${DetailDestinations.DETAIL_GENERAL_ROUTE}/?origin=$origin")
//        }
//    }

}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}