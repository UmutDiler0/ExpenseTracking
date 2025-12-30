package com.expense.expensetracking.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.expensetracking.ui.theme.PrimaryGreen
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

// Binlik ayraç formatı için yardımcı fonksiyon
fun formatNumberWithDots(input: String): String {
    val digitsOnly = input.replace(".", "")
    if (digitsOnly.isEmpty()) return ""
    
    val symbols = DecimalFormatSymbols(Locale.GERMAN).apply {
        groupingSeparator = '.'
    }
    val formatter = DecimalFormat("#,###", symbols)
    return try {
        formatter.format(digitsOnly.toLong())
    } catch (e: Exception) {
        digitsOnly
    }
}

// Formatlı stringi düz sayıya çevirme
fun removeFormatting(formatted: String): String {
    return formatted.replace(".", "")
}

// Binlik ayraç için VisualTransformation
class ThousandSeparatorTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) return TransformedText(text, OffsetMapping.Identity)
        
        val formatted = formatNumberWithDots(originalText)
        
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val textUpToOffset = originalText.take(offset)
                val formattedTextUpToOffset = formatNumberWithDots(textUpToOffset)
                return formattedTextUpToOffset.length
            }
            
            override fun transformedToOriginal(offset: Int): Int {
                val formatted = formatNumberWithDots(originalText)
                if (offset >= formatted.length) return originalText.length
                
                // Formatlanmış metindeki pozisyondan orijinal pozisyonu bul
                var originalOffset = 0
                var formattedOffset = 0
                
                while (formattedOffset < offset && originalOffset < originalText.length) {
                    if (formatted[formattedOffset] == '.') {
                        formattedOffset++
                    } else {
                        formattedOffset++
                        originalOffset++
                    }
                }
                
                return originalOffset
            }
        }
        
        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

@Composable
fun CustomInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isNumeric: Boolean,
    isSingleLine: Boolean,
    minHeight: androidx.compose.ui.unit.Dp = 64.dp,
    backgroundColor: Color,
    textColor: Color,
    labelColor: Color,
    minValue: Int = 1,
    maxValue: Int = 999999999
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = labelColor
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                if (isNumeric) {
                    // Sadece rakam kabul et (nokta otomatik eklenecek)
                    val digitsOnly = newValue.filter { it.isDigit() }
                    
                    // Max ve min değer kontrolü
                    if (digitsOnly.isNotEmpty()) {
                        val numericValue = digitsOnly.toLongOrNull() ?: 0L
                        if (numericValue <= maxValue) {
                            onValueChange(digitsOnly)
                        }
                    } else {
                        onValueChange(digitsOnly)
                    }
                } else {
                    onValueChange(newValue)
                }
            },
            keyboardOptions = if (isNumeric) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
            visualTransformation = if (isNumeric) ThousandSeparatorTransformation() else VisualTransformation.None,
            singleLine = isSingleLine,
            textStyle = TextStyle(
                fontSize = if (isNumeric) 24.sp else 16.sp,
                fontWeight = if (isNumeric) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = minHeight)
                        .clip(RoundedCornerShape(12.dp))
                        .background(backgroundColor)
                        .padding(16.dp),
                    contentAlignment = if(isSingleLine) Alignment.CenterStart else Alignment.TopStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = TextStyle(
                                        fontSize = if (isNumeric) 24.sp else 16.sp,
                                        fontWeight = if (isNumeric) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSystemInDarkTheme()) Color(0xFF6B7280) else Color(0xFF9CA3AF)
                                    )
                                )
                            }
                            innerTextField()
                        }
                        if (isNumeric && value.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "₺",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }
            }
        )
    }
}