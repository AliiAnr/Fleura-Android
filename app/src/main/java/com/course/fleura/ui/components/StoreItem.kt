package com.course.fleura.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.course.fleura.R
import com.course.fleura.data.model.remote.StoreItem
import com.course.fleura.ui.screen.navigation.MainDestinations
import com.course.fleura.ui.theme.FleuraTheme
import com.course.fleura.ui.theme.base500
import com.course.fleura.ui.theme.secColor

@Composable
fun ListStoreItem(
    modifier: Modifier = Modifier,
    storeId: String,
    imageUrl: String? = null,
    name: String,
    openingHours: String,
    onStoreClick: (String, String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = modifier
            .width(200.dp)
            .clickable(
                onClick = { onStoreClick(storeId, MainDestinations.DASHBOARD_ROUTE) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (imageUrl.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.placeholder),  // Use a placeholder image
                    contentDescription = "Store Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(imageUrl)
//                        .crossfade(true)
//                        .build(),
//                    contentDescription = "Store Image",
//                    contentScale = ContentScale.Crop,
//                    placeholder = painterResource(id = R.drawable.placeholder),
//                    error = painterResource(id = R.drawable.placeholder),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(115.dp)
//                        .clip(RoundedCornerShape(10.dp))
//                )

                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Store Image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder),
                    error = painterResource(id = R.drawable.placeholder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.clock),
                    contentDescription = null,
                    tint = base500,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = openingHours,
                    fontSize = 12.sp,
                    color = base500
                )
            }
        }
    }
}


@Composable
fun SearchListStoreItem(
    modifier: Modifier = Modifier,
    store: StoreItem,
    onStoreClick: (String, String) -> Unit
) {

    val picture = store.picture.orEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onStoreClick(store.id, MainDestinations.DASHBOARD_ROUTE)
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (picture.isBlank()) {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = null,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            AsyncImage(
                model = picture,
                contentDescription = null,
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = store.name ?: "dbao",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Rating",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = store?.operationalDay ?: "Not specified",
                    color = base500,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.clock),
                    contentDescription = "Rating",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = store?.operationalHour ?: "Not specified",
                    color = base500,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewStoreItem() {
//    FleuraTheme {
//        StoreItem(
//            imageRes = R.drawable.store_1, // Replace with your image resource
//            name = "Eunoia",
//            openingHours = "09:00 am - 10:00 pm"
//        )
//    }
//}
