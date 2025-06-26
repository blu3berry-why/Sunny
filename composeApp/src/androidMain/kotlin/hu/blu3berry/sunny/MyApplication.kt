package hu.blu3berry.sunny

import android.app.Application
import hu.blu3berry.sunny.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
         }
    }
}