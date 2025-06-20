package com.course.fleura.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.course.fleura.R
import com.course.fleura.data.model.remote.DataCartItem
import com.course.fleura.data.model.remote.ItemFromCart
import com.course.fleura.ui.common.formatCurrency
import com.course.fleura.ui.common.formatCurrencyFromString
import com.course.fleura.ui.common.formatNumber
import com.course.fleura.ui.common.getTotalPrice
import com.course.fleura.ui.theme.base100
import com.course.fleura.ui.theme.base40
import com.course.fleura.ui.theme.err
import com.course.fleura.ui.theme.listOrderColor
import com.course.fleura.ui.theme.tert

@Composable
fun RedeemItemCard(item: OrderItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = listOrderColor, shape = RoundedCornerShape(10.dp))
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = item.name,
            modifier = Modifier
                .padding(start = 12.dp)
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = "${item.quantity} item",
                    color = base100,
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.LineThrough
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.star_point),
                    contentDescription = "Points",
                    modifier = Modifier.size(18.dp),
                    tint = Color.Unspecified
                )
                Text(
                    text = "${item.points}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Composable
fun OrderItemCard(
    item: ItemFromCart
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, base40, shape = RoundedCornerShape(10.dp))
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (item.product.picture.isNullOrEmpty()) {
//            Log.e("MerchantFlowerItem", "Image URL is null or empty")
            Image(
                painter = painterResource(id = R.drawable.placeholder),  // Use a placeholder image
                contentDescription = "Store Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        } else {
//            Log.d("MerchantFlowerItem", "Image URL: ${item.picture}")
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(item.picture.firstOrNull()?.path)
//                    .crossfade(true)
//                    .memoryCachePolicy(CachePolicy.ENABLED)
//                    .build(),
//                contentDescription = "Store Image",
////                contentScale = ContentScale.Crop,
//                placeholder = painterResource(id = R.drawable.placeholder),
//                error = painterResource(id = R.drawable.placeholder),
//                modifier = Modifier
//                    .size(110.dp)
//                    .clip(RoundedCornerShape(10.dp))
//            )
            AsyncImage(
                model = item.product.picture[0].path,
                contentDescription = item.product.name,
                placeholder = painterResource(R.drawable.placeholder),
                error       = painterResource(R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = item.product.name,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(text = "${item.quantity} item", color = base100, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatCurrencyFromString(item.product.price),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun PointCard(
    modifier: Modifier = Modifier,
    data: RewardItem,
    onRedeemClicked: (Long, String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
        border = BorderStroke(1.dp, base40),
        modifier = modifier
            .width(166.dp)
            .height(196.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = data.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.height(40.dp)
            )
            Image(
                painter = painterResource(id = data.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(100.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatNumber(data.points) + " Points",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            CustomButton(
                text = "Redeem",
                onClick = { onRedeemClicked(data.points, data.name) },
                fontSize = 14.sp,
                defaultHeight = 28.dp,
                defaultWidth = 123.dp,
                textVerticalPadding = 0.dp,
                textHorizontalPadding = 0.dp,
                isAvailable = data.available,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun OrderSummary(
    order: Order,
    isCompleted: Boolean = false
) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .background(
                Color.White
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.shop),
                        contentDescription = "Store Icon",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = order.storeName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Store Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            order.items.forEach { item ->
                if (order.isRedeemOrder) {
                    RedeemItemCard(item)
                } else {
//                    OrderItemCard(item)

                    //INIII
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .height(25.dp)
                    .width(120.dp)
                    .background(if (isCompleted) tert else err),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isCompleted) "Paid" else "Unpaid",
                    color = Color.White,
                    fontSize = 14.sp,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Total Pesanan:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = base100
                )
                if (order.isRedeemOrder) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.star_point), // Ikon bintang
                            contentDescription = "Points",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = "${order.totalPoints}",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                } else {
                    Text(
                        text = formatCurrency(order.totalPrice),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CartSummary(
    order: DataCartItem,
    onOrderDetail: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .background(
                Color.White
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.shop),
                        contentDescription = "Store Icon",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = order.storeName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Store Icon",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(
                            onClick = {
                                onOrderDetail()
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            order.items.forEach { item ->
//                if (order.isRedeemOrder) {
//                    RedeemItemCard(item)
//                } else {
                OrderItemCard(item)
//                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Total:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = base100
                )
//                if (order.isRedeemOrder) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.padding(start = 8.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.star_point), // Ikon bintang
//                            contentDescription = "Points",
//                            tint = Color.Unspecified,
//                            modifier = Modifier.size(20.dp),
//                        )
//                        Text(
//                            text = "${order.totalPoints}",
//                            color = MaterialTheme.colorScheme.primary,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 16.sp,
//                            modifier = Modifier.padding(start = 4.dp)
//                        )
//                    }
//                } else {
                Text(
                    text = formatCurrencyFromString(order.getTotalPrice().toString()),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
//                }
            }
        }
    }
}


