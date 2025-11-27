@file:OptIn(
    ExperimentalSharedTransitionApi::class
)

package com.course.fleura

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.course.fleura.data.model.notification.DownloadPermissionHandler
import com.course.fleura.data.resource.Resource
import com.course.fleura.di.factory.CartViewModelFactory
import com.course.fleura.di.factory.HomeViewModelFactory
import com.course.fleura.di.factory.LoginViewModelFactory
import com.course.fleura.di.factory.OnBoardingViewModelFactory
import com.course.fleura.di.factory.OrderViewModelFactory
import com.course.fleura.di.factory.StoreViewModelFactory
//import com.course.fleura.socket.SocketManager
import com.course.fleura.ui.components.FleuraBottomBar
import com.course.fleura.ui.components.HomeSections
import com.course.fleura.ui.screen.authentication.address.AuthAddress
import com.course.fleura.ui.screen.authentication.login.LoginScreen
import com.course.fleura.ui.screen.authentication.login.LoginScreenViewModel
import com.course.fleura.ui.screen.authentication.newpassword.NewPasswordScreen
import com.course.fleura.ui.screen.authentication.otp.OtpScreen
import com.course.fleura.ui.screen.authentication.register.RegisterScreen
import com.course.fleura.ui.screen.authentication.username.UsernameScreen
import com.course.fleura.ui.screen.authentication.welcome.WelcomeScreen
import com.course.fleura.ui.screen.dashboard.cart.CartViewModel
import com.course.fleura.ui.screen.dashboard.detail.history.OrderHistory
import com.course.fleura.ui.screen.dashboard.detail.home.DetailTest
import com.course.fleura.ui.screen.dashboard.detail.home.Merchant
import com.course.fleura.ui.screen.dashboard.detail.order.ConfirmOrder
import com.course.fleura.ui.screen.dashboard.detail.order.DetailTransferOrder
import com.course.fleura.ui.screen.dashboard.detail.order.FlowerDetail
import com.course.fleura.ui.screen.dashboard.detail.order.GeneralOrder
import com.course.fleura.ui.screen.dashboard.detail.order.PaymentSuccessScreen
import com.course.fleura.ui.screen.dashboard.detail.order.QrCreatedOrder
import com.course.fleura.ui.screen.dashboard.detail.order.QrOrder
import com.course.fleura.ui.screen.dashboard.detail.profile.AddAdress
import com.course.fleura.ui.screen.dashboard.detail.profile.AddressDetail
import com.course.fleura.ui.screen.dashboard.detail.profile.GeneralDetail
import com.course.fleura.ui.screen.dashboard.home.HomeViewModel
import com.course.fleura.ui.screen.dashboard.order.OrderViewModel
import com.course.fleura.ui.screen.dashboard.profile.ProfileViewModel
import com.course.fleura.ui.screen.navigation.DetailDestinations
import com.course.fleura.ui.screen.navigation.FleuraNavController
import com.course.fleura.ui.screen.navigation.FleuraScaffold
import com.course.fleura.ui.screen.navigation.MainDestinations
import com.course.fleura.ui.screen.navigation.QueryKeys
import com.course.fleura.ui.screen.navigation.addHomeGraph
import com.course.fleura.ui.screen.navigation.composableWithCompositionLocal
import com.course.fleura.ui.screen.navigation.nonSpatialExpressiveSpring
import com.course.fleura.ui.screen.navigation.rememberFleuraNavController
import com.course.fleura.ui.screen.navigation.rememberFleuraScaffoldState
import com.course.fleura.ui.screen.navigation.spatialExpressiveSpring
import com.course.fleura.ui.screen.onboarding.OnBoardingScreen
import com.course.fleura.ui.screen.onboarding.OnBoardingViewModel
import com.course.fleura.ui.theme.FleuraTheme
import kotlinx.coroutines.flow.first

@Composable
fun FleuraApp() {


    val onBoardingViewModel: OnBoardingViewModel =
        viewModel(factory = OnBoardingViewModelFactory.getInstance(Resource.appContext))

    val onBoardingStatus =
        onBoardingViewModel.onBoardingStatus.collectAsStateWithLifecycle(initialValue = false)

    val loginViewModel: LoginScreenViewModel = viewModel(
        factory = LoginViewModelFactory.getInstance(
            Resource.appContext
        )
    )

    val navigationViewModel: NavigationDestinationViewModel = viewModel(
        factory = StartupNavigationViewModelFactory(
            onBoardingViewModel,
            loginViewModel
        )
    )

    val destination by navigationViewModel.startDestination.collectAsStateWithLifecycle(
        initialValue = MainDestinations.ONBOARDING_ROUTE
    )

    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory.getInstance(
            Resource.appContext
        )
    )

    val cartViewModel: CartViewModel = viewModel(
        factory = CartViewModelFactory.getInstance(
            Resource.appContext
        )
    )

    val orderViewModel: OrderViewModel = viewModel(
        factory = OrderViewModelFactory.getInstance(
            Resource.appContext
        )
    )

    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory.getInstance(
            Resource.appContext
        )
    )

    LaunchedEffect(key1 = Unit) {
        val isLoggedIn = loginViewModel.isUserLoggedIn().first()
        if (isLoggedIn) {
            loginViewModel.saveToken()
        }
    }

    FleuraTheme {
        val fleuraNavController = rememberFleuraNavController()
        SharedTransitionLayout {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this
            ) {
//
//                LaunchedEffect(Unit) {
//                    SocketManager.paymentEvents.collect { data ->
//                        // Validasi apakah navigasi aman
//                        try {
//                            fleuraNavController.navigateToPaymentSuccess(data.orderId)
//                        } catch (e: Exception) {
//                            Log.e("Navigation", "Failed to navigate: ${e.message}")
//                        }
//                    }
//                }

//                if(onBoardingStatus.value) MainDestinations.WELCOME_ROUTE else MainDestinations.ONBOARDING_ROUTE
                NavHost(
                    navController = fleuraNavController.navController,
                    startDestination =
                        destination,
//                        DetailDestinations.DETAIL_GENERAL_ROUTE,
//                        DetailDestinations.DETAIL_CONFIRM_ORDER,
//                        DetailDestinations.DETAIL_TRANSFER_ORDER,
                    contentAlignment = Alignment.Center
                ) {
                    composableWithCompositionLocal(
                        route = MainDestinations.WELCOME_ROUTE
                    ) { backStackEntry ->
                        WelcomeScreen(
                            navigateToRoute = fleuraNavController::navigateToNonBottomBarRoute
                        )
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.ONBOARDING_ROUTE
                    ) { backStackEntry ->
                        OnBoardingScreen(
                            navigateToRoute = fleuraNavController::navigateToNonBottomBarRoute,
                            setOnBoardingCompleted = onBoardingViewModel::setOnBoardingCompleted
                        )
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.LOGIN_ROUTE
                    ) { backStackEntry ->
                        LoginScreen(
                            navigateToRoute = fleuraNavController::navigateToNonBottomBarRoute,
                            onBackClick = fleuraNavController::upPress,
                            loginViewModel = loginViewModel
                        )
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.REGISTER_ROUTE
                    ) { backStackEntry ->
                        RegisterScreen(
                            navigateToRoute = fleuraNavController::navigateToNonBottomBarRoute,
                            onBackClick = fleuraNavController::upPress
                        )
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.USERNAME_ROUTE
                    ) { backStackEntry ->
                        UsernameScreen(
                            loginScreenViewModel = loginViewModel,
                            navigateToRoute = fleuraNavController::navigateToNonBottomBarRoute
                        )
                    }

                    composableWithCompositionLocal(
                        route = "${MainDestinations.OTP_ROUTE}?" + "email={${QueryKeys.EMAIL}}"
                    ) { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val email = arguments.getString(QueryKeys.EMAIL) ?: "Email tidak ditemukan"
                        OtpScreen(
                            email = email,
                            navigateToRoute = fleuraNavController::navigateToNonBottomBarRoute,
                            onBackClick = fleuraNavController::upPress
                        )
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.NEW_PASSWORD_ROUTE
                    ) { backStackEntry ->
                        NewPasswordScreen()
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.DASHBOARD_ROUTE
                    ) { backStackEntry ->
                        MainContainer(
                            onSnackSelected = fleuraNavController::navigateToSnackDetail,
                            onStoreSelected = fleuraNavController::navigateToStoreDetail,
                            onFlowerSelected = fleuraNavController::navigateToFlowerDetail,
                            onOrderDetail = fleuraNavController::navigateToOrderDetail,
                            onCreatedOrderDetail = fleuraNavController::navigateToCreatedOrder,
                            onProfileDetail = fleuraNavController::navigateToProfileDetail,
                            homeViewModel = homeViewModel,
                            cartViewModel = cartViewModel,
                            orderViewModel = orderViewModel,
                            profileViewModel = profileViewModel,
                        )
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.ORDER_HISTORY_ROUTE
                    ) { backStackEntry ->
                        OrderHistory(

                        )
                    }

                    composableWithCompositionLocal(
                        route = "${DetailDestinations.DETAIL_FLOWER}/" +
                                "{${MainDestinations.FLOWER_ID_KEY}}" +
                                "?origin={${MainDestinations.ORIGIN}}",
                        arguments = listOf(
                            navArgument(MainDestinations.FLOWER_ID_KEY) {
                                type = NavType.StringType
                            }
                        ),
                    ) { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val flowerId = arguments.getString(MainDestinations.FLOWER_ID_KEY)
                        val origin = arguments.getString(MainDestinations.ORIGIN)

                        val selectedProduct by homeViewModel.selectedProduct.collectAsStateWithLifecycle()
                        selectedProduct?.let {
                            FlowerDetail(
                                flowerId = flowerId ?: "",
                                origin = origin ?: "",
                                selectedProduct = it,
                                homeViewModel = homeViewModel,
                                onStoreClick = { storeid, origin ->
                                    fleuraNavController.navigateToStoreDetail(
                                        storeid,
                                        origin,
                                        backStackEntry
                                    )
                                },
                                onBackClick = fleuraNavController::upPress,
                            )
                        }
                    }

                    composableWithCompositionLocal(
                        route = "${DetailDestinations.DETAIL_MERCHANT}/" +
                                "{${MainDestinations.STORE_ID_KEY}}" +
                                "?origin={${MainDestinations.ORIGIN}}",
                        arguments = listOf(
                            navArgument(MainDestinations.STORE_ID_KEY) {
                                type = NavType.StringType
                            }
                        ),
                    ) { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val storeId = arguments.getString(MainDestinations.STORE_ID_KEY)
                        val origin = arguments.getString(MainDestinations.ORIGIN)

                        Merchant(
                            homeViewModel = homeViewModel,
                            storeId = storeId ?: "Store ID tidak ditemukan",
                            origin = origin ?: "NULL",
                            onBackClick = fleuraNavController::upPress,
                            onFlowerClick = { flowerId, from ->
                                fleuraNavController.navigateToFlowerDetail(
                                    flowerId,
                                    from,
                                    backStackEntry
                                )
                            },
                            storeViewModel = viewModel(
                                factory = StoreViewModelFactory.getInstance(
                                    Resource.appContext
                                )
                            )
//                            onFlowerClick
                        )
                    }

                    composableWithCompositionLocal(
                        route = "${DetailDestinations.DETAIL_PROFILE_ROUTE}/" +
                                "{${MainDestinations.GENERAL_PROFILE_ID_KEY}}",
                        arguments = listOf(
                            navArgument(MainDestinations.GENERAL_PROFILE_ID_KEY) {
                                type = NavType.StringType
                            }
                        ),

                    ) { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val generalProfileLocation = arguments.getString(MainDestinations.GENERAL_PROFILE_ID_KEY)

                        GeneralDetail(
                            location = generalProfileLocation ?: "Address",
                            onBackClick = fleuraNavController::upPress,
                            profileViewModel = profileViewModel,
                            onAddAddressClick = {
                                fleuraNavController.navigateToAddAddress(
                                    DetailDestinations.DETAIL_PROFILE_ROUTE,
                                    backStackEntry
                                )
                            },
                            onAddressClick = { id ->
                                fleuraNavController.navigateToAddressDetail(
                                    id,
                                    backStackEntry
                                )
                            }
                        )
                    }

                    composableWithCompositionLocal(
                        route = "${DetailDestinations.DETAIL_GENERAL_ROUTE}/" +
                                "{${MainDestinations.GENERAL_ORDER_ID_KEY}}",
                        arguments = listOf(
                            navArgument(MainDestinations.GENERAL_ORDER_ID_KEY) {
                                type = NavType.StringType
                            }
                        ),
                        enterTransition = {
                            // Muncul dari kanan
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        exitTransition = {
                            // Keluar ke kanan
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        popEnterTransition = {
                            // Jika balik (back), bisa juga dari kiri ke posisi normal
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        popExitTransition = {
                            // Jika back, keluar ke kanan
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        }
                    ) { backStackEntry ->

                        val arguments = requireNotNull(backStackEntry.arguments)
                        val generalOrderLocation = arguments.getString(MainDestinations.GENERAL_ORDER_ID_KEY)

                        GeneralOrder(
                            onBackClick = fleuraNavController::upPress,
                            cartViewModel = cartViewModel,
                            location = generalOrderLocation ?: "Payment Method",
                        )
                    }

                    composableWithCompositionLocal(
                        route = "${DetailDestinations.DETAIL_ADD_ADDRESS}/" + "{${MainDestinations.DETAIL_ADDRESS_ID_KEY}}",
                        arguments = listOf(
                            navArgument(MainDestinations.DETAIL_ADDRESS_ID_KEY) {
                                type = NavType.StringType
                            }
                        ),
                        enterTransition = {
                            // Muncul dari kanan
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        exitTransition = {
                            // Keluar ke kanan
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        popEnterTransition = {
                            // Jika balik (back), bisa juga dari kiri ke posisi normal
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        popExitTransition = {
                            // Jika back, keluar ke kanan
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        }
                    ) { backStackEntry ->

                        val arguments = requireNotNull(backStackEntry.arguments)
                        val location = arguments.getString(MainDestinations.DETAIL_ADDRESS_ID_KEY)

                        AddAdress(
                            onBackClick = fleuraNavController::upPress,
                            profileViewModel = profileViewModel
                        )
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.ADDRESS_ROUTE
                    ) { backStackEntry ->
                        AuthAddress(
                            navigateToRoute = fleuraNavController::navigateToNonBottomBarRoute,
                            loginViewModel = loginViewModel
                        )
                    }

                    composableWithCompositionLocal(
                        route = "${DetailDestinations.DETAIL_ADDRESS}/" + "{${MainDestinations.ADDRESS_ID_KEY}}",
                        arguments = listOf(
                            navArgument(MainDestinations.ADDRESS_ID_KEY) {
                                type = NavType.StringType
                            }
                        ),
                        enterTransition = {
                            // Muncul dari kanan
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        exitTransition = {
                            // Keluar ke kanan
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        popEnterTransition = {
                            // Jika balik (back), bisa juga dari kiri ke posisi normal
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        popExitTransition = {
                            // Jika back, keluar ke kanan
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        }
                    ) { backStackEntry ->

                        val arguments = requireNotNull(backStackEntry.arguments)
                        val addressId = arguments.getString(MainDestinations.ADDRESS_ID_KEY)

                        val selectedAddressItem by profileViewModel.selectedAddressItem.collectAsStateWithLifecycle()

                        selectedAddressItem?.let {
                            AddressDetail(
                                onBackClick = fleuraNavController::upPress,
                                profileViewModel = profileViewModel,
                                selectedAddressItem = it
                            )
                        }
                    }

                    composableWithCompositionLocal(
                        route = "${DetailDestinations.DETAIL_CONFIRM_ORDER}/" +
                                "{${MainDestinations.STORE_ID_KEY}}" +
                                "?origin={${MainDestinations.ORIGIN}}",
                        arguments = listOf(
                            navArgument(MainDestinations.STORE_ID_KEY) {
                                type = NavType.StringType
                            }
                        ),
                    ) { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val storeId = arguments.getString(MainDestinations.STORE_ID_KEY)
                        val origin = arguments.getString(MainDestinations.ORIGIN)

                        val selectedCartItem by cartViewModel.selectedCartItem.collectAsStateWithLifecycle()

                        selectedCartItem?.let {
                            ConfirmOrder(
                                storeId = storeId ?: "",
                                origin = origin ?: "",
                                selectedCartItem = it,
                                cartViewModel = cartViewModel,
                                onBackClick = fleuraNavController::upPress,
                                onChooseClick = { location ->
                                    fleuraNavController.navigateToGeneralOrder(
                                        location,
                                        backStackEntry
                                    )
                                },
                                onOrderSuccess = {
                                    fleuraNavController.navigateToQrOrder(backStackEntry)
                                }
                            )
                        }
                    }

                    composableWithCompositionLocal(
                        route = "${DetailDestinations.DETAIL_TRANSFER_ORDER}/" +
                                "{${MainDestinations.ORDER_ID_KEY}}" +
                                "?origin={${MainDestinations.ORIGIN}}",
                        arguments = listOf(
                            navArgument(MainDestinations.ORDER_ID_KEY) {
                                type = NavType.StringType
                            }
                        )
                    ) { backStackEntry ->

                        val arguments = requireNotNull(backStackEntry.arguments)
                        val id = arguments.getLong(MainDestinations.ORDER_ID_KEY)
                        val origin = arguments.getString(MainDestinations.ORIGIN)

                        val selectedOrderItem by orderViewModel.selectedOrderItem.collectAsStateWithLifecycle()

                        selectedOrderItem?.let {
                            DetailTransferOrder(
                                id = 0,
                                selectedOrderItem = it,
                                orderViewModel = orderViewModel,
                                onBackClick = fleuraNavController::upPress,
                                onPaymentClick = {
                                    orderViewModel.setSelectedCreatedOrderItem(it)
                                    fleuraNavController.navigateToQrCreatedOrder(backStackEntry)
                                }
                            )
                        }
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.DETAIL_QR_ORDER

                        ) { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        QrOrder(
                            cartViewModel = cartViewModel,
                            onDownloadQr = { url, filename ->
                                val context = Resource.appContext
                                DownloadPermissionHandler.requestDownload(context, url, filename)
                            },
                            onBack = fleuraNavController::upPress
                        )
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.DETAIL_QR_CREATED_ORDER,

                        enterTransition = {
                            // Muncul dari kanan
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        exitTransition = {
                            // Keluar ke kanan
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        popEnterTransition = {
                            // Jika balik (back), bisa juga dari kiri ke posisi normal
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        popExitTransition = {
                            // Jika back, keluar ke kanan
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        }


                    ) { backStackEntry ->


                        val arguments = requireNotNull(backStackEntry.arguments)
                        val id = arguments.getLong(MainDestinations.ORDER_ID_KEY)
                        val origin = arguments.getString(MainDestinations.ORIGIN)

                        val selectedCreatedOrderItem by orderViewModel.selectedCreatedOrderItem.collectAsStateWithLifecycle()

                        selectedCreatedOrderItem?.let {
                            QrCreatedOrder(
                                selectedCreatedOrderItem = it,
                                onDownloadQr = { url, filename ->
                                    val context = Resource.appContext
                                    DownloadPermissionHandler.requestDownload(
                                        context,
                                        url,
                                        filename
                                    )
                                },
                                onBack = fleuraNavController::upPress,
                                orderViewModel = orderViewModel
                            )
                        }
                    }

                    composableWithCompositionLocal(
                        "${MainDestinations.PAYMENT_SUCCESS_ROUTE}/" +
                                "{${MainDestinations.ORDER_PAYMENT_ID_KEY}}",
                        arguments = listOf(
                            navArgument(MainDestinations.ORDER_PAYMENT_ID_KEY) {
                                type = NavType.StringType
                            }
                        ),

                        ) { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val id = arguments.getString(MainDestinations.ORDER_PAYMENT_ID_KEY)
                        val origin = arguments.getString(MainDestinations.ORIGIN)
                        PaymentSuccessScreen(
                            orderId = id ?: "Order ID tidak ditemukan",
                            upPress = fleuraNavController::upPress
                        )
                    }




                    composableWithCompositionLocal(
                        "${MainDestinations.SNACK_DETAIL_ROUTE}/" +
                                "{${MainDestinations.SNACK_ID_KEY}}" +
                                "?origin={${MainDestinations.ORIGIN}}",
                        arguments = listOf(
                            navArgument(MainDestinations.SNACK_ID_KEY) {
                                type = NavType.LongType
                            }
                        ),

                        ) { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val id = arguments.getLong(MainDestinations.SNACK_ID_KEY)
                        val origin = arguments.getString(MainDestinations.ORIGIN)
                        DetailTest(
                            id,
                            origin = origin ?: "",
                            upPress = fleuraNavController::upPress
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainContainer(
    modifier: Modifier = Modifier,
    onSnackSelected: (Long, String, NavBackStackEntry) -> Unit,
    onStoreSelected: (String, String, NavBackStackEntry) -> Unit,
    onFlowerSelected: (String, String, NavBackStackEntry) -> Unit,
    onOrderDetail: (String, String, NavBackStackEntry) -> Unit,
    onCreatedOrderDetail: (String, String, NavBackStackEntry) -> Unit,
    onProfileDetail: (String, NavBackStackEntry) -> Unit,
    homeViewModel: HomeViewModel,
    cartViewModel: CartViewModel,
    orderViewModel: OrderViewModel,
    profileViewModel: ProfileViewModel
) {
    val fleuraScaffoldState = rememberFleuraScaffoldState()
    val nestedNavController = rememberFleuraNavController()
    val navBackStackEntry by nestedNavController.navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    FleuraScaffold(
        bottomBar = {
            with(animatedVisibilityScope) {
                with(sharedTransitionScope) {
                    FleuraBottomBar(
                        tabs = HomeSections.entries.toTypedArray(),
                        currentRoute = currentRoute ?: HomeSections.Home.route,
                        navigateToRoute = nestedNavController::navigateToBottomBarRoute,
                        modifier = Modifier
                            .renderInSharedTransitionScopeOverlay(
                                zIndexInOverlay = 1f,
                            )
                            .animateEnterExit(
                                enter = fadeIn(nonSpatialExpressiveSpring()) + slideInVertically(
                                    spatialExpressiveSpring()
                                ) {
                                    it
                                },
                                exit = fadeOut(nonSpatialExpressiveSpring()) + slideOutVertically(
                                    spatialExpressiveSpring()
                                ) {
                                    it
                                }
                            )
                    )
                }
            }
        },
        modifier = modifier

    ) { padding ->
        NavHost(
            navController = nestedNavController.navController,
            startDestination = HomeSections.Home.route,
            contentAlignment = Alignment.Center
        ) {
            addHomeGraph(
                onSnackSelected = onSnackSelected,
                onStoreClick = onStoreSelected,
                onFlowerClick = onFlowerSelected,
                onOrderDetail = onOrderDetail,
                onCreatedOrderDetail = onCreatedOrderDetail,
                onProfileDetail = onProfileDetail,
                homeViewModel = homeViewModel,
                cartViewModel = cartViewModel,
                orderViewModel = orderViewModel,
                profileViewModel = profileViewModel,
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding),

            )
        }
    }
}

val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

