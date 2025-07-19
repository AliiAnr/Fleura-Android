package com.course.fleura.ui.screen.dashboard.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleura.BuildConfig
import com.course.fleura.data.model.remote.OrderAddressResponse
import com.course.fleura.data.model.remote.OrderDataItem
import com.course.fleura.data.model.remote.OrderListResponse
import com.course.fleura.data.repository.OrderRepository
import com.course.fleura.socket.WebSocketManager
import com.course.fleura.ui.common.ResultResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel (
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val webSocketManager = WebSocketManager.getInstance()
    private val socketUrl = BuildConfig.SOCKET_URL

    private val _realtimeOrderStatus = MutableStateFlow<String?>(null)
    val realtimeOrderStatus: StateFlow<String?> = _realtimeOrderStatus.asStateFlow()

    private val _realtimePaymentStatus = MutableStateFlow<String?>(null)
    val realtimePaymentStatus: StateFlow<String?> = _realtimePaymentStatus.asStateFlow()


    private val _dataInitialized = MutableStateFlow(false)
    val dataInitialized: StateFlow<Boolean> = _dataInitialized

    private val _orderListState =
        MutableStateFlow<ResultResponse<OrderListResponse>>(ResultResponse.None)
    val orderListState: StateFlow<ResultResponse<OrderListResponse>> = _orderListState

    private val _orderAddressState =
        MutableStateFlow<ResultResponse<OrderAddressResponse>>(ResultResponse.None)
    val orderAddressState: StateFlow<ResultResponse<OrderAddressResponse>> = _orderAddressState

    private val _selectedOrderItem = MutableStateFlow<OrderDataItem?>(null)
    val selectedOrderItem: StateFlow<OrderDataItem?> = _selectedOrderItem.asStateFlow()

    private val _selectedCreatedOrderItem = MutableStateFlow<OrderDataItem?>(null)
    val selectedCreatedOrderItem: StateFlow<OrderDataItem?> = _selectedCreatedOrderItem.asStateFlow()

    fun setSelectedOrderItem(orderItem: OrderDataItem) {
        _selectedOrderItem.value = orderItem
    }

    fun setSelectedCreatedOrderItem(orderItem: OrderDataItem) {
        _selectedCreatedOrderItem.value = orderItem
    }

    fun startWebSocketConnection(orderId: String) {
        viewModelScope.launch {
            webSocketManager.connect(orderId, socketUrl)

            // Listen for order status updates
            webSocketManager.orderStatusUpdates.collect { update ->
                if (update.orderId == orderId) {
                    _realtimeOrderStatus.value = update.status
                    updateSelectedOrderStatus(update.status)
                }
            }
        }

        viewModelScope.launch {
            // Listen for payment status updates
            webSocketManager.paymentStatusUpdates.collect { update ->
                if (update.orderId == orderId) {
                    _realtimePaymentStatus.value = update.status
                    updateSelectedOrderPaymentStatus(update.status)
                }
            }
        }
    }
    private fun updateSelectedOrderPaymentStatus(status: String) {
        _selectedOrderItem.value?.let { currentOrder ->
            _selectedOrderItem.value = currentOrder.copy(
                payment = currentOrder.payment.copy(
                    status = status,
                    successAt = currentOrder.payment.successAt ?: "", // Ensure successAt is never null
                    qrisExpiredAt = currentOrder.payment.qrisExpiredAt ?: "", // Ensure this is also not null
                    qrisUrl = currentOrder.payment.qrisUrl ?: "", // Ensure this is also not null
                    methode = currentOrder.payment.methode ?: "" // Ensure this is also not null
                )
            )
        }
        _selectedCreatedOrderItem.value?.let { currentOrder ->
            _selectedCreatedOrderItem.value = currentOrder.copy(
                payment = currentOrder.payment.copy(
                    status = status,
                    successAt = currentOrder.payment.successAt ?: "",
                    qrisExpiredAt = currentOrder.payment.qrisExpiredAt ?: "",
                    qrisUrl = currentOrder.payment.qrisUrl ?: "",
                    methode = currentOrder.payment.methode ?: ""
                )
            )
        }
    }

    private fun updateSelectedOrderStatus(status: String) {
        _selectedOrderItem.value?.let { currentOrder ->
            _selectedOrderItem.value = currentOrder.copy(
                status = status,
                // Ensure other nullable fields are handled
                note = currentOrder.note ?: "",
                addressId = currentOrder.addressId ?: ""
            )
        }
        _selectedCreatedOrderItem.value?.let { currentOrder ->
            _selectedCreatedOrderItem.value = currentOrder.copy(
                status = status,
                note = currentOrder.note ?: "",
                addressId = currentOrder.addressId ?: ""
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopWebSocketConnection()
    }


    fun stopWebSocketConnection() {
        webSocketManager.disconnect()
        _realtimeOrderStatus.value = null
        _realtimePaymentStatus.value = null
    }

    fun loadInitialData(){
//        if (!_dataInitialized.value){
            getOrderList()
//            _dataInitialized.value = true
//        }
    }

    fun loadInitialOrderData(){
//        if (!_dataInitialized.value){
        getOrderBuyerAddress()
//            _dataInitialized.value = true
//        }
    }


    private fun getOrderList(){
        viewModelScope.launch {
            try {
                _orderListState.value = ResultResponse.Loading
                orderRepository.getOrderList()
                    .collect { result ->
                        _orderListState.value = result
                    }
            } catch (e: Exception) {
                _orderListState.value = ResultResponse.Error(e.localizedMessage ?: "Error fetching order list")
            }
        }
    }

    private fun getOrderBuyerAddress(){
        viewModelScope.launch {
            try {
                _orderAddressState.value = ResultResponse.Loading
                orderRepository.getOrderBuyerAddress(
                        addressId = _selectedOrderItem.value?.addressId ?: "Unknown"
                    )
                    .collect { result ->
                        _orderAddressState.value = result
                    }
            } catch (e: Exception) {
                _orderAddressState.value = ResultResponse.Error(e.localizedMessage ?: "Error fetching order list")
            }
        }
    }
}