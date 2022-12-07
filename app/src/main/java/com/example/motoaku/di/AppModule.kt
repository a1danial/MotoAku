package com.example.motoaku.di

import android.content.Context
import com.example.motoaku.database.AppDatabase
import com.example.motoaku.database.fix.FixDao
import com.example.motoaku.database.fix.FixRepository
import com.example.motoaku.database.motorcycle.MotorcycleDao
import com.example.motoaku.database.motorcycle.MotorcycleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideMotoDao(
        appDatabase: AppDatabase
    ) = appDatabase.motoDao()

    @Provides
    fun provideMotoRepo(
        motoDao: MotorcycleDao
    ): MotorcycleRepository = MotorcycleRepository(motoDao)

    // Pool Database
    @Provides
    fun provideFixDao(
        appDatabase: AppDatabase
    ) = appDatabase.fixDao()

    @Provides
    fun provideFixRepo(
        fixDao: FixDao
    ): FixRepository = FixRepository(fixDao)
}