package com.course.fleura.ui.screen.authentication.otp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
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
import com.course.fleura.ui.common.OtpAction
import com.course.fleura.ui.common.OtpState
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.OtpInputField
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.primaryLight
import kotlinx.coroutines.delay
import kotlin.code

@Composable
fun OtpScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel<OtpScreenViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusRequesters = remember {
        List(5) { FocusRequester() }
    }
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.focusedIndex) {
        state.focusedIndex?.let { index ->
            focusRequesters.getOrNull(index)?.requestFocus()
        }
    }

    LaunchedEffect(state.code, keyboardManager) {
        val allNumbersEntered = state.code.none { it == null }
        if (allNumbersEntered) {
            focusRequesters.forEach {
                it.freeFocus()
            }
            focusManager.clearFocus()
            keyboardManager?.hide()
        }
    }

    LaunchedEffect(state.isValid) {
        if (state.isValid == false) {
            viewModel.onAction(OtpAction.OnError)
            focusManager.clearFocus()
        }
    }

    OtpScreen(
        state = state,
        focusRequesters = focusRequesters,
        onAction = { action ->
            when (action) {
                is OtpAction.OnEnterNumber -> {
                    if (action.number != null) {
                        focusRequesters[action.index].freeFocus()
                    }
                }

                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}


@Composable
private fun OtpScreen(
    modifier: Modifier = Modifier,
    state: OtpState,
    focusRequesters: List<FocusRequester>,
    onAction: (OtpAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var showInvalidOtpMessage by remember { mutableStateOf(false) }

    LaunchedEffect(state.isValid) {
        if (state.isValid == false) {
            showInvalidOtpMessage = true
            delay(500)
            showInvalidOtpMessage = false
        }
    }

    FleuraSurface(
        modifier = modifier.fillMaxSize(),
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
                    showNavigationIcon = true
                )
                Spacer(modifier = Modifier.height(80.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val title = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal)) {
                            append("Didn't receive an email? ")
                        }
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Normal)) {
                            append("Resent")
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.amico),
                        contentDescription = null,
                        modifier = Modifier
                            .size(250.dp)
                            .padding(bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "OTP Verification",
                        textAlign = TextAlign.Start,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Enter the verification code was just send to your email address",
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        state.code.forEachIndexed { index, number ->
                            OtpInputField(
                                number = number,
                                focusRequester = focusRequesters[index],
                                onFocusChanged = { isFocused ->
                                    if (isFocused) {
                                        onAction(OtpAction.OnChangeFieldFocused(index))
                                    }
                                },
                                onNumberChanged = { newNumber ->
                                    onAction(OtpAction.OnEnterNumber(newNumber, index))
                                },
                                onKeyboardBack = {
                                    onAction(OtpAction.OnKeyboardBack)
                                },
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (!showInvalidOtpMessage){
                        Spacer(modifier = Modifier.height(24.dp))
                    } else {
                        AnimatedVisibility(
                            visible = showInvalidOtpMessage,
                            enter = fadeIn() + scaleIn(initialScale = 0.8f),
                            exit = fadeOut(animationSpec = tween(durationMillis = 500)) + scaleOut(targetScale = 0.8f)
                        ) {
                            Text(
                                text = "OTP is invalid!",
                                color = Color.Red,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}