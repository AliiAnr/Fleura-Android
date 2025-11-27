package com.course.fleura.ui.screen.navigation

import android.util.Log
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
    const val PAYMENT_SUCCESS_ROUTE = "paymentSuccess"
    const val ORDER_PAYMENT_ID_KEY = "orderPaymentId"
    const val SNACK_ID_KEY = "snackId"
    const val STORE_ID_KEY = "storeId"
    const val GENERAL_ORDER_ID_KEY = "generalOrderId"
    const val GENERAL_PROFILE_ID_KEY = "generalProfileId"
    const val DETAIL_ADDRESS_ID_KEY = "AddaddressId"
    const val ADDRESS_ID_KEY = "addressId"
    const val ORDER_ID_KEY = "orderId"
    const val FLOWER_ID_KEY = "flowerId"
    const val ORIGIN = "origin"
    const val OTP_ROUTE = "otp"
    const val NEW_PASSWORD_ROUTE = "newPassword"
    const val ADDRESS_ROUTE = "addAddress"
}

object DetailDestinations {
    const val DETAIL_PROFILE_ROUTE = "profileDetail"
    const val DETAIL_GENERAL_ROUTE = "generalOrder"
    const val DETAIL_ADD_ADDRESS = "addAddress"
    const val DETAIL_MERCHANT = "merchant"
    const val DETAIL_ADDRESS = "addressDetail"
    const val DETAIL_FLOWER = "flowerDetail"
    const val DETAIL_CONFIRM_ORDER = "confirmOrder"
    const val DETAIL_TRANSFER_ORDER = "orderDetail"
    const val DETAIL_QR_ORDER = "qrOrder"
    const val DETAIL_QR_CREATED_ORDER = "qrCreatedOrder"
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

    fun navigateToOrderDetail(id: String, origin: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${DetailDestinations.DETAIL_CONFIRM_ORDER}/$id?origin=$origin")
        }
    }

    fun navigateToFlowerDetail(id: String, origin: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${DetailDestinations.DETAIL_FLOWER}/$id?origin=$origin")
        }
    }

    fun navigateToProfileDetail(location: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${DetailDestinations.DETAIL_PROFILE_ROUTE}/$location")
        }
    }

    fun navigateToGeneralOrder(location: String, from: NavBackStackEntry,) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${DetailDestinations.DETAIL_GENERAL_ROUTE}/$location")
        }
    }

    fun navigateToQrOrder(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(DetailDestinations.DETAIL_QR_ORDER)
        }
    }

    fun navigateToQrCreatedOrder(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(DetailDestinations.DETAIL_QR_CREATED_ORDER)
        }
    }

    fun navigateToAddressDetail(id: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${DetailDestinations.DETAIL_ADDRESS}/$id")
        }
    }

    fun navigateToAddAddress(location: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${DetailDestinations.DETAIL_ADD_ADDRESS}/$location")
        }
    }

    fun navigateToCreatedOrder(id: String, origin: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${DetailDestinations.DETAIL_TRANSFER_ORDER}/$id?origin=$origin")
        }
    }

    fun navigateToPaymentSuccess(orderId: String) {
        if (navController.currentDestination?.route != null) {
            try {
                navController.navigate("${MainDestinations.PAYMENT_SUCCESS_ROUTE}/$orderId") {
                    // Clear back stack sampai dashboard agar user tidak bisa kembali ke payment
                    popUpTo(MainDestinations.DASHBOARD_ROUTE) {
                        inclusive = false
                        saveState = false // Jangan save state untuk payment flow
                    }
                    launchSingleTop = true
                    restoreState = false // Payment success tidak perlu restore state
                }
            } catch (e: Exception) {
                Log.e("Navigation", "Failed to navigate to payment success: ${e.message}")
            }
        }
    }



}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}