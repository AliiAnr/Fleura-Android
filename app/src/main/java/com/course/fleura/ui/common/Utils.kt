package com.course.fleura.ui.common

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
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

fun formatNumber(number: Long): String {
    return String.format(Locale.US, "%,d", number).replace(',', '.')
}
