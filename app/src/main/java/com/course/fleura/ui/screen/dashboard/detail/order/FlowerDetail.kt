package com.course.fleura.ui.screen.dashboard.detail.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.R
import com.course.fleura.ui.common.formatCurrency
import com.course.fleura.ui.components.CustomButton
import com.course.fleura.ui.components.CustomTextInput
import com.course.fleura.ui.components.FakeCategory
import com.course.fleura.ui.components.Flower
import com.course.fleura.ui.components.QuantitySelector
import com.course.fleura.ui.components.UserRating
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base300
import com.course.fleura.ui.theme.base500
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView

@Composable
fun FlowerDetail(
    modifier: Modifier = Modifier,
    flowerId: Long,
) {
    FlowerDetail(
        data = null
    )
}

@Composable
private fun FlowerDetail(
    modifier: Modifier = Modifier,
    data: Flower? = null
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
                Box(
                    modifier = Modifier.weight(1f)
                ) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        item {
                            DescFlower()
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            RatingsSection(
                                ratings = FakeCategory.listRating
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            NoteSection()
                        }

                        item {
                            QuantitySection()
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
                        text = "Add to cart",
                        onClick = { }
                    )
                }
            }
        }
    }
}


@Composable
private fun DescFlower(
    modifier: Modifier = Modifier
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
                    painter = painterResource(id = R.drawable.money),
                    contentDescription = "Rating",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatCurrency(15000),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
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
                text = "Detail Bouquet",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Eunoia is a flower shop that sell many kind of flower. Available fresh flower and artificial flower. You can also order a bouquet and feel free to do custom order.",
                color = base500,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Type: Bouquet",
                color = base500,
                fontSize = 12.sp,
                fontWeight = FontWeight.W700,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Lokasi
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.timer),
                    contentDescription = "Rating",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "50 minutes",
                    color = base500,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700
                )
            }

        }
    }
}

@Composable
fun RatingsSection(
    modifier: Modifier = Modifier,
    ratings: List<UserRating>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp)
    ) {
        // Header "Ratings"
        Text(
            text = "Ratings",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // LazyRow untuk menampilkan review secara horizontal
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ratings) { rating ->
                RatingCard(rating = rating)
            }
        }
    }
}

@Composable
fun RatingCard(
    modifier: Modifier = Modifier,
    rating: UserRating
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(base20)
            .padding(16.dp)
    ) {
        // Barisan Bintang
        StarsRow(rating = rating.rating)

        // Komentar
        Text(
            text = rating.description,
            fontSize = 12.sp,
            color = base500,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 200.dp)
        )
    }
}

@Composable
fun StarsRow(rating: Int) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Menampilkan 5 bintang, beberapa aktif dan lainnya non-aktif
        repeat(5) { index ->
            Icon(
                painter = painterResource(id = if (index < rating) R.drawable.star else R.drawable.empty_star),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
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
            .padding(20.dp)
    ) {

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
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
private fun QuantitySection(
    modifier: Modifier = Modifier
) {
    val temp = remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        QuantitySelector(
            quantity = temp.intValue,
            onQuantityChange = {
                temp.intValue = it
            }
        )

    }
}