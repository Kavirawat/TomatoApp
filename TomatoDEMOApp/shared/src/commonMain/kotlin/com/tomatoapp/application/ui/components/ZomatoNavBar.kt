package com.tomatoapp.application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomatoapp.application.state.BottomTab
import com.tomatoapp.application.state.LocalTomatoAppState
import com.tomatoapp.application.ui.theme.*

/**
 * Top Navbar designed matching Figma Screen 2 layout with Dark Mode support:
 *
 * Features:
 * - Adapts to Dark Mode (dark surfaces, light text, dark search field) & Light Mode (clean white)
 * - Left: Tomato Brand Logo + Red Location Pin + Bold Location Title ("Manav-aashram ˅") + Subtitle ("Mehsana,Gujrat")
 * - Right: "Login" button (when logged out) OR User Profile Avatar with Initial (when logged in)
 * - Bottom Row: Rounded elevated Search Bar with Red Search Icon 🔍 & "Restaurant name or Dish......" placeholder (Mic removed as requested)
 */
@Composable
fun ZomatoNavBar(
    modifier: Modifier = Modifier
) {
    val appState = LocalTomatoAppState.current
    val isDark = appState.isDarkMode

    // Dynamic colors based on Dark Mode
    val navBgColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val navBorderColor = if (isDark) Color(0xFF2C2C2C) else Color(0xFFF0F0F0)
    val dividerColor = if (isDark) Color(0xFF383838) else Color(0xFFE2E2E2)
    val titleColor = if (isDark) Color.White else Color(0xFF1C1C1C)
    val subtitleColor = if (isDark) Color(0xFFAAAAAA) else Color(0xFF828282)
    val searchBgColor = if (isDark) Color(0xFF2A2A2A) else Color.White
    val searchBorderColor = if (isDark) Color(0xFF3D3D3D) else Color(0xFFE8E8E8)
    val searchTextColor = if (isDark) Color.White else Color(0xFF1C1C1C)
    val searchPlaceholderColor = if (isDark) Color(0xFF888888) else Color(0xFF9E9E9E)
    val clearIconColor = if (isDark) Color(0xFFAAAAAA) else Color(0xFF757575)

    Surface(
        color = navBgColor,
        shadowElevation = if (isDark) 1.dp else 2.dp,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, navBorderColor),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 12.dp)
        ) {
            // ROW 1: BRAND LOGO + LOCATION PIN & DROPDOWN + LOGIN/PROFILE (RIGHT CORNER)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // LEFT: Tomato Brand Logo + Red Location Pin & Title/Subtitle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Preserved Tomato Brand Logo
                    TomatoBrandLogo(
                        iconSize = 26.dp,
                        showText = true,
                        textColor = Color(0xFFCB202D),
                        onClick = {
                            appState.currentScreen = com.tomatoapp.application.state.AppScreen.Home
                            appState.currentTab = BottomTab.HOME
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Subtle vertical divider between logo and location
                    Box(
                        modifier = Modifier
                            .height(22.dp)
                            .width(1.dp)
                            .background(dividerColor)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Red Location Pin
                    Icon(
                        imageVector = TomatoIcons.Nearby,
                        contentDescription = "Location",
                        tint = Color(0xFFE23744),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { appState.showSavedAddressesModal = true }
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // Location Text: Bold Title with Downward Chevron & Subtitle
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { appState.showSavedAddressesModal = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val locationTitle = appState.selectedAddress?.areaOrVillage?.ifBlank { null }
                                ?: appState.userLocation.substringAfter("• ").substringBefore(",").trim().ifBlank { null }
                                ?: appState.userCity.ifBlank { null }
                                ?: "Manav-aashram"

                            Text(
                                text = locationTitle,
                                color = titleColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.width(2.dp))

                            Icon(
                                imageVector = TomatoIcons.ChevronDown,
                                contentDescription = "Select Location",
                                tint = titleColor,
                                modifier = Modifier.size(15.dp)
                            )
                        }

                        val locationSubtitle = appState.selectedAddress?.let { "${it.cityOrDistrict}, ${it.state}" }
                            ?: if (appState.userCity.isNotBlank()) "${appState.userCity}, India" else "Mehsana,Gujrat"

                        Text(
                            text = locationSubtitle,
                            color = subtitleColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // RIGHT CORNER: Login button (if not logged in) OR Profile Avatar (if logged in)
                if (appState.isLoggedIn) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFCB202D),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (isDark) Color.White.copy(alpha = 0.35f) else Color.White
                        ),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .size(34.dp)
                            .clickable { appState.showProfileModal = true }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            val initial = appState.userName.trim().firstOrNull()?.uppercase() ?: "U"
                            Text(
                                text = initial,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFFCB202D),
                        shadowElevation = 1.dp,
                        modifier = Modifier.clickable {
                            appState.authModalInitialMode = "LOGIN"
                            appState.showPhoneLoginModal = true
                        }
                    ) {
                        Text(
                            text = "Login",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ROW 2: FULL-WIDTH ROUNDED SEARCH BAR (MIC REMOVED AS REQUESTED)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = searchBgColor,
                shadowElevation = if (isDark) 1.dp else 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, searchBorderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Vibrant Red Search Icon
                    Icon(
                        imageVector = TomatoIcons.Search,
                        contentDescription = "Search",
                        tint = Color(0xFFE23744),
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        if (appState.searchQuery.isEmpty()) {
                            Text(
                                text = "Restaurant name or Dish......",
                                fontSize = 13.5.sp,
                                color = searchPlaceholderColor
                            )
                        }
                        BasicTextField(
                            value = appState.searchQuery,
                            onValueChange = { appState.searchQuery = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = searchTextColor
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (appState.searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = TomatoIcons.Clear,
                            contentDescription = "Clear",
                            tint = clearIconColor,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { appState.searchQuery = "" }
                                .padding(2.dp)
                        )
                    }
                }
            }
        }
    }
}
