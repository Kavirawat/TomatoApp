package com.tomatoapp.application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomatoapp.application.ui.theme.ZomatoRed

/**
 * Authentic Zomato-style Brand Icon:
 * Vibrant Red Squircle Badge with crisp white bold italic typography / emblem.
 */
@Composable
fun TomatoBrandIcon(
    size: Dp = 38.dp,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val cornerRadius = size * 0.28f
    val clickModifier = if (onClick != null) modifier.clickable { onClick() } else modifier

    Surface(
        modifier = clickModifier.size(size),
        shape = RoundedCornerShape(cornerRadius),
        color = Color(0xFFE23744),
        shadowElevation = 3.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFFF334B), Color(0xFFE23744), Color(0xFFC71C2E))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "t",
                    color = Color.White,
                    fontSize = (size.value * 0.70f).sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    lineHeight = (size.value * 0.70f).sp
                )
            }
        }
    }
}

/**
 * Full Zomato-style Brand Logo:
 * [Red Icon Badge] + [Italic "tomato" Wordmark]
 */
@Composable
fun TomatoBrandLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = 34.dp,
    showText: Boolean = true,
    textColor: Color = Color(0xFFE23744),
    onClick: (() -> Unit)? = null
) {
    val clickModifier = if (onClick != null) modifier.clickable { onClick() } else modifier

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = clickModifier
    ) {
        TomatoBrandIcon(size = iconSize)

        if (showText) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "tomato",
                color = textColor,
                fontSize = (iconSize.value * 0.82f).sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                letterSpacing = (-1.2).sp
            )
        }
    }
}
