package com.example.motoaku.database

import androidx.room.TypeConverter
import com.example.motoaku.database.motorcycle.Brand
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromBrand(value: Brand?): String? {
        return value?.name
    }

    @TypeConverter
    fun toBrand(name: String?): Brand? {
        try {
            return Brand.valueOf(name ?: "")
        } catch (e: Exception) {
            return null
        }
    }
}