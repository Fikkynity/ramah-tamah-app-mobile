package com.example.ramah_tamah_app.data.local.dao

import androidx.room.*
import com.example.ramah_tamah_app.data.local.entity.AdminStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminStatsDao {
    
    @Query("SELECT * FROM admin_stats WHERE id = 1 LIMIT 1")
    fun getAdminStats(): Flow<AdminStatsEntity?>
    
    @Query("SELECT * FROM admin_stats WHERE id = 1 LIMIT 1")
    suspend fun getAdminStatsSync(): AdminStatsEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: AdminStatsEntity)
    
    @Query("DELETE FROM admin_stats")
    suspend fun clearAll()
}
