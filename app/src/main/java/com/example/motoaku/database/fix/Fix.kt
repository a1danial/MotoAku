package com.example.motoaku.database.fix

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.motoaku.database.motorcycle.Motorcycle
import java.util.Date

@Entity(foreignKeys = [ForeignKey(entity = Motorcycle::class, parentColumns = ["mId"], childColumns = ["fixId"])])
data class Fix (
    @PrimaryKey(autoGenerate = true) val fixId: Int = 0,
    @ColumnInfo(name = "moto_id") val motoId: Int,
    @ColumnInfo(name = "date_start") val dateStart: Date,
    // [Fix Details]
    @ColumnInfo(name = "part") val part: String,

    // [Optional]
    @ColumnInfo(name = "date_end") val dateEnd: Date?,
    @ColumnInfo(name = "odo_start") val odoStart: Double?,
    @ColumnInfo(name = "odo_end") val odoEnd: Double?,
    @ColumnInfo(name = "cost_price") val costPrice: Double?,
    @ColumnInfo(name = "cost_currency") val costCurrency: String?,
    @ColumnInfo(name = "comment") val comment: String?,
)