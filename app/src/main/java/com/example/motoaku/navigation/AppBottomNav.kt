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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.motoaku.ViewModel
import com.example.motoaku.ui.*

@ExperimentalMaterial3Api
@Composable
fun BottomNavigation(
    navController: NavHostController,
    vm: ViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var selectedIndex by remember { mutableStateOf(Screen.Moto) }

    Scaffold(
        floatingActionButton = {
            Visibility( when {
                navBackStackEntry?.destination?.route == Screen.Home.name &&
                        selectedIndex.equals(Screen.Fix) &&
                        vm.MotoList.isEmpty() -> false
                navBackStackEntry?.destination?.route == Screen.Home.name -> true
                else -> false }
            ) {
                FloatingActionButton(
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                        if (selectedIndex.equals(Screen.Moto)) {
                            navController.navigate(Screen.AddMoto.name)
                        }
                        else if (selectedIndex.equals(Screen.Fix) && vm.MotoList.isNotEmpty()) {
                            navController.navigate(Screen.AddFix.name+"/?motoId=${vm.motoTracker.mId}")
                            Log.i("FAB","motoTracker: $vm.motoTracker")
                        }
                    },
                ) { Icon(selectedIndex.iconSelected2?:Icons.Filled.Add, null) }
            }
        }
    ) { innerPadding ->
        GraphRoot(
            navController = navController,
            innerPadding = innerPadding,
            selectedIndex = selectedIndex,
            vm = vm
        ) { selectedIndex = it }
    }
}

@ExperimentalMaterial3Api
@Composable
fun GraphRoot(
    navController: NavHostController,
    innerPadding: PaddingValues,
    selectedIndex: Screen,
    vm: ViewModel,
    function: (Screen) -> Unit
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
                selectedScreen = selectedIndex,
                onChangeSelectedScreen = { function(it) },
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

enum class Screen(
    val iconSelected2: ImageVector?,
    val iconUnselected2: ImageVector?,
) {
    Home(null,null),
    Moto(Icons.Filled.Motorcycle,Icons.Outlined.Motorcycle),
    Fix(Icons.Filled.Handyman,Icons.Outlined.Handyman),
    AddMoto(null,null),
    AddFix(null,null)
}

@Composable
fun Visibility(value: Boolean, content: @Composable AnimatedVisibilityScope.() -> Unit) {
    AnimatedVisibility(
        visible = value,
        enter = EnterTransition.None,
        exit = ExitTransition.None,
        content = content
    )
}

/*@Composable
fun AppBottomNav(navController: NavHostController) {
    val screens = listOf(Screen.Moto, Screen.Fix)
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    bottomBarState.value = navBackStackEntry?.destination?.route == Screen.Moto.name ||
            navBackStackEntry?.destination?.route == Screen.Fix.name
//            navBackStackEntry?.destination?.route == NavItem.Accounts.navRoute ||
//            navBackStackEntry?.destination?.route == NavItem.Settings.navRoute
    Scaffold(
        bottomBar = {
            Hide(bottomBarState.value) {
                BottomNavigation {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    screens.forEach { screen ->
                        val selectedComparator = navBackStackEntry?.destination?.hierarchy?.any { it.route == screen.name } == true
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    if (selectedComparator) painterResource(screen.iconSelected!!) else painterResource(screen.iconUnselected!!),
                                    contentDescription = null
                                )
                            },
                            label = { Text(text = screen.name) },
                            selected = currentRoute == screen.name,
                            onClick = {
                                navController.navigate(screen.name) {
                                    navController.graph.startDestinationRoute?.let { screen_route ->
                                        popUpTo(screen_route) { saveState = true }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            Hide(bottomBarState.value) {
                FloatingActionButton(
                    onClick = {
                        if (navBackStackEntry?.destination?.route.equals(Screen.Moto.name)) {
                            navController.navigate(Screen.AddMoto.name) }
                        else if (navBackStackEntry?.destination?.route.equals(Screen.Fix.name)) {
                            navController.navigate(Screen.AddFix.name) }
                    },
                ) { Icon(painterResource(id = R.drawable.add), null) }
            }
        },
    ) { innerPadding ->
        GraphRoot(navController, innerPadding)
    }
}*/


