package com.course.fleura.data.model.remote

import com.google.gson.annotations.SerializedName

data class GetUserResponse(

	@field:SerializedName("data")
	val data: Detail,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class Detail(

	@field:SerializedName("phone")
	val phone: Any,

	@field:SerializedName("verified_at")
	val verifiedAt: String,

	@field:SerializedName("name")
	val name: String?,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("point")
	val point: Int
) {
	fun isProfileComplete(): Boolean {
		return !name.isNullOrBlank()
	}
}
