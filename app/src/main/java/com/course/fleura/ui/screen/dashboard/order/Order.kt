package com.course.fleura.ui.screen.dashboard.order

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
fun Order(
    onSnackClick: (Long, String) -> Unit,
    modifier: Modifier
) {
    Order(
        modifier = modifier,
        onSnackClick = onSnackClick,
        data = 12,
        orderList = null
    )
}

@Composable
private fun Order(
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
                )

                if (orderList?.items?.isEmpty() != false) {
                    EmptyCart(
                        painter = painterResource(id = R.drawable.order_empty),
                        title = "You have not order anything yet",
                        description = "Go order today and will show up here"
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