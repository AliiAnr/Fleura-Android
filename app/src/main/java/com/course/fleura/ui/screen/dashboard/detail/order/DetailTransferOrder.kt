package com.course.fleura.ui.screen.dashboard.detail.order

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.course.fleura.R
import com.course.fleura.data.model.remote.OrderAddressData
import com.course.fleura.data.model.remote.OrderAddressResponse
import com.course.fleura.data.model.remote.OrderDataItem
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.common.extractOrderId
import com.course.fleura.ui.common.formatCurrency
import com.course.fleura.ui.common.parseDateTime
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.screen.dashboard.order.OrderViewModel
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base100
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base40
import com.course.fleura.ui.theme.base500
import com.course.fleura.ui.theme.base60
import com.course.fleura.ui.theme.err
import com.course.fleura.ui.theme.primaryLight
import com.course.fleura.ui.theme.secColor
import com.course.fleura.ui.theme.tert

@Composable
fun DetailTransferOrder(
    modifier: Modifier = Modifier,
    selectedOrderItem: OrderDataItem,
    orderViewModel: OrderViewModel,
    onBackClick: () -> Unit,
    onPaymentClick: () -> Unit,
    id: Long,
) {

    val realtimeOrderStatus by orderViewModel.realtimeOrderStatus.collectAsStateWithLifecycle()
    val realtimePaymentStatus by orderViewModel.realtimePaymentStatus.collectAsStateWithLifecycle()

    //call API here

    var showCircularProgress by remember { mutableStateOf(true) }

    val orderAddressState by orderViewModel.orderAddressState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    LaunchedEffect(selectedOrderItem.id) {
        orderViewModel.startWebSocketConnection(selectedOrderItem.id)
    }

    DisposableEffect(Unit) {
        onDispose {
            orderViewModel.stopWebSocketConnection()
        }
    }

    LaunchedEffect(Unit) {
        orderViewModel.loadInitialOrderData()
    }

    val updatedOrderItem = selectedOrderItem.copy(
        payment = selectedOrderItem.payment.copy(
            status = realtimePaymentStatus ?: selectedOrderItem.payment.status,
            successAt = selectedOrderItem.payment.successAt ?: "",
            qrisUrl = selectedOrderItem.payment.qrisUrl ?: "",
            qrisExpiredAt = selectedOrderItem.payment.qrisExpiredAt ?: "",
            methode = selectedOrderItem.payment.methode ?: ""
        ),
        status = realtimeOrderStatus ?: selectedOrderItem.status,
        note = selectedOrderItem.note ?: "",
        addressId = selectedOrderItem.addressId ?: ""
    )


    LaunchedEffect(orderAddressState) {
        when (orderAddressState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                Log.e(
                    "ORDER SCREEN",
                    "SUGSESS ADDRESS: ${(orderAddressState as ResultResponse.Success).data}"
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
                    "ORDER error: ${(orderAddressState as ResultResponse.Error).error}"
                )
                // Display error message to the user
                // Toast.makeText(context, otpState.message, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    val orderAddressData = when (orderAddressState) {
        is ResultResponse.Success -> (orderAddressState as ResultResponse.Success<OrderAddressResponse>).data.data
        else -> OrderAddressData(
            name = "John Doe",
            phone = "1234567890",
            detail = "123 Main St",
            road = "Main St",
            district = "Downtown",
            city = "Cityville",
            province = "Province",
            postcode = "12345",
            latitude = 0.0,
            longitude = 0.0,
            id = "2344"
        )
    }

    DetailTransferOrder(
        orderAddressData = orderAddressData,
        selectedOrderItem = updatedOrderItem, // Use updated item
        showCircularProgress = showCircularProgress,
        onBackClick = {
            orderViewModel.stopWebSocketConnection()
            onBackClick()
                      },
        onPaymentClick = onPaymentClick
    )
}

@Composable
private fun DetailTransferOrder(
    modifier: Modifier = Modifier,
    selectedOrderItem: OrderDataItem,
    showCircularProgress: Boolean,
//    orderData: List<CartItem>? = null,
    orderAddressData: OrderAddressData,
    onBackClick: () -> Unit,
    onPaymentClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var rating by remember { mutableIntStateOf(0) }
    var reviewComment by remember { mutableStateOf("") }
    val isButtonEnabled = rating > 0 && reviewComment.isNotEmpty()

    val isPaid = selectedOrderItem.payment.status == "paid"

    val isDelivery = selectedOrderItem.takenMethod == "delivery"

    val currentStatus = selectedOrderItem.status

    FleuraSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                },
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
                    title = "Detail Order",
                    showNavigationIcon = true,
                    onBackClick = onBackClick
                )

                if (showCircularProgress) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f))
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                // Do nothing - this prevents clicks from passing through
                            }
                    ) {
                        CircularProgressIndicator(color = primaryLight)
                    }
                } else {

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (!isPaid && selectedOrderItem.payment.methode != "cash") {
                                item {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    PaymentStatusSection(
                                        onPaymentClick = onPaymentClick
                                    )
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                DetailOrderStatus(
                                    isDelivery = isDelivery,
                                    currentStatus = currentStatus
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                DetailOrderAddressSection(
                                    selectedAddress = orderAddressData
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                DetailOrderMerchantProfileSection(
                                    storeData = selectedOrderItem
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(horizontal = 20.dp, vertical = 12.dp),
                                ) {
                                    Text(
                                        text = "Order Summary",
                                        color = Color.Black,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    selectedOrderItem.orderItems.forEachIndexed { index, item ->
                                        val isLastItem =
                                            index == selectedOrderItem.orderItems.size - 1
                                        OrderSummaryItem(
                                            quantity = item.quantity,
                                            name = item.product.name,
                                            description = item.product.description,
                                            price = item.product.price
                                        )

                                        if (!isLastItem) {
                                            HorizontalDivider(
                                                color = base40,
                                            )
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                DateAndTimeDisplay(
                                    selectedOrderItem = selectedOrderItem
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                NoteSection(
                                    note = selectedOrderItem.note ?: "-"
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                PaymentSummary(
                                    selectedOrderItem = selectedOrderItem
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                DetailOrderTotalPriceSection(
                                    selectedOrderItem = selectedOrderItem
                                )
                            }

//                        item {
//                            Spacer(modifier = Modifier.height(8.dp))
//                            StarSection(
//                                rating = rating,
//                                onRatingChanged = { rating = it },
//                            )
//                        }

//                        item {
//                            Spacer(modifier = Modifier.height(8.dp))
//                            AddReviewSection(
//                                reviewComment = reviewComment,
//                                onRatingChanged = { reviewComment = it }
//                            )
//                        }
                        }
                    }
                }

//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color.White)
//                        .height(90.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CustomButton(
//                        text = "Add Review",
//                        onClick = { },
//                        isAvailable = isButtonEnabled
//                    )
//                }
            }

        }
    }
}

@Composable
private fun PaymentStatusSection(
    modifier: Modifier = Modifier,
    onPaymentClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Text(
            text = "Order not yet paid!",
            color = err,
            fontSize = 14.sp,
            fontWeight = FontWeight.W700
        )
        Text(
            text = "Make a payment now so that your order can be processed by the seller",
            color = Color.Black,
            lineHeight = 20.sp,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .height(25.dp)
                .width(120.dp)
                .background(secColor)
                .clickable(
                    onClick = onPaymentClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Pay Now",
                color = Color.White,
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
private fun DetailOrderAddressSection(
    modifier: Modifier = Modifier,
    selectedAddress: OrderAddressData
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
        ) {

            Icon(
                painter = painterResource(id = R.drawable.loc),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column(
            ) {
                Text(
                    text = "Destination Address",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Column {
                    Text(
                        text = selectedAddress.name,
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = selectedAddress.phone,
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = buildString {
                            append(selectedAddress.detail)
                            if (selectedAddress.detail != "") append(", ")
                            append(selectedAddress.road)
                            append(", ")
                            append(selectedAddress.district)
                            append(", ")
                            append(selectedAddress.city)
                            append(", ")
                            append(selectedAddress.province)
                            append(" ")
                            append(selectedAddress.postcode)
                        },
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailOrderStatus(
    modifier: Modifier = Modifier,
    currentStatus: String = "completed",
    isDelivery: Boolean,
) {

    val statuses = if (isDelivery) {
        listOf("process", "delivery", "completed")
    } else {
        listOf("process", "pickup", "completed")
    }

    Box(
        modifier = Modifier
            .height(90.dp)
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .height(50.dp)
                .fillMaxWidth(),
        ) {
            HorizontalDivider(
                modifier = Modifier.padding(top = 12.dp),
                color = base60,
                thickness = 2.dp,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                statuses.forEach { status ->
                    StatusItem(status = status, currentStatus = currentStatus)
                }
            }
        }
    }
}

@Composable
private fun StatusItem(
    modifier: Modifier = Modifier,
    status: String,
    currentStatus: String
) {
    val iconRes = when (status) {
        "process" -> if (currentStatus == "process") R.drawable.check_circle else R.drawable.elipse
        "delivery" -> if (currentStatus == "delivery") R.drawable.check_circle else R.drawable.elipse
        "pickup" -> if (currentStatus == "pickup") R.drawable.check_circle else R.drawable.elipse
        "completed" -> if (currentStatus == "completed") R.drawable.check_circle else R.drawable.elipse
        else -> R.drawable.elipse
    }

    val statusText = when (status) {
        "process" -> "Processed"
        "delivery" -> "Delivery"
        "pickup" -> "Ready to Pickup"
        "completed" -> "Completed"
        else -> ""
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(25.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = if (currentStatus == status) MaterialTheme.colorScheme.primary else Color.Unspecified,
                modifier = Modifier.size(if (currentStatus == status) 24.dp else 10.dp)
            )
        }
        Text(
            text = statusText,
            color = if (currentStatus == status) Color.Black else base40,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun NoteSection(
    modifier: Modifier = Modifier,
    note: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Note",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (note.isEmpty()) "-" else note,
            color = Color.Black,
            fontSize = 12.sp,
            lineHeight = 20.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }

}

@Composable
private fun DateAndTimeDisplay(
    modifier: Modifier = Modifier,
    selectedOrderItem: OrderDataItem
) {

    val (pickupDate, pickupTime) = parseDateTime(selectedOrderItem.takenDate)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Column(
        ) {
            Text(
                text = "Pickup Date",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_edit),
                        contentDescription = null,
                        tint = base500,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "$pickupDate",
                        color = base100,
                        fontSize = 12.sp,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(

        ) {
            Text(
                text = "Pickup Time",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.clock),
                        contentDescription = null,
                        tint = base500,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$pickupTime",
                        color = base100,
                        fontSize = 12.sp,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentSummary(
    modifier: Modifier = Modifier,
    selectedOrderItem: OrderDataItem
) {

    val paymentMethod = when (selectedOrderItem.payment.methode) {
        "qris" -> "QRIS"
        "cash" -> "CASH"
        else -> "Unknown Payment Method"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Payment Method",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.cash),
                contentDescription = null,
                tint = base500,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = paymentMethod,
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "Order Number",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.order_number),
                contentDescription = null,
                tint = base500,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "#${extractOrderId(selectedOrderItem.id)}",
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DetailOrderTotalPriceSection(
    modifier: Modifier = Modifier,
    selectedOrderItem: OrderDataItem
) {

    val subtotal = selectedOrderItem.total

    val deliveryFee = if (selectedOrderItem.takenMethod == "delivery") 15000 else 0


    val paymentStatus = when (selectedOrderItem.payment.status) {
        "paid" -> "Paid"
        "unpaid" -> "Unpaid"
        else -> "Unknown Status"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total Order",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = formatCurrency(subtotal.toLong()),
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Delivery Fee",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = formatCurrency(deliveryFee.toLong()),
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total Payment",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatCurrency(subtotal.toLong()),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Payment Status",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = paymentStatus,
                color = if (selectedOrderItem.payment.status == "paid") tert else err,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(26.dp))
    }
}

@Composable
fun StarSection(
    modifier: Modifier = Modifier,
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Rate the Product!",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        RatingBar(
            rating = rating,
            onRatingSelected = onRatingChanged
        )
    }
}

@Composable
fun RatingBar(
    rating: Int,
    onRatingSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(5) { index ->
            val starIndex = index + 1
            Icon(
                painter = painterResource(if (starIndex <= rating) R.drawable.star else R.drawable.empty_star),
                contentDescription = "Star $starIndex",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onRatingSelected(starIndex) }
            )
        }
    }
}

@Composable
fun AddReviewSection(
    modifier: Modifier = Modifier,
    reviewComment: String,
    onRatingChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 30.dp),
    ) {
        Text(
            text = "Add review",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        BasicTextField(
            value = reviewComment,
            onValueChange = onRatingChanged,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 12.sp,
                color = Color.Black
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (reviewComment.isEmpty()) {
                        Text(
                            text = "Add your review of the product...",
                            color = Color.Gray,
                            style = LocalTextStyle.current.copy(
                                fontSize = 12.sp,
                                color = base100
                            )
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}

@Composable
fun DetailOrderMerchantProfileSection(
    modifier: Modifier = Modifier,
    storeData: OrderDataItem
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (storeData.orderItems[0].product.store.logo.isNullOrEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.placeholder),  // Use a placeholder image
                contentDescription = "Store Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        } else {
            AsyncImage(
                model = storeData.orderItems[0].product.store.logo,
                contentDescription = "Store Image",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.placeholder),
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }

        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = storeData.orderItems[0].product.store.name,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}