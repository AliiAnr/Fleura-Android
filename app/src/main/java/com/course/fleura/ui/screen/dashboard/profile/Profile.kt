package com.course.fleura.ui.screen.dashboard.profile

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleura.R
import com.course.fleura.data.model.remote.Detail
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.components.AccountList
import com.course.fleura.ui.components.CustomButton
import com.course.fleura.ui.components.CustomPopUpDialog
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.FakeCategory
import com.course.fleura.ui.components.TEMP_ID
import com.course.fleura.ui.screen.dashboard.home.SectionText
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.screen.navigation.MainDestinations
import com.course.fleura.ui.theme.base40
import com.course.fleura.ui.theme.primaryLight

@Composable
fun Profile(
    modifier: Modifier,
    onProfileDetailClick: (String) -> Unit,
    onNavigateOut: (String, Boolean) -> Unit,
    profileViewModel: ProfileViewModel
) {

    LaunchedEffect(Unit) {
        profileViewModel.loadInitialData()
    }

    val profileState by profileViewModel.profileDetailState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

    LaunchedEffect(profileState) {
        Log.e("PROFILE STATE", profileState.toString())
    }

    var logoutLoading by remember { mutableStateOf(false) }

    val logoutState by profileViewModel.logoutState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    val userData = when (profileState) {
        is ResultResponse.Success -> (profileState as ResultResponse.Success<Detail?>).data
        else -> null
    }

    LaunchedEffect(logoutState) {
        when(logoutState) {
            is ResultResponse.Loading -> {
                logoutLoading = true
            }
            is ResultResponse.Success -> {
                logoutLoading = false
                profileViewModel.resetLogoutState()
                onNavigateOut(MainDestinations.WELCOME_ROUTE, true)
            }
            else -> {

            }
        }
    }

    Profile(
        modifier = modifier,
        logoutLoading = logoutLoading,
        onProfileDetailClick = onProfileDetailClick,
        userData = userData,
        onLogout = {
            profileViewModel.logout()
        }
    )
}

@Composable
private fun Profile(
    modifier: Modifier = Modifier,
    logoutLoading: Boolean,
    onProfileDetailClick: (String) -> Unit,
    userData: Detail?,
    onLogout: () -> Unit,
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

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
                    Header(
                        name = userData?.name ?: "No Name",
                        email = userData?.email ?: "No Email",
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    AccountList(
                        data = FakeCategory.accountItem,
                        onProfileDetailClick = onProfileDetailClick
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    GeneralList(
                        data = FakeCategory.generalItem,
                        onProfileDetailClick = onProfileDetailClick
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    CustomButton(
                        text = "Logout",
                        onClick = {
                            showConfirmDialog = true
                        }
                    )
                }
            }

            if (logoutLoading) {
                Dialog(
                    onDismissRequest = { /* block dismiss */ },
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false,
                        decorFitsSystemWindows = false
                    )
                ) {
                    val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
                    SideEffect {
                        dialogWindowProvider.window.setDimAmount(0f)
                        dialogWindowProvider.window.setBackgroundDrawable(
                            android.graphics.Color.TRANSPARENT.toDrawable()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f))
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { /* block clicks */ },
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = primaryLight)
                    }
                }
            }


            // inside your composable
            if (showConfirmDialog) {
                Dialog(
                    onDismissRequest = { showConfirmDialog = false },
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false,
                        decorFitsSystemWindows = false
                    )
                ) {
                    val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
                    SideEffect {
                        dialogWindowProvider.window.setDimAmount(0f)
                        dialogWindowProvider.window.setBackgroundDrawable(
                            android.graphics.Color.TRANSPARENT.toDrawable()
                        )
                    }
//
                    CustomPopUpDialog(
                        onDismiss = { showConfirmDialog = false },
                        isShowIcon = true,
                        isShowTitle = true,
                        isShowDescription = false,
                        isShowButton = true,
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.think),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.height(150.dp)
                            )
                        },
                        title = "Are you sure you want to Logout?",
                        buttons = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                OutlinedButton(
                                    onClick = { showConfirmDialog = false },
                                    border = BorderStroke(1.dp, primaryLight),
                                    shape = RoundedCornerShape(28.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 6.dp)
                                ) {
                                    Text("Cancel", color = primaryLight)
                                }
                                Button(
                                    onClick = {
                                        onLogout()
                                        showConfirmDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = primaryLight),
                                    shape = RoundedCornerShape(28.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 6.dp)
                                ) {
                                    Text("Confirm", color = Color.White)
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    name: String,
    email: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(70.dp)
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
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = name,
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = email,
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
    onProfileDetailClick: (String) -> Unit
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
//                    if (title != "Language") {
                    onProfileDetailClick(title)
//                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Navigate",
                tint = Color.Black,
                modifier = Modifier
                    .size(18.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 14.sp,
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
        HorizontalDivider(color = base40, thickness = 1.dp)
    }
}

@Composable
private fun AccountList(
    modifier: Modifier = Modifier,
    data: List<AccountList>,
    onProfileDetailClick: (String) -> Unit,
) {
    Column(

    ) {
        SectionText(
            title = "My Account",
            fontWeight = FontWeight.W700,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        data.forEach {
            MenuItem(
                icon = it.icon,
                id = TEMP_ID,
                title = it.name,
                onProfileDetailClick = onProfileDetailClick
            )
        }

    }
}

@Composable
private fun GeneralList(
    modifier: Modifier = Modifier,
    data: List<AccountList>,
    onProfileDetailClick: (String) -> Unit,
) {
    Column(

    ) {
        SectionText(
            title = "General",
            fontWeight = FontWeight.W700,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        data.forEach {
            MenuItem(
                icon = it.icon,
                id = TEMP_ID,
                title = it.name,
                onProfileDetailClick = onProfileDetailClick,
            )
        }

    }
}
