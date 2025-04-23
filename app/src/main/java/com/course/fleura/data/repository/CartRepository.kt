package com.course.fleura.data.repository

import android.content.Context
import com.course.fleura.data.model.remote.CartListResponse
import com.course.fleura.data.model.remote.ListStoreResponse
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

            val response = cartService.getDetailStore(buyerId = buyerId)
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
        private var INSTANCE: CartRepository? = null

        fun getInstance(context: Context): CartRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: CartRepository(context).also { INSTANCE = it }
            }
    }
}