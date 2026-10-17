package com.tomatoapp.application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tomatoapp.application.state.LocalTomatoAppState
import com.tomatoapp.application.ui.theme.*

@Composable
fun PhoneLoginModal(
    onDismiss: () -> Unit
) {
    val appState = LocalTomatoAppState.current

    // Active mode: "LOGIN" or "SIGNUP"
    var authMode by remember { mutableStateOf(appState.authModalInitialMode) }
    val isSignUp = authMode == "SIGNUP"

    var phoneInput by remember {
        mutableStateOf(
            appState.userPhone.filter { it.isDigit() }.takeLast(10).ifEmpty { "9876543210" }
        )
    }
    var nameInput by remember {
        mutableStateOf(
            if (appState.userName != "Guest User") appState.userName else ""
        )
    }
    var otpInput by remember { mutableStateOf("") }

    var isOtpSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var demoOtpHint by remember { mutableStateOf<String?>(null) }

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
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Header Row with Zomato-style Logo & Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TomatoBrandLogo(
                        iconSize = 32.dp,
                        showText = true
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

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (!isOtpSent) {
                        if (isSignUp) "Create your Tomato account" else "Login with your phone number"
                    } else {
                        "Enter 4-digit verification code"
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (!isOtpSent) {
                    // SWITCHER TABS: [ Login ] | [ Sign Up ]
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.background,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.fillMaxWidth().height(40.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            // Login Tab
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(
                                        color = if (!isSignUp) ZomatoRed else Color.Transparent,
                                        shape = RoundedCornerShape(7.dp)
                                    )
                                    .clickable {
                                        authMode = "LOGIN"
                                        feedbackMessage = null
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Login",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!isSignUp) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }

                            // Sign Up Tab
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(
                                        color = if (isSignUp) ZomatoRed else Color.Transparent,
                                        shape = RoundedCornerShape(7.dp)
                                    )
                                    .clickable {
                                        authMode = "SIGNUP"
                                        feedbackMessage = null
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Sign Up",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSignUp) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // IF SIGN UP: Name input
                    if (isSignUp) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (nameInput.isEmpty()) {
                                    Text(
                                        text = "Full Name",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                                BasicTextField(
                                    value = nameInput,
                                    onValueChange = { nameInput = it },
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Phone Number Input
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "+91  ",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Box(modifier = Modifier.weight(1f)) {
                                if (phoneInput.isEmpty()) {
                                    Text(
                                        text = "Phone number",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                                BasicTextField(
                                    value = phoneInput,
                                    onValueChange = {
                                        phoneInput = it.filter { ch -> ch.isDigit() }.take(10)
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    textStyle = TextStyle(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // ACTION BUTTON: "Login with OTP" / "Sign Up with OTP"
                    Button(
                        onClick = {
                            if (phoneInput.length != 10) {
                                isError = true
                                feedbackMessage = "Please enter a valid 10-digit phone number"
                                return@Button
                            }
                            isLoading = true
                            feedbackMessage = null
                            isError = false
                            appState.sendPhoneOtp(phoneInput) { success, msg, otp ->
                                isLoading = false
                                if (success) {
                                    val validOtp = otp ?: "1234"
                                    isOtpSent = true
                                    otpInput = validOtp
                                    demoOtpHint = validOtp
                                    feedbackMessage = "OTP generated: $validOtp (Auto-filled ✓)"
                                    isError = false
                                } else {
                                    isError = true
                                    feedbackMessage = msg
                                }
                            }
                        },
                        enabled = !isLoading && phoneInput.length == 10,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ZomatoRed),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = if (isSignUp) "Sign Up with OTP" else "Login with OTP",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                } else {
                    // STEP 2: VERIFY OTP
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "OTP sent to +91 $phoneInput",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "Edit",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ZomatoRed,
                            modifier = Modifier.clickable {
                                isOtpSent = false
                                feedbackMessage = null
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // OTP Input Field
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ZomatoRed),
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (otpInput.isEmpty()) {
                                Text(
                                    text = "Enter 4-digit OTP",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                            BasicTextField(
                                value = otpInput,
                                onValueChange = {
                                    otpInput = it.filter { ch -> ch.isDigit() }.take(4)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 6.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    if (demoOtpHint != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Your OTP: $demoOtpHint (Auto-filled ✓)",
                            fontSize = 12.sp,
                            color = ZomatoGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            if (otpInput.length != 4) {
                                isError = true
                                feedbackMessage = "Please enter the 4-digit OTP"
                                return@Button
                            }
                            isLoading = true
                            feedbackMessage = null
                            isError = false
                            val chosenName = if (isSignUp && nameInput.isNotBlank()) nameInput.trim() else ""
                            appState.verifyPhoneOtp(phoneInput, otpInput, chosenName) { success, msg ->
                                isLoading = false
                                if (success) {
                                    onDismiss()
                                } else {
                                    isError = true
                                    feedbackMessage = msg
                                }
                            }
                        },
                        enabled = !isLoading && otpInput.length == 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ZomatoGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Verify & Login",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Error feedback
                if (feedbackMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = feedbackMessage.orEmpty(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isError) ZomatoRed else ZomatoGreen,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
