package com.example.nammashasana.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.nammashasana.data.Inscription
import com.example.nammashasana.ui.theme.HeritageRed
import com.example.nammashasana.ui.theme.StoneDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInscriptionScreen(
    viewModel: ShasanaViewModel,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var translation by remember { mutableStateOf("") }
    var englishTranslation by remember { mutableStateOf("") }
    var explanation by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf("") }
    var lng by remember { mutableStateOf("") }
    var era by remember { mutableStateOf("") }
    var dynasty by remember { mutableStateOf("") }
    
    var isScanning by remember { mutableStateOf(false) }
    var hasPhoto by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tag New Inscription", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Photo Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
                    .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (!hasPhoto) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = { hasPhoto = true }) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(48.dp))
                        }
                        Text("Tap to take a photo of the stone", style = MaterialTheme.typography.labelMedium)
                    }
                } else {
                    // Mocked Image
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1599341671302-3f1df2156294?q=80&w=1000",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    
                    if (isScanning) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                            color = HeritageRed
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("GenAI Decoding...", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            AnimatedVisibility(visible = hasPhoto && !isScanning && translation.isEmpty()) {
                Button(
                    onClick = {
                        scope.launch {
                            isScanning = true
                            delay(2000) // Mock AI Processing
                            title = "New Discovery near Temple"
                            dynasty = "Unknown (Analysis suggests Hoysala style)"
                            era = "12th Century"
                            translation = "ಧರ್ಮಾರ್ಥವಾಗಿ ನೀಡಿದ ಭೂಮಿ... (AI detected Old Kannada characters)"
                            englishTranslation = "Land donated for religious purposes... (Decoded from Halegannada)"
                            explanation = "This stone appears to be a land grant. The script matches the Halegannada transitions found in the south."
                            isScanning = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = StoneDark)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("GenAI Decode Inscription")
                }
            }

            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = dynasty, onValueChange = { dynasty = it }, label = { Text("Dynasty") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = era, onValueChange = { era = it }, label = { Text("Era") }, modifier = Modifier.fillMaxWidth())
            
            OutlinedTextField(
                value = translation, 
                onValueChange = { translation = it }, 
                label = { Text("Modern Kannada Translation") }, 
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = englishTranslation, 
                onValueChange = { englishTranslation = it }, 
                label = { Text("English Translation") }, 
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            OutlinedTextField(
                value = explanation, 
                onValueChange = { explanation = it }, 
                label = { Text("Significance") }, 
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = lat, onValueChange = { lat = it }, label = { Text("Latitude") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = lng, onValueChange = { lng = it }, label = { Text("Longitude") }, modifier = Modifier.weight(1f))
            }

            Button(
                onClick = {
                    val inscription = Inscription(
                        title = title,
                        description = "User discovered inscription",
                        translation = translation,
                        englishTranslation = englishTranslation,
                        explanation = explanation,
                        latitude = lat.toDoubleOrNull() ?: 12.9716,
                        longitude = lng.toDoubleOrNull() ?: 77.5946,
                        era = era,
                        dynasty = dynasty
                    )
                    viewModel.addInscription(inscription)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && hasPhoto,
                colors = ButtonDefaults.buttonColors(containerColor = HeritageRed)
            ) {
                Text("Save to National Archive")
            }
        }
    }
}
