package com.hansoft.foodordering.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.hansoft.foodordering.data.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders



    // Function to fetch user's orders
    fun getUserOrders(userId: String) {
        db.collection("orders")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                val fetchedOrders = snapshot.documents.mapNotNull { it.toObject(Order::class.java) }
                _orders.value = fetchedOrders
            }
    }

    fun getNewID(callback: (String) -> Unit) {
        var count = 0
        val collectionRef = db.collection("orders") // Replace with your collection name

        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                count = querySnapshot.size() // Get the number of documents in the collection
                Log.d("Firestore", "Number of records in the 'orders' collection: $count")
                callback(count.toString())
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting record count: $e")
                callback("0")
            }

    }

    // Function to place an order
    fun placeOrder(order: Order, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("orders")
            .document(order.orderId)
            .set(order)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }
    }
}