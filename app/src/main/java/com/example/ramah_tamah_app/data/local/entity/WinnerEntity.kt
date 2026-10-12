package com.example.ramah_tamah_app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "winners")
data class WinnerEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    
    @ColumnInfo(name = "peserta_id")
    val pesertaId: Long,
    
    @ColumnInfo(name = "nik")
    val nik: String,
    
    @ColumnInfo(name = "nama")
    val nama: String,
    
    @ColumnInfo(name = "departemen")
    val departemen: String,
    
    @ColumnInfo(name = "hadiah")
    val hadiah: String,
    
    @ColumnInfo(name = "waktu_menang")
    val waktuMenang: String? = null,
    
    @ColumnInfo(name = "status_pengambilan")
    val statusPengambilan: String = "MENANG",
    
    @ColumnInfo(name = "waktu_pengambilan")
    val waktuPengambilan: String? = null,
    
    @ColumnInfo(name = "last_synced")
    val lastSynced: Long = System.currentTimeMillis()
)
