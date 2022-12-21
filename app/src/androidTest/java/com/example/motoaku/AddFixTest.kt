package com.example.motoaku

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.motoaku.database.fix.FixRepository
import com.example.motoaku.database.motorcycle.MotorcycleRepository
import com.example.motoaku.navigation.BottomNavigation
import com.example.motoaku.navigation.Screen
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
class AddFixTest {
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

    @Test
    fun When_dataIsKeyedIn_expect_dataCorrectlyDisplayed() {
        composeTestRule.onNodeWithTag(TestTags.HOMESCREEN_CONTENTBUTTON_FIX).performClick()
        composeTestRule.onNodeWithTag(TestTags.BOTTOMNAV_FAB).performClick()
        // Amount input based on 2 decimal formatting
        composeTestRule.onNodeWithTag(TestTags.ADDFIX_INPUTCOSTPRICE) // TODO
    //        TODO Text input
        with(composeTestRule) {

        }
        // Navigate back to home page
        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(Screen.Home.name,route)
//        assertThat(route).isEqualTo(Route.Home.screen)
    }

//    fun When_addMotoSuccessful_expect_returnToHome&Database() { // TODO
//        TODO return Home
//        TODO data reflected in Moto table
}
