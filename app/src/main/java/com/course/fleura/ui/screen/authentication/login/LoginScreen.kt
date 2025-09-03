package com.course.fleura.ui.screen.authentication.login

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.course.fleura.R
import com.course.fleura.data.model.remote.LoginResponse
import com.course.fleura.data.resource.Resource
import com.course.fleura.di.factory.LoginViewModelFactory
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.components.CustomButton
import com.course.fleura.ui.components.CustomTextField
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.screen.navigation.MainDestinations
import com.course.fleura.ui.theme.primaryLight
import kotlinx.coroutines.delay
import kotlin.math.log

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToRoute: (String, Boolean) -> Unit,
    loginViewModel: LoginScreenViewModel,
    onBackClick: () -> Unit
) {

    var showCircularProgress by remember { mutableStateOf(false) }
    var showInvalidMessage by remember { mutableStateOf(false) }

    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

    val userState by loginViewModel.userState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

    LaunchedEffect(loginState) {
        when (loginState) {
            is ResultResponse.Success -> {
                Log.e(
                    "LoginScreen",
                    "Login successful: ${(loginState as ResultResponse.Success).data}"
                )

//                print("doksaodk")

                loginViewModel.getUser()

//                navigateToRoute("${MainDestinations.DASHBOARD_ROUTE}?", true)
            }

            is ResultResponse.Loading -> {
                showCircularProgress = true
            }

            is ResultResponse.Error -> {
                showCircularProgress = false
                showInvalidMessage = true
                Log.e(
                    "LoginScreen",
                    "Login error: ${(loginState as ResultResponse.Error).error}"
                )
            }

            else -> {}
        }
    }

    LaunchedEffect(userState) {
        when (userState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                val detail = (userState as ResultResponse.Success).data.data
                Log.e("LoginScreen", "User detail: $detail")

                if (detail.isProfileComplete()) {
                    loginViewModel.setPersonalizeCompleted()
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

    LaunchedEffect(showInvalidMessage) {
        if (showInvalidMessage) {
            delay(5000L)
            showInvalidMessage = false
        }
    }

    LoginScreen(
        modifier = modifier,
        loginViewModel = loginViewModel,
        showCircularProgress = showCircularProgress,
        showInvalidMessage = showInvalidMessage,
        onBackClick = onBackClick,
        navigateToRoute = navigateToRoute
    )
}

@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginScreenViewModel,
    onBackClick: () -> Unit,
    showCircularProgress: Boolean,
    showInvalidMessage: Boolean,
    navigateToRoute: (String, Boolean) -> Unit
) {

    val focusManager = LocalFocusManager.current

    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFFFFD700), fontWeight = FontWeight.ExtraBold)) {
            append("Welcome back!")
        }
        withStyle(style = SpanStyle(color = primaryLight, fontWeight = FontWeight.ExtraBold)) {
            append("\nGlad to see you")
        }

    }

    FleuraSurface(modifier = modifier.fillMaxSize()) {
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
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "",
                    showNavigationIcon = true,
                    horizontalPadding = 0.dp,
                    onBackClick = {
                        onBackClick()
                        loginViewModel.resetState()
                    }
                )
                Text(
                    text = title,
                    textAlign = TextAlign.Start,
                    fontSize = 36.sp,
                    color = primaryLight,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 50.sp,
                    modifier = Modifier
                        .padding(top = 30.dp, bottom = 30.dp)
                        .align(Alignment.Start)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CustomTextField(
                        value = loginViewModel.emailValue,
                        onChange = loginViewModel::setEmail,
                        placeholder = "Email",
                        isError = loginViewModel.emailError.isNotEmpty(),
                        icon = Icons.Rounded.Email,
                        errorMessage = loginViewModel.emailError,
                        keyboardType = KeyboardType.Email,
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    CustomTextField(
                        value = loginViewModel.passwordValue,
                        onChange = loginViewModel::setPassword,
                        placeholder = "Password",
                        isError = loginViewModel.passwordError.isNotEmpty(),
                        icon = Icons.Rounded.Lock,
                        isPassword = true,
                        errorMessage = loginViewModel.passwordError,
                    )
                    Text(
                        text = "Forgot Password?",
                        modifier = Modifier
                            .clickable {
//                            navController.navigate(Graph.FORGOT_PASSWORD)
                            }
                            .align(Alignment.End)
                            .padding(bottom = 35.dp),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                    )
                }

                CustomButton(
                    text = "Login",
                    isAvailable = loginViewModel.emailError.isEmpty() && loginViewModel.emailValue.isNotEmpty() &&
                            loginViewModel.passwordError.isEmpty() && loginViewModel.passwordValue.isNotEmpty() && !showCircularProgress,
                    onClick = {
                        focusManager.clearFocus()
                        loginViewModel.loginUser()
                    },
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.5.dp,
                        color = Color.Gray
                    )
                    Text(
                        text = " Or Login with ",
                        color = Color(0xFF1C1C1C),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.5.dp,
                        color = Color.Gray
                    )
                }

                CustomButton(
                    text = "",
                    isOutlined = true,
                    borderWidth = 1.dp,
                    outlinedColor = Color.Gray,
                    icon = painterResource(id = R.drawable.google_ic),
                    onClick = {
                        // Handle Google login action
                    },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .height(55.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(
                        text = "Don't Have Account?",
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                    )
                    Text(
                        text = " Register!",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    navigateToRoute(MainDestinations.REGISTER_ROUTE, true)
                                    loginViewModel.resetState()
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                        color = primaryLight,
                    )
                }

                if (!showInvalidMessage) {
                    Spacer(modifier = Modifier.height(24.dp))
                } else {
                    AnimatedVisibility(
                        visible = showInvalidMessage,
                        enter = fadeIn() + scaleIn(initialScale = 0.8f),
                        exit = fadeOut(animationSpec = tween(durationMillis = 500)) + scaleOut(
                            targetScale = 0.8f
                        )
                    ) {
                        Text(
                            text = "Email atau Kata sandi salah!",
                            color = Color.Red,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
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

//private fun handleLoginError(context: Context, errorMessage: String) {
//    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//}
