package com.example.motoaku.database.motorcycle

import androidx.annotation.DrawableRes
import androidx.room.*
import com.example.motoaku.R

@Entity
data class Motorcycle (
    @PrimaryKey(autoGenerate = true) val mId: Int = 0,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "model") val model: String,

    // Optional
    @ColumnInfo(name = "imageRes") val imageRes: Brand?,
    @ColumnInfo(name = "VIN") val vin: String?,
    @ColumnInfo(name = "year_built") val yearBuilt: Int?,
)

enum class Brand (
    val brandName: String,
    @DrawableRes val imageRes: Int,
    val altName: List<String>?
) {



    //AJP("AJP", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
    //APRILIA("Aprilia", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//AVETA("Aveta", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//BENELLI("Benelli", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//BMW("BMW", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//BRIXTON("Brixton", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//CFMOTO("CFMoto", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//CMC("CMC", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//DAIICHI("Daiichi", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//DUCATI("Ducati", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//ECLIMO("Eclimo", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//GPX("GPX", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
    HARLEYDAVIDSON("Harley-Davidson", R.drawable.harley_davidson_logo, listOf("Harley Davidson", "HD", "Harley")),
    HONDA("Honda", R.drawable.honda_logo, null),
//ITALJET("Italjet", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
    KAWASAKI("Kawasaki", R.drawable.kawasaki_logo, null),
//KEEWAY("Keeway", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//KTM("KTM", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//KTNS("KTNS", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//KYMCO("Kymco", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//MODENAS("Modenas", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//MOTO GUZZI("Moto Guzzi", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//MOTO MORINI("Moto Morini", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//NIU("Niu", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//OTTIMO("Ottimo", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
    PIAGGIO("Piaggio", R.drawable.piaggio_logo, null),
//QJ MOTOR("QJ Motor", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//ROYAL ENFIELD("Royal Enfield", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//SCOMADI("Scomadi", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//SHERCO("Sherco", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//SM SPORT("SM Sport", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//SUZUKI("Suzuki", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//SYM("Sym", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
//TREELETRIK("Treeletrik", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
    TRIUMPH("Triumph", R.drawable.triumph_logo, null),
    VESPA("Vespa", R.drawable.vespa_logo, null),
//WMOTO("Wmoto", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname
    YAMAHA("Yamaha", R.drawable.yamaha_logo, null),
//ZONTES("Zontes", R.drawable.triumph_logo, null), // TODO Picture, brandname and altname

    ;

    companion object {
        fun checkBrand(name: String): Brand? {
            var brandCheck: Brand? = null
            val trimName = name.trim()
            for (brand in values()) {
                val list =
                    listOf<String>(brand.name, brand.brandName).plus(brand.altName ?: emptyList())
                for (brandName in list) {
                    if (brandName.equals(trimName, true)) {
                        brandCheck = brand
                    }
                }
            }
            return brandCheck
        }
    }
}
