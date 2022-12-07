package com.example.motoaku

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motoaku.database.fix.Fix
import com.example.motoaku.database.fix.FixRepository
import com.example.motoaku.database.motorcycle.Motorcycle
import com.example.motoaku.database.motorcycle.MotorcycleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repoFix: FixRepository,
    private val repoMoto: MotorcycleRepository
): ViewModel() {

    private val _uiMotoState = MutableStateFlow(
        Motorcycle(
            brand = "",
            model = "",
            imageRes = null,
            vin = null,
            yearBuilt = null
        ) )
    val uiMotoState: StateFlow<Motorcycle> = _uiMotoState.asStateFlow()

    var MotoList by mutableStateOf(listOf(Motorcycle(
        brand = "",
        model = "",
        imageRes = null,
        vin = null,
        yearBuilt = null)))
    var FixList by mutableStateOf(emptyList<Fix>())

    // [Main Screen]
    fun mainInit() {
        viewModelScope.launch {
            repoMoto.allMoto.collect {
                MotoList = it
            }
        }
    }

    // [Add Moto Screen]
    fun addMoto(motorcycle: Motorcycle) = viewModelScope.launch {
        repoMoto.insert(motorcycle)
    }

    // [Fix Screen]
    fun fixScreenInit() {
        viewModelScope.launch {
            repoMoto.allMoto.collect { MotoList = it }
        }
    }

    // [Add Fix Screen]
    fun addFixScreenInit() {
        viewModelScope.launch {
            repoMoto.allMoto.collect { MotoList = it }
        }
    }
    fun addFix(fix: Fix) = viewModelScope.launch {
        repoFix.insert(fix)
    }
}