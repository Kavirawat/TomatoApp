package com.tomatoapp.application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomatoapp.application.ui.theme.TomatoIcons

/**
 * Authentic branded vector icons for Payment Methods:
 * PhonePe, Google Pay (GPay), Paytm, UPI, Cards, Net Banking, COD, Amazon Pay, etc.
 */
object PaymentBrandIcons {

    val GoogleG: ImageVector by lazy {
        ImageVector.Builder(
            name = "GoogleG",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).addPath(
            pathData = PathParser().parsePathString("M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z").toNodes(),
            fill = SolidColor(Color(0xFF4285F4)) // Google Blue
        ).addPath(
            pathData = PathParser().parsePathString("M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z").toNodes(),
            fill = SolidColor(Color(0xFF34A853)) // Google Green
        ).addPath(
            pathData = PathParser().parsePathString("M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z").toNodes(),
            fill = SolidColor(Color(0xFFFBBC05)) // Google Yellow
        ).addPath(
            pathData = PathParser().parsePathString("M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z").toNodes(),
            fill = SolidColor(Color(0xFFEA4335)) // Google Red
        ).build()
    }

    val UpiLogo: ImageVector by lazy {
        ImageVector.Builder(
            name = "UpiLogo",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).addPath(
            pathData = PathParser().parsePathString("M12 3l-8 9h6l-2 9 10-11h-6l2-7z").toNodes(),
            fill = SolidColor(Color(0xFF097939)) // NPCI UPI Green
        ).addPath(
            pathData = PathParser().parsePathString("M16 3l-4 5h3l-2 5 5-6h-3l2-4z").toNodes(),
            fill = SolidColor(Color(0xFFED7524)) // UPI Orange
        ).build()
    }
}

@Composable
fun PaymentBrandBadge(
    methodKey: String,
    size: Dp = 38.dp,
    modifier: Modifier = Modifier
) {
    val key = methodKey.lowercase()

    when {
        // PHONEPE - Official Purple Badge with White Devanagari 'पे'
        key.contains("phonepe") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .background(Color(0xFF5F259F), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "पे",
                    color = Color.White,
                    fontSize = (size.value * 0.52f).sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // GOOGLE PAY - Official 4-Color Google G Badge
        key.contains("google") || key.contains("gpay") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = PaymentBrandIcons.GoogleG,
                    contentDescription = "Google Pay",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(size * 0.65f)
                )
            }
        }

        // PAYTM UPI / WALLET - Official Dark Blue & Cyan Badge
        key.contains("paytm wallet") || (key.contains("paytm") && key.contains("wallet")) -> {
            Box(
                modifier = modifier
                    .size(size)
                    .background(Color(0xFFE1F5FE), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF00BAF2), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = TomatoIcons.Wallet,
                    contentDescription = "Paytm Wallet",
                    tint = Color(0xFF00BAF2),
                    modifier = Modifier.size(size * 0.58f)
                )
            }
        }

        key.contains("paytm") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFB0BEC5), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Pay",
                        color = Color(0xFF002970), // Paytm Dark Blue
                        fontSize = (size.value * 0.32f).sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "tm",
                        color = Color(0xFF00BAF2), // Paytm Cyan Blue
                        fontSize = (size.value * 0.32f).sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        // AMAZON PAY
        key.contains("amazon") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .background(Color(0xFF232F3E), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "pay",
                        color = Color(0xFFFF9900),
                        fontSize = (size.value * 0.30f).sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Box(
                        modifier = Modifier
                            .width(size * 0.5f)
                            .height(2.dp)
                            .background(Color(0xFFFF9900), RoundedCornerShape(1.dp))
                    )
                }
            }
        }

        // GENERIC UPI
        key.contains("upi") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = PaymentBrandIcons.UpiLogo,
                        contentDescription = "UPI",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(size * 0.45f)
                    )
                    Text(
                        text = "UPI",
                        color = Color(0xFF097939),
                        fontSize = (size.value * 0.28f).sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }

        // NEW CARD / ADD CARD
        key.contains("new card") || key.contains("add card") || key.contains("add new") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .background(Color(0xFFEDE7F6), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFB39DDB), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = TomatoIcons.AddCard,
                    contentDescription = "Add Card",
                    tint = Color(0xFF5E35B1),
                    modifier = Modifier.size(size * 0.6f)
                )
            }
        }

        // CARDS (DEBIT / CREDIT)
        key.contains("card") || key.contains("hdfc") || key.contains("debit") || key.contains("credit") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .background(Color(0xFF0D47A1), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(4.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Gold EMV Chip
                        Box(
                            modifier = Modifier
                                .size(size * 0.22f, size * 0.18f)
                                .background(Color(0xFFFFD54F), RoundedCornerShape(2.dp))
                        )
                        // Visa/Mastercard circles
                        Row {
                            Box(modifier = Modifier.size(size * 0.16f).background(Color(0xFFEB001B), CircleShape))
                            Box(modifier = Modifier.size(size * 0.16f).offset(x = (-3).dp).background(Color(0xFFF79E1B), CircleShape))
                        }
                    }
                    Text(
                        text = "••••",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = (size.value * 0.22f).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // NET BANKING
        key.contains("net banking") || key.contains("bank") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .background(Color(0xFFE0F2F1), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = TomatoIcons.Bank,
                    contentDescription = "Net Banking",
                    tint = Color(0xFF00695C),
                    modifier = Modifier.size(size * 0.6f)
                )
            }
        }

        // CASH ON DELIVERY (COD)
        key.contains("cash") || key.contains("cod") || key.contains("delivery") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF81C784), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = TomatoIcons.Cash,
                        contentDescription = "Cash",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(size * 0.5f)
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                    Text(
                        text = "₹",
                        color = Color(0xFF2E7D32),
                        fontSize = (size.value * 0.36f).sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        // FALLBACK / DEFAULT
        else -> {
            Box(
                modifier = modifier
                    .size(size)
                    .background(Color(0xFFFFF3E0), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = TomatoIcons.CreditCard,
                    contentDescription = "Payment",
                    tint = Color(0xFFE65100),
                    modifier = Modifier.size(size * 0.6f)
                )
            }
        }
    }
}
