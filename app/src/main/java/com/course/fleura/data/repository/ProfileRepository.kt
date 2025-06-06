package com.course.fleura.data.repository

import android.content.Context
import android.util.Log
import com.course.fleura.data.model.remote.AddAddressRequest
import com.course.fleura.data.model.remote.AddAddressResponse
import com.course.fleura.data.model.remote.AddressItem
import com.course.fleura.data.model.remote.CartListResponse
import com.course.fleura.data.model.remote.CartOrderItems
import com.course.fleura.data.model.remote.CartOrderRequest
import com.course.fleura.data.model.remote.CartOrderResponse
import com.course.fleura.data.model.remote.Detail
import com.course.fleura.data.model.remote.ListAddressResponse
import com.course.fleura.data.model.remote.UpdateAddressRequest
import com.course.fleura.data.store.DataStoreManager
import com.course.fleura.retrofit.api.ApiConfig
import com.course.fleura.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProfileRepository private constructor(
    context: Context
) {

    private val dataStoreManager = DataStoreManager(context)
    private val profileService = ApiConfig.getProfileService(context)

    fun addUserAddress(
        name: String = "",
        phone: String = "",
        province: String = "",
        road: String = "",
        city: String = "",
        district: String = "",
        postcode: String = "",
        detail: String = "",
        latitude: Double = 0.0,
        longitude: Double = 0.0,
    ): Flow<ResultResponse<AddAddressResponse>> = flow {
        emit(ResultResponse.Loading)
        try {
            val response = profileService.addUserAddress(
                request = AddAddressRequest(
                    name = name,
                    phone = phone,
                    province = province,
                    road = road,
                    city = city,
                    district = district,
                    postcode = postcode,
                    detail = detail,
                    latitude = latitude,
                    longitude = longitude
                )
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Add Address failed, response body is empty"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)

    fun getUserAddressList(): Flow<ResultResponse<ListAddressResponse>> = flow {

        emit(ResultResponse.Loading)
        try {
            val response = profileService.getAllUserAddress()
            if (response.isSuccessful) {
                response.body()?.let {
                    dataStoreManager.saveAddressList(it.data)
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)

    fun getUserAddressListFromDataStore(): Flow<ResultResponse<List<AddressItem>>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = dataStoreManager.getUserAddressList()
            emit(ResultResponse.Success(response))
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Error retrieving user details"))
        }
    }.flowOn(Dispatchers.IO)

    fun updateUserAddress(
        addressId: String,
        name: String = "",
        phone: String = "",
        province: String = "",
        road: String = "",
        city: String = "",
        district: String = "",
        postcode: String = "",
        detail: String = "",
        latitude: Double = 0.0,
        longitude: Double = 0.0,
    ): Flow<ResultResponse<AddAddressResponse>> = flow {
        emit(ResultResponse.Loading)
        try {
            val response = profileService.updateUserAddress(
                request = UpdateAddressRequest(
                    addressId = addressId,
                    name = name,
                    phone = phone,
                    province = province,
                    road = road,
                    city = city,
                    district = district,
                    postcode = postcode,
                    detail = detail,
                    latitude = latitude,
                    longitude = longitude
                )
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Update Address failed, response body is empty"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        @Volatile
        private var INSTANCE: ProfileRepository? = null

        fun getInstance(context: Context): ProfileRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProfileRepository(context).also { INSTANCE = it }
            }
    }
}