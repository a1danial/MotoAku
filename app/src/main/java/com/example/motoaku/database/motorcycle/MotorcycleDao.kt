package com.example.motoaku.database.motorcycle

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MotorcycleDao {
    @Query("SELECT * FROM motorcycle")
    fun getAll(): Flow<List<Motorcycle>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(motorcycle: Motorcycle)
     /*
     INSERT INTO Motorcycle (brand, model, imageRes)
     VALUES ('Triumph', 'Tiger 800XC ABS A1 SE', 'TRIUMPH');
     */

    // TESTING
    @Query("DELETE FROM Motorcycle")
    fun deleteAll()

}