package com.example.ramah_tamah_app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: Long,
    
    @ColumnInfo(name = "nik")
    val nik: String,
    
    @ColumnInfo(name = "nama")
    val nama: String,
    
    @ColumnInfo(name = "departemen")
    val departemen: String,
    
    @ColumnInfo(name = "role")
    val role: String,
    
    @ColumnInfo(name = "sudah_hadir")
    val sudahHadir: Boolean = false,
    
    @ColumnInfo(name = "waktu_datang")
    val waktuDatang: String? = null,
    
    @ColumnInfo(name = "is_pemenang")
    val isPemenang: Boolean = false,
    
    @ColumnInfo(name = "pemenang_id")
    val pemenangId: Long? = null,
    
    @ColumnInfo(name = "hadiah")
    val hadiah: String? = null,
    
    @ColumnInfo(name = "waktu_menang")
    val waktuMenang: String? = null,
    
    @ColumnInfo(name = "status_pengambilan")
    val statusPengambilan: String? = null,
    
    @ColumnInfo(name = "waktu_pengambilan")
    val waktuPengambilan: String? = null,
    
    @ColumnInfo(name = "last_synced")
    val lastSynced: Long = System.currentTimeMillis()
) {
    val isAdmin: Boolean
        get() = role == "admin"
}
