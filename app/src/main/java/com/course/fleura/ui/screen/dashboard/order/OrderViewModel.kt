package com.course.fleura.ui.screen.dashboard.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleura.data.model.remote.OrderAddressResponse
import com.course.fleura.data.model.remote.OrderDataItem
import com.course.fleura.data.model.remote.OrderListResponse
import com.course.fleura.data.repository.OrderRepository
import com.course.fleura.ui.common.ResultResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel (
    private val orderRepository: OrderRepository
) : ViewModel() {

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