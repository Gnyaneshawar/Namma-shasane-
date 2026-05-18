package com.example.nammashasana.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.nammashasana.data.Inscription
import com.example.nammashasana.ui.theme.IgBackground
import com.example.nammashasana.ui.theme.IgSecondaryText
import com.example.nammashasana.ui.theme.HeritageRed
import com.example.nammashasana.ui.theme.StoneDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    viewModel: ShasanaViewModel,
    onInscriptionClick: (Long) -> Unit
) {
    val inscriptions by viewModel.allInscriptions.collectAsState()
    val dynasties = inscriptions.map { it.dynasty }.distinct()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Namma Shasana", 
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-1).sp
                        )
                    ) 
                },
                actions = {
                    IconButton(onClick = { /* Activity */ }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Activity")
                    }
                    IconButton(onClick = { /* Direct */ }) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Messages")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IgBackground)
            )
        },
        containerColor = IgBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Dynasty Stories
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(dynasties) { dynasty ->
                        DynastyStoryItem(dynasty)
                    }
                }
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
            }

            // Feed Posts
            items(inscriptions) { inscription ->
                DiscoverPostItem(
                    inscription = inscription,
                    onPostClick = { onInscriptionClick(inscription.id) },
                    onShare = { viewModel.shareInscription(inscription) }
                )
            }
        }
    }
}

@Composable
fun DynastyStoryItem(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(listOf(Color.Yellow, HeritageRed, Color.Magenta)),
                    shape = CircleShape
                )
                .padding(3.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(StoneDark),
                contentAlignment = Alignment.Center
            ) {
                Text(name.take(1), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 4.dp),
            maxLines = 1
        )
    }
}

@Composable
fun DiscoverPostItem(
    inscription: Inscription,
    onPostClick: () -> Unit,
    onShare: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        // Post Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(StoneDark),
                contentAlignment = Alignment.Center
            ) {
                Text(inscription.dynasty.take(1), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = inscription.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${inscription.dynasty} • ${inscription.era}",
                    style = MaterialTheme.typography.labelSmall,
                    color = IgSecondaryText
                )
            }
            IconButton(onClick = { /* More options */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }
        }

        // Post Image with improved loading
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.LightGray.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(inscription.imageUrl ?: "https://images.unsplash.com/photo-1599341671302-3f1df2156294?q=80&w=1000")
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                onLoading = { isLoading = true; isError = false },
                onSuccess = { isLoading = false; isError = false },
                onError = { isLoading = false; isError = true }
            )
            
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(32.dp), strokeWidth = 2.dp, color = HeritageRed)
            }
            
            if (isError) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ImageNotSupported, contentDescription = null, tint = Color.Gray)
                    Text("Image unavailable", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            }
        }

        // Post Actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Like */ }) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = "Like")
            }
            IconButton(onClick = onPostClick) {
                Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Comment")
            }
            IconButton(onClick = onShare) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Share")
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /* Save */ }) {
                Icon(Icons.Default.BookmarkBorder, contentDescription = "Save")
            }
        }

        // Post Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = "${(10..100).random()} likes",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = inscription.title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = inscription.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }
            Text(
                text = "View all comments",
                style = MaterialTheme.typography.bodySmall,
                color = IgSecondaryText,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
