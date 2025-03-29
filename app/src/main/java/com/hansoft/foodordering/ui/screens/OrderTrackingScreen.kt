package com.hansoft.foodordering.ui.screens

import com.google.firebase.firestore.FirebaseFirestore
import com.hansoft.foodordering.data.model.Order

fun placeOrder(order: Order) {
    val db = FirebaseFirestore.getInstance()
    db.collection("orders")
        .document(order.orderId)
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
    db.collection("orders")
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
    db.collection("orders")
        .document(orderId)
        .addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null || !snapshot.exists()) return@addSnapshotListener
            val order = snapshot.toObject(Order::class.java)
            order?.let { onUpdate(it) }
        }
}