package com.tomatoapp.application

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tomatoapp.application.state.*
import com.tomatoapp.application.ui.components.UserProfileModal
import com.tomatoapp.application.ui.components.ZomatoBottomNavBar
import com.tomatoapp.application.ui.screens.*
import com.tomatoapp.application.ui.theme.TomatoTheme

@Composable
fun App() {
    val appState = remember { TomatoAppState() }

    TomatoTheme(darkTheme = appState.isDarkMode) {
        CompositionLocalProvider(LocalTomatoAppState provides appState) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .safeDrawingPadding()
            ) {
                when (val screen = appState.currentScreen) {
                    is AppScreen.RestaurantDetail -> {
                        RestaurantDetailScreen(restaurantId = screen.restaurantId)
                    }
                    is AppScreen.Cart -> {
                        CartScreen()
                    }
                    is AppScreen.OrderTracking -> {
                        OrderTrackingScreen(orderId = screen.orderId)
                    }
                    is AppScreen.Home -> {
                        Scaffold(
                            bottomBar = {
                                ZomatoBottomNavBar(
                                    currentTab = appState.currentTab,
                                    onTabSelected = { selectedTab ->
                                        when (selectedTab) {
                                            BottomTab.HOME -> {
                                                appState.currentScreen = AppScreen.Home
                                                appState.currentTab = BottomTab.HOME
                                            }
                                            BottomTab.NEARBY -> {
                                                appState.showLocationModal = true
                                            }
                                            BottomTab.CART -> {
                                                appState.currentScreen = AppScreen.Cart
                                            }
                                            BottomTab.PROFILE -> {
                                                appState.currentTab = BottomTab.PROFILE
                                            }
                                        }
                                    }
                                )
                            },
                            containerColor = MaterialTheme.colorScheme.background
                        ) { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                AnimatedContent(
                                    targetState = appState.currentTab,
                                    transitionSpec = { fadeIn() togetherWith fadeOut() }
                                ) { tab ->
                                    when (tab) {
                                        BottomTab.HOME -> HomeScreen()
                                        BottomTab.NEARBY -> HomeScreen()
                                        BottomTab.CART -> HomeScreen()
                                        BottomTab.PROFILE -> ProfileScreen()
                                    }
                                }
                            }
                        }
                    }
                }

                // Global Profile & Settings Modal
                if (appState.showProfileModal) {
                    UserProfileModal(onDismiss = { appState.showProfileModal = false })
                }

                // Global Location & State Selection Modal
                if (appState.showLocationModal) {
                    com.tomatoapp.application.ui.components.LocationSelectionModal(
                        onDismiss = { appState.showLocationModal = false }
                    )
                }

                // Global Saved Addresses Modal
                if (appState.showSavedAddressesModal) {
                    com.tomatoapp.application.ui.components.SavedAddressesModal(
                        onDismiss = { appState.showSavedAddressesModal = false }
                    )
                }

                // Global Payment Methods Modal
                if (appState.showPaymentMethodsModal) {
                    com.tomatoapp.application.ui.components.PaymentMethodsModal(
                        onDismiss = { appState.showPaymentMethodsModal = false }
                    )
                }

                // Global Notifications Modal
                if (appState.showNotificationsModal) {
                    com.tomatoapp.application.ui.components.NotificationsModal(
                        onDismiss = { appState.showNotificationsModal = false }
                    )
                }

                // Top Heads-Up Order Notification Banner
                val activeNotif = appState.activeNotificationBanner
                if (activeNotif != null) {
                    Box(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)) {
                        com.tomatoapp.application.ui.components.TopOrderNotificationBanner(
                            notification = activeNotif,
                            onDismiss = { appState.activeNotificationBanner = null },
                            onClick = {
                                appState.activeNotificationBanner = null
                                if (activeNotif.orderId.startsWith("TOM-")) {
                                    appState.currentScreen = AppScreen.OrderTracking(activeNotif.orderId)
                                }
                            }
                        )
                    }
                }

                // Global Order Chat with Admin Modal
                val activeOrd = appState.activeOrder
                if (appState.showChatModal && activeOrd != null) {
                    com.tomatoapp.application.ui.components.OrderChatModal(
                        order = activeOrd,
                        onDismiss = { appState.showChatModal = false }
                    )
                }

                // Global Phone Login & Register Modal
                if (appState.showPhoneLoginModal) {
                    com.tomatoapp.application.ui.components.PhoneLoginModal(
                        onDismiss = { appState.showPhoneLoginModal = false }
                    )
                }

                // Global My Shop Management Modal
                if (appState.showMyShopModal) {
                    com.tomatoapp.application.ui.components.MyShopModal(
                        onDismiss = { appState.showMyShopModal = false }
                    )
                }

                // Global Comprehensive Filters Modal
                if (appState.showFiltersModal) {
                    com.tomatoapp.application.ui.components.ComprehensiveFiltersModal(
                        onDismiss = { appState.showFiltersModal = false }
                    )
                }

                // Global Cuisines Selection Modal
                if (appState.showCuisinesModal) {
                    com.tomatoapp.application.ui.components.CuisinesSelectionModal(
                        onDismiss = { appState.showCuisinesModal = false }
                    )
                }
            }
        }
    }
}