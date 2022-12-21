package com.example.motoaku

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material.icons.outlined.Motorcycle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.motoaku.TestTags.BOTTOMNAV_FAB
import com.example.motoaku.TestTags.HOMESCREEN_CONTENTBUTTON_FIX
import com.example.motoaku.TestTags.HOMESCREEN_SELECTEDMOTOCARD
import com.example.motoaku.database.fix.FixRepository
import com.example.motoaku.database.motorcycle.Motorcycle
import com.example.motoaku.database.motorcycle.MotorcycleRepository
import com.example.motoaku.navigation.BottomNavigation
import com.example.motoaku.navigation.Content
import com.example.motoaku.navigation.FAB
import com.example.motoaku.navigation.Screen
import com.example.motoaku.ui.SelectedMotoCard
import com.example.motoaku.ui.theme.MotoAkuTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalMaterial3Api
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @ExperimentalMaterial3Api
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var repoMoto: MotorcycleRepository
    lateinit var repoFix: FixRepository

    private lateinit var navController: NavHostController
    private lateinit var viewModel: ViewModel

    @ExperimentalMaterial3Api
    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.setContent {
            viewModel = hiltViewModel()
            navController = rememberNavController()
            MotoAkuTheme {
                BottomNavigation(
                    navController = navController,
                    vm = viewModel
                )
            }
        }
    }

//    fun openAppFirstTime() { // TODO

    @Test
    fun When_appOpen_expect_displayHomeScreenAndShowMotoContent() {
        // FAB show Moto icon
        composeTestRule.onNodeWithTag(BOTTOMNAV_FAB).assertContentDescriptionEquals(Icons.Outlined.Motorcycle.name)
        // Content tracker variable is selected
        Assert.assertEquals(Content.Moto,viewModel.contentTracker)
    }

    @Test
    fun When_contentButtonPressed_expect_displayRelevantContent() {
        // Click fix content button
        composeTestRule.onNodeWithTag(HOMESCREEN_CONTENTBUTTON_FIX).performClick()
        // FAB show Moto icon
        composeTestRule.onNodeWithTag(BOTTOMNAV_FAB).assertContentDescriptionEquals(Icons.Outlined.Handyman.name)
        // Content tracker variable is selected
        Assert.assertEquals(Content.Fix,viewModel.contentTracker)

        // Click moto content button
        composeTestRule.onNodeWithTag(TestTags.HOMESCREEN_CONTENTBUTTON_MOTO).performClick()
        // FAB show Moto icon
        composeTestRule.onNodeWithTag(BOTTOMNAV_FAB).assertContentDescriptionEquals(Icons.Outlined.Motorcycle.name)
        // Content tracker variable is selected
        Assert.assertEquals(Content.Moto,viewModel.contentTracker)
    }

    @Test
    fun When_motoFABPressed_expect_navigateToAddMotoScreen() {
        // Click moto FAB
        composeTestRule.onNodeWithTag(BOTTOMNAV_FAB).performClick()
        // Determine if route is navigated to Moto screen
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(Screen.AddMoto.name,route)
    }
}

@ExperimentalMaterial3Api
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeScreenNavigateTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @ExperimentalMaterial3Api
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var repoMoto: MotorcycleRepository
    lateinit var repoFix: FixRepository

    private lateinit var navController: NavHostController
    private lateinit var viewModel: ViewModel

    @ExperimentalMaterial3Api
    @Test
    fun When_noMotoInDb_expect_selectedMotoCardEmpty() {
        // Simulate empty motolist
        composeTestRule.setContent {
            SelectedMotoCard(
                Motorcycle.emptyMoto
            )
        }
        // Text = '[, ]'
        composeTestRule.onNodeWithTag(HOMESCREEN_SELECTEDMOTOCARD).assertExists()
        composeTestRule.onNodeWithTag(HOMESCREEN_SELECTEDMOTOCARD).assertTextEquals("")
    }

    @ExperimentalMaterial3Api
    @Test
    fun When_noMotoInDb_expect_FABNotShown() {
        // FIXME
        // Simulate empty motolist
        hiltRule.inject()
        composeTestRule.setContent {
            navController = rememberNavController()
            viewModel = hiltViewModel()
            viewModel.contentTracker = Content.Fix
            viewModel.MotoList = emptyList<Motorcycle>()
            var navBackStackEntry = navController.currentBackStackEntry
            navBackStackEntry?.destination?.route = Screen.Home.name

            FAB(
                vm = viewModel,
                navController = navController,
                navBackStackEntry = navBackStackEntry
            ) {}
        }
        // Verify FAB is not shown
        composeTestRule.onNodeWithTag(BOTTOMNAV_FAB).assertDoesNotExist()
    }

    @ExperimentalMaterial3Api
    @Test
    fun When_motoInDBAndfixFABPressed_expect_navigateToAddFixScreen() {
        // FIXME
        // Simulate a non empty moto list
        hiltRule.inject()
        composeTestRule.setContent {
            viewModel = hiltViewModel()
            viewModel.MotoList = listOf(
                Motorcycle(
                    1,
                    "Honba",
                    "Scooty",
                    null, null, null
                )
            )
            navController = rememberNavController()
            MotoAkuTheme {
                BottomNavigation(
                    navController = navController,
                    vm = viewModel
                )
            }
        }

//        hiltRule.inject()
//        composeTestRule.setContent {
//            navController = rememberNavController()
//            viewModel = hiltViewModel()
//            viewModel.contentTracker = Content.Fix
//            viewModel.MotoList = listOf(
//                Motorcycle(
//                    1,
//                    "Honba",
//                    "Scooty",
//                    null, null, null
//                )
//            )
//            val navBackStackEntry by navController.currentBackStackEntryAsState()
//            FAB(
//                vm = viewModel,
//                navController = navController,
//                navBackStackEntry = navBackStackEntry
//            ) {}
//        }
        // Select fix content button
        composeTestRule.onNodeWithTag(HOMESCREEN_CONTENTBUTTON_FIX).performClick()
        // Click fix FAB
        composeTestRule.onNodeWithTag(BOTTOMNAV_FAB).assertExists() // FIXME Possibly due to button not showing
        composeTestRule.onNodeWithTag(BOTTOMNAV_FAB,useUnmergedTree = true).performClick()
        // Determine if route is navigated to Fix screen
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(Screen.AddFix.name+"/?motoId={motoId}",route)
    }
}