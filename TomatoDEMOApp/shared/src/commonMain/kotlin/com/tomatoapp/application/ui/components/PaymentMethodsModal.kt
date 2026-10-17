package com.tomatoapp.application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.tomatoapp.application.state.LocalTomatoAppState
import com.tomatoapp.application.ui.theme.*

@Composable
fun PaymentMethodsModal(
    onDismiss: () -> Unit
) {
    val appState = LocalTomatoAppState.current

    var selectedMethod by remember { mutableStateOf(appState.selectedPaymentMethod) }
    var customUpiId by remember { mutableStateOf("") }
    var showCustomUpiField by remember { mutableStateOf(false) }

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
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ZomatoRed)
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = TomatoIcons.CreditCard,
                                contentDescription = "Payment",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Select Payment Option",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = if (appState.totalAmount > 0) "Amount to pay: ₹${appState.totalAmount.toInt()}" else "Choose your preferred payment method",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.25f),
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onDismiss() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = TomatoIcons.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Payment Options List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // UPI SECTION
                    item {
                        PaymentCategorySection(
                            title = "UPI APPS & VPA",
                            tag = "INSTANT & FREE"
                        ) {
                            PaymentOptionTile(
                                brandKey = "Google Pay",
                                title = "Google Pay (GPay)",
                                subtitle = "Fastest UPI 1-click payment",
                                isSelected = selectedMethod == "Google Pay",
                                onSelect = { selectedMethod = "Google Pay" }
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                            PaymentOptionTile(
                                brandKey = "PhonePe",
                                title = "PhonePe UPI",
                                subtitle = "Pay directly via PhonePe",
                                isSelected = selectedMethod == "PhonePe",
                                onSelect = { selectedMethod = "PhonePe" }
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                            PaymentOptionTile(
                                brandKey = "Paytm",
                                title = "Paytm UPI",
                                subtitle = "Direct bank transfer",
                                isSelected = selectedMethod == "Paytm UPI",
                                onSelect = { selectedMethod = "Paytm UPI" }
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                            PaymentOptionTile(
                                brandKey = "UPI",
                                title = "Add Any Other UPI ID",
                                subtitle = if (customUpiId.isNotBlank()) customUpiId else "e.g. yourname@oksbi",
                                isSelected = selectedMethod.startsWith("UPI ID:"),
                                onSelect = {
                                    showCustomUpiField = true
                                    selectedMethod = if (customUpiId.isNotBlank()) "UPI ID: $customUpiId" else "UPI / Other App"
                                }
                            )

                            if (showCustomUpiField) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, ZomatoRed),
                                    modifier = Modifier.fillMaxWidth().height(40.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        if (customUpiId.isEmpty()) {
                                            Text("Enter UPI ID (e.g. mobile@upi)", fontSize = 12.sp, color = ZomatoTextLight)
                                        }
                                        BasicTextField(
                                            value = customUpiId,
                                            onValueChange = {
                                                customUpiId = it
                                                selectedMethod = "UPI ID: $it"
                                            },
                                            singleLine = true,
                                            textStyle = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // CARDS SECTION
                    item {
                        PaymentCategorySection(
                            title = "CREDIT & DEBIT CARDS",
                            tag = "CARDS"
                        ) {
                            PaymentOptionTile(
                                brandKey = "HDFC Card",
                                title = "HDFC Bank Debit Card",
                                subtitle = "•••• •••• •••• 4821 (Saved)",
                                isSelected = selectedMethod == "HDFC Card (•••• 4821)",
                                onSelect = { selectedMethod = "HDFC Card (•••• 4821)" }
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                            PaymentOptionTile(
                                brandKey = "New Card",
                                title = "Add New Card",
                                subtitle = "Visa, MasterCard, RuPay, Diners",
                                isSelected = selectedMethod == "New Card",
                                onSelect = { selectedMethod = "New Card" }
                            )
                        }
                    }

                    // WALLETS
                    item {
                        PaymentCategorySection(
                            title = "WALLETS & BALANCES",
                            tag = "WALLETS"
                        ) {
                            PaymentOptionTile(
                                brandKey = "Paytm Wallet",
                                title = "Paytm Wallet",
                                subtitle = "Available Balance: ₹450.00",
                                isSelected = selectedMethod == "Paytm Wallet",
                                onSelect = { selectedMethod = "Paytm Wallet" }
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                            PaymentOptionTile(
                                brandKey = "Amazon Pay",
                                title = "Amazon Pay Balance",
                                subtitle = "1-click checkout",
                                isSelected = selectedMethod == "Amazon Pay",
                                onSelect = { selectedMethod = "Amazon Pay" }
                            )
                        }
                    }

                    // NET BANKING
                    item {
                        PaymentCategorySection(
                            title = "NET BANKING",
                            tag = "BANK"
                        ) {
                            PaymentOptionTile(
                                brandKey = "Net Banking",
                                title = "Net Banking (All Major Banks)",
                                subtitle = "SBI, HDFC, ICICI, Axis, PNB",
                                isSelected = selectedMethod == "Net Banking",
                                onSelect = { selectedMethod = "Net Banking" }
                            )
                        }
                    }

                    // CASH ON DELIVERY (COD)
                    item {
                        PaymentCategorySection(
                            title = "PAY ON DELIVERY",
                            tag = "CASH / QR"
                        ) {
                            PaymentOptionTile(
                                brandKey = "Cash on Delivery",
                                title = "Cash on Delivery (COD)",
                                subtitle = "Pay via cash or UPI QR to delivery rider at doorstep",
                                isSelected = selectedMethod == "Cash on Delivery",
                                onSelect = { selectedMethod = "Cash on Delivery" }
                            )
                        }
                    }
                }

                // Bottom Action Bar
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "SELECTED METHOD",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ZomatoTextMuted
                            )
                            Text(
                                text = selectedMethod,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Button(
                            onClick = {
                                appState.selectedPaymentMethod = selectedMethod
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ZomatoRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(44.dp)
                        ) {
                            Text(
                                text = "Confirm Payment Method",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentCategorySection(
    title: String,
    tag: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ZomatoTextMuted,
                    letterSpacing = 0.8.sp
                )
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = tag,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun PaymentOptionTile(
    brandKey: String,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            PaymentBrandBadge(
                methodKey = brandKey,
                size = 38.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) ZomatoRed else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = ZomatoTextMuted
                )
            }
        }

        Box(
            modifier = Modifier
                .size(22.dp)
                .background(
                    color = if (isSelected) ZomatoRed else Color.Transparent,
                    shape = CircleShape
                )
                .border(
                    width = 1.5.dp,
                    color = if (isSelected) ZomatoRed else MaterialTheme.colorScheme.outline,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = TomatoIcons.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}
