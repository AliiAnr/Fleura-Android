package com.course.fleura.ui.screen.dashboard.detail.order

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.course.fleura.R
import com.course.fleura.data.model.remote.CartOrderResponse
import com.course.fleura.data.model.remote.CartOrderResponseData
import com.course.fleura.data.model.remote.OrderDataItem
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.components.CustomButton
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.screen.dashboard.cart.CartViewModel
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.err
import com.course.fleura.ui.theme.primaryLight
import com.course.fleura.ui.theme.secColor
import com.course.fleura.ui.theme.tert
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@Composable
fun QrOrder(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel,
    onDownloadQr: (String, String) -> Unit,
    nowProvider: () -> Instant = { Clock.System.now() },
    onBack: () -> Unit = { /* Default no-op */ },
) {
    val orderResponse by cartViewModel.orderState.collectAsStateWithLifecycle(ResultResponse.None)

    val orderData = when (orderResponse) {
        is ResultResponse.Success -> (orderResponse as ResultResponse.Success<CartOrderResponse>).data.data
        is ResultResponse.Loading -> null
        else -> CartOrderResponseData(
            methode = "Error",
            qrisUrl = "Error",
            orderId = "Error",
            createdAt = "Error",
            successAt = "Error",
            id = "error",
            qrisExpiredAt = Clock.System.now().toString(),
            status = "error"
        )
    }

    if (orderData == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = primaryLight)
        }
    } else {
        Log.e("QrOrder", "Order Data time: ${orderData.qrisExpiredAt}")
        QrOrder(
            order = orderData,
            onDownloadQr = onDownloadQr,
            nowProvider = nowProvider,
            onBack = onBack
        )
    }
}

@Composable
private fun QrOrder(
    modifier: Modifier = Modifier,
    order: CartOrderResponseData,
    onDownloadQr: (String, String) -> Unit,
    nowProvider: () -> Instant = { Clock.System.now() },
    onBack: () -> Unit = {}
) {
    val tz = TimeZone.currentSystemDefault() // Asia/Jakarta

    // 1) Parse order.qrisExpiredAt sebagai LocalDateTime (buang 'Z')
    val expiredInstant = remember(order.qrisExpiredAt) {
        try {
            val withoutZ = order.qrisExpiredAt.removeSuffix("Z")
            val ldt = LocalDateTime.parse(withoutZ)
            // 2) Convert ke Instant, menganggap ldt itu di zona Asia/Jakarta
            ldt.toInstant(tz)
        } catch (e: Exception) {
            // fallback kalau parse gagal
            Clock.System.now() + 15.minutes
        }
    }

    // State remaining millis (still using UTC for calculations)
    var remaining by remember { mutableStateOf(expiredInstant - nowProvider()) }

    // Rest of the code remains the same...
    // Tick every second
    LaunchedEffect(expiredInstant) {
        while (true) {
            val current = nowProvider()
            remaining = expiredInstant - current
            if (remaining.inWholeMilliseconds <= 0) break
            delay(1_000)
        }
    }

    val isExpired = remaining.inWholeMilliseconds <= 0
    val isPaid = order.status.lowercase() == "paid"

    // Format hh:mm:ss
    val hours = if (remaining.inWholeMilliseconds > 0) remaining.inWholeHours else 0
    val minutes =
        if (remaining.inWholeMilliseconds > 0) (remaining - hours.hours).inWholeMinutes else 0
    val seconds =
        if (remaining.inWholeMilliseconds > 0) (remaining - hours.hours - minutes.minutes).inWholeSeconds else 0

    // Rest of your UI code remains the same
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
                    title = "Payment QR Code",
                    showNavigationIcon = true,
                    onBackClick = {
                        onBack()
                        onBack()
                    }
                )
                Box(
                    modifier = Modifier.weight(1f)
                ) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        item {
                            Spacer(modifier = Modifier.height(24.dp))

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = when {
                                            isPaid -> "PAYMENT SUCCESSFUL"
                                            isExpired -> "PAYMENT EXPIRED"
                                            else -> "SCAN QR CODE TO PAY"
                                        },
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            isPaid -> tert
                                            isExpired -> err
                                            else -> secColor
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    // QR image with border
                                    Card(
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 1.dp
                                        ),
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    ) {
                                        AsyncImage(
                                            model = order.qrisUrl,
                                            contentDescription = "QR Code",
                                            placeholder = painterResource(R.drawable.placeholder),
                                            error       = painterResource(R.drawable.placeholder),
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .size(240.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Timer section only if not paid and not expired
                                    if (!isPaid && !isExpired) {
                                        Text(
                                            text = "Time remaining:",
                                            fontSize = 14.sp,
                                            color = Color.Gray
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = String.format(
                                                Locale.ROOT,
                                                "%02d:%02d:%02d",
                                                hours,
                                                minutes,
                                                seconds
                                            ),
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = primaryLight
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = "Please complete your payment before the timer ends",
                                            fontSize = 14.sp,
                                            color = Color.Gray,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(horizontal = 24.dp)
                                        )
                                    } else if (isPaid) {
                                        Text(
                                            text = "Thank you for your payment",
                                            fontSize = 16.sp,
                                            color = tert,
                                            textAlign = TextAlign.Center
                                        )
                                    } else {
                                        Text(
                                            text = "This payment session has expired",
                                            fontSize = 16.sp,
                                            color = err,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
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

                    // Download button
                    CustomButton(
                        text = "Download QR Code",
                        onClick = {
                            val filename = "qr_${order.id}.png"
                            onDownloadQr(order.qrisUrl, filename)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                }

            }
        }
    }
}

@Composable
fun QrCreatedOrder(
    modifier: Modifier = Modifier,
    selectedCreatedOrderItem : OrderDataItem,
    onDownloadQr: (String, String) -> Unit,
    nowProvider: () -> Instant = { Clock.System.now() },
    onBack: () -> Unit = { /* Default no-op */ },
) {
           QrCreatedOrder(
            order = selectedCreatedOrderItem,
            onDownloadQr = onDownloadQr,
            nowProvider = nowProvider,
            onBack = onBack,
            id = selectedCreatedOrderItem.id
        )
}

@Composable
private fun QrCreatedOrder(
    modifier: Modifier = Modifier,
    order: OrderDataItem,
    onDownloadQr: (String, String) -> Unit,
    nowProvider: () -> Instant = { Clock.System.now() },
    onBack: () -> Unit = {},
    id: String ,
) {
    val tz = TimeZone.currentSystemDefault() // Asia/Jakarta

    // 1) Parse order.qrisExpiredAt sebagai LocalDateTime (buang 'Z')
    val expiredInstant = remember(order.payment.qrisExpiredAt) {
        try {
            val withoutZ = order.payment.qrisExpiredAt.removeSuffix("Z")
            val ldt = LocalDateTime.parse(withoutZ)
            // 2) Convert ke Instant, menganggap ldt itu di zona Asia/Jakarta
            ldt.toInstant(tz)
        } catch (e: Exception) {
            // fallback kalau parse gagal
            Clock.System.now() + 15.minutes
        }
    }

    // State remaining millis (still using UTC for calculations)
    var remaining by remember { mutableStateOf(expiredInstant - nowProvider()) }

    // Rest of the code remains the same...
    // Tick every second
    LaunchedEffect(expiredInstant) {
        while (true) {
            val current = nowProvider()
            remaining = expiredInstant - current
            if (remaining.inWholeMilliseconds <= 0) break
            delay(1_000)
        }
    }

    val isExpired = remaining.inWholeMilliseconds <= 0
    val isPaid = order.status.lowercase() == "paid"

    // Format hh:mm:ss
    val hours = if (remaining.inWholeMilliseconds > 0) remaining.inWholeHours else 0
    val minutes =
        if (remaining.inWholeMilliseconds > 0) (remaining - hours.hours).inWholeMinutes else 0
    val seconds =
        if (remaining.inWholeMilliseconds > 0) (remaining - hours.hours - minutes.minutes).inWholeSeconds else 0

    // Rest of your UI code remains the same
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
                    title = "Payment QR Code",
                    showNavigationIcon = true,
                    onBackClick = {
                        onBack()
                    }
                )
                Box(
                    modifier = Modifier.weight(1f)
                ) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        item {
                            Spacer(modifier = Modifier.height(24.dp))

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = when {
                                            isPaid -> "PAYMENT SUCCESSFUL"
                                            isExpired -> "PAYMENT EXPIRED"
                                            else -> "SCAN QR CODE TO PAY"
                                        },
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            isPaid -> tert
                                            isExpired -> err
                                            else -> secColor
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    // QR image with border
                                    Card(
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 1.dp
                                        ),
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    ) {
                                        AsyncImage(
                                            model = order.payment.qrisUrl,
                                            contentDescription = "QR Code",
                                            placeholder = painterResource(R.drawable.placeholder),
                                            error       = painterResource(R.drawable.placeholder),
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .size(240.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Timer section only if not paid and not expired
                                    if (!isPaid && !isExpired) {
                                        Text(
                                            text = "Time remaining:",
                                            fontSize = 14.sp,
                                            color = Color.Gray
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = String.format(
                                                Locale.ROOT,
                                                "%02d:%02d:%02d",
                                                hours,
                                                minutes,
                                                seconds
                                            ),
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = primaryLight
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = "Please complete your payment before the timer ends",
                                            fontSize = 14.sp,
                                            color = Color.Gray,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(horizontal = 24.dp)
                                        )
                                    } else if (isPaid) {
                                        Text(
                                            text = "Thank you for your payment",
                                            fontSize = 16.sp,
                                            color = tert,
                                            textAlign = TextAlign.Center
                                        )
                                    } else {
                                        Text(
                                            text = "This payment session has expired",
                                            fontSize = 16.sp,
                                            color = err,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
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

                    // Download button
                    CustomButton(
                        text = "Download QR Code",
                        onClick = {
                            val filename = "qr_${order.id}.png"
                            onDownloadQr(order.payment.qrisUrl, filename)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                }

            }
        }
    }
}