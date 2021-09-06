package com.devcraft.freesignalsfortrading.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.devcraft.freesignalsfortrading.app.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel("NOTIFICATION_CHANNEL", name, importance)
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        startKoin {
            androidContext(this@App)
            modules(
                netModule,
                netApiModule,
                repositoryMappersModule,
                viewModule
            )
        }
        instance = this
    }
}