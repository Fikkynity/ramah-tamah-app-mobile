package com.example.ramah_tamah_app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ramah_tamah_app.data.local.dao.AdminStatsDao
import com.example.ramah_tamah_app.data.local.dao.UserDao
import com.example.ramah_tamah_app.data.local.dao.WinnerDao
import com.example.ramah_tamah_app.data.local.entity.AdminStatsEntity
import com.example.ramah_tamah_app.data.local.entity.UserEntity
import com.example.ramah_tamah_app.data.local.entity.WinnerEntity

@Database(
    entities = [
        UserEntity::class,
        AdminStatsEntity::class,
        WinnerEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun adminStatsDao(): AdminStatsDao
    abstract fun winnerDao(): WinnerDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ramah_tamah_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
