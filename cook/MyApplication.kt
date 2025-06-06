package com.zjgsu.cook

import android.app.Application
import android.content.Context
import com.zjgsu.cook.data.AppDatabase

class MyApplication : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LanguageHelper.applyLocale(base))
    }
}
