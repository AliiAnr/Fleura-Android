package com.course.fleura.data.repository

import android.content.Context
import android.util.Log
import com.course.fleura.data.model.remote.CartListResponse
import com.course.fleura.data.model.remote.CartOrderItems
import com.course.fleura.data.model.remote.CartOrderRequest
import com.course.fleura.data.model.remote.CartOrderResponse
import com.course.fleura.data.model.remote.QrisPaymentResponse
import com.course.fleura.data.store.DataStoreManager
import com.course.fleura.retrofit.api.ApiConfig
import com.course.fleura.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CartRepository private constructor(
    context: Context
) {
    private val cartService = ApiConfig.getCartService(context)
    private val dataStoreManager = DataStoreManager(context)


    fun getCartList(): Flow<ResultResponse<CartListResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val userDetail = dataStoreManager.getUserDetail()

            val buyerId = userDetail?.id ?: run {
                emit(ResultResponse.Error("User ID not found. Please login again."))
                return@flow
            }

            val response = cartService.getCartList(buyerId = buyerId)
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

    fun createOrder(
        orderItems: List<CartOrderItems> = listOf(),
        takenMethod: String = "",
        paymentMethod: String = "",
        addressId: String = "",
        note: String = "",
        takenDate: String = ""
    ): Flow<ResultResponse<CartOrderResponse>> = flow {
        emit(ResultResponse.Loading)
        try {
            val response = cartService.createOrder(
                request = CartOrderRequest(
                    items = orderItems,
                    takenMethod = takenMethod,
                    paymentMethod = paymentMethod,
                    addressId = addressId,
                    note = note,
                    takenDate = takenDate
                )
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Order creation failed, response body is empty"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)

    fun getQrisPaymentUrl(
        orderId: String
    ): Flow<ResultResponse<QrisPaymentResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = cartService.getQrisPayment(orderId)
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

    // Add this to your CartRepository implementation
//    fun createOrder(
//        orderItems: List<CartOrderItems>,
//        takenMethod: String,
//        paymentMethod: String,
//        addressId: String,
//        note: String,
//        takenDate: String
//    ): Flow<ResultResponse<CartOrderResponse>> = flow {
//        try {
//            // Debug logging
//            Log.d("CartRepository", "===== CREATE ORDER DEBUG =====")
//            Log.d("CartRepository", "Items count: ${orderItems.size}")
//            orderItems.forEachIndexed { index, item ->
//                Log.d("CartRepository", "Item $index: productId=${item.productId}, quantity=${item.quantity}")
//            }
//            Log.d("CartRepository", "takenMethod: $takenMethod")
//            Log.d("CartRepository", "paymentMethod: $paymentMethod")
//            Log.d("CartRepository", "addressId: $addressId")
//            Log.d("CartRepository", "note: $note")
//            Log.d("CartRepository", "takenDate: $takenDate")
//
//            // Create request object for API
//            val request = CartOrderRequest(
//                note = note,
//                takenDate = takenDate,
//                takenMethod = takenMethod,
//                items = orderItems,
//                paymentMethod = paymentMethod,
//                addressId = addressId
//            )
//
//            // Debug the request object
//            Log.d("CartRepository", "Request: $request")
//
//            emit(ResultResponse.Loading)
//
//            // Make API call
//            Log.d("CartRepository", "Making API call...")
////            val response = apiService.createOrder(request)
////
////            // Debug response
////            Log.d("CartRepository", "API Response: $response")
////            Log.d("CartRepository", "Status code: ${response.statusCode}")
////            Log.d("CartRepository", "Message: ${response.message}")
////
////            if (response.statusCode == 200) {
////                Log.d("CartRepository", "Success! Order ID: ${response.data.orderId}")
////                emit(ResultResponse.Success(response.data))
////            } else {
////                Log.e("CartRepository", "Error from API: ${response.message}")
////                emit(ResultResponse.Error(response.message))
////            }
//        } catch (e: Exception) {
//            Log.e("CartRepository", "Exception during order creation", e)
//            Log.e("CartRepository", "Error message: ${e.message}")
//            Log.e("CartRepository", "Error cause: ${e.cause}")
//            Log.e("CartRepository", "Stack trace: ${e.stackTraceToString()}")
//            emit(ResultResponse.Error("Failed to create order: ${e.message}"))
//        }
//    }

    companion object {
        @Volatile
        private var INSTANCE: CartRepository? = null

        fun getInstance(context: Context): CartRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: CartRepository(context).also { INSTANCE = it }
            }
    }
}