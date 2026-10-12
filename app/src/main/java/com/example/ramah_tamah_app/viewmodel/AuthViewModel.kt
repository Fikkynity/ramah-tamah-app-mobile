package com.example.ramah_tamah_app.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ramah_tamah_app.data.local.AppDatabase
import com.example.ramah_tamah_app.data.model.AdminStats
import com.example.ramah_tamah_app.data.model.User
import com.example.ramah_tamah_app.data.model.UserListItem
import com.example.ramah_tamah_app.data.model.WinnerItem
import com.example.ramah_tamah_app.data.repository.RamahTamahRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = RamahTamahRepository(
        database.userDao(),
        database.adminStatsDao(),
        database.winnerDao()
    )

    // Current user state
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Loading states
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Admin stats with Flow
    private val _adminStats = MutableStateFlow<AdminStats?>(null)
    val adminStats: StateFlow<AdminStats?> = _adminStats.asStateFlow()

    // Winners list with Flow
    private val _winnersList = MutableStateFlow<List<WinnerItem>>(emptyList())
    val winnersList: StateFlow<List<WinnerItem>> = _winnersList.asStateFlow()

    var isLoadingWinners by mutableStateOf(false)
        private set

    // Users list
    var usersList by mutableStateOf<List<UserListItem>>(emptyList())
        private set

    var isLoadingUsers by mutableStateOf(false)
        private set

    init {
        // Observe admin stats from Room
        viewModelScope.launch {
            repository.getAdminStats().collect { stats ->
                _adminStats.value = stats
            }
        }

        // Observe winners from Room
        viewModelScope.launch {
            repository.getWinners().collect { winners ->
                _winnersList.value = winners
            }
        }
    }

    fun login(nik: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            val result = repository.login(nik, password)
            
            result.onSuccess { user ->
                _currentUser.value = user
                
                // Observe user updates
                viewModelScope.launch {
                    repository.getUserByNik(user.nik).collect { updatedUser ->
                        if (updatedUser != null) {
                            _currentUser.value = updatedUser
                        }
                    }
                }
                
                onSuccess()
            }.onFailure { error ->
                errorMessage = error.message ?: "Login gagal"
                onError(errorMessage ?: "Login gagal")
            }
            
            isLoading = false
        }
    }

    fun refreshUserData() {
        _currentUser.value?.nik?.let { nik ->
            viewModelScope.launch {
                repository.refreshUserData(nik)
            }
        }
    }

    fun loadAdminStats() {
        viewModelScope.launch {
            repository.loadAdminStats()
        }
    }

    fun scanAttendance(nik: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            
            val result = repository.scanAttendance(nik)
            
            result.onSuccess { response ->
                val participantName = response.participant?.nama ?: "Peserta"
                onSuccess("✓ $participantName berhasil dicatat hadir")
            }.onFailure { error ->
                onError(error.message ?: "Scan gagal")
            }
            
            isLoading = false
        }
    }

    fun scanPrize(nik: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            
            val result = repository.scanPrize(nik)
            
            result.onSuccess { response ->
                val winnerName = response.winner?.nama ?: "Pemenang"
                val prize = response.winner?.hadiah ?: "Hadiah"
                onSuccess("✓ $winnerName berhasil ambil: $prize")
            }.onFailure { error ->
                onError(error.message ?: "Scan gagal")
            }
            
            isLoading = false
        }
    }

    fun loadWinners(page: Int = 1) {
        viewModelScope.launch {
            isLoadingWinners = true
            repository.loadWinners(page)
            isLoadingWinners = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.clearUserCache()
            _currentUser.value = null
            _adminStats.value = null
            _winnersList.value = emptyList()
            usersList = emptyList()
            errorMessage = null
        }
    }

    fun loadUsers(search: String = "", role: String = "") {
        viewModelScope.launch {
            isLoadingUsers = true
            try {
                val response = com.example.ramah_tamah_app.ApiClient.apiService.getUsersList(search, role, 1, 100)
                if (response.isSuccessful) {
                    usersList = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                // Silent fail
            } finally {
                isLoadingUsers = false
            }
        }
    }

    fun updateUserRole(nik: String, newRole: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            
            val result = repository.updateUserRole(nik, newRole)
            
            result.onSuccess { response ->
                onSuccess(response.message ?: "Role berhasil diubah")
                loadUsers() // Refresh list
            }.onFailure { error ->
                onError(error.message ?: "Gagal mengubah role")
            }
            
            isLoading = false
        }
    }

    fun updateProfile(nik: String, nama: String, departemen: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            
            val result = repository.updateProfile(nik, nama, departemen)
            
            result.onSuccess { updatedUser ->
                _currentUser.value = updatedUser
                onSuccess("Profile berhasil diperbarui")
            }.onFailure { error ->
                onError(error.message ?: "Gagal memperbarui profile")
            }
            
            isLoading = false
        }
    }
}
