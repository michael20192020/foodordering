package com.hansoft.foodordering

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hansoft.foodordering.data.model.MenuItem
import com.hansoft.foodordering.ui.screens.HomeScreen
import com.hansoft.foodordering.ui.screens.LoginScreen
import com.hansoft.foodordering.ui.screens.MenuScreen
import com.hansoft.foodordering.ui.screens.OrderListScreen
import com.hansoft.foodordering.ui.screens.PlaceOrderScreen
import com.hansoft.foodordering.ui.screens.SignUpScreen
import com.hansoft.foodordering.ui.theme.FoodOrderingTheme
import com.hansoft.foodordering.viewmodel.AuthViewModel
import com.hansoft.foodordering.viewmodel.MenuViewModel
import com.hansoft.foodordering.viewmodel.OrderViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        // Initialize ViewModels
        val menuViewModel: MenuViewModel by viewModels()
        val orderViewModel: OrderViewModel by viewModels()
        val authViewModel: AuthViewModel by viewModels()

        setContent {
            val userId = remember { "user_12345" }
            val navController = rememberNavController()
            FoodOrderingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(authViewModel,menuViewModel, orderViewModel, navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun MainScreen(menuViewModel: MenuViewModel, orderViewModel: OrderViewModel, userId: String,modifier: Modifier = Modifier) {
    var screen by remember { mutableStateOf("orders") }
    var selectedItem by remember { mutableStateOf<MenuItem?>(null) }

    Column {
        Row {
            Button(onClick = { screen = "orders" }) { Text("View Orders") }
            Button(onClick = { screen = "place_order" }) { Text("Place Order") }
        }

        when (screen) {
            "orders" -> OrderListScreen(orderViewModel, userId)
            "place_order" ->

                if (selectedItem == null) {
                   // MenuScreen(menuViewModel,orderViewModel.newid, orderViewModel.placeOrder()) {

                } else {
                    PlaceOrderScreen(orderViewModel, menuViewModel, userId)
                }
             //   PlaceOrderScreen(viewModel, userId)
        }
    }
}

@Composable
fun AppNavigation(authViewModel: AuthViewModel,menuViewModel: MenuViewModel, orderViewModel: OrderViewModel,navController: NavHostController,modifier: Modifier = Modifier) {

    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(authViewModel,navController) }
        composable("main/{userId}") {  backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            HomeScreen(menuViewModel, orderViewModel, userId) }
        composable("signup") { SignUpScreen(authViewModel,navController) }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FoodOrderingTheme {
        Greeting("Android")
    }
}