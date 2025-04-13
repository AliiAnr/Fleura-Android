package com.course.fleura.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.course.fleura.data.repository.LoginRepository
import com.course.fleura.di.Injection
import com.course.fleura.ui.screen.authentication.login.LoginScreenViewModel

class LoginViewModelFactory private constructor(private val loginRepository: LoginRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginScreenViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                LoginScreenViewModel(loginRepository = loginRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): LoginViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: LoginViewModelFactory(Injection.provideLoginRepository(context)).also {
                    INSTANCE = it
                }
            }
    }
}