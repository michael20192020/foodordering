package com.hansoft.foodordering.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.hansoft.foodordering.data.model.MenuItem
import androidx.compose.foundation.lazy.items
import com.hansoft.foodordering.data.model.Order

@Composable
fun MenuScreen() {
    var menuItems by remember { mutableStateOf<List<MenuItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        getMenuItems { items ->
            menuItems = items
        }
    }

    LazyColumn {
        items(menuItems) { item ->
            MenuItemCard(item)
        }
    }
}

val db = FirebaseFirestore.getInstance()

@Composable
fun MenuItemCard(item: MenuItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "$${item.price}", fontSize = 16.sp, color = Color.Gray)
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }
    }
}

fun getMenuItems(onResult: (List<MenuItem>) -> Unit) {
   // val db = FirebaseFirestore.getInstance()

    db.collection("menuItems")
        .get()
        .addOnSuccessListener { documents ->
            val items = documents.map { document ->
                MenuItem(
                    id = document.id,
                    name = document.getString("name") ?: "",
                    price = document.getDouble("price") ?: 0.0,
                    imageUrl = document.getString("imageUrl") ?: "",
                    category = document.getString("category") ?: ""
                )
            }
            onResult(items)
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error getting menu items", e)
        }
}




fun addMenuItem() {
    val menuItem = hashMapOf(
        "name" to "Margherita Pizza",
        "price" to 12.99,
        "imageUrl" to "https://example.com/pizza.jpg",
        "category" to "Pizza",
        "restaurantId" to "12345"
    )

    db.collection("menuItems")
        .add(menuItem)
        .addOnSuccessListener { documentReference ->
            Log.d("Firestore", "MenuItem added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error adding menu item", e)
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

fun deleteMenuItem(itemId: String) {
   // val db = FirebaseFirestore.getInstance()
    db.collection("menuItems").document(itemId)
        .delete()
        .addOnSuccessListener {
            Log.d("Firestore", "Item deleted successfully")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error deleting item", e)
        }
}

