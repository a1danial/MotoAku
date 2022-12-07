package com.example.motoaku.database.fix

import androidx.annotation.WorkerThread
import java.util.concurrent.Flow

class FixRepository(private val fixDao: FixDao) {

    val allFix: kotlinx.coroutines.flow.Flow<List<Fix>> = fixDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(fix: Fix) {
        fixDao.insert(fix)
    }
}