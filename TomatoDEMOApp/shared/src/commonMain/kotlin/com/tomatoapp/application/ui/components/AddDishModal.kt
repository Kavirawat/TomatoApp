package com.tomatoapp.application.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tomatoapp.application.state.LocalTomatoAppState
import com.tomatoapp.application.ui.theme.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import tomatoapp.shared.generated.resources.*

@Composable
fun AddDishModal(
    restaurantId: String,
    restaurantName: String,
    onDismiss: () -> Unit
) {
    val appState = LocalTomatoAppState.current

    val originPhotoOptions = listOf(
        OriginPhotoOption("pn", "Shahi Paneer", Res.drawable.dish_paneer),
        OriginPhotoOption("by", "Dum Biryani", Res.drawable.dish_biryani),
        OriginPhotoOption("pz", "Cheese Pizza", Res.drawable.dish_pizza),
        OriginPhotoOption("bg", "Crispy Burger", Res.drawable.dish_burger),
        OriginPhotoOption("cb", "Chole Bhature", Res.drawable.dish_chole_bhature),
        OriginPhotoOption("ds", "Masala Dosa", Res.drawable.dish_dosa),
        OriginPhotoOption("sw", "Club Sandwich", Res.drawable.cat_sandwich),
        OriginPhotoOption("id", "Idli Sambar", Res.drawable.cat_idli),
        OriginPhotoOption("pr", "Aloo Paratha", Res.drawable.cat_paratha),
        OriginPhotoOption("ch", "Dahi Chaat", Res.drawable.dish_chaat),
        OriginPhotoOption("gj", "Gulab Jamun", Res.drawable.dish_gulab_jamun),
        OriginPhotoOption("th", "Special Thali", Res.drawable.banner_thali),
        OriginPhotoOption("dsrt", "Sweet Dessert", Res.drawable.banner_dessert)
    )

    var selectedPhotoOption by remember { mutableStateOf(originPhotoOptions[0]) }
    var dishName by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Main Course") }
    var isVeg by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    val categories = listOf("Main Course", "Starters", "Breads", "Desserts", "Beverages", "Snacks")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 16.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Bar
                Surface(
                    color = ZomatoRed,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "👨‍🍳", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Add New Food Item",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Hotel & Restaurant Partner • $restaurantName",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.25f),
                            modifier = Modifier
                                .size(30.dp)
                                .clickable { onDismiss() }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "✕", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Scrollable Form Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (errorMessage.isNotEmpty()) {
                        Surface(
                            color = if (appState.isDarkMode) Color(0xFF381215) else Color(0xFFFEF2F2),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (appState.isDarkMode) Color(0xFFE57373) else Color(0xFFF87171)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⚠️ $errorMessage",
                                color = if (appState.isDarkMode) Color(0xFFFF8A80) else Color(0xFFB91C1C),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    // Food Name Input
                    Column {
                        Text(
                            text = "Dish / Item Name *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.background,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth().height(46.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp), contentAlignment = Alignment.CenterStart) {
                                if (dishName.isEmpty()) {
                                    Text("e.g. Special Butter Paneer Masala", fontSize = 13.sp, color = ZomatoTextLight)
                                }
                                BasicTextField(
                                    value = dishName,
                                    onValueChange = { dishName = it },
                                    singleLine = true,
                                    textStyle = TextStyle(fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // Price Input
                    Column {
                        Text(
                            text = "Price (₹) *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.background,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth().height(46.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp), contentAlignment = Alignment.CenterStart) {
                                if (priceText.isEmpty()) {
                                    Text("e.g. 240", fontSize = 13.sp, color = ZomatoTextLight)
                                }
                                BasicTextField(
                                    value = priceText,
                                    onValueChange = { priceText = it.filter { char -> char.isDigit() } },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    textStyle = TextStyle(fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // Veg / Non-Veg Toggle
                    Column {
                        Text(
                            text = "Dietary Preference",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isVeg) {
                                    if (appState.isDarkMode) Color(0xFF1B3B22) else ZomatoGreenLight
                                } else MaterialTheme.colorScheme.background,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.5.dp,
                                    if (isVeg) ZomatoGreen else MaterialTheme.colorScheme.outline
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { isVeg = true }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    VegNonVegBadge(isVeg = true)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Vegetarian",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isVeg) ZomatoGreen else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (!isVeg) {
                                    if (appState.isDarkMode) Color(0xFF381215) else ZomatoRedLight
                                } else MaterialTheme.colorScheme.background,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.5.dp,
                                    if (!isVeg) ZomatoRed else MaterialTheme.colorScheme.outline
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { isVeg = false }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    VegNonVegBadge(isVeg = false)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Non-Veg",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (!isVeg) ZomatoRed else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    // Category Selection
                    Column {
                        Text(
                            text = "Category",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(categories) { cat ->
                                val isSelected = selectedCategory == cat
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) {
                                        if (appState.isDarkMode) Color(0xFF381215) else ZomatoRedLight
                                    } else MaterialTheme.colorScheme.background,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) ZomatoRed else MaterialTheme.colorScheme.outline
                                    ),
                                    modifier = Modifier.clickable { selectedCategory = cat }
                                ) {
                                    Text(
                                        text = cat,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) ZomatoRed else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Original Food Photo Selection (Replaces Food Icon emojis)
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Choose Food Photo *",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (appState.isDarkMode) Color(0xFF381215) else Color(0xFFFFEBEE)
                            ) {
                                Text(
                                    text = "📷 Original Photo",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ZomatoRed,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Selected Photo Live Preview Card
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (appState.isDarkMode) Color(0xFF242424) else Color(0xFFF8F9FA),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth().height(92.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize().padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(76.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                ) {
                                    Image(
                                        painter = painterResource(selectedPhotoOption.resource),
                                        contentDescription = selectedPhotoOption.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(4.dp)
                                            .size(20.dp)
                                            .background(ZomatoRed, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "✓", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Selected Photo Preview",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = selectedPhotoOption.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Tap on any photo below to change",
                                        fontSize = 10.sp,
                                        color = ZomatoRed,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Horizontal Carousel of Original Food Photos
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(horizontal = 2.dp, vertical = 2.dp)
                        ) {
                            items(originPhotoOptions) { opt ->
                                val isSelected = selectedPhotoOption.id == opt.id
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .width(72.dp)
                                        .clickable {
                                            selectedPhotoOption = opt
                                            if (dishName.isBlank()) {
                                                dishName = opt.name
                                            }
                                            if (opt.id == "by") {
                                                isVeg = false
                                            }
                                        }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(68.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .border(
                                                width = if (isSelected) 2.5.dp else 1.dp,
                                                color = if (isSelected) ZomatoRed else MaterialTheme.colorScheme.outline,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                    ) {
                                        Image(
                                            painter = painterResource(opt.resource),
                                            contentDescription = opt.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        if (isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.BottomEnd)
                                                    .padding(3.dp)
                                                    .size(18.dp)
                                                    .background(ZomatoRed, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(text = "✓", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = opt.name,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) ZomatoRed else MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }

                    // Description Input
                    Column {
                        Text(
                            text = "Item Description",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.background,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth().height(60.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize().padding(12.dp), contentAlignment = Alignment.TopStart) {
                                if (description.isEmpty()) {
                                    Text("Brief description of ingredients or preparation...", fontSize = 12.sp, color = ZomatoTextLight)
                                }
                                BasicTextField(
                                    value = description,
                                    onValueChange = { description = it },
                                    textStyle = TextStyle(fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                // Bottom Action Bar
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp,
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f).height(46.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cancel", color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
                        }

                        Button(
                            onClick = {
                                if (dishName.isBlank()) {
                                    errorMessage = "Please enter dish name"
                                    return@Button
                                }
                                val price = priceText.toDoubleOrNull()
                                if (price == null || price <= 0) {
                                    errorMessage = "Please enter a valid price"
                                    return@Button
                                }
                                errorMessage = ""
                                val emojiFallback = when (selectedPhotoOption.id) {
                                    "pn" -> "🍲"
                                    "by" -> "🍛"
                                    "pz" -> "🍕"
                                    "bg" -> "🍔"
                                    "cb" -> "🥟"
                                    "ds", "id" -> "🥞"
                                    "sw" -> "🥪"
                                    "pr" -> "🫓"
                                    "ch" -> "🥗"
                                    "gj", "dsrt" -> "🍰"
                                    else -> if (isVeg) "🥗" else "🍗"
                                }
                                appState.addDishToRestaurant(
                                    restaurantId = restaurantId,
                                    name = dishName.trim(),
                                    price = price,
                                    description = description.trim(),
                                    category = selectedCategory,
                                    isVeg = isVeg,
                                    imageEmoji = emojiFallback,
                                    imageRes = selectedPhotoOption.resource
                                )
                                appState.triggerNotification(
                                    title = "Item Added to Menu! 🍽️",
                                    message = "'$dishName' (₹${price.toInt()}) with photo is now live on $restaurantName's menu.",
                                    orderId = "MENU_ADD"
                                )
                                onDismiss()
                            },
                            modifier = Modifier.weight(1.5f).height(46.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ZomatoRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Add to Menu ✓", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
