package com.course.fleura.ui.screen.dashboard.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.R
import com.course.fleura.ui.components.AccountList
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.FakeCategory
import com.course.fleura.ui.components.Profile
import com.course.fleura.ui.components.TEMP_ID
import com.course.fleura.ui.screen.dashboard.home.SectionText
import com.course.fleura.ui.screen.dashboard.point.Point
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base20

@Composable
fun Profile(
    modifier: Modifier
) {
    Profile(
        modifier = modifier,
        onItemClick = { _, _ ->

        },
        data = Profile(
            image = R.drawable.profile_temp,
            name = "John Doe",
            email = "alidaldia@gmail.com",
            phone = "081234567890",
            points = 1020,
            address = listOf("Jl. Raya Bogor", "Bogor", "Jawa Barat", "Indonesia")
        )
    )
}

@Composable
private fun Profile(
    modifier: Modifier = Modifier,
    onItemClick: (Long, String) -> Unit,
    data: Profile
) {
    FleuraSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    CustomTopAppBar(
                        title = "Profile",
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }
                item {
                    Header(
                        data = data
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    AccountList(
                        data = FakeCategory.accountItem,
                        onItemClick = onItemClick
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    GeneralList(
                        data = FakeCategory.generalItem,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    data: Profile
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = data.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(100.dp))
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = data.name,
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = data.email,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    icon: Int,
    id: Long,
    title: String,
    onItemClick: (Long, String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
                .clickable {
                    onItemClick(id, title)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Navigate",
                tint = Color.Black,
                modifier = Modifier
                    .size(16.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = "Navigate",
                tint = Color.Black,
                modifier = Modifier
                    .size(16.dp)
            )
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

@Composable
private fun AccountList(
    modifier: Modifier = Modifier,
    data: List<AccountList>,
    onItemClick: (Long, String) -> Unit,
) {
    Column(

    ) {
        SectionText(
            title = "My Account",
            fontWeight = FontWeight.W700,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        data.forEach {
            MenuItem(
                icon = it.icon,
                id = TEMP_ID,
                title = it.name,
                onItemClick = onItemClick
            )
        }

    }
}

@Composable
private fun GeneralList(
    modifier: Modifier = Modifier,
    data: List<AccountList>,
    onItemClick: (Long, String) -> Unit,
) {
    Column(

    ) {
        SectionText(
            title = "General",
            fontWeight = FontWeight.W700,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        data.forEach {
            MenuItem(
                icon = it.icon,
                id = TEMP_ID,
                title = it.name,
                onItemClick = onItemClick,
            )
        }

    }
}
