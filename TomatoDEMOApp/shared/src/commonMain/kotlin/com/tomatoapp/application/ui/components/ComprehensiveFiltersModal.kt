package com.tomatoapp.application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tomatoapp.application.data.MockData
import com.tomatoapp.application.state.LocalTomatoAppState
import com.tomatoapp.application.state.SortOption
import com.tomatoapp.application.ui.theme.*

@Composable
fun ComprehensiveFiltersModal(
    onDismiss: () -> Unit
) {
    val appState = LocalTomatoAppState.current

    var selectedSort by remember { mutableStateOf(appState.currentSortOption) }
    var vegOnly by remember { mutableStateOf(appState.vegOnly) }
    var ratingFourPlus by remember { mutableStateOf(appState.ratingFourPlusOnly) }
    var fastDelivery by remember { mutableStateOf(appState.fastDeliveryOnly) }
    var selectedCategory by remember { mutableStateOf(appState.selectedCategoryId) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.80f),
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 16.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filters & Sort",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "✕",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier
                                .clickable { onDismiss() }
                                .padding(4.dp)
                        )
                    }
                }

                // Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(18.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // 1. SORT BY
                    Column {
                        Text(
                            text = "SORT BY",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        SortOption.values().forEach { option ->
                            val isSelected = selectedSort == option
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedSort = option }
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = option.label,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) ZomatoRed else MaterialTheme.colorScheme.onSurface
                                )
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedSort = option },
                                    colors = RadioButtonDefaults.colors(selectedColor = ZomatoRed)
                                )
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                        }
                    }

                    // 2. QUICK FILTERS
                    Column {
                        Text(
                            text = "QUICK FILTERS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Pure Veg Toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { vegOnly = !vegOnly }
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🟢", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Pure Veg Only",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Switch(
                                checked = vegOnly,
                                onCheckedChange = { vegOnly = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ZomatoGreen)
                            )
                        }

                        // Rating 4.0+ Toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { ratingFourPlus = !ratingFourPlus }
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "★", fontSize = 16.sp, color = Color(0xFFFFB300))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Rating 4.0 & Above",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Switch(
                                checked = ratingFourPlus,
                                onCheckedChange = { ratingFourPlus = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ZomatoRed)
                            )
                        }

                        // Fast Delivery Toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { fastDelivery = !fastDelivery }
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "⚡", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Fast Delivery (< 25 mins)",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Switch(
                                checked = fastDelivery,
                                onCheckedChange = { fastDelivery = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ZomatoRed)
                            )
                        }
                    }

                    // 3. CUISINE / CATEGORY FILTER
                    Column {
                        Text(
                            text = "CUISINE CATEGORY",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            MockData.categories.forEach { cat ->
                                val isSelected = selectedCategory == cat.id
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) ZomatoRedLight else MaterialTheme.colorScheme.background,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) ZomatoRed else MaterialTheme.colorScheme.outline
                                    ),
                                    modifier = Modifier.clickable { selectedCategory = cat.id }
                                ) {
                                    Text(
                                        text = "${cat.emoji} ${cat.name}",
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) ZomatoRed else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Bottom Action Bar
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                selectedSort = SortOption.RELEVANCE
                                vegOnly = false
                                ratingFourPlus = false
                                fastDelivery = false
                                selectedCategory = "all"
                                appState.currentSortOption = SortOption.RELEVANCE
                                appState.vegOnly = false
                                appState.ratingFourPlusOnly = false
                                appState.fastDeliveryOnly = false
                                appState.selectedCategoryId = "all"
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(46.dp)
                        ) {
                            Text("Reset All", color = MaterialTheme.colorScheme.onSurface)
                        }

                        Button(
                            onClick = {
                                appState.currentSortOption = selectedSort
                                appState.vegOnly = vegOnly
                                appState.ratingFourPlusOnly = ratingFourPlus
                                appState.fastDeliveryOnly = fastDelivery
                                appState.selectedCategoryId = selectedCategory
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ZomatoRed),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1.5f).height(46.dp)
                        ) {
                            Text("Apply Filters ✓", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CuisinesSelectionModal(
    onDismiss: () -> Unit
) {
    val appState = LocalTomatoAppState.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 16.dp
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Cuisine",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "✕",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MockData.categories.forEach { cat ->
                        val isSelected = appState.selectedCategoryId == cat.id
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) ZomatoRedLight else MaterialTheme.colorScheme.background,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) ZomatoRed else MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    appState.selectedCategoryId = cat.id
                                    onDismiss()
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = cat.emoji, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = cat.name,
                                        fontSize = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) ZomatoRed else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                if (isSelected) {
                                    Text(text = "✓", color = ZomatoRed, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
