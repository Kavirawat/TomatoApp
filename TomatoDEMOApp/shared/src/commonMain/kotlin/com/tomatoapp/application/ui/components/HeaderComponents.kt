package com.tomatoapp.application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomatoapp.application.ui.theme.*

@Composable
fun VegNonVegBadge(isVeg: Boolean, modifier: Modifier = Modifier) {
    val borderColor = if (isVeg) ZomatoGreen else ZomatoNonVegRed
    val dotColor = if (isVeg) ZomatoGreen else ZomatoNonVegRed

    Box(
        modifier = modifier
            .size(16.dp)
            .border(width = 1.5.dp, color = borderColor, shape = RoundedCornerShape(3.dp))
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(color = dotColor, shape = CircleShape)
        )
    }
}

@Composable
fun RatingBadge(rating: Double, ratingCount: String? = null, modifier: Modifier = Modifier) {
    Surface(
        color = ZomatoGreen,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$rating",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "★",
                color = Color.White,
                fontSize = 10.sp
            )
            if (ratingCount != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "($ratingCount)",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun TopLocationBar(
    location: String,
    vegOnly: Boolean,
    onVegOnlyToggle: (Boolean) -> Unit,
    onProfileClick: () -> Unit,
    onLocationClick: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onLocationClick() }
            ) {
                Text(text = "📍", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Home",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "▼", fontSize = 10.sp, color = ZomatoRed)
                    }
                    Text(
                        text = location,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Veg Mode Toggle Button
            Surface(
                color = if (vegOnly) {
                    if (MaterialTheme.colorScheme.surface.red < 0.5f) Color(0xFF1B3B22) else ZomatoGreenLight
                } else {
                    MaterialTheme.colorScheme.surface
                },
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (vegOnly) ZomatoGreen else MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.clickable { onVegOnlyToggle(!vegOnly) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VegNonVegBadge(isVeg = true)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "VEG",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (vegOnly) ZomatoGreen else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // User Avatar
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape,
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onProfileClick() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "👤", fontSize = 18.sp)
                }
            }
        }
    }
}
