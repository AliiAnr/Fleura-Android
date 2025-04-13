package com.course.fleura.data.model.remote

import com.google.gson.annotations.SerializedName


data class PersonalizeResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("timestamp")
    val timestamp: String
)

data class NameRequest(
    @field:SerializedName("username")
    val username: String
)