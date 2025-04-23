package com.course.fleura.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.course.fleura.ui.theme.base20

@Composable
fun ArticleCategoryLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0..3) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .height(30.dp)
                        .padding(vertical = 2.dp)
                        .loadingFx()
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 0..5) {
                ArticleItemLoading()

            }
        }
    }
}

@Composable
fun StoreListLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding( bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(15.dp)
                    .loadingFx()
            )

            Box(
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 2.dp)
                    .width(20.dp)
                    .height(15.dp)
                    .loadingFx()
            )

        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 2.dp)
        )
        {
            items(3) {
                StoreItemLoading()
            }
        }
    }
}

@Composable
fun RatingListSkeleton(
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(4) {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .width(240.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .loadingFx()
            )
        }
    }
}

@Composable
fun StoreItemLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .width(200.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 2.dp)
                .fillMaxWidth()
                .height(115.dp)
                .clip(RoundedCornerShape(10.dp))
                .loadingFx()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .height(12.dp)
                .fillMaxWidth(0.4f)
                .loadingFx()
        )
        Box(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 2.dp)
                .height(10.dp)
                .fillMaxWidth(0.6f)
                .loadingFx()
        )
    }
}

@Composable
fun ProductListLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(15.dp)
                    .loadingFx()
            )

            Box(
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 2.dp)
                    .width(20.dp)
                    .height(15.dp)
                    .loadingFx()
            )

        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 2.dp)
        )
        {
            items(3) {
                ProductItemLoading()
            }
        }
    }
}

@Composable
fun ProductItemLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .width(140.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 2.dp)
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(10.dp))
                .loadingFx()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .height(12.dp)
                .fillMaxWidth(0.8f)
                .loadingFx()
        )
        Box(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 2.dp)
                .height(10.dp)
                .fillMaxWidth(0.4f)
                .loadingFx()
        )
        Box(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 2.dp)
                .height(10.dp)
                .fillMaxWidth(0.6f)
                .loadingFx()
        )
    }
}


@Composable
fun ArticleItemLoading(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .height(95.dp)
            .loadingFx()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(95.dp)
                .clip(RoundedCornerShape(10.dp))
                .loadingFx(),
        )
        Spacer(modifier = Modifier.width(25.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 2.dp)
                    .height(12.dp)
                    .fillMaxWidth(0.5f)
                    .loadingFx()
            )
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .height(10.dp)
                    .fillMaxWidth(0.9f)
                    .loadingFx()
            )
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 2.dp)
                    .height(10.dp)
                    .fillMaxWidth(0.8f)
                    .loadingFx()
            )
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 5.dp)
                    .height(10.dp)
                    .fillMaxWidth(0.4f)
                    .loadingFx()
            )
        }
    }
}