package com.tomatoapp.application.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomatoapp.application.model.OrderStatus
import com.tomatoapp.application.state.AppScreen
import com.tomatoapp.application.state.LocalTomatoAppState
import com.tomatoapp.application.ui.components.OrderChatModal
import com.tomatoapp.application.ui.components.VegNonVegBadge
import com.tomatoapp.application.ui.theme.*

@Composable
fun OrderTrackingScreen(orderId: String) {
    val appState = LocalTomatoAppState.current
    val order = appState.orders.find { it.id == orderId } ?: appState.activeOrder

    var showChat by remember { mutableStateOf(false) }

    // Live polling from backend (TomatoDEMOServer) to sync status updates, OTP verification, and chat messages in real time
    LaunchedEffect(order?.id) {
        if (order != null) {
            appState.syncActiveOrderWithBackend()
            appState.syncChatMessagesWithBackend(order.id)
            while (true) {
                kotlinx.coroutines.delay(2000)
                appState.syncActiveOrderWithBackend()
                appState.syncChatMessagesWithBackend(order.id)
            }
        }
    }

    if (order == null) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Order details not found", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { appState.currentScreen = AppScreen.Home },
                    colors = ButtonDefaults.buttonColors(containerColor = ZomatoRed)
                ) {
                    Text("Go to Home", color = Color.White)
                }
            }
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Header Bar
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "←",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .clickable { appState.currentScreen = AppScreen.Home }
                                    .padding(end = 12.dp)
                            )
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = order.receiverName.ifBlank { appState.userName.ifBlank { "Customer" } },
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = if (appState.isDarkMode) Color(0xFF2D3748) else Color(0xFFF1F5F9),
                                        shape = RoundedCornerShape(4.dp),
                                        border = androidx.compose.foundation.BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline)
                                    ) {
                                        Text(
                                            text = "#${order.id}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ZomatoTextMuted,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Live Order Tracking",
                                    fontSize = 11.sp,
                                    color = ZomatoTextMuted
                                )
                            }
                        }

                        // Direct Chat with Admin Header Button
                        if (order.status != OrderStatus.DELIVERED) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = ZomatoRedLight,
                                border = androidx.compose.foundation.BorderStroke(1.dp, ZomatoRed),
                                modifier = Modifier.clickable { showChat = true }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "💬", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Chat Admin",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ZomatoRed
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // CLEAN DELIVERY OTP CARD (Only OTP display as requested)
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (order.isOtpVerified) ZomatoGreen else ZomatoRed
                    ),
                    shadowElevation = 3.dp
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (order.isOtpVerified) "Delivery OTP Verified ✓" else "Your Delivery OTP",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (order.isOtpVerified) ZomatoGreen else MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Clean prominent OTP Box
                        Surface(
                            color = if (order.isOtpVerified) ZomatoGreenLight else MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (order.isOtpVerified) ZomatoGreen else ZomatoRed.copy(alpha = 0.5f)
                            )
                        ) {
                            Text(
                                text = order.deliveryOtp,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 10.sp,
                                color = if (order.isOtpVerified) ZomatoGreen else ZomatoRed,
                                modifier = Modifier.padding(horizontal = 28.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }

            // DEDICATED RESTAURANT & KITCHEN CHAT OPTION CARD
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (order.status == OrderStatus.DELIVERED) ZomatoGreen.copy(alpha = 0.5f) else ZomatoRed.copy(alpha = 0.35f)
                    ),
                    shadowElevation = 2.dp
                ) {
                    if (order.status == OrderStatus.DELIVERED) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        if (appState.isDarkMode) Color(0xFF1B3B22) else ZomatoGreenLight,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "✅", fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Order Delivered • Chat Closed",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "This order was completed successfully. Kitchen & support chat is now closed.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showChat = true }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(
                                            if (appState.isDarkMode) Color(0xFF381215) else ZomatoRedLight,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "💬", fontSize = 22.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Chat with Kitchen & Admin",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Spice customization, cutlery, or timing updates",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            Button(
                                onClick = { showChat = true },
                                colors = ButtonDefaults.buttonColors(containerColor = ZomatoRed),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Chat ➔", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            // ETA & PROGRESS STEPPER
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (order.status == OrderStatus.DELIVERED) "Order Delivered!" else "Estimated Delivery",
                                    fontSize = 12.sp,
                                    color = ZomatoTextMuted
                                )
                                Text(
                                    text = if (order.status == OrderStatus.DELIVERED) "Enjoy your meal 🎉" else "${order.etaMinutes} mins",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (order.status == OrderStatus.DELIVERED) ZomatoGreen else ZomatoTextDark
                                )
                            }
                            Surface(
                                shape = CircleShape,
                                color = if (order.status == OrderStatus.DELIVERED) ZomatoGreenLight else ZomatoRedLight,
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = if (order.status == OrderStatus.DELIVERED) "🎉" else "🛵",
                                        fontSize = 22.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Tracking Stepper
                        val steps = OrderStatus.values()
                        steps.forEachIndexed { index, step ->
                            val isCompleted = order.status.step >= step.step
                            val isCurrent = order.status == step

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Surface(
                                        shape = CircleShape,
                                        color = when {
                                            isCompleted -> ZomatoGreen
                                            else -> MaterialTheme.colorScheme.outline
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            if (isCompleted) {
                                                Text(text = "✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .background(Color.White, CircleShape)
                                                )
                                            }
                                        }
                                    }

                                    if (index < steps.size - 1) {
                                        Box(
                                            modifier = Modifier
                                                .width(2.dp)
                                                .height(34.dp)
                                                .background(
                                                    if (order.status.step > step.step) ZomatoGreen else MaterialTheme.colorScheme.outline
                                                )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.padding(bottom = if (index < steps.size - 1) 18.dp else 0.dp)) {
                                    Text(
                                        text = step.title,
                                        fontSize = 14.sp,
                                        fontWeight = if (isCurrent) FontWeight.ExtraBold else if (isCompleted) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isCurrent) ZomatoTextDark else if (isCompleted) ZomatoTextDark else ZomatoTextLight
                                    )
                                    Text(
                                        text = step.subtitle,
                                        fontSize = 11.sp,
                                        color = if (isCurrent) ZomatoGreen else ZomatoTextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // DELIVERY PARTNER DETAILS CARD (Hidden when DELIVERED)
            if (order.status != OrderStatus.DELIVERED) {
                item {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = "🛵", fontSize = 22.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = order.deliveryPartnerName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Delivery Partner • 4.8 ★",
                                        fontSize = 11.sp,
                                        color = ZomatoTextMuted
                                    )
                                }
                            }

                            // Call Action Button
                            Surface(
                                shape = CircleShape,
                                color = ZomatoGreenLight,
                                border = androidx.compose.foundation.BorderStroke(1.dp, ZomatoGreen),
                                modifier = Modifier
                                    .size(38.dp)
                                    .clickable {
                                        appState.triggerNotification(
                                            "Calling Delivery Partner",
                                            "Connecting call to ${order.deliveryPartnerName} (${order.deliveryPartnerPhone})...",
                                            order.id
                                        )
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = "📞", fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
            }

            // ORDER ITEMS & BILL SUMMARY
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = order.restaurantName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = order.restaurantAddress,
                            fontSize = 11.sp,
                            color = ZomatoTextMuted
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Items list
                        if (order.items.isNotEmpty()) {
                            order.items.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        VegNonVegBadge(isVeg = item.dish.isVeg)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${item.quantity}x ${item.dish.name}",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Text(
                                        text = "₹${(item.dish.price * item.quantity).toInt()}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = "Order placed via partner store",
                                fontSize = 12.sp,
                                color = ZomatoTextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Total Paid", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "₹${order.totalAmount.toInt()}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ZomatoGreen
                            )
                        }
                    }
                }
            }

            // DELIVERY ADDRESS DETAILS
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (appState.isDarkMode) Color(0xFF381215) else Color(0xFFFDE8E9),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = TomatoIcons.MapMarkerAlt,
                                    contentDescription = "Delivering to",
                                    tint = Color(0xFFCB202D),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Delivering to",
                                fontSize = 11.sp,
                                color = ZomatoTextMuted
                            )
                            Text(
                                text = order.deliveryAddress,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Bottom Sticky Action Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (order.status != OrderStatus.DELIVERED) {
                    Button(
                        onClick = { showChat = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ZomatoRed),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "💬 Chat with Admin",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ZomatoRed
                        )
                    }
                }

                Button(
                    onClick = { appState.currentScreen = AppScreen.Home },
                    modifier = Modifier
                        .then(if (order.status == OrderStatus.DELIVERED) Modifier.fillMaxWidth() else Modifier.weight(1.3f))
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ZomatoRed),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Back to Home",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Direct Chat Dialog
        if (showChat) {
            OrderChatModal(
                order = order,
                onDismiss = { showChat = false }
            )
        }
    }
}
