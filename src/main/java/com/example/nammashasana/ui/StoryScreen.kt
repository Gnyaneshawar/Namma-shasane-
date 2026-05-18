package com.example.nammashasana.ui

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.nammashasana.data.Inscription
import com.example.nammashasana.ui.theme.HeritageGreen
import com.example.nammashasana.ui.theme.HeritageRed
import com.example.nammashasana.ui.theme.IgBackground
import com.example.nammashasana.ui.theme.IgSecondaryText
import com.example.nammashasana.ui.theme.StoneDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryScreen(
    inscriptionId: Long,
    viewModel: ShasanaViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val inscriptions by viewModel.allInscriptions.collectAsState()
    val inscription = inscriptions.find { it.id == inscriptionId }
    var isPlayingAudio by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var reportNotes by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "History Record", 
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* More menu */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IgBackground)
            )
        },
        containerColor = IgBackground
    ) { padding ->
        inscription?.let { item ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(StoneDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(item.dynasty.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(item.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${item.dynasty} • ${item.era}", color = IgSecondaryText, fontSize = 11.sp)
                    }
                }

                // Image with Loading state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    var isLoading by remember { mutableStateOf(true) }
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.imageUrl ?: "https://images.unsplash.com/photo-1599341671302-3f1df2156294?q=80&w=1000")
                            .crossfade(true)
                            .build(),
                        contentDescription = item.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        onLoading = { isLoading = true },
                        onSuccess = { isLoading = false },
                        onError = { isLoading = false }
                    )
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                
                // Interaction Bar
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.markAsVisited(item.id, !item.isVisited) }) {
                        Icon(
                            imageVector = if (item.isVisited) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Visited",
                            tint = if (item.isVisited) HeritageGreen else Color.Black
                        )
                    }
                    IconButton(onClick = { /* Comment */ }) {
                        Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Comment")
                    }
                    IconButton(onClick = { viewModel.shareInscription(item) }) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Share")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { showReportDialog = true }) {
                        Icon(Icons.Default.WarningAmber, contentDescription = "Report Damage", tint = HeritageRed)
                    }
                }

                // Heritage Content
                Column(modifier = Modifier.padding(16.dp)) {
                    ParchmentCard {
                        // Interactive Audio Guide Tool
                        Surface(
                            color = Color.Black.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FloatingActionButton(
                                    onClick = { isPlayingAudio = !isPlayingAudio },
                                    modifier = Modifier.size(40.dp),
                                    containerColor = StoneDark,
                                    contentColor = Color.White,
                                    shape = CircleShape
                                ) {
                                    Icon(
                                        imageVector = if (isPlayingAudio) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text("GenAI Narrative Guide", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                                    Text(
                                        if (isPlayingAudio) "Speaking... (Kannada & English)" else "Tap to listen to the history",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }

                        ShasanaTitle(text = "Modern Kannada")
                        ShasanaBody(text = item.translation)

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        ShasanaTitle(text = "English Decryption")
                        ShasanaBody(text = item.englishTranslation)
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        ShasanaTitle(text = "Historical Significance")
                        ShasanaBody(text = item.explanation)

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = {
                                val gmmIntentUri = "google.navigation:q=${item.latitude},${item.longitude}".toUri()
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                context.startActivity(mapIntent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = HeritageGreen),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Directions, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Navigate to Monument")
                        }
                    }

                    if (item.isDamageReported) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = HeritageRed.copy(alpha = 0.05f)),
                            modifier = Modifier.fillMaxWidth(),
                            border = androidx.compose.foundation.BorderStroke(1.dp, HeritageRed.copy(alpha = 0.2f))
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = HeritageRed)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Preservation Alert", color = HeritageRed, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(item.damageNotes ?: "Damage reported. Verification in progress.", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // Damage Reporting Dialog
    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = { Text("Preservation Report") },
            text = {
                Column {
                    Text("Help protect our history. Describe any signs of neglect or damage at this site.")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = reportNotes,
                        onValueChange = { reportNotes = it },
                        placeholder = { Text("Describe the situation...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.reportDamage(inscriptionId, reportNotes)
                    showReportDialog = false
                }) {
                    Text("Submit Alert", color = HeritageRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
