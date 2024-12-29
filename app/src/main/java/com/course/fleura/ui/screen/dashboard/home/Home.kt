package com.course.fleura.ui.screen.dashboard.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageInfo
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableIntStateOf
import com.course.fleura.ui.components.Flower
import com.course.fleura.ui.components.FlowerItem
import com.course.fleura.ui.components.Store
import com.course.fleura.ui.components.StoreItem
import com.course.fleura.ui.theme.base60
import com.course.fleura.ui.theme.base80

@Composable
fun Home(
    onSnackClick: (Long, String) -> Unit,
    modifier: Modifier
) {
    // call API in this section
    Home()
}

@Composable
private fun Home(modifier: Modifier = Modifier) {
    var textState by remember { mutableStateOf("") } // Menggunakan remember untuk state

    FleuraSurface(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn (
                modifier = Modifier.fillMaxSize().statusBarsPadding(),
            ) {
                item {
                    Header()
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
                    ListStores(
                        storeList = FakeCategory.stores,
                        onNavigate = {
                            // call API
                        },
                        onStoreClick = { id, name ->
                            // call API
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    ListFlowers(
                        flowerList = FakeCategory.flowers,
                        onNavigate = {
                            // call API
                        },
                        onStoreClick = { id, name ->
                            // call API
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
    modifier: Modifier = Modifier
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
                    withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)) {
                        append("Halo, ")
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                        append("name!")
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
            .height(112.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(listCategory, key = { it.id }){ category ->
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
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ){
        repeat(pageCount){
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
    modifier : Modifier = Modifier,
    storeList : List<Store>,
    onNavigate: () -> Unit,
    onStoreClick: (Long, String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Explore Stores!",
                fontSize = 20.sp,
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
                StoreItem(
                    imageRes = store.image,
                    name = store.name,
                    openingHours = store.openHours
                )
            }

        }
    }
}

@Composable
fun ListFlowers(
    modifier : Modifier = Modifier,
    flowerList : List<Flower>,
    onNavigate: () -> Unit,
    onStoreClick: (Long, String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Best Ratings!",
                fontSize = 20.sp,
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
                    onFlowerClick = { id, name ->
                        // call API
                    },
                    imageRes = flower.image,
                    storeName = flower.storeName,
                    flowerName = flower.flowerName,
                    rating = flower.rating,
                    reviewsCount = flower.reviewsCount,
                    price = flower.price
                )
            }

        }
    }
}

@Composable
fun SectionText(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}



@Preview(showBackground = true)
@Composable
fun GreetingHeaderPreview() {
    FleuraTheme {
        Header()
    }
}
