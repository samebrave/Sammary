package com.samprojects.sammary.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.YoutubeSearchedFor
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.samprojects.sammary.model.SummarizerViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: SummarizerViewModel) {
    var youtubeLink by remember { mutableStateOf("") }
    val preferences by viewModel.userPreferences

    Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 48.dp)
        ) {
            Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp).padding(bottom = 16.dp)
            )

            Text(
                    text = "YouTube Summarizer",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                    text = "Transform long videos into concise summaries",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
            )
        }

        Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                OutlinedTextField(
                        value = youtubeLink,
                        onValueChange = { youtubeLink = it },
                        placeholder = {
                            Text(
                                    "Paste YouTube link here...",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        leadingIcon = {
                            Icon(
                                    imageVector = Icons.Default.YoutubeSearchedFor,
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors =
                                OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor =
                                                MaterialTheme.colorScheme.outline.copy(
                                                        alpha = 0.5f
                                                ),
                                        focusedBorderColor = MaterialTheme.colorScheme.primary
                                ),
                        modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                        onClick = {
                            if (youtubeLink.isNotBlank()) {
                                viewModel.fetchTranscript(
                                        youtubeLink.trim(),
                                        language = preferences.language
                                )
                                navController.navigate("summary")
                            }
                        },
                        enabled = youtubeLink.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        disabledContainerColor =
                                                MaterialTheme.colorScheme.surfaceVariant
                                )
                ) { Text("Generate Summary", fontSize = 16.sp, fontWeight = FontWeight.Medium) }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
        ) {
            InfoChip("Fast", "⚡", modifier = Modifier.weight(1f))
            InfoChip("Accurate", "🎯", modifier = Modifier.weight(1f))
            InfoChip("Multi-language", "🌍", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun InfoChip(title: String, emoji: String, modifier: Modifier = Modifier) {
    Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
            modifier = modifier
    ) {
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
        ) {
            Text(text = emoji, fontSize = 20.sp, modifier = Modifier.padding(bottom = 4.dp))
            Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
            )
        }
    }
}
