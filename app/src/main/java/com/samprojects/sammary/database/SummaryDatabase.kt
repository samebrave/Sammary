package com.samprojects.sammary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SummaryEntity::class], version = 1, exportSchema = false)
abstract class SummaryDatabase : RoomDatabase() {
    abstract fun summaryDao(): SummaryDao

    companion object {
        @Volatile private var INSTANCE: SummaryDatabase? = null

        fun getDatabase(context: Context): SummaryDatabase {
            return INSTANCE
                    ?: synchronized(this) {
                        val instance =
                                Room.databaseBuilder(
                                                context.applicationContext,
                                                SummaryDatabase::class.java,
                                                "summary_database"
                                        )
                                        .build()
                        INSTANCE = instance
                        instance
                    }
        }
    }
}
