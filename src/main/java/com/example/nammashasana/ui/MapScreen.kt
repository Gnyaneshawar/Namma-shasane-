package com.example.nammashasana.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.nammashasana.ui.theme.HeritageRed
import com.example.nammashasana.ui.theme.StoneDark
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(
    viewModel: ShasanaViewModel,
    onInscriptionClick: (Long) -> Unit,
    onAddClick: () -> Unit
) {
    val context = LocalContext.current
    val inscriptions by viewModel.allInscriptions.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showTrail by remember { mutableStateOf(false) }
    
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasLocationPermission = isGranted }
    )

    val filteredInscriptions = remember(inscriptions, searchQuery) {
        if (searchQuery.isBlank()) inscriptions
        else inscriptions.filter { 
            it.title.contains(searchQuery, ignoreCase = true) || 
            it.dynasty.contains(searchQuery, ignoreCase = true) 
        }
    }

    val karnataka = LatLng(15.3173, 75.7139)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(karnataka, 7f)
    }

    Scaffold(
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = { 
                        if (!hasLocationPermission) launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    },
                    containerColor = if (hasLocationPermission) Color.White else Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "My Location")
                }
                
                FloatingActionButton(
                    onClick = { showTrail = !showTrail },
                    containerColor = if (showTrail) HeritageRed else StoneDark,
                    contentColor = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Default.HistoryEdu, contentDescription = "Heritage Trail")
                }
                
                FloatingActionButton(onClick = onAddClick) {
                    Icon(Icons.Default.Add, contentDescription = "Add Inscription")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
                uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
            ) {
                filteredInscriptions.forEach { inscription ->
                    Marker(
                        state = MarkerState(position = LatLng(inscription.latitude, inscription.longitude)),
                        title = inscription.title,
                        snippet = "${inscription.dynasty} | ${inscription.era}",
                        onClick = {
                            onInscriptionClick(inscription.id)
                            true
                        }
                    )
                }

                if (showTrail && inscriptions.isNotEmpty()) {
                    Polyline(
                        points = inscriptions.sortedBy { it.latitude }.map { LatLng(it.latitude, it.longitude) },
                        color = HeritageRed.copy(alpha = 0.6f),
                        width = 10f,
                        geodesic = true
                    )
                }
            }

            // Search Bar Overlay
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by Dynasty or Name...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )
            }
        }
    }
}
