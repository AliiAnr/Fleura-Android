package com.course.fleura.ui.common

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.core.view.WindowCompat
import com.course.fleura.data.model.remote.Address
import com.course.fleura.data.model.remote.DataCartItem
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ChangeStatusBarColor(
    newStatusBarColor: Color,
    isStatusBarIconsDark: Boolean
) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = (context as Activity).window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        window.statusBarColor = newStatusBarColor.toArgb()
        windowInsetsController.isAppearanceLightStatusBars = isStatusBarIconsDark

        onDispose {

        }
    }
}

@Composable
fun getScreenHeightInDp(): Int {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    return screenHeightDp
}

fun formatCurrency(amount: Long): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return formatter.format(amount).replace(",00", "")
}

fun formatCurrencyFromString(amount: String): String {
    return try {
        val amountDouble = amount.toDoubleOrNull() ?: 0.0
        val amountLong = amountDouble.toLong()

        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        formatter.format(amountLong).replace(",00", "")
    } catch (e: Exception) {
        "Rp0"
    }
}

fun formatNumber(number: Long): String {
    return String.format(Locale.US, "%,d", number).replace(',', '.')
}

fun Modifier.loadingFx(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "")

    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
        ), label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFE0E0E0),
                Color(0xFFB9B2B2),
                Color(0xFFE0E0E0),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

fun Address.toFormattedAddress(): String {
    val addressParts = mutableListOf<String>()

    // Add road
    if (road.isNotBlank()) {
        addressParts.add(road)
    }

    // Add detail (like RT/RW) if available
    if (detail.isNotBlank()) {
        addressParts.add(detail)
    }

    // Add district (kecamatan)
    if (district.isNotBlank()) {
        addressParts.add("Kec. $district")
    }

    // Add city/regency (kota/kabupaten)
    if (city.isNotBlank()) {
        addressParts.add(city)
    }

    // Add province
    if (province.isNotBlank()) {
        addressParts.add(province)
    }

    // Add postal code
    if (postcode.isNotBlank()) {
        addressParts.add(postcode)
    }

    return addressParts.joinToString(", ")
}

fun DataCartItem.getTotalPrice(): Int {
    return items.sumOf {
        it.price.toIntOrNull()?.times(it.quantity) ?: it.total
    }
}

fun String.toInstant(): Instant =
    Instant.parse(this) // "2025-06-14T09:55:33.000Z"

object NetworkUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}

fun extractOrderId(input: String): String {
    val digits = input.filter { it.isDigit() }
    return digits.toList().sorted().take(4).joinToString("")
}

fun parseDateTime(dateTimeString: String): Pair<kotlinx.datetime.LocalDate, kotlinx.datetime.LocalTime> {
    val instant = Instant.parse(dateTimeString)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.date to localDateTime.time
}