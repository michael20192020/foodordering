package com.hansoft.foodordering.viewmodel

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hansoft.foodordering.data.model.MenuItem
import com.hansoft.foodordering.ui.screens.CART_TYPE_KEY
import com.hansoft.foodordering.ui.screens.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MenuViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val menuItems: StateFlow<List<MenuItem>> = _menuItems
    private val _selectedItems = MutableStateFlow<MutableList<MenuItem>>(mutableListOf())
    val selectedItems: StateFlow<List<MenuItem>> = _selectedItems
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"
 //   private val _cartItems = MutableStateFlow<List<MenuItem>>(emptyList())
 //   val cartItems: StateFlow<List<MenuItem>> = _cartItems
  //  private val _categoryItems = MutableStateFlow<List<String>>(emptyList())
  //  val categoryItems: StateFlow<List<String>> = _categoryItems
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
                        category = doc.getString("category") ?: "",
                        price = doc.getDouble("price") ?: 0.0,
                        imageUrl = doc.getString("imageUrl") ?: ""
                    )
                }
                _menuItems.value = items
               // _categoryItems.value = items.map { it.category }.distinct()
            }
            .addOnFailureListener { exception ->
                Log.d("aaa", "Error fetching menu: ${exception.message}")
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
                        category = doc.getString("category") ?: "",
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

    fun readCartType(context: Context): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[CART_TYPE_KEY] ?: "dine in"  }

    }

    fun saveCartType(context: Context, value: String) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[CART_TYPE_KEY] = value
            }
        }
    }

    /*
    suspend fun saveCartType(context: Context, value: String) {
        context.dataStore.edit { preferences ->
            preferences[CART_TYPE_KEY] = value
        }
    }

     */

    fun updateMenuItemByMenuId(menuItem: MenuItem,onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("menuItems")
            .whereEqualTo("id", menuItem.id)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.d("Firestore", "No item found with menuId: ${menuItem.id}")
                    return@addOnSuccessListener
                }

                for (document in documents) {
                    db.collection("menuItems").document(document.id)
                        .set(menuItem) // Replace the whole document with new data
                        .addOnSuccessListener {
                            Log.d("Firestore", "Menu item with id=${menuItem.id} updated successfully")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error updating item with id=${menuItem.id}", e)
                            onError(e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error finding item by menuId", e)
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
      //  Log.d("aaa", "Selected items: ${selectedItems.value}")
    }

    fun deleteMenuItem(itemId: String) {

        db.collection("menuItems").document(itemId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Item deleted successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error deleting item", e)
            }
    }

    fun updateMenuItem(itemId: String, newName: String) {
        // val db = FirebaseFirestore.getInstance()
        db.collection("menuItems").document(itemId)
            .update("name", newName)
            .addOnSuccessListener {
                Log.d("Firestore", "Item updated successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating item", e)
            }
    }

    /*
    fun addToCart(item: MenuItem) {
        _cartItems.value = _cartItems.value + item
    }

    fun removeFromCart(item: MenuItem) {
        _cartItems.value = _cartItems.value - item
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

     */

    fun searchMenuItemById(id: Int, onResult: (MenuItem?) -> Unit) {
        db.collection("menuItems")
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener { result ->
                val item = result.documents.firstOrNull()?.toObject(MenuItem::class.java)
                onResult(item)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}