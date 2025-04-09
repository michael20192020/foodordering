package com.hansoft.foodordering.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.hansoft.foodordering.data.model.CartItem
import com.hansoft.foodordering.data.model.Order
import com.hansoft.foodordering.viewmodel.OrderViewModel

@Composable
fun CheckoutScreen(
    cartItems: List<CartItem>,
    userId: String,
    orderViewModel: OrderViewModel,
    onOrderPlaced: () -> Unit
) {
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Delivery Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (address.isBlank() || phoneNumber.isBlank()) {
                    message = "Please fill all fields"
                    return@Button
                }

                orderViewModel.getNewID { orderId ->
                    val order = Order(
                        orderId = orderId,
                        userId = userId,
                        items = cartItems.map { "${it.name} x${it.quantity}" + "   $" + (it.quantity * it.price).toString() + " \n" },
                        totalPrice = cartItems.sumOf { it.quantity * it.price },
                        address = address,
                        phoneNumber = phoneNumber,
                        timestamp = Timestamp.now()
                    )

                    orderViewModel.placeOrderNew(
                        order,
                        onSuccess = {
                            message = "Order placed successfully!"
                            onOrderPlaced()
                        },
                        onError = {
                            message = "Error: ${it.message}"
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Place Order")
        }

        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = message, color = Color.Red)
        }
    }
}