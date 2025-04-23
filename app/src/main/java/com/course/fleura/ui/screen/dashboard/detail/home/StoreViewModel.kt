package com.course.fleura.ui.screen.dashboard.detail.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleura.data.model.remote.Detail
import com.course.fleura.data.model.remote.DetailStoreResponse
import com.course.fleura.data.model.remote.ProductReviewResponse
import com.course.fleura.data.model.remote.StoreProductResponse
import com.course.fleura.data.repository.detail.StoreRepository
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.components.Store
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoreViewModel(
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _storeDetailState =
        MutableStateFlow<ResultResponse<DetailStoreResponse>>(ResultResponse.None)
    val storeDetailState: StateFlow<ResultResponse<DetailStoreResponse>> = _storeDetailState

    private val _storeProductState =
        MutableStateFlow<ResultResponse<StoreProductResponse>>(ResultResponse.None)
    val storeProductState: StateFlow<ResultResponse<StoreProductResponse>> = _storeProductState

    fun getStoreDetail(storeId: String) {
        viewModelScope.launch {
            try {
                _storeDetailState.value = ResultResponse.Loading
                storeRepository.getStoreDetail(storeId = storeId)
                    .collect { result ->
                        _storeDetailState.value = result
                    }
            } catch (e: Exception) {
                _storeDetailState.value = ResultResponse.Error("Failed to get user: ${e.message}")
            }
        }
    }

    fun getAllStoreProduct(storeId: String) {
        viewModelScope.launch {
            try {
                _storeProductState.value = ResultResponse.Loading
                storeRepository.getAllStoreProduct(storeId = storeId)
                    .collect { result ->
                        _storeProductState.value = result
                    }
            } catch (e: Exception) {
                _storeProductState.value = ResultResponse.Error("Failed to get user: ${e.message}")
            }
        }
    }



}