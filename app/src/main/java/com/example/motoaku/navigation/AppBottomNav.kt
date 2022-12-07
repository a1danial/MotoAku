package com.example.motoaku.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Motorcycle
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material.icons.outlined.Motorcycle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.motoaku.ViewModel
import com.example.motoaku.database.motorcycle.Motorcycle
import com.example.motoaku.ui.AddFixScreen
import com.example.motoaku.ui.AddMotoScreen
import com.example.motoaku.ui.FixScreen
import com.example.motoaku.ui.MotoScreen

@ExperimentalMaterial3Api
@Composable
fun BottomNavigation(
    navController: NavHostController,
    vm: ViewModel = hiltViewModel()
) {
    val screens = listOf(Screen.Moto, Screen.Fix)
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    bottomBarState.value = navBackStackEntry?.destination?.route == Screen.Moto.name ||
            navBackStackEntry?.destination?.route == Screen.Fix.name

    var selectedItem by remember { mutableStateOf(0) }

    LaunchedEffect(vm.MotoList) {
        vm.fixScreenInit()
    }

    Scaffold(
        bottomBar = {
            Hide(bottomBarState.value) {
                NavigationBar {
                    screens.forEachIndexed { index, screen ->
                        NavigationBarItem(
                            icon = { Icon(
                                if (index == selectedItem) screen.iconSelected2!! else screen.iconUnselected2!!,
                                null,
                            ) },
                            label = { Text(screen.name) },
                            selected = selectedItem == index,
                            onClick = {
                                navController.navigate(screen.name)
                                selectedItem = index
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
                        else if (navBackStackEntry?.destination?.route.equals(Screen.Fix.name) && vm.MotoList.isNotEmpty()) { // TODO dont allow ifno moto
                            navController.navigate(Screen.AddFix.name) }
                    },
                ) { Icon(Icons.Filled.Add, null) }
            }
        }
    ) { innerPadding ->
        GraphRoot(navController, innerPadding)
    }
}

@ExperimentalMaterial3Api
@Composable
fun GraphRoot(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.Moto.name,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(route = Screen.Moto.name) {
            MotoScreen()
        }

        composable(route = Screen.Fix.name) {
            FixScreen()
        }

        composable(route = Screen.AddMoto.name) {
            AddMotoScreen(navController)
        }

        composable(route = Screen.AddFix.name) {
            AddFixScreen(navController)
        }
    }
}

enum class Screen(
    val iconSelected2: ImageVector?,
    val iconUnselected2: ImageVector?,
) {
    Moto(Icons.Filled.Motorcycle,Icons.Outlined.Motorcycle),
    Fix(Icons.Filled.Handyman,Icons.Outlined.Handyman),
    AddMoto(null,null),
    AddFix(null,null)
}

@Composable
fun Hide(value: Boolean, content: @Composable AnimatedVisibilityScope.() -> Unit) {
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


