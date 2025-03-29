package com.hansoft.foodordering.data.model

data class Order(val orderId: String = "",
                 val userId: String = "",
                 val items: List<String> = listOf(),
                 val totalPrice: Double = 0.0,
                 val status: String = "Pending")
