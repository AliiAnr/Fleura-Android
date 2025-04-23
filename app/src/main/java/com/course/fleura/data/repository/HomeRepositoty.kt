package com.course.fleura.data.repository

import android.content.Context
import android.util.Log
import com.course.fleura.data.model.remote.Detail
import com.course.fleura.data.model.remote.ListProductResponse
import com.course.fleura.data.model.remote.ListStoreResponse
import com.course.fleura.data.model.remote.ProductReviewResponse
import com.course.fleura.data.model.remote.SaveProductToCartRequest
import com.course.fleura.data.model.remote.SaveProductToCartResponse
import com.course.fleura.data.model.remote.StoreProductResponse
import com.course.fleura.data.store.DataStoreManager
import com.course.fleura.retrofit.api.ApiConfig
import com.course.fleura.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HomeRepository private constructor(
    context: Context
) {
    private val dataStoreManager = DataStoreManager(context)

    private val homeService = ApiConfig.getHomeService(context)

    fun getUserDetail(): Flow<ResultResponse<Detail?>> = flow {
        emit(ResultResponse.Loading)

        try {
            val detail = dataStoreManager.getUserDetail()
            if (detail != null) {
                Log.e("DETAIL USER", detail.toString())
                emit(ResultResponse.Success(detail))
            } else {
                emit(ResultResponse.Error("User profile not found"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Error retrieving user details"))
        }
    }.flowOn(Dispatchers.IO)

    fun getAllStore(): Flow<ResultResponse<ListStoreResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = homeService.getListStore()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)

    fun getAllProdcut(): Flow<ResultResponse<StoreProductResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = homeService.getProductList()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)

    fun getProductReview(productId: String): Flow<ResultResponse<ProductReviewResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = homeService.getProductReview(productId = productId)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Error retrieving user details"))
        }
    }.flowOn(Dispatchers.IO)

    fun saveProductToCart(quantity: Int, productId: String): Flow<ResultResponse<SaveProductToCartResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val userDetail = dataStoreManager.getUserDetail()
            val buyerId = userDetail?.id ?: run {
                emit(ResultResponse.Error("User ID not found. Please login again."))
                return@flow
            }

            val cartRequest = SaveProductToCartRequest(productId = productId, quantity = quantity)

            val response = homeService.saveProductToCart(buyerId = buyerId, item = cartRequest)

            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Error retrieving user details"))
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        @Volatile
        private var INSTANCE: HomeRepository? = null

        fun getInstance(context: Context): HomeRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeRepository(context).also { INSTANCE = it }
            }
    }
}