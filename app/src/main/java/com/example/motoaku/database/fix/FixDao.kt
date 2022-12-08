package com.example.motoaku.database.fix

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FixDao {
    @Query("SELECT * FROM fix WHERE moto_id = :motoId")
    fun getFixFromMotoId(motoId: Int): Flow<List<Fix>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(fix: Fix)

    @Query("DELETE FROM Fix")
    suspend fun deleteAll()
}