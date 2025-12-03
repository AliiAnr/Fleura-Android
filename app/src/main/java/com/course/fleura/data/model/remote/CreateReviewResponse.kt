package com.course.fleura.data.model.remote

import com.google.gson.annotations.SerializedName


data class CreateReviewRequest(

	@field:SerializedName("productId")
	val productId: String,

	@field:SerializedName("rate")
	val rate: Int,

	@field:SerializedName("message")
	val message: String
)

data class CreateReviewResponse(
	
	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("timestamp")
	val timestamp: String
)

