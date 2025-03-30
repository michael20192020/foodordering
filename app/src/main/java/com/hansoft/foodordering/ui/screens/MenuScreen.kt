package com.hansoft.foodordering.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import com.hansoft.foodordering.data.model.Order
import com.hansoft.foodordering.viewmodel.MenuViewModel
import com.hansoft.foodordering.viewmodel.OrderViewModel

@Composable
fun MenuScreen(viewModel: MenuViewModel, orderViewModel: OrderViewModel, userId: String, onProceedToOrder: () -> Unit) {
    // var menuItems by remember { mutableStateOf<List<MenuItem>>(emptyList()) }

    val menuItems by viewModel.menuItems.collectAsState()
    val selectedItems by viewModel.selectedItems.collectAsState()
    var message by remember { mutableStateOf("") }
    /*
    LaunchedEffect(Unit) {
        getMenuItems { items ->
            menuItems = items
        }
    }

     */
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Menu Screen", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(menuItems) { item ->
                MenuItemCard(item, viewModel::toggleSelection, selectedItems.contains(item))
            }
        }

        if (selectedItems.isNotEmpty()) {



            Button(
                onClick = {
                   // var newid = "0"
                    orderViewModel.getNewID { count ->
                        // Use the `count` string here, for example, to generate a new order ID
                        Log.d("Firestore", "The new order ID count is: $count")
                     //   newid = count

                        val order = Order(
                            orderId = count,
                            userId = userId,
                            items = selectedItems.map { it.name + "   $" + it.price.toString() + " \n" },
                            totalPrice = selectedItems.sumOf { it.price }
                        )

                        orderViewModel.placeOrder(
                            order,
                            onSuccess = { message = "Order placed successfully!" },
                            onError = { message = "Failed to place order: ${it.message}" })
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Proceed to Order (${selectedItems.size} items)")
            }

            if (message.isNotEmpty()) {
                Text(message, color = Color.Green, modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}

val db = FirebaseFirestore.getInstance()

@Composable
fun MenuItemCard(item: MenuItem, onItemSelected: (MenuItem) -> Unit,isSelected: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemSelected(item) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(modifier = Modifier.padding(16.dp),verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = item.name, fontWeight = FontWeight.Bold)
                Text(text = "$${item.price}", color = Color.Gray)
            }
            Checkbox(checked = isSelected, onCheckedChange = { onItemSelected(item) })
        }
        /*
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

         */
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

