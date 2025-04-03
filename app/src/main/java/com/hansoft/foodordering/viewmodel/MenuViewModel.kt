package com.hansoft.foodordering.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hansoft.foodordering.data.model.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MenuViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val menuItems: StateFlow<List<MenuItem>> = _menuItems
    private val _selectedItems = MutableStateFlow<MutableList<MenuItem>>(mutableListOf())
    val selectedItems: StateFlow<List<MenuItem>> = _selectedItems
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"
    init {
       // fetchMenuItems()
       fetchMenuItemsCoroutine()
    }

    fun clearSelection() {
        _selectedItems.value = mutableListOf() // Replace with a new list to trigger recomposition
    }

    private fun fetchMenuItems() {
        db.collection("menuItems")
            .get()
            .addOnSuccessListener { result ->
                val items = result.documents.map { doc ->
                    MenuItem(
                        //id = doc.id.toIntOrNull() ?: 0,
                        name = doc.getString("name") ?: "",
                        description = doc.getString("description") ?: "",
                        price = doc.getDouble("price") ?: 0.0,
                        imageUrl = doc.getString("imageUrl") ?: ""
                    )
                }
                _menuItems.value = items
            }
            .addOnFailureListener { exception ->
                Log.d("aaa", "Error fetching menu: ${exception.message}")
            }
    }

    fun getNewID(callback: (Int) -> Unit) {
        var count = 0
        val collectionRef = db.collection("menuItems") // Replace with your collection name

        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                count = querySnapshot.size() + 1 // Get the number of documents in the collection
                Log.d("aaa", "Number of records in the 'menuItems' collection: $count")
                callback(count)
            }
            .addOnFailureListener { e ->
                Log.e("aaa", "Error getting record count: $e")
                callback(0)
            }

    }

    fun fetchMenuItemsCoroutine() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("menuItems").get().await()
                val items = snapshot.documents.map { doc ->
                    MenuItem(
                        //id = doc.id.toInt(),
                        name = doc.getString("name") ?: "",
                        description = doc.getString("description") ?: "",
                        price = doc.getDouble("price") ?: 0.0,
                        imageUrl = doc.getString("imageUrl") ?: ""
                    )
                }
                _menuItems.value = items
            } catch (e: Exception) {
                Log.d("aaa", "Error fetching menu items: ${e.message}")
            }
        }
    }

    fun addMenuItem(menuItem: MenuItem,  onSuccess: () -> Unit, onError: (Exception) -> Unit) {

        db.collection("menuItems")
            .add(menuItem)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                exception -> onError(exception)
            }
    }

    fun placeOrder(selectedItems: List<MenuItem>) {
        viewModelScope.launch {
            val order = hashMapOf(
                "userId" to userId,
                "items" to selectedItems.map { mapOf("name" to it.name, "price" to it.price) },
                "status" to "Pending",
                "timestamp" to FieldValue.serverTimestamp()
            )

            try {
                db.collection("ordersNew").add(order).await()
                Log.d("Order", "Order placed successfully")
            } catch (e: Exception) {
                Log.e("Order", "Error placing order", e)
            }
        }
    }

    fun toggleSelection(item: MenuItem) {
        val currentList = _selectedItems.value.toMutableList()
        if (currentList.contains(item)) {
            currentList.remove(item)
        } else {
            currentList.add(item)
        }
        _selectedItems.value = currentList
    }
}