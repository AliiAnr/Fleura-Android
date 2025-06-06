package com.course.fleura.retrofit.services

import com.course.fleura.data.model.remote.AddAddressRequest
import com.course.fleura.data.model.remote.AddAddressResponse
import com.course.fleura.data.model.remote.CartListResponse
import com.course.fleura.data.model.remote.ListAddressResponse
import com.course.fleura.data.model.remote.UpdateAddressRequest
import com.course.fleura.data.model.remote.VerifyOtpRequest
import com.course.fleura.data.model.remote.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ProfileService {
    @POST("api/buyer/address")
    suspend fun addUserAddress(@Body request: AddAddressRequest): Response<AddAddressResponse>

    @GET("api/buyer/address")
    suspend fun getAllUserAddress(): Response<ListAddressResponse>

    @PUT("api/buyer/address")
    suspend fun updateUserAddress(@Body request: UpdateAddressRequest): Response<AddAddressResponse>

}