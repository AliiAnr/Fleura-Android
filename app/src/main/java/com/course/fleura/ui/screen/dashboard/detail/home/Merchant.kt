package com.course.fleura.ui.screen.dashboard.detail.home

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.R
import com.course.fleura.ui.components.FakeCategory
import com.course.fleura.ui.components.Merchant
import com.course.fleura.ui.components.MerchantFlowerItem
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base40
import com.course.fleura.ui.theme.base500
import com.course.fleura.ui.theme.base60
import com.course.fleura.ui.theme.secColor

@Composable
fun Merchant(
    modifier: Modifier = Modifier,
    id: Long
) {

    Merchant(
        modifier = modifier,
        data = FakeCategory.merchantItem
    )

}

@Composable
private fun Merchant(
    modifier: Modifier = Modifier,
    data: Merchant
) {
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    item {
                        DescMerchant(
                            name = data.name,
                            description = data.description,
                            location = data.address,
                            phoneNumber = data.phone,
                            openHours = data.openHours,
                            openDays = data.openDays
                        )
                    }

                    data.categories.forEach { category ->
                        // Header untuk kategori
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = category.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth().background(Color.White).padding(start = 20.dp, top = 20.dp, end = 20.dp)
                            )
                        }

//                         Daftar item untuk kategori ini
                        itemsIndexed(category.items) { index, item ->
                            val isLastItem = index == category.items.size - 1
                            Column (
                                modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 20.dp)
                            ){
                                MerchantFlowerItem(
                                    item = item,
                                    onFlowerClick = { _, _ ->

                                    }
                                )
                                if (!isLastItem) {
                                    HorizontalDivider(
                                        color = base40,
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DescMerchant(
    modifier: Modifier = Modifier,
    name: String,
    description: String,
    location: String,
    phoneNumber: String,
    openHours: String,
    openDays: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Header Image with Icons
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            // Gambar Header
            Image(
                painter = painterResource(id = R.drawable.store_1),
                contentDescription = "Flower Shop",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Ikon di atas gambar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable(
                            onClick = { },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .size(20.dp)
                            .graphicsLayer(rotationZ = 180f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable(
                            onClick = { },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.share),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
            }
        }

        // Konten di bawah gambar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp)
        ) {
            // Nama Toko
            Text(
                text = "Eunoias",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Deskripsi
            Text(
                text = "Eunoia is a flower shop that sell many kind of flower. Available fresh flower and artificial flower. You can also order a bouquet and feel free to do custom order.",
                color = base500,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Lokasi
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.loc),
                    contentDescription = "Rating",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Jl. Majapahit, No. 15",
                    color = base500,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700
                )
            }

            // Nomor Telepon
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bubble_chat),
                    contentDescription = "Rating",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "083-777-888-999",
                    color = base500,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp)
        ) {
            // Nama Toko
            Text(
                text = "Opening Hours",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Lokasi
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
                    text = "Jl. Majapahit, No. 15",
                    color = base500,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700
                )
            }

            // Nomor Telepon
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
                    text = "083-777-888-999",
                    color = base500,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700
                )
            }
        }
    }
}
