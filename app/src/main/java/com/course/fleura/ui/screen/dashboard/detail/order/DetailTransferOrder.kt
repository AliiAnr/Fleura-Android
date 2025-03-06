package com.course.fleura.ui.screen.dashboard.detail.order

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
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
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base100
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base40
import com.course.fleura.ui.theme.base500
import com.course.fleura.ui.theme.base60
import com.course.fleura.ui.theme.err
import com.course.fleura.ui.theme.secColor
import com.course.fleura.ui.theme.tert
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import network.chaintech.kmp_date_time_picker.ui.date_range_picker.formatToString
import network.chaintech.kmp_date_time_picker.utils.now
import kotlin.compareTo

@Composable
fun DetailTranferOrder(
    modifier: Modifier = Modifier,
    id: Long,
) {

    //call API here

    DetailTransferOrder(
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
private fun DetailTransferOrder(
    modifier: Modifier = Modifier,
    isPaid: Boolean = true,
    orderData: List<CartItem>? = null
) {
    val focusManager = LocalFocusManager.current
    var rating by remember { mutableIntStateOf(0) }
    var reviewComment by remember { mutableStateOf("") }
    val isButtonEnabled = rating > 0 && reviewComment.isNotEmpty()
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
                    showNavigationIcon = true
                )
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (isPaid) {
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                PaymentStatusSection()
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            OrderStatus(
                                isDelivery = true
                            )
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
                            DateAndTimeDisplay(
                                date = LocalDate.now(),
                                time = LocalTime.now()
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            NoteSection(
                                note = "Lorem ipsesciunt a quia voluptatem."
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            PaymentSummary()
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            TotalPriceSection()
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            StarSection(
                                rating = rating,
                                onRatingChanged = { rating = it },
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            AddReviewSection(
                                reviewComment = reviewComment,
                                onRatingChanged = { reviewComment = it }
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
                    CustomButton(
                        text = "Add Review",
                        onClick = { },
                        isAvailable = isButtonEnabled
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentStatusSection(
    modifier: Modifier = Modifier
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
                .background(secColor),
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
    }
}

@Composable
private fun OrderStatus(
    modifier: Modifier = Modifier,
    currentStatus: String = "completed",
    isDelivery: Boolean
) {
    val statuses = if (isDelivery) {
        listOf("process", "delivery", "completed")
    } else {
        listOf("process", "ready_+to_pickup", "completed")
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
        "ready_to_pickup" -> if (currentStatus == "ready_to_pickup") R.drawable.check_circle else R.drawable.elipse
        "completed" -> if (currentStatus == "completed") R.drawable.check_circle else R.drawable.elipse
        else -> R.drawable.elipse
    }

    val statusText = when (status) {
        "process" -> "Processed"
        "delivery" -> "Delivery"
        "ready_to_pickup" -> "Ready to Pickup"
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
    note: String = ""
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
            overflow = TextOverflow.Ellipsis,
            lineHeight = 20.sp,
            maxLines = Int.MAX_VALUE,
            modifier = Modifier.height(if (note.isEmpty()) 20.dp else 50.dp)
        )
    }

}

@Composable
private fun DateAndTimeDisplay(
    modifier: Modifier = Modifier,
    date: LocalDate,
    time: LocalTime,
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
                        text = date.formatToString(formatType = "MMMM dd yyyy"),
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
            }
        }
    }
}

@Composable
private fun PaymentSummary(
    modifier: Modifier = Modifier
) {
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
                text = "QRIS",
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
                text = "#1239",
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
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
                text = "Total Payment",
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
                text = "Unpaid",
                color = err,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
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