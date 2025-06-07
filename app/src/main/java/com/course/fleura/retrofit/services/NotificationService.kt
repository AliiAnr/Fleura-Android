package com.course.fleura.retrofit.services

import com.course.fleura.data.model.remote.LoginResponse
import com.course.fleura.data.model.remote.NotificationRequest
import com.course.fleura.data.model.remote.NotificationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationService {

    @POST("api/notification/buyer/save-token")
    suspend fun saveToken(
        @Body token: NotificationRequest,
    ): Response<NotificationResponse>

}