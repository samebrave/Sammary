package com.samprojects.sammary.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SummaryDao {
    @Query("SELECT * FROM summaries ORDER BY createdAt DESC")
    fun getAllSummaries(): Flow<List<SummaryEntity>>

    @Query("SELECT * FROM summaries WHERE id = :id")
    suspend fun getSummaryById(id: Long): SummaryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummary(summary: SummaryEntity): Long

    @Update suspend fun updateSummary(summary: SummaryEntity)

    @Delete suspend fun deleteSummary(summary: SummaryEntity)

    @Query("DELETE FROM summaries WHERE id = :id") suspend fun deleteSummaryById(id: Long)

    @Query("DELETE FROM summaries") suspend fun deleteAllSummaries()

    @Query("SELECT COUNT(*) FROM summaries") suspend fun getSummaryCount(): Int
}
