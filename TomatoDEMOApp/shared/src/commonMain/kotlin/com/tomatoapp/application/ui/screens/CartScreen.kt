package com.tomatoapp.application.ui.screens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomatoapp.application.data.MockData
import com.tomatoapp.application.state.LocalTomatoAppState
import com.tomatoapp.application.ui.components.VegNonVegBadge
import com.tomatoapp.application.ui.components.PaymentBrandBadge
import com.tomatoapp.application.ui.theme.*

@Composable
fun CartScreen() {
    val appState = LocalTomatoAppState.current
    val isDark = appState.isDarkMode
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val screenBg = MaterialTheme.colorScheme.background
    val textMuted = if (isDark) Color(0xFFAAAAAA) else Color(0xFF757575)
    val cardBorder = MaterialTheme.colorScheme.outline
    val innerChipBg = if (isDark) Color(0xFF262626) else Color(0xFFF5F5F5)

    var couponInput by remember { mutableStateOf("") }
    var couponMessage by remember { mutableStateOf<String?>(null) }
    var customTipInput by remember { mutableStateOf("") }
    var showCustomTipDialog by remember { mutableStateOf(false) }

    if (appState.cartItems.isEmpty()) {
        // Empty Cart UI
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(screenBg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "🛒", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your Cart is Empty",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = onSurfaceColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Good food is always waiting for you. Find your favorite dishes and order now!",
                    fontSize = 14.sp,
                    color = textMuted,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { appState.navigateBack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCB202D)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Browse Restaurants", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(screenBg)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 140.dp)
        ) {
            // Top Bar - Crimson Red with Centered "Cart"
            item {
                Surface(
                    color = Color(0xFFCB202D),
                    shadowElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Squircle back button
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isDark) Color(0xFF2C2C2C) else Color.White,
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .size(38.dp)
                                .clickable { appState.navigateBack() }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "❮",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else Color(0xFF333333)
                                )
                            }
                        }

                        // Centered "Cart" title
                        Text(
                            text = "Cart",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        // Placeholder for symmetry
                        Spacer(modifier = Modifier.size(38.dp))
                    }
                }
            }

            // Cart Items List
            item {
                Surface(
                    color = surfaceColor,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        appState.cartItems.values.forEach { cartItem ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Food thumbnail on left
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = innerChipBg,
                                        modifier = Modifier.size(54.dp)
                                    ) {
                                        val photoRes = cartItem.dish.imageRes ?: com.tomatoapp.application.ui.components.getDishPhotoFallback(cartItem.dish)
                                        androidx.compose.foundation.Image(
                                            painter = org.jetbrains.compose.resources.painterResource(photoRes),
                                            contentDescription = cartItem.dish.name,
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = cartItem.dish.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = onSurfaceColor
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            // Crossed-out original price
                                            val origPrice = (cartItem.dish.price * 1.25).toInt()
                                            Text(
                                                text = "₹$origPrice",
                                                fontSize = 12.sp,
                                                color = textMuted,
                                                textDecoration = TextDecoration.LineThrough
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            // Current price
                                            Text(
                                                text = "₹${cartItem.dish.price.toInt()}",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = onSurfaceColor
                                            )
                                        }
                                    }
                                }

                                // Circular Red Stepper: [-] Qty [+]
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Minus button
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .border(1.5.dp, Color(0xFFCB202D), CircleShape)
                                            .clickable { appState.removeFromCart(cartItem.dish.id) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "−",
                                            color = Color(0xFFCB202D),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    // Quantity
                                    Text(
                                        text = "${cartItem.quantity}",
                                        color = onSurfaceColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    // Plus button
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .border(1.5.dp, Color(0xFFCB202D), CircleShape)
                                            .clickable { appState.addToCart(cartItem.dish, cartItem.restaurantId.ifBlank { "rest_1" }) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "+",
                                            color = Color(0xFFCB202D),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            HorizontalDivider(color = cardBorder, thickness = 0.6.dp)
                        }

                        // "Write instructions for restaurant" card
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        ) {
                            Text(text = "📝", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                if (appState.cookingInstructions.isEmpty()) {
                                    Text(
                                        text = "Write instructions for restaurant",
                                        fontSize = 12.sp,
                                        color = textMuted
                                    )
                                }
                                BasicTextField(
                                    value = appState.cookingInstructions,
                                    onValueChange = { appState.cookingInstructions = it },
                                    singleLine = true,
                                    textStyle = TextStyle(fontSize = 12.sp, color = onSurfaceColor),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }

            // Promo code & Offers section
            item {
                Surface(
                    color = surfaceColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "🏷️", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Offers & Promo Codes",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = onSurfaceColor
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (appState.appliedCoupon != null) {
                            Surface(
                                color = if (isDark) Color(0xFF1B3820) else Color(0xFFE8F5E9),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4CAF50)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "🎉", fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = "${appState.appliedCoupon?.code} APPLIED",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = if (isDark) Color(0xFFB0FFB5) else Color(0xFF2E7D32)
                                            )
                                            Text(
                                                text = "You saved ₹${appState.discountAmount.toInt()} on this order!",
                                                fontSize = 11.sp,
                                                color = if (isDark) Color(0xFFE0E0E0) else Color(0xFF333333)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "REMOVE",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color(0xFFCB202D),
                                        modifier = Modifier.clickable {
                                            appState.removeCoupon()
                                            couponMessage = null
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Manual promo code input
                        Surface(
                            color = innerChipBg,
                            shape = RoundedCornerShape(24.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 14.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        if (couponInput.isEmpty()) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "Enter promo code (e.g. FLAT60)",
                                                    fontSize = 13.sp,
                                                    color = textMuted
                                                )
                                            }
                                        }
                                        BasicTextField(
                                            value = couponInput,
                                            onValueChange = { couponInput = it.uppercase() },
                                            singleLine = true,
                                            textStyle = TextStyle(
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = onSurfaceColor
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        val codeToApply = if (couponInput.isBlank()) "FLAT60" else couponInput
                                        couponMessage = appState.applyCoupon(codeToApply)
                                        couponInput = ""
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCB202D)),
                                    shape = RoundedCornerShape(20.dp),
                                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text("Apply", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                                }
                            }
                        }

                        if (couponMessage != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = couponMessage.orEmpty(),
                                fontSize = 12.sp,
                                color = if (appState.appliedCoupon != null) Color(0xFF2E7D32) else Color(0xFFCB202D)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Available Offers for You:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = textMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // 1-tap clickable coupons list (no min order requirement)
                        MockData.coupons.values.forEach { coupon ->
                            val isApplied = appState.appliedCoupon?.code == coupon.code
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isApplied) {
                                    if (isDark) Color(0xFF1B3820) else Color(0xFFF1F8E9)
                                } else {
                                    if (isDark) Color(0xFF242424) else Color(0xFFFAFAFA)
                                },
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isApplied) Color(0xFF4CAF50) else cardBorder
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        if (isApplied) {
                                            appState.removeCoupon()
                                            couponMessage = null
                                        } else {
                                            couponMessage = appState.applyCoupon(coupon.code)
                                        }
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Surface(
                                            color = if (isApplied) Color(0xFF2E7D32) else Color(0xFFCB202D),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = coupon.code,
                                                color = Color.White,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = coupon.description,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = onSurfaceColor
                                            )
                                            Text(
                                                text = "Applicable on this order • Tap to apply",
                                                fontSize = 10.sp,
                                                color = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Surface(
                                        color = if (isApplied) Color(0xFF2E7D32) else Color(0xFFCB202D).copy(alpha = 0.08f),
                                        shape = RoundedCornerShape(14.dp),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isApplied) Color(0xFF2E7D32) else Color(0xFFCB202D)
                                        ),
                                        modifier = Modifier.clickable {
                                            if (isApplied) {
                                                appState.removeCoupon()
                                                couponMessage = null
                                            } else {
                                                couponMessage = appState.applyCoupon(coupon.code)
                                            }
                                        }
                                    ) {
                                        Text(
                                            text = if (isApplied) "APPLIED ✓" else "APPLY",
                                            color = if (isApplied) Color.White else Color(0xFFCB202D),
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Tip delivery partner section: "Tip your hunger saviour"
            item {
                Surface(
                    color = surfaceColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Tip your hunger saviour",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurfaceColor
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Thank your delivery partner by leaving them a tips.",
                            fontSize = 11.sp,
                            color = textMuted
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Row of chips: [₹20] [₹30] [₹50] [Other]
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf(20, 30, 50).forEach { tip ->
                                val isSelected = appState.deliveryTip == tip
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) (if (isDark) Color(0xFF381518) else Color(0xFFFFF0F2)) else innerChipBg,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.2.dp,
                                        if (isSelected) Color(0xFFCB202D) else cardBorder
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            appState.deliveryTip = if (isSelected) 0 else tip
                                        }
                                ) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "₹$tip",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color(0xFFCB202D) else onSurfaceColor
                                        )
                                    }
                                }
                            }

                            // [Other] chip
                            val isOtherSelected = appState.deliveryTip !in listOf(0, 20, 30, 50)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isOtherSelected) (if (isDark) Color(0xFF381518) else Color(0xFFFFF0F2)) else innerChipBg,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.2.dp,
                                    if (isOtherSelected) Color(0xFFCB202D) else cardBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { showCustomTipDialog = true }
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isOtherSelected) "₹${appState.deliveryTip}" else "Other",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isOtherSelected) Color(0xFFCB202D) else onSurfaceColor
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bill Details Card
            item {
                Surface(
                    color = surfaceColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 16.dp, end = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Bill Details",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurfaceColor
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        BillRow("Item Amt", "₹${appState.cartSubtotal.toInt()}")
                        BillRow("Delivery Fee", if (appState.deliveryFee == 0.0) "FREE" else "₹${appState.deliveryFee.toInt()}")
                        BillRow("Taxes & Charges", "₹${appState.taxesAndCharges.toInt()}")

                        if (appState.deliveryTip > 0) {
                            BillRow("Delivery Tip", "₹${appState.deliveryTip}")
                        }

                        if (appState.discountAmount > 0) {
                            BillRow(
                                "Coupon Discount",
                                "-₹${appState.discountAmount.toInt()}",
                                isHighlight = true
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = cardBorder, thickness = 0.8.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "To Pay",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = onSurfaceColor
                            )
                            Text(
                                text = "₹${appState.totalAmount.toInt()}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = onSurfaceColor
                            )
                        }
                    }
                }
            }
        }

        // Bottom Sticky Checkout Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = surfaceColor,
            shadowElevation = 12.dp,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, cardBorder)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                // Delivery Address & Payment selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { appState.showSavedAddressesModal = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = TomatoIcons.MapMarkerAlt,
                            contentDescription = "Delivery Location",
                            tint = Color(0xFFCB202D),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = appState.selectedAddress?.let { "To: ${it.label}" } ?: "Deliver: Home",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurfaceColor
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CHANGE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCB202D)
                        )
                    }

                    Row(
                        modifier = Modifier.clickable { appState.showPaymentMethodsModal = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PaymentBrandBadge(
                            methodKey = appState.selectedPaymentMethod,
                            size = 20.dp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = appState.selectedPaymentMethod,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurfaceColor
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CHANGE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCB202D)
                        )
                    }
                }

                // Place Order Button
                Button(
                    onClick = {
                        if (!appState.isLoggedIn) {
                            appState.showPhoneLoginModal = true
                            appState.triggerNotification(
                                title = "Login Required",
                                message = "Please login with your phone number to place your order!",
                                orderId = "LOGIN"
                            )
                        } else {
                            appState.placeOrder()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCB202D)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "₹${appState.totalAmount.toInt()} Total",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (appState.isLoggedIn) "PLACE ORDER" else "LOGIN TO ORDER",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "➔", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // Custom Tip Dialog
        if (showCustomTipDialog) {
            AlertDialog(
                onDismissRequest = { showCustomTipDialog = false },
                containerColor = surfaceColor,
                title = { Text("Add Custom Tip", fontWeight = FontWeight.Bold, color = onSurfaceColor) },
                text = {
                    Column {
                        Text("Enter tip amount for your delivery partner:", color = textMuted)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = customTipInput,
                            onValueChange = { customTipInput = it.filter { ch -> ch.isDigit() } },
                            placeholder = { Text("e.g. 40", color = textMuted) },
                            singleLine = true,
                            textStyle = TextStyle(color = onSurfaceColor)
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        val tipVal = customTipInput.toIntOrNull() ?: 0
                        appState.deliveryTip = tipVal
                        showCustomTipDialog = false
                    }) {
                        Text("Add Tip", color = Color(0xFFCB202D), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCustomTipDialog = false }) {
                        Text("Cancel", color = textMuted)
                    }
                }
            )
        }
    }
}

@Composable
private fun BillRow(label: String, value: String, isHighlight: Boolean = false) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = if (isHighlight) Color(0xFF2E7D32) else onSurface.copy(alpha = 0.75f)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlight) Color(0xFF2E7D32) else onSurface
        )
    }
}
