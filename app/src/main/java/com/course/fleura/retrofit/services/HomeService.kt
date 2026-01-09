package com.course.fleura.retrofit.services

import com.course.fleura.data.model.remote.DetailStoreResponse
import com.course.fleura.data.model.remote.ListProductResponse
import com.course.fleura.data.model.remote.ListStoreResponse
import com.course.fleura.data.model.remote.OtpRequest
import com.course.fleura.data.model.remote.OtpResponse
import com.course.fleura.data.model.remote.ProductReviewResponse
import com.course.fleura.data.model.remote.RegisterRequest
import com.course.fleura.data.model.remote.RegisterResponse
import com.course.fleura.data.model.remote.SaveProductToCartRequest
import com.course.fleura.data.model.remote.SaveProductToCartResponse
import com.course.fleura.data.model.remote.StoreProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeService {
    @GET("api/store")
    suspend fun getListStore(): Response<ListStoreResponse>

//    @GET("api/store/detail/{storeId}")
//    suspend fun getDetailStore(@Path("storeId") storeId: String): Response<DetailStoreResponse>

    @GET("api/store/detail")
    suspend fun getDetailStore(
        @Query("storeId") storeId: String
    ): Response<DetailStoreResponse>

    @GET("api/product/store/{storeId}")
    suspend fun getAllStoreProduct(@Path("storeId") storeId: String): Response<StoreProductResponse>

    @GET("api/product/review/product/{productId}")
    suspend fun getProductReview(@Path("productId") productId: String): Response<ProductReviewResponse>

    @GET("api/product")
    suspend fun getProductList(): Response<StoreProductResponse>

    @POST("api/cart/save")
    suspend fun saveProductToCart(@Query("buyerId") buyerId: String, @Body item: SaveProductToCartRequest): Response<SaveProductToCartResponse>

}
