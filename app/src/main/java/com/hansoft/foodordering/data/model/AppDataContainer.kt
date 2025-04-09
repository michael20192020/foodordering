package com.hansoft.foodordering.data.model

import android.content.Context
import com.hansoft.foodordering.data.repository.CartRepository

class AppDataContainer(private val context: Context) : AppContainer {
    override val cartRepository: CartRepository by lazy {
        CartRepository(AppDatabase.getDatabase(context).cartItemDao())
    }
}