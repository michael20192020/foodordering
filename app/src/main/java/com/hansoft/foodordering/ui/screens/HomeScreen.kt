package com.hansoft.foodordering.ui.screens

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hansoft.foodordering.data.model.MenuItem
import com.hansoft.foodordering.utils.CustomTopAppBar
import com.hansoft.foodordering.viewmodel.AuthViewModel
import com.hansoft.foodordering.viewmodel.CartViewModel
import com.hansoft.foodordering.viewmodel.MenuViewModel
import com.hansoft.foodordering.viewmodel.OrderViewModel


@Composable
fun HomeScreen(menuViewModel: MenuViewModel, orderViewModel: OrderViewModel,
               cartViewModel: CartViewModel, userId: String, onBackClick: () -> Unit,modifier: Modifier = Modifier) {
    var screen by remember { mutableStateOf("create_order") }
    var selectedItem by remember { mutableStateOf<MenuItem?>(null) }
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Online Food Ordering",
                onNavigationClick = onBackClick
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Text(text = "Home Screen", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            // Spacer(modifier = Modifier.height(16.dp))
            // Text(text = "Online Food Ordering", style = MaterialTheme.typography.headlineMedium)
            // Spacer(modifier = Modifier.height(16.dp))
            //  Text(text = "User ID: $userId", style = MaterialTheme.typography.bodyMedium)
            // Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    screen = "create_order"
                    menuViewModel.clearSelection()
                })
                { Text("Menu") }
                Button(onClick = { screen = "view_orders" })
                { Text("Orders") }
                Button(onClick = { screen = "view_cart" })
                { Text("Cart") }
            }
            Spacer(modifier = Modifier.height(6.dp))
            when (screen) {
                "view_orders" -> OrderListScreen(orderViewModel, userId)
                "create_order" -> MenuScreen(menuViewModel, orderViewModel, cartViewModel, userId,
                    {
                        screen = "Dine In"
                    },
                    { screen = "Takeaway" })

                "Dine In" -> DineInScreen(cartViewModel,orderViewModel,userId) { screen = "view_orders" }
                "Takeaway" -> TakeawayScreen(cartViewModel,orderViewModel,userId) { screen = "view_orders" }
            }
        }
    }
}