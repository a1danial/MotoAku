package com.example.motoaku.database.motorcycle

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


class MotorcycleRepository(private val motorcycleDao: MotorcycleDao) {

    val allMoto: Flow<List<Motorcycle>> = motorcycleDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(motorcycle: Motorcycle) {
        motorcycleDao.insert(motorcycle)
    }
}