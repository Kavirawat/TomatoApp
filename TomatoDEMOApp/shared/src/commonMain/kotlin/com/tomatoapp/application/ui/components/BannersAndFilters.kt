package com.tomatoapp.application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomatoapp.application.data.PromoBanner
import com.tomatoapp.application.state.SortOption
import com.tomatoapp.application.ui.theme.*

import androidx.compose.foundation.Image
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
fun BannerCarousel(
    banners: List<PromoBanner>,
    onBannerClick: (PromoBanner) -> Unit,
    modifier: Modifier = Modifier
) {
    if (banners.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { banners.size })

    // Auto-scroll banners smoothly
    LaunchedEffect(banners.size) {
        while (true) {
            delay(3800)
            val nextPage = (pagerState.currentPage + 1) % banners.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 12.dp),
            pageSpacing = 10.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(165.dp)
        ) { page ->
            val banner = banners[page]
            val bannerColor = when (page % 3) {
                0 -> Color(0xFFFF7043) // Vibrant Orange for "Today's Best Deals"
                1 -> Color(0xFFE91E63) // Magenta for "Weekly Deals"
                else -> Color(0xFFCB202D) // Zomato Red
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable { onBannerClick(banner) },
                shape = RoundedCornerShape(16.dp),
                color = Color.Black,
                shadowElevation = 4.dp
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Full-bleed food image covering the entire banner
                    if (banner.imageRes != null) {
                        Image(
                            painter = painterResource(banner.imageRes),
                            contentDescription = banner.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(bannerColor)
                        )
                    }

                    // Scrim / dark gradient overlay so text remains razor-sharp and legible
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.85f),
                                        Color.Black.copy(alpha = 0.55f),
                                        Color.Black.copy(alpha = 0.15f)
                                    )
                                )
                            )
                    )

                    // Banner content text and badges
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (banner.tag.isNotBlank()) {
                            Surface(
                                color = Color(0xFFCB202D),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.padding(bottom = 6.dp)
                            ) {
                                Text(
                                    text = banner.tag.uppercase(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Text(
                            text = banner.title,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 24.sp,
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            color = Color.White.copy(alpha = 0.28f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = banner.subtitle,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        if (banner.code.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Use Code: ",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = banner.code,
                                    color = Color(0xFFFFEB3B),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Pager indicator dots (Active red dot + inactive grey dots)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            banners.indices.forEach { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (isSelected) 7.dp else 5.dp)
                        .background(
                            color = if (isSelected) Color(0xFFCB202D) else Color(0xFFD6D6D6),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun FilterBar(
    ratingFourPlus: Boolean,
    onRatingToggle: () -> Unit,
    fastDelivery: Boolean,
    onFastDeliveryToggle: () -> Unit,
    pureVeg: Boolean,
    onPureVegToggle: () -> Unit,
    currentSort: SortOption,
    onSortToggle: () -> Unit,
    onFiltersClick: (() -> Unit)? = null,
    onMyShopClick: (() -> Unit)? = null,
    onCuisineClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Filters button with sliders icon (Opens Filters dialog)
        item {
            FilterChipItem(
                label = "Filters",
                isActive = ratingFourPlus || fastDelivery || pureVeg || currentSort != SortOption.RELEVANCE,
                icon = "⚙️",
                onClick = { onFiltersClick?.invoke() ?: onSortToggle() }
            )
        }

        // 2. My Shop / Menu Management
        item {
            FilterChipItem(
                label = "My Shop",
                isActive = false,
                icon = "🏪",
                onClick = { onMyShopClick?.invoke() }
            )
        }

        // 3. Pure Veg pill
        item {
            FilterChipItem(
                label = "Pure Veg",
                isActive = pureVeg,
                icon = "🟢",
                onClick = onPureVegToggle
            )
        }

        // 4. Cuisines with dropdown arrow
        item {
            FilterChipItem(
                label = "Cuisines ⌄",
                isActive = false,
                icon = "🍽️",
                onClick = { onCuisineClick?.invoke() }
            )
        }

        // 5. Rating 4.0+
        item {
            FilterChipItem(
                label = "Rating 4.0+",
                isActive = ratingFourPlus,
                icon = "★",
                onClick = onRatingToggle
            )
        }

        // 6. Fast Delivery
        item {
            FilterChipItem(
                label = "Fast Delivery (<25m)",
                isActive = fastDelivery,
                icon = "⚡",
                onClick = onFastDeliveryToggle
            )
        }

        // 7. Sort Option
        item {
            FilterChipItem(
                label = currentSort.label,
                isActive = currentSort != SortOption.RELEVANCE,
                icon = "⇅",
                onClick = onSortToggle
            )
        }
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    isActive: Boolean,
    icon: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isActive) 1.5.dp else 1.dp,
            color = if (isActive) ZomatoRed else MaterialTheme.colorScheme.outline
        ),
        shadowElevation = 2.dp,
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isActive) ZomatoRed else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
