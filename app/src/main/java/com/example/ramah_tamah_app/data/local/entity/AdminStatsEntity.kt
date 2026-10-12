package com.example.ramah_tamah_app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin_stats")
data class AdminStatsEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 1, // Always 1 row
    
    @ColumnInfo(name = "total_peserta")
    val totalPeserta: Int = 0,
    
    @ColumnInfo(name = "total_hadir")
    val totalHadir: Int = 0,
    
    @ColumnInfo(name = "total_pemenang")
    val totalPemenang: Int = 0,
    
    @ColumnInfo(name = "total_hadiah_diambil")
    val totalHadiahDiambil: Int = 0,
    
    @ColumnInfo(name = "total_jenis_hadiah")
    val totalJenisHadiah: Int = 0,
    
    @ColumnInfo(name = "last_synced")
    val lastSynced: Long = System.currentTimeMillis()
)
