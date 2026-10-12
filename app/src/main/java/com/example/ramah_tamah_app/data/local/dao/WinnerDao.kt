package com.example.ramah_tamah_app.data.local.dao

import androidx.room.*
import com.example.ramah_tamah_app.data.local.entity.WinnerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WinnerDao {
    
    @Query("SELECT * FROM winners ORDER BY waktu_menang DESC")
    fun getAllWinners(): Flow<List<WinnerEntity>>
    
    @Query("SELECT * FROM winners WHERE nik = :nik LIMIT 1")
    suspend fun getWinnerByNik(nik: String): WinnerEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWinner(winner: WinnerEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWinners(winners: List<WinnerEntity>)
    
    @Update
    suspend fun updateWinner(winner: WinnerEntity)
    
    @Query("DELETE FROM winners")
    suspend fun clearAll()
}
