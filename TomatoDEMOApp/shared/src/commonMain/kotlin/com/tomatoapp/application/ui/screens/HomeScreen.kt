package com.tomatoapp.application.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomatoapp.application.data.MockData
import com.tomatoapp.application.state.BottomTab
import com.tomatoapp.application.state.LocalTomatoAppState
import com.tomatoapp.application.state.SortOption
import com.tomatoapp.application.ui.components.*
import com.tomatoapp.application.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val appState = LocalTomatoAppState.current
    var bannerToast by remember { mutableStateOf<String?>(null) }
    var showAllCategoriesModal by remember { mutableStateOf(false) }
    var showAllPopularModal by remember { mutableStateOf(false) }
    val brandsListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Filter restaurants based on state (sourced from server via AppState)
    val filteredRestaurants = appState.restaurants.filter { rest ->
        // Veg Only Filter
        val matchesVeg = if (appState.vegOnly) rest.isPureVeg else true

        // Category Filter
        val matchesCategory = if (appState.selectedCategoryId == "all") {
            true
        } else {
            val catName = MockData.categories.find { it.id == appState.selectedCategoryId }?.name.orEmpty()
            rest.cuisines.any { it.contains(catName, ignoreCase = true) } ||
                    rest.dishes.any { it.category.contains(catName, ignoreCase = true) }
        }

        // Rating 4.0+
        val matchesRating = if (appState.ratingFourPlusOnly) rest.rating >= 4.0 else true

        // Fast Delivery (<25m)
        val matchesFastDelivery = if (appState.fastDeliveryOnly) rest.deliveryTimeMinutes <= 25 else true

        // Search Query
        val query = appState.searchQuery.trim()
        val matchesSearch = if (query.isEmpty()) {
            true
        } else {
            rest.name.contains(query, ignoreCase = true) ||
                    rest.cuisines.any { it.contains(query, ignoreCase = true) } ||
                    rest.dishes.any { it.name.contains(query, ignoreCase = true) }
        }

        matchesVeg && matchesCategory && matchesRating && matchesFastDelivery && matchesSearch
    }.let { list ->
        when (appState.currentSortOption) {
            SortOption.RELEVANCE -> list
            SortOption.RATING -> list.sortedByDescending { it.rating }
            SortOption.DELIVERY_TIME -> list.sortedBy { it.deliveryTimeMinutes }
            SortOption.COST_LOW_TO_HIGH -> list.sortedBy { it.costForTwo }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // 1. TOP TOMATO NAVBAR WITH BRANDING, UNIFIED SEARCH & BREADCRUMBS
            item {
                ZomatoNavBar()
            }

            // 2. PROMOTIONAL DISCOUNT BANNER CAROUSEL (IN PLACE OF DINING OUT SECTION)
            item {
                Spacer(modifier = Modifier.height(12.dp))
                BannerCarousel(
                    banners = MockData.promotionalBanners,
                    onBannerClick = { banner ->
                        val result = appState.applyCoupon(banner.code)
                        bannerToast = "Coupon ${banner.code}: $result"
                        appState.triggerNotification(
                            title = "Promo Applied! 🎉",
                            message = "Discount code ${banner.code} activated for your order.",
                            orderId = "COUPON"
                        )
                    }
                )

                if (bannerToast != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = if (appState.isDarkMode) Color(0xFF1B3B22) else ZomatoGreenLight,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ZomatoGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🎉 ${bannerToast.orEmpty()}",
                                color = if (appState.isDarkMode) Color(0xFF81C784) else ZomatoGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "✕",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                fontSize = 12.sp,
                                modifier = Modifier.clickable { bannerToast = null }
                            )
                        }
                    }
                }
            }

            // 3. CATEGORIES SECTION (MATCHING ZOMATO-HOME.PNG)
            item {
                Column(modifier = Modifier.padding(top = 18.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Categories",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "View all",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCB202D),
                            modifier = Modifier.clickable { showAllCategoriesModal = true }
                        )
                    }
                    CategoryRow(
                        categories = MockData.categories,
                        selectedCategoryId = appState.selectedCategoryId,
                        onCategorySelect = { appState.selectedCategoryId = it }
                    )
                }
            }

            // 4. POPULAR RESTAURANTS SECTION (MATCHING ZOMATO-HOME.PNG)
            item {
                Column(modifier = Modifier.padding(top = 20.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Popular Restaurants",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "View all",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCB202D),
                            modifier = Modifier.clickable { showAllPopularModal = true }
                        )
                    }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(appState.restaurants) { rest ->
                            PopularRestaurantCard(
                                restaurant = rest,
                                onClick = { appState.navigateToRestaurant(rest.id) }
                            )
                        }
                    }
                }
            }

            // 5. FILTER PILLS
            item {
                Spacer(modifier = Modifier.height(18.dp))
                FilterBar(
                    ratingFourPlus = appState.ratingFourPlusOnly,
                    onRatingToggle = { appState.ratingFourPlusOnly = !appState.ratingFourPlusOnly },
                    fastDelivery = appState.fastDeliveryOnly,
                    onFastDeliveryToggle = { appState.fastDeliveryOnly = !appState.fastDeliveryOnly },
                    pureVeg = appState.vegOnly,
                    onPureVegToggle = { appState.vegOnly = !appState.vegOnly },
                    currentSort = appState.currentSortOption,
                    onSortToggle = {
                        val allSorts = SortOption.values()
                        val nextIndex = (appState.currentSortOption.ordinal + 1) % allSorts.size
                        appState.currentSortOption = allSorts[nextIndex]
                    },
                    onFiltersClick = { appState.showFiltersModal = true },
                    onMyShopClick = { appState.showMyShopModal = true },
                    onCuisineClick = { appState.showCuisinesModal = true }
                )
            }

            // 6. RESTAURANT SECTION HEADER
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Delivery Restaurants in ${appState.userCity}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // 7. RESPONSIVE MULTI-COLUMN RESTAURANT CARDS GRID (SCREENSHOT 2)
            if (filteredRestaurants.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🍽️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No restaurants found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try clearing search or filters to see more options",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                appState.searchQuery = ""
                                appState.selectedCategoryId = "all"
                                appState.vegOnly = false
                                appState.ratingFourPlusOnly = false
                                appState.fastDeliveryOnly = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ZomatoRed)
                        ) {
                            Text("Reset Filters")
                        }
                    }
                }
            } else {
                item {
                    BoxWithConstraints(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                        val cols = if (maxWidth >= 1050.dp) 3 else if (maxWidth >= 680.dp) 2 else 1
                        val chunked = filteredRestaurants.chunked(cols)

                        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            chunked.forEach { rowRestaurants ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    rowRestaurants.forEach { rest ->
                                        Box(modifier = Modifier.weight(1f)) {
                                            RestaurantCard(
                                                restaurant = rest,
                                                onClick = { appState.navigateToRestaurant(rest.id) }
                                            )
                                        }
                                    }
                                    val emptySlots = cols - rowRestaurants.size
                                    repeat(emptySlots) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        // FLOATING CART BAR (WHEN CART HAS ITEMS)
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

        // FLOATING LIVE ORDER TRACKING BAR (WHEN AN ORDER IS IN PROGRESS)
        val liveOrder = appState.activeOrder
        if (liveOrder != null && liveOrder.status != com.tomatoapp.application.model.OrderStatus.DELIVERED && appState.cartItemCount == 0) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .clickable { appState.currentScreen = com.tomatoapp.application.state.AppScreen.OrderTracking(liveOrder.id) },
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF1E293B),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = ZomatoRed,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "🛵", fontSize = 18.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Order in Progress",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• #${liveOrder.id}",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                text = "${liveOrder.restaurantName} • OTP: ${liveOrder.deliveryOtp}",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ZomatoRed
                    ) {
                        Text(
                            text = "Track ➔",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // All Categories Dialog
        if (showAllCategoriesModal) {
            AlertDialog(
                onDismissRequest = { showAllCategoriesModal = false },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("All Categories", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            text = "Reset All",
                            fontSize = 12.sp,
                            color = Color(0xFFCB202D),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                appState.selectedCategoryId = "all"
                                showAllCategoriesModal = false
                            }
                        )
                    }
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth().heightIn(max = 380.dp)) {
                        androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                            columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(MockData.categories.size) { index ->
                                val cat = MockData.categories[index]
                                val isSelected = appState.selectedCategoryId == cat.id
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) {
                                        if (appState.isDarkMode) Color(0xFF381215) else Color(0xFFFFF0F2)
                                    } else {
                                        MaterialTheme.colorScheme.surface
                                    },
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) Color(0xFFCB202D) else MaterialTheme.colorScheme.outline
                                    ),
                                    modifier = Modifier.clickable {
                                        appState.selectedCategoryId = cat.id
                                        showAllCategoriesModal = false
                                     }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = if (appState.isDarkMode) Color(0xFF2A2A2A) else Color.White,
                                            modifier = Modifier.size(52.dp)
                                        ) {
                                            if (cat.imageRes != null) {
                                                androidx.compose.foundation.Image(
                                                    painter = org.jetbrains.compose.resources.painterResource(cat.imageRes),
                                                    contentDescription = cat.name,
                                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            } else {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(text = cat.emoji, fontSize = 24.sp)
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = cat.name,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color(0xFFCB202D) else MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAllCategoriesModal = false }) {
                        Text("Close", color = Color(0xFFCB202D), fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // All Popular Restaurants Dialog
        if (showAllPopularModal) {
            AlertDialog(
                onDismissRequest = { showAllPopularModal = false },
                title = {
                    Text("Popular Restaurants", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                text = {
                    val popularList = appState.restaurants.sortedByDescending { it.rating }
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(popularList) { rest ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showAllPopularModal = false
                                        appState.navigateToRestaurant(rest.id)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (appState.isDarkMode) Color(0xFF2A2A2A) else Color(0xFFF5F5F5),
                                        modifier = Modifier.size(54.dp)
                                    ) {
                                        if (rest.coverImageRes != null) {
                                            androidx.compose.foundation.Image(
                                                painter = org.jetbrains.compose.resources.painterResource(rest.coverImageRes),
                                                contentDescription = rest.name,
                                                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        } else {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(text = rest.coverEmoji, fontSize = 24.sp)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = rest.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = rest.cuisines.joinToString(", "),
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "★ ${rest.rating}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2E7D32)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "• ${rest.deliveryTimeMinutes} mins",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                            )
                                        }
                                    }

                                    Text(
                                        text = "➔",
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAllPopularModal = false }) {
                        Text("Close", color = Color(0xFFCB202D), fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}
