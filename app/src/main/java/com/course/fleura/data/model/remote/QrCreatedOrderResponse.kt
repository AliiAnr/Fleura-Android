package com.course.fleura.data.model.remote

import com.google.gson.annotations.SerializedName

data class QrCreatedOrderResponse(

	@field:SerializedName("data")
	val data: QrCreatedOrderData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class QrCreatedOrderData(

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
