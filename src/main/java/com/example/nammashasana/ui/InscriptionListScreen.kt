package com.example.nammashasana.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nammashasana.data.Inscription
import com.example.nammashasana.ui.theme.HeritageRed
import com.example.nammashasana.ui.theme.StoneDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InscriptionListScreen(
    viewModel: ShasanaViewModel,
    onInscriptionClick: (Long) -> Unit
) {
    val inscriptions by viewModel.allInscriptions.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Heritage Directory", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(inscriptions) { inscription ->
                InscriptionItem(
                    inscription = inscription,
                    onClick = { onInscriptionClick(inscription.id) }
                )
            }
        }
    }
}

@Composable
fun InscriptionItem(
    inscription: Inscription,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = inscription.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = StoneDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${inscription.dynasty} Dynasty • ${inscription.era}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Place,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = HeritageRed
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "View on Map",
                        style = MaterialTheme.typography.labelSmall,
                        color = HeritageRed
                    )
                }
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}
