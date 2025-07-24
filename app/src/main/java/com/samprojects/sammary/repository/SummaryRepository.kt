package com.samprojects.sammary.repository

import com.samprojects.sammary.database.SummaryDao
import com.samprojects.sammary.database.SummaryEntity
import com.samprojects.sammary.model.SummaryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SummaryRepository(private val summaryDao: SummaryDao) {

    fun getAllSummaries(): Flow<List<SummaryItem>> {
        return summaryDao.getAllSummaries().map { entities ->
            entities.map { entity ->
                SummaryItem(id = entity.id, title = entity.title, transcript = entity.transcript)
            }
        }
    }

    suspend fun insertSummary(summaryItem: SummaryItem): Long {
        val entity =
                SummaryEntity(
                        id = if (summaryItem.id == 0L) 0 else summaryItem.id,
                        title = summaryItem.title,
                        transcript = summaryItem.transcript
                )
        return summaryDao.insertSummary(entity)
    }

    suspend fun deleteSummary(id: Long) {
        summaryDao.deleteSummaryById(id)
    }

    suspend fun deleteAllSummaries() {
        summaryDao.deleteAllSummaries()
    }

    suspend fun getSummaryCount(): Int {
        return summaryDao.getSummaryCount()
    }
}
