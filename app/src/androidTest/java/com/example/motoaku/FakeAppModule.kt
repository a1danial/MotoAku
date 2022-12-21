package com.example.motoaku

import android.content.Context
import com.example.motoaku.database.AppDatabase
import com.example.motoaku.database.fix.FixDao
import com.example.motoaku.database.fix.FixRepository
import com.example.motoaku.database.motorcycle.MotorcycleDao
import com.example.motoaku.database.motorcycle.MotorcycleRepository
import com.example.motoaku.di.AppModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object FakeAppModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideMotoDao(
        appDatabase: AppDatabase
    ) = appDatabase.getMotoDao()

    @Provides
    fun provideMotoRepo(
        motoDao: MotorcycleDao
    ): MotorcycleRepository = MotorcycleRepository(motoDao)

    // Pool Database
    @Provides
    fun provideFixDao(
        appDatabase: AppDatabase
    ) = appDatabase.getFixDao()

    @Provides
    fun provideFixRepo(
        fixDao: FixDao
    ): FixRepository = FixRepository(fixDao)

}