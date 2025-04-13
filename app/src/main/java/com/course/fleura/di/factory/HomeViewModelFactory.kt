package com.course.fleura.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.course.fleura.data.repository.HomeRepository
import com.course.fleura.data.repository.LoginRepository
import com.course.fleura.di.Injection
import com.course.fleura.ui.screen.authentication.login.LoginScreenViewModel
import com.course.fleura.ui.screen.dashboard.home.HomeViewModel

class HomeViewModelFactory private constructor(private val homeRepository: HomeRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                HomeViewModel(homeRepository = homeRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: HomeViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): HomeViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeViewModelFactory(Injection.provideHomeRepository(context)).also {
                    INSTANCE = it
                }
            }
    }
}