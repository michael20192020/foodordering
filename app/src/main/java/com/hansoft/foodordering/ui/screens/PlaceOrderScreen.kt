package com.hansoft.foodordering.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.hansoft.foodordering.data.model.MenuItem
import com.hansoft.foodordering.data.model.Order
import com.hansoft.foodordering.viewmodel.MenuViewModel
import com.hansoft.foodordering.viewmodel.OrderViewModel
import kotlinx.coroutines.flow.last

@Composable
fun PlaceOrderScreen(orderViewModel: OrderViewModel, menuViewModel: MenuViewModel, userId: String) {
    var items by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    val selectedItems by menuViewModel.selectedItems.collectAsState()
    var message by remember { mutableStateOf("") }


    Column(modifier = Modifier.fillMaxSize().padding(16.dp),horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Confirm Order",fontSize = 20.sp, fontWeight = FontWeight.Bold)

        LazyColumn {
            items(selectedItems) { item ->
                Text("${item.name} - $${item.price}")
            }
        }

        val totalPrice = selectedItems.sumOf { it.price }
        Text("Total Price: $${String.format("%.2f", totalPrice)}",fontWeight = FontWeight.Bold, fontSize = 18.sp)


        Button(
            onClick = {
                val order = Order(
                    orderId = 0,
                    userId = userId,
                    items = selectedItems.map { it.name + "   " + it.price},
                    totalPrice = totalPrice,
                    timestamp = Timestamp.now()
                )
                //orderViewModel.placeOrder(order,onSuccess = { message = "Order placed successfully!" },onError = { message = "Failed to place order: ${it.message}"})
                orderViewModel.placeOrderNew(order,onSuccess = { message = "Order placed successfully!" },onError = { message = "Failed to place order: ${it.message}"})

            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Place Order")
        }

        if (message.isNotEmpty()) {
            Text(message, color = Color.Green, modifier = Modifier.padding(top = 16.dp))
        }


    }

}
