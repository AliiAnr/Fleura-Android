package com.course.fleura.ui.screen.dashboard.detail.order

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.R
import com.course.fleura.data.model.remote.AddressItem
import com.course.fleura.ui.components.CustomButton
import com.course.fleura.ui.components.CustomTopAppBar
import com.course.fleura.ui.components.FakeCategory
import com.course.fleura.ui.screen.dashboard.cart.CartViewModel
import com.course.fleura.ui.screen.dashboard.profile.ProfileViewModel
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base20
import com.course.fleura.ui.theme.base40

@Composable
fun GeneralOrder(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    cartViewModel: CartViewModel,
    location: String
) {

    //call API here

    cartViewModel.getSelectedCartAddress()?.let {
        GeneralOrder(
            data = "httpscomdkajhdkj",
            onBackClick = onBackClick,
            location = location,
            selectedPaymentMethod = cartViewModel.getSelectedPaymentMethod(),
            selectedAddress = it,
            addressList = cartViewModel.getAddressList(),
            onPaymentMethodSelected = { selectedPaymentMethod ->
                cartViewModel.setSelectedPaymentMethod(selectedPaymentMethod)
            },
            onAddressSelected = { selectedAddress ->
                cartViewModel.setSelectedCartAddress(selectedAddress)
            }
        )
    }
}

@Composable
private fun GeneralOrder(
    modifier: Modifier = Modifier,
    data: String,
    onBackClick: () -> Unit,
    selectedPaymentMethod: String,
    selectedAddress: AddressItem,
    addressList: List<AddressItem>,
    location: String,
    onPaymentMethodSelected: (String) -> Unit,
    onAddressSelected: (AddressItem) -> Unit
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
                        "Payment Method" -> {
                            Spacer(modifier = Modifier.height(8.dp))
                            PaymentMethod(
                                selectedMethod = selectedPaymentMethod,
                                onMethodSelected = {
                                    onPaymentMethodSelected(it)
                                },
                                onBackClick = onBackClick
                            )
                        }

                        "Payment Process" -> {
                            PaymentProcess(
                                status = "UNPAID",
                                image = R.drawable.qr_image
                            )
                        }

                        "Select Address" -> {
                            ListSelectAddress(
                                addressList = addressList,
                                onAddressSelected = onAddressSelected,
                                selectedAddress = selectedAddress,
                                onBackClick = onBackClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentMethod(
    modifier: Modifier = Modifier,
    selectedMethod: String,
    onMethodSelected: (String) -> Unit,
    onBackClick: () -> Unit
) {

    var selectedPaymentMethod by remember { mutableStateOf(selectedMethod) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(base20)

    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(FakeCategory.paymentMethods) { index, item ->
                    val isLastItem = index == FakeCategory.paymentMethods.size - 1
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 20.dp)
                    ) {
                        PaymentItem(
                            icon = item.icon,
                            title = item.title,
                            isSelected = item.title == selectedPaymentMethod,
                            onClick = {
                                selectedPaymentMethod = it
                            }
                        )

                        if (!isLastItem) {
                            HorizontalDivider(
                                color = base40,
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .navigationBarsPadding()
                .height(90.dp),
            contentAlignment = Alignment.Center
        ) {
            CustomButton(
                text = "Save",
                onClick = {
                    onMethodSelected(selectedPaymentMethod)
                    onBackClick()
                }
            )
        }
    }
}

@Composable
private fun PaymentItem(
    modifier: Modifier = Modifier,
    icon: Int,
    title: String,
    isSelected: Boolean,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable(
                onClick = { onClick(title) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = Color(0xFFFF4081),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500
            )
        }
        if (isSelected) {
            Icon(
                painter = painterResource(id = R.drawable.check),
                contentDescription = "Check",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun PaymentProcess(
    modifier: Modifier = Modifier,
    status: String,
    image: Int
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(base20)
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Payment Process",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = status,
                            color = Color.Red,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W600,
                        )
                        Image(
                            painter = painterResource(id = image),
                            contentDescription = "Payment Process",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .navigationBarsPadding()
                .height(90.dp),
            contentAlignment = Alignment.Center
        ) {
            CustomButton(
                text = "Download QR Code",
                onClick = { }
            )
        }
    }
}

@Composable
fun ListSelectAddress(
    modifier: Modifier = Modifier,
    addressList: List<AddressItem>,
    onAddressSelected: (AddressItem) -> Unit,
    selectedAddress: AddressItem,
    onBackClick: () -> Unit
) {

    var address by remember { mutableStateOf(selectedAddress) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(base20)

    ) {
        Box(
            modifier = Modifier.weight(1f)
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W700,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                    )
                }

                items(addressList) {
                    SelectAddressListItem(
                        addressData = it,
                        onAddressClick = { newAddress ->
                            address = newAddress
                        },
                        isSelected = it.id == address.id
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .navigationBarsPadding()
                .height(90.dp),
            contentAlignment = Alignment.Center
        ) {
            CustomButton(
                text = "Save",
                onClick = {
                    onAddressSelected(address)
                    onBackClick()
                }
            )
        }

    }
}

@Composable
private fun SelectAddressListItem(
    modifier: Modifier = Modifier,
    addressData: AddressItem,
    isSelected: Boolean,
    onAddressClick: (AddressItem) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth() // Pastikan Row berada dalam kotak selebar layar
            .clickable(
                onClick = { onAddressClick(addressData) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Garis pemisah atas
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            // ICON RUMAH/PIN
            Icon(
                painter = painterResource(R.drawable.address_home),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // DATA TEKS: NAMA, TELEPON, DAN ALAMAT
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = addressData.name,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = addressData.phone,
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildString {
                        append(addressData.detail)
                        if (addressData.detail.isNotEmpty()) append(", ")
                        append(addressData.road).append(", ")
                        append(addressData.district).append(", ")
                        append(addressData.city).append(", ")
                        append(addressData.province).append(" ")
                        append(addressData.postcode)
                    },
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            // Spacer dengan weight di sini berguna mendorong ikon "check" ke kanan
            Spacer(modifier = Modifier.width(8.dp))

            // ICON CHECK (yang hanya muncul jika isSelected = true)
            if (isSelected) {
                Icon(
                    painter = painterResource(id = R.drawable.check),
                    contentDescription = "Checked",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Top) // sesuaikan alignment jika perlu
                )
            } else {
                // Bila tidak terpilih, kosongkan ruang sebesar ikon (agar layout tidak lompat)
                Spacer(modifier = Modifier.size(20.dp))
            }
        }
    }
}

