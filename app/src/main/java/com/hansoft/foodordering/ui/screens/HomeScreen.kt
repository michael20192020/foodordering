package com.hansoft.foodordering.ui.screens

import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hansoft.foodordering.data.model.MenuItem
import com.hansoft.foodordering.viewmodel.AuthViewModel
import com.hansoft.foodordering.viewmodel.MenuViewModel
import com.hansoft.foodordering.viewmodel.OrderViewModel


@Composable
fun HomeScreen(menuViewModel: MenuViewModel, orderViewModel: OrderViewModel, userId: String, modifier: Modifier = Modifier) {
    var screen by remember { mutableStateOf("create_order") }
    var selectedItem by remember { mutableStateOf<MenuItem?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))
       // Text(text = "Home Screen", fontSize = 24.sp, fontWeight = FontWeight.Bold)
       // Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Online Food Ordering", style = MaterialTheme.typography.headlineMedium)
       // Spacer(modifier = Modifier.height(16.dp))
      //  Text(text = "User ID: $userId", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        )  {
            Button(onClick = {
                screen = "create_order"
                menuViewModel.clearSelection()
            }) { Text("Create Order") }
            Button(onClick = { screen = "view_orders" }) { Text("View Orders") }
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (screen) {
            "view_orders" -> OrderListScreen(orderViewModel, userId)
            "create_order" ->

                if (selectedItem == null) {
                    MenuScreen(menuViewModel,orderViewModel,userId) {
                        screen = "view_orders"
                    }
                } else {
                   // Log.d("aaa", "PlaceOrderScreen")
                   // PlaceOrderScreen(orderViewModel, menuViewModel, userId)
                }

        }
    }
}