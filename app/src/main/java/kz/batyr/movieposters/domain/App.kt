package kz.batyr.movieposters.domain

import android.app.Application
import androidx.room.Room
import kz.batyr.movieposters.data.AppDatabase

class App:Application() {
    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.inMemoryDatabaseBuilder(
            applicationContext,
            AppDatabase::class.java,

        ).build()
    }
}