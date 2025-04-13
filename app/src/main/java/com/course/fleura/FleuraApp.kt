@file:OptIn(
    ExperimentalSharedTransitionApi::class
)

package com.course.fleura

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.course.fleura.data.resource.Resource
import com.course.fleura.data.store.DataStoreManager
import com.course.fleura.di.factory.LoginViewModelFactory
import com.course.fleura.di.factory.OnBoardingViewModelFactory
import com.course.fleura.ui.components.FleuraBottomBar
import com.course.fleura.ui.components.HomeSections
import com.course.fleura.ui.screen.authentication.login.LoginScreen
import com.course.fleura.ui.screen.authentication.login.LoginScreenViewModel
import com.course.fleura.ui.screen.authentication.newpassword.NewPasswordScreen
import com.course.fleura.ui.screen.authentication.otp.OtpScreen
import com.course.fleura.ui.screen.authentication.register.RegisterScreen
import com.course.fleura.ui.screen.authentication.username.UsernameScreen
import com.course.fleura.ui.screen.authentication.welcome.WelcomeScreen
import com.course.fleura.ui.screen.dashboard.detail.history.OrderHistory
import com.course.fleura.ui.screen.dashboard.detail.home.DetailTest
import com.course.fleura.ui.screen.dashboard.detail.home.Merchant
import com.course.fleura.ui.screen.dashboard.detail.order.ConfirmOrder
import com.course.fleura.ui.screen.dashboard.detail.order.DetailTranferOrder
import com.course.fleura.ui.screen.dashboard.detail.order.FlowerDetail
import com.course.fleura.ui.screen.dashboard.detail.order.GeneralOrder
import com.course.fleura.ui.screen.dashboard.detail.profile.AddAdress
import com.course.fleura.ui.screen.dashboard.detail.profile.GeneralDetail
import com.course.fleura.ui.screen.navigation.DetailDestinations
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
import java.util.prefs.Preferences

@Composable
fun FleuraApp() {

    val onBoardingViewModel : OnBoardingViewModel = viewModel(factory = OnBoardingViewModelFactory.getInstance(Resource.appContext))

    val onBoardingStatus = onBoardingViewModel.onBoardingStatus.collectAsStateWithLifecycle(initialValue = false)

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

    FleuraTheme {
        val fleuraNavController = rememberFleuraNavController()
        SharedTransitionLayout {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this
            ) {
//                if(onBoardingStatus.value) MainDestinations.WELCOME_ROUTE else MainDestinations.ONBOARDING_ROUTE
                NavHost(
                    navController = fleuraNavController.navController,
                    startDestination =
                        destination
                    ,
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
                            onSnackSelected = fleuraNavController::navigateToSnackDetail
                        )
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.ORDER_HISTORY_ROUTE
                    ) { backStackEntry ->
                        OrderHistory(

                        )
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.DETAIL_FLOWER
                    ) { backStackEntry ->
                        FlowerDetail(
                            flowerId = 0
                        )
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.DETAIL_MERCHANT
                    ) { backStackEntry ->
                        Merchant(
                            id = 0
                        )
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.DETAIL_PROFILE_ROUTE
                    ) { backStackEntry ->
                        //ambil location dari arguments
                        GeneralDetail(
                            id = 0,
                            location = "Address"
                        )
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.DETAIL_GENERAL_ROUTE
                    ) { backStackEntry ->
                        GeneralOrder(
                            id = 0,
                            location = "Payment Process"
                        )
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.DETAIL_ADD_ADDRESS
                    ) { backStackEntry ->
                        AddAdress()
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.DETAIL_CONFIRM_ORDER
                    ) { backStackEntry ->
                        ConfirmOrder(
                            flowerId = 0
                        )
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.DETAIL_TRANSFER_ORDER
                    ) { backStackEntry ->
                        DetailTranferOrder(
                            id = 0
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
    onSnackSelected: (Long, String, NavBackStackEntry) -> Unit
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
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding)
            )
        }
    }
}

val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

