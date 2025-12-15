package com.expense.expensetracking.presentation.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.expense.expensetracking.presentation.reports.ui.HeaderSection
import com.expense.expensetracking.ui.theme.BackgroundDark
import com.expense.expensetracking.ui.theme.BorderColor
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.SurfaceDark
import com.expense.expensetracking.ui.theme.TextGray

@Composable
fun ProfileScreen(

) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item { HeaderSection(title = "Profil") }

        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.size(128.dp)) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(PrimaryGreen.copy(alpha = 0.2f), Color(0xFF27272a))))
                        .padding(4.dp)) {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
                            contentDescription = "Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(4.dp, BackgroundDark, CircleShape)
                        )
                    }
                    // Edit Icon
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-4).dp, y = (-4).dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PrimaryGreen)
                            .border(3.dp, BackgroundDark, CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(androidx.compose.material.icons.Icons.Default.Edit, contentDescription = null, tint = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Ahmet Yılmaz", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("ahmet.yilmaz@email.com", fontSize = 16.sp, color = TextGray, fontWeight = FontWeight.Medium)
            }
        }

        // Genel Ayarlar
        item {
            SectionTitle("GENEL")
            ProfileSectionContainer {
                ProfileMenuItem(icon = androidx.compose.material.icons.Icons.Default.Settings, label = "Hesap Ayarları", showDivider = true)
                ProfileMenuItem(icon = androidx.compose.material.icons.Icons.Default.Notifications, label = "Bildirimler")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Destek
        item {
            SectionTitle("DESTEK")
            ProfileSectionContainer {
                ProfileMenuItem(icon = androidx.compose.material.icons.Icons.Default.Help, label = "Yardım Merkezi", showDivider = true)
                ProfileMenuItem(icon = androidx.compose.material.icons.Icons.Default.Message, label = "Geri Bildirim Gönder")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Çıkış
        item {
            ProfileSectionContainer {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clickable { }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(40.dp).background(Color(0xFFef4444).copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(androidx.compose.material.icons.Icons.Default.ExitToApp, contentDescription = null, tint = Color(0xFFef4444))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Çıkış Yap", color = Color(0xFFef4444), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF71717a),
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
    )
}

@Composable
fun ProfileSectionContainer(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
        content = content
    )
}

@Composable
fun ProfileMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, showDivider: Boolean = false) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clickable { }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(PrimaryGreen.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = PrimaryGreen)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, color = Color(0xFFf4f4f5), fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            Icon(androidx.compose.material.icons.Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF52525b))
        }
        if (showDivider) {
            Divider(color = BorderColor, modifier = Modifier.padding(start = 72.dp, end = 16.dp))
        }
    }
}