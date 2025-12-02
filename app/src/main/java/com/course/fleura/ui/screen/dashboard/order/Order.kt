package com.course.fleura.ui.screen.dashboard.order

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
import com.course.fleura.data.model.remote.OrderDataItem
import com.course.fleura.data.model.remote.OrderListResponse
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.components.CreatedOrderSummary
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.EmptyCart
import com.course.fleura.ui.components.FakeCategory
import com.course.fleura.ui.components.HistoryTopBar
import com.course.fleura.ui.components.HomeSections
import com.course.fleura.ui.components.Order
import com.course.fleura.ui.components.OrderSummary
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.primaryLight

@Composable
fun Order(
    modifier: Modifier,
    orderViewModel: OrderViewModel,
    onCreatedOrderDetail: (String, String) -> Unit,
    onOrderHistory: () -> Unit
) {

    val orderListState by orderViewModel.orderListState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

    var showCircularProgress by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        orderViewModel.loadInitialData()
    }

    LaunchedEffect(orderListState) {
        when (orderListState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                Log.e(
                    "ORDER SCREEN",
                    "SUGSESS ORDER: ${(orderListState as ResultResponse.Success).data}"
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
                    "ORDER error: ${(orderListState as ResultResponse.Error).error}"
                )
                // Display error message to the user
                // Toast.makeText(context, otpState.message, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    val orderList = when (orderListState) {
        is ResultResponse.Success -> (orderListState as ResultResponse.Success<OrderListResponse>).data.data
        else -> emptyList()
    }

    Order(
        modifier = modifier,
        orderList = orderList,
        showCircularProgress = showCircularProgress,
        orderViewModel = orderViewModel,
        onCreatedOrderClick = onCreatedOrderDetail,
        onOrderHistory = onOrderHistory
    )

}

@Composable
private fun Order(
    modifier: Modifier = Modifier,
    orderList: List<OrderDataItem>,
    showCircularProgress: Boolean,
    orderViewModel: OrderViewModel,
    onCreatedOrderClick: (String, String) -> Unit,
    onOrderHistory: () -> Unit,
) {

    val validOrderList = orderList.filter { it.orderItems.isNotEmpty() && it.status != "completed" }

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
                HistoryTopBar(
                    title = "Order",
                    onHistoryClick = onOrderHistory
                )

                if (showCircularProgress){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        CircularProgressIndicator(color = primaryLight)
                    }
                } else if (orderList.isNullOrEmpty() || validOrderList.isEmpty()) {
                    Log.e("ORDER SCREEN", "ORDER LIST IS EMPTY")
                    EmptyCart(
                        painter = painterResource(id = R.drawable.order_empty),
                        title = "You have not order anything yet",
                        description = "Go order today and will show up here"
                    )
                } else {
                    Log.e("ORDER SCREEN", "ORDER LIST: ${orderList[0].orderItems}")
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        items(validOrderList) { orderItem ->
                            CreatedOrderSummary(
                                orderItem = orderItem,
                                onCreatedOrderDetail = {
                                    orderViewModel.setSelectedOrderItem(orderItem)
                                    onCreatedOrderClick(
                                        orderItem.id,
                                        HomeSections.Order.route
                                    )
                                }
                            )
                        }

                        //set selected order, dan show detail

                    }
                }
            }
        }
    }
}