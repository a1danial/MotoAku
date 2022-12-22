package com.example.motoaku.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.motoaku.ViewModel
import com.example.motoaku.database.motorcycle.Brand
import com.example.motoaku.database.motorcycle.Motorcycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMotoScreen(
    navController: NavHostController,
    vm: ViewModel = hiltViewModel()
) {
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var vin by remember { mutableStateOf("") }
    var yearBuilt by remember { mutableStateOf("") }

    val modifier = Modifier.fillMaxWidth()

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(top = 30.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        BrandField(brand, modifier) { brand = it } // Fixme show nothing if empty

        TextField(
            value = model,
            onValueChange = { model = it },
            modifier = modifier,
            label = { Text(text = "Model") }
        )
        TextField(
            value = vin,
            onValueChange = { vin = it },
            modifier = modifier,
            label = { Text(text = "VIN") }
        )
        TextField(
            value = yearBuilt,
            onValueChange = { yearBuilt = it },
            modifier = modifier,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Year built") }
        )
        Button(
            onClick = {
                vm.addMoto(
                    Motorcycle(
                        brand = brand.trim(),
                        model = model.trim(),
                        imageRes = Brand.checkBrand(brand),
                        vin = vin.trim().ifEmpty { null },
                        yearBuilt = yearBuilt.trim().ifEmpty { null }?.toInt())
                )
                navController.popBackStack()
        }) {
            Text(text = "Add Moto", color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BrandField(brand: String, modifier: Modifier, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var rowSize by remember { mutableStateOf(Size.Zero) }
    var brandSuggestion by remember { mutableStateOf(emptyList<String>()) }
    val allBrandList = Brand.values().map { it.brandName }.sorted()

    Box {
        TextField(
            value = brand,
            onValueChange = { newBrand ->
                onValueChange(newBrand)
                expanded = true
                brandSuggestion = Brand.values().map { it.brandName }.filter {
                    it.contains(newBrand,true) }
                            },
            modifier = modifier
                .onGloballyPositioned { coordinates ->
                    rowSize = coordinates.size.toSize() },
            label = { Text(text = "Brand") }
        )
        DropdownMenu(
            expanded = expanded,
            properties = PopupProperties(),
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { rowSize.width.toDp() }),
        ) {
            val brandInputIsNotEmptyANDNoBrandSuggestion = brand.isNotEmpty() && brandSuggestion.isEmpty()
            val brandInputIsNotEmpty = brand.isNotEmpty()
            when {
                brandInputIsNotEmptyANDNoBrandSuggestion -> expanded = false
                brandInputIsNotEmpty ->
                    brandSuggestion.sorted().forEach {
                        DropdownMenuItem(
                            text = { Text(text = it, modifier = Modifier.fillMaxWidth()) },
                            onClick = {
                                onValueChange(it)
                                expanded = false
                            })
                    }
                else ->
                    allBrandList.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it, modifier = Modifier.fillMaxWidth()) },
                            onClick = {
                                onValueChange(it)
                                expanded = false
                            })
                    }

            }
        }
    }
}
