package com.tomatoapp.application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomatoapp.application.state.BottomTab
import com.tomatoapp.application.state.LocalTomatoAppState
import com.tomatoapp.application.ui.theme.*

@Composable
fun FloatingCartBar(
    itemCount: Int,
    subtotal: Double,
    onViewCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (itemCount <= 0) return

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onViewCartClick() },
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFCB202D),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "$itemCount ${if (itemCount == 1) "Item" else "Item"}",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "₹${subtotal.toInt()}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Next",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "➔",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Authentic Zomato Bottom Navigation Bar (Matching zomato-home.png)
 * Features 4 Tabs: Home, Nearby, Cart (with red badge), Profile
 * Active tab highlighted in vibrant Zomato Crimson Red (Color(0xFFCB202D)).
 */
@Composable
fun ZomatoBottomNavBar(
    currentTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val appState = LocalTomatoAppState.current
    val activeRed = Color(0xFFCB202D)
    val inactiveGrey = if (appState.isDarkMode) Color(0xFFA0A0A0) else Color(0xFF757575)

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomTab.values().forEach { tab ->
                val isSelected = tab == currentTab

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 12.dp, vertical = 2.dp)
                ) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        val tabIcon = when (tab) {
                            BottomTab.HOME -> TomatoIcons.Home
                            BottomTab.NEARBY -> TomatoIcons.Nearby
                            BottomTab.CART -> TomatoIcons.Cart
                            BottomTab.PROFILE -> TomatoIcons.Profile
                        }

                        Icon(
                            imageVector = tabIcon,
                            contentDescription = tab.title,
                            tint = if (isSelected) activeRed else inactiveGrey,
                            modifier = Modifier.size(24.dp)
                        )

                        // Red Cart Badge when items exist (matching zomato-home.png badge '3')
                        if (tab == BottomTab.CART && appState.cartItemCount > 0) {
                            Box(
                                modifier = Modifier
                                    .offset(x = 10.dp, y = (-4).dp)
                                    .size(17.dp)
                                    .background(activeRed, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${appState.cartItemCount}",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = tab.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) activeRed else inactiveGrey
                    )
                }
            }
        }
    }
}
