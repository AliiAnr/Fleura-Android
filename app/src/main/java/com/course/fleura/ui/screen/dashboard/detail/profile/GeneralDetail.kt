package com.course.fleura.ui.screen.dashboard.detail.profile

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleura.R
import com.course.fleura.data.model.remote.AddressItem
import com.course.fleura.data.model.remote.Detail
import com.course.fleura.data.model.remote.ListAddressResponse
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.components.CustomButton
import com.course.fleura.ui.components.CustomPopUpDialog
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.EmptyCart
import com.course.fleura.ui.components.Profile
import com.course.fleura.ui.screen.dashboard.profile.ProfileViewModel
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base500
import com.course.fleura.ui.theme.dividerColor

@Composable
fun GeneralDetail(
    modifier: Modifier = Modifier,
    location: String,
    onBackClick: () -> Unit,
    onAddAddressClick: () -> Unit,
    onAddressClick: (String) -> Unit,
    profileViewModel: ProfileViewModel
) {

    val profileState by profileViewModel.profileDetailState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)
    val listAddressState by profileViewModel.userAddressListState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

    val userData = when (profileState) {
        is ResultResponse.Success -> (profileState as ResultResponse.Success<Detail?>).data
        else -> null
    }

    val addressList = when (listAddressState) {
        is ResultResponse.Success -> (listAddressState as ResultResponse.Success<ListAddressResponse>).data.data
        else -> emptyList()
    }

    LaunchedEffect(profileState) {
        Log.e("PROFILE STATE DETAIYL", profileState.toString())
    }

    var showDialog by remember { mutableStateOf(false) }

    GeneralDetail(
        modifier = modifier,
        addressList = addressList,
        data = Profile(
            image = R.drawable.profile_temp,
            name = "John Doe",
            email = "alidaldia@gmail.com",
            phone = "081234567890",
            points = 1020,
            address = listOf("Jl. Raya Bogor", "Bogor", "Jawa Barat", "Indonesia")
        ),
        location = location,
        onBackClick = onBackClick,
        onAddressClick = { id, addressItem ->
            profileViewModel.setSelectedAddress(addressItem)
            onAddressClick(id)
        },
        onAddAddressClick = onAddAddressClick,
        userData = userData,
        showDialog = showDialog,
        onDismiss = { showDialog = false }
    )
}

@Composable
private fun GeneralDetail(
    modifier: Modifier = Modifier,
    data: Profile,
    addressList : List<AddressItem>,
    location: String,
    onBackClick: () -> Unit,
    onAddressClick: (String, AddressItem) -> Unit,
    onAddAddressClick: () -> Unit,
    userData: Detail?,
    showDialog: Boolean,
    onDismiss: () -> Unit
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
                    onBackClick = onBackClick
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    when (location) {
                        "Edit Profile" -> {
                            EditProfile(
                                userData = userData
                            )
                        }

                        "Address" -> {
                            ListAddress (
                                addressList = addressList,
                                onAddAddressClick = onAddAddressClick,
                                onAddressClick = onAddressClick
                            )
                        }

                        "Change Password" -> {
                            Text(text = "Change Password")
                        }
                    }
                }
            }
            if (showDialog) {
                CustomPopUpDialog(
                    onDismiss = onDismiss,
                    isShowIcon = true,
                    isShowTitle = true,
                    isShowDescription = true,
                    isShowButton = false,
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ceklist),
                                contentDescription = null,
                                tint = Color.Unspecified,
                            )
                        }
                    },
                    title = "Pembayaran Berhasil",
                    description = "Terimakasih telah melakukan pembayaran"
                )
            }
        }
    }
}

@Composable
fun EditProfile(
    modifier: Modifier = Modifier,
    userData: Detail?,
) {

    val focusManager = LocalFocusManager.current

// Simpan nilai awal userData
    val initialName = userData?.name ?: "No Name"
    val initialEmail = userData?.email ?: "No Email"
    val initialPhone = userData?.phone ?: ""

// State input
    var name by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf(initialEmail) }
    var phone by remember { mutableStateOf(initialPhone) }

// Update state saat userData berubah
    LaunchedEffect(userData) {
        name = initialName
        email = initialEmail
        phone = initialPhone
    }

// Deteksi perubahan
    val isChanged = name != initialName || email != initialEmail || phone != initialPhone

    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
        EditProfileHeader(
            modifier = modifier,
            name = name
        )
        Spacer(modifier = Modifier.height(20.dp))

        FlatInputText(
            label = "Name",
            value = name,
            onValueChange = { name = it }
        )
        Spacer(Modifier.height(12.dp))
        FlatInputText(
            label = "Email",
            value = email,
            isEmail = true,
            onValueChange = { email = it },
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(12.dp))
        FlatPhoneNumberInput(
            phoneNumber = phone.toString(),
            onValueChange = { phone = it }
        )
        Spacer(Modifier.height(32.dp))
        CustomButton(
            text = "Save",
            onClick = {

            },
            isAvailable = isChanged
        )
    }
}

@Composable
fun EditProfileHeader(
    modifier: Modifier = Modifier,
    name: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
//            Box(
//                modifier = Modifier
//                    .size(80.dp)
//                    .clip(CircleShape),
//                contentAlignment = Alignment.Center
//            ) {
//                val image = null
//                if (image == null) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.default_profile), // Replace with your default profile icon
//                        contentDescription = "Default Profile Icon",
//                        tint = Color.Black,
//                        modifier = Modifier.size(70.dp)
//                    )
//
//                } else {
//                    Image(
//                        painter = painterResource(R.drawable.flag), // Example using Coil for image loading
//                        contentDescription = "Profile Picture",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .clip(CircleShape)
//                    )
//                }
//            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFC8A2C8))
            ) {
                Text(
                    text = name.firstOrNull()?.uppercase() ?: "",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                )
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
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            color = dividerColor
        )
    }
}

@Composable
fun FlatInputText(
    label: String,
    value: String,
    isEmail: Boolean = false,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = base500,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 2.dp)
        )

        if (isEmail){
            Column (modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.5f))
            ){
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = dividerColor
            )
                }
        }
        else {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                decorationBox = { innerTextField ->
                    Column {
                        innerTextField()
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.LightGray,
                        )
                    }
                }
            )
        }
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
                HorizontalDivider(
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
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = dividerColor
                )
            }
        }
    }
}

@Composable
fun FlatPhoneNumberInput(
    phoneNumber: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Phone Number",
            fontSize = 14.sp,
            color = base500,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.width(55.dp),
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
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color.LightGray
                )
            }
            Spacer(Modifier.width(10.dp))
            BasicTextField(
                value = phoneNumber,
                onValueChange = { onValueChange(it.filter { c -> c.isDigit() }) },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Column {
                        Box {
                            if (phoneNumber.isEmpty()) {
                                Text(
                                    text = "No Phone Number",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                            innerTextField()
                        }
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                    }
                }
            )

        }
    }
}