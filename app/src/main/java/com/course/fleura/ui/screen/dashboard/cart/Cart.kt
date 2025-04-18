package com.course.fleura.ui.screen.dashboard.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.course.fleura.R
import com.course.fleura.ui.components.CartSummary
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.EmptyCart
import com.course.fleura.ui.components.FakeCategory
import com.course.fleura.ui.components.Order
import com.course.fleura.ui.components.OrderItemCard
import com.course.fleura.ui.components.OrderSummary
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base20

@Composable
fun Cart(
    onSnackClick: (Long, String) -> Unit,
    modifier: Modifier
) {

    //Call API disini dan passing data UiState ke Cart
    // Data result API akan di passing ke cart
    Cart(
        modifier = modifier,
        onSnackClick = onSnackClick,
        data = 12,
        orderList = FakeCategory.orders
    )
}

@Composable
private fun Cart(
    modifier: Modifier = Modifier,
    orderList: List<Order>,
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
                CustomTopAppBar(
                    title = "Your Cart",
                    showNavigationIcon = false
                )

                if (orderList.isEmpty()) {
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
                        items(orderList) { orderList ->
                            CartSummary(order = orderList)
                        }
                    }
                }
            }
        }
    }
}