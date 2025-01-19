package com.course.fleura.ui.screen.dashboard.detail.order

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.datetime.LocalDate
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.course.fleura.R
import com.course.fleura.ui.common.formatCurrency
import com.course.fleura.ui.components.CartItem
import com.course.fleura.ui.components.CartOrder
import com.course.fleura.ui.components.CustomButton
import com.course.fleura.ui.components.CustomTextInput
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.FakeCategory
import com.course.fleura.ui.components.HistoryTopBar
import com.course.fleura.ui.components.Profile
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base100
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base300
import com.course.fleura.ui.theme.base40
import com.course.fleura.ui.theme.base500
import com.course.fleura.ui.theme.tert
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.toJavaLocalDate
import network.chaintech.kmp_date_time_picker.ui.date_range_picker.formatToString
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerDialog
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.ui.timepicker.WheelTimePickerDialog
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.TimeFormat
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults.selectorProperties
import network.chaintech.kmp_date_time_picker.utils.now
import java.time.format.DateTimeFormatter
import kotlin.text.compareTo

@Composable
fun ConfirmOrder(
    modifier: Modifier = Modifier,
    flowerId: Long,
) {
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
        )
    )
}

@Composable
private fun ConfirmOrder(
    modifier: Modifier = Modifier,
    userData: Profile? = null,
    orderData: List<CartItem>? = null
) {

    val focusManager = LocalFocusManager.current

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
                    showNavigationIcon = true
                )
                Box(
                    modifier = Modifier.weight(1f)
                ) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            OrderPickupSection()
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            AddressSection()
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            MerchantProfileSection()
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

                                orderData?.forEachIndexed { index, item ->
                                    val isLastItem = index == orderData.size - 1
                                    OrderSummaryItem(
                                        quantity = item.quantity,
                                        name = item.cartOrder.name,
                                        description = item.cartOrder.description,
                                        price = item.cartOrder.price
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
                            DateAndTimeSection()
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            NoteSection()
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            PaymentMethodSection()
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            TotalPriceSection()
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
                            append(formatCurrency(10000))
                        }
                    }
                    CustomButton(
                        textAnnotatedString = text,
                        onClick = { }
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderPickupSection(
    modifier: Modifier = Modifier
) {

    val temp = remember { mutableIntStateOf(1) }
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
                    }
                )
                Spacer(modifier = Modifier.size(8.dp))
                PickupChoice(
                    label = "Delivery",
                    isEnabled = if (temp.intValue == 0) 1 else 0,
                    onClick = {
                        temp.intValue = 0
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
private fun AddressSection(
    modifier: Modifier = Modifier
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

                Text(
                    text = "Lorem ipsulor sit amet. Nam voluptatem tenetur et voluptas nesciunt a quia voluptatem. tenetur et voluptas nesciunt a quia voluptatem.",
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .height(50.dp)
                        .padding(end = 20.dp)
                )
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
private fun MerchantProfileSection(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.store_1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = "Buga Adik",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun OrderSummaryItem(
    modifier: Modifier = Modifier,
    quantity: Int = 0,
    name: String = "",
    description: String = "",
    price: Long
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
                text = "5x",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column(
            ) {
                Text(
                    text = "Sunflower Bouquet",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Lorem ipsulor sit amet. Nam voluptatem tenetur et voluptas nesciunt a quia voluptatem. tenetur et voluptas nesciunt a quia voluptatem.",
                    color = base100,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .height(50.dp)
                        .padding(end = 20.dp)
                )
            }
        }
        Text(
            text = formatCurrency(10000),
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DateAndTimeSection(
    modifier: Modifier = Modifier
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

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
                selectedDate = date
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
                selectedTime = time
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
                        painter = painterResource(id = R.drawable.pen_date),
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
) {
    val temp = remember { mutableStateOf("") }
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
            value = temp.value,
            onChange = { temp.value = it },
            placeholder = "Ex : Use purple color",
            horizontalPadding = 0.dp,
        )
    }
}

@Composable
private fun PaymentMethodSection(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Pickup Time",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pickup Time",
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
private fun TotalPriceSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp)
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
                text = formatCurrency(10000),
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
                text = formatCurrency(10000),
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
                text = "Pickup Time",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatCurrency(10000),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

