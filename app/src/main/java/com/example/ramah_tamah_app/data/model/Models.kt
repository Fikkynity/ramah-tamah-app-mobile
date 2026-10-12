package com.example.ramah_tamah_app.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val nik: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val type: String? = null,
    val message: String? = null,
    val user: User? = null
)

data class User(
    @SerializedName("user_id")
    val userId: Long? = 0,
    val nik: String = "",
    val nama: String = "",
    val departemen: String = "",
    val role: String = "user",
    @SerializedName("sudah_hadir")
    val sudahHadir: Boolean = false,
    @SerializedName("waktu_datang")
    val waktuDatang: String? = null,
    @SerializedName("is_pemenang")
    val isPemenang: Boolean = false,
    @SerializedName("pemenang_id")
    val pemenangId: Long? = null,
    val hadiah: String? = null,
    @SerializedName("waktu_menang")
    val waktuMenang: String? = null,
    @SerializedName("status_pengambilan")
    val statusPengambilan: String? = null,
    @SerializedName("waktu_pengambilan")
    val waktuPengambilan: String? = null
) {
    val isAdmin: Boolean
        get() = role == "admin"
}

data class UserDashboardResponse(
    val success: Boolean,
    val type: String? = null,
    val message: String? = null,
    val user: User? = null
)

data class AdminStatsResponse(
    val success: Boolean,
    val type: String? = null,
    val stats: AdminStats? = null
)

data class AdminStats(
    @SerializedName("total_peserta")
    val totalPeserta: Int = 0,
    @SerializedName("total_hadir")
    val totalHadir: Int = 0,
    @SerializedName("total_pemenang")
    val totalPemenang: Int = 0,
    @SerializedName("total_hadiah_diambil")
    val totalHadiahDiambil: Int = 0,
    @SerializedName("total_jenis_hadiah")
    val totalJenisHadiah: Int = 0
)

data class ScanRequest(
    val nik: String
)

data class ScanResponse(
    val success: Boolean,
    val type: String? = null,
    val message: String? = null,
    val participant: ParticipantInfo? = null,
    val winner: WinnerInfo? = null,
    @SerializedName("waktu_datang")
    val waktuDatang: String? = null
)

data class ParticipantInfo(
    val id: Long = 0,
    val nik: String = "",
    val nama: String = "",
    val departemen: String = ""
)

data class WinnerInfo(
    val id: Long = 0,
    val nik: String = "",
    val nama: String = "",
    val departemen: String = "",
    val hadiah: String = "",
    @SerializedName("status_pengambilan")
    val statusPengambilan: String? = null,
    @SerializedName("waktu_pengambilan")
    val waktuPengambilan: String? = null
)

data class WinnerItem(
    val id: Long = 0,
    @SerializedName("peserta_id")
    val pesertaId: Long = 0,
    val nik: String = "",
    val nama: String = "",
    val departemen: String = "",
    val hadiah: String = "",
    @SerializedName("waktu_menang")
    val waktuMenang: String? = null,
    @SerializedName("status_pengambilan")
    val statusPengambilan: String = "MENANG",
    @SerializedName("waktu_pengambilan")
    val waktuPengambilan: String? = null
)

data class WinnersPaginatedResponse(
    val success: Boolean,
    val data: List<WinnerItem> = emptyList(),
    val pagination: PaginationInfo? = null
)

data class PaginationInfo(
    val page: Int = 1,
    @SerializedName("per_page")
    val perPage: Int = 50,
    val total: Int = 0,
    @SerializedName("total_pages")
    val totalPages: Int = 1
)

data class UserListItem(
    @SerializedName("user_id")
    val userId: Long = 0,
    val nik: String = "",
    val nama: String = "",
    val departemen: String = "",
    val role: String = "user",
    @SerializedName("created_at")
    val createdAt: String? = null
)

data class UsersListResponse(
    val success: Boolean,
    val data: List<UserListItem> = emptyList(),
    val pagination: PaginationInfo? = null
)

data class UpdateRoleRequest(
    val nik: String,
    val role: String
)

data class UpdateProfileRequest(
    val nik: String,
    val nama: String,
    val departemen: String
)

data class UpdateRoleResponse(
    val success: Boolean,
    val type: String? = null,
    val message: String? = null,
    val user: UserListItem? = null
)
