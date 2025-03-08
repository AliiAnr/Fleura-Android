package com.course.fleura.di

import android.content.Context
import com.course.fleura.data.repository.OnBoardingRepository

object Injection {
    fun provideOnBoardingRepository(context: Context): OnBoardingRepository {
        return OnBoardingRepository.getInstance(context)
    }

}