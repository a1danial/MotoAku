package com.example.motoaku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.motoaku.TestTags.TEST_BUTTON
import com.example.motoaku.TestTags.TEST_SURFACE
import com.example.motoaku.TestTags.TEST_TEXT
import com.example.motoaku.navigation.BottomNavigation
import com.example.motoaku.navigation.GraphRoot
import com.example.motoaku.ui.theme.MotoAkuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            MotoAkuTheme {
                BottomNavigation(navController)
            }
        }
    }
}

@Composable
fun TestScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .testTag(TEST_SURFACE)
        .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier.testTag(TEST_BUTTON),
            onClick = { /*TODO*/ }) {
            Text(text = "Test Button")
        }
        Text(text = "Test Text",modifier = Modifier.testTag(TEST_TEXT))
    }
}

@Preview
@Composable
fun testPreview() {
    TestScreen()
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MotoAkuTheme {
        Greeting("Android")
    }
}