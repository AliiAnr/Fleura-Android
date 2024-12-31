package com.course.fleura.ui.screen.dashboard.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.FakeCategory
import com.course.fleura.ui.components.HistoryTopBar
import com.course.fleura.ui.components.Order
import com.course.fleura.ui.components.OrderSummary
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
        orderList = FakeCategory.orders[0]
    )
}

@Composable
private fun Order(
    modifier: Modifier = Modifier,
    orderList: Order,
    onSnackClick: (Long, String) -> Unit,
    data: Int = 0
) {

    FleuraSurface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(base20),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
            ) {
                item {
                    HistoryTopBar(
                        title = "Order",
                    )
                }

                item{
                    Text(
                        text = "Order Summary",
                    )
                }

                item {

                }
            }
        }
    }

}