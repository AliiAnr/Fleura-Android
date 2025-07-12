package com.course.fleura.data.repository

import android.content.Context
import com.course.fleura.data.model.remote.CartListResponse
import com.course.fleura.data.model.remote.OrderAddressResponse
import com.course.fleura.data.model.remote.OrderListResponse
import com.course.fleura.retrofit.api.ApiConfig
import com.course.fleura.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class OrderRepository private constructor(
    context: Context
) {

//    private val dataStoreManager = DataStoreManager(context)

    private val orderService = ApiConfig.getOrderService(context)

    fun getOrderList(): Flow<ResultResponse<OrderListResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = orderService.getBuyerOrders()
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

    fun getOrderBuyerAddress(
        addressId: String
    ): Flow<ResultResponse<OrderAddressResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = orderService.getBuyerAddress(addressId = addressId)
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

    companion object {
        @Volatile
        private var INSTANCE: OrderRepository? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): OrderRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: OrderRepository(context).also {
                    INSTANCE = it
                }
            }
    }
}