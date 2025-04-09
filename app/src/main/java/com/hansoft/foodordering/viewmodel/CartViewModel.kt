package com.hansoft.foodordering.viewmodel

import androidx.lifecycle.ViewModel
import com.hansoft.foodordering.data.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel: ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun addToCart(item: CartItem) {
        val currentList = _cartItems.value.toMutableList()
        val existingItem = currentList.find { it.id == item.id }

        if (existingItem != null) {
            existingItem.quantity += item.quantity
        } else {
            currentList.add(item)
        }

        _cartItems.value = currentList
    }

    fun removeFromCart(itemId: Int) {
        _cartItems.value = _cartItems.value.filter { it.id != itemId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }


    fun deleteCartItem(itemId: Int) {
        _cartItems.value = _cartItems.value.filter { it.id != itemId }
    }
    fun reduceItemQuantity(itemId: Int) {
        _cartItems.value = _cartItems.value.map { item ->
            if (item.id == itemId && item.quantity > 1) {
                // Reduce quantity if it's greater than 1
                item.copy(quantity = item.quantity - 1)
            } else {
                item
            }
        }
    }

    fun increaseItemQuantity(itemId: Int) {
        _cartItems.value = _cartItems.value.map { item ->
            if (item.id == itemId) {
                item.copy(quantity = item.quantity + 1)
            } else {
                item
            }
        }

    }

    // Other functions like adding items, calculating total, etc.
    fun getTotalPrice(): Double {
        return _cartItems.value.sumByDouble { it.price * it.quantity }
    }
}