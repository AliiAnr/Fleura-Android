package com.course.fleura.ui.screen.dashboard.detail.order

import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.course.fleura.R
import com.course.fleura.data.model.remote.DetailStoreResponse
import com.course.fleura.data.model.remote.ProductReviewResponse
import com.course.fleura.data.model.remote.ReviewItem
import com.course.fleura.data.model.remote.StoreProduct
import com.course.fleura.ui.common.RatingListSkeleton
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.common.formatCurrency
import com.course.fleura.ui.common.formatCurrencyFromString
import com.course.fleura.ui.components.CustomButton
import com.course.fleura.ui.components.CustomTextInput
import com.course.fleura.ui.components.FakeCategory
import com.course.fleura.ui.components.Flower
import com.course.fleura.ui.components.QuantitySelector
import com.course.fleura.ui.components.UserRating
import com.course.fleura.ui.screen.dashboard.home.HomeViewModel
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base100
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base300
import com.course.fleura.ui.theme.base500
import com.course.fleura.ui.theme.primaryLight
import com.course.fleura.ui.theme.secColor
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView

@Composable
fun FlowerDetail(
    modifier: Modifier = Modifier,
    origin: String,
    flowerId: String,
    selectedProduct: StoreProduct,
    homeViewModel: HomeViewModel,
    onStoreClick: (String, String) -> Unit,
    onBackClick: () -> Unit,
) {
    val productReviewState by homeViewModel.productReviewState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    val saveProductState by homeViewModel.saveProductState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    var showCircularProgress by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        homeViewModel.getProductReview(productId = flowerId)
    }

    LaunchedEffect(productReviewState) {
        when (productReviewState) {
            is ResultResponse.Success -> {
                Log.e(
                    "Merchant Fetched",
                    "Success: ${(productReviewState as ResultResponse.Success<ProductReviewResponse>).data}"
                )

            }

            is ResultResponse.Loading -> {

            }

            is ResultResponse.Error -> {
            }

            else -> {}
        }
    }

    LaunchedEffect(saveProductState) {
        when (saveProductState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
            }

            is ResultResponse.Loading -> {
                showCircularProgress = true
            }

            is ResultResponse.Error -> {
                showCircularProgress = false
            }

            else -> {}
        }
    }

    val isLoading =
        productReviewState is ResultResponse.Loading ||
                (productReviewState is ResultResponse.None)


    val reviewData = when (productReviewState) {
        is ResultResponse.Success -> (productReviewState as ResultResponse.Success<ProductReviewResponse>).data.data
        else -> null
    }

    FlowerDetail(
        onBackClick = onBackClick,
        item = selectedProduct,
        isLoading = isLoading,
        reviewData = reviewData ?: emptyList(),
        onAddToCart = homeViewModel::saveProductToCart,
        onQuantityChange = homeViewModel::setQuantity,
        onStoreClick = onStoreClick,
        showCircularProgress = showCircularProgress
    )
}

@Composable
private fun FlowerDetail(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    isLoading: Boolean,
    reviewData: List<ReviewItem>,
    onAddToCart: (String) -> Unit,
    onQuantityChange: (Int) -> Unit,
    onStoreClick: (String, String) -> Unit,
    showCircularProgress: Boolean,
    item: StoreProduct
) {

    var quantity by remember { mutableIntStateOf(0) }

    val focusManager = LocalFocusManager.current

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
                Box(
                    modifier = Modifier.weight(1f)
                ) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        item {
                            DescFlower(
                                onBackClick = onBackClick,
                                onStoreClick = onStoreClick,
                                item = item
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            RatingsSection(
                                isLoading = isLoading,
                                reviewData = reviewData
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            NoteSection()
                        }

                        item {
                            QuantitySection(
                                quantity = quantity,
                                onChange = { newQuantity ->
                                    quantity = newQuantity
                                    onQuantityChange(quantity)
                                }
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
                        isAvailable = quantity > 0,
                        text = "Add to cart",
                        onClick = {
                            onAddToCart(item.id)
                            quantity = 0
                            onQuantityChange(0)
                        }
                    )
                }
            }
            if (showCircularProgress) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f))
                ) {
                    CircularProgressIndicator(color = primaryLight)
                }
            }
        }
    }
}


@Composable
private fun DescFlower(
    modifier: Modifier = Modifier,
    item: StoreProduct,
    onStoreClick: (String, String) -> Unit,
    onBackClick: () -> Unit
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
            if (item.picture.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = "Store Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            } else {

                AsyncImage(
                    model = item.picture.firstOrNull()?.path,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.placeholder),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }

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
                            onClick = {
                                onBackClick()
                            },
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.name,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = "Rating",
                        tint = secColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${item.rating} | 100",
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W700,
                    )
                }
            }


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
                    text = formatCurrencyFromString(item.price),
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
                text = item.description,
                color = base500,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Category: ${item.category.name}",
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
                    text = item.arrangeTime,
                    color = base500,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700
                )
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        StoreItemLogo(
            item = item,
            onStoreClick = onStoreClick
        )
    }
}

@Composable
fun StoreItemLogo(
    modifier: Modifier = Modifier,
    item: StoreProduct,
    onStoreClick: (String, String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row (
            modifier = Modifier
                .clickable(
                    onClick = {
                        onStoreClick(item.store.id, item.name)
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            verticalAlignment = Alignment.CenterVertically
        ){
            if (item.picture.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = "Store Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(
                            RoundedCornerShape(50.dp)
                        )
                )
            } else {
                AsyncImage(
                    model = item.picture.firstOrNull()?.path,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.placeholder),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(
                            RoundedCornerShape(50.dp)
                        ),
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = item.store.name,
                color = base500,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }

    }
}

@Composable
fun RatingsSection(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    reviewData: List<ReviewItem>?
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

        if (isLoading) {
            RatingListSkeleton()
        } else if (reviewData.isNullOrEmpty()) {
            Text(
                text = "No ratings yet",
                fontSize = 12.sp,
                color = base500,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(reviewData) { rating ->
                    RatingCard(rating = rating)
                }
            }
        }
    }
}

@Composable
fun RatingCard(
    modifier: Modifier = Modifier,
    rating: ReviewItem
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .height(80.dp)
            .background(base20)
            .padding(16.dp)
    ) {
        // Barisan Bintang
        StarsRow(rating = rating.rate)

        // Komentar
        Text(
            text = rating.message,
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
    modifier: Modifier = Modifier,
    quantity: Int,
    onChange: (Int) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        QuantitySelector(
            quantity = quantity,
            onQuantityChange = {
                onChange(it)
            }
        )

    }
}