package com.course.fleura.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.course.fleura.data.repository.detail.StoreRepository
import com.course.fleura.di.Injection
import com.course.fleura.ui.screen.dashboard.detail.home.StoreViewModel

class StoreViewModelFactory private constructor(private val storeRepository: StoreRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StoreViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                StoreViewModel(storeRepository = storeRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: StoreViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): StoreViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: StoreViewModelFactory(Injection.provideStoreRepository(context)).also {
                    INSTANCE = it
                }
            }
    }
}