package com.course.fleura.ui.screen.dashboard.detail.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.course.fleura.R
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.EmptyCart
import com.course.fleura.ui.components.HistoryTopBar
import com.course.fleura.ui.components.Order
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base20

@Composable
fun OrderHistory(
    modifier: Modifier = Modifier,
) {
    OrderHistory(
        modifier = modifier,
        orderList = null,
        onSnackClick = { _, _ -> }
    )
}

@Composable
private fun OrderHistory(
    modifier: Modifier = Modifier,
    orderList: Order?,
    onSnackClick: (Long, String) -> Unit,
    data: Int = 0
) {

    FleuraSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(base20),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "Order History",
                    showNavigationIcon = true,
                )

                if (orderList?.items?.isEmpty() != false) {
                    EmptyCart(
                        painter = painterResource(id = R.drawable.order_history_empty),
                        title = "You have not finished any order yet",
                        description = "Start ordering now!"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        item {
//                    OrderSummary(order = orderList)
                        }
                    }
                }
            }
        }
    }

}