package com.example.motoaku.database.fix

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class FixRepository(private val fixDao: FixDao) {

    fun fixFromMotoId(motoId: Int): Flow<List<Fix>> = fixDao.getFixFromMotoId(motoId)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(fix: Fix) {
        fixDao.insert(fix)
    }
}