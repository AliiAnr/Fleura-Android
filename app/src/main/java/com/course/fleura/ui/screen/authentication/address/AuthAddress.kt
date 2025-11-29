package com.course.fleura.ui.screen.authentication.address

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.common.formatCurrency
import com.course.fleura.ui.components.CustomButton
import com.course.fleura.ui.components.CustomTextField
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.screen.authentication.login.LoginScreenViewModel
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.screen.navigation.MainDestinations
import com.course.fleura.ui.theme.primaryLight


@Composable
fun AuthAddress(
    modifier: Modifier = Modifier,
    navigateToRoute: (String, Boolean) -> Unit,
    loginViewModel: LoginScreenViewModel
) {

    var showCircularProgress by remember { mutableStateOf(false) }

    val personalizeState by loginViewModel.personalizeState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    val userState by loginViewModel.userState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    LaunchedEffect(personalizeState) {
        Log.e("AddressScreen", "Personalize State: $personalizeState")
        when (personalizeState) {
            is ResultResponse.Success -> {
                showCircularProgress = true
                loginViewModel.getUser()
            }

            is ResultResponse.Loading -> {
                showCircularProgress = true
            }

            is ResultResponse.Error -> {
                showCircularProgress = false
                loginViewModel.setPersonalizeState(ResultResponse.None)
            }

            else -> {}
        }
    }

    LaunchedEffect(userState) {
        when (userState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                val detail = (userState as ResultResponse.Success).data.data
                Log.e("AddressScreen", "User detail: $detail")
                if (detail.isProfileComplete()) {
                    loginViewModel.setPersonalizeCompleted()
                    navigateToRoute(MainDestinations.DASHBOARD_ROUTE, true)
                } else {
                    navigateToRoute(MainDestinations.USERNAME_ROUTE, true)
                }
                loginViewModel.resetAllState()
            }

            is ResultResponse.Loading -> {
                showCircularProgress = true
            }

            is ResultResponse.Error -> {
                showCircularProgress = false
                loginViewModel.setPersonalizeState(ResultResponse.None)
            }

            else -> {}
        }
    }

    AddressScreen(
        modifier = modifier,
        showCircularProgress = showCircularProgress,
        setCircularProgress = { value ->
            showCircularProgress = value
        },
        loginViewModel = loginViewModel,
    )

}

@Composable
private fun AddressScreen(
    modifier: Modifier = Modifier,
    showCircularProgress: Boolean,
    setCircularProgress: (Boolean) -> Unit,
    loginViewModel: LoginScreenViewModel,
) {

    val focusManager = LocalFocusManager.current

    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFFFFD700), fontWeight = FontWeight.ExtraBold)) {
            append("Yeay ")
        }

        withStyle(style = SpanStyle(color = primaryLight, fontWeight = FontWeight.ExtraBold)) {
            append("you are in the final step to sign in!")
        }

    }

    var citizenCardUri by remember { mutableStateOf<Uri?>(null) }

    FleuraSurface(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                },
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
                    title = "",
                    showNavigationIcon = false
                )
                Box(
                    modifier = Modifier.weight(1f) // Membuat LazyColumn fleksibel
                ) {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        item {
                            Text(
                                text = title,
                                textAlign = TextAlign.Start,
                                fontSize = 36.sp,
                                color = primaryLight,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 50.sp,
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                            )
//                        Spacer(modifier = Modifier.height(20.dp))
                        }

                        item {
                            Text(
                                text = "Please enter name & phone in this address where we can deliver your order:",
                                textAlign = TextAlign.Start,
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(vertical = 15.dp ,horizontal = 20.dp)
                            )
//                        Spacer(modifier = Modifier.height(20.dp))
                        }

                        item {
                            CustomTextField(
                                value = loginViewModel.addressNameValue,
                                onChange = loginViewModel::setAddressName,
                                placeholder = "Name",
                                isError = false,
                                errorMessage = "",
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }

                        item {
                            CustomTextField(
                                value = loginViewModel.phonNumberValue,
                                onChange = loginViewModel::setPhonNumber,
                                placeholder = "Phone Number",
                                isError = false,
                                keyboardType = KeyboardType.NumberPassword,
                                errorMessage = "",
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        item {
                            Text(
                                text = "Please enter your address:",
                                textAlign = TextAlign.Start,
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(vertical = 15.dp,horizontal = 20.dp).fillMaxWidth()
                            )
//                        Spacer(modifier = Modifier.height(20.dp))
                        }

                        item {
                            CustomTextField(
                                value = loginViewModel.provinceValue,
                                onChange = loginViewModel::setProvince,
                                placeholder = "Province",
                                isError = false,
                                errorMessage = "",
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        item {
                            CustomTextField(
                                value = loginViewModel.cityValue,
                                onChange = loginViewModel::setCity,
                                placeholder = "City/District",
                                isError = false,
                                errorMessage = "",
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        item {
                            CustomTextField(
                                value = loginViewModel.subDistrictValue,
                                onChange = loginViewModel::setSubDistrict,
                                placeholder = "Sub-District",
                                isError = false,
                                errorMessage = "",
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        item {
                            CustomTextField(
                                value = loginViewModel.postalCodeValue,
                                onChange = loginViewModel::setPostalCode,
                                placeholder = "Postal Code",
                                isError = false,
                                errorMessage = "",
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        item {
                            CustomTextField(
                                value = loginViewModel.streetNameValue,
                                onChange = loginViewModel::setStreetName,
                                placeholder = "Street Name, Building, House No.",
                                isError = false,
                                errorMessage = "",
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }

//                    item {
//                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
//                            ImagePickerCard(
//                                label = "Shop Photo",
//                                imageUri = citizenCardUri,
//                                onImagePicked = { uri ->
//                                    citizenCardUri = uri
//                                }
//                            )
//                        }
//                        Spacer(modifier = Modifier.height(20.dp))
//                    }

                        item {
                            CustomTextField(
                                value = loginViewModel.additionalDetailValue,
                                onChange = loginViewModel::setAdditionalDetail,
                                placeholder = "Additional Detail",
                                isError = false,
                                errorMessage = "",
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .height(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomButton(
                        text = "Register",
                        isOutlined = true,
                        outlinedColor = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        isAvailable = loginViewModel.streetNameValue.isNotEmpty() &&
                                loginViewModel.subDistrictValue.isNotEmpty() &&
                                loginViewModel.cityValue.isNotEmpty() &&
                                loginViewModel.provinceValue.isNotEmpty() &&
                                loginViewModel.postalCodeValue.isNotEmpty() &&
                                loginViewModel.additionalDetailValue.isNotEmpty() &&
                                !showCircularProgress,
                        onClick = {
                            setCircularProgress(true)
                            focusManager.clearFocus()
                            loginViewModel.addUserAddress()
                        },
                    )
                }
            }

            if (showCircularProgress) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            // Do nothing - this prevents clicks from passing through
                        }
                ) {
                    CircularProgressIndicator(color = primaryLight)
                }
            }
        }
    }
}