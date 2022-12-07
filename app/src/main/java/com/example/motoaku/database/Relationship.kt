package com.example.motoaku.database

import androidx.room.Embedded
import androidx.room.Relation
import com.example.motoaku.database.fix.Fix
import com.example.motoaku.database.motorcycle.Motorcycle

data class MotoFixes(
    @Embedded val moto: Motorcycle,

    @Relation(
        parentColumn = "mId",
        entityColumn = "motoId"
    )
    val fixes: List<Fix> = emptyList()
)