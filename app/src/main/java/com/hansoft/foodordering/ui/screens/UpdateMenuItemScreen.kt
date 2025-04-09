package com.hansoft.foodordering.ui.screens

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansoft.foodordering.data.model.MenuItem
import com.hansoft.foodordering.utils.CustomTopAppBar
import com.hansoft.foodordering.viewmodel.MenuViewModel

@Composable
fun UpdateMenuItemScreen(menuViewModel: MenuViewModel,onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    var id by remember { mutableStateOf("") }
    var searchedItem by remember { mutableStateOf<MenuItem?>(null) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Edit Menu Item",
                onNavigationClick = {
                    onBackClick()
                    Log.d("aaa", "AddMenuItemScreen: back to admin screen")
                },
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
          //  Text("Edit Menu Item", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = id,
                interactionSource = interactionSource,
                onValueChange = { id = it },
                label = { Text("Item ID",onTextLayout = { layoutResult: TextLayoutResult ->
                    // Do something
                },) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val intId = id.toIntOrNull()
                    if (intId != null) {
                        menuViewModel.searchMenuItemById(intId) { result ->
                            if (result != null) {
                                searchedItem = result
                                message = ""
                            } else {
                                message = "Item not found"
                                searchedItem = null
                            }
                        }
                    } else {
                        message = "Please enter a valid number"
                        searchedItem = null
                    }
                },
                interactionSource = interactionSource
            ) {
                Text("Search",onTextLayout = { layoutResult: TextLayoutResult ->
                    // Do something
                },)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (searchedItem != null) {

                OutlinedTextField(
                    value = searchedItem!!.name,
                    interactionSource = interactionSource,
                    onValueChange = { searchedItem = searchedItem?.copy(name = it) },
                    label = { Text("Item Name",onTextLayout = { layoutResult: TextLayoutResult ->
                        // Do something
                    },) }
                )



                OutlinedTextField(
                    value = searchedItem!!.description,
                    interactionSource = interactionSource,
                    onValueChange = { searchedItem = searchedItem?.copy(description = it) },
                    label = { Text("Description",onTextLayout = { layoutResult: TextLayoutResult ->
                        // Do something
                    },) }
                )

                OutlinedTextField(
                    value = searchedItem!!.category,
                    interactionSource = interactionSource,
                    onValueChange = { searchedItem = searchedItem?.copy(category = it) },
                    label = { Text("Category",onTextLayout = { layoutResult: TextLayoutResult ->
                        // Do something
                    },) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(modifier=Modifier.height(100.dp).width(280.dp),
                    value = searchedItem!!.imageUrl,
                    interactionSource = interactionSource,
                    onValueChange = { searchedItem = searchedItem?.copy(imageUrl = it) },
                    label = { Text("Image URL",onTextLayout = { layoutResult: TextLayoutResult ->
                        // Do something
                    },) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchedItem!!.price.toString(),
                    interactionSource = interactionSource,
                    onValueChange = {
                        searchedItem = searchedItem?.copy(price = it.toDoubleOrNull() ?: 0.0)
                    },
                    label = { Text("Price",onTextLayout = { layoutResult: TextLayoutResult ->
                        // Do something
                    },) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // val priceDouble = price.toDoubleOrNull() ?: 0.0
                Button(
                    onClick = {

                        menuViewModel.updateMenuItemByMenuId(
                            searchedItem!!,
                            onSuccess = { message = "Update menu item successfully!"},
                            onError = { message = "Failed to update menu item: ${it.message}" })

                    },
                    interactionSource = interactionSource
                ) {
                    Text("Update Item",onTextLayout = { layoutResult: TextLayoutResult ->
                        // Do something
                    },)
                }
            }
            if (message.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(message, onTextLayout = { layoutResult: TextLayoutResult ->
                        // Do something
                    },color = Color.Green, modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
    }

}
