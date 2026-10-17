package com.tomatoapp.application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tomatoapp.application.state.LocalTomatoAppState
import com.tomatoapp.application.ui.theme.*

@Composable
fun UserProfileModal(
    onDismiss: () -> Unit
) {
    val appState = LocalTomatoAppState.current

    var editName by remember { mutableStateOf(appState.userName) }
    var editEmail by remember { mutableStateOf(appState.userEmail) }
    var editPhone by remember { mutableStateOf(appState.userPhone) }
    var saveFeedback by remember { mutableStateOf<String?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    // Sync orders from MongoDB whenever user opens their profile
    LaunchedEffect(Unit) {
        appState.fetchOrdersFromBackend()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 16.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Modal Header (Zomato Crimson Red theme)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFCB202D))
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                isEditing = true
                                editName = appState.userName
                                editEmail = appState.userEmail
                                editPhone = appState.userPhone
                            }
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(2.dp, Color.White),
                            shadowElevation = 4.dp,
                            modifier = Modifier.size(52.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = appState.userName.firstOrNull()?.uppercase() ?: "U",
                                    color = Color(0xFFCB202D),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = appState.userName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = TomatoIcons.Edit,
                                    contentDescription = "Edit Profile",
                                    tint = Color.White.copy(alpha = 0.9f),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Text(
                                text = if (appState.userPhone.isNotBlank()) "+91 ${appState.userPhone} • Tap to edit" else "Tap here to edit profile ➔",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onDismiss() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = TomatoIcons.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Modal Content
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // PHONE LOGIN & MONGODB SYNC SECTION
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (appState.isLoggedIn) {
                                if (appState.isDarkMode) Color(0xFF1B3B22) else ZomatoGreenLight
                            } else {
                                if (appState.isDarkMode) Color(0xFF382C10) else Color(0xFFFFF3CD)
                            },
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (appState.isLoggedIn) ZomatoGreen else (if (appState.isDarkMode) Color(0xFF8C7118) else Color(0xFFFFC107))
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = if (appState.isLoggedIn) TomatoIcons.Phone else TomatoIcons.Lock,
                                        contentDescription = "Auth",
                                        tint = if (appState.isLoggedIn) ZomatoGreen else Color(0xFFCB202D),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = if (appState.isLoggedIn) "Logged in with Phone" else "Phone Login & Register",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (appState.isLoggedIn) ZomatoGreen else (if (appState.isDarkMode) Color(0xFFFFE082) else Color(0xFF856404))
                                        )
                                        Text(
                                            text = if (appState.isLoggedIn)
                                                "+91 ${appState.userPhone} • Verified Account"
                                            else
                                                "Login via OTP to view your past orders",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        if (appState.isLoggedIn) {
                                            appState.logout()
                                        } else {
                                            appState.showPhoneLoginModal = true
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (appState.isLoggedIn) MaterialTheme.colorScheme.surface else ZomatoRed
                                    ),
                                    border = if (appState.isLoggedIn) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null,
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = if (appState.isLoggedIn) "Logout" else "Login / OTP",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (appState.isLoggedIn) MaterialTheme.colorScheme.onSurface else Color.White
                                    )
                                }
                            }
                        }
                    }
                    // THEME SWITCH SECTION (Light / Dark Mode)
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = TomatoIcons.Star,
                                        contentDescription = "Theme",
                                        tint = Color(0xFFCB202D),
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = if (appState.isDarkMode) "Dark Theme Active" else "Light Theme Active",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "Tap switch to change appearance",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                                        )
                                    }
                                }

                                Switch(
                                    checked = appState.isDarkMode,
                                    onCheckedChange = { appState.toggleDarkMode() },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = ZomatoRed
                                    )
                                )
                            }
                        }
                    }

                    // EDIT PROFILE SECTION
                    item {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "PERSONAL INFORMATION",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ZomatoTextMuted,
                                        letterSpacing = 0.5.sp
                                    )

                                    Text(
                                        text = if (isEditing) "Cancel" else "Edit",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ZomatoRed,
                                        modifier = Modifier.clickable {
                                            isEditing = !isEditing
                                            saveFeedback = null
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Name Field
                                ProfileInputField(
                                    label = "Full Name",
                                    value = editName,
                                    isEditable = isEditing,
                                    onValueChange = { editName = it }
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Email Field
                                ProfileInputField(
                                    label = "Email Address",
                                    value = editEmail,
                                    isEditable = isEditing,
                                    onValueChange = { editEmail = it }
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Phone Field
                                ProfileInputField(
                                    label = "Phone Number",
                                    value = editPhone,
                                    isEditable = isEditing,
                                    onValueChange = { editPhone = it }
                                )

                                if (isEditing) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = {
                                            appState.updateProfile(editName, editEmail, editPhone)
                                            isEditing = false
                                            saveFeedback = "Profile updated successfully! ✓"
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = ZomatoRed),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Save Changes", fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }

                                if (saveFeedback != null) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = saveFeedback.orEmpty(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ZomatoGreen
                                    )
                                }
                            }
                        }
                    }

                    // SETTINGS & PREFERENCES
                    item {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "SETTINGS & PREFERENCES",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ZomatoTextMuted,
                                    letterSpacing = 0.5.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Veg Preference
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = TomatoIcons.Check,
                                            contentDescription = "Veg",
                                            tint = ZomatoGreen,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "Pure Veg Preference",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "Show only vegetarian food",
                                                fontSize = 11.sp,
                                                color = ZomatoTextMuted
                                            )
                                        }
                                    }
                                    Switch(
                                        checked = appState.vegOnly,
                                        onCheckedChange = { appState.vegOnly = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = ZomatoGreen
                                        )
                                    )
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)

                                SettingsRowItem(
                                    icon = TomatoIcons.LocationOn,
                                    title = "Saved Addresses",
                                    subtitle = appState.selectedAddress?.let { "${it.label} • ${it.areaOrVillage}, ${it.state}" } ?: appState.userLocation,
                                    onClick = { appState.showSavedAddressesModal = true }
                                )
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                                SettingsRowItem(
                                    icon = TomatoIcons.CreditCard,
                                    title = "Payment Methods",
                                    subtitle = appState.selectedPaymentMethod,
                                    onClick = { appState.showPaymentMethodsModal = true }
                                )
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                                SettingsRowItem(
                                    icon = TomatoIcons.Notifications,
                                    title = "Order Notifications",
                                    subtitle = "${appState.notifications.size} updates • Push & SMS",
                                    onClick = { appState.showNotificationsModal = true }
                                )
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                                SettingsRowItem(
                                    icon = TomatoIcons.Phone,
                                    title = "Tomato Support 24x7",
                                    subtitle = "Instant chat assistance",
                                    onClick = { appState.showChatModal = true }
                                )
                            }
                        }
                    }

                    // YOUR ORDERS SECTION (COMPLETED & ACTIVE ORDERS FROM MONGODB)
                    item {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "YOUR ORDERS (${appState.orders.size})",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ZomatoTextMuted,
                                        letterSpacing = 0.5.sp
                                    )
                                    if (appState.orders.isNotEmpty()) {
                                        Text(
                                            text = "Refresh 🔄",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ZomatoRed,
                                            modifier = Modifier.clickable { appState.fetchOrdersFromBackend() }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                if (appState.orders.isEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "No orders placed yet",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                } else {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        appState.orders.forEach { order ->
                                            Surface(
                                                shape = RoundedCornerShape(10.dp),
                                                color = MaterialTheme.colorScheme.background,
                                                border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(modifier = Modifier.padding(12.dp)) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = order.restaurantName,
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 14.sp,
                                                            color = MaterialTheme.colorScheme.onSurface
                                                        )
                                                        Surface(
                                                            color = if (order.status == com.tomatoapp.application.model.OrderStatus.DELIVERED) ZomatoGreenLight else ZomatoRedLight,
                                                            shape = RoundedCornerShape(4.dp)
                                                        ) {
                                                            Text(
                                                                text = if (order.status == com.tomatoapp.application.model.OrderStatus.DELIVERED) "✓ DELIVERED" else order.status.title.uppercase(),
                                                                color = if (order.status == com.tomatoapp.application.model.OrderStatus.DELIVERED) ZomatoGreen else ZomatoRed,
                                                                fontSize = 10.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                            )
                                                        }
                                                    }

                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = order.items.joinToString(", ") { "${it.quantity}x ${it.dish.name}" },
                                                        fontSize = 12.sp,
                                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                                        maxLines = 1,
                                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                                    )

                                                    Spacer(modifier = Modifier.height(6.dp))
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = "₹${order.totalAmount.toInt()} • Delivery OTP: ${order.deliveryOtp}",
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = MaterialTheme.colorScheme.onSurface
                                                        )
                                                        if (order.status != com.tomatoapp.application.model.OrderStatus.DELIVERED) {
                                                            Text(
                                                                text = "Track ➔",
                                                                fontSize = 12.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = ZomatoRed,
                                                                modifier = Modifier.clickable {
                                                                    onDismiss()
                                                                    appState.currentScreen = com.tomatoapp.application.state.AppScreen.OrderTracking(order.id)
                                                                }
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // CLOSE BUTTON
                    item {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Close Profile",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Phone Login & OTP Dialog
        if (appState.showPhoneLoginModal) {
            PhoneLoginModal(onDismiss = { appState.showPhoneLoginModal = false })
        }
    }
}

@Composable
private fun ProfileInputField(
    label: String,
    value: String,
    isEditable: Boolean,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(text = label, fontSize = 11.sp, color = ZomatoTextMuted, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(2.dp))
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isEditable) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isEditable) ZomatoRed else MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.fillMaxWidth().height(38.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (isEditable) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = value,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsRowItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color = Color(0xFFCB202D),
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                shape = CircleShape,
                color = iconTint.copy(alpha = 0.12f),
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        Icon(
            imageVector = TomatoIcons.KeyboardArrowRight,
            contentDescription = "Go",
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
            modifier = Modifier.size(20.dp)
        )
    }
}
