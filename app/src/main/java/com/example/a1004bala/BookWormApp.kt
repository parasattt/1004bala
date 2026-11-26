package com.example.a1004bala

import android.app.Application
import com.example.a1004bala.data.AppContainer

class BookWormApp: Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}