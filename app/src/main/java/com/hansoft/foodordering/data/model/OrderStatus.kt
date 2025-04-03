package com.hansoft.foodordering.data.model

enum class OrderStatus(val displayName: String) {
    PENDING("Pending"),
    PREPARING("Preparing"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered")
}