package com.example.motoaku.navigation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Motorcycle
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material.icons.outlined.Motorcycle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.motoaku.TestTags.BOTTOMNAV_FAB
import com.example.motoaku.TestTags.HOMESCREEN_CONTENTBUTTON_FIX
import com.example.motoaku.TestTags.HOMESCREEN_CONTENTBUTTON_MOTO
import com.example.motoaku.ViewModel
import com.example.motoaku.database.motorcycle.Motorcycle
import com.example.motoaku.ui.AddFixScreen
import com.example.motoaku.ui.AddMotoScreen
import com.example.motoaku.ui.HomeScreen

@ExperimentalMaterial3Api
@Composable
fun BottomNavigation(
    navController: NavHostController,
    vm: ViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var motoTracker by remember { mutableStateOf(Motorcycle.emptyMoto) }
    LaunchedEffect(Unit) {
        vm.mainInit(returnFromAddFix = motoTracker != Motorcycle.emptyMoto)
    }

    Scaffold(
        floatingActionButton = {
            FAB(
                vm = vm,
                navController = navController,
                navBackStackEntry = navBackStackEntry,
            ) {
                motoTracker = vm.motoTracker
            }
        }
    ) { innerPadding ->
        GraphRoot(
            navController = navController,
            innerPadding = innerPadding,
            vm = vm
        )
    }
}

@Composable
fun FAB(
    vm: ViewModel,
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry?,
    motoTrackerChange: () -> Unit
) {
    Visibility( when {
        navBackStackEntry?.destination?.route == Screen.Home.name &&
                vm.contentTracker == Content.Fix &&
                vm.MotoList.isEmpty() -> false
        navBackStackEntry?.destination?.route == Screen.Home.name -> true
        else -> false
    }) {
        FloatingActionButton(
            modifier = Modifier.padding(10.dp)
                .testTag(BOTTOMNAV_FAB),
            onClick = {
                if (vm.contentTracker.equals(Content.Moto)) {
                    navController.navigate(Screen.AddMoto.name)
                }
                else if (vm.contentTracker.equals(Content.Fix) && vm.MotoList.isNotEmpty()) {
                    motoTrackerChange()
                    navController.navigate(Screen.AddFix.name+"/?motoId=${vm.motoTracker.mId}")
                }
            },
        ) { Icon(vm.contentTracker.iconUnselected?:Icons.Filled.Add, vm.contentTracker.iconUnselected?.name) }
    }
}

@ExperimentalMaterial3Api
@Composable
fun GraphRoot(
    navController: NavHostController,
    innerPadding: PaddingValues,
    vm: ViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        composable(route = Screen.Home.name) {
            HomeScreen(
                navController = navController,
                vm = vm
            )
        }

        composable(route = Screen.AddMoto.name) {
            AddMotoScreen(navController)
        }

        composable(
            route = Screen.AddFix.name+"/?motoId={motoId}",
            arguments = listOf(navArgument("motoId") {
                defaultValue = 0
                type = NavType.IntType
            } )
        ) { navBackStackEntry ->
            val motoId = navBackStackEntry.arguments?.getInt("motoId")
            motoId?.let { AddFixScreen(navController,it) }
        }
    }
}

enum class Screen {
    Home,AddMoto,AddFix
}

enum class Content(
    val iconSelected: ImageVector?,
    val iconUnselected: ImageVector?,
    val testTag: String
) {
    Moto(Icons.Filled.Motorcycle,Icons.Outlined.Motorcycle,HOMESCREEN_CONTENTBUTTON_MOTO),
    Fix(Icons.Filled.Handyman,Icons.Outlined.Handyman, HOMESCREEN_CONTENTBUTTON_FIX),
}

@Composable
fun Visibility(value: Boolean, content: @Composable AnimatedVisibilityScope.() -> Unit) {
    Icons.Filled.Motorcycle.name
    AnimatedVisibility(
        visible = value,
        enter = EnterTransition.None,
        exit = ExitTransition.None,
        content = content
    )
}


