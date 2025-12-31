package com.expense.expensetracking.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.expensetracking.R
import com.expense.expensetracking.common.component.BackBtn
import com.expense.expensetracking.ui.theme.Dimens

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateChangePassword: () -> Unit = {},
    onNavigateDeleteAccount: () -> Unit = {}
) {
    val state by viewModel.uiDataState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimens.paddingNormal)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackBtn {
                onNavigateBack()
            }
            Spacer(modifier = Modifier.width(Dimens.spacingSmall))
            Text(
                text = stringResource(R.string.settings_title),
                fontSize = Dimens.textLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(Dimens.spacingLarge))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingSmall)
        ) {
            // Tema Ayarı
            item {
                SettingsSection(title = stringResource(R.string.settings_section_appearance))
                ThemeSettingItem(
                    isDarkMode = state.isDarkMode,
                    onToggle = { isDark ->
                        viewModel.handleIntent(SettingsIntent.ToggleDarkMode(isDark))
                    }
                )
            }

            // Dil Ayarı
            item {
                Spacer(modifier = Modifier.height(Dimens.spacingNormal))
                SettingsSection(title = stringResource(R.string.settings_section_general))
                LanguageSettingItem(
                    selectedLanguage = state.selectedLanguage,
                    onLanguageSelect = { language ->
                        viewModel.handleIntent(SettingsIntent.ChangeLanguage(language))
                    }
                )
                Spacer(modifier = Modifier.height(Dimens.spacingSmall))
                CurrencySettingItem(
                    selectedCurrency = state.selectedCurrency,
                    currencyList = state.currencyList,
                    loadState = state.currencyLoadState,
                    onCurrencySelect = { currency ->
                        viewModel.handleIntent(SettingsIntent.ChangeCurrency(currency))
                    },
                    onLoadCurrencies = {
                        viewModel.handleIntent(SettingsIntent.LoadCurrencies)
                    }
                )
            }

            // Hesap Ayarları
            item {
                Spacer(modifier = Modifier.height(Dimens.spacingNormal))
                SettingsSection(title = stringResource(R.string.settings_section_account))
                SettingItem(
                    icon = Icons.Default.Lock,
                    title = stringResource(R.string.settings_change_password),
                    subtitle = stringResource(R.string.settings_change_password_subtitle),
                    onClick = onNavigateChangePassword
                )
                Spacer(modifier = Modifier.height(Dimens.spacingSmall))
                SettingItem(
                    icon = Icons.Default.Delete,
                    title = stringResource(R.string.settings_delete_account),
                    subtitle = stringResource(R.string.settings_delete_account_subtitle),
                    onClick = onNavigateDeleteAccount,
                    isDestructive = true
                )
            }
        }
    }
}

@Composable
fun SettingsSection(title: String) {
    Text(
        text = title,
        fontSize = Dimens.textSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        modifier = Modifier.padding(
            start = Dimens.paddingSmall,
            bottom = Dimens.paddingTiny
        )
    )
}

@Composable
fun ThemeSettingItem(
    isDarkMode: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.cornerRadiusMedium))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = Dimens.strokeThin,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(Dimens.cornerRadiusMedium)
            )
            .padding(Dimens.paddingNormal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
            contentDescription = "Theme Icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(Dimens.iconNormal)
        )
        Spacer(modifier = Modifier.width(Dimens.spacingNormal))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (isDarkMode) stringResource(R.string.settings_dark_theme) else stringResource(R.string.settings_light_theme),
                fontSize = Dimens.textMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.settings_theme_description),
                fontSize = Dimens.textTiny,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Switch(
            checked = isDarkMode,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                uncheckedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}

@Composable
fun LanguageSettingItem(
    selectedLanguage: String,
    onLanguageSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("Türkçe", "English", "Deutsch", "Français")

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens.cornerRadiusMedium))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = Dimens.strokeThin,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(Dimens.cornerRadiusMedium)
                )
                .clickable { expanded = true }
                .padding(Dimens.paddingNormal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = "Language",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(Dimens.iconNormal)
            )
            Spacer(modifier = Modifier.width(Dimens.spacingNormal))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.settings_language),
                    fontSize = Dimens.textMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = selectedLanguage,
                    fontSize = Dimens.textTiny,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = language,
                            color = if (language == selectedLanguage) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onLanguageSelect(language)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CurrencySettingItem(
    selectedCurrency: String,
    currencyList: List<String>,
    loadState: CurrencyLoadState,
    onCurrencySelect: (String) -> Unit,
    onLoadCurrencies: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Liste açıldığında ve loading state'deyse, veri yüklemeyi tetikle
    LaunchedEffect(expanded) {
        if (expanded && loadState is CurrencyLoadState.Loading) {
            onLoadCurrencies()
        }
    }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens.cornerRadiusMedium))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = Dimens.strokeThin,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(Dimens.cornerRadiusMedium)
                )
                .clickable { expanded = true }
                .padding(Dimens.paddingNormal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AttachMoney,
                contentDescription = "Currency",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(Dimens.iconNormal)
            )
            Spacer(modifier = Modifier.width(Dimens.spacingNormal))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Para Birimi",
                    fontSize = Dimens.textMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = selectedCurrency,
                    fontSize = Dimens.textTiny,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            when (loadState) {
                is CurrencyLoadState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.paddingNormal),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                is CurrencyLoadState.Error -> {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Liste yüklenemedi",
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = { }
                    )
                }
                is CurrencyLoadState.Idle, is CurrencyLoadState.Success -> {
                    if (currencyList.isEmpty()) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = selectedCurrency,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            onClick = {
                                onCurrencySelect(selectedCurrency)
                                expanded = false
                            }
                        )
                    } else {
                        currencyList.forEach { currency ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = currency,
                                        color = if (currency == selectedCurrency)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                onClick = {
                                    onCurrencySelect(currency)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.cornerRadiusMedium))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = Dimens.strokeThin,
                color = if (isDestructive) 
                    MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
                else 
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(Dimens.cornerRadiusMedium)
            )
            .clickable { onClick() }
            .padding(Dimens.paddingNormal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isDestructive) 
                MaterialTheme.colorScheme.error 
            else 
                MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(Dimens.iconNormal)
        )
        Spacer(modifier = Modifier.width(Dimens.spacingNormal))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = Dimens.textMedium,
                fontWeight = FontWeight.Medium,
                color = if (isDestructive) 
                    MaterialTheme.colorScheme.error 
                else 
                    MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = Dimens.textTiny,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}
