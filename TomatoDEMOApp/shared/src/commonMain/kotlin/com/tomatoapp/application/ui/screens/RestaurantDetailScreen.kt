package com.tomatoapp.application.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomatoapp.application.data.MockData
import com.tomatoapp.application.model.Dish
import com.tomatoapp.application.state.LocalTomatoAppState
import com.tomatoapp.application.ui.components.*
import com.tomatoapp.application.ui.theme.*

@Composable
fun RestaurantDetailScreen(restaurantId: String) {
    val appState = LocalTomatoAppState.current
    val restaurant = appState.restaurants.find { it.id == restaurantId } ?: MockData.restaurants.find { it.id == restaurantId }

    if (restaurant == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Restaurant not found", color = ZomatoTextDark)
        }
        return
    }

    var menuVegOnly by remember { mutableStateOf(false) }
    var menuSearchQuery by remember { mutableStateOf("") }
    var showAddDishModal by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }

    val filteredDishes = restaurant.dishes.filter { dish ->
        val matchesVeg = if (menuVegOnly) dish.isVeg else true
        val matchesSearch = if (menuSearchQuery.trim().isEmpty()) {
            true
        } else {
            dish.name.contains(menuSearchQuery.trim(), ignoreCase = true) ||
                    dish.description.contains(menuSearchQuery.trim(), ignoreCase = true) ||
                    dish.category.contains(menuSearchQuery.trim(), ignoreCase = true)
        }
        matchesVeg && matchesSearch
    }

    // Group dishes by category
    val dishesByCategory = filteredDishes.groupBy { it.category }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = if (appState.cartItemCount > 0) 120.dp else 80.dp)
        ) {
            // 1. TOP NAVBAR (Color & design matching dashboard navbar: Clean white in Light Mode, Dark in Dark Mode)
            item {
                val isDark = appState.isDarkMode
                val navBg = if (isDark) Color(0xFF1E1E1E) else Color.White
                val navBorder = if (isDark) Color(0xFF2C2C2C) else Color(0xFFF0F0F0)
                val onNavColor = if (isDark) Color.White else Color(0xFF1C1C1C)
                val subtitleColor = if (isDark) Color(0xFFAAAAAA) else Color(0xFF828282)

                Surface(
                    color = navBg,
                    shadowElevation = if (isDark) 1.dp else 2.dp,
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, navBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                // Back Button (Squircle dashboard style)
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isDark) Color(0xFF2A2A2A) else Color(0xFFF7F7F7),
                                    border = androidx.compose.foundation.BorderStroke(0.8.dp, navBorder),
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clickable { appState.navigateBack() }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "❮",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = onNavColor
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = restaurant.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = onNavColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${restaurant.deliveryTimeMinutes} mins • ${restaurant.address.take(28)}",
                                        fontSize = 11.sp,
                                        color = subtitleColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Search Menu Button (Squircle dashboard style)
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isDark) Color(0xFF2A2A2A) else Color(0xFFF7F7F7),
                                border = androidx.compose.foundation.BorderStroke(0.8.dp, navBorder),
                                modifier = Modifier
                                    .size(38.dp)
                                    .clickable { showSearchBar = !showSearchBar }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = TomatoIcons.Search,
                                        contentDescription = "Search menu",
                                        tint = Color(0xFFCB202D),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        // Collapsible Search Bar (Dashboard style)
                        if (showSearchBar) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = if (isDark) Color(0xFF2A2A2A) else Color(0xFFF5F5F5),
                                border = androidx.compose.foundation.BorderStroke(0.8.dp, navBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = TomatoIcons.Search,
                                        contentDescription = "Search",
                                        tint = Color(0xFFCB202D),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    androidx.compose.foundation.text.BasicTextField(
                                        value = menuSearchQuery,
                                        onValueChange = { menuSearchQuery = it },
                                        singleLine = true,
                                        textStyle = androidx.compose.ui.text.TextStyle(
                                            color = onNavColor,
                                            fontSize = 13.sp
                                        ),
                                        modifier = Modifier.weight(1f),
                                        decorationBox = { innerTextField ->
                                            if (menuSearchQuery.isEmpty()) {
                                                Text(
                                                    "Search in ${restaurant.name}...",
                                                    color = subtitleColor,
                                                    fontSize = 13.sp
                                                )
                                            }
                                            innerTextField()
                                        }
                                    )
                                    if (menuSearchQuery.isNotEmpty()) {
                                        Text(
                                            text = "✕",
                                            color = subtitleColor,
                                            fontSize = 14.sp,
                                            modifier = Modifier.clickable { menuSearchQuery = "" }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 2. DIRECTLY STARTS WITH "Popular" SECTION (Intermediary banners & add items removed as requested)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Popular",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    // Clean compact Veg-only switch
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Veg only",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = menuVegOnly,
                            onCheckedChange = { menuVegOnly = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF2E7D32),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFD6D6D6)
                            )
                        )
                    }
                }
            }

            // 3. DISHES LIST GROUPED BY CATEGORY
            if (filteredDishes.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No dishes match your filter",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                dishesByCategory.forEach { (categoryName, dishList) ->
                    item {
                        Surface(
                            color = if (appState.isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF1F2F4),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "$categoryName (${dishList.size})".uppercase(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (appState.isDarkMode) Color(0xFFB0B0B0) else Color(0xFF757575),
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    items(dishList) { dish ->
                        val qty = appState.getQuantityInCart(dish.id)
                        DishCard(
                            dish = dish,
                            quantityInCart = qty,
                            onAddClick = { appState.addToCart(dish, restaurant.id) },
                            onRemoveClick = { appState.removeFromCart(dish.id) }
                        )
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }

        // 4. FLOATING "➕ Add Item" BUTTON (Placed in place of Browse Menu as requested)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (appState.cartItemCount > 0) 72.dp else 20.dp)
        ) {
            Surface(
                color = Color(0xFFCB202D),
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 6.dp,
                modifier = Modifier.clickable { showAddDishModal = true }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Add Item",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 5. STICKY FLOATING CART BAR (When items exist in cart)
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

        // 6. ADD DISH MODAL (Opens when clicking the floating "Add Item" button)
        if (showAddDishModal) {
            AddDishModal(
                restaurantId = restaurant.id,
                restaurantName = restaurant.name,
                onDismiss = { showAddDishModal = false }
            )
        }
    }
}
