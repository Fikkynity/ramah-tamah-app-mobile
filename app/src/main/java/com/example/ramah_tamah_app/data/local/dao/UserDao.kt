package com.example.ramah_tamah_app.data.local.dao

import androidx.room.*
import com.example.ramah_tamah_app.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    
    @Query("SELECT * FROM users WHERE nik = :nik LIMIT 1")
    fun getUserByNik(nik: String): Flow<UserEntity?>
    
    @Query("SELECT * FROM users WHERE nik = :nik LIMIT 1")
    suspend fun getUserByNikSync(nik: String): UserEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    @Update
    suspend fun updateUser(user: UserEntity)
    
    @Query("UPDATE users SET nama = :nama, departemen = :departemen WHERE nik = :nik")
    suspend fun updateProfile(nik: String, nama: String, departemen: String)
    
    @Query("DELETE FROM users WHERE nik = :nik")
    suspend fun deleteUser(nik: String)
    
    @Query("DELETE FROM users")
    suspend fun clearAll()
    
    @Query("SELECT * FROM users ORDER BY last_synced DESC")
    fun getAllUsers(): Flow<List<UserEntity>>
}
