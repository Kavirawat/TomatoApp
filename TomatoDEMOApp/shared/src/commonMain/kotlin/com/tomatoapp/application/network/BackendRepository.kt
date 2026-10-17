package com.tomatoapp.application.network

import com.tomatoapp.application.model.CartItem
import com.tomatoapp.application.model.ChatMessage
import com.tomatoapp.application.model.Dish
import com.tomatoapp.application.model.Order
import com.tomatoapp.application.model.OrderStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object BackendRepository {
    val BASE_URLS = listOf(
        "http://10.0.2.2:5000/api",
        "http://192.168.1.19:5000/api",
        "http://localhost:5000/api",
        "http://127.0.0.1:5000/api"
    )

    var activeBaseUrl = BASE_URLS.first()

    suspend fun placeOrderOnServer(order: Order, receiverName: String, userPhone: String = "9876543210"): Boolean = withContext(Dispatchers.Default) {
        val itemsJson = order.items.joinToString(",", "[", "]") {
            """{"id":"${it.dish.id}","name":"${escapeJson(it.dish.name)}","price":${it.dish.price},"quantity":${it.quantity}}"""
        }

        val cleanPhone = userPhone.filter { it.isDigit() }.takeLast(10).ifEmpty { "9876543210" }

        val jsonBody = """
            {
                "id": "${order.id}",
                "userPhone": "$cleanPhone",
                "restaurantName": "${escapeJson(order.restaurantName)}",
                "restaurantAddress": "${escapeJson(order.restaurantAddress)}",
                "items": $itemsJson,
                "itemTotal": ${order.itemTotal},
                "deliveryFee": ${order.deliveryFee},
                "taxes": ${order.taxes},
                "discount": ${order.discount},
                "totalAmount": ${order.totalAmount},
                "deliveryAddress": "${escapeJson(order.deliveryAddress)}",
                "deliveryOtp": "${order.deliveryOtp}",
                "receiverName": "${escapeJson(receiverName)}"
            }
        """.trimIndent()

        // 1. Try activeBaseUrl first
        val primaryResponse = makeHttpRequest("$activeBaseUrl/orders", "POST", jsonBody)
        if (primaryResponse != null && primaryResponse.contains("\"success\":true")) {
            return@withContext true
        }

        // 2. Discover across alternative candidate URLs
        for (base in BASE_URLS) {
            if (base == activeBaseUrl) continue
            val response = makeHttpRequest("$base/orders", "POST", jsonBody)
            if (response != null && response.contains("\"success\":true")) {
                activeBaseUrl = base
                return@withContext true
            }
        }
        false
    }

    suspend fun fetchOrderStatus(orderId: String): ServerOrderUpdate? = withContext(Dispatchers.Default) {
        val response = makeHttpRequest("$activeBaseUrl/orders/$orderId", "GET")
            ?: run {
                for (base in BASE_URLS) {
                    if (base == activeBaseUrl) continue
                    val r = makeHttpRequest("$base/orders/$orderId", "GET")
                    if (r != null && r.contains("\"success\":true")) {
                        activeBaseUrl = base
                        return@run r
                    }
                }
                null
            } ?: return@withContext null

        try {
            val status = extractJsonString(response, "status")
            val adminNotes = extractJsonString(response, "adminNotes")
            val isOtpVerified = response.contains("\"isOtpVerified\":true")
            val deliveryOtp = extractJsonString(response, "deliveryOtp")

            val orderStatus = when (status) {
                "PREPARING" -> OrderStatus.PREPARING
                "OUT_FOR_DELIVERY" -> OrderStatus.OUT_FOR_DELIVERY
                "DELIVERED" -> OrderStatus.DELIVERED
                else -> OrderStatus.PLACED
            }

            ServerOrderUpdate(
                orderId = orderId,
                status = orderStatus,
                adminNotes = adminNotes,
                isOtpVerified = isOtpVerified,
                deliveryOtp = deliveryOtp
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun sendChatMessage(orderId: String, senderName: String, text: String): Boolean = withContext(Dispatchers.Default) {
        val jsonBody = """
            {
                "sender": "user",
                "senderName": "${escapeJson(senderName)}",
                "message": "${escapeJson(text)}"
            }
        """.trimIndent()

        val response = makeHttpRequest("$activeBaseUrl/orders/$orderId/chat", "POST", jsonBody)
            ?: run {
                for (base in BASE_URLS) {
                    if (base == activeBaseUrl) continue
                    val r = makeHttpRequest("$base/orders/$orderId/chat", "POST", jsonBody)
                    if (r != null && r.contains("\"success\":true")) {
                        activeBaseUrl = base
                        return@run r
                    }
                }
                null
            }
        response != null && response.contains("\"success\":true")
    }

    suspend fun fetchChatMessages(orderId: String): List<ChatMessage>? = withContext(Dispatchers.Default) {
        val response = makeHttpRequest("$activeBaseUrl/orders/$orderId/chat", "GET")
            ?: run {
                for (base in BASE_URLS) {
                    if (base == activeBaseUrl) continue
                    val r = makeHttpRequest("$base/orders/$orderId/chat", "GET")
                    if (r != null && r.contains("\"success\":true")) {
                        activeBaseUrl = base
                        return@run r
                    }
                }
                null
            } ?: return@withContext null

        try {
            if (!response.contains("\"success\":true")) return@withContext null
            val list = mutableListOf<ChatMessage>()

            // Extract each object {...} inside response
            val objRegex = """\{([^{}]+)\}""".toRegex()
            objRegex.findAll(response).forEach { match ->
                val objStr = match.groupValues[1]
                val msg = extractJsonString(objStr, "message")
                val sender = extractJsonString(objStr, "sender")
                if (msg != null && msg.isNotBlank() && sender != null) {
                    val id = extractJsonString(objStr, "id") ?: "msg_${list.size}"
                    val senderName = extractJsonString(objStr, "senderName") ?: if (sender == "admin") "Admin Support" else "Customer"
                    val time = extractJsonString(objStr, "time") ?: "Live"
                    list.add(
                        ChatMessage(
                            id = id,
                            orderId = orderId,
                            sender = sender,
                            senderName = senderName,
                            message = unescapeJson(msg),
                            time = time
                        )
                    )
                }
            }
            if (list.isNotEmpty()) list else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addDishToRestaurant(restaurantId: String, dish: Dish): Boolean = withContext(Dispatchers.Default) {
        val jsonBody = """
            {
                "name": "${escapeJson(dish.name)}",
                "price": ${dish.price},
                "description": "${escapeJson(dish.description)}",
                "category": "${escapeJson(dish.category)}",
                "isVeg": ${dish.isVeg},
                "imageEmoji": "${escapeJson(dish.imageEmoji)}"
            }
        """.trimIndent()

        val response = makeHttpRequest("$activeBaseUrl/restaurants/$restaurantId/items", "POST", jsonBody)
            ?: run {
                for (base in BASE_URLS) {
                    if (base == activeBaseUrl) continue
                    val r = makeHttpRequest("$base/restaurants/$restaurantId/items", "POST", jsonBody)
                    if (r != null && r.contains("\"success\":true")) {
                        activeBaseUrl = base
                        return@run r
                    }
                }
                null
            }
        response != null && response.contains("\"success\":true")
    }

    suspend fun fetchRestaurantsRaw(): String? = withContext(Dispatchers.Default) {
        val response = makeHttpRequest("$activeBaseUrl/restaurants", "GET") ?: run {
            for (base in BASE_URLS) {
                val r = makeHttpRequest("$base/restaurants", "GET")
                if (r != null) {
                    activeBaseUrl = base
                    return@run r
                }
            }
            null
        }
        response
    }

    suspend fun sendOtp(phone: String): SendOtpResult? = withContext(Dispatchers.Default) {
        val cleanPhone = phone.filter { it.isDigit() }.takeLast(10)
        val jsonBody = """{"phone":"$cleanPhone"}"""
        val response = makeHttpRequest("$activeBaseUrl/auth/send-otp", "POST", jsonBody)
            ?: run {
                for (base in BASE_URLS) {
                    if (base == activeBaseUrl) continue
                    val r = makeHttpRequest("$base/auth/send-otp", "POST", jsonBody)
                    if (r != null && r.contains("\"success\":true")) {
                        activeBaseUrl = base
                        return@run r
                    }
                }
                null
            } ?: return@withContext null

        try {
            val success = response.contains("\"success\":true")
            val message = extractJsonString(response, "message") ?: "OTP sent"
            val otp = extractJsonString(response, "otp")
            val retPhone = extractJsonString(response, "phone") ?: cleanPhone
            SendOtpResult(success = success, message = message, otp = otp, phone = retPhone)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun verifyOtp(phone: String, otp: String, name: String? = null): VerifyOtpResult? = withContext(Dispatchers.Default) {
        val cleanPhone = phone.filter { it.isDigit() }.takeLast(10)
        val jsonBody = """{"phone":"$cleanPhone","otp":"${otp.trim()}","name":"${escapeJson(name ?: "")}"}"""
        val response = makeHttpRequest("$activeBaseUrl/auth/verify-otp", "POST", jsonBody)
            ?: run {
                for (base in BASE_URLS) {
                    if (base == activeBaseUrl) continue
                    val r = makeHttpRequest("$base/auth/verify-otp", "POST", jsonBody)
                    if (r != null && r.contains("\"success\":true")) {
                        activeBaseUrl = base
                        return@run r
                    }
                }
                null
            } ?: return@withContext null

        try {
            val success = response.contains("\"success\":true")
            val message = extractJsonString(response, "message") ?: "Verification status"
            val userBlock = run {
                val userIdx = response.indexOf("\"user\"")
                if (userIdx != -1) {
                    val open = response.indexOf('{', userIdx)
                    val close = response.indexOf('}', open)
                    if (open != -1 && close > open) response.substring(open, close + 1) else response
                } else response
            }
            val userPhone = extractJsonString(userBlock, "phone") ?: cleanPhone
            val userName = extractJsonString(userBlock, "name")
            val userEmail = extractJsonString(userBlock, "email")
            val orders = parseOrdersFromJson(response)
            VerifyOtpResult(
                success = success,
                message = message,
                userPhone = userPhone,
                userName = userName,
                userEmail = userEmail,
                orders = orders
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUserProfileOnServer(phone: String, name: String, email: String): Boolean = withContext(Dispatchers.Default) {
        val cleanPhone = phone.filter { it.isDigit() }.takeLast(10)
        val jsonBody = """
            {
                "phone": "$cleanPhone",
                "name": "${escapeJson(name)}",
                "email": "${escapeJson(email)}"
            }
        """.trimIndent()

        val response = makeHttpRequest("$activeBaseUrl/auth/profile", "PUT", jsonBody)
            ?: run {
                for (base in BASE_URLS) {
                    if (base == activeBaseUrl) continue
                    val r = makeHttpRequest("$base/auth/profile", "PUT", jsonBody)
                    if (r != null && r.contains("\"success\":true")) {
                        activeBaseUrl = base
                        return@run r
                    }
                }
                null
            }
        response != null && response.contains("\"success\":true")
    }

    suspend fun fetchUserOrdersFromMongoDB(phone: String): List<Order>? = withContext(Dispatchers.Default) {
        val cleanPhone = phone.filter { it.isDigit() }.takeLast(10)
        val response = makeHttpRequest("$activeBaseUrl/auth/orders/$cleanPhone", "GET")
            ?: run {
                for (base in BASE_URLS) {
                    if (base == activeBaseUrl) continue
                    val r = makeHttpRequest("$base/auth/orders/$cleanPhone", "GET")
                    if (r != null && r.contains("\"success\":true")) {
                        activeBaseUrl = base
                        return@run r
                    }
                }
                null
            } ?: return@withContext null

        try {
            if (!response.contains("\"success\":true")) return@withContext null
            parseOrdersFromJson(response)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchAllOrders(): List<Order>? = withContext(Dispatchers.Default) {
        val response = makeHttpRequest("$activeBaseUrl/orders", "GET")
            ?: run {
                for (base in BASE_URLS) {
                    if (base == activeBaseUrl) continue
                    val r = makeHttpRequest("$base/orders", "GET")
                    if (r != null && r.contains("\"success\":true")) {
                        activeBaseUrl = base
                        return@run r
                    }
                }
                null
            } ?: return@withContext null

        try {
            if (!response.contains("\"success\":true")) return@withContext null
            parseOrdersFromJson(response)
        } catch (e: Exception) {
            null
        }
    }

    fun parseOrdersFromJson(json: String): List<Order> {
        val list = mutableListOf<Order>()
        val arrayStartCandidates = listOf(
            json.indexOf("\"orders\""),
            json.indexOf("\"data\"")
        ).filter { it != -1 }

        val searchRegion = if (arrayStartCandidates.isNotEmpty()) {
            val keyPos = arrayStartCandidates.minOrNull() ?: 0
            val openBracket = json.indexOf('[', keyPos)
            if (openBracket != -1) {
                var bracketDepth = 0
                var closeBracket = -1
                var inQuotes = false
                var isEscaped = false
                for (i in openBracket until json.length) {
                    val c = json[i]
                    if (isEscaped) { isEscaped = false; continue }
                    if (c == '\\') { isEscaped = true; continue }
                    if (c == '"') { inQuotes = !inQuotes; continue }
                    if (!inQuotes) {
                        if (c == '[') bracketDepth++
                        else if (c == ']') {
                            bracketDepth--
                            if (bracketDepth == 0) {
                                closeBracket = i
                                break
                            }
                        }
                    }
                }
                if (closeBracket != -1) json.substring(openBracket + 1, closeBracket) else json.substring(openBracket + 1)
            } else {
                json
            }
        } else {
            val openBracket = json.indexOf('[')
            val closeBracket = json.lastIndexOf(']')
            if (openBracket != -1 && closeBracket > openBracket) json.substring(openBracket + 1, closeBracket) else json
        }

        val orderObjects = extractTopLevelObjects(searchRegion)
        for (objStr in orderObjects) {
            val id = extractJsonString(objStr, "id") ?: continue
            if (!id.startsWith("TOM-")) continue

            val restaurantName = extractJsonString(objStr, "restaurantName") ?: "Tomato Partner Kitchen"
            val restaurantAddress = extractJsonString(objStr, "restaurantAddress") ?: "Local Restaurant Kitchen"
            val totalAmountStr = extractJsonString(objStr, "totalAmount") ?: "0"
            val itemTotalStr = extractJsonString(objStr, "itemTotal") ?: totalAmountStr
            val deliveryFeeStr = extractJsonString(objStr, "deliveryFee") ?: "35"
            val taxesStr = extractJsonString(objStr, "taxes") ?: "15"
            val discountStr = extractJsonString(objStr, "discount") ?: "0"
            val statusStr = extractJsonString(objStr, "status")
            val deliveryOtp = extractJsonString(objStr, "deliveryOtp") ?: "1234"
            val deliveryPartnerName = extractJsonString(objStr, "deliveryPartnerName") ?: "Vikram Singh"
            val deliveryPartnerPhone = extractJsonString(objStr, "deliveryPartnerPhone") ?: "+91 98101 23456"
            val isOtpVerified = objStr.contains("\"isOtpVerified\":true")
            val adminNotes = extractJsonString(objStr, "adminNotes") ?: "Admin: Order accepted"
            val deliveryAddress = extractJsonString(objStr, "deliveryAddress") ?: "Home • Noida"
            val orderedAt = extractJsonString(objStr, "orderedAt") ?: "Recently"
            val etaMinutesStr = extractJsonString(objStr, "etaMinutes") ?: "25"
            val receiverName = extractJsonString(objStr, "receiverName") ?: "Customer"
            val userPhone = extractJsonString(objStr, "userPhone") ?: ""

            val itemsList = parseItemsInsideOrder(objStr)

            val status = when (statusStr) {
                "PREPARING" -> OrderStatus.PREPARING
                "OUT_FOR_DELIVERY" -> OrderStatus.OUT_FOR_DELIVERY
                "DELIVERED" -> OrderStatus.DELIVERED
                else -> OrderStatus.PLACED
            }

            list.add(
                Order(
                    id = id,
                    restaurantName = restaurantName,
                    restaurantAddress = restaurantAddress,
                    items = itemsList,
                    itemTotal = itemTotalStr.toDoubleOrNull() ?: 250.0,
                    deliveryFee = deliveryFeeStr.toDoubleOrNull() ?: 35.0,
                    taxes = taxesStr.toDoubleOrNull() ?: 15.0,
                    discount = discountStr.toDoubleOrNull() ?: 0.0,
                    totalAmount = totalAmountStr.toDoubleOrNull() ?: 300.0,
                    status = status,
                    etaMinutes = etaMinutesStr.toIntOrNull() ?: 25,
                    deliveryPartnerName = deliveryPartnerName,
                    deliveryPartnerPhone = deliveryPartnerPhone,
                    orderedAt = orderedAt,
                    deliveryAddress = deliveryAddress,
                    deliveryState = "Delhi NCR",
                    deliveryOtp = deliveryOtp,
                    isOtpVerified = isOtpVerified,
                    adminNotes = adminNotes,
                    receiverName = receiverName,
                    userPhone = userPhone
                )
            )
        }
        return list
    }

    private fun parseItemsInsideOrder(orderObjStr: String): List<CartItem> {
        val itemsKeyIdx = orderObjStr.indexOf("\"items\"")
        if (itemsKeyIdx == -1) return emptyList()

        val openBracket = orderObjStr.indexOf('[', itemsKeyIdx)
        if (openBracket == -1) return emptyList()

        var bracketDepth = 0
        var closeBracket = -1
        var inQuotes = false
        var isEscaped = false
        for (i in openBracket until orderObjStr.length) {
            val c = orderObjStr[i]
            if (isEscaped) { isEscaped = false; continue }
            if (c == '\\') { isEscaped = true; continue }
            if (c == '"') { inQuotes = !inQuotes; continue }
            if (!inQuotes) {
                if (c == '[') bracketDepth++
                else if (c == ']') {
                    bracketDepth--
                    if (bracketDepth == 0) {
                        closeBracket = i
                        break
                    }
                }
            }
        }
        if (closeBracket == -1) return emptyList()

        val itemsRegion = orderObjStr.substring(openBracket + 1, closeBracket)
        val itemObjs = extractTopLevelObjects(itemsRegion)
        val result = mutableListOf<CartItem>()

        for (itemStr in itemObjs) {
            val id = extractJsonString(itemStr, "id") ?: "item_${result.size}"
            val name = extractJsonString(itemStr, "name") ?: continue
            val priceStr = extractJsonString(itemStr, "price") ?: "100"
            val qtyStr = extractJsonString(itemStr, "quantity") ?: "1"
            val isVeg = itemStr.contains("\"isVeg\":true")
            val price = priceStr.toDoubleOrNull() ?: 100.0
            val qty = qtyStr.toIntOrNull() ?: 1

            val dish = Dish(
                id = id,
                name = unescapeJson(name),
                description = "Delicious & freshly prepared",
                price = price,
                isVeg = isVeg,
                rating = 4.5,
                ratingCount = 120,
                category = "Main"
            )
            result.add(CartItem(dish = dish, quantity = qty, restaurantId = ""))
        }
        return result
    }

    private fun extractTopLevelObjects(region: String): List<String> {
        val result = mutableListOf<String>()
        var depth = 0
        var startIdx = -1
        var inQuotes = false
        var isEscaped = false

        for (i in region.indices) {
            val c = region[i]
            if (isEscaped) {
                isEscaped = false
                continue
            }
            if (c == '\\') {
                isEscaped = true
                continue
            }
            if (c == '"') {
                inQuotes = !inQuotes
                continue
            }
            if (!inQuotes) {
                if (c == '{') {
                    if (depth == 0) {
                        startIdx = i
                    }
                    depth++
                } else if (c == '}') {
                    depth--
                    if (depth == 0 && startIdx != -1) {
                        result.add(region.substring(startIdx, i + 1))
                        startIdx = -1
                    }
                }
            }
        }
        return result
    }

    private fun escapeJson(str: String): String {
        return str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "")
    }

    private fun unescapeJson(str: String): String {
        return str.replace("\\\"", "\"").replace("\\\\", "\\").replace("\\n", "\n")
    }

    private fun extractJsonString(json: String, key: String): String? {
        val regex = """"$key"\s*:\s*"([^"\\]*(?:\\.[^"\\]*)*)"""".toRegex()
        val match = regex.find(json)?.groupValues?.get(1)
        if (match != null) return match

        // Also attempt unquoted numbers or booleans if key matches
        val numRegex = """"$key"\s*:\s*([0-9.]+)""".toRegex()
        return numRegex.find(json)?.groupValues?.get(1)
    }
}

data class ServerOrderUpdate(
    val orderId: String,
    val status: OrderStatus,
    val adminNotes: String?,
    val isOtpVerified: Boolean,
    val deliveryOtp: String?
)

data class SendOtpResult(
    val success: Boolean,
    val message: String,
    val otp: String?,
    val phone: String?
)

data class VerifyOtpResult(
    val success: Boolean,
    val message: String,
    val userPhone: String?,
    val userName: String?,
    val userEmail: String?,
    val orders: List<Order>
)
