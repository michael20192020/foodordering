package com.hansoft.foodordering.data.model

import com.hansoft.foodordering.data.repository.CartRepository

interface AppContainer {
    val cartRepository: CartRepository
}