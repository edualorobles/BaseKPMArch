package es.edualorobles.basekpmarch

import android.app.Application
import org.koin.android.ext.koin.androidContext
import es.edualorobles.basekpmarch.di.initKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@BaseApplication)
        }
    }
}