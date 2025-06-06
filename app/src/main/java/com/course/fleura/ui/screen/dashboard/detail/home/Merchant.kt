package com.course.fleura.ui.screen.dashboard.detail.home

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.course.fleura.R
import com.course.fleura.data.model.remote.DetailStoreData
import com.course.fleura.data.model.remote.DetailStoreResponse
import com.course.fleura.data.model.remote.StoreProduct
import com.course.fleura.data.model.remote.StoreProductResponse
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.common.toFormattedAddress
import com.course.fleura.ui.components.MerchantFlowerItem
import com.course.fleura.ui.screen.dashboard.home.HomeViewModel
import com.course.fleura.ui.screen.navigation.DetailDestinations
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.screen.navigation.MainDestinations
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base40
import com.course.fleura.ui.theme.base500
import com.course.fleura.ui.theme.primaryLight

@Composable
fun Merchant(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    storeId: String,
    origin: String,
    onBackClick: () -> Unit,
    onFlowerClick: (String, String) -> Unit,
    storeViewModel: StoreViewModel
) {
    val storeDetailState by storeViewModel.storeDetailState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)
    val storeProductState by storeViewModel.storeProductState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)
//    val productReviewState by storeViewModel.productReviewState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

    var showCircularProgress by remember { mutableStateOf(false) }

    LaunchedEffect(storeId) {
        storeViewModel.getStoreDetail(storeId)
        storeViewModel.getAllStoreProduct(storeId)
//        storeViewModel.getProductReview(storeId)
    }

    LaunchedEffect(storeDetailState) {
        when (storeDetailState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                Log.e(
                    "Merchant Fetched",
                    "Success: ${(storeDetailState as ResultResponse.Success<DetailStoreResponse>).data}"
                )
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

    LaunchedEffect(storeProductState) {
        when (storeProductState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                Log.e(
                    "Merchant Products Fetched",
                    "Success: ${(storeProductState as ResultResponse.Success<StoreProductResponse>).data.data}"
                )
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



    val isLoading = storeDetailState is ResultResponse.Loading ||
            storeProductState is ResultResponse.Loading ||
            (storeDetailState is ResultResponse.None && storeProductState is ResultResponse.None)


    val storeData = when (storeDetailState) {
        is ResultResponse.Success -> (storeDetailState as ResultResponse.Success<DetailStoreResponse>).data.data
        else -> null
    }

    val productData = when (storeProductState) {
        is ResultResponse.Success -> (storeProductState as ResultResponse.Success<StoreProductResponse>).data.data
        else -> null
    }

    Merchant(
        modifier = modifier,
        storeData = storeData,
        productData = productData,
        isLoading = isLoading,
        onBackClick = onBackClick,
        onFlowerClick = onFlowerClick,
        homeViewModel = homeViewModel
    )

}

@Composable
private fun Merchant(
    modifier: Modifier = Modifier,
    storeData: DetailStoreData?,
    homeViewModel: HomeViewModel,
    productData: List<StoreProduct>?,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onFlowerClick: (String, String) -> Unit
) {

    val groupedByCat = remember(productData) {
        productData?.groupBy { it.category.name }?.toSortedMap() ?: sortedMapOf()
    }

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
                if (isLoading) {
                    // Show empty state when there's no data and not loading
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {}
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        item {
                            DescMerchant(
                                storeData = storeData,
                                onBackClick = onBackClick
                            )
                        }

                        groupedByCat.forEach { (categoryName, itemsInCat) ->
                            // Header kategori
                            item(key = "header-$categoryName") {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = categoryName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(start = 20.dp, top = 20.dp, end = 20.dp)
                                )
                            }

                            items(
                                items = itemsInCat,
                                key = { it.id }
                            ) { product ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(horizontal = 20.dp)
                                ) {
                                    MerchantFlowerItem(
                                        item = product,
                                        onFlowerClick = {
                                            homeViewModel.setSelectedProduct(product)
                                            onFlowerClick(
                                                product.id,
                                                DetailDestinations.DETAIL_MERCHANT
                                            )
                                        }
                                    )
                                    // Divider kecuali item terakhir
                                    if (product != itemsInCat.last()) {
                                        HorizontalDivider(color = base40)
                                    }
                                }
                            }
                        }

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .background(Color.White)
                            )
                        }

//                        data.categories.forEach { category ->
//                            // Header untuk kategori
//                            item {
//                                Spacer(modifier = Modifier.height(8.dp))
//                                Text(
//                                    text = category.title,
//                                    fontSize = 18.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    color = Color.Black,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .background(Color.White)
//                                        .padding(start = 20.dp, top = 20.dp, end = 20.dp)
//                                )
//                            }
//
//                            itemsIndexed(category.items) { index, item ->
//                                val isLastItem = index == category.items.size - 1
//                                Column(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .background(Color.White)
//                                        .padding(horizontal = 20.dp)
//                                ) {
//                                    MerchantFlowerItem(
//                                        item = item,
//                                        onFlowerClick = { _, _ ->
//
//                                        }
//                                    )
//                                    if (!isLastItem) {
//                                        HorizontalDivider(
//                                            color = base40,
//                                        )
//                                    }
//                                }
//
//                            }
//                        }
                    }
                }
            }
            if (isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator(color = primaryLight)
                }
            }
        }
    }
}

@Composable
private fun DescMerchant(
    modifier: Modifier = Modifier,
    storeData: DetailStoreData?,
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

            if (storeData?.picture.isNullOrEmpty()) {
                Log.e("WOIII KOSONG", "${storeData?.picture}")
                Image(
                    painter = painterResource(id = R.drawable.placeholder),  // Use a placeholder image
                    contentDescription = "Store Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            } else {
                Log.e("WOIII ADA", "${storeData?.picture}")
                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(storeData?.picture)
//                        .crossfade(true)
//                        .build(),
                    model = storeData?.picture,
                    contentDescription = "Store Image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder),
                    error = painterResource(id = R.drawable.placeholder),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            // Gambar Header

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
                            onClick = { onBackClick() },
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
                text = storeData?.name ?: "STORE NAME",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Deskripsi
            Text(
                text = storeData?.description
                    ?: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
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
                    text = storeData?.address?.toFormattedAddress() ?: "Address not available",
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
                    text = storeData?.phone ?: "Phone not available",
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
                    text = storeData?.operationalDay ?: "Not specified",
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
                    text = storeData?.operationalHour ?: "Not specified",
                    color = base500,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700
                )
            }
        }
    }
}
