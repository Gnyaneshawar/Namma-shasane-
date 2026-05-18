package com.example.nammashasana.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.nammashasana.data.Inscription
import com.example.nammashasana.ui.theme.IgBackground
import com.example.nammashasana.ui.theme.IgBorder
import com.example.nammashasana.ui.theme.IgSecondaryText
import com.example.nammashasana.ui.theme.StoneDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassportScreen(
    viewModel: ShasanaViewModel,
    onInscriptionClick: (Long) -> Unit
) {
    val inscriptions by viewModel.allInscriptions.collectAsState()
    val visitedInscriptions = inscriptions.filter { it.isVisited }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("heritage_explorer_2024", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Achievements */ }) {
                        Icon(Icons.Default.Badge, contentDescription = "Achievements")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IgBackground)
            )
        },
        containerColor = IgBackground
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Profile Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .border(1.dp, IgBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.Gray)
                }

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStat(count = visitedInscriptions.size.toString(), label = "Found")
                    ProfileStat(count = (inscriptions.size - visitedInscriptions.size).toString(), label = "Remaining")
                    ProfileStat(count = "Lv. ${visitedInscriptions.size / 2 + 1}", label = "Rank")
                }
            }

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("Explorer of the Ancient", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Preserving Karnataka's history, one stone at a time. 🏛️", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedButton(
                    onClick = { /* Share */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("Share Passport")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TabRow(
                selectedTabIndex = 0,
                containerColor = IgBackground,
                contentColor = Color.Black,
                divider = { HorizontalDivider(thickness = 0.5.dp, color = IgBorder) }
            ) {
                Tab(selected = true, onClick = {}) {
                    Icon(Icons.Default.GridOn, contentDescription = null, modifier = Modifier.padding(12.dp))
                }
            }

            if (visitedInscriptions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Your journey begins with the first stone.", color = IgSecondaryText)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(1.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(visitedInscriptions) { inscription ->
                        PassportStampItem(
                            inscription = inscription,
                            onClick = { onInscriptionClick(inscription.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStat(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, fontSize = 12.sp, color = IgSecondaryText)
    }
}

@Composable
fun PassportStampItem(inscription: Inscription, onClick: () -> Unit) {
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(Color.LightGray.copy(alpha = 0.1f))
            .clickable(onClick = onClick),
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
            onLoading = { isLoading = true },
            onSuccess = { isLoading = false },
            onError = { isLoading = false }
        )
        
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
        }

        Box(
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.BottomEnd)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Text(
                inscription.dynasty.take(3).uppercase(),
                color = Color.White,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
