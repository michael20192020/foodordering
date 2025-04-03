package com.hansoft.foodordering.data.model

import com.google.firebase.Timestamp

data class Order(val orderId: Int = 0,
                 val userId: String = "",
                 val items: List<String> = listOf(),
                 val totalPrice: Double = 0.0,
                 val status: String = "Pending",
                 val timestamp: Timestamp = Timestamp.now()
                 )
