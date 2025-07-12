package com.course.fleura.retrofit.services

import com.course.fleura.data.model.remote.OrderAddressResponse
import com.course.fleura.data.model.remote.OrderListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderService {

    @GET("api/order/buyer")
    suspend fun getBuyerOrders(): Response<OrderListResponse>

    @GET("api/buyer/address/detail/{addressId}")
    suspend fun getBuyerAddress(@Path("addressId") addressId: String): Response<OrderAddressResponse>

//    @GET("api/payment/detail/{orderId}")
//    suspend fun getBuyerAddress(@Path("orderId") orderId: String): Response<OrderAddressResponse>


}