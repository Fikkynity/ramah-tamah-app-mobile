package com.example.ramah_tamah_app.data.local

import com.example.ramah_tamah_app.data.local.entity.AdminStatsEntity
import com.example.ramah_tamah_app.data.local.entity.UserEntity
import com.example.ramah_tamah_app.data.local.entity.WinnerEntity
import com.example.ramah_tamah_app.data.model.AdminStats
import com.example.ramah_tamah_app.data.model.User
import com.example.ramah_tamah_app.data.model.WinnerItem

// User mapping
fun User.toEntity(): UserEntity {
    return UserEntity(
        userId = this.userId ?: 0L,
        nik = this.nik,
        nama = this.nama,
        departemen = this.departemen,
        role = this.role,
        sudahHadir = this.sudahHadir,
        waktuDatang = this.waktuDatang,
        isPemenang = this.isPemenang,
        pemenangId = this.pemenangId,
        hadiah = this.hadiah,
        waktuMenang = this.waktuMenang,
        statusPengambilan = this.statusPengambilan,
        waktuPengambilan = this.waktuPengambilan
    )
}

fun UserEntity.toModel(): User {
    return User(
        userId = this.userId,
        nik = this.nik,
        nama = this.nama,
        departemen = this.departemen,
        role = this.role,
        sudahHadir = this.sudahHadir,
        waktuDatang = this.waktuDatang,
        isPemenang = this.isPemenang,
        pemenangId = this.pemenangId,
        hadiah = this.hadiah,
        waktuMenang = this.waktuMenang,
        statusPengambilan = this.statusPengambilan,
        waktuPengambilan = this.waktuPengambilan
    )
}

// AdminStats mapping
fun AdminStats.toEntity(): AdminStatsEntity {
    return AdminStatsEntity(
        id = 1,
        totalPeserta = this.totalPeserta,
        totalHadir = this.totalHadir,
        totalPemenang = this.totalPemenang,
        totalHadiahDiambil = this.totalHadiahDiambil,
        totalJenisHadiah = this.totalJenisHadiah
    )
}

fun AdminStatsEntity.toModel(): AdminStats {
    return AdminStats(
        totalPeserta = this.totalPeserta,
        totalHadir = this.totalHadir,
        totalPemenang = this.totalPemenang,
        totalHadiahDiambil = this.totalHadiahDiambil,
        totalJenisHadiah = this.totalJenisHadiah
    )
}

// Winner mapping
fun WinnerItem.toEntity(): WinnerEntity {
    return WinnerEntity(
        id = this.id,
        pesertaId = this.pesertaId,
        nik = this.nik,
        nama = this.nama,
        departemen = this.departemen,
        hadiah = this.hadiah,
        waktuMenang = this.waktuMenang,
        statusPengambilan = this.statusPengambilan,
        waktuPengambilan = this.waktuPengambilan
    )
}

fun WinnerEntity.toModel(): WinnerItem {
    return WinnerItem(
        id = this.id,
        pesertaId = this.pesertaId,
        nik = this.nik,
        nama = this.nama,
        departemen = this.departemen,
        hadiah = this.hadiah,
        waktuMenang = this.waktuMenang,
        statusPengambilan = this.statusPengambilan,
        waktuPengambilan = this.waktuPengambilan
    )
}
