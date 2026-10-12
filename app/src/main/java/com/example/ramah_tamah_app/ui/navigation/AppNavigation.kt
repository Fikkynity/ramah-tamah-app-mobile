package com.example.ramah_tamah_app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ramah_tamah_app.ui.admin.AdminDashboardScreen
import com.example.ramah_tamah_app.ui.admin.ScanAttendanceScreen
import com.example.ramah_tamah_app.ui.admin.ScanPrizeScreen
import com.example.ramah_tamah_app.ui.admin.UserManagementScreen
import com.example.ramah_tamah_app.ui.auth.LoginScreen
import com.example.ramah_tamah_app.ui.dashboard.DashboardHadiahScreen
import com.example.ramah_tamah_app.ui.profile.EditProfileScreen
import com.example.ramah_tamah_app.ui.qr.QrPesertaScreen
import com.example.ramah_tamah_app.ui.theme.Primary
import com.example.ramah_tamah_app.viewmodel.AuthViewModel

sealed class Screen(val route: String, val title: String) {
    object Login : Screen("login", "Login")
    object DashboardHadiah : Screen("dashboard_hadiah", "Hadiah")
    object QrPeserta : Screen("qr_peserta", "QR Peserta")
    object EditProfile : Screen("edit_profile", "Edit Profile")
    object AdminDashboard : Screen("admin_dashboard", "Admin Dashboard")
    object AdminScanAttendance : Screen("admin_scan_attendance", "Scan Hadir")
    object AdminScanPrize : Screen("admin_scan_prize", "Scan Hadiah")
    object AdminUserManagement : Screen("admin_user_management", "Kelola User")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    viewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentUser by viewModel.currentUser.collectAsState()

    val startDestination = if (currentUser == null) {
        Screen.Login.route
    } else if (currentUser?.isAdmin == true) {
        Screen.AdminDashboard.route
    } else {
        Screen.DashboardHadiah.route
    }

    Scaffold(
        topBar = {
            if (currentRoute != Screen.Login.route && currentUser != null) {
                TopAppBar(
                    title = {
                        Text(
                            text = when (currentRoute) {
                                Screen.DashboardHadiah.route -> "Dashboard Hadiah"
                                Screen.QrPeserta.route -> "QR Code Saya"
                                Screen.EditProfile.route -> "Edit Profile"
                                Screen.AdminDashboard.route -> "Dashboard Admin"
                                Screen.AdminScanAttendance.route -> "Scan Kehadiran"
                                Screen.AdminScanPrize.route -> "Scan Hadiah"
                                Screen.AdminUserManagement.route -> "Kelola User"
                                else -> "Ramah Tamah"
                            },
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                viewModel.logout()
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Logout",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Primary
                    )
                )
            }
        },
        bottomBar = {
            if (currentRoute != Screen.Login.route && currentUser != null) {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    if (currentUser?.isAdmin == true) {
                        // Admin Bottom Navigation: 3 tabs
                        NavigationBarItem(
                            selected = currentRoute == Screen.AdminDashboard.route,
                            onClick = {
                                navController.navigate(Screen.AdminDashboard.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = "Dashboard"
                                )
                            },
                            label = { Text("Dashboard") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Primary,
                                selectedTextColor = Primary,
                                indicatorColor = Primary.copy(alpha = 0.1f)
                            )
                        )

                        NavigationBarItem(
                            selected = currentRoute == Screen.AdminScanAttendance.route,
                            onClick = {
                                navController.navigate(Screen.AdminScanAttendance.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = "Scan Hadir"
                                )
                            },
                            label = { Text("Scan Hadir") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Primary,
                                selectedTextColor = Primary,
                                indicatorColor = Primary.copy(alpha = 0.1f)
                            )
                        )

                        NavigationBarItem(
                            selected = currentRoute == Screen.AdminScanPrize.route,
                            onClick = {
                                navController.navigate(Screen.AdminScanPrize.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.CardGiftcard,
                                    contentDescription = "Scan Hadiah"
                                )
                            },
                            label = { Text("Scan Hadiah") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Primary,
                                selectedTextColor = Primary,
                                indicatorColor = Primary.copy(alpha = 0.1f)
                            )
                        )

                        NavigationBarItem(
                            selected = currentRoute == Screen.AdminUserManagement.route,
                            onClick = {
                                navController.navigate(Screen.AdminUserManagement.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Group,
                                    contentDescription = "Kelola User"
                                )
                            },
                            label = { Text("Kelola User") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Primary,
                                selectedTextColor = Primary,
                                indicatorColor = Primary.copy(alpha = 0.1f)
                            )
                        )

                        NavigationBarItem(
                            selected = currentRoute == Screen.QrPeserta.route,
                            onClick = {
                                navController.navigate(Screen.QrPeserta.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.QrCode,
                                    contentDescription = "QR Saya"
                                )
                            },
                            label = { Text("QR Saya") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Primary,
                                selectedTextColor = Primary,
                                indicatorColor = Primary.copy(alpha = 0.1f)
                            )
                        )
                    } else {
                        // User Bottom Navigation: 2 tabs
                        NavigationBarItem(
                            selected = currentRoute == Screen.DashboardHadiah.route,
                            onClick = {
                                navController.navigate(Screen.DashboardHadiah.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.CardGiftcard,
                                    contentDescription = "Hadiah"
                                )
                            },
                            label = { Text("Hadiah") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Primary,
                                selectedTextColor = Primary,
                                indicatorColor = Primary.copy(alpha = 0.1f)
                            )
                        )

                        NavigationBarItem(
                            selected = currentRoute == Screen.QrPeserta.route,
                            onClick = {
                                navController.navigate(Screen.QrPeserta.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.QrCode,
                                    contentDescription = "QR Peserta"
                                )
                            },
                            label = { Text("QR Peserta") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Primary,
                                selectedTextColor = Primary,
                                indicatorColor = Primary.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = { isAdmin ->
                        val targetRoute = if (isAdmin) Screen.AdminDashboard.route else Screen.DashboardHadiah.route
                        navController.navigate(targetRoute) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.DashboardHadiah.route) {
                val user = viewModel.currentUser.collectAsState().value
                if (user != null) {
                    DashboardHadiahScreen(user = user)
                }
            }

            composable(Screen.QrPeserta.route) {
                val user = viewModel.currentUser.collectAsState().value
                if (user != null) {
                    QrPesertaScreen(
                        user = user,
                        onEditProfile = {
                            navController.navigate(Screen.EditProfile.route)
                        }
                    )
                }
            }

            composable(Screen.EditProfile.route) {
                val user = viewModel.currentUser.collectAsState().value
                if (user != null) {
                    EditProfileScreen(
                        user = user,
                        viewModel = viewModel,
                        onProfileUpdated = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable(Screen.AdminDashboard.route) {
                val user = viewModel.currentUser.collectAsState().value
                if (user != null) {
                    AdminDashboardScreen(user = user, viewModel = viewModel)
                }
            }

            composable(Screen.AdminScanAttendance.route) {
                ScanAttendanceScreen(viewModel = viewModel)
            }

            composable(Screen.AdminScanPrize.route) {
                ScanPrizeScreen(viewModel = viewModel)
            }

            composable(Screen.AdminUserManagement.route) {
                UserManagementScreen(viewModel = viewModel)
            }
        }
    }
}
