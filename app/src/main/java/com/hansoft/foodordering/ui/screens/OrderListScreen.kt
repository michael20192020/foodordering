package com.hansoft.foodordering.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansoft.foodordering.data.model.Order
import com.hansoft.foodordering.viewmodel.OrderViewModel

@Composable
fun OrderListScreen(viewModel: OrderViewModel, userId: String) {
    val orders by viewModel.orders.collectAsState()
    val listState = rememberLazyListState()
    LaunchedEffect(userId) {
        viewModel.getUserOrders(userId)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Your Orders", style = MaterialTheme.typography.headlineMedium)

        LazyColumn(state = listState) {
            items(orders) { order ->
                OrderItem(order)
            }
        }
    }
}

@Composable
fun OrderItem(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Order ID: ${order.orderId}", fontWeight = FontWeight.Bold)
            Text("Items:")
            order.items.forEach { item ->
                //Text(text = item, modifier = Modifier.padding(8.dp))
                val parts = item.split("   ")
                Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = parts[0],fontSize = 16.sp)
                    Text(text = parts[1],fontSize = 16.sp)
                }
                //Text(text = item)
            }
            //Text("Items: \n ${order.items}")
            Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Total Price: ")
                Text(text = "$${String.format("%.2f",order.totalPrice)}")
            }
            //Text("Total Price: $${String.format("%.2f",order.totalPrice)}")
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Status: ")
                Text(text = "${order.status}")
            }
            //Text("Status: ${order.status}")
        }
    }
}