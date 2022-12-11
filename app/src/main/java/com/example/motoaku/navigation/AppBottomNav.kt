package com.example.motoaku.navigation

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.motoaku.TestTags.BOTTOMNAV_FAB
import com.example.motoaku.ViewModel
import com.example.motoaku.database.motorcycle.Motorcycle
import com.example.motoaku.ui.*

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
            Visibility( when {
                navBackStackEntry?.destination?.route == Screen.Home.name &&
                        vm.contentTracker == Content.Fix &&
                        vm.MotoList.isEmpty() -> false
                navBackStackEntry?.destination?.route == Screen.Home.name -> true
                else -> false }
            ) {
                FloatingActionButton(
                    modifier = Modifier.padding(10.dp)
                        .testTag(BOTTOMNAV_FAB),
                    onClick = {
                        if (vm.contentTracker.equals(Content.Moto)) {
                            navController.navigate(Screen.AddMoto.name)
                        }
                        else if (vm.contentTracker.equals(Content.Fix) && vm.MotoList.isNotEmpty()) {
                            motoTracker = vm.motoTracker
                            navController.navigate(Screen.AddFix.name+"/?motoId=${vm.motoTracker.mId}")
                        }
                    },
                ) { Icon(vm.contentTracker.iconSelected?:Icons.Filled.Add, null) }
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
) {
    Moto(Icons.Filled.Motorcycle,Icons.Outlined.Motorcycle),
    Fix(Icons.Filled.Handyman,Icons.Outlined.Handyman),
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


