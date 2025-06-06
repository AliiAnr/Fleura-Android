package com.course.fleura.ui.screen.dashboard.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleura.data.model.remote.Detail
import com.course.fleura.data.model.remote.ListProductResponse
import com.course.fleura.data.model.remote.ListStoreResponse
import com.course.fleura.data.model.remote.ProductReviewResponse
import com.course.fleura.data.model.remote.SaveProductToCartResponse
import com.course.fleura.data.model.remote.StoreProduct
import com.course.fleura.data.model.remote.StoreProductResponse
import com.course.fleura.data.repository.HomeRepository
import com.course.fleura.data.repository.ProfileRepository
import com.course.fleura.ui.common.ResultResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _dataInitialized = MutableStateFlow(false)
    val dataInitialized: StateFlow<Boolean> = _dataInitialized

    private val _userDetailState =
        MutableStateFlow<ResultResponse<Detail?>>(ResultResponse.None)
    val userDetailState: StateFlow<ResultResponse<Detail?>> = _userDetailState

    private val _storeListState =
        MutableStateFlow<ResultResponse<ListStoreResponse>>(ResultResponse.None)
    val storeListState: StateFlow<ResultResponse<ListStoreResponse>> = _storeListState

    private val _selectedProduct = MutableStateFlow<StoreProduct?>(null)
    val selectedProduct: StateFlow<StoreProduct?> = _selectedProduct.asStateFlow()

    private val _productReviewState =
        MutableStateFlow<ResultResponse<ProductReviewResponse>>(ResultResponse.None)
    val productReviewState: StateFlow<ResultResponse<ProductReviewResponse>> = _productReviewState

   private val _productListState =
        MutableStateFlow<ResultResponse<StoreProductResponse>>(ResultResponse.None)
    val productListState: StateFlow<ResultResponse<StoreProductResponse>> = _productListState

    private val _saveProductState =
        MutableStateFlow<ResultResponse<SaveProductToCartResponse>>(ResultResponse.None)
    val saveProductState: StateFlow<ResultResponse<SaveProductToCartResponse>> = _saveProductState


    private var productQuantity by mutableIntStateOf(0)
        private set

    fun setQuantity(value: Int) {
        productQuantity = value
    }


//    init {
//        getUserDetail()
//        getListStore()
//        Log.e("MyViewModel", "Fungsi doSomething dijalankan")
//        println("Fungsi doSomething dijalankan")
//    }

    fun setSelectedProduct(storeProduct: StoreProduct) {
        _selectedProduct.value = storeProduct
    }

    fun loadInitialData() {
        if (!_dataInitialized.value) {
            getUserDetail()
            getListStore()
            getListProduct()
            _dataInitialized.value = true
        }
    }

    fun getUserDetail() {
        viewModelScope.launch {
            try {
                _userDetailState.value = ResultResponse.Loading
                homeRepository.getUserDetail()
                    .collect { result ->
                        _userDetailState.value = result
                    }
            } catch (e: Exception) {
                _userDetailState.value =
                    ResultResponse.Error("Failed to generate OTP: ${e.message}")
            }
        }
    }

    fun getListStore() {
        viewModelScope.launch {
            try {
                _storeListState.value = ResultResponse.Loading
                homeRepository.getAllStore()
                    .collect { result ->
                        _storeListState.value = result
                    }
            } catch (e: Exception) {
                _storeListState.value =
                    ResultResponse.Error("Failed to generate OTP: ${e.message}")
            }
        }
    }

    fun getListProduct() {
        viewModelScope.launch {
            try {
                _productListState.value = ResultResponse.Loading
                homeRepository.getAllProdcut()
                    .collect { result ->
                        _productListState.value = result
                    }
            } catch (e: Exception) {
                _productListState.value =
                    ResultResponse.Error("Failed to generate OTP: ${e.message}")
            }
        }
    }


    fun getProductReview(productId: String) {
        viewModelScope.launch {
            try {
                _productReviewState.value = ResultResponse.Loading
                homeRepository.getProductReview(productId = productId)
                    .collect { result ->
                        _productReviewState.value = result
                    }
            } catch (e: Exception) {
                _productReviewState.value = ResultResponse.Error("Failed to get user: ${e.message}")
            }
        }
    }

    fun saveProductToCart(productId: String) {
        viewModelScope.launch {
            try {
                _saveProductState.value = ResultResponse.Loading
                homeRepository.saveProductToCart(quantity = productQuantity, productId = productId)
                    .collect { result ->
                        _saveProductState.value = result
                    }
            } catch (e: Exception) {
                _saveProductState.value = ResultResponse.Error("Failed to get user: ${e.message}")
            }
        }
    }

    fun refreshData() {
        // Force refresh data regardless of initialization state
        getUserDetail()
        getListStore()
        getListProduct()
    }
}