package com.hansoft.foodordering.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.hansoft.foodordering.data.model.Order


@Composable
fun OrderTrackingScreen(orderId: String,modifier: Modifier = Modifier) {
    val db = FirebaseFirestore.getInstance()
    val orderStatus = remember { mutableStateOf("Pending") }
    val orderState = remember { mutableStateOf<Order?>(null) }

    LaunchedEffect(orderId) {
        val orderRef = db.collection("ordersNew").document(orderId)

        orderRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("Firestore", "Error fetching order status", error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                orderStatus.value = snapshot.getString("status") ?: "Pending"
                orderState.value = snapshot.toObject(Order::class.java)
            }
        }
    }

    orderState.value?.let { order ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Order ID: ${orderId}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Status: ${order.status}", fontSize = 16.sp)

            // Animated Progress Bar
            val progress = when (order.status) {
                "Pending" -> 0.1f
                "In Progress" -> 0.4f
                "Out for Delivery" -> 0.7f
                "Delivered" -> 1.0f
                else -> 0.0f
            }

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                color = Color.Green
            )
        }
    } ?: Text("Loading order details...", modifier = Modifier.padding(16.dp))

   // OrderProgressUI(orderStatus.value)
}

@Composable
fun OrderProgressUI(orderStatus: String) {
    val progress = when (orderStatus) {
        "Pending" -> 0.2f
        "In Progress" -> 0.5f
        "Out for Delivery" -> 0.8f
        "Delivered" -> 1.0f
        else -> 0.0f
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Order Status: $orderStatus", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())
    }
}

fun updateOrderStatus(orderId: String, newStatus: String) {
    val db = FirebaseFirestore.getInstance()
    db.collection("ordersNew").document(orderId)
        .update("status", newStatus)
        .addOnSuccessListener {
            Log.d("Firestore", "Order status updated to $newStatus")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error updating order status", e)
        }
}

fun placeOrder(order: Order) {
    val db = FirebaseFirestore.getInstance()
    db.collection("ordersNew")
        .document(order.orderId.toString())
        .set(order)
        .addOnSuccessListener {
            println("Order placed successfully")
        }
        .addOnFailureListener {
            println("Error placing order: ${it.message}")
        }
}


fun getUserOrders(userId: String, onResult: (List<Order>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("ordersNew")
        .whereEqualTo("userId", userId)
        .get()
        .addOnSuccessListener { snapshot ->
            val orders = snapshot.documents.map { it.toObject(Order::class.java)!! }
            onResult(orders)
        }
        .addOnFailureListener {
            println("Error fetching orders: ${it.message}")
        }
}

fun listenForOrderUpdates(orderId: String, onUpdate: (Order) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("ordersNew")
        .document(orderId)
        .addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null || !snapshot.exists()) return@addSnapshotListener
            val order = snapshot.toObject(Order::class.java)
            order?.let { onUpdate(it) }
        }
}