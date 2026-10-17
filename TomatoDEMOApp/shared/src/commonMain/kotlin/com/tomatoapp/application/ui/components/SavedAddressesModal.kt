package com.tomatoapp.application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tomatoapp.application.model.SavedAddress
import com.tomatoapp.application.state.LocalTomatoAppState
import com.tomatoapp.application.ui.theme.*

@Composable
fun SavedAddressesModal(
    onDismiss: () -> Unit
) {
    val appState = LocalTomatoAppState.current
    var isAddingNew by remember { mutableStateOf(false) }

    // New Address Form State
    var label by remember { mutableStateOf("Home") }
    var houseNo by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Noida") }
    var state by remember { mutableStateOf("Delhi NCR") }
    var pinCode by remember { mutableStateOf("") }
    var receiverName by remember { mutableStateOf(appState.userName) }
    var receiverPhone by remember { mutableStateOf(appState.userPhone) }
    var setAsActive by remember { mutableStateOf(true) }
    var formError by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f),
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
                            Icon(
                                imageVector = TomatoIcons.MapMarkerAlt,
                                contentDescription = "Delivery Address",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (isAddingNew) "Add Delivery Address" else "Select Delivery Address",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Choose where to deliver your food order",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.25f),
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { onDismiss() }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "✕", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Body Content
                if (isAddingNew) {
                    // ADD NEW ADDRESS FORM
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                text = "SELECT ADDRESS TYPE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ZomatoTextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Home 🏠", "Work 🏢", "Other 📍").forEach { typeStr ->
                                    val typeName = typeStr.split(" ").first()
                                    val isSelected = label == typeName
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) ZomatoRedLight else MaterialTheme.colorScheme.surface,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) ZomatoRed else MaterialTheme.colorScheme.outline
                                        ),
                                        modifier = Modifier.clickable { label = typeName }
                                    ) {
                                        Text(
                                            text = typeStr,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) ZomatoRed else MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            AddressFormField("House / Flat / Floor No *", houseNo, "e.g. Flat 301, Tower C") { houseNo = it }
                        }
                        item {
                            AddressFormField("Area / Street / Colony *", area, "e.g. Sector 62, Near Metro Station") { area = it }
                        }
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    AddressFormField("City / District", city, "e.g. Noida") { city = it }
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    AddressFormField("Pincode *", pinCode, "e.g. 201301") { pinCode = it }
                                }
                            }
                        }
                        item {
                            AddressFormField("Receiver Name", receiverName, "Full name of receiver") { receiverName = it }
                        }
                        item {
                            AddressFormField("Receiver Phone Number", receiverPhone, "10-digit mobile number") { receiverPhone = it }
                        }

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { setAsActive = !setAsActive }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = setAsActive,
                                    onCheckedChange = { setAsActive = it },
                                    colors = CheckboxDefaults.colors(checkedColor = ZomatoRed)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Make this my active delivery address now",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        if (formError != null) {
                            item {
                                Text(
                                    text = formError.orEmpty(),
                                    color = ZomatoRed,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { isAddingNew = false },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Cancel", color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        if (houseNo.isBlank() || area.isBlank() || pinCode.isBlank()) {
                                            formError = "Please fill in House No, Area and Pincode."
                                        } else {
                                            val newAddr = SavedAddress(
                                                id = "addr_${(1000..9999).random()}",
                                                label = label,
                                                houseNo = houseNo.trim(),
                                                areaOrVillage = area.trim(),
                                                cityOrDistrict = city.trim(),
                                                state = state.trim(),
                                                pinCode = pinCode.trim(),
                                                receiverName = receiverName.trim(),
                                                receiverPhone = receiverPhone.trim(),
                                                isDefault = setAsActive
                                            )
                                            appState.saveNewAddress(newAddr)
                                            isAddingNew = false
                                        }
                                    },
                                    modifier = Modifier.weight(1.5f),
                                    colors = ButtonDefaults.buttonColors(containerColor = ZomatoRed),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Save & Deliver Here", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    // SAVED ADDRESSES LIST
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(appState.savedAddresses) { addr ->
                            val isActive = addr.id == appState.selectedAddressId || addr.isDefault

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isActive) ZomatoRedLight else MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isActive) 1.5.dp else 1.dp,
                                    color = if (isActive) ZomatoRed else MaterialTheme.colorScheme.outline
                                ),
                                shadowElevation = if (isActive) 3.dp else 1.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Surface(
                                                color = if (isActive) ZomatoRed else MaterialTheme.colorScheme.primaryContainer,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = addr.label.uppercase(),
                                                    color = if (isActive) Color.White else MaterialTheme.colorScheme.onPrimaryContainer,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                )
                                            }

                                            if (isActive) {
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Surface(
                                                    color = ZomatoGreenLight,
                                                    shape = RoundedCornerShape(4.dp),
                                                    border = androidx.compose.foundation.BorderStroke(0.6.dp, ZomatoGreen)
                                                ) {
                                                    Text(
                                                        text = "✓ ACTIVE FOR DELIVERY",
                                                        color = ZomatoGreen,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Text(
                                            text = "✕",
                                            fontSize = 12.sp,
                                            color = ZomatoTextLight,
                                            modifier = Modifier
                                                .clickable { appState.deleteSavedAddress(addr.id) }
                                                .padding(4.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "${addr.houseNo}, ${addr.areaOrVillage}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Text(
                                        text = "${addr.cityOrDistrict}, ${addr.state} - ${addr.pinCode}",
                                        fontSize = 12.sp,
                                        color = ZomatoTextMuted
                                    )

                                    if (addr.receiverName.isNotBlank() || addr.receiverPhone.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Recipient: ${addr.receiverName} (${addr.receiverPhone})",
                                            fontSize = 11.sp,
                                            color = ZomatoTextLight
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    if (!isActive) {
                                        Button(
                                            onClick = { appState.selectSavedAddress(addr) },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(36.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = ZomatoRed),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "DELIVER TO THIS ADDRESS (SET ACTIVE)",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Button(
                                onClick = { isAddingNew = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ZomatoRed),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "+ Add New Delivery Address",
                                    color = ZomatoRed,
                                    fontSize = 13.sp,
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

@Composable
private fun AddressFormField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 11.sp, color = ZomatoTextMuted, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Surface(
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(text = placeholder, fontSize = 12.sp, color = ZomatoTextLight)
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
