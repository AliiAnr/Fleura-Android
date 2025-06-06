package com.course.fleura.retrofit.services

import com.course.fleura.data.model.remote.CartListResponse
import com.course.fleura.data.model.remote.CartOrderRequest
import com.course.fleura.data.model.remote.CartOrderResponse
import com.course.fleura.data.model.remote.QrisPaymentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CartService {

    @GET("api/cart")
    suspend fun getCartList(@Query("buyerId") buyerId: String): Response<CartListResponse>

    @POST("api/order")
    suspend fun createOrder(@Body request: CartOrderRequest): Response<CartOrderResponse>

    @POST("api/payment/qris/{orderId}")
    suspend fun getQrisPayment(@Path("orderId") orderId: String): Response<QrisPaymentResponse>

}