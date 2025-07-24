package com.samprojects.sammary.presentation

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.samprojects.sammary.model.SummarizerViewModel

@Composable
fun SettingsScreen(navController: NavController, viewModel: SummarizerViewModel) {
    val context = LocalContext.current
    val preferences by viewModel.userPreferences

    LazyColumn(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                    text = "Display Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            SettingSwitchItem(
                    icon = Icons.Default.DarkMode,
                    title = "Dark Mode",
                    checked = preferences.isDarkMode,
                    onCheckedChange = { viewModel.updateDarkMode(it) }
            )
        }

        item {
            FontSizeSettingItem(
                    currentSize = preferences.summaryFontSize,
                    onSizeChange = { viewModel.updateFontSize(it) }
            )
        }

        item {
            Text(
                    text = "General Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }

        item {
            SettingSwitchItem(
                    icon = Icons.Default.Save,
                    title = "Auto-save Summaries",
                    checked = preferences.autoSave,
                    onCheckedChange = { viewModel.updateAutoSave(it) }
            )
        }

        item {
            LanguageSettingItem(
                    currentLanguage = preferences.language,
                    onLanguageChange = { viewModel.updateLanguage(it) }
            )
        }

        item {
            Text(
                    text = "Data Management",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }

        item {
            var showDeleteDialog by remember { mutableStateOf(false) }

            if (showDeleteDialog) {
                AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text("Clear All History") },
                        text = {
                            Text(
                                    "Are you sure you want to delete all saved summaries? This action cannot be undone."
                            )
                        },
                        confirmButton = {
                            TextButton(
                                    onClick = {
                                        viewModel.deleteAllSummaries()
                                        showDeleteDialog = false
                                    }
                            ) { Text("Delete All", color = MaterialTheme.colorScheme.error) }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
                        }
                )
            }

            SettingItem(
                    icon = Icons.Default.DeleteSweep,
                    title = "Clear All History",
                    onClick = { showDeleteDialog = true }
            )
        }

        item {
            Text(
                    text = "Legal & Support",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }

        item {
            SettingItem(
                    icon = Icons.Default.PrivacyTip,
                    title = "Privacy Policy",
                    onClick = {
                        val url = "https://samebrave.github.io/privacy-policy.html"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
            )
        }

        item {
            SettingItem(
                    icon = Icons.Default.Info,
                    title = "Terms & Conditions",
                    onClick = {
                        val url = "https://samebrave.github.io/terms-conditions.html"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
            )
        }
    }
}

@Composable
fun SettingItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Surface(
            modifier = Modifier.fillMaxWidth().height(56.dp).clickable { onClick() },
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp
    ) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
        ) {
            Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
        }
    }
}

@Composable
fun SettingSwitchItem(
        icon: ImageVector,
        title: String,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit
) {
    Surface(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp
    ) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = title, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
            }

            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@Composable
fun FontSizeSettingItem(currentSize: Float, onSizeChange: (Float) -> Unit) {
    Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                        imageVector = Icons.Default.FormatSize,
                        contentDescription = "Font Size",
                        tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                        text = "Summary Font Size",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                        text = "${currentSize.toInt()}sp",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Slider(
                    value = currentSize,
                    onValueChange = onSizeChange,
                    valueRange = 12f..24f,
                    steps = 11
            )

            Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                        text = "Small",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                        text = "Large",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun LanguageSettingItem(currentLanguage: String, onLanguageChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val languages =
            mapOf(
                    "en" to "English",
                    "tr" to "Türkçe",
                    "es" to "Español",
                    "fr" to "Français",
                    "de" to "Deutsch",
                    "it" to "Italiano",
                    "pt" to "Português",
                    "ru" to "Русский"
            )

    Surface(
            modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp
    ) {
        Column {
            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                            text = "Language",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                            text = languages[currentLanguage] ?: "English",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                            imageVector =
                                    if (expanded) Icons.Default.ExpandLess
                                    else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "Collapse" else "Expand",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (expanded) {
                languages.forEach { (code, name) ->
                    Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                                    Modifier.fillMaxWidth()
                                            .clickable {
                                                onLanguageChange(code)
                                                expanded = false
                                            }
                                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        RadioButton(
                                selected = currentLanguage == code,
                                onClick = {
                                    onLanguageChange(code)
                                    expanded = false
                                }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                                text = name,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
