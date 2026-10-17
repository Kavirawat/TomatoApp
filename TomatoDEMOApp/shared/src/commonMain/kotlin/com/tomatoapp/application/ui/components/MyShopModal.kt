package com.tomatoapp.application.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tomatoapp.application.model.Dish
import com.tomatoapp.application.state.LocalTomatoAppState
import com.tomatoapp.application.ui.theme.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import tomatoapp.shared.generated.resources.*

data class OriginPhotoOption(
    val id: String,
    val name: String,
    val resource: DrawableResource
)

@Composable
fun MyShopModal(
    onDismiss: () -> Unit
) {
    val appState = LocalTomatoAppState.current

    // Target restaurant for the shop (default: first restaurant or active)
    val shopRestaurant = appState.restaurants.find { it.id == appState.myShopRestaurantId }
        ?: appState.restaurants.firstOrNull()

    var searchQuery by remember { mutableStateOf("") }
    var editingDish by remember { mutableStateOf<Dish?>(null) }
    var isAddingNewDish by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 16.dp
        ) {
            if (shopRestaurant == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No shop found", color = MaterialTheme.colorScheme.onSurface)
                }
                return@Surface
            }

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
                            Surface(
                                shape = CircleShape,
                                color = Color.White,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = "🏪", fontSize = 20.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "My Shop - ${shopRestaurant.name}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${shopRestaurant.dishes.size} Items • ${shopRestaurant.address}",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }

                        Text(
                            text = "✕",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                                .clickable { onDismiss() }
                                .padding(4.dp)
                        )
                    }
                }

                // Subheader with Add Dish CTA & Search
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Shop Menu Items",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Button(
                                onClick = { isAddingNewDish = true },
                                colors = ButtonDefaults.buttonColors(containerColor = ZomatoGreen),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "➕ Add Item",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Search Field inside My Shop
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier.fillMaxWidth().height(42.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = TomatoIcons.Search,
                                    contentDescription = "Search",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(modifier = Modifier.weight(1f)) {
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Search dishes in your shop...",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                        )
                                    }
                                    BasicTextField(
                                        value = searchQuery,
                                        onValueChange = { searchQuery = it },
                                        singleLine = true,
                                        textStyle = TextStyle(
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                if (searchQuery.isNotEmpty()) {
                                    Text(
                                        text = "✕",
                                        fontSize = 12.sp,
                                        modifier = Modifier.clickable { searchQuery = "" }
                                    )
                                }
                            }
                        }
                    }
                }

                // Filtered Dishes List
                val displayedDishes = shopRestaurant.dishes.filter {
                    searchQuery.isBlank() ||
                            it.name.contains(searchQuery, ignoreCase = true) ||
                            it.category.contains(searchQuery, ignoreCase = true)
                }

                if (displayedDishes.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🍽️", fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No items match your search",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentPadding = PaddingValues(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(displayedDishes, key = { it.id }) { dish ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                shadowElevation = 1.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Dish Image or Emoji
                                    Box(
                                        modifier = Modifier
                                            .size(68.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.background)
                                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (dish.imageRes != null) {
                                            Image(
                                                painter = painterResource(dish.imageRes),
                                                contentDescription = dish.name,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        } else {
                                            Text(text = dish.imageEmoji, fontSize = 32.sp)
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    // Dish Info
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            VegNonVegBadge(isVeg = dish.isVeg)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = dish.name,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                maxLines = 1
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "₹${dish.price.toInt()} • ${dish.category}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = ZomatoRed
                                        )
                                        Text(
                                            text = dish.description,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                            maxLines = 1
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Action Buttons: Edit & Delete
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        // Edit Button
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, ZomatoRed.copy(alpha = 0.5f)),
                                            modifier = Modifier.clickable {
                                                editingDish = dish
                                            }
                                        ) {
                                            Text(
                                                text = "✏️ Edit",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ZomatoRed,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                            )
                                        }

                                        // Delete Button
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (appState.isDarkMode) Color(0xFF381215) else Color(0xFFFEF2F2),
                                            border = androidx.compose.foundation.BorderStroke(
                                                1.dp,
                                                if (appState.isDarkMode) Color(0xFF6A1E24) else Color(0xFFFCA5A5)
                                            ),
                                            modifier = Modifier.clickable {
                                                appState.deleteDishFromRestaurant(shopRestaurant.id, dish.id)
                                            }
                                        ) {
                                            Text(
                                                text = "🗑️",
                                                fontSize = 13.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ADD / EDIT DISH DIALOG
            if (isAddingNewDish || editingDish != null) {
                DishFormDialog(
                    restaurantId = shopRestaurant.id,
                    initialDish = editingDish,
                    onDismiss = {
                        isAddingNewDish = false
                        editingDish = null
                    },
                    onSaved = {
                        isAddingNewDish = false
                        editingDish = null
                    }
                )
            }
        }
    }
}

@Composable
fun DishFormDialog(
    restaurantId: String,
    initialDish: Dish?,
    onDismiss: () -> Unit,
    onSaved: () -> Unit
) {
    val appState = LocalTomatoAppState.current
    val isEditing = initialDish != null

    var name by remember { mutableStateOf(initialDish?.name ?: "") }
    var priceText by remember { mutableStateOf(initialDish?.price?.toInt()?.toString() ?: "") }
    var category by remember { mutableStateOf(initialDish?.category ?: "Main Course") }
    var description by remember { mutableStateOf(initialDish?.description ?: "") }
    var isVeg by remember { mutableStateOf(initialDish?.isVeg ?: true) }
    var selectedEmoji by remember { mutableStateOf(initialDish?.imageEmoji ?: "🍲") }
    var selectedPhotoRes by remember { mutableStateOf<DrawableResource?>(initialDish?.imageRes) }
    var errorMessage by remember { mutableStateOf("") }

    val categories = listOf(
        "Chole Bhature", "Sandwich", "Dosa", "Burger", "Idli", "Paratha",
        "North Indian", "South Indian", "Fast Food", "Chinese", "Desserts", "Beverages"
    )

    // Authentic Origin Photos available in the project
    val originPhotoOptions = listOf(
        OriginPhotoOption("cb", "Chole Bhature", Res.drawable.cat_chole_bhature),
        OriginPhotoOption("sw", "Sandwich", Res.drawable.cat_sandwich),
        OriginPhotoOption("ds", "Masala Dosa", Res.drawable.cat_dosa),
        OriginPhotoOption("bg", "Veg Burger", Res.drawable.cat_burger),
        OriginPhotoOption("id", "Idli Sambar", Res.drawable.cat_idli),
        OriginPhotoOption("pr", "Aloo Paratha", Res.drawable.cat_paratha),
        OriginPhotoOption("by", "Biryani", Res.drawable.dish_biryani),
        OriginPhotoOption("pn", "Paneer", Res.drawable.dish_paneer),
        OriginPhotoOption("pz", "Pizza", Res.drawable.dish_pizza),
        OriginPhotoOption("gj", "Gulab Jamun", Res.drawable.dish_gulab_jamun)
    )

    val emojis = listOf("🍲", "🍛", "🍗", "🍕", "🍔", "🥟", "🥗", "🍰", "🥤", "🧆", "🍜")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.88f),
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 20.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Dialog Header
                Surface(
                    color = ZomatoRed,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isEditing) "Edit Food Item Details" else "Add Item with Origin Photo",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "✕",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.clickable { onDismiss() }
                        )
                    }
                }

                // Scrollable Form
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
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    // 1. ORIGIN PHOTO SELECTION SECTION
                    Column {
                        Text(
                            text = "Choose Origin Food Photo *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(originPhotoOptions) { opt ->
                                val isSelected = selectedPhotoRes == opt.resource
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        selectedPhotoRes = opt.resource
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(
                                                width = if (isSelected) 2.5.dp else 1.dp,
                                                color = if (isSelected) ZomatoRed else MaterialTheme.colorScheme.outline,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        Image(
                                            painter = painterResource(opt.resource),
                                            contentDescription = opt.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = opt.name,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) ZomatoRed else MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    // 2. DISH NAME
                    Column {
                        Text(
                            text = "Item Name *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.background,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth().height(46.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp), contentAlignment = Alignment.CenterStart) {
                                if (name.isEmpty()) {
                                    Text("e.g. Special Chole Bhature", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                                }
                                BasicTextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    singleLine = true,
                                    textStyle = TextStyle(fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // 3. PRICE
                    Column {
                        Text(
                            text = "Price (₹) *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.background,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth().height(46.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp), contentAlignment = Alignment.CenterStart) {
                                if (priceText.isEmpty()) {
                                    Text("e.g. 150", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                                }
                                BasicTextField(
                                    value = priceText,
                                    onValueChange = { priceText = it.filter { ch -> ch.isDigit() } },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    textStyle = TextStyle(fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // 4. VEG / NON-VEG TOGGLE
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isVeg) {
                                if (appState.isDarkMode) Color(0xFF1B3B22) else ZomatoGreenLight
                            } else MaterialTheme.colorScheme.background,
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (isVeg) ZomatoGreen else MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier.weight(1f).clickable { isVeg = true }
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
                            shape = RoundedCornerShape(8.dp),
                            color = if (!isVeg) {
                                if (appState.isDarkMode) Color(0xFF381215) else ZomatoRedLight
                            } else MaterialTheme.colorScheme.background,
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (!isVeg) ZomatoRed else MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier.weight(1f).clickable { isVeg = false }
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

                    // 5. CATEGORY SELECTION
                    Column {
                        Text(
                            text = "Category",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(categories) { cat ->
                                val isSelected = category == cat
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isSelected) ZomatoRed else MaterialTheme.colorScheme.background,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) ZomatoRed else MaterialTheme.colorScheme.outline),
                                    modifier = Modifier.clickable { category = cat }
                                ) {
                                    Text(
                                        text = cat,
                                        fontSize = 11.sp,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }

                    // 6. DESCRIPTION
                    Column {
                        Text(
                            text = "Description",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.background,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth().height(55.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize().padding(10.dp), contentAlignment = Alignment.TopStart) {
                                if (description.isEmpty()) {
                                    Text("Brief description of ingredients...", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
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

                // Action Buttons
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(46.dp)
                        ) {
                            Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                        }

                        Button(
                            onClick = {
                                if (name.isBlank()) {
                                    errorMessage = "Please enter item name"
                                    return@Button
                                }
                                val price = priceText.toDoubleOrNull()
                                if (price == null || price <= 0) {
                                    errorMessage = "Please enter valid price"
                                    return@Button
                                }

                                if (initialDish != null) {
                                    val updated = initialDish.copy(
                                        name = name.trim(),
                                        price = price,
                                        category = category,
                                        description = description.ifBlank { initialDish.description },
                                        isVeg = isVeg,
                                        imageRes = selectedPhotoRes ?: initialDish.imageRes,
                                        imageEmoji = selectedEmoji
                                    )
                                    appState.updateDishInRestaurant(restaurantId, updated)
                                    appState.triggerNotification(
                                        title = "Item Updated! ✓",
                                        message = "'${name.trim()}' has been updated in your shop.",
                                        orderId = "SHOP_EDIT"
                                    )
                                } else {
                                    appState.addDishToRestaurant(
                                        restaurantId = restaurantId,
                                        name = name.trim(),
                                        price = price,
                                        description = description.trim(),
                                        category = category,
                                        isVeg = isVeg,
                                        imageEmoji = selectedEmoji,
                                        imageRes = selectedPhotoRes
                                    )
                                    appState.triggerNotification(
                                        title = "Item Added! 🍽️",
                                        message = "'${name.trim()}' is now available in your shop.",
                                        orderId = "SHOP_ADD"
                                    )
                                }
                                onSaved()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ZomatoRed),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1.5f).height(46.dp)
                        ) {
                            Text(
                                text = if (isEditing) "Save Changes ✓" else "Add to Shop ✓",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
