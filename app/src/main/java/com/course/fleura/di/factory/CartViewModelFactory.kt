package com.course.fleura.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.course.fleura.data.repository.CartRepository
import com.course.fleura.data.repository.HomeRepository
import com.course.fleura.di.Injection
import com.course.fleura.ui.screen.dashboard.cart.CartViewModel
import com.course.fleura.ui.screen.dashboard.home.HomeViewModel

class CartViewModelFactory private constructor(private val cartRepository: CartRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                CartViewModel(cartRepository = cartRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: CartViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): CartViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: CartViewModelFactory(Injection.provideCartRepository(context)).also {
                    INSTANCE = it
                }
            }
    }
}