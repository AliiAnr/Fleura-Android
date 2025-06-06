package com.course.fleura.data.model.remote

import com.google.gson.annotations.SerializedName

data class CartOrderRequest(

	@field:SerializedName("note")
	val note: String,

	@field:SerializedName("taken_date")
	val takenDate: String,

	@field:SerializedName("taken_method")
	val takenMethod: String,

	@field:SerializedName("items")
	val items: List<CartOrderItems>,

	@field:SerializedName("payment_method")
	val paymentMethod: String,

	@field:SerializedName("addressId")
	val addressId: String
)

data class CartOrderItems(

	@field:SerializedName("quantity")
	val quantity: Int,

	@field:SerializedName("productId")
	val productId: String
)

// =====================================
// =====================================

data class CartOrderResponse(

	@field:SerializedName("data")
	val data: CartOrderResponseData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class CartOrderResponseData(

	@field:SerializedName("methode")
	val methode: String,

	@field:SerializedName("qris_url")
	val qrisUrl: String,

	@field:SerializedName("orderId")
	val orderId: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("success_at")
	val successAt: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("qris_expired_at")
	val qrisExpiredAt: String,

	@field:SerializedName("status")
	val status: String
)


// =====================================
// =====================================

data class CartListResponse(

	@field:SerializedName("data")
	val data: List<DataCartItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class CartItemCategory(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)

data class ItemFromCart(

	@field:SerializedName("product")
	val product: CartItemProduct,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("quantity")
	val quantity: Int,

	@field:SerializedName("price")
	val price: String
)

data class CartItemProduct(

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("arrange_time")
	val arrangeTime: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("pre_order")
	val preOrder: Boolean,

	@field:SerializedName("stock")
	val stock: Int,

	@field:SerializedName("category")
	val category: CartItemCategory,

	@field:SerializedName("point")
	val point: Int,

	@field:SerializedName("picture")
	val picture: List<PictureCartItem>
)

data class PictureCartItem(

	@field:SerializedName("path")
	val path: String,

	@field:SerializedName("id")
	val id: String
)

data class DataCartItem(

	@field:SerializedName("storePicture")
	val storePicture: String,

	@field:SerializedName("storeName")
	val storeName: String,

	@field:SerializedName("storeId")
	val storeId: String,

	@field:SerializedName("items")
	val items: List<ItemFromCart>
)

// =====================================
// =====================================

data class QrisPaymentResponse(

	@field:SerializedName("data")
	val data: QrisPaymentData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class QrisPaymentData(

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("expiry_time")
	val expiryTime: String,

	@field:SerializedName("qris")
	val qris: String,

	@field:SerializedName("status")
	val status: String
)
