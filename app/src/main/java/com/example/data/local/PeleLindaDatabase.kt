package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserProfileEntity::class,
        RoutineHistoryEntity::class,
        FavoriteRecipeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PeleLindaDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao
    abstract fun routineHistoryDao(): RoutineHistoryDao
    abstract fun favoriteRecipeDao(): FavoriteRecipeDao

    companion object {
        @Volatile
        private var INSTANCE: PeleLindaDatabase? = null

        fun getInstance(context: Context): PeleLindaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PeleLindaDatabase::class.java,
                    "pele_linda_room.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
