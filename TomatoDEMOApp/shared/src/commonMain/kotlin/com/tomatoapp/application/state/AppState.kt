package com.tomatoapp.application.state

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import com.tomatoapp.application.data.Coupon
import com.tomatoapp.application.data.MockData
import com.tomatoapp.application.model.*

sealed interface AppScreen {
    data object Home : AppScreen
    data class RestaurantDetail(val restaurantId: String) : AppScreen
    data object Cart : AppScreen
    data class OrderTracking(val orderId: String) : AppScreen
}

enum class BottomTab(val title: String, val iconEmoji: String) {
    HOME("Home", "🏠"),
    NEARBY("Nearby", "📍"),
    CART("Cart", "🛒"),
    PROFILE("Profile", "👤");

    companion object {
        val SEARCH = HOME
        val ORDERS = CART
        val NOTIFICATIONS = PROFILE
        val DELIVERY = HOME
    }
}

enum class SortOption(val label: String) {
    RELEVANCE("Relevance"),
    RATING("Rating 4.0+"),
    DELIVERY_TIME("Fastest Delivery"),
    COST_LOW_TO_HIGH("Cost: Low to High")
}

class TomatoAppState {
    var currentScreen by mutableStateOf<AppScreen>(AppScreen.Home)
    var currentTab by mutableStateOf(BottomTab.HOME)
    var activeTabType by mutableStateOf("Delivery") // "Delivery", "Dining Out", "Nightlife"

    // Theme & User Profile
    var isDarkMode by mutableStateOf(false)
    var notificationsEnabled by mutableStateOf(true)
    var dataSaverEnabled by mutableStateOf(false)
    var showProfileModal by mutableStateOf(false)
    var showPhoneLoginModal by mutableStateOf(false)
    var authModalInitialMode by mutableStateOf("LOGIN") // "LOGIN" or "SIGNUP"
    var localGeneratedOtp by mutableStateOf<String?>(null)
    var isLoggedIn by mutableStateOf(false)
    var userName by mutableStateOf("User")
    var userEmail by mutableStateOf("")
    var userPhone by mutableStateOf("")
    var userCity by mutableStateOf("Delhi NCR")

    // Filters & Search
    var searchQuery by mutableStateOf("")
    var selectedCategoryId by mutableStateOf("all")
    var vegOnly by mutableStateOf(false)
    var ratingFourPlusOnly by mutableStateOf(false)
    var fastDeliveryOnly by mutableStateOf(false)
    var currentSortOption by mutableStateOf(SortOption.RELEVANCE)
    val hasActiveFilters: Boolean
        get() = vegOnly || ratingFourPlusOnly || fastDeliveryOnly || selectedCategoryId != "all" || currentSortOption != SortOption.RELEVANCE
    var showMyShopModal by mutableStateOf(false)
    var showFiltersModal by mutableStateOf(false)
    var showCuisinesModal by mutableStateOf(false)
    var myShopRestaurantId by mutableStateOf("r1") // Default restaurant for user's shop

    // Cart
    var cartItems = mutableStateMapOf<String, CartItem>()
    var cartRestaurantId by mutableStateOf<String?>(null)
    var appliedCoupon by mutableStateOf<Coupon?>(null)
    var deliveryTip by mutableStateOf(20)
    var cookingInstructions by mutableStateOf("")

    // Location & Saved Addresses
    var showLocationModal by mutableStateOf(false)
    var showSavedAddressesModal by mutableStateOf(false)
    var showAddAddressModal by mutableStateOf(false)
    var userLocation by mutableStateOf("Home • Sector 62, Noida")

    var selectedAddressId by mutableStateOf("addr_1")
    val selectedAddress: SavedAddress?
        get() = savedAddresses.find { it.id == selectedAddressId } ?: savedAddresses.firstOrNull()

    var savedAddresses = mutableStateListOf<SavedAddress>(
        SavedAddress(
            id = "addr_1",
            label = "Home",
            houseNo = "Flat 402, Block B, Green Heights",
            areaOrVillage = "Sector 62",
            cityOrDistrict = "Noida",
            state = "Delhi NCR",
            pinCode = "201309",
            receiverName = "Kavi Rawat",
            receiverPhone = "+91 98765 43210",
            isDefault = true
        ),
        SavedAddress(
            id = "addr_2",
            label = "Work",
            houseNo = "Tower B, 5th Floor, Cyber City",
            areaOrVillage = "DLF Phase 2",
            cityOrDistrict = "Gurugram",
            state = "Delhi NCR",
            pinCode = "122002",
            receiverName = "Kavi Rawat (Work)",
            receiverPhone = "+91 98765 43210",
            isDefault = false
        )
    )

    fun saveNewAddress(address: SavedAddress) {
        // Deactivate others if this is set as default
        if (address.isDefault) {
            val updatedList = savedAddresses.map { it.copy(isDefault = false) }
            savedAddresses.clear()
            savedAddresses.addAll(updatedList)
        }
        savedAddresses.add(0, address)
        selectSavedAddress(address)
        showAddAddressModal = false
        showSavedAddressesModal = false
    }

    fun selectSavedAddress(address: SavedAddress) {
        selectedAddressId = address.id
        val updatedList = savedAddresses.map { it.copy(isDefault = it.id == address.id) }
        savedAddresses.clear()
        savedAddresses.addAll(updatedList)
        userLocation = "${address.label} • ${address.areaOrVillage}, ${address.cityOrDistrict}"
        userCity = address.state
        showSavedAddressesModal = false
    }

    fun deleteSavedAddress(id: String) {
        savedAddresses.removeAll { it.id == id }
        if (selectedAddressId == id && savedAddresses.isNotEmpty()) {
            selectSavedAddress(savedAddresses.first())
        }
    }

    fun updateLocation(state: String, area: String = "") {
        userCity = state
        userLocation = if (area.isNotBlank()) "Home • $area, $state" else "Home • $state"
        showLocationModal = false
    }

    // Payment Method
    var showPaymentMethodsModal by mutableStateOf(false)
    var selectedPaymentMethod by mutableStateOf("UPI / Google Pay")

    // Notifications
    var showNotificationsModal by mutableStateOf(false)
    var activeNotificationBanner by mutableStateOf<OrderNotification?>(null)
    var notifications = mutableStateListOf<OrderNotification>(
        OrderNotification(
            id = "notif_welcome",
            orderId = "WELCOME",
            title = "Welcome to Tomato! 🍅",
            message = "Enjoy flat 50% discount on your first order. Use code TOMATO50 at checkout.",
            time = "Today"
        )
    )

    fun triggerNotification(title: String, message: String, orderId: String = "INFO") {
        val notif = OrderNotification(
            id = "notif_${(1000..9999).random()}",
            orderId = orderId,
            title = title,
            message = message,
            time = "Just now"
        )
        notifications.add(0, notif)
        activeNotificationBanner = notif
    }

    // Orders
    var orders = mutableStateListOf<Order>()
    var activeOrder by mutableStateOf<Order?>(null)

    // Admin Chat Messages per Order
    var chatMessages = mutableStateListOf<ChatMessage>()
    var showChatModal by mutableStateOf(false)

    val cartItemCount: Int
        get() = cartItems.values.sumOf { it.quantity }

    val cartSubtotal: Double
        get() = cartItems.values.sumOf { it.dish.price * it.quantity }

    val deliveryFee: Double
        get() = if (cartSubtotal > 399.0 || appliedCoupon?.code == "GOLDDEL") 0.0 else 35.0

    val taxesAndCharges: Double
        get() = if (cartSubtotal > 0) (cartSubtotal * 0.05) + 15.0 else 0.0

    val discountAmount: Double
        get() {
            val coupon = appliedCoupon ?: return 0.0
            if (cartSubtotal < coupon.minOrderAmount) return 0.0
            val discount = (cartSubtotal * (coupon.percentage / 100.0))
            return discount.coerceAtMost(coupon.maxDiscount)
        }

    val totalAmount: Double
        get() {
            if (cartSubtotal == 0.0) return 0.0
            val total = cartSubtotal + deliveryFee + taxesAndCharges + deliveryTip - discountAmount
            return total.coerceAtLeast(0.0)
        }

    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
    }

    fun updateProfile(name: String, email: String, phone: String) {
        if (name.isNotBlank()) userName = name.trim()
        if (email.isNotBlank()) userEmail = email.trim()
        if (phone.isNotBlank()) userPhone = phone.trim()
        val targetPhone = userPhone.ifBlank { phone.trim() }
        if (targetPhone.isNotBlank()) {
            CoroutineScope(Dispatchers.Default).launch {
                com.tomatoapp.application.network.BackendRepository.updateUserProfileOnServer(targetPhone, userName, userEmail)
            }
        }
    }

    fun addToCart(dish: Dish, restaurantId: String) {
        if (cartRestaurantId != null && cartRestaurantId != restaurantId && cartItems.isNotEmpty()) {
            cartItems.clear()
            appliedCoupon = null
        }
        cartRestaurantId = restaurantId
        val existing = cartItems[dish.id]
        if (existing == null) {
            cartItems[dish.id] = CartItem(dish = dish, quantity = 1, restaurantId = restaurantId)
        } else {
            cartItems[dish.id] = existing.copy(quantity = existing.quantity + 1)
        }
    }

    fun removeFromCart(dishId: String) {
        val existing = cartItems[dishId] ?: return
        if (existing.quantity <= 1) {
            cartItems.remove(dishId)
            if (cartItems.isEmpty()) {
                cartRestaurantId = null
                appliedCoupon = null
            }
        } else {
            cartItems[dishId] = existing.copy(quantity = existing.quantity - 1)
        }
    }

    fun getQuantityInCart(dishId: String): Int {
        return cartItems[dishId]?.quantity ?: 0
    }

    fun applyCoupon(code: String): String {
        val coupon = MockData.coupons[code.uppercase().trim()]
        return if (coupon == null) {
            "Invalid coupon code"
        } else if (cartSubtotal < coupon.minOrderAmount) {
            "Minimum order of ₹${coupon.minOrderAmount.toInt()} required"
        } else {
            appliedCoupon = coupon
            "Applied! Saved ₹${discountAmount.toInt()}"
        }
    }

    fun removeCoupon() {
        appliedCoupon = null
    }

    // Food & Restaurant Data (Loaded from Server with local cache fallback)
    var restaurants = mutableStateListOf<Restaurant>().apply {
        addAll(MockData.restaurants)
    }

    // Backend Connection State
    var isBackendConnected by mutableStateOf(false)
    var backendSyncStatusText by mutableStateOf("Connected to Kitchen Server")

    init {
        fetchFoodDataFromServer()
        fetchOrdersFromBackend()
    }

    fun fetchFoodDataFromServer() {
        CoroutineScope(Dispatchers.Default).launch {
            val raw = com.tomatoapp.application.network.BackendRepository.fetchRestaurantsRaw()
            if (raw != null && raw.contains("\"success\":true")) {
                withContext(Dispatchers.Main) {
                    isBackendConnected = true
                }
            }
        }
    }

    fun fetchOrdersFromBackend() {
        CoroutineScope(Dispatchers.Default).launch {
            val serverOrders = com.tomatoapp.application.network.BackendRepository.fetchAllOrders()
            if (serverOrders != null && serverOrders.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    serverOrders.forEach { sOrder ->
                        val idx = orders.indexOfFirst { it.id == sOrder.id }
                        if (idx != -1) {
                            orders[idx] = sOrder
                        } else {
                            orders.add(sOrder)
                        }
                    }
                    if (activeOrder == null) {
                        activeOrder = orders.firstOrNull { it.status != OrderStatus.DELIVERED }
                    }
                }
            }
        }
    }

    fun addDishToRestaurant(
        restaurantId: String,
        name: String,
        price: Double,
        description: String,
        category: String,
        isVeg: Boolean,
        imageEmoji: String = "🍲",
        imageRes: org.jetbrains.compose.resources.DrawableResource? = null
    ): Boolean {
        val restIdx = restaurants.indexOfFirst { it.id == restaurantId }
        if (restIdx == -1) return false
        val rest = restaurants[restIdx]

        val newDish = Dish(
            id = "dish_${(1000..9999).random()}",
            name = name.trim(),
            description = description.ifBlank { "Freshly prepared by restaurant chef." },
            price = price,
            originalPrice = price + 40,
            isVeg = isVeg,
            rating = 4.8,
            ratingCount = 1,
            category = category.ifBlank { "Specialties" },
            isBestseller = true,
            imageEmoji = imageEmoji.ifBlank { if (isVeg) "🥗" else "🍗" },
            imageRes = imageRes
        )

        val updatedDishes = listOf(newDish) + rest.dishes
        val updatedRest = rest.copy(dishes = updatedDishes)
        restaurants[restIdx] = updatedRest

        // Sync to backend server
        CoroutineScope(Dispatchers.Default).launch {
            com.tomatoapp.application.network.BackendRepository.addDishToRestaurant(restaurantId, newDish)
        }
        return true
    }

    fun updateDishInRestaurant(
        restaurantId: String,
        updatedDish: Dish
    ): Boolean {
        val restIdx = restaurants.indexOfFirst { it.id == restaurantId }
        if (restIdx == -1) return false
        val rest = restaurants[restIdx]
        val dishIdx = rest.dishes.indexOfFirst { it.id == updatedDish.id }
        if (dishIdx == -1) return false

        val updatedDishes = rest.dishes.toMutableList()
        updatedDishes[dishIdx] = updatedDish
        restaurants[restIdx] = rest.copy(dishes = updatedDishes)
        return true
    }

    fun deleteDishFromRestaurant(
        restaurantId: String,
        dishId: String
    ): Boolean {
        val restIdx = restaurants.indexOfFirst { it.id == restaurantId }
        if (restIdx == -1) return false
        val rest = restaurants[restIdx]
        val updatedDishes = rest.dishes.filter { it.id != dishId }
        restaurants[restIdx] = rest.copy(dishes = updatedDishes)
        return true
    }


    fun placeOrder(): Order? {
        if (!isLoggedIn) {
            showPhoneLoginModal = true
            triggerNotification(
                title = "Login Required",
                message = "Please login with your phone number to place your order!"
            )
            return null
        }
        if (cartItems.isEmpty()) return null
        val restId = cartRestaurantId ?: cartItems.values.firstOrNull()?.restaurantId
        val rest = restaurants.find { it.id == restId } ?: MockData.restaurants.find { it.id == restId } ?: restaurants.firstOrNull() ?: MockData.restaurants.first()
        val generatedOtp = (1000..9999).random().toString()

        val newOrder = Order(
            id = "TOM-${(1000..9999).random()}",
            restaurantName = rest.name,
            restaurantAddress = rest.address,
            items = cartItems.values.toList(),
            itemTotal = cartSubtotal,
            deliveryFee = deliveryFee,
            taxes = taxesAndCharges,
            discount = discountAmount,
            totalAmount = totalAmount,
            status = OrderStatus.PLACED,
            etaMinutes = rest.deliveryTimeMinutes,
            deliveryPartnerName = "Vikram Singh",
            deliveryPartnerPhone = "+91 98101 23456",
            orderedAt = "Just now",
            deliveryAddress = selectedAddress?.fullFormattedAddress ?: userLocation,
            deliveryState = selectedAddress?.state ?: userCity,
            deliveryOtp = generatedOtp,
            isOtpVerified = false,
            adminNotes = "Admin: Order accepted and sent to kitchen.",
            receiverName = userName.ifBlank { "Customer" },
            userPhone = userPhone
        )

        orders.add(0, newOrder)
        activeOrder = newOrder
        cartItems.clear()
        cartRestaurantId = null
        appliedCoupon = null

        // Sync order to Backend immediately so it appears live in TomatoDEMOAdmin & MongoDB
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default).launch {
            val success = com.tomatoapp.application.network.BackendRepository.placeOrderOnServer(newOrder, userName, userPhone)
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                isBackendConnected = success
                backendSyncStatusText = if (success)
                    "Order #${newOrder.id} confirmed & live in Admin Console! ✓"
                else
                    "Order placed. Connecting to backend..."
            }
        }

        // Initialize Chat with Admin for this order
        chatMessages.clear()
        chatMessages.add(
            ChatMessage(
                id = "chat_welcome",
                orderId = newOrder.id,
                sender = "admin",
                senderName = "Admin (${newOrder.restaurantName})",
                message = "Namaste $userName! We've received your order #${newOrder.id}. Our kitchen is preparing your dishes hot and fresh. Let us know if you need any specific cooking instructions!",
                time = "Just now"
            )
        )

        // Trigger real-time order notification from Admin
        triggerNotification(
            title = "Admin: Order Confirmed! 🎉",
            message = "Order #${newOrder.id} confirmed! Delivering to ${newOrder.deliveryAddress}. Your Delivery OTP is $generatedOtp.",
            orderId = newOrder.id
        )

        currentScreen = AppScreen.OrderTracking(newOrder.id)
        return newOrder
    }

    fun navigateToRestaurant(restaurantId: String) {
        currentScreen = AppScreen.RestaurantDetail(restaurantId)
    }

    fun navigateToCart() {
        currentScreen = AppScreen.Cart
    }

    fun navigateBack() {
        currentScreen = AppScreen.Home
    }

    // Sync active order with Backend (e.g. status updates or OTP verification done by Admin in TomatoDEMOAdmin)
    fun syncActiveOrderWithBackend() {
        val current = activeOrder ?: return
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default).launch {
            val update = com.tomatoapp.application.network.BackendRepository.fetchOrderStatus(current.id)
            if (update != null) {
                val hasChanges = current.status != update.status ||
                        current.isOtpVerified != update.isOtpVerified ||
                        (update.adminNotes != null && current.adminNotes != update.adminNotes)

                if (hasChanges) {
                    val updated = current.copy(
                        status = update.status,
                        isOtpVerified = update.isOtpVerified,
                        adminNotes = update.adminNotes ?: current.adminNotes
                    )
                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                        activeOrder = updated
                        val index = orders.indexOfFirst { it.id == current.id }
                        if (index != -1) {
                            orders[index] = updated
                        }

                        // Trigger notifications on status changes from Admin
                        if (update.status == OrderStatus.PREPARING && current.status != OrderStatus.PREPARING) {
                            triggerNotification(
                                "Admin: Khana Ban Raha Hai 👨‍🍳",
                                "Chef has started cooking your order at ${current.restaurantName}.",
                                current.id
                            )
                        } else if (update.status == OrderStatus.OUT_FOR_DELIVERY && current.status != OrderStatus.OUT_FOR_DELIVERY) {
                            triggerNotification(
                                "Admin: Khana Raste Mein Hai 🛵",
                                "Rider ${current.deliveryPartnerName} is on the way. Share OTP ${current.deliveryOtp} to complete delivery.",
                                current.id
                            )
                        } else if (update.isOtpVerified && !current.isOtpVerified) {
                            triggerNotification(
                                "Admin: Order Delivered! 🍽️",
                                "OTP verified successfully. Enjoy your hot and fresh meal!",
                                current.id
                            )
                        }
                    }
                }
            }
        }
    }

    fun advanceOrderStatusByAdmin(orderId: String) {
        val current = activeOrder ?: return
        if (current.id != orderId) return
        val nextStatus = when (current.status) {
            OrderStatus.PLACED -> OrderStatus.PREPARING
            OrderStatus.PREPARING -> OrderStatus.OUT_FOR_DELIVERY
            OrderStatus.OUT_FOR_DELIVERY -> OrderStatus.OUT_FOR_DELIVERY // Requires OTP to deliver!
            OrderStatus.DELIVERED -> OrderStatus.DELIVERED
        }
        val adminNote = when (nextStatus) {
            OrderStatus.PREPARING -> "Admin Update: Chef is cooking your fresh dishes in the kitchen (Ban raha hai 👨‍🍳)."
            OrderStatus.OUT_FOR_DELIVERY -> "Admin Update: Partner ${current.deliveryPartnerName} is on the way (Raste mein hai 🛵). Share OTP ${current.deliveryOtp} upon arrival."
            else -> current.adminNotes
        }
        val updated = current.copy(status = nextStatus, adminNotes = adminNote)
        activeOrder = updated
        val index = orders.indexOfFirst { it.id == orderId }
        if (index != -1) {
            orders[index] = updated
        }

        // Trigger notification for order status transition from Admin
        val notifTitle = when (nextStatus) {
            OrderStatus.PREPARING -> "Admin: Food is Being Prepared! 👨‍🍳"
            OrderStatus.OUT_FOR_DELIVERY -> "Admin: Out for Delivery! 🛵"
            else -> "Admin: Order Update"
        }
        val notifMsg = when (nextStatus) {
            OrderStatus.PREPARING -> "Chef has started preparing your order at ${current.restaurantName} (Ban raha hai)."
            OrderStatus.OUT_FOR_DELIVERY -> "${current.deliveryPartnerName} is on the way! Please share OTP ${current.deliveryOtp} upon arrival (Raste mein hai)."
            else -> "Your order #${orderId} status has been updated by Admin."
        }
        triggerNotification(notifTitle, notifMsg, orderId)
    }

    fun verifyDeliveryOtp(orderId: String, enteredOtp: String): Boolean {
        val current = activeOrder ?: return false
        if (current.id != orderId) return false
        if (current.deliveryOtp == enteredOtp.trim()) {
            val updated = current.copy(
                status = OrderStatus.DELIVERED,
                isOtpVerified = true,
                adminNotes = "Admin: Delivery confirmed via OTP verification (${current.deliveryOtp}). Enjoy your meal!"
            )
            activeOrder = updated
            val index = orders.indexOfFirst { it.id == orderId }
            if (index != -1) {
                orders[index] = updated
            }
            triggerNotification(
                title = "Admin: Order Delivered! 🍽️",
                message = "Delivery partner verified OTP ${current.deliveryOtp}. Food safely delivered at ${current.deliveryAddress}!",
                orderId = orderId
            )
            return true
        }
        return false
    }

    fun syncChatMessagesWithBackend(orderId: String) {
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default).launch {
            val serverMessages = com.tomatoapp.application.network.BackendRepository.fetchChatMessages(orderId)
            if (serverMessages != null && serverMessages.isNotEmpty()) {
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    chatMessages.clear()
                    chatMessages.addAll(serverMessages)
                }
            }
        }
    }

    fun sendChatMessage(orderId: String, text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(
            id = "msg_${(1000..9999).random()}",
            orderId = orderId,
            sender = "user",
            senderName = userName,
            message = text.trim(),
            time = "Just now"
        )
        chatMessages.add(userMsg)

        // Sync to Backend immediately and fetch server thread
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default).launch {
            com.tomatoapp.application.network.BackendRepository.sendChatMessage(orderId, userName, text.trim())
            val serverMessages = com.tomatoapp.application.network.BackendRepository.fetchChatMessages(orderId)
            if (serverMessages != null && serverMessages.isNotEmpty()) {
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    chatMessages.clear()
                    chatMessages.addAll(serverMessages)
                }
            }
        }
    }

    fun sendPhoneOtp(phone: String, onResult: (Boolean, String, String?) -> Unit) {
        val cleanPhone = phone.filter { it.isDigit() }.takeLast(10).ifEmpty { "9876543210" }
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val result = withTimeoutOrNull(2000L) {
                    com.tomatoapp.application.network.BackendRepository.sendOtp(cleanPhone)
                }
                withContext(Dispatchers.Main) {
                    val finalOtp = result?.otp?.takeIf { it.isNotBlank() } ?: (1000..9999).random().toString()
                    localGeneratedOtp = finalOtp
                    triggerNotification(
                        title = "Tomato Login OTP 🔑",
                        message = "Your verification OTP for +91 $cleanPhone is $finalOtp",
                        orderId = "AUTH"
                    )
                    onResult(true, "Your OTP is $finalOtp", finalOtp)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val fallbackOtp = (1000..9999).random().toString()
                    localGeneratedOtp = fallbackOtp
                    triggerNotification(
                        title = "Tomato Login OTP 🔑",
                        message = "Your verification OTP for +91 $cleanPhone is $fallbackOtp",
                        orderId = "AUTH"
                    )
                    onResult(true, "Your OTP is $fallbackOtp", fallbackOtp)
                }
            }
        }
    }

    fun verifyPhoneOtp(phone: String, otp: String, chosenName: String, onResult: (Boolean, String) -> Unit) {
        val cleanPhone = phone.filter { it.isDigit() }.takeLast(10).ifEmpty { "9876543210" }
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val result = withTimeoutOrNull(2000L) {
                    com.tomatoapp.application.network.BackendRepository.verifyOtp(cleanPhone, otp, chosenName)
                }
                withContext(Dispatchers.Main) {
                    val isValid = (result != null && result.success) ||
                            otp.trim() == "1234" ||
                            (localGeneratedOtp != null && otp.trim() == localGeneratedOtp)

                    if (isValid) {
                        isLoggedIn = true
                        userPhone = cleanPhone

                        // 1. Restore persistent User Name from MongoDB
                        if (chosenName.isNotBlank() && chosenName != "User") {
                            userName = chosenName.trim()
                        } else if (!result?.userName.isNullOrBlank() && result?.userName != "User") {
                            userName = result.userName.orEmpty()
                        } else if (userName.isBlank() || userName == "User") {
                            userName = result?.userName?.takeIf { it.isNotBlank() } ?: "User"
                        }

                        // 2. Restore persistent User Email from MongoDB
                        if (!result?.userEmail.isNullOrBlank()) {
                            userEmail = result.userEmail.orEmpty()
                        } else if (userEmail.isBlank()) {
                            userEmail = "user_${cleanPhone}@tomato.com"
                        }

                        // 3. Clear and restore user's past orders from MongoDB Atlas
                        orders.clear()
                        if (result != null && result.orders.isNotEmpty()) {
                            orders.addAll(result.orders)
                            activeOrder = orders.firstOrNull { it.status != OrderStatus.DELIVERED }
                        } else {
                            activeOrder = null
                        }

                        // Additional background fetch to ensure full sync with Atlas
                        CoroutineScope(Dispatchers.Default).launch {
                            val mongoOrders = com.tomatoapp.application.network.BackendRepository.fetchUserOrdersFromMongoDB(cleanPhone)
                            if (!mongoOrders.isNullOrEmpty()) {
                                withContext(Dispatchers.Main) {
                                    orders.clear()
                                    orders.addAll(mongoOrders)
                                    if (activeOrder == null) {
                                        activeOrder = orders.firstOrNull { it.status != OrderStatus.DELIVERED }
                                    }
                                }
                            }
                        }

                        triggerNotification(
                            title = "Welcome $userName! 🎉",
                            message = "Logged in successfully to Tomato as +91 $userPhone.",
                            orderId = "AUTH"
                        )

                        onResult(true, "Login successful!")
                    } else {
                        onResult(false, result?.message ?: "Invalid OTP! Try code: 1234")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isLoggedIn = true
                    userPhone = cleanPhone
                    if (chosenName.isNotBlank() && chosenName != "User") {
                        userName = chosenName.trim()
                    }
                    if (userEmail.isBlank()) {
                        userEmail = "user_${cleanPhone}@tomato.com"
                    }
                    triggerNotification(
                        title = "Welcome $userName! 🎉",
                        message = "Logged in successfully to Tomato.",
                        orderId = "AUTH"
                    )
                    onResult(true, "Login successful!")
                }
            }
        }
    }

    fun quickDemoLogin(name: String = "User", phone: String = "9876543210") {
        isLoggedIn = true
        userName = name
        userPhone = phone
        userEmail = if (phone.isNotBlank()) "user_${phone}@tomato.com" else "user@tomato.com"
        showPhoneLoginModal = false
        triggerNotification(
            title = "Welcome $userName! 🎉",
            message = "Login to Tomato successful.",
            orderId = "AUTH"
        )
    }

    fun logout() {
        isLoggedIn = false
        userPhone = ""
        userName = "User"
        userEmail = ""
        orders.clear()
        activeOrder = null
        triggerNotification(
            title = "Logged Out",
            message = "You have been logged out of Tomato.",
            orderId = "AUTH"
        )
    }
}

val LocalTomatoAppState = staticCompositionLocalOf<TomatoAppState> {
    error("No TomatoAppState provided")
}
