package com.example.motoaku

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.motoaku.database.fix.FixRepository
import com.example.motoaku.database.motorcycle.MotorcycleRepository
import com.example.motoaku.navigation.BottomNavigation
import com.example.motoaku.ui.theme.MotoAkuTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


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

    @ExperimentalMaterial3Api
    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.setContent {
//            viewModel = hiltViewModel()
            navController = rememberNavController()
            MotoAkuTheme {
                BottomNavigation(
                    navController = navController,
                )
            }
        }
    }

    @ExperimentalMaterial3Api
    @Test
    fun testButton() {
        composeTestRule.onNodeWithTag(TestTags.BOTTOMNAV_FAB).assertExists()
    }
}



class TestTest {
    @ExperimentalMaterial3Api
    @get:Rule
    val composeTestRule = createComposeRule()

    @ExperimentalMaterial3Api
    @Before
    fun setUp() {
        composeTestRule.setContent {
            MotoAkuTheme {
                TestScreen()
            }
        }
    }

    @ExperimentalMaterial3Api
    @Test
    fun testButton() {
        composeTestRule.onNodeWithTag(TestTags.TEST_BUTTON).assertExists()
        composeTestRule.onNodeWithTag(TestTags.TEST_BUTTON).assertTextContains("Test Button")
        composeTestRule.onNodeWithTag(TestTags.TEST_TEXT).assertTextContains("Test Text",false,true)
    }
}