package com.hansoft.foodordering.data.model

data class CartItem(val id: Int,
                    val name: String,
                    val price: Double,
                    var quantity: Int = 1,
                    val imageUrl: String = "")
