package com.course.fleura.data.repository

import android.content.Context
import com.course.fleura.data.model.remote.NotificationRequest
import com.course.fleura.data.model.remote.NotificationResponse
import com.course.fleura.data.store.DataStoreManager
import com.course.fleura.retrofit.api.ApiConfig
import com.course.fleura.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NotificationRepository private constructor(
    context: Context
) {

    private val notificationService = ApiConfig.getNotificationService(context)
    private val dataStoreManager = DataStoreManager(context)


    fun saveToken(token: String): Flow<ResultResponse<NotificationResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            if (!dataStoreManager.isUserLoggedIn.first()) {
                emit(ResultResponse.Error("User not logged in"))
                return@flow
            }

            val userToken = NotificationRequest(
                token = token,
            )

            val response = notificationService.saveToken(token = userToken)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Error saving token"))
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        @Volatile
        private var INSTANCE: NotificationRepository? = null
        fun getInstance(context: Context): NotificationRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NotificationRepository(context).also { INSTANCE = it }
            }
    }
}
