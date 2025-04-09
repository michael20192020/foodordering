package com.hansoft.foodordering

import android.app.Application
import com.hansoft.foodordering.data.model.AppContainer
import com.hansoft.foodordering.data.model.AppDataContainer

class OrderApplication: Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        container = AppDataContainer(this)
    }
}