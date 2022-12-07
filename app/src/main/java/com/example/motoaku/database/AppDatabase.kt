package com.example.motoaku.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.motoaku.database.fix.Fix
import com.example.motoaku.database.fix.FixDao
import com.example.motoaku.database.motorcycle.Motorcycle
import com.example.motoaku.database.motorcycle.MotorcycleDao

@Database(entities = [Motorcycle::class, Fix::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun fixDao(): FixDao
    abstract fun motoDao(): MotorcycleDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}

const val DATABASE_NAME = "motoaku-db"