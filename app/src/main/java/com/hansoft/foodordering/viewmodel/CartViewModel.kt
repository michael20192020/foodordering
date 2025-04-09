package com.hansoft.foodordering.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hansoft.foodordering.OrderApplication
import com.hansoft.foodordering.data.model.CartItem
import com.hansoft.foodordering.data.model.CartItemEntity
import com.hansoft.foodordering.data.model.CartUiState
import com.hansoft.foodordering.data.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository): ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems
    var cartkUiState: StateFlow<CartUiState> = getAllNotebookStream().map { CartUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = CartUiState()
        )

    fun getAllNotebookStream() : Flow<List<CartItemEntity>>
    {
        return cartRepository.cartItems
    }

    fun addToCart(item: CartItemEntity) {
        viewModelScope.launch {
            cartRepository.addToCart(item)
        }
    }



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
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

    fun clearCartOld() {
        _cartItems.value = emptyList()
    }

    fun deleteCartItem(item: CartItemEntity) {
        viewModelScope.launch {
            cartRepository.removeItem(item)
        }
    }

    fun deleteCartItem(itemId: Int) {
        _cartItems.value = _cartItems.value.filter { it.id != itemId }
    }

    fun reduceItemQuantity(item: CartItemEntity) {
        viewModelScope.launch {
            if (item.quantity > 0) {
                //item.copy(quantity = item.quantity - 1)
                val updatedItem = item.copy(quantity = item.quantity - 1)
                cartRepository.updateItem(updatedItem)
            }
        }
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

    fun increaseItemQuantity(item: CartItemEntity) {
        viewModelScope.launch {
            val updatedItem = item.copy(quantity = item.quantity + 1)
            cartRepository.updateItem(updatedItem)

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

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as OrderApplication
                val cartRepository = application.container.cartRepository
                CartViewModel(cartRepository)
            }
        }
        private const val TIMEOUT_MILLIS = 5_000L

    }
}