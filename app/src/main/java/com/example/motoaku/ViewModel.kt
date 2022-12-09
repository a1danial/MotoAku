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
import com.example.motoaku.navigation.Content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repoFix: FixRepository,
    private val repoMoto: MotorcycleRepository
): ViewModel() {
    var MotoList by mutableStateOf(listOf(Motorcycle.emptyMoto))

    // [Main Screen]
    var motoTracker by mutableStateOf(Motorcycle.emptyMoto)
    var contentTracker by mutableStateOf(Content.Moto)
    var FixList by mutableStateOf(emptyList<Fix>())
    fun mainInit(returnFromAddFix: Boolean) {
        viewModelScope.launch {
            repoMoto.allMoto.collect {
                MotoList = it
                if (it.isNotEmpty()) {
                    if (!returnFromAddFix) motoTracker =  it[0]
                    repoFix.fixFromMotoId(it[0].mId).collect {
                        FixList = it
                    }
                }
            }
        }
    }
    fun returnFromAddMoto() {
        viewModelScope.launch {
            repoMoto.allMoto.collect { MotoList = it }
        }
    }
    fun mainShowMotoIdFixList(motoId: Int) {
        viewModelScope.launch {
            repoFix.fixFromMotoId(motoId).collect { FixList = it }
        }
    }
    fun mainOnChangeSelectedScreen(content: Content) {
        contentTracker = content
    }

    // [Add Moto Screen]
    fun addMoto(motorcycle: Motorcycle) = viewModelScope.launch {
        repoMoto.insert(motorcycle)
    }

    // [Add Fix Screen]
    fun addFixScreenInit(motoId: Int) {
        viewModelScope.launch {
            repoMoto.allMoto.collect { list ->
                MotoList = list
                motoTracker = list.find { it.mId == motoId }?:Motorcycle.emptyMoto
            }
        }
    }
    fun addFix(fix: Fix) = viewModelScope.launch {
        repoFix.insert(fix)
    }
}