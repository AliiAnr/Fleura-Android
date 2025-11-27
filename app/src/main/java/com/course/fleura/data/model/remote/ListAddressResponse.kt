package com.course.fleura.data.model.remote

import com.google.gson.annotations.SerializedName


data class AddAddressRequest(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("province")
	val province: String,

	@field:SerializedName("road")
	val road: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("district")
	val district: String,

	@field:SerializedName("postcode")
	val postcode: String,

	@field:SerializedName("detail")
	val detail: String,

	@field:SerializedName("latitude")
	val latitude: Double = 0.0,

	@field:SerializedName("longitude")
	val longitude: Double = 0.0

)

data class UpdateAddressRequest(

	@field:SerializedName("addressId")
	val addressId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("province")
	val province: String,

	@field:SerializedName("road")
	val road: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("district")
	val district: String,

	@field:SerializedName("postcode")
	val postcode: String,

	@field:SerializedName("detail")
	val detail: String,

	@field:SerializedName("latitude")
	val latitude: Double = 0.0,

	@field:SerializedName("longitude")
	val longitude: Double = 0.0

)

data class AddAddressResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class ListAddressResponse(

	@field:SerializedName("data")
	val data: List<AddressItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class AddressItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("province")
	val province: String,

	@field:SerializedName("road")
	val road: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("district")
	val district: String,

	@field:SerializedName("postcode")
	val postcode: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("detail")
	val detail: String,

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("longitude")
	val longitude: Double

) {

	fun isAddressCompleted(): Boolean {
		return name.isNotBlank() &&
				phone.isNotBlank() &&
				province.isNotBlank() &&
				road.isNotBlank() &&
				city.isNotBlank() &&
				district.isNotBlank() &&
				postcode.isNotBlank() &&
				detail.isNotBlank()
	}

}
