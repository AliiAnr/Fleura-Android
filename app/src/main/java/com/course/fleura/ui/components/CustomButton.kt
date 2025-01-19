package com.course.fleura.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.R
import com.course.fleura.ui.theme.base40
import com.course.fleura.ui.theme.noAvail
import com.course.fleura.ui.theme.onPrimaryLight
import com.course.fleura.ui.theme.primaryLight

@Composable
fun CustomButton(
    modifier: Modifier = Modifier, // Modifier default tanpa ukuran
    defaultWidth: Dp = 370.dp, // Ukuran default yang dapat diubah
    defaultHeight: Dp = 55.dp,
    textVerticalPadding: Dp = 12.dp,
    textHorizontalPadding: Dp = 24.dp,
    text: String = "",
    isAvailable: Boolean = true,
    isOutlined: Boolean = false,
    backgroundColor: Color = primaryLight,
    outlinedColor: Color = primaryLight,
    textColor: Color = onPrimaryLight,
    shape: RoundedCornerShape = RoundedCornerShape(50.dp),
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    icon: Painter? = null, // Parameter untuk ikon atau gambar
    iconSpacing: Dp = 8.dp, // Jarak antara ikon dan teks
    borderWidth: Dp = 1.dp,
    textAnnotatedString: AnnotatedString? = null,
    onClick: () -> Unit
) {
    val buttonModifier = modifier
        .width(defaultWidth)
        .height(defaultHeight)

    if (isOutlined) {
        // Outlined Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = buttonModifier
                .border(width = borderWidth, color = outlinedColor, shape = shape)
                .clickable(
                    onClick = onClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() })
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (icon != null) {
                    Image(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(iconSpacing))
                }
                Text(
                    text = text,
                    color = outlinedColor,
                    fontSize = fontSize,
                    fontWeight = fontWeight
                )
            }
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = buttonModifier
                .background(
                    color = if (isAvailable) backgroundColor else noAvail,
                    shape = shape
                )
                .clickable(
                    onClick = onClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() })
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (icon != null) {
                    Image(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(iconSpacing))
                }

                if (textAnnotatedString != null) {
                    Text(
                        text = textAnnotatedString,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                    )
                } else {
                    Text(
                        text = text,
                        color = textColor,
                        fontSize = fontSize,
                        fontWeight = fontWeight
                    )
                }
            }
        }
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
    ) {
        // Tombol minus (-)
        Box(
            modifier = Modifier
                .size(40.dp)
                .border(2.dp, base40, RoundedCornerShape(6.dp))
                .clickable(
                    onClick = { if (quantity > 0) onQuantityChange(quantity - 1) },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                painter = painterResource(id = R.drawable.minus),
                contentDescription = "Minus",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

        }

        // Teks quantity
        Text(
            text = quantity.toString(),
            fontSize = 18.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(60.dp)

        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .border(2.dp, base40, RoundedCornerShape(6.dp))
                .clickable(
                    onClick = { onQuantityChange(quantity + 1) },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                painter = painterResource(id = R.drawable.add),
                contentDescription = "Plus",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

        }
    }
}
