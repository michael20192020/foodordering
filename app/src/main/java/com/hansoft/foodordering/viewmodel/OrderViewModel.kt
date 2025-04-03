package com.hansoft.foodordering.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hansoft.foodordering.data.model.MenuItem
import com.hansoft.foodordering.data.model.Order
import com.hansoft.foodordering.data.model.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OrderViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders
   // private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"


    // Function to fetch user's orders
    fun getUserOrders(userId: String) {
        db.collection("ordersNew")
            .whereEqualTo("userId", userId)
            .orderBy("orderId", Query.Direction.DESCENDING) // Sort by id in descending order
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                val fetchedOrders = snapshot.documents.mapNotNull { it.toObject(Order::class.java) }
                _orders.value = fetchedOrders
            }
    }

    fun getNewID(callback: (Int) -> Unit) {
        var count = 0
        val collectionRef = db.collection("ordersNew") // Replace with your collection name

        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                count = querySnapshot.size() // Get the number of documents in the collection
                Log.d("Firestore", "Number of records in the 'orders' collection: $count")
                callback(count)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting record count: $e")
                callback(0)
            }

    }

    // Function to place an order
    fun placeOrder(order: Order, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("ordersNew")
            .document(order.orderId.toString())
            .set(order)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }
    }

    fun placeOrderNew(order: Order, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            /*
            val order2 = hashMapOf(
                "userId" to userId,
                "items" to order.items,
                "status" to "Pending",
                "totalPrice" to order.totalPrice,
                "timestamp" to order.timestamp       //FieldValue.serverTimestamp()
            )

             */

            try {
                //db.collection("orders").add(order).await()
                db.collection("ordersNew")
                .document(order.orderId.toString())
                    .set(order)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { exception -> onError(exception) }
                Log.d("aaa", "Order placed successfully")
            } catch (e: Exception) {
                Log.e("aaa", "Error placing order", e)
            }
        }
    }

    fun updateOrderStatus(orderId: String,status: OrderStatus,onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("ordersNew")
            .document(orderId)
            .update("status", status.displayName)
            .addOnSuccessListener {
                onSuccess()
                Log.d("aaa", "Order status updated successfully")
            }
            .addOnFailureListener { e ->
                onError(e)
                Log.e("aaa", "Error updating order status", e)
            }
    }

}