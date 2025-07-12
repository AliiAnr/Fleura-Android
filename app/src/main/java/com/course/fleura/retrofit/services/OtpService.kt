package com.course.fleura.retrofit.services

import com.course.fleura.data.model.remote.VerifyOtpRequest
import com.course.fleura.data.model.remote.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OtpService {

    @POST("api/buyer/auth/otp/verify")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

}


