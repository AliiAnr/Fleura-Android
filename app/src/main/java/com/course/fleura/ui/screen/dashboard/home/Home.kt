package com.course.fleura.ui.screen.dashboard.home

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.R
import com.course.fleura.ui.components.Category
import com.course.fleura.ui.components.CategoryItem
import com.course.fleura.ui.components.Corousel
import com.course.fleura.ui.components.FakeCategory
import com.course.fleura.ui.components.SearchBar
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.FleuraTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.unit.TextUnit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleura.data.model.remote.Detail
import com.course.fleura.data.model.remote.ListStoreResponse
import com.course.fleura.data.model.remote.StoreItem
import com.course.fleura.data.model.remote.StoreProduct
import com.course.fleura.data.model.remote.StoreProductResponse
import com.course.fleura.ui.common.ProductListLoading
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.common.StoreListLoading
import com.course.fleura.ui.components.Common
import com.course.fleura.ui.components.CommonItem
import com.course.fleura.ui.components.FlowerItem
import com.course.fleura.ui.components.ListStoreItem
import com.course.fleura.ui.screen.dashboard.profile.ProfileViewModel
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base60
import com.course.fleura.ui.theme.primaryLight

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Home(
    modifier: Modifier,
    onSnackClick: (Long, String) -> Unit,
    onStoreClick: (String, String) -> Unit,
    onFlowerClick: (String, String) -> Unit,
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel
) {

    val isRefreshing by homeViewModel.isRefreshing.collectAsStateWithLifecycle()

    val pullToRefreshState = rememberPullToRefreshState()


    // call API in this section
    LaunchedEffect(Unit) {
        // This ensures the API calls happen after the composable is fully set up
//        delay(3000)
        homeViewModel.loadInitialData()
        profileViewModel.loadInitialData()
    }

    val userState by homeViewModel.userDetailState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

    val listProductData by homeViewModel.productListState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

    val listStoreData by homeViewModel.storeListState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

//    LaunchedEffect(userState) {
//        when (userState) {
//            is ResultResponse.Success -> {
////                showCircularProgress = false
//                Log.e(
//                    "HOME SCRENN",
//                    "SUGSESS: ${(userState as ResultResponse.Success).data}"
//                )
////                navigateToRoute(MainDestinations.LOGIN_ROUTE, true)
//            }
//
//            is ResultResponse.Loading -> {
////                showCircularProgress = true
//                Log.e(
//                    "HOME USER STET SCRENN",
//                    "LOADING"
//                )
//            }
//
//            is ResultResponse.Error -> {
//                Log.e(
//                    "HOME USER STEET SCRENN",
//                    "ERROR JIRR"
//                )
////                showCircularProgress = false
//                Log.e(
//                    "ERRROOR",
//                    "USERSTEERR error: ${(userState as ResultResponse.Error).error}"
//                )
//                // Display error message to the user
//                // Toast.makeText(context, otpState.message, Toast.LENGTH_SHORT).show()
//            }
//
//            else -> {}
//        }
//    }

    LaunchedEffect(listProductData) {
        when (listProductData) {
            is ResultResponse.Success -> {
//                showCircularProgress = false
                Log.e(
                    "HOME SCRENN",
                    "SUGSESS PRODUGGG: ${(listProductData as ResultResponse.Success).data}"
                )
//                navigateToRoute(MainDestinations.LOGIN_ROUTE, true)
            }

            is ResultResponse.Loading -> {
//                showCircularProgress = true
                Log.e(
                    "HOME USER STET SCRENN",
                    "LOADING"
                )
            }

            is ResultResponse.Error -> {
                Log.e(
                    "HOME USER STEET SCRENN",
                    "ERROR JIRR"
                )
//                showCircularProgress = false
                Log.e(
                    "ERRROOR",
                    "USERSTEERR error: ${(listProductData as ResultResponse.Error).error}"
                )
                // Display error message to the user
                // Toast.makeText(context, otpState.message, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

//    LaunchedEffect(listStoreData) {
//        when (listStoreData) {
//            is ResultResponse.Success -> {
////                showCircularProgress = false
//                Log.e(
//                    "HOME SCRENN",
//                    "SUGSESS: ${(listStoreData as ResultResponse.Success).data}"
//                )
////                navigateToRoute(MainDestinations.LOGIN_ROUTE, true)
//            }
//
//            is ResultResponse.Loading -> {
////                showCircularProgress = true
//                Log.e(
//                    "HOME SCRENN",
//                    "LOADING"
//                )
//            }
//
//            is ResultResponse.Error -> {
//                Log.e(
//                    "HOME SCRENN",
//                    "ERROR JIRR"
//                )
////                showCircularProgress = false
//                Log.e(
//                    "ERRROOR",
//                    "STORE error: ${(listStoreData as ResultResponse.Error).error}"
//                )
//                // Display error message to the user
//                // Toast.makeText(context, otpState.message, Toast.LENGTH_SHORT).show()
//            }
//
//            else -> {}
//        }
//    }

    val isLoading = userState is ResultResponse.Loading ||
            listStoreData is ResultResponse.Loading ||
            listProductData is ResultResponse.Loading ||
            (userState is ResultResponse.None && listStoreData is ResultResponse.None && listProductData is ResultResponse.None)

    // Extract userData
    val userData = when (userState) {
        is ResultResponse.Success -> (userState as ResultResponse.Success<Detail?>).data
        else -> null
    }

    // Extract stores from listStoreData
    val storeData = when (listStoreData) {
        is ResultResponse.Success -> (listStoreData as ResultResponse.Success<ListStoreResponse>).data.data
        else -> emptyList()
    }

    val productData = when (listProductData) {
        is ResultResponse.Success -> (listProductData as ResultResponse.Success<StoreProductResponse>).data.data
        else -> emptyList()
    }

    Home(
        modifier = modifier,
        userData = userData,
        storeData = storeData,
        productData = productData,
        isLoding = isLoading,
        onStoreClick = onStoreClick,
        onFlowerClick = onFlowerClick,
        setSelectedProduct = homeViewModel::setSelectedProduct,
        pullToRefreshState = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = { homeViewModel.refreshData() }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun Home(
    modifier: Modifier = Modifier,
    onStoreClick: (String, String) -> Unit,
    onFlowerClick: (String, String) -> Unit,
    setSelectedProduct: (StoreProduct) -> Unit,
    userData: Detail?,
    productData: List<StoreProduct>,
    storeData: List<StoreItem>,
    isLoding: Boolean = false,
    isRefreshing: Boolean = false,
    pullToRefreshState: PullToRefreshState,
    onRefresh: () -> Unit
) {
    var textState by remember { mutableStateOf("") } // Menggunakan remember untuk state

    FleuraSurface(
        modifier = modifier.fillMaxSize()
    ) {
//        Box(
//            modifier = Modifier.fillMaxSize()
//        ) {

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            modifier = Modifier
                .fillMaxSize(),
            indicator = {
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    state = pullToRefreshState,
                    threshold = 100.dp,
                    color = primaryLight,
                    containerColor = Color.White
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
            ) {
                item {
                    Header(
                        name = userData?.name ?: "User"
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    SearchBar(
                        query = textState,
                        onQueryChange = {
                            textState = it
                        },
                        onSearch = {
                            // call API
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                }

                item {
                    ListCategory(
                        onCategoryClick = { id, name ->
                            // call API
                        },
                        listCategory = FakeCategory.categories
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    SectionText(title = "Explore Offers!")
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Corousel(
                        listCorousel = FakeCategory.corousels
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    if (isLoding) {
                        StoreListLoading()

                    } else {
                        ListStores(
                            storeList = storeData,
                            onNavigate = {
                                // call
                            },
                            onStoreClick = { storeId, origin ->
                                onStoreClick(storeId, origin)
                            }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    if (isLoding){
                        ProductListLoading()
                    } else {
                        ListFlowers(
                            flowerList = productData,
                            onNavigate = {
                                // call API
                            },
                            setSelectedProduct = setSelectedProduct,
                            onFlowerClick = { productId, origin ->
                                onFlowerClick(productId, origin)
                            }
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    ListCommon(
                        items = FakeCategory.commons,
                        onItemClicked = { id, name ->
                            // call API
                        },
                        onNavigate = {

                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(200.dp))
                }


            }
        }
    }
}


@Composable
private fun Header(
    modifier: Modifier = Modifier,
    name: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Hello, ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("$name!")
                    }
                },
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "What are you looking for right now?",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.W600
            )
        }
        IconButton(onClick = { /* Handle notification click */ }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.notification),
                contentDescription = "Notification",
                tint = Color.Black
            )
        }
    }
}

@Composable
private fun ListCategory(
    modifier: Modifier = Modifier,
    onCategoryClick: (Long, String) -> Unit,
    listCategory: List<Category>
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(112.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(listCategory, key = { it.id }) { category ->
            CategoryItem(category = category, onCategoryClick = onCategoryClick)
        }
    }
}

@Composable
private fun Corousel(
    modifier: Modifier = Modifier,
    listCorousel: List<Corousel>
) {
    val pagerState = rememberPagerState {
        listCorousel.size
    }

    val coroutineScope = rememberCoroutineScope()
    var isUserDragging by remember { mutableStateOf(true) }

    var scrollDirection by remember { mutableIntStateOf(1) }

//    LaunchedEffect(isUserDragging) {
//        coroutineScope.launch {
//            while (true) {
//                delay(2000)
//
//                val nextPage = pagerState.currentPage + scrollDirection
//
//                if (nextPage >= pagerState.pageCount) {
//                    scrollDirection = -1
//                } else if (nextPage < 0) {
//                    scrollDirection = 1
//                }
//
//                pagerState.animateScrollToPage(
//                    page = pagerState.currentPage + scrollDirection,
//                    animationSpec = tween(durationMillis = 1000)
//                )
//            }
//        }
//    }


    Column(
        modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier.wrapContentSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = modifier
                    .wrapContentSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isUserDragging = !isUserDragging
                            }
                        )
                    }

            ) { currentPage ->
                Card(
                    modifier
                        .wrapContentSize()
                        .padding(horizontal = 20.dp),
                    elevation = CardDefaults.cardElevation(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = listCorousel[currentPage].image),
                        contentDescription = ""
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        PageIndicator(
            pageCount = listCorousel.size,
            currentPage = pagerState.currentPage,
            modifier = modifier
        )
    }
}

@Composable
private fun PageIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(pageCount) {
            IndicatorDots(
                isSelected = it == currentPage,
                modifier = modifier
            )
        }

    }

}

@Composable
private fun IndicatorDots(
    modifier: Modifier = Modifier,
    isSelected: Boolean
) {
    val size = animateDpAsState(targetValue = if (isSelected) 9.5.dp else 8.dp, label = "")
    Box(
        modifier = modifier
            .padding(horizontal = 7.dp)
            .size(size.value)
            .clip(CircleShape)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary else base60
            )
    )
}

@Composable
fun ListStores(
    modifier: Modifier = Modifier,
    storeList: List<StoreItem>,
    onNavigate: () -> Unit,
    onStoreClick: (String, String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Explore Stores!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Icon(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = "Navigate",
                tint = Color.Black,
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onNavigate() }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(storeList, key = { it.id }) { store ->
                ListStoreItem(
                    storeId = store.id,
                    imageUrl = store.picture,
                    name = store.name,
                    openingHours = store.operationalHour,
                    onStoreClick = onStoreClick
                )
            }

        }
    }
}

@Composable
fun ListFlowers(
    modifier: Modifier = Modifier,
    flowerList: List<StoreProduct>,
    onNavigate: () -> Unit,
    setSelectedProduct: (StoreProduct) -> Unit,
    onFlowerClick: (String, String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Best Ratings!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Icon(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = "Navigate",
                tint = Color.Black,
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onNavigate() }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(flowerList, key = { it.id }) { flower ->
                FlowerItem(
                    onFlowerClick = onFlowerClick,
                   item = flower,
                    setSelectedProduct = setSelectedProduct
                )
            }

        }
    }
}

@Composable
fun ListCommon(
    modifier: Modifier = Modifier,
    items: List<Common>,
    onNavigate: () -> Unit,
    onItemClicked: (Long, String) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Best Ratings!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Icon(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = "Navigate",
                tint = Color.Black,
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onNavigate() }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            flingBehavior = snapFlingBehavior,
        ) {
            items(items.chunked(4)) { chunkedItems ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    chunkedItems.chunked(2).forEach { rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(11.dp),
                        ) {
                            rowItems.forEach { item ->
                                CommonItem(
                                    imageRes = item.image,
                                    name = item.name,
                                    price = item.price,
                                    onCommonClicked = onItemClicked
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SectionText(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = Color.Black,
    fontSize : TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Bold,
) {
    Text(
        text = title,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingHeaderPreview() {
    FleuraTheme {
//        Header()
    }
}
