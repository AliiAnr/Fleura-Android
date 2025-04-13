package com.course.fleura.retrofit.services

import com.course.fleura.data.model.remote.OtpRequest
import com.course.fleura.data.model.remote.OtpResponse
import com.course.fleura.data.model.remote.RegisterRequest
import com.course.fleura.data.model.remote.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("api/buyer/register")
    suspend fun registerBuyer(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/buyer/auth/otp/generate")
    suspend fun generateOtp(@Body request: OtpRequest): Response<OtpResponse>
}


