package com.course.fleura.data.repository

import android.content.Context
import com.course.fleura.data.model.remote.AddAddressRequest
import com.course.fleura.data.model.remote.AddAddressResponse
import com.course.fleura.data.model.remote.GetUserResponse
import com.course.fleura.data.model.remote.ListAddressResponse
import com.course.fleura.data.model.remote.LoginRequest
import com.course.fleura.data.model.remote.LoginResponse
import com.course.fleura.data.model.remote.NameRequest
import com.course.fleura.data.model.remote.PersonalizeResponse
import com.course.fleura.data.store.DataStoreManager
import com.course.fleura.retrofit.api.ApiConfig
import com.course.fleura.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LoginRepository private constructor(
    context: Context
) {

    private val dataStoreManager = DataStoreManager(context)
    private val loginService = ApiConfig.getLoginService(context)

    fun loginUser(email: String, password: String): Flow<ResultResponse<LoginResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = loginService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let {
                    dataStoreManager.saveUserToken(it.data.accessToken)
                    dataStoreManager.setUserLoggedIn(true)
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }

    }.flowOn(Dispatchers.IO)

    fun loginGoogleUser(): Flow<ResultResponse<LoginResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = loginService.googleLogin()
            if (response.isSuccessful) {
                response.body()?.let {
                    dataStoreManager.saveUserToken(it.data.accessToken)
                    dataStoreManager.setUserLoggedIn(true)
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }

    }.flowOn(Dispatchers.IO)

    fun getUser(): Flow<ResultResponse<GetUserResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = loginService.getUser()
            if (response.isSuccessful) {
                response.body()?.let {
                    dataStoreManager.saveUserDetail(it.data)
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)

    fun setUsername(username: String): Flow<ResultResponse<PersonalizeResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = loginService.setUsername(NameRequest(username))
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
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
            val response = loginService.getAllUserAddress()
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
    ): Flow<ResultResponse<PersonalizeResponse>> = flow {
        emit(ResultResponse.Loading)
        try {
            val response = loginService.addUserAddress(
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

    fun isUserLoggedIn(): Flow<Boolean> = dataStoreManager.isUserLoggedIn

    fun getUserToken(): Flow<String?> = dataStoreManager.userToken

    suspend fun setPersonalizeFilled() {
        dataStoreManager.setPersonalizedFilled(dataStoreManager.isPersonalizeCompleted())
    }

    fun isPersonalizeFilled() : Flow<Boolean> = dataStoreManager.personalizedFilled

    suspend fun logout() {
        dataStoreManager.clearUserData()
    }


    companion object {
        @Volatile
        private var INSTANCE: LoginRepository? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): LoginRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: LoginRepository(context).also {
                    INSTANCE = it
                }
            }
    }
}