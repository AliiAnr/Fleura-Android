package com.course.fleura.ui.screen.dashboard.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleura.data.model.remote.CartListResponse
import com.course.fleura.data.model.remote.StoreProductResponse
import com.course.fleura.data.repository.CartRepository
import com.course.fleura.data.repository.detail.StoreRepository
import com.course.fleura.ui.common.ResultResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel (
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartListState =
        MutableStateFlow<ResultResponse<CartListResponse>>(ResultResponse.None)
    val cartListState: StateFlow<ResultResponse<CartListResponse>> = _cartListState

    fun getCartList(buyerId: String) {
        viewModelScope.launch {
            try {
                _cartListState.value = ResultResponse.Loading
                cartRepository.getCartList()
                    .collect { result ->
                        _cartListState.value = result
                    }
            } catch (e: Exception) {
                _cartListState.value = ResultResponse.Error("Failed to get user: ${e.message}")
            }
        }
    }

}