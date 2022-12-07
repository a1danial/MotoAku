package com.example.motoaku.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.motoaku.ViewModel
import com.example.motoaku.database.motorcycle.Motorcycle
import com.example.motoaku.navigation.Screen

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    navController: NavHostController,
    vm: ViewModel = hiltViewModel()
) {
    // Moto/Fix content variables
    val bottomScreens = listOf(Screen.Moto, Screen.Fix)
    var selectedIndex by remember { mutableStateOf(Screen.Moto) }

    // Dropdown Menu variables
    var expanded by remember { mutableStateOf(false) }
    var selectedMoto by remember { mutableStateOf(Motorcycle(0,"", "", null, null, null)) }
    var rowSize by remember { mutableStateOf(Size.Zero) }

    LaunchedEffect(vm.MotoList) {
        vm.mainInit()
        selectedMoto = if (vm.MotoList.isNotEmpty()) vm.MotoList[0] else Motorcycle(0,"", "", null, null, null)
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Moto Selector
        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 30.dp, bottom = 30.dp)
                .onGloballyPositioned { rowSize = it.size.toSize() }
        ) {
            SelectedMotoCard(selectedMoto) {
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

        // Moto/Fix Content
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                bottomScreens.forEach { screen ->
                    Button(
                        modifier = Modifier.size(width = 125.dp, height = 35.dp),
                        onClick = { selectedIndex = screen },
                        shape = RoundedCornerShape(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor =  if (selectedIndex == screen) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.primaryContainer.copy(0.3f),
                            contentColor = if (selectedIndex == screen) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onPrimary.copy(0.5f)
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            if (selectedIndex == screen) screen.iconSelected2!! else screen.iconUnselected2!!,
                            null)
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(text = screen.name)
                    }
                }
            }

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 30.dp)
            ) {
               item {
                   Text(text = "VIN : ${selectedMoto.vin?:""}")
                   Text(text = "Year Built : ${selectedMoto.yearBuilt?.toString() ?: ""}")
               }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun SelectedMotoCard(
    moto: Motorcycle,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (moto.imageRes != null) {
                Image(
                    painter = painterResource(id = moto.imageRes.imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Column {
                Text(text = moto.brand,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(text = moto.model,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

}