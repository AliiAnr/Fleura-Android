package com.course.fleura.di.factory

import com.course.fleura.data.repository.OrderRepository
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.course.fleura.di.Injection
import com.course.fleura.ui.screen.dashboard.order.OrderViewModel

class OrderViewModelFactory private constructor(private val orderRepository: OrderRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(OrderViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                OrderViewModel(orderRepository = orderRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: OrderViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): OrderViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: OrderViewModelFactory(Injection.provideOrderRepository(context)).also {
                    INSTANCE = it
                }
            }
    }

}