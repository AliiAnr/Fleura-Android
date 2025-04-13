package com.course.fleura.data.repository

import android.content.Context
import com.course.fleura.data.store.DataStoreManager

class HomeRepository private constructor(
    context: Context
) {
    private val dataStoreManager = DataStoreManager(context)

    companion object {
        @Volatile
        private var INSTANCE: HomeRepository? = null

        fun getInstance(context: Context): HomeRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeRepository(context).also { INSTANCE = it }
            }
    }
}