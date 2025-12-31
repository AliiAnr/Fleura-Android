package com.course.fleura.ui.screen.dashboard.point

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.R
import com.course.fleura.ui.common.formatNumber
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.FakeCategory
import com.course.fleura.ui.components.PointCard
import com.course.fleura.ui.components.RewardItem
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base100
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.secColor

@Composable
fun Point(
    modifier: Modifier
) {
    Point(
        modifier = modifier,
        onSnackClick = { _, _ -> }
    )
}

@Composable
private fun Point(
    modifier: Modifier = Modifier,
    onSnackClick: (Long, String) -> Unit,
) {
    FleuraSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(base20)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "Points",
                )
                TotalPoint(
                    point = 3000
                )
                OrderList(
                    items = FakeCategory.rewardItems,
                    onItemClicked = onSnackClick
                )

            }
//                InfoBox()
//                OrderList(
//                    items = FakeCategory.rewardItems,
//                    onItemClicked = onSnackClick
//                )
        }
    }
}


@Composable
private fun TotalPoint(
    modifier: Modifier = Modifier,
    point: Long
) {
    Box(
        modifier = modifier
            .padding(top = 8.dp)
            .background(Color.White)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.point_icon),
                    contentDescription = null,
                    tint = secColor,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatNumber(point),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Points",
                    color = base100,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
        }
    }
}

@Composable
private fun InfoBox(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                text = "Get Reward",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.W800,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.check_circle),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "The more often you shop, the more points you earn!",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.check_circle),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Enjoy greater rewards with every purchase.",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}

@Composable
private fun OrderList(
    modifier: Modifier = Modifier,
    items: List<RewardItem>,
    onItemClicked: (Long, String) -> Unit
) {
    Box(
        modifier = modifier
            .padding(top = 8.dp)
            .background(Color.White)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    InfoBox()
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = "Redeem Reward",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W800,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(items) { index ->
                    PointCard(
                        data = index,
                        onRedeemClicked = onItemClicked
                    )
                }
            }
        }
    }
}
