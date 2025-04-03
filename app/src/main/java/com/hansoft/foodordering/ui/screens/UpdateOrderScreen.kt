package com.hansoft.foodordering.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hansoft.foodordering.data.model.OrderStatus
import com.hansoft.foodordering.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateOrderScreen(viewModel: OrderViewModel) {
    var orderId by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf(OrderStatus.PREPARING) }
    var expanded by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = orderId,
            onValueChange = { orderId = it },
            label = { Text("Order Id") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                value = selectedStatus.name,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable { expanded = true },
                label = { Text("Select Status") },
                trailingIcon = {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                OrderStatus.values().forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status.name) },
                        onClick = {
                            if (orderId == "")
                            {
                                message = "Please enter order id"
                                return@DropdownMenuItem
                            }
                            else {
                                selectedStatus = status
                                expanded = false
                                viewModel.updateOrderStatus(orderId, status,
                                    onSuccess = { message = "Order status updated successfully!" },
                                    onError = {
                                        message = "Failed to update order status: ${it.message}"
                                    })
                            }
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(200.dp))
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