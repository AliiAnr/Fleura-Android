package com.course.fleura.data.model.remote

import com.google.gson.annotations.SerializedName

data class CartListResponse(

	@field:SerializedName("data")
	val data: List<CartItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class CartItem(

	@field:SerializedName("storeName")
	val storeName: String,

	@field:SerializedName("storeId")
	val storeId: String,

	@field:SerializedName("items")
	val items: List<CartProduct>
)

data class CartProduct(

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("quantity")
	val quantity: Int,

	@field:SerializedName("productId")
	val productId: String,

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("productName")
	val productName: String
)

// ========================================
// ========================================

