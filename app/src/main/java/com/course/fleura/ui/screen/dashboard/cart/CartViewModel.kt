package com.course.fleura.ui.screen.dashboard.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleura.data.model.remote.AddressItem
import com.course.fleura.data.model.remote.CartListResponse
import com.course.fleura.data.model.remote.CartOrderItems
import com.course.fleura.data.model.remote.CartOrderResponse
import com.course.fleura.data.model.remote.DataCartItem
import com.course.fleura.data.repository.CartRepository
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.common.getTotalPrice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import android.util.Log
import network.chaintech.kmp_date_time_picker.utils.now

class CartViewModel (
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _dataInitialized = MutableStateFlow(false)
    val dataInitialized: StateFlow<Boolean> = _dataInitialized

    private val _cartListState =
        MutableStateFlow<ResultResponse<CartListResponse>>(ResultResponse.None)
    val cartListState: StateFlow<ResultResponse<CartListResponse>> = _cartListState

    private val _orderState = MutableStateFlow<ResultResponse<CartOrderResponse>>(ResultResponse.None)
    val orderState : StateFlow<ResultResponse<CartOrderResponse>> = _orderState

    private val _selectedCartItem = MutableStateFlow<DataCartItem?>(null)
    val selectedCartItem: StateFlow<DataCartItem?> = _selectedCartItem.asStateFlow()

    private val _addressList = MutableStateFlow<List<AddressItem>>(emptyList())
    val addressList: StateFlow<List<AddressItem>> = _addressList.asStateFlow()

    // Add to CartViewModel
    private val _totalPayment = MutableStateFlow(0L)
    val totalPayment: StateFlow<Long> = _totalPayment

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _selectedTime = MutableStateFlow(LocalTime.now())
    val selectedTime: StateFlow<LocalTime> = _selectedTime

    private val _paymentMethod = MutableStateFlow(PaymentMethod.QRIS)
    val paymentMethod: StateFlow<PaymentMethod> = _paymentMethod.asStateFlow()

    // State to track selected delivery method
    private val _orderNote = MutableStateFlow("")
    val orderNote: StateFlow<String> = _orderNote.asStateFlow()

    private val _deliveryMethod = MutableStateFlow(DeliveryMethod.PICKUP)
    val deliveryMethod: StateFlow<DeliveryMethod> = _deliveryMethod.asStateFlow()

    private val _selectedCartAddress = MutableStateFlow<AddressItem?>(null)
    val selectedCartAddress: StateFlow<AddressItem?> = _selectedCartAddress.asStateFlow()


    fun setOrderState(state: ResultResponse<CartOrderResponse>) {
        _orderState.value = state
    }

    // Method to update date
    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    // Method to update time
    fun setSelectedTime(time: LocalTime) {
        _selectedTime.value = time
    }

    fun getFormattedDateTime(): String {
        val date = _selectedDate.value
        val time = _selectedTime.value

        // Format: "yyyy-MM-dd HH:mm:ss.SSS"
        return "${date.year}-${date.monthNumber.toString().padStart(2, '0')}-${date.dayOfMonth.toString().padStart(2, '0')} " +
                "${time.hour.toString().padStart(2, '0')}:${time.minute.toString().padStart(2, '0')}:00.000"
    }

    fun calculateTotalPayment(selectedCartItem: DataCartItem) {
        val subtotal = selectedCartItem.getTotalPrice()
        val deliveryFee = if (_deliveryMethod.value == DeliveryMethod.DELIVERY) 15000 else 0
        _totalPayment.value = (subtotal + deliveryFee).toLong()
    }

    fun setOrderNote(note: String) {
        _orderNote.value = note
    }

    fun getOrderNote(): String {
        return _orderNote.value
    }

    fun setSelectedCartAddress(address: AddressItem) {
        _selectedCartAddress.value = address
    }

    fun getSelectedCartAddress(): AddressItem? {
        return _selectedCartAddress.value
    }

    fun setAddressList(addressList: List<AddressItem>){
        _addressList.value = addressList
    }

    fun getAddressList(): List<AddressItem> {
        return _addressList.value
    }

    // Set delivery method using string (for backward compatibility)
    fun setSelectedDeliveryMethod(methodString: String) {
        _deliveryMethod.value = DeliveryMethod.fromString(methodString)
    }

    // Get the display name for the current delivery method
    fun getSelectedDeliveryMethod(): String {
        return _deliveryMethod.value.displayName
    }

    fun setSelectedPaymentMethod(method: PaymentMethod) {
        _paymentMethod.value = method
    }

    // Alternative string-based setter for backward compatibility
    fun setSelectedPaymentMethod(methodString: String) {
        _paymentMethod.value = PaymentMethod.fromString(methodString)
    }

    // Get the display name for the current payment method
    fun getSelectedPaymentMethod(): String {
        return _paymentMethod.value.displayName
    }


    fun loadInitialData() {
//        if (!_dataInitialized.value) {
            getCartList()
//            _dataInitialized.value = true
//        }
    }

    fun setSelectedCartItem(cartItem: DataCartItem) {
        _selectedCartItem.value = cartItem
    }


    private fun getCartList() {
        viewModelScope.launch {
            try {
                _cartListState.value = ResultResponse.Loading
                cartRepository.getCartList()
                    .collect { result ->
                        _cartListState.value = result
                    }
            } catch (e: Exception) {
                _cartListState.value = ResultResponse.Error("Failed to get Cart List: ${e.message}")
            }
        }
    }

//    fun createOrder() {
//        viewModelScope.launch {
//            try {
//                _orderState.value = ResultResponse.Loading
//
//                // Get the selected cart items
//                val cartItems = getSelectedCartItem()
//                if (cartItems.isEmpty()) {
//                    _orderState.value = ResultResponse.Error("No items selected for order")
//                    return@launch
//                }
//
//                // Convert delivery method and payment method to lowercase for API
//                val takenMethodLowercase = _deliveryMethod.value.displayName.lowercase()
//                val paymentMethodLowercase = _paymentMethod.value.displayName.lowercase()
//val selectedAddress = getSelectedCartAddress()
//    if (selectedAddress == null) {
//        Log.e("CartViewModel", "Error: No address selected")
//        _orderState.value = ResultResponse.Error("No address selected")
//        return@launch
//    }
//
//                getSelectedCartAddress()?.let {
//                    cartRepository.createOrder(
//                        orderItems = cartItems,
//                        takenMethod = takenMethodLowercase,
//                        paymentMethod = paymentMethodLowercase,
//                        addressId = it.id,
//                        note = getOrderNote(),
//                        takenDate = getFormattedDateTime()
//                    ).collect { result ->
//                        _orderState.value = result
//                    }
//                }
//            } catch (e: Exception) {
//                _orderState.value = ResultResponse.Error("Failed to create order: ${e.message}")
//            }
//        }
//    }

    fun createOrder() {
        viewModelScope.launch {
            try {
                _orderState.value = ResultResponse.Loading

                // Get the selected cart items
                val cartItems = getSelectedCartItem()

                // Debug log cart items
                Log.d("CartViewModel", "Cart Items: ${cartItems.size} items")
                cartItems.forEachIndexed { index, item ->
                    Log.d("CartViewModel", "Item $index: productId=${item.productId}, quantity=${item.quantity}")
                }

                if (cartItems.isEmpty()) {
                    Log.e("CartViewModel", "Error: No items selected for order")
                    _orderState.value = ResultResponse.Error("No items selected for order")
                    return@launch
                }

                // Convert delivery method and payment method to lowercase for API
                val takenMethodLowercase = _deliveryMethod.value.displayName.lowercase()
                val paymentMethodLowercase = _paymentMethod.value.displayName.lowercase()

                // Debug log delivery and payment methods
                Log.d("CartViewModel", "Delivery Method: ${_deliveryMethod.value} ($takenMethodLowercase)")
                Log.d("CartViewModel", "Payment Method: ${_paymentMethod.value} ($paymentMethodLowercase)")

                // Debug log date and time
                Log.d("CartViewModel", "Selected Date: ${_selectedDate.value}")
                Log.d("CartViewModel", "Selected Time: ${_selectedTime.value}")
                Log.d("CartViewModel", "Formatted DateTime: ${getFormattedDateTime()}")

                // Debug log order note
                Log.d("CartViewModel", "Order Note: '${getOrderNote()}'")

                val selectedAddress = getSelectedCartAddress()
                if (selectedAddress == null) {
                    Log.e("CartViewModel", "Error: No address selected")
                    _orderState.value = ResultResponse.Error("No address selected")
                    return@launch
                }

                // Debug log address
                Log.d("CartViewModel", "Selected Address: id=${selectedAddress.id}, name=${selectedAddress.name}")

                // All data looks good, proceed with order creation
                Log.d("CartViewModel", "Creating order with repository...")

                cartRepository.createOrder(
                    orderItems = cartItems,
                    takenMethod = takenMethodLowercase,
                    paymentMethod = paymentMethodLowercase,
                    addressId = selectedAddress.id,
                    note = getOrderNote(),
                    takenDate = getFormattedDateTime()
                ).collect { result ->
                    when (result) {
                        is ResultResponse.Success -> Log.d("CartViewModel", "Order created successfully: ${result.data}")
                        is ResultResponse.Error -> Log.e("CartViewModel", "Order creation failed: ${result.error}")
                        is ResultResponse.Loading -> Log.d("CartViewModel", "Order creation in progress...")
                        else -> Log.d("CartViewModel", "Unknown result state")
                    }
                    _orderState.value = result
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Exception during order creation: ${e.message}", e)
                _orderState.value = ResultResponse.Error("Failed to create order: ${e.message}")
            }
        }
    }

    private fun getSelectedCartItem(): List<CartOrderItems> {
        val listOrderItem: MutableList<CartOrderItems> = mutableListOf()
        selectedCartItem.value?.items?.forEach { cartItem ->
            listOrderItem.add(
                CartOrderItems(
                    quantity = cartItem.quantity,
                    productId = cartItem.product.id
                )
            )
        }
        return listOrderItem
    }

    fun refreshData() {
       getCartList()
    }

}

enum class PaymentMethod(val displayName: String) {
    QRIS("Qris"),
    CASH("Cash"),
    POINT("Point");

    companion object {
        fun fromString(value: String): PaymentMethod {
            return when (value.lowercase()) {
                "qris" -> QRIS
                "cash" -> CASH
                "point" -> POINT
                else -> QRIS // Default to QRIS
            }
        }
    }
}

enum class DeliveryMethod(val displayName: String) {
    PICKUP("Pickup"),
    DELIVERY("Delivery");

    companion object {
        fun fromString(value: String): DeliveryMethod {
            return when (value.lowercase()) {
                "pickup" -> PICKUP
                "delivery" -> DELIVERY
                else -> PICKUP // Default to PICKUP
            }
        }
    }
}

