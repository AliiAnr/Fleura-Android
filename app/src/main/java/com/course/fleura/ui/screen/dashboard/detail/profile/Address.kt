package com.course.fleura.ui.screen.dashboard.detail.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.R
import com.course.fleura.ui.components.CustomButton
import com.course.fleura.ui.components.CustomTextInput
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.Profile
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base40
import com.course.fleura.ui.theme.base60
import com.course.fleura.ui.theme.errorLight
import com.course.fleura.ui.theme.secColor

@Composable
fun AddAdress(
    modifier
    : Modifier = Modifier,
) {

    AddAddress(
        modifier = modifier,
        onClick = { _, _ ->

        },
        id = 0
    )
}

@Composable
private fun AddAddress(
    modifier: Modifier = Modifier,
    onClick: (Long, String) -> Unit,
    id: Long,
) {

    var value = remember { mutableStateOf("") }
    //Jangan lupa setiap edit text memiliki value sendiri, jangan ditambahkan value yang sama pada setiap EDT
    FleuraSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        val focusManager = LocalFocusManager.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    // Hapus fokus jika area lain diklik
                    indication = null, // Tidak ada animasi klik
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
                    .background(base20),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "Address Detail",
                    showNavigationIcon = true,
                )

                // Konten utama menggunakan LazyColumn
                Box(
                    modifier = Modifier.weight(1f) // Membuat LazyColumn fleksibel
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            FirstContent(
                                value = value.value,
                                onChage = { value.value = it }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            SecondContent(
                                value = value.value,
                                onChage = { value.value = it }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            ThirdContent(
                                label = "Location",
                                onClick = { }
                            )
                        }
                    }
                }

                // Tombol berada di bagian bawah
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .height(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomButton(
                        text = "Save",
                        onClick = { }
                    )
                }
            }
        }
    }
}

@Composable
private fun FirstContent(
    modifier: Modifier = Modifier,
    value: String,
    onChage: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 14.dp, bottom = 8.dp)
    ) {
        EditItem(
            label = "Name",
            value = value,
            onChage = onChage
        )
        EditItem(
            label = "Phone Number",
            value = value,
            onChage = onChage
        )
    }
}

@Composable
private fun SecondContent(
    modifier: Modifier = Modifier,
    value: String,
    onChage: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 14.dp, bottom = 8.dp)
    ) {
        EditItem(
            label = "Province, City/District, Sub-district, Postal Code",
            value = value,
            onChage = onChage
        )
        EditItem(
            label = "Street Name, Building, House No.",
            value = value,
            onChage = onChage
        )
        EditItem(
            label = "Additional Detail",
            value = value,
            onChage = onChage
        )
    }
}

@Composable
private fun ThirdContent(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 14.dp, bottom = 20.dp)
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .border(
                    width = 1.dp,
                    color = base60,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { onClick() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.address_map),
                contentDescription = "Rating",
                tint = Color.Unspecified,
                modifier = Modifier.size(35.dp)
            )
            Text(
                text = "Tekan untuk memilih lokasi spesifik",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}


@Composable
private fun EditItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    placeHolder: String = "",
    isError: Boolean = false,
    errorMessage: String = "",
    onChage: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        CustomTextInput(
            value = value,
            onChange = onChage,
            placeholder = placeHolder,
            isError = isError,
            errorMessage = errorMessage,
        )
    }
}

@Composable
fun ListAddress(
    modifier: Modifier = Modifier,
    data: Profile
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(base20)

    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Address",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            items(data.address) {
                AddressItem(
                    data = it
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    //tambahkan clickable
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.add_circle),
                            contentDescription = "Navigate",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add another address",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600,
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun AddressItem(
    modifier: Modifier = Modifier,
    data: String
) {

    //tambahkan id dari address dan tambahkan clickable
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
        Divider(color = Color.LightGray, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                painter = painterResource(R.drawable.address_home),
                contentDescription = "Navigate",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.padding(vertical = 7.dp)
            ) {
                Text(
                    text = "Home",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                )
                Text(
                    text = data,
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }

        }
    }
}
