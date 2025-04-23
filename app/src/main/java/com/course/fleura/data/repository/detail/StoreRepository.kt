package com.course.fleura.data.repository.detail

import android.content.Context
import android.util.Log
import com.course.fleura.data.model.remote.Detail
import com.course.fleura.data.model.remote.DetailStoreResponse
import com.course.fleura.data.model.remote.ProductReviewResponse
import com.course.fleura.data.model.remote.StoreProductResponse
import com.course.fleura.data.repository.HomeRepository
import com.course.fleura.retrofit.api.ApiConfig
import com.course.fleura.ui.common.ResultResponse
import com.course.fleura.ui.components.Store
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class StoreRepository private constructor(
    context: Context
) {

    private val homeService = ApiConfig.getHomeService(context)

    fun getStoreDetail(storeId: String): Flow<ResultResponse<DetailStoreResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = homeService.getDetailStore(storeId = storeId)
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

    fun getAllStoreProduct(storeId: String): Flow<ResultResponse<StoreProductResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = homeService.getAllStoreProduct(storeId = storeId)
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
        private var INSTANCE: StoreRepository? = null

        fun getInstance(context: Context): StoreRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: StoreRepository(context).also { INSTANCE = it }
            }
    }
}