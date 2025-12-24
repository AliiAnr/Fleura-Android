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
import com.course.fleura.data.model.remote.ListStoreResponse
import com.course.fleura.data.model.remote.StoreItem
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.EmptyProduct
import com.course.fleura.ui.components.SearchBar
import com.course.fleura.ui.components.SearchListStoreItem
import com.course.fleura.ui.screen.dashboard.home.HomeViewModel
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base40
import com.course.fleura.ui.theme.primaryLight
import kotlinx.coroutines.delay


@Composable
fun SearchStore(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    onStoreClick: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    val storeState by homeViewModel.storeListState.collectAsStateWithLifecycle()
    var showCircularProgress by remember { mutableStateOf(false) }

    LaunchedEffect(storeState) {
        showCircularProgress = storeState is ResultResponse.Loading
    }

    val storeData: List<StoreItem> = when (storeState) {
        is ResultResponse.Success -> (storeState as ResultResponse.Success<ListStoreResponse>).data.data
        else -> emptyList()
    }

    SearchStoreContent(
        modifier = modifier,
        showCircularProgress = showCircularProgress,
        storeData = storeData,
        onStoreClick = onStoreClick,
        onBackClick = onBackClick
    )
}

@Composable
private fun SearchStoreContent(
    modifier: Modifier = Modifier,
    showCircularProgress: Boolean,
    storeData: List<StoreItem>,
    onStoreClick: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var query by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var filtered by remember { mutableStateOf(storeData) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(query, storeData) {
        isLoading = true
        delay(500)
        filtered = if (query.isBlank()) {
            storeData
        } else {
            storeData.filter { it.name.contains(query, ignoreCase = true) }
        }
        isLoading = false
    }

    FleuraSurface(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = { focusManager.clearFocus() },
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
                    title = "Search Store",
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
                        onQueryChange = { query = it },
                        focusRequester = focusRequester
                    )
                }

                when {
                    showCircularProgress || isLoading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(if (isLoading) Color.White else base20)
                        ) {
                            CircularProgressIndicator(color = primaryLight)
                        }
                    }
                    filtered.isEmpty() -> {
                        EmptyProduct(
                            title = "No store found",
                            description = "Try another keyword"
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            itemsIndexed(filtered) { index, store ->
                                SearchListStoreItem(
                                    store = store,
                                    onStoreClick = { storeId, origin ->
                                        onStoreClick(storeId, origin )
                                    }
                                )
                                Spacer(modifier = Modifier.height(14.dp))
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
}