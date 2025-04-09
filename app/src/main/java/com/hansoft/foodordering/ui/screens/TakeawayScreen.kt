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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.hansoft.foodordering.data.model.CartItem
import com.hansoft.foodordering.data.model.CartItemEntity
import com.hansoft.foodordering.data.model.Order
import com.hansoft.foodordering.viewmodel.CartViewModel
import com.hansoft.foodordering.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakeawayScreen(cartViewModel: CartViewModel, orderViewModel: OrderViewModel, userId: String, onCheckout: () -> Unit) {
    var uiState = cartViewModel.cartkUiState.collectAsState()
    val cartItems = uiState.value.itemList
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    // val cartItems by cartViewModel.cartItems.collectAsState()
    val totalPrice = cartItems.sumOf { it.quantity * it.price }
    var message by remember { mutableStateOf("") }

    LazyColumn(modifier = Modifier.fillMaxSize()
                        .padding(16.dp)) {
        item {
            Text("Takeaway", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Delivery Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
      //  LazyColumn {
            items(cartItems) { item ->
                SwipeToDismiss(
                    state = rememberDismissState(DismissValue.Default),
                    background = { Color.Red },
                    dismissContent = {
                        TakeawayItemView(
                            item = item,
                            onDeleteClick = {
                                //cartViewModel.deleteCartItem(item.id)
                                cartViewModel.deleteCartItem(item)
                                            },
                            onReduceQuantityClick = {
                                //cartViewModel.reduceItemQuantity(item.id)
                                cartViewModel.reduceItemQuantity(item)
                                                    },
                            onIncreaseQuantityClick = {
                                //cartViewModel.increaseItemQuantity(item.id)
                                cartViewModel.increaseItemQuantity(item)
                            }
                        )
                    }
                )
            }
       // }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Total: $${String.format("%.2f", totalPrice)}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {


                    orderViewModel.getNewID { count ->
                        // Use the `count` string here, for example, to generate a new order ID
                        Log.d("aaa", "The new order ID is: $count")
                        //   newid = count

                        val order = Order(
                            orderId = count,
                            userId = userId,
                            //items = cartItems.map { it.name + "   $" + (it.quantity * it.price).toString() + " \n" },
                            items = cartItems.map { "${it.name} x${it.quantity}" + "   $" + (it.quantity * it.price).toString() + " \n" },
                            totalPrice = cartItems.sumOf { (it.quantity * it.price) },
                            name = name,
                            phoneNumber = phone,
                            address = address,
                            timestamp = Timestamp.now()
                        )

                        orderViewModel.placeOrderNew(
                            order,
                            onSuccess = {
                                message = "Order placed successfully!"
                                onCheckout()
                            },
                            onError = { message = "Failed to place order: ${it.message}" })


                    }


                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Checkout")
            }
        }
    }
}

@Composable
fun TakeawayItemView(item: CartItemEntity, onDeleteClick: () -> Unit, onReduceQuantityClick: () -> Unit,
                 onIncreaseQuantityClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("${item.name}")
                Spacer(modifier = Modifier.height(6.dp))
                Text("${item.quantity}")
                Spacer(modifier = Modifier.height(6.dp))
                Text("${String.format("%.2f", item.price * item.quantity)}")
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Buttons for reducing quantity and deleting item
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onIncreaseQuantityClick) {
                    Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
                }
                IconButton(onClick = onReduceQuantityClick) {
                    Icon(Icons.Default.Remove, contentDescription = "Reduce Quantity")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Item")
                }
            }
        }
    }
}