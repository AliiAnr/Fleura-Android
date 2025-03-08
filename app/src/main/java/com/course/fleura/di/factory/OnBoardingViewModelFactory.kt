package com.course.fleura.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.course.fleura.data.repository.OnBoardingRepository
import com.course.fleura.di.Injection
import com.course.fleura.ui.screen.onboarding.OnBoardingViewModel

class OnBoardingViewModelFactory private constructor(private val onBoardingRepository: OnBoardingRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(OnBoardingViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                OnBoardingViewModel(onBoardingRepository = onBoardingRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: OnBoardingViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): OnBoardingViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: OnBoardingViewModelFactory(Injection.provideOnBoardingRepository(context)).also {
                    INSTANCE = it
                }
            }
    }
}