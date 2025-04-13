package com.course.fleura.data.repository

import android.content.Context
import com.course.fleura.data.resource.Resource
import com.course.fleura.data.store.DataStoreManager
import kotlinx.coroutines.flow.Flow

class OnBoardingRepository private constructor(
    context: Context
) {
    private val dataStoreManager = DataStoreManager(context)

    suspend fun setOnboardingCompleted(status: Boolean) {
        dataStoreManager.setOnboardingCompleted(status)
    }

    fun getOnBoardingStatus(): Flow<Boolean> = dataStoreManager.onboardingCompleted

    companion object {
        @Volatile
        private var INSTANCE: OnBoardingRepository? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): OnBoardingRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: OnBoardingRepository(context).also {
                    INSTANCE = it
                }
            }
    }
}
