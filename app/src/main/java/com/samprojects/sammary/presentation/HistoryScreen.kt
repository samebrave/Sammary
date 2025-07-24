package com.samprojects.sammary.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.samprojects.sammary.model.SummarizerViewModel

@Composable
fun HistoryScreen(navController: NavController, viewModel: SummarizerViewModel) {
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    val history = viewModel.history

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading...", fontSize = 18.sp)
            }
        }
        error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Hata: ${error ?: "Unknown error"}", color = Color.Red)
            }
        }
        history.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No summarized video yet.")
            }
        }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 40.dp)) {
                items(history) { item ->
                    SammaryCard(
                            title = item.title,
                            description = item.transcript,
                            onClick = {
                                viewModel.videoTitle.value = item.title
                                viewModel.transcript.value = item.transcript
                                navController.navigate("summary")
                            },
                            onLongClick = {
                                viewModel.deleteSummary(item)
                            }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SammaryCard(title: String, description: String, onClick: () -> Unit, onLongClick: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Summary") },
                text = { Text("Are you sure you want to delete this summary?") },
                confirmButton = {
                    TextButton(
                            onClick = {
                                onLongClick()
                                showDeleteDialog = false
                            }
                    ) { Text("Delete", color = Color.Red) }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
                }
        )
    }

    Card(
            modifier =
                    Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .combinedClickable(
                                    onClick = { onClick() },
                                    onLongClick = { showDeleteDialog = true }
                            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                    modifier =
                            Modifier.size(100.dp, 80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(generateGradientFromTitle(description))
            ) {
                Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.align(Alignment.Center).size(32.dp)
                )

                // Show first letter of title in bottom right
                Box(
                        modifier =
                                Modifier.align(Alignment.BottomEnd)
                                        .padding(6.dp)
                                        .size(20.dp)
                                        .background(
                                                Color.Black.copy(alpha = 0.3f),
                                                RoundedCornerShape(10.dp)
                                        )
                ) {
                    Text(
                            text = title.firstOrNull()?.toString()?.uppercase() ?: "S",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                        text = title,
                        style =
                                MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        lineHeight = 22.sp
                                ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                        text = description,
                        style =
                                MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                ),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

fun generateGradientFromTitle(title: String): Brush {
    val hash = title.hashCode()
    val colors =
            listOf(
                    listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)), // Purple-Indigo
                    listOf(Color(0xFF10B981), Color(0xFF06B6D4)), // Emerald-Cyan
                    listOf(Color(0xFFF59E0B), Color(0xFFEF4444)), // Amber-Red
                    listOf(Color(0xFF8B5CF6), Color(0xFFEC4899)), // Purple-Pink
                    listOf(Color(0xFF06B6D4), Color(0xFF3B82F6)), // Cyan-Blue
                    listOf(Color(0xFFEF4444), Color(0xFFF97316)), // Red-Orange
                    listOf(Color(0xFF10B981), Color(0xFF84CC16)), // Emerald-Lime
                    listOf(Color(0xFF6366F1), Color(0xFF06B6D4)) // Indigo-Cyan
            )

    val selectedColors = colors[Math.abs(hash) % colors.size]
    return Brush.linearGradient(selectedColors)
}
