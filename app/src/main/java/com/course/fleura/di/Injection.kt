package com.course.fleura.di

import android.content.Context
import com.course.fleura.data.repository.CartRepository
import com.course.fleura.data.repository.HomeRepository
import com.course.fleura.data.repository.LoginRepository
import com.course.fleura.data.repository.OnBoardingRepository
import com.course.fleura.data.repository.OtpRepository
import com.course.fleura.data.repository.ProfileRepository
import com.course.fleura.data.repository.RegisterRepository
import com.course.fleura.data.repository.detail.StoreRepository

object Injection {
    fun provideOnBoardingRepository(context: Context): OnBoardingRepository {
        return OnBoardingRepository.getInstance(context)
    }

    fun provideRegisterRepository(context: Context): RegisterRepository {
        return RegisterRepository.getInstance(context)
    }

    fun provideOtpRepository(context: Context): OtpRepository {
        return OtpRepository.getInstance(context)
    }

    fun provideLoginRepository(context: Context): LoginRepository {
        return LoginRepository.getInstance(context)
    }

    fun provideHomeRepository(context: Context): HomeRepository {
        return HomeRepository.getInstance(context)
    }

    fun provideCartRepository(context: Context): CartRepository {
        return CartRepository.getInstance(context)
    }

    fun provideProfileRepository(context: Context): ProfileRepository {
        return ProfileRepository.getInstance(context)
    }

//    DETAIL REPOSITORY

    fun provideStoreRepository(context: Context): StoreRepository {
        return StoreRepository.getInstance(context)
    }
}