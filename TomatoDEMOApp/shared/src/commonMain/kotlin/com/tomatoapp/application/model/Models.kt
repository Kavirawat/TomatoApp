package com.tomatoapp.application.model

import org.jetbrains.compose.resources.DrawableResource

data class Dish(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double? = null,
    val isVeg: Boolean,
    val rating: Double,
    val ratingCount: Int,
    val category: String,
    val isBestseller: Boolean = false,
    val imageEmoji: String = "🍲",
    val imageRes: DrawableResource? = null
)

data class Restaurant(
    val id: String,
    val name: String,
    val cuisines: List<String>,
    val rating: Double,
    val ratingCount: String,
    val deliveryTimeMinutes: Int,
    val distanceKm: Double,
    val costForTwo: Int,
    val costForOne: Int = costForTwo / 2,
    val deliveryTimeRange: String = "${deliveryTimeMinutes}-${deliveryTimeMinutes + 5} min",
    val offer: String? = null,
    val isPureVeg: Boolean = false,
    val isPromoted: Boolean = false,
    val address: String,
    val coverEmoji: String = "🍽️",
    val coverImageRes: DrawableResource? = null,
    val dishes: List<Dish>
)

data class CartItem(
    val dish: Dish,
    val quantity: Int,
    val restaurantId: String
)

enum class OrderStatus(val title: String, val subtitle: String, val step: Int) {
    PLACED("Order Placed", "Your order has been sent to the restaurant", 1),
    PREPARING("Preparing Food", "Chef is cooking your delicious meal", 2),
    OUT_FOR_DELIVERY("Out for Delivery", "Delivery partner is on the way to you", 3),
    DELIVERED("Delivered", "Food delivered! Enjoy your meal", 4)
}

data class Order(
    val id: String,
    val restaurantName: String,
    val restaurantAddress: String,
    val items: List<CartItem>,
    val itemTotal: Double,
    val deliveryFee: Double,
    val taxes: Double,
    val discount: Double,
    val totalAmount: Double,
    val status: OrderStatus,
    val etaMinutes: Int,
    val deliveryPartnerName: String = "Rahul Sharma",
    val deliveryPartnerPhone: String = "+91 98765 43210",
    val deliveryPartnerRating: Double = 4.8,
    val orderedAt: String,
    val deliveryAddress: String = "Home • Sector 62, Noida",
    val deliveryState: String = "Delhi NCR",
    val deliveryOtp: String = "4829",
    val isOtpVerified: Boolean = false,
    val adminNotes: String = "Admin: Order accepted and sent to kitchen.",
    val receiverName: String = "Customer",
    val userPhone: String = ""
)

data class FoodCategory(
    val id: String,
    val name: String,
    val emoji: String,
    val imageRes: DrawableResource? = null
)

data class SavedAddress(
    val id: String,
    val label: String = "Home",
    val houseNo: String,
    val areaOrVillage: String,
    val cityOrDistrict: String,
    val state: String,
    val pinCode: String,
    val receiverName: String = "",
    val receiverPhone: String = "",
    val isDefault: Boolean = false
) {
    val fullFormattedAddress: String
        get() = listOfNotNull(
            houseNo.takeIf { it.isNotBlank() },
            areaOrVillage.takeIf { it.isNotBlank() },
            cityOrDistrict.takeIf { it.isNotBlank() },
            state.takeIf { it.isNotBlank() },
            pinCode.takeIf { it.isNotBlank() }
        ).joinToString(", ")
}

data class OrderNotification(
    val id: String,
    val orderId: String,
    val title: String,
    val message: String,
    val time: String,
    val icon: String = "🔔",
    val isRead: Boolean = false
)

data class ChatMessage(
    val id: String,
    val orderId: String,
    val sender: String, // "user" or "admin"
    val senderName: String,
    val message: String,
    val time: String
)
