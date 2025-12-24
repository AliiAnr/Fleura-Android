package com.course.fleura.ui.screen.dashboard.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleura.data.model.remote.AddAddressResponse
import com.course.fleura.data.model.remote.AddressItem
import com.course.fleura.data.model.remote.Detail
import com.course.fleura.data.model.remote.ListAddressResponse
import com.course.fleura.data.model.remote.LogoutResponse
import com.course.fleura.data.repository.HomeRepository
import com.course.fleura.data.repository.ProfileRepository
import com.course.fleura.ui.common.ResultResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _dataInitialized = MutableStateFlow(false)
    val dataInitialized: StateFlow<Boolean> = _dataInitialized

    private val _selectedAddressItem = MutableStateFlow<AddressItem?>(null)
    val selectedAddressItem: StateFlow<AddressItem?> = _selectedAddressItem.asStateFlow()

    private val _selectedCartAddress = MutableStateFlow<AddressItem?>(null)
    val selectedCartAddress: StateFlow<AddressItem?> = _selectedCartAddress.asStateFlow()

    private val _profileDetailState =
        MutableStateFlow<ResultResponse<Detail?>>(ResultResponse.None)
    val profileDetailState: StateFlow<ResultResponse<Detail?>> = _profileDetailState

    private val _logoutState: MutableStateFlow<ResultResponse<LogoutResponse>> =
        MutableStateFlow(ResultResponse.None)
    val logoutState: StateFlow<ResultResponse<LogoutResponse>> = _logoutState.asStateFlow()


    private val _addAddressState =
        MutableStateFlow<ResultResponse<AddAddressResponse>>(ResultResponse.None)
    val addAddressState: StateFlow<ResultResponse<AddAddressResponse>> = _addAddressState

    private val _userAddressListState =
        MutableStateFlow<ResultResponse<ListAddressResponse>>(ResultResponse.None)
    val userAddressListState: StateFlow<ResultResponse<ListAddressResponse>> = _userAddressListState

    private val _updateUserAddressState =
        MutableStateFlow<ResultResponse<AddAddressResponse>>(ResultResponse.None)
    val updateUserAddressState: StateFlow<ResultResponse<AddAddressResponse>> = _updateUserAddressState

    fun loadInitialData() {
        if (!_dataInitialized.value) {
            getUserProfileDetail()
            getUserAddressList()
            _dataInitialized.value = true
        }
    }

    fun setSelectedAddress(address: AddressItem) {
        _selectedAddressItem.value = address
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = ResultResponse.Loading
//            profileRepository.logout()
            delay(1500)
            _logoutState.value = ResultResponse.Success(LogoutResponse(success = true))
        }
    }

    fun getSelectedCartAddress(): AddressItem?{
        return _selectedCartAddress.value
    }

    fun getAddressList(): List<AddressItem> {
        if (_userAddressListState.value is ResultResponse.Success){
            return (_userAddressListState.value as ResultResponse.Success<ListAddressResponse>).data.data
        }
        return emptyList()
    }

    fun setSelectedCartAddress(address: AddressItem) {
        _selectedAddressItem.value = address
    }

    fun getUserProfileDetail() {
        viewModelScope.launch {
            try {
                _profileDetailState.value = ResultResponse.Loading
                homeRepository.getUserDetail()
                    .collect { result ->
                        _profileDetailState.value = result
                    }
            } catch (e: Exception) {
                _profileDetailState.value =
                    ResultResponse.Error("Failed to generate OTP: ${e.message}")
            }
        }
    }

    fun resetLogoutState() {
        _logoutState.value = ResultResponse.None
    }

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
        longitude: Double = 0.0
    ) {
        viewModelScope.launch {
            try {
                _addAddressState.value = ResultResponse.Loading

                profileRepository.addUserAddress(
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
                ).collect { result ->
                    _addAddressState.value = result
                    if (result is ResultResponse.Success) {
                        getUserAddressList()
                    }
                }
            } catch (e: Exception) {
                _addAddressState.value =
                    ResultResponse.Error("Failed to add address: ${e.message}")
            }
        }
    }

    fun getUserAddressList() {
        viewModelScope.launch {
            try {
                _userAddressListState.value = ResultResponse.Loading

                profileRepository.getUserAddressList()
                    .collect { result ->
                        _userAddressListState.value = result
                        if (result is ResultResponse.Success){
                            _selectedCartAddress.value = result.data.data[0]
                        }
                    }
            } catch (e: Exception) {
                _userAddressListState.value =
                    ResultResponse.Error("Failed to fetch address list: ${e.message}")
            }
        }
    }

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
        longitude: Double = 0.0
    ) {
        viewModelScope.launch {
            try {
                _updateUserAddressState.value = ResultResponse.Loading

                profileRepository.updateUserAddress(
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
                ).collect { result ->
                    _updateUserAddressState.value = result
                    if (result is ResultResponse.Success) {
                        getUserAddressList()
                    }
                }
            } catch (e: Exception) {
                _updateUserAddressState.value =
                    ResultResponse.Error("Failed to update address: ${e.message}")
            }
        }
    }

//    fun getUserAddressListFromDataStore(){
//        viewModelScope.launch {
//            try {
//                _userAddressListState.value = ResultResponse.Loading
//
//                profileRepository.getUserAddressListFromDataStore()
//                    .collect { result ->
//                        _userAddressListState.value = result
//                    }
//            } catch (e: Exception) {
//                _userAddressListState.value =
//                    ResultResponse.Error("Failed to fetch address list from data store: ${e.message}")
//            }
//        }
//    }

}