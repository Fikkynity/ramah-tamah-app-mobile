package com.example.ramah_tamah_app.data.repository

import com.example.ramah_tamah_app.ApiClient
import com.example.ramah_tamah_app.data.local.dao.AdminStatsDao
import com.example.ramah_tamah_app.data.local.dao.UserDao
import com.example.ramah_tamah_app.data.local.dao.WinnerDao
import com.example.ramah_tamah_app.data.local.toEntity
import com.example.ramah_tamah_app.data.local.toModel
import com.example.ramah_tamah_app.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RamahTamahRepository(
    private val userDao: UserDao,
    private val adminStatsDao: AdminStatsDao,
    private val winnerDao: WinnerDao
) {
    
    // ==================== USER ====================
    
    suspend fun login(nik: String, password: String): Result<User> {
        return try {
            val response = ApiClient.apiService.login(LoginRequest(nik, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.user != null) {
                    // Cache user to Room
                    userDao.insertUser(body.user.toEntity())
                    Result.success(body.user)
                } else {
                    Result.failure(Exception(body?.message ?: "Login gagal"))
                }
            } else {
                Result.failure(Exception("Terjadi kesalahan server"))
            }
        } catch (e: Exception) {
            // Fallback to local cache if network error
            val cachedUser = userDao.getUserByNikSync(nik)
            if (cachedUser != null && cachedUser.nik == password) {
                Result.success(cachedUser.toModel())
            } else {
                Result.failure(e)
            }
        }
    }
    
    fun getUserByNik(nik: String): Flow<User?> {
        return userDao.getUserByNik(nik).map { it?.toModel() }
    }
    
    suspend fun updateProfile(nik: String, nama: String, departemen: String): Result<User> {
        return try {
            // Update API
            val response = ApiClient.apiService.updateProfile(
                UpdateProfileRequest(nik, nama, departemen)
            )
            
            if (response.isSuccessful && response.body()?.user != null) {
                val user = response.body()!!.user!!
                // Update local cache
                userDao.insertUser(user.toEntity())
                Result.success(user)
            } else {
                // Fallback: update local only
                userDao.updateProfile(nik, nama, departemen)
                val cachedUser = userDao.getUserByNikSync(nik)
                if (cachedUser != null) {
                    Result.success(cachedUser.toModel())
                } else {
                    Result.failure(Exception("Failed to update profile"))
                }
            }
        } catch (e: Exception) {
            // Network error: update local only
            try {
                userDao.updateProfile(nik, nama, departemen)
                val cachedUser = userDao.getUserByNikSync(nik)
                if (cachedUser != null) {
                    Result.success(cachedUser.toModel())
                } else {
                    Result.failure(e)
                }
            } catch (localError: Exception) {
                Result.failure(localError)
            }
        }
    }
    
    suspend fun refreshUserData(nik: String): Result<User> {
        return try {
            val response = ApiClient.apiService.getUserDashboard(nik)
            if (response.isSuccessful && response.body()?.user != null) {
                val user = response.body()!!.user!!
                userDao.insertUser(user.toEntity())
                Result.success(user)
            } else {
                Result.failure(Exception("Failed to refresh user data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun clearUserCache() {
        userDao.clearAll()
    }
    
    // ==================== ADMIN STATS ====================
    
    fun getAdminStats(): Flow<AdminStats?> {
        return adminStatsDao.getAdminStats().map { it?.toModel() }
    }
    
    suspend fun loadAdminStats(): Result<AdminStats> {
        return try {
            val response = ApiClient.apiService.getAdminStats()
            if (response.isSuccessful && response.body()?.stats != null) {
                val stats = response.body()!!.stats!!
                adminStatsDao.insertStats(stats.toEntity())
                Result.success(stats)
            } else {
                Result.failure(Exception("Failed to load admin stats"))
            }
        } catch (e: Exception) {
            // Return cached data on network error
            val cached = adminStatsDao.getAdminStatsSync()
            if (cached != null) {
                Result.success(cached.toModel())
            } else {
                Result.failure(e)
            }
        }
    }
    
    // ==================== SCAN ====================
    
    suspend fun scanAttendance(nik: String): Result<ScanResponse> {
        return try {
            val response = ApiClient.apiService.scanAttendance(ScanRequest(nik))
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    // Refresh stats cache
                    loadAdminStats()
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Scan gagal"))
                }
            } else {
                Result.failure(Exception("Terjadi kesalahan server"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun scanPrize(nik: String): Result<ScanResponse> {
        return try {
            val response = ApiClient.apiService.scanPrize(ScanRequest(nik))
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    // Refresh stats and winners cache
                    loadAdminStats()
                    loadWinners()
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Scan gagal"))
                }
            } else {
                Result.failure(Exception("Terjadi kesalahan server"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ==================== WINNERS ====================
    
    fun getWinners(): Flow<List<WinnerItem>> {
        return winnerDao.getAllWinners().map { list ->
            list.map { it.toModel() }
        }
    }
    
    suspend fun loadWinners(page: Int = 1): Result<List<WinnerItem>> {
        return try {
            val response = ApiClient.apiService.getWinnersPaginated(page, 100)
            if (response.isSuccessful) {
                val winners = response.body()?.data ?: emptyList()
                // Cache to Room
                winnerDao.clearAll()
                winnerDao.insertWinners(winners.map { it.toEntity() })
                Result.success(winners)
            } else {
                Result.failure(Exception("Failed to load winners"))
            }
        } catch (e: Exception) {
            // Return cached data on network error
            val cached = winnerDao.getAllWinners()
            Result.failure(e)
        }
    }
    
    // ==================== USER MANAGEMENT ====================
    
    suspend fun updateUserRole(nik: String, newRole: String): Result<UpdateRoleResponse> {
        return try {
            val response = ApiClient.apiService.updateUserRole(UpdateRoleRequest(nik, newRole))
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Gagal mengubah role"))
                }
            } else {
                Result.failure(Exception("Terjadi kesalahan server"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
