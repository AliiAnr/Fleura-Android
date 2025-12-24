package com.course.fleura.ui.screen.dashboard.detail.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleura.data.model.remote.StoreProduct
import com.course.fleura.data.model.remote.StoreProductResponse
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.EmptyProduct
import com.course.fleura.ui.components.SearchBar
import com.course.fleura.ui.components.SearchMerchantFlowerItem
import com.course.fleura.ui.screen.dashboard.home.HomeViewModel
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.screen.navigation.MainDestinations
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base40
import com.course.fleura.ui.theme.primaryLight
import kotlinx.coroutines.delay


@Composable
fun SearchBestRatings (
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    onFlowerClick: (String, String) -> Unit,
    onBackClick: () -> Unit
) {

    val storeProductState by homeViewModel.productListState.collectAsStateWithLifecycle()

    var showCircularProgress by remember { mutableStateOf(false) }

    LaunchedEffect(storeProductState) {
        when (storeProductState) {
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

    val productData: List<StoreProduct> = when (storeProductState) {
        is ResultResponse.Success -> {
            (storeProductState as ResultResponse.Success<StoreProductResponse>).data.data
        }

        else -> {
            emptyList()
        }
    }

    SearchBestRatings (
        modifier = modifier,
        showCircularProgress = showCircularProgress,
        productData = productData,
        homeViewModel = homeViewModel,
        onFlowerClick = onFlowerClick,
        onBackClick = onBackClick
    )

}

@Composable
private fun SearchBestRatings(
    modifier: Modifier = Modifier,
    showCircularProgress: Boolean,
    productData: List<StoreProduct>,
    homeViewModel: HomeViewModel,
    onFlowerClick: (String, String) -> Unit,
    onBackClick: () -> Unit
) {

    val bestRatedProducts by remember(productData) {
        mutableStateOf(
            productData.sortedWith(
                compareByDescending<StoreProduct> { it.rating }
                    .thenByDescending { it.reviewCount }
            )
        )
    }

    val focusManager = LocalFocusManager.current
    var query by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var filtered by remember { mutableStateOf(bestRatedProducts) }
    val focusRequester = remember { FocusRequester() }


    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(query, bestRatedProducts) {
        isLoading = true
        delay(1500)
        filtered = if (query.isBlank()) {
            bestRatedProducts
        } else {
            bestRatedProducts.filter { product ->
                product.name?.contains(query, ignoreCase = true) == true
            }
        }
        isLoading = false
    }

    FleuraSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = {
                        focusManager.clearFocus()
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "Best Ratings",
                    showNavigationIcon = true,
                    onBackClick = {
                        onBackClick()
                        focusManager.clearFocus()
                    }
                )


                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SearchBar(
                        query = query,
                        onSearch = {},
                        onQueryChange = {
                            query = it
                        },
                        focusRequester = focusRequester
                    )
                }

                if (showCircularProgress || isLoading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(if (isLoading) Color.White else base20)
                    ) {
                        CircularProgressIndicator(color = primaryLight)
                    }
                } else if (filtered.isEmpty()) {
                    EmptyProduct(
                        title = "No product found",
                        description = "Try another keyword"
                    )
                }
                else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(filtered) { index, product ->
                            SearchMerchantFlowerItem(
                                item = product,
                                onFlowerClick = {
//                                    onFlowerDetail(product.id)
                                    onFlowerClick(product.id, MainDestinations.DASHBOARD_ROUTE)
                                    homeViewModel.setSelectedProduct(storeProduct = product)
                                }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            if (index < filtered.lastIndex) {
                                HorizontalDivider(color = base40)
                            }
                        }
                    }
                }
            }

        }
    }
}