package com.course.fleura.ui.screen.authentication.username

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.components.CustomButton
import com.course.fleura.ui.components.CustomTextField
import com.course.fleura.ui.screen.authentication.login.LoginScreenViewModel
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.screen.navigation.MainDestinations
import com.course.fleura.ui.theme.primaryLight

@Composable
fun UsernameScreen(
//    navController: NavHostController,
//    context: MainActivity,
    modifier: Modifier = Modifier,
    loginScreenViewModel: LoginScreenViewModel,
    navigateToRoute: (String, Boolean) -> Unit,
) {
    var showCircularProgress by remember { mutableStateOf(false) }

    val personalizeState by loginScreenViewModel.personalizeState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)
    val userState by loginScreenViewModel.userState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)


// Then add a LaunchedEffect to respond to state changes
    LaunchedEffect(personalizeState) {
        Log.e("UsernameScreen", "PersonalizeState changed: $personalizeState")
        when (personalizeState) {
            is ResultResponse.Success -> {
                Log.e("UsernameScreen", "Username set successfully")
                showCircularProgress = true
                loginScreenViewModel.getUser()
            }
            is ResultResponse.Loading -> {
                Log.e("UsernameScreen", "Loading...")
                showCircularProgress = true
            }
            is ResultResponse.Error -> {
                Log.e("UsernameScreen", "Error: ${(personalizeState as ResultResponse.Error).error}")
                showCircularProgress = false
                // Show error message
            }
            else -> {}
        }
    }

    LaunchedEffect(userState) {
        when (userState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                val detail = (userState as ResultResponse.Success).data.data
                Log.e("GoalScreen", "User detail: $detail")
                // Cek langsung pada detail yang diterima
                if (detail.isProfileComplete()) {
                    loginScreenViewModel.setPersonalizeCompleted()
                    navigateToRoute(MainDestinations.DASHBOARD_ROUTE, true)
                } else {
                    navigateToRoute(MainDestinations.USERNAME_ROUTE, true)
                }
            }
            is ResultResponse.Loading -> {
                showCircularProgress = true
            }
            is ResultResponse.Error -> {
                showCircularProgress = false
                Log.e("LoginScreen", "Get user error: ${(userState as ResultResponse.Error).error}")
            }
            else -> {}
        }
    }

    UsernameScreen (
        modifier = modifier,
        loginScreenViewModel = loginScreenViewModel,
        showCircularProgress = showCircularProgress
    )

}

@Composable
private fun UsernameScreen(
    modifier: Modifier = Modifier,
    loginScreenViewModel: LoginScreenViewModel,
    showCircularProgress: Boolean,
) {
    val focusManager = LocalFocusManager.current


    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = primaryLight, fontWeight = FontWeight.ExtraBold)) {
            append("How should we call you?")
        }

    }
    FleuraSurface(
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        onClick = {
                            focusManager.clearFocus()
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Start,
                    fontSize = 36.sp,
                    color = primaryLight,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 50.sp,
                    modifier = Modifier
                        .padding(top = 120.dp, bottom = 16.dp)
                        .align(Alignment.Start)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Please enter your nickname (one word only)",
                        textAlign = TextAlign.Start,
                        fontSize = 15.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 50.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    CustomTextField(
                        value = loginScreenViewModel.usernameValue,
                        onChange = loginScreenViewModel::setUsername,
                        placeholder = "Username",
                        isError = loginScreenViewModel.usernameError.isNotEmpty(),
                        icon = Icons.Rounded.Email,
                        errorMessage = loginScreenViewModel.usernameError,
                        modifier = Modifier.padding(bottom = 40.dp)
                    )
                }

                CustomButton(
                    text = "Submit",
                    isAvailable = loginScreenViewModel.usernameError.isEmpty() && loginScreenViewModel.usernameValue.isNotEmpty() && !showCircularProgress,
                    onClick = {
                        focusManager.clearFocus()
                        try {
                            Log.e("CLIC", "Button clicked - calling inputUsername()")
                            loginScreenViewModel.inputUsername() // Make sure this method exists in your ViewModel
                            Log.e("CLIC", "inputUsername() call completed")
                        } catch (e: Exception) {
                            Log.e("CLIC", "Error calling inputUsername(): ${e.message}", e)
                        }
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
            ) {
                CircularProgressIndicator(color = primaryLight)
            }
        }
    }
}

//private fun handleLoginError(context: Context, errorMessage: String) {
//    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//}
