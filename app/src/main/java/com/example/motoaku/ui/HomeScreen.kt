package com.example.motoaku.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
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
import com.example.motoaku.database.fix.Fix
import com.example.motoaku.database.motorcycle.Motorcycle
import com.example.motoaku.navigation.Screen
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    navController: NavHostController,
    selectedScreen: Screen,
    onChangeSelectedScreen: (Screen) -> Unit,
    vm: ViewModel
) {
    // Moto/Fix content variables
    val bottomScreens = listOf(Screen.Moto, Screen.Fix)
    var rowContentSize by remember { mutableStateOf(Size.Zero) }
    // FIXME Fix content not correctly after adding fix, although button is correctly selected
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Dropdown Menu variables
    var expanded by remember { mutableStateOf(false) }
    var rowMenuSize by remember { mutableStateOf(Size.Zero) }

    LaunchedEffect(vm.MotoList) {
        vm.mainInit()
    }
    LaunchedEffect(vm.motoTracker) {  }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Moto Selector
        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 30.dp, bottom = 30.dp)
                .onGloballyPositioned { rowMenuSize = it.size.toSize() }
        ) {
            SelectedMotoCard(vm.motoTracker) {
                expanded = !expanded
            }
            DropdownMenu(
                expanded = expanded,
                properties = PopupProperties(),
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(with(LocalDensity.current) { rowMenuSize.width.toDp() }),
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
                            vm.motoTracker = moto
                            vm.mainShowMotoIdFixList(moto.mId)
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
                bottomScreens.forEachIndexed { index, screen ->
                    Button(
                        modifier = Modifier.size(width = 125.dp, height = 35.dp),
                        onClick = {
                            onChangeSelectedScreen(screen)
                            coroutineScope.launch { listState.animateScrollToItem(index = index) }
                                  },
                        shape = RoundedCornerShape(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor =  if (selectedScreen == screen) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.primaryContainer.copy(0.3f),
                            contentColor = if (selectedScreen == screen) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onPrimary.copy(0.5f)
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            if (selectedScreen == screen) screen.iconSelected2!! else screen.iconUnselected2!!,
                            null)
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(text = screen.name)
                    }
                }
            }
            Spacer(modifier = Modifier.size(20.dp))
            LazyRow(
                state = listState,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimary.copy(0.05f),
                        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .fillMaxSize()
                    .onGloballyPositioned { rowContentSize = it.size.toSize() },
                userScrollEnabled = false
            ) {
                item {
                    MotoContent(
                        vin = vm.motoTracker.vin,
                        yearBuilt = vm.motoTracker.yearBuilt,
                        rowContentSize = rowContentSize
                    )
                    FixContent(
                        rowContentSize = rowContentSize,
                        fixList = vm.FixList
                    )
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

@Composable
fun MotoContent(
    vin: String?,
    yearBuilt: Int?,
    rowContentSize: Size
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 30.dp),
        modifier = Modifier
            .fillMaxHeight()
            .width(with(LocalDensity.current) { rowContentSize.width.toDp() }),
    ) {
        item {
            Text(text = "VIN : ${vin?:""}")
            Text(text = "Year Built : ${yearBuilt?.toString() ?: ""}")
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun FixContent(
    rowContentSize: Size,
    fixList: List<Fix>
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 30.dp),
        modifier = Modifier
            .fillMaxHeight()
            .width(with(LocalDensity.current) { rowContentSize.width.toDp() }),
    ) {
        items(items = fixList) { fix ->
            Card(
                onClick = { /*TODO*/ }
            ) {
                Column(
                    modifier = Modifier.padding(15.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = fix.part, style = MaterialTheme.typography.labelMedium)
                    Row {
                        Text(text = "Last Service",
                            Modifier.weight(1f))
                        Text(text = "Next Service",
                            Modifier.weight(1f))
                    }
                    Row {
                        Text(text = SimpleDateFormat("EEE d MMM yyyy").format(fix.dateStart),
                            Modifier.weight(1f))
                        Text(text =
                        if (fix.dateEnd != null) SimpleDateFormat("EEE d MMM yyyy").format(fix.dateEnd)
                        else "",
                            Modifier.weight(1f))
                    }
                    Row {
                        Text(text = if (fix.odoStart != null) "${fix.odoStart} KM" else "",
                            Modifier.weight(1f))
                        Text(text = if (fix.odoStart != null) "${fix.odoEnd} KM" else "",
                            Modifier.weight(1f))
                    }
                }
            }
        }
    }
}