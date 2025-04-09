package com.hansoft.foodordering.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansoft.foodordering.data.model.Order
import com.hansoft.foodordering.data.model.OrderStatus
import com.hansoft.foodordering.utils.CustomTopAppBar
import com.hansoft.foodordering.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateOrderScreen(orderViewModel: OrderViewModel,onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    var orderId by remember { mutableStateOf("") }
    var searchedItem by remember { mutableStateOf<Order?>(null) }
    var selectedStatus by remember { mutableStateOf(OrderStatus.PREPARING) }
    var expanded by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Update Order Status",
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
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = orderId,
                interactionSource = interactionSource,
                onValueChange = { orderId = it },
                label = { Text("Order Id",onTextLayout = { layoutResult: TextLayoutResult ->
                    // Do something
                },) },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val intId = orderId.toIntOrNull()
                    if (intId != null) {
                        orderViewModel.searchOrderItemById(intId) { result ->
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
                })
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (searchedItem != null) {


                Text("Items:",onTextLayout = { layoutResult: TextLayoutResult ->
                    // Do something
                },)
                searchedItem!!.items.forEach { item ->
                    //Text(text = item, modifier = Modifier.padding(8.dp))
                    val parts = item.split("   ")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = parts[0], fontSize = 16.sp,onTextLayout = { layoutResult: TextLayoutResult ->
                            // Do something
                        },)
                        Text(text = parts[1], fontSize = 16.sp,onTextLayout = { layoutResult: TextLayoutResult ->
                            // Do something
                        },)
                    }
                    //Text(text = item)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total Price: ",onTextLayout = { layoutResult: TextLayoutResult ->
                        // Do something
                    },)
                    Text(text = "$${String.format("%.2f", searchedItem!!.totalPrice)}",
                        onTextLayout = { layoutResult: TextLayoutResult ->
                            // Do something
                        },)
                }
                //Text("Total Price: $${String.format("%.2f",order.totalPrice)}")
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Status: ",onTextLayout = { layoutResult: TextLayoutResult ->
                        // Do something
                    },)
                    Text(text = "${searchedItem!!.status}",onTextLayout = { layoutResult: TextLayoutResult ->
                        // Do something
                    },)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Change status",onTextLayout = { layoutResult: TextLayoutResult ->
                    // Do something
                },)
                Spacer(modifier = Modifier.height(16.dp))
                androidx.compose.material3.ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = selectedStatus.name,
                        interactionSource = interactionSource,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .clickable { expanded = true },
                        label = { Text("Select Status",onTextLayout = { layoutResult: TextLayoutResult ->
                            // Do something
                        },) },
                        trailingIcon = {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OrderStatus.values().forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.name,onTextLayout = { layoutResult: TextLayoutResult ->
                                    // Do something
                                },) },
                                interactionSource = interactionSource,
                                onClick = {
                                    if (orderId == "") {
                                        message = "Please enter order id"
                                        return@DropdownMenuItem
                                    } else {
                                        selectedStatus = status
                                        expanded = false
                                        orderViewModel.updateOrderStatus(orderId, status,
                                            onSuccess = {
                                                message = "Order status updated successfully!"
                                                searchedItem =
                                                    searchedItem?.copy(status = status.displayName)
                                            },
                                            onError = {
                                                message =
                                                    "Failed to update order status: ${it.message}"
                                            })
                                    }
                                }
                            )
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
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