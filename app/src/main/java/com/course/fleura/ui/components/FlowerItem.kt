package com.course.fleura.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.R
import com.course.fleura.ui.common.formatCurrency
import com.course.fleura.ui.theme.base100
import com.course.fleura.ui.theme.base500
import com.course.fleura.ui.theme.secColor

@Composable
fun FlowerItem(
    modifier: Modifier = Modifier,
    onFlowerClick: (Long, String) -> Unit,
    imageRes: Int,
    storeName: String,
    flowerName: String,
    rating: Double,
    reviewsCount: Int,
    price: Long
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = modifier
            .width(140.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "$storeName - $flowerName",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Rating",
                    tint = secColor,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$rating | $reviewsCount",
                    fontSize = 10.sp,
                    color = base100
                )
            }
            Text(
                text = formatCurrency(price),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

        }
    }
}

@Composable
fun MerchantFlowerItem(
    onFlowerClick: (Long, String) -> Unit,
    item: Flower
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Gambar item
        Image(
            painter = painterResource(id = item.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(110.dp)
                .clip(RoundedCornerShape(10.dp))
        )

        Spacer(modifier = Modifier.width(18.dp))

        // Detail item
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "flowerName",
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = "Filled yourself up with Filled yourself up with this beaut this beautiful pack of pink flowers.",
                fontSize = 12.sp,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = base500,
                modifier = Modifier.height(38.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Rating",
                    tint = secColor,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${item.rating} | ${item.reviewsCount}",
                    fontSize = 10.sp,
                    color = base100
                )
            }
            Text(
                text = formatCurrency(item.price),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

        }
    }
}