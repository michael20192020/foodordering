package com.hansoft.foodordering.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cart_items")
data class CartItemEntity(@PrimaryKey(autoGenerate = true) val id: Int = 0,
                          val name: String,
                          val price: Double,
                          val quantity: Int = 1,
                          val imageUrl: String = "")
