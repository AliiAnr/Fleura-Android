package com.course.fleura.retrofit.services

import com.course.fleura.data.model.remote.AddAddressRequest
import com.course.fleura.data.model.remote.AddAddressResponse
import com.course.fleura.data.model.remote.GetUserResponse
import com.course.fleura.data.model.remote.ListAddressResponse
import com.course.fleura.data.model.remote.LoginRequest
import com.course.fleura.data.model.remote.LoginResponse
import com.course.fleura.data.model.remote.NameRequest
import com.course.fleura.data.model.remote.PersonalizeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface LoginService {
    @POST("api/buyer/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/buyer/profile")
    suspend fun getUser(): Response<GetUserResponse>

    @PUT("api/buyer/username")
    suspend fun setUsername(@Body username: NameRequest): Response<PersonalizeResponse>

    @GET("api/buyer/address")
    suspend fun getAllUserAddress(): Response<ListAddressResponse>

    @POST("api/buyer/address")
    suspend fun addUserAddress(@Body request: AddAddressRequest): Response<PersonalizeResponse>

}
