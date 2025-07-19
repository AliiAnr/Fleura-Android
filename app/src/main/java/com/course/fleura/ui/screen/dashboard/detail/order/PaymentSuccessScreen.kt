package com.course.fleura.ui.screen.dashboard.detail.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleura.BuildConfig

@Composable
fun PaymentSuccessScreen(
    orderId: String,
    upPress: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = "Payment Success!",
                // styling...
            )

            Text(
                text = "Order ID: $orderId"
            )

            // Debug info (hapus di production
        }
    }
}