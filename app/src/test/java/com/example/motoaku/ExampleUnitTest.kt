package com.example.motoaku

import com.example.motoaku.database.motorcycle.Brand
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

class MotorCycleDataClass {
    @Test
    fun When_checkingBrand_expect_returnCorrectBrand() {
        assertEquals(Brand.TRIUMPH,Brand.checkBrand("triumph"))
        assertEquals(Brand.TRIUMPH,Brand.checkBrand(" triumph "))
        assertEquals(Brand.HARLEYDAVIDSON,Brand.checkBrand(" Harley Davidson "))
    }


}

class AddMotoScreen {
    @Test
    // Reference to MenuField of BrandField
    fun When_brandInputHasNoSuggestion_expect_dropDownMenuInvinsible() {
        val brandSuggestion = Brand.values().map { it.brandName }.filter {
            it.lowercase().contains("Hdnaxn".lowercase()) }
        assertTrue(brandSuggestion.isEmpty())
        // TODO("Test to prove if drop down menu is invisible")
    }
}