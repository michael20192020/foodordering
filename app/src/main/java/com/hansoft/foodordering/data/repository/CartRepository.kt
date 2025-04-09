package com.hansoft.foodordering.data.repository

import com.hansoft.foodordering.data.model.CartItemDao
import com.hansoft.foodordering.data.model.CartItemEntity
import kotlinx.coroutines.flow.Flow

class CartRepository(private val dao: CartItemDao) {

    val cartItems: Flow<List<CartItemEntity>> = dao.getAllCartItems()
    suspend fun addToCart(item: CartItemEntity) = dao.insertCartItem(item)
    suspend fun updateItem(item: CartItemEntity) = dao.updateCartItem(item)
    suspend fun removeItem(item: CartItemEntity) = dao.deleteCartItem(item)
    suspend fun clearCart() = dao.clearCart()
}