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
fun CategoryDetail (
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit
) {

    var isEmpty by remember { mutableStateOf(false) }


    CategoryDetail(
        modifier = modifier,
        title = title,
        isEmpty = isEmpty,
        onBackClick = onBackClick
    )

}

@Composable
private fun CategoryDetail (
    modifier: Modifier = Modifier,
    title: String,
    isEmpty: Boolean,
    onBackClick: () -> Unit
) {

    FleuraSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
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
                    title = title,
                    showNavigationIcon = true,
                    onBackClick = {
                        onBackClick()
                    }
                )

                EmptyProduct(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    title = "Coming Soon",
                    description = "This category feature is currently under development"
                )



            }
        }
    }
}