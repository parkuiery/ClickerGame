package com.uiel.clickergame

import android.app.Application
import android.content.Context

class CkApplication : Application() {
    init{
        instance = this
    }

    companion object {
        lateinit var instance: CkApplication
        fun applicationContext() : Context {
            return instance.applicationContext
        }
    }
}