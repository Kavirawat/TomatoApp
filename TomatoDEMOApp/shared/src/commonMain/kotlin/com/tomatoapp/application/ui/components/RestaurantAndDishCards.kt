package com.tomatoapp.application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomatoapp.application.model.Dish
import com.tomatoapp.application.model.Restaurant
import com.tomatoapp.application.ui.theme.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import tomatoapp.shared.generated.resources.*

/**
 * Intelligent helper to provide an authentic original photo for any dish
 * if explicit imageRes is not attached.
 */
fun getDishPhotoFallback(dish: Dish): DrawableResource {
    val name = dish.name.lowercase()
    return when {
        "paneer" in name || "kadai" in name || "shahi" in name -> Res.drawable.dish_paneer
        "biryani" in name || "rice" in name || "pulao" in name -> Res.drawable.dish_biryani
        "pizza" in name -> Res.drawable.dish_pizza
        "burger" in name -> Res.drawable.dish_burger
        "chole" in name || "bhature" in name -> Res.drawable.dish_chole_bhature
        "dosa" in name -> Res.drawable.dish_dosa
        "sandwich" in name || "toast" in name -> Res.drawable.cat_sandwich
        "idli" in name || "vada" in name || "sambar" in name -> Res.drawable.cat_idli
        "paratha" in name || "roti" in name || "naan" in name -> Res.drawable.cat_paratha
        "chaat" in name || "tikki" in name || "papdi" in name || "golgappe" in name -> Res.drawable.dish_chaat
        "jamun" in name || "gulab" in name || "rasgulla" in name || "sweet" in name || "mithai" in name || "dessert" in name || "cake" in name -> Res.drawable.dish_gulab_jamun
        "thali" in name || "combo" in name || "meal" in name -> Res.drawable.banner_thali
        dish.isVeg -> Res.drawable.dish_paneer
        else -> Res.drawable.dish_biryani
    }
}

/**
 * Popular Restaurant Card matching zomato-home.png
 * Features full-bleed photography, top-left red discount badge ("50% OFF"),
 * and bottom gradient overlay with restaurant name, cuisines, and rating.
 */
@Composable
fun PopularRestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(260.dp)
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 3.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (restaurant.coverImageRes != null) {
                androidx.compose.foundation.Image(
                    painter = org.jetbrains.compose.resources.painterResource(restaurant.coverImageRes),
                    contentDescription = restaurant.name,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = restaurant.coverEmoji, fontSize = 48.sp)
                }
            }

            // Dark gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.30f),
                                Color.Black.copy(alpha = 0.88f)
                            ),
                            startY = 60f
                        )
                    )
            )

            // Top-Left: Red Discount Pill Badge ("50% OFF")
            Surface(
                color = Color(0xFFCB202D),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            ) {
                Text(
                    text = restaurant.offer ?: "50% OFF",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                )
            }

            // Bottom Info: Restaurant Name + Cuisines + Rating
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = restaurant.name,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = restaurant.cuisines.joinToString(", "),
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "★ ${restaurant.rating}",
                        color = Color(0xFFFFD700),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // HERO IMAGE WITH PROMOTED AND BLUE DISCOUNT BADGES (SCREENSHOT 2)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(14.dp))
            ) {
                if (restaurant.coverImageRes != null) {
                    androidx.compose.foundation.Image(
                        painter = org.jetbrains.compose.resources.painterResource(restaurant.coverImageRes),
                        contentDescription = restaurant.name,
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFF9D8D6), Color(0xFFE57373))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = restaurant.coverEmoji,
                            fontSize = 58.sp
                        )
                    }
                }

                // Top-Left: "Promoted" Tag
                if (restaurant.isPromoted) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.50f),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Promoted",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Bottom-Left: Vibrant Blue Discount Badge (e.g. ₹140 OFF, 50% OFF, 30% OFF, ₹150 OFF)
                if (restaurant.offer != null) {
                    Surface(
                        color = Color(0xFF256FEF),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = restaurant.offer,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // INFO SECTION (SCREENSHOT 2)
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp)) {
                // ROW 1: Restaurant Name + Dark Green Rating Pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = restaurant.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF24963F) // Dark rich green matching Zomato
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${restaurant.rating}",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "★",
                                color = Color.White,
                                fontSize = 9.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // ROW 2: Cuisines (Left) + Cost for one (Right)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = restaurant.cuisines.joinToString(", "),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "₹${restaurant.costForOne} for one",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // ROW 3: Delivery Time (Right-Aligned)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = restaurant.deliveryTimeRange,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun DishCard(
    dish: Dish,
    quantityInCart: Int,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Left content: info
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    VegNonVegBadge(isVeg = dish.isVeg)
                    if (dish.isBestseller) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = if (MaterialTheme.colorScheme.surface.red < 0.5f) Color(0xFF332900) else ZomatoGoldLight,
                            shape = RoundedCornerShape(4.dp),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, ZomatoGold)
                        ) {
                            Text(
                                text = "★ BESTSELLER",
                                color = ZomatoGold,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = dish.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹${dish.price.toInt()}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (dish.originalPrice != null && dish.originalPrice > dish.price) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "₹${dish.originalPrice.toInt()}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "★ ${dish.rating}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ZomatoGreen
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${dish.ratingCount})",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = dish.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }

            // Right content: Visual Box + Add / Stepper Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(110.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(96.dp)
                ) {
                    val photoRes = dish.imageRes ?: getDishPhotoFallback(dish)
                    androidx.compose.foundation.Image(
                        painter = painterResource(photoRes),
                        contentDescription = dish.name,
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Quantity / Add Button
                if (quantityInCart == 0) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ZomatoRed),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(34.dp)
                            .clickable { onAddClick() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "ADD",
                                color = ZomatoRed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ZomatoRed,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(34.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .clickable { onRemoveClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "-",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "$quantityInCart",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .clickable { onAddClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
