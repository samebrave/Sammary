package com.samprojects.sammary.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "summaries")
data class SummaryEntity(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val title: String,
        val transcript: String,
        val createdAt: Long = System.currentTimeMillis()
)
