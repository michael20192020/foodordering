package com.hansoft.foodordering.data.model

data class MenuItem(val id: Int = 0,
                    val name: String = "",
                    val price: Double = 0.0,
                    val description: String = "",
                    val imageUrl: String = "",
                    val category: String = "")
