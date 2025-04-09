package com.hansoft.foodordering.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.google.firebase.Timestamp
import com.hansoft.foodordering.data.model.CartItem
import com.hansoft.foodordering.data.model.Order
import com.hansoft.foodordering.viewmodel.CartViewModel
import com.hansoft.foodordering.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DineInScreen(cartViewModel: CartViewModel, orderViewModel: OrderViewModel,userId: String, onCheckout: () -> Unit) {

    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalPrice = cartItems.sumOf { it.quantity * it.price }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Dine In", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        LazyColumn {
            items(cartItems) { item ->
                SwipeToDismiss(
                    state = rememberDismissState(DismissValue.Default),
                    background = { Color.Red },
                    dismissContent = {
                        DineInItemView(
                            item = item,
                            onDeleteClick = { cartViewModel.deleteCartItem(item.id) },
                            onReduceQuantityClick = { cartViewModel.reduceItemQuantity(item.id)},
                            onIncreaseQuantityClick = { cartViewModel.increaseItemQuantity(item.id)}
                        )
                    }
                )
            }
        }
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

@Composable
fun DineInItemView(item: CartItem, onDeleteClick: () -> Unit, onReduceQuantityClick: () -> Unit,
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