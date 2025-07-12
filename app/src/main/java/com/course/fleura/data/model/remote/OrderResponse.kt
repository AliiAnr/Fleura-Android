package com.course.fleura.data.model.remote

import com.google.gson.annotations.SerializedName

data class OrderListResponse(

	@field:SerializedName("data")
	val data: List<OrderDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class OrderItemsItem(

	@field:SerializedName("product")
	val product: OrderProduct,

	@field:SerializedName("quantity")
	val quantity: Int,

	@field:SerializedName("id")
	val id: String
)

data class OrderStore(

	@field:SerializedName("operational_day")
	val operationalDay: String,

	@field:SerializedName("sellerId")
	val sellerId: String,

	@field:SerializedName("balance")
	val balance: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("logo")
	val logo: String,

	@field:SerializedName("operational_hour")
	val operationalHour: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("picture")
	val picture: String,

	@field:SerializedName("admin_verified_at")
	val adminVerifiedAt: Any
)

data class OrderPictureItem(

	@field:SerializedName("path")
	val path: String,

	@field:SerializedName("id")
	val id: String
)

data class OrderCategory(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)

data class OrderPayment(

	@field:SerializedName("methode")
	val methode: String,

	@field:SerializedName("qris_url")
	val qrisUrl: String,

	@field:SerializedName("orderId")
	val orderId: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("success_at")
	val successAt: Any,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("qris_expired_at")
	val qrisExpiredAt: String,

	@field:SerializedName("status")
	val status: String
)

data class OrderProduct(

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

	@field:SerializedName("store")
	val store: OrderStore,

	@field:SerializedName("stock")
	val stock: Int,

	@field:SerializedName("category")
	val category: OrderCategory,

	@field:SerializedName("point")
	val point: Int,

	@field:SerializedName("admin_verified_at")
	val adminVerifiedAt: Any,

	@field:SerializedName("picture")
	val picture: List<OrderPictureItem>
)

data class OrderDataItem(

	@field:SerializedName("note")
	val note: String,

	@field:SerializedName("taken_date")
	val takenDate: String,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("taken_method")
	val takenMethod: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("payment")
	val payment: OrderPayment,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("orderItems")
	val orderItems: List<OrderItemsItem>,

	@field:SerializedName("point")
	val point: Int,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("addressId")
	val addressId: String
)

// ==============================================
// ==============================================


data class OrderAddressResponse(

	@field:SerializedName("data")
	val data: OrderAddressData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class OrderAddressData(

	@field:SerializedName("province")
	val province: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("road")
	val road: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("district")
	val district: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("postcode")
	val postcode: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("detail")
	val detail: String,

	@field:SerializedName("longitude")
	val longitude: Double
)
