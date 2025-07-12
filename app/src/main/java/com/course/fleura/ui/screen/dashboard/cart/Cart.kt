package com.course.fleura.ui.screen.dashboard.cart

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleura.R
//import com.course.fleura.data.model.remote.CartItem
import com.course.fleura.data.model.remote.CartListResponse
import com.course.fleura.data.model.remote.DataCartItem
import com.course.fleura.data.model.remote.Detail
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.components.CartSummary
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.EmptyCart
import com.course.fleura.ui.components.FakeCategory
import com.course.fleura.ui.components.HomeSections
import com.course.fleura.ui.screen.dashboard.profile.ProfileViewModel
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.screen.navigation.MainDestinations
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.primaryLight
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import network.chaintech.kmp_date_time_picker.utils.now

@Composable
fun Cart(
    onSnackClick: (Long, String) -> Unit,
    onOrderDetail: (String, String) -> Unit,
    cartViewModel: CartViewModel,
    profileViewModel: ProfileViewModel,
    modifier: Modifier
) {

    val listCartData by cartViewModel.cartListState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

    var showCircularProgress by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // This ensures the API calls happen after the composable is fully set up
//        delay(3000)
        profileViewModel.getSelectedCartAddress()?.let { cartViewModel.setSelectedCartAddress(it) }
        cartViewModel.setAddressList(profileViewModel.getAddressList())
        cartViewModel.loadInitialData()
        cartViewModel.setSelectedPaymentMethod("qris")
        cartViewModel.setSelectedDeliveryMethod("pickup")
        cartViewModel.setSelectedDate(LocalDate.now())
        cartViewModel.setSelectedTime(LocalTime.now())
        cartViewModel.setOrderNote("")
        cartViewModel.setOrderState(ResultResponse.None)
    }

    LaunchedEffect(listCartData) {
        when (listCartData) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                Log.e(
                    "CART SCREEN",
                    "SUGSESS PRODUGGG: ${(listCartData as ResultResponse.Success).data}"
                )
//                navigateToRoute(MainDestinations.LOGIN_ROUTE, true)
            }

            is ResultResponse.Loading -> {
                showCircularProgress = true
                Log.e(
                    "CART SCREEN",
                    "LOADING"
                )
            }

            is ResultResponse.Error -> {
                showCircularProgress = false
                Log.e(
                    "ERRROOR",
                    "USERSTEERR error: ${(listCartData as ResultResponse.Error).error}"
                )
                // Display error message to the user
                // Toast.makeText(context, otpState.message, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    val cartData = when (listCartData) {
        is ResultResponse.Success -> (listCartData as ResultResponse.Success<CartListResponse>).data.data
        else -> emptyList()
    }

    Cart(
        modifier = modifier,
        onSnackClick = onSnackClick,
        onOrderDetail = onOrderDetail,
        cartData = cartData,
        cartViewModel = cartViewModel,
        showCircularProgress = showCircularProgress
    )
}

@Composable
private fun Cart(
    modifier: Modifier = Modifier,
    onSnackClick: (Long, String) -> Unit,
    onOrderDetail: (String, String) -> Unit,
    cartData: List<DataCartItem>,
    cartViewModel: CartViewModel,
    showCircularProgress: Boolean
) {

    FleuraSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(base20),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "Your Cart",
                    showNavigationIcon = false
                )

                if (showCircularProgress){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        CircularProgressIndicator(color = primaryLight)
                    }
                }
                else if (cartData.isNullOrEmpty()) {
                    EmptyCart(
                        painter = painterResource(id = R.drawable.cart_empty),
                        title = "You have not added anything",
                        description = "Explore the menu and add to your cart"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(cartData) { orderItem ->
                            CartSummary(
                                order = orderItem,
                                onOrderDetail = {
                                    cartViewModel.setSelectedCartItem(orderItem)
                                    onOrderDetail(
                                        orderItem.storeId,
                                        HomeSections.Cart.route
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}