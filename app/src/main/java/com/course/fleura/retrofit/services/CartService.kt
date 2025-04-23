package com.course.fleura.retrofit.services

import com.course.fleura.data.model.remote.CartListResponse
import com.course.fleura.data.model.remote.DetailStoreResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CartService {

    @GET("api/cart")
    suspend fun getDetailStore(@Query("buyerId") buyerId: String): Response<CartListResponse>

}