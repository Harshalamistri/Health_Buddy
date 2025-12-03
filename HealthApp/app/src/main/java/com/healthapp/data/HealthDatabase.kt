package com.healthapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [VaccinationEntity::class, UploadedFileEntity::class, ReminderEntity::class],
    version = 2,
    exportSchema = false
)
abstract class HealthDatabase : RoomDatabase() {
    abstract fun vaccinationDao(): VaccinationDao
    abstract fun uploadedFileDao(): UploadedFileDao
    abstract fun reminderDao(): ReminderDao
}

object AppDatabaseProvider {
    @Volatile
    private var INSTANCE: HealthDatabase? = null

    fun getInstance(context: Context): HealthDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                HealthDatabase::class.java,
                "health_app_db"
            ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
        }
    }

    fun reset(context: Context) {
        synchronized(this) {
            INSTANCE?.close()
            context.deleteDatabase("health_app_db")
            INSTANCE = null
        }
    }
}
