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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomatoapp.application.data.MockData
import com.tomatoapp.application.model.OrderStatus
import com.tomatoapp.application.state.AppScreen
import com.tomatoapp.application.state.BottomTab
import com.tomatoapp.application.state.LocalTomatoAppState
import androidx.compose.ui.draw.clip
import com.tomatoapp.application.ui.components.FloatingCartBar
import com.tomatoapp.application.ui.components.RatingBadge
import com.tomatoapp.application.ui.components.RestaurantCard
import com.tomatoapp.application.ui.components.VegNonVegBadge
import com.tomatoapp.application.ui.theme.*

@Composable
fun DiningScreen() {
    val appState = LocalTomatoAppState.current

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🍽️", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Dining Out & Table Booking",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Explore best dine-in restaurants & bars near you",
                                fontSize = 12.sp,
                                color = ZomatoTextMuted
                            )
                        }
                    }
                }
            }

            // Dine-in Promos
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            Brush.horizontalGradient(listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0))),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "PRE-BOOK YOUR TABLE",
                            color = Color.Yellow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Get Up to 40% OFF on Dining Bills",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Pay via Tomato Pay at 5,000+ premium restaurants",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // POPULAR DINE-IN DISHES & SPECIALTIES
            item {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(
                        text = "POPULAR DINE-IN SPECIALTIES",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                    val popularDishes = appState.restaurants.flatMap { rest ->
                        rest.dishes.take(2).map { dish -> Pair(dish, rest) }
                    }.take(8)

                    androidx.compose.foundation.lazy.LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(popularDishes) { (dish, rest) ->
                            val qty = appState.getQuantityInCart(dish.id)
                            Surface(
                                modifier = Modifier
                                    .width(160.dp)
                                    .wrapContentHeight(),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                shadowElevation = 2.dp
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(80.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        val photoRes = dish.imageRes ?: com.tomatoapp.application.ui.components.getDishPhotoFallback(dish)
                                        androidx.compose.foundation.Image(
                                            painter = org.jetbrains.compose.resources.painterResource(photoRes),
                                            contentDescription = dish.name,
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        VegNonVegBadge(isVeg = dish.isVeg)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "★ ${dish.rating}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ZomatoGreen
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = dish.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )

                                    Text(
                                        text = rest.name,
                                        fontSize = 10.sp,
                                        color = ZomatoTextMuted,
                                        maxLines = 1
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "₹${dish.price.toInt()}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        if (qty == 0) {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = MaterialTheme.colorScheme.surface,
                                                border = androidx.compose.foundation.BorderStroke(1.dp, ZomatoRed),
                                                modifier = Modifier.clickable {
                                                    appState.addToCart(dish, rest.id)
                                                }
                                            ) {
                                                Text(
                                                    text = "ADD +",
                                                    color = ZomatoRed,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                )
                                            }
                                        } else {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = ZomatoRed
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "-",
                                                        color = Color.White,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.clickable { appState.removeFromCart(dish.id) }
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = "$qty",
                                                        color = Color.White,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = "+",
                                                        color = Color.White,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.clickable { appState.addToCart(dish, rest.id) }
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

            // ALL DINE-IN RESTAURANTS HEADER
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ALL DINE-IN RESTAURANTS (${appState.restaurants.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // DINE-IN RESTAURANTS LIST (Using RestaurantCard like HomeScreen)
            items(appState.restaurants) { rest ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    RestaurantCard(
                        restaurant = rest,
                        onClick = { appState.navigateToRestaurant(rest.id) }
                    )
                }
            }
        }

        // FLOATING CART BAR (WHEN CART HAS ITEMS IN DINING)
        if (appState.cartItemCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                FloatingCartBar(
                    itemCount = appState.cartItemCount,
                    subtotal = appState.cartSubtotal,
                    onViewCartClick = { appState.navigateToCart() }
                )
            }
        }
    }
}

@Composable
fun NightlifeScreen() {
    val appState = LocalTomatoAppState.current

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🍸", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Nightlife, Clubs & Lounges",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Explore best bars, pubs, breweries & rooftop lounges",
                                fontSize = 12.sp,
                                color = ZomatoTextMuted
                            )
                        }
                    }
                }
            }

            // Glamorous Nightlife VIP Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            Brush.horizontalGradient(listOf(Color(0xFF2C003E), Color(0xFF0F0C29))),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "✨", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "TOMATO NIGHTLIFE PASS",
                                color = Color(0xFFFFD700),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Up to 40% OFF on Drinks & Entry",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Free cocktail vouchers & VIP guestlist at 2,000+ top clubs",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // POPULAR NIGHTLIFE BITES & DRINKS
            item {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(
                        text = "POPULAR NIGHTLIFE BITES & DRINKS",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                    val nightlifeDishes = appState.restaurants.reversed().flatMap { rest ->
                        rest.dishes.take(2).map { dish -> Pair(dish, rest) }
                    }.take(8)

                    androidx.compose.foundation.lazy.LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(nightlifeDishes) { (dish, rest) ->
                            val qty = appState.getQuantityInCart(dish.id)
                            Surface(
                                modifier = Modifier
                                    .width(160.dp)
                                    .wrapContentHeight(),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                shadowElevation = 2.dp
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(80.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        val photoRes = dish.imageRes ?: com.tomatoapp.application.ui.components.getDishPhotoFallback(dish)
                                        androidx.compose.foundation.Image(
                                            painter = org.jetbrains.compose.resources.painterResource(photoRes),
                                            contentDescription = dish.name,
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        VegNonVegBadge(isVeg = dish.isVeg)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "★ ${dish.rating}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ZomatoGreen
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = dish.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )

                                    Text(
                                        text = rest.name,
                                        fontSize = 10.sp,
                                        color = ZomatoTextMuted,
                                        maxLines = 1
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "₹${dish.price.toInt()}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        if (qty == 0) {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = MaterialTheme.colorScheme.surface,
                                                border = androidx.compose.foundation.BorderStroke(1.dp, ZomatoRed),
                                                modifier = Modifier.clickable {
                                                    appState.addToCart(dish, rest.id)
                                                }
                                            ) {
                                                Text(
                                                    text = "ADD +",
                                                    color = ZomatoRed,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                )
                                            }
                                        } else {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = ZomatoRed
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "-",
                                                        color = Color.White,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.clickable { appState.removeFromCart(dish.id) }
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = "$qty",
                                                        color = Color.White,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = "+",
                                                        color = Color.White,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.clickable { appState.addToCart(dish, rest.id) }
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

            // ALL NIGHTLIFE & CLUBS HEADER
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ALL NIGHTLIFE VENUES & LOUNGES (${appState.restaurants.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // NIGHTLIFE VENUES LIST
            items(appState.restaurants) { rest ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    RestaurantCard(
                        restaurant = rest,
                        onClick = { appState.navigateToRestaurant(rest.id) }
                    )
                }
            }
        }

        // FLOATING CART BAR (WHEN CART HAS ITEMS IN NIGHTLIFE)
        if (appState.cartItemCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                FloatingCartBar(
                    itemCount = appState.cartItemCount,
                    subtotal = appState.cartSubtotal,
                    onViewCartClick = { appState.navigateToCart() }
                )
            }
        }
    }
}

@Composable
fun OrdersHistoryScreen() {
    val appState = LocalTomatoAppState.current

    // Sync all orders from backend server so placed orders are never lost
    LaunchedEffect(Unit) {
        appState.fetchOrdersFromBackend()
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
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
                            Text(text = "📦", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Your Orders",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.clickable { appState.fetchOrdersFromBackend() }
                        ) {
                            Text(
                                text = "🔄 Refresh",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            if (appState.orders.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🛍️", fontSize = 54.sp)
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "No orders yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "When you place an order, it will appear here",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            } else {
                items(appState.orders) { order ->
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        shadowElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = order.receiverName.ifBlank { appState.userName.ifBlank { "Customer" } },
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = if (appState.isDarkMode) Color(0xFF2D3748) else Color(0xFFF1F5F9),
                                            shape = RoundedCornerShape(4.dp),
                                            border = androidx.compose.foundation.BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline)
                                        ) {
                                            Text(
                                                text = "#${order.id}",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ZomatoTextMuted,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${order.restaurantName} • ${order.orderedAt}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }

                                Surface(
                                    color = if (order.status == OrderStatus.DELIVERED) ZomatoGreenLight else ZomatoRedLight,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (order.status == OrderStatus.DELIVERED) "✓ DELIVERED" else order.status.title.uppercase(),
                                        color = if (order.status == OrderStatus.DELIVERED) ZomatoGreen else ZomatoRed,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = order.items.joinToString(", ") { "${it.quantity}x ${it.dish.name}" },
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "₹${order.totalAmount.toInt()} • Delivery OTP: ${order.deliveryOtp}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Button(
                                    onClick = {
                                        appState.currentScreen = AppScreen.OrderTracking(order.id)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ZomatoRed),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = if (order.status == OrderStatus.DELIVERED) "View Details" else "Track Order",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
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

@Composable
fun ProfileScreen() {
    val appState = LocalTomatoAppState.current
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var showTermsModal by remember { mutableStateOf(false) }
    var showPrivacyModal by remember { mutableStateOf(false) }
    var showSecurityModal by remember { mutableStateOf(false) }

    // Sync past orders from backend
    LaunchedEffect(Unit) {
        appState.fetchOrdersFromBackend()
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // 1. TOP ZOMATO CRIMSON RED HEADER
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFCB202D))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top Nav Icons: Back Arrow on Left & Settings Gear on Right
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .size(38.dp)
                                    .clickable {
                                        appState.currentScreen = AppScreen.Home
                                        appState.currentTab = BottomTab.HOME
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = TomatoIcons.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .size(38.dp)
                                    .clickable { appState.showProfileModal = true }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = TomatoIcons.Settings,
                                        contentDescription = "Settings",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Circular Avatar with border and elevation
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(3.dp, Color.White),
                            shadowElevation = 6.dp,
                            modifier = Modifier
                                .size(88.dp)
                                .clickable { appState.showProfileModal = true }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.background(Color.White)
                            ) {
                                Text(
                                    text = appState.userName.firstOrNull()?.uppercase() ?: "U",
                                    fontSize = 38.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFCB202D)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // User Name
                        Text(
                            text = appState.userName,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = if (appState.isLoggedIn && appState.userPhone.isNotBlank()) "+91 ${appState.userPhone}" else "Tap to edit profile",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.clickable { appState.showProfileModal = true }
                        )
                    }
                }
            }

            // 2. MAIN SHEET CONTAINER (ROUNDED TOP CORNERS, FIGMA DESIGN)
            item {
                Surface(
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-16).dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 22.dp)
                    ) {
                        if (toastMessage != null) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (appState.isDarkMode) Color(0xFF1B3B22) else ZomatoGreenLight,
                                border = androidx.compose.foundation.BorderStroke(1.dp, ZomatoGreen),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = toastMessage.orEmpty(),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (appState.isDarkMode) Color(0xFF81C784) else ZomatoGreen
                                    )
                                    Text(
                                        text = "✕",
                                        fontSize = 13.sp,
                                        color = if (appState.isDarkMode) Color(0xFF81C784) else ZomatoGreen,
                                        modifier = Modifier.clickable { toastMessage = null }
                                    )
                                }
                            }
                        }

                        // ================= SECTION 1: ACCOUNT =================
                        FigmaSectionHeader("Account")

                        FigmaOptionRow(
                            icon = TomatoIcons.Person,
                            title = "Edit profile",
                            trailingType = TrailingType.Chevron,
                            onClick = { appState.showProfileModal = true }
                        )

                        FigmaOptionRow(
                            icon = TomatoIcons.Lock,
                            title = "Security",
                            trailingType = TrailingType.Chevron,
                            onClick = { showSecurityModal = true }
                        )

                        FigmaOptionRow(
                            icon = TomatoIcons.Notifications,
                            title = "Notifications",
                            trailingType = TrailingType.Switch(
                                checked = appState.notificationsEnabled,
                                onCheckedChange = { appState.notificationsEnabled = it }
                            )
                        )

                        FigmaOptionRow(
                            icon = TomatoIcons.Info,
                            title = "Privacy",
                            trailingType = TrailingType.Chevron,
                            onClick = { showPrivacyModal = true }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // ================= SECTION 2: SUPPORT & ABOUT =================
                        FigmaSectionHeader("Support & About")

                        FigmaOptionRow(
                            icon = TomatoIcons.ShoppingCart,
                            title = "My Orders & Subscriptions",
                            subtitle = if (appState.orders.isNotEmpty()) "${appState.orders.size} active/past orders" else "No active subscriptions",
                            trailingType = TrailingType.Chevron,
                            onClick = { appState.currentTab = BottomTab.ORDERS }
                        )

                        FigmaOptionRow(
                            icon = TomatoIcons.Phone,
                            title = "Help & Support",
                            subtitle = "24x7 Customer assistance",
                            trailingType = TrailingType.Chevron,
                            onClick = { appState.showChatModal = true }
                        )

                        FigmaOptionRow(
                            icon = TomatoIcons.Info,
                            title = "Terms and Policies",
                            trailingType = TrailingType.Chevron,
                            onClick = { showTermsModal = true }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // ================= SECTION 3: CACHE & CELLULAR =================
                        FigmaSectionHeader("Cache & cellular")

                        FigmaOptionRow(
                            icon = TomatoIcons.Delete,
                            title = "Free up space",
                            subtitle = "Clear temporary cached files",
                            trailingType = TrailingType.Chevron,
                            onClick = {
                                toastMessage = "Cache cleaned successfully! 18.4 MB freed."
                            }
                        )

                        FigmaOptionRow(
                            icon = TomatoIcons.Refresh,
                            title = "Data Saver",
                            subtitle = "Optimize images for lower data consumption",
                            trailingType = TrailingType.Switch(
                                checked = appState.dataSaverEnabled,
                                onCheckedChange = { appState.dataSaverEnabled = it }
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // ================= SECTION 4: ACTIONS =================
                        FigmaSectionHeader("Actions")

                        FigmaOptionRow(
                            icon = TomatoIcons.Star,
                            title = "Dark Theme",
                            subtitle = if (appState.isDarkMode) "Dark theme enabled" else "Light theme enabled",
                            trailingType = TrailingType.Switch(
                                checked = appState.isDarkMode,
                                onCheckedChange = { appState.toggleDarkMode() }
                            )
                        )

                        FigmaOptionRow(
                            icon = TomatoIcons.ExitToApp,
                            title = if (appState.isLoggedIn) "Log out" else "Log in / Sign up",
                            subtitle = if (appState.isLoggedIn) "Account: ${appState.userPhone}" else "Login to access saved orders & profile",
                            trailingType = TrailingType.Chevron,
                            onClick = {
                                if (appState.isLoggedIn) {
                                    appState.logout()
                                    toastMessage = "Logged out successfully"
                                } else {
                                    appState.authModalInitialMode = "LOGIN"
                                    appState.showPhoneLoginModal = true
                                }
                            }
                        )
                    }
                }
            }
        }

        // Informational dialogs for Security, Terms, and Privacy
        if (showSecurityModal) {
            AlertDialog(
                onDismissRequest = { showSecurityModal = false },
                title = { Text("Account Security", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text(text = "🛡️ Status: Protected & Verified", fontWeight = FontWeight.SemiBold, color = ZomatoGreen)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "• Phone OTP Authentication is active.\n• Cloud Database encrypted with TLS.\n• Past orders and delivery addresses are safely synced.")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showSecurityModal = false }) {
                        Text("OK", color = ZomatoRed, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        if (showTermsModal) {
            AlertDialog(
                onDismissRequest = { showTermsModal = false },
                title = { Text("Terms & Policies", fontWeight = FontWeight.Bold) },
                text = {
                    Text("By using Tomato, you agree to our terms of service, contactless food delivery policy, and restaurant hygiene guidelines.")
                },
                confirmButton = {
                    TextButton(onClick = { showTermsModal = false }) {
                        Text("Close", color = ZomatoRed, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        if (showPrivacyModal) {
            AlertDialog(
                onDismissRequest = { showPrivacyModal = false },
                title = { Text("Privacy Policy", fontWeight = FontWeight.Bold) },
                text = {
                    Text("Your privacy is our utmost priority. Your phone number and address are used exclusively for food deliveries and order tracking.")
                },
                confirmButton = {
                    TextButton(onClick = { showPrivacyModal = false }) {
                        Text("Got it", color = ZomatoRed, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

private sealed class TrailingType {
    data object Chevron : TrailingType()
    data class Switch(val checked: Boolean, val onCheckedChange: (Boolean) -> Unit) : TrailingType()
}

@Composable
private fun FigmaSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun FigmaOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color = Color(0xFFCB202D),
    title: String,
    subtitle: String? = null,
    trailingType: TrailingType,
    onClick: () -> Unit = {}
) {
    val rowModifier = when (trailingType) {
        is TrailingType.Chevron -> Modifier.clickable { onClick() }
        is TrailingType.Switch -> Modifier
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(rowModifier)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                shape = CircleShape,
                color = iconTint.copy(alpha = 0.12f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                    )
                }
            }
        }

        when (trailingType) {
            is TrailingType.Chevron -> {
                Icon(
                    imageVector = TomatoIcons.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }
            is TrailingType.Switch -> {
                Switch(
                    checked = trailingType.checked,
                    onCheckedChange = trailingType.onCheckedChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFCB202D),
                        checkedBorderColor = Color(0xFFCB202D)
                    )
                )
            }
        }
    }
}
