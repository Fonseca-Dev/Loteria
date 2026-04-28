package co.tiagoaguiar.loteriacomposedev

import android.app.Application
import co.tiagoaguiar.loteriacomposedev.data.AppDatabase

class App : Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getInstance(this)
    }
}