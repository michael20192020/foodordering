package com.hansoft.foodordering.data.model

data class MenuItem(var id: Int = 0,
                    var name: String = "",
                    var price: Double = 0.0,
                    var description: String = "",
                    var imageUrl: String = "",
                    var category: String = "")
