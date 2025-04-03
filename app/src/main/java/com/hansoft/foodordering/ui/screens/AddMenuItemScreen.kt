package com.hansoft.foodordering.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.hansoft.foodordering.data.model.MenuItem
import com.hansoft.foodordering.data.model.Order
import com.hansoft.foodordering.viewmodel.MenuViewModel

@Composable
fun AddMenuItemScreen(menuViewModel: MenuViewModel,  modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Menu Item", fontSize = 24.sp, fontWeight = FontWeight.Bold)



        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Item Name") }
        )



        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") }
        )

        Spacer(modifier = Modifier.height(8.dp))



        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Image URL") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val priceDouble = price.toDoubleOrNull() ?: 0.0
        Button(
            onClick = {

                menuViewModel.getNewID { count ->
                    // Use the `count` string here, for example, to generate a new order ID
                    Log.d("aaa", "The new menu item id is: $count")
                    //   newid = count

                    val menuItem = MenuItem(
                        id = count,
                        name = name,
                        description = description,
                        category = category,
                        price = priceDouble,
                        imageUrl = imageUrl)


                    menuViewModel.addMenuItem(menuItem,onSuccess = { message = "Add menu item successfully!" },
                        onError = { message = "Failed to add menu item: ${it.message}" })

                }


            },
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Adding..." else "Add Item")
        }
        if (message.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(message, color = Color.Green, modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
    
}
