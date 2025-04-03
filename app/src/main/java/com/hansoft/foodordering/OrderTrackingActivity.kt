package com.hansoft.foodordering

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hansoft.foodordering.ui.screens.OrderTrackingScreen
import com.hansoft.foodordering.ui.theme.FoodOrderingTheme

class OrderTrackingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        val orderId = intent.getStringExtra("orderId") ?: return
        setContent {
            FoodOrderingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OrderTrackingScreen(orderId,modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    FoodOrderingTheme {
        Greeting2("Android")
    }
}