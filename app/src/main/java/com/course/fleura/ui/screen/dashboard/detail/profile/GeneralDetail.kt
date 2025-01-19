package com.course.fleura.ui.screen.dashboard.detail.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.R
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.EmptyCart
import com.course.fleura.ui.components.Profile
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base500
import com.course.fleura.ui.theme.dividerColor

@Composable
fun GeneralDetail(
    modifier: Modifier = Modifier,
    id: Int = 0,
    location: String
) {

    //call API here

    GeneralDetail(
        modifier = modifier,
        data = Profile(
            image = R.drawable.profile_temp,
            name = "John Doe",
            email = "alidaldia@gmail.com",
            phone = "081234567890",
            points = 1020,
            address = listOf("Jl. Raya Bogor", "Bogor", "Jawa Barat", "Indonesia")
        ),
        location = location
    )
}

@Composable
private fun GeneralDetail(
    modifier: Modifier = Modifier,
    data: Profile,
    location: String
) {
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
                CustomTopAppBar(
                    title = location,
                    showNavigationIcon = true,
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    when (location) {
                        "Profile" -> {
                            EditProfile(
                                data = data
                            )
                        }

                        "Address" -> {
                            ListAddress (
                                data = data
                            )
                        }

                        "Change Password" -> {
                            Text(text = "Change Password")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditProfile(
    modifier: Modifier = Modifier,
    data: Profile
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
        EditProfileHeader(
            modifier = modifier,
            data = data
        )
        Spacer(modifier = Modifier.height(20.dp))
        EditProfileItem(
            label = "Name",
            value = data.name
        )
        EditProfileItem(
            label = "Email",
            value = data.email
        )
        PhoneNumberItem(
            phoneNumber = data.phone
        )
    }
}

@Composable
fun EditProfileHeader(
    modifier: Modifier = Modifier,
    data: Profile
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                val image = null
                if (image == null) {
                    Icon(
                        painter = painterResource(id = R.drawable.default_profile), // Replace with your default profile icon
                        contentDescription = "Default Profile Icon",
                        tint = Color.Black,
                        modifier = Modifier.size(70.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(image), // Example using Coil for image loading
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
            }
            Icon(
                painter = painterResource(id = R.drawable.editprofile), // Replace with your edit icon
                contentDescription = "Edit Icon",
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 0.dp, y = 0.dp),
                tint = Color.Black
            )

        }
    }
}

@Composable
fun EditProfileItem(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = base500,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = dividerColor
        )
    }
}

@Composable
fun PhoneNumberItem(
    phoneNumber: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Phone Number",
            color = base500,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.width(50.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.flag), // Replace with your edit icon
                        contentDescription = "Edit Icon",
                        modifier = Modifier
                            .size(20.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+62",
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    color = Color.LightGray
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = phoneNumber,
                    fontSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = dividerColor
                )
            }
        }
    }
}