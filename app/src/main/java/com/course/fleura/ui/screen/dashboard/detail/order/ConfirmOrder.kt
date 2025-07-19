package com.course.fleura.ui.screen.dashboard.detail.order

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.course.fleura.R
import com.course.fleura.data.model.remote.AddressItem
import com.course.fleura.data.model.remote.DataCartItem
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.common.formatCurrency
import com.course.fleura.ui.common.formatCurrencyFromString
import com.course.fleura.ui.common.getTotalPrice
import com.course.fleura.ui.components.CartItem
import com.course.fleura.ui.components.CartOrder
import com.course.fleura.ui.components.CustomButton
import com.course.fleura.ui.components.CustomPopUpDialog
import com.course.fleura.ui.components.CustomTextInput
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.Profile
import com.course.fleura.ui.screen.dashboard.cart.CartViewModel
import com.course.fleura.ui.screen.dashboard.cart.DeliveryMethod
import com.course.fleura.ui.screen.dashboard.cart.PaymentMethod
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base100
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base300
import com.course.fleura.ui.theme.base40
import com.course.fleura.ui.theme.base500
import com.course.fleura.ui.theme.primaryLight
import com.course.fleura.ui.theme.tert
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import network.chaintech.kmp_date_time_picker.ui.date_range_picker.formatToString
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerDialog
import network.chaintech.kmp_date_time_picker.ui.timepicker.WheelTimePickerDialog
import network.chaintech.kmp_date_time_picker.utils.TimeFormat
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults.selectorProperties
import network.chaintech.kmp_date_time_picker.utils.now

@Composable
fun ConfirmOrder(
    modifier: Modifier = Modifier,
    storeId: String,
    origin: String,
    cartViewModel: CartViewModel,
    selectedCartItem: DataCartItem,
    onChooseClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onOrderSuccess: () -> Unit
) {

    val selectedDeliveryMethod by cartViewModel.deliveryMethod.collectAsStateWithLifecycle()

    val totalPayment by cartViewModel.totalPayment.collectAsStateWithLifecycle()
    val selectedAddress by cartViewModel.selectedCartAddress.collectAsStateWithLifecycle()

    val orderState by cartViewModel.orderState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

    var showCircularProgress by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(orderState) {
        when (orderState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                showSuccessDialog = true
            }

            is ResultResponse.Loading -> {
                showCircularProgress = true
            }

            is ResultResponse.Error -> {
                showCircularProgress = false
            }

            else -> {
                // Do nothing
            }
        }
    }

    LaunchedEffect(selectedDeliveryMethod) {
        Log.e("ConfirmOrder", "Selected Delivery Method: $selectedDeliveryMethod")

    }

    LaunchedEffect (selectedCartItem) {
        Log.e("ConfirmOrder", "Selected Cart Item: $selectedCartItem")
    }

    LaunchedEffect(selectedAddress) {
        Log.e("ConfirmOrder", "Selected Address: $selectedAddress")
    }

    LaunchedEffect(selectedCartItem, selectedDeliveryMethod) {
        cartViewModel.calculateTotalPayment(selectedCartItem)
    }

    ConfirmOrder(
        orderData = listOf(
            CartItem(
                quantity = 5,
                cartOrder = CartOrder(
                    name = "Sunflower Bouquet",
                    description = "Lorem ipsulor sit amet. Nam voluptatem tenetur et voluptas nesciunt a quia voluptatem. tenetur et voluptas nesciunt a quia voluptatem.",
                    price = 10000
                )
            )
        ),
        selectedCartItem = selectedCartItem,
        onBackClick = onBackClick,
        cartViewModel = cartViewModel,
        deliveryMethod = selectedDeliveryMethod,
        onChooseClick = onChooseClick,
        totalPayment = totalPayment,
        showCircularProgress = showCircularProgress,
        showSuccessDialog = showSuccessDialog,
        onSuccessDialogDismiss = {
            showSuccessDialog = false
        },
        onQrisPayment = {
            onOrderSuccess()
        }
    )
}

@Composable
private fun ConfirmOrder(
    modifier: Modifier = Modifier,
    userData: Profile? = null,
    orderData: List<CartItem>? = null,
    selectedCartItem: DataCartItem,
    onBackClick: () -> Unit,
    cartViewModel: CartViewModel,
    deliveryMethod: DeliveryMethod,
    onChooseClick: (String) -> Unit,
    totalPayment: Long,
    showCircularProgress: Boolean,
    showSuccessDialog: Boolean,
    onSuccessDialogDismiss: () -> Unit,
    onQrisPayment: () -> Unit
) {

    val focusManager = LocalFocusManager.current

    var showConfirmDialog by remember { mutableStateOf(false) }

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
                    title = "Confirm Order",
                    showNavigationIcon = true,
                    onBackClick = onBackClick
                )
                Box(
                    modifier = Modifier.weight(1f)
                ) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            ConfirmOrderPickupSection(
                                setSelectedDeliveryMethod = { selectedDeliveryMethod ->
                                    cartViewModel.setSelectedDeliveryMethod(selectedDeliveryMethod)
                                },
                                method = cartViewModel.getSelectedDeliveryMethod()
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            cartViewModel.getSelectedCartAddress()?.let {
                                ConfirmOrderAddressSection(
                                    onAddressClick = onChooseClick,
                                    selectedAddress = it
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            MerchantProfileSection(
                                storeData = selectedCartItem
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

                                selectedCartItem.items.forEachIndexed { index, item ->
                                    val isLastItem = index == selectedCartItem.items.size - 1
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
                            DateAndTimeSection(
                                cartViewModel = cartViewModel
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            PaymentMethodSection(
                                onPaymentMethodClick = onChooseClick,
                                paymentMethod = cartViewModel.getSelectedPaymentMethod()
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            NoteSection(
                                cartViewModel = cartViewModel
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            ConfirmOrderTotalPriceSection(
                                deliveryMethod = deliveryMethod,
                                selectedCartItem = selectedCartItem
                            )
                        }

                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .height(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.W600
                            )
                        ) {
                            append("Checkout\n")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 14.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(formatCurrency(totalPayment))
                        }
                    }
                    CustomButton(
                        textAnnotatedString = text,
                        onClick = {
                            showConfirmDialog = true
                        }
                    )
                }
            }

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
            }

            if (showSuccessDialog) {
                CustomPopUpDialog(
                    onDismiss = {
                        if (cartViewModel.getSelectedPaymentMethod() == PaymentMethod.QRIS.displayName) {
                            onSuccessDialogDismiss()
                            onQrisPayment()
                        } else {
                            onSuccessDialogDismiss()
                            onBackClick()
                        }
                    },
                    isShowIcon = true,
                    isShowTitle = true,
                    isShowDescription = true,
                    isShowButton = false,
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ceklist),
                                contentDescription = null,
                                tint = Color.Unspecified,
                            )
                        }
                    },
                    title = "Order Successful",
                    description = "Your order has been successfully placed. Thank you for shopping with us!. Check your order for payment details.",
                )
            }

            if (showConfirmDialog) {
                CustomPopUpDialog(
                    onDismiss = { showConfirmDialog = false },
                    isShowIcon = true,
                    isShowTitle = true,
                    isShowDescription = false,
                    isShowButton = true,
                    icon = {

                        Icon(
                            painter = painterResource(id = R.drawable.think),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.height(150.dp)
                        )
                    },
                    title = "Are you sure you want to continue?",
                    buttons = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedButton(
                                onClick = { showConfirmDialog = false },
                                border = BorderStroke(1.dp, primaryLight),
                                shape = RoundedCornerShape(28.dp),
                                modifier = Modifier.weight(1f).padding(end = 6.dp)
                            ) {
                                Text("Cancel", color = primaryLight)
                            }
                            Button(
                                onClick = {
                                    cartViewModel.createOrder()
                                    showConfirmDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryLight
                                ),
                                shape = RoundedCornerShape(28.dp),
                                modifier = Modifier.weight(1f).padding(start = 6.dp)
                            ) {
                                Text("Confirm", color = Color.White)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ConfirmOrderPickupSection(
    modifier: Modifier = Modifier,
    setSelectedDeliveryMethod: (String) -> Unit,
    method: String
) {

    val temp = remember { mutableIntStateOf(
        when (method) {
            "Pickup" -> 1
            "Delivery" -> 0
            else -> 0
        }
    ) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.pick_box),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column(

        ) {
            Text(
                text = "Order Pickup",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Row {
                PickupChoice(
                    label = "Pickup",
                    isEnabled = if (temp.intValue == 1) 1 else 0,
                    onClick = {
                        temp.intValue = 1
                        setSelectedDeliveryMethod(
                            when (temp.intValue) {
                                1 -> "Pickup"
                                else -> "Delivery"
                            }
                        )
                    }
                )
                Spacer(modifier = Modifier.size(8.dp))
                PickupChoice(
                    label = "Delivery",
                    isEnabled = if (temp.intValue == 0) 1 else 0,
                    onClick = {
                        temp.intValue = 0
                        setSelectedDeliveryMethod(
                            when (temp.intValue) {
                                1 -> "Pickup"
                                else -> "Delivery"
                            }
                        )
                    }
                )
            }
        }
    }

}

@Composable
private fun PickupChoice(
    modifier: Modifier = Modifier,
    label: String,
    isEnabled: Int,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(15.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50.dp))
                .clickable(
                    onClick = onClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        if (isEnabled == 1) tert else Color.White
                    )
                    .padding(2.dp)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = Color.Black,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun ConfirmOrderAddressSection(
    modifier: Modifier = Modifier,
    onAddressClick:(String) -> Unit,
    selectedAddress: AddressItem
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onAddressClick(
                        "Select Address"
                    )
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
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
        Icon(
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun MerchantProfileSection(
    modifier: Modifier = Modifier,
    storeData: DataCartItem
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (storeData.storePicture.isNullOrEmpty()) {
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
                model = storeData.storePicture,
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
            text = storeData.storeName,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun OrderSummaryItem(
    modifier: Modifier = Modifier,
    quantity: Int = 0,
    name: String = "",
    description: String = "",
    price: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = "$quantity" + "x",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column(
            ) {
                Text(
                    text = name,
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = base100,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .heightIn(max = 50.dp)
                        .padding(end = 20.dp)
                )
            }
        }

        Text(
            text = formatCurrencyFromString(price),
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DateAndTimeSection(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel
) {
    val selectedDate by cartViewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedTime by cartViewModel.selectedTime.collectAsStateWithLifecycle()

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(selectedTime) {
        Log.e("DateAndTimeSection", "Selected Time: $selectedTime")
        Log.e("RESULTTTTDETTIME", cartViewModel.getFormattedDateTime())
    }

    LaunchedEffect(selectedDate) {
        Log.e("DateAndTimeSection", "Selected Date: $selectedDate")
    }

    DateAndTimeDisplay(
        date = selectedDate,
        time = selectedTime,
        onDateClick = { showDatePicker = true },
        onTimeClick = { showTimePicker = true }
    )

    if (showDatePicker) {
        WheelDatePickerDialog(
            modifier = Modifier.padding(vertical = 16.dp),
            showDatePicker = showDatePicker,
            onDismiss = { showDatePicker = false },
            onDoneClick = { date ->
                cartViewModel.setSelectedDate(date)
                showDatePicker = false
            },
            minDate = LocalDate.now(),
            title = "Pick Date",
            titleStyle = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            doneLabelStyle = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            dateTextColor = Color.Black,
            dateTextStyle = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            ),
            selectorProperties = selectorProperties(
                enabled = true,
                borderColor = MaterialTheme.colorScheme.primary
            ),
            yearsRange = 2025..2025,
            height = 200.dp,
        )
    }

    if (showTimePicker) {
        WheelTimePickerDialog(
            modifier = Modifier.padding(vertical = 16.dp),
            showTimePicker = showTimePicker,
            onDismiss = { showTimePicker = false },
            onDoneClick = { time ->
                cartViewModel.setSelectedTime(time)
                showTimePicker = false
            },
            startTime = selectedTime,
            title = "Pick Time",
            minTime = LocalTime.now(),
            titleStyle = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            doneLabelStyle = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            ),
            textColor = Color.Black,
            selectorProperties = selectorProperties(
                enabled = true,
                borderColor = MaterialTheme.colorScheme.primary
            ),
            timeFormat = TimeFormat.HOUR_24,
            height = 200.dp,
        )
    }
}

@Composable
private fun DateAndTimeDisplay(
    modifier: Modifier = Modifier,
    date: LocalDate,
    time: LocalTime,
    onDateClick: () -> Unit = {},
    onTimeClick: () -> Unit = {}
) {
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
                    .fillMaxWidth()
                    .clickable(
                        onClick = onDateClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }),
                horizontalArrangement = Arrangement.SpaceBetween,
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
                        text = date.formatToString(formatType = "MMMM dd yyyy"),
                        color = base100,
                        fontSize = 12.sp,
                    )
                }
                Text(
                    text = "(click to change)",
                    color = base100,
                    fontSize = 12.sp,
                )
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
                    .fillMaxWidth()
                    .clickable(
                        onClick = onTimeClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }),
                horizontalArrangement = Arrangement.SpaceBetween,
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
                        text = time.format(LocalTime.Format {
                            hour()
                            char(':')
                            minute()
                        }),
                        color = base100,
                        fontSize = 12.sp,
                        modifier = Modifier
                    )
                }
                Text(
                    text = "(click to change)",
                    color = base100,
                    fontSize = 12.sp,
                )
            }
        }
    }
}


@Composable
private fun NoteSection(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel
) {
    val orderNote by cartViewModel.orderNote.collectAsStateWithLifecycle(initialValue = "")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                ) {
                    append("Note to seller ")
                }
                withStyle(
                    style = SpanStyle(
                        color = base300,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("(Optional)")
                }
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        CustomTextInput(
            value = orderNote,
            onChange = {
                cartViewModel.setOrderNote(it)
                       },
            placeholder = "Ex : Use purple color",
            horizontalPadding = 0.dp,
        )
    }
}

@Composable
private fun PaymentMethodSection(
    modifier: Modifier = Modifier,
    onPaymentMethodClick: (String) -> Unit,
    paymentMethod: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onPaymentMethodClick(
                        "Payment Method"
                    )
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
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
            Text(
                text = paymentMethod,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = null,
                tint = base500,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun ConfirmOrderTotalPriceSection(
    modifier: Modifier = Modifier,
    deliveryMethod: DeliveryMethod,
    selectedCartItem: DataCartItem
) {
    // Calculate subtotal from all items
    val subtotal = selectedCartItem.getTotalPrice()

    // Set delivery fee based on selected method
    val deliveryFee = if (deliveryMethod == DeliveryMethod.DELIVERY) 15000 else 0

    // Calculate total payment
    val totalPayment = subtotal + deliveryFee

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Column content remains the same
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
                text = "Total",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatCurrency(totalPayment.toLong()),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}