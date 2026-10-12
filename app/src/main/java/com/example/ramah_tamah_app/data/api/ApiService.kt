package com.example.ramah_tamah_app.data.api

import com.example.ramah_tamah_app.data.model.AdminStatsResponse
import com.example.ramah_tamah_app.data.model.LoginRequest
import com.example.ramah_tamah_app.data.model.LoginResponse
import com.example.ramah_tamah_app.data.model.ScanRequest
import com.example.ramah_tamah_app.data.model.ScanResponse
import com.example.ramah_tamah_app.data.model.UpdateProfileRequest
import com.example.ramah_tamah_app.data.model.UpdateRoleRequest
import com.example.ramah_tamah_app.data.model.UpdateRoleResponse
import com.example.ramah_tamah_app.data.model.UserDashboardResponse
import com.example.ramah_tamah_app.data.model.UsersListResponse
import com.example.ramah_tamah_app.data.model.WinnersPaginatedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("index.php?action=login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("index.php?action=user-dashboard")
    suspend fun getUserDashboard(@Query("nik") nik: String): Response<UserDashboardResponse>

    @GET("index.php?action=admin-stats")
    suspend fun getAdminStats(): Response<AdminStatsResponse>

    @POST("index.php?action=scan-attendance")
    suspend fun scanAttendance(@Body request: ScanRequest): Response<ScanResponse>

    @POST("index.php?action=scan-prize")
    suspend fun scanPrize(@Body request: ScanRequest): Response<ScanResponse>

    @GET("index.php?action=winners-paginated")
    suspend fun getWinnersPaginated(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 50
    ): Response<WinnersPaginatedResponse>

    @GET("index.php?action=users-list")
    suspend fun getUsersList(
        @Query("search") search: String = "",
        @Query("role") role: String = "",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 50
    ): Response<UsersListResponse>

    @POST("index.php?action=update-user-role")
    suspend fun updateUserRole(@Body request: UpdateRoleRequest): Response<UpdateRoleResponse>

    @POST("index.php?action=update-profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UserDashboardResponse>
}
