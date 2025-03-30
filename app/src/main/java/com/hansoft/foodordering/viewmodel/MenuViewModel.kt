package com.hansoft.foodordering.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.hansoft.foodordering.data.model.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MenuViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val menuItems: StateFlow<List<MenuItem>> = _menuItems
    private val _selectedItems = MutableStateFlow<MutableList<MenuItem>>(mutableListOf())
    val selectedItems: StateFlow<List<MenuItem>> = _selectedItems
    init {
        fetchMenuItems()
    }

    private fun fetchMenuItems() {
        db.collection("menuItems")
            .get()
            .addOnSuccessListener { result ->
                val items = result.documents.map { doc ->
                    MenuItem(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
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