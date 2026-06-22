package com.aiscern.app.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanHistoryDao {

    @Query("SELECT * FROM scan_history ORDER BY created_at DESC")
    fun getAllScans(): Flow<List<ScanHistoryEntity>>

    @Query("SELECT * FROM scan_history WHERE modality = :modality ORDER BY created_at DESC")
    fun getScansByModality(modality: String): Flow<List<ScanHistoryEntity>>

    @Query("""
        SELECT * FROM scan_history 
        WHERE input_preview LIKE '%' || :query || '%' 
        ORDER BY created_at DESC
    """)
    fun searchScans(query: String): Flow<List<ScanHistoryEntity>>

    @Query("SELECT * FROM scan_history WHERE id = :id LIMIT 1")
    suspend fun getScanById(id: String): ScanHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: ScanHistoryEntity)

    @Query("DELETE FROM scan_history WHERE id = :id")
    suspend fun deleteScan(id: String)

    @Query("DELETE FROM scan_history")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM scan_history WHERE created_at > :since")
    suspend fun getScanCountSince(since: Long): Int
}
