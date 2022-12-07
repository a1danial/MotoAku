package com.example.motoaku.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.motoaku.ViewModel
import com.example.motoaku.database.fix.Fix
import com.example.motoaku.database.motorcycle.Motorcycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FixScreen(
    vm: ViewModel = hiltViewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedMoto by remember { mutableStateOf(Motorcycle(0,"", "", null, null, null)) }
    var rowSize by remember { mutableStateOf(Size.Zero) }
    LaunchedEffect(vm.MotoList) {
        vm.fixScreenInit()
        selectedMoto = if (vm.MotoList.isNotEmpty()) vm.MotoList[0] else Motorcycle(0,"", "", null, null, null)
    }

    Box(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 30.dp)
            .onGloballyPositioned { rowSize = it.size.toSize() }
    ) {
        MotoCard(selectedMoto) {
            expanded = !expanded
        }
        DropdownMenu(
            expanded = expanded,
            properties = PopupProperties(),
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { rowSize.width.toDp() }),
        ) {
            vm.MotoList.forEach { moto ->
                DropdownMenuItem(
                    text = { Text(text = (if (moto.imageRes != null) moto.imageRes.brandName+" - " else "") + moto.model,
                        modifier = Modifier.fillMaxWidth()) },
                    leadingIcon = {
                        if (moto.imageRes != null) {
                            Image(painterResource(moto.imageRes.imageRes),
                                null,
                                Modifier.size(24.dp)) } },
                    onClick = {
                        selectedMoto = moto
                        expanded = false
                    })
            }
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxHeight(7f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 30.dp)
    ) {
        items(
            items = vm.FixList,
        ) { fix -> FixCard(fix = fix)}
    }
}

@ExperimentalMaterial3Api
@Composable
fun FixCard(
    fix: Fix,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {

    }
}