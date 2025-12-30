package com.expense.expensetracking.common.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.TextBlack
import com.expense.expensetracking.ui.theme.TextGrayDark
import com.expense.expensetracking.ui.theme.TextGrayLight
import com.expense.expensetracking.ui.theme.TextWhite
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

// Binlik ayraç için VisualTransformation
private class ThousandSeparatorTransformation : VisualTransformation {
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

// Binlik ayraç formatı için yardımcı fonksiyon
private fun formatNumberWithDots(input: String): String {
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

@Composable
fun AddCardTextField(
    header: String,
    value: String,
    hint: String,
    onTextValueChange: (String) -> Unit,
    minValue: Int = 0,
    maxValue: Int = 999999999,
    backgroundColor: Color? = null,
    headerColor: Color? = null,
    isError: Boolean = false
) {
    val isDark = isSystemInDarkTheme()

    // Dinamik Renk Tanımlamaları - MaterialTheme öncelikli
    val finalHeaderColor = headerColor ?: MaterialTheme.colorScheme.onSurface
    val finalBackgroundColor = backgroundColor ?: MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface
    val placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = header,
            fontSize = 14.sp,
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            color = finalHeaderColor
        )

        TextField(
            value = value,
            onValueChange = { newValue ->
                if (header == "Bakiye" || header == "Başlangıç Bakiyesi") {
                    // Sadece rakam kabul et (nokta otomatik eklenecek)
                    val digitsOnly = newValue.filter { it.isDigit() }
                    
                    // Max ve min değer kontrolü
                    if (digitsOnly.isNotEmpty()) {
                        val numericValue = digitsOnly.toLongOrNull() ?: 0L
                        if (numericValue <= maxValue) {
                            onTextValueChange(digitsOnly)
                        }
                    } else {
                        onTextValueChange(digitsOnly)
                    }
                } else {
                    onTextValueChange(newValue)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            isError = isError,
            visualTransformation = if (header == "Bakiye" || header == "Başlangıç Bakiyesi") ThousandSeparatorTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (header == "Bakiye" || header == "Başlangıç Bakiyesi") KeyboardType.Number else KeyboardType.Text
            ),
            placeholder = {
                Text(
                    text = hint,
                    color = placeholderColor.copy(alpha = 0.6f),
                    fontSize = 16.sp,
                    fontFamily = Manrope
                )
            },
            trailingIcon = if (header == "Bakiye" || header == "Başlangıç Bakiyesi") {
                {
                    Text(
                        text = "₺",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                        fontFamily = Manrope
                    )
                }
            } else null,
            colors = TextFieldDefaults.colors(
                // Arka Plan Renkleri
                focusedContainerColor = finalBackgroundColor,
                unfocusedContainerColor = finalBackgroundColor,
                disabledContainerColor = finalBackgroundColor,
                errorContainerColor = finalBackgroundColor,

                // Yazı ve İmleç Renkleri
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                errorTextColor = textColor,
                cursorColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,

                // Alt Çizgileri Kaldır
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            )
        )
    }
}