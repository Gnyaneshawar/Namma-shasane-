package com.example.nammashasana.ui

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammashasana.data.AppDatabase
import com.example.nammashasana.data.Inscription
import com.example.nammashasana.data.InscriptionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShasanaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: InscriptionRepository

    val allInscriptions: StateFlow<List<Inscription>>

    init {
        val dao = AppDatabase.getDatabase(application).inscriptionDao()
        repository = InscriptionRepository(dao)
        allInscriptions = repository.allInscriptions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
        viewModelScope.launch {
            // Check if we need to seed or update existing null images
            val currentList = repository.allInscriptions.first()
            if (currentList.isEmpty() || currentList.any { it.imageUrl == null }) {
                seedData()
            }
        }
    }

    private suspend fun seedData() {
        val initialData = listOf(
            Inscription(
                id = 1,
                title = "Halmidi Inscription",
                description = "The oldest known Kannada inscription found in Halmidi village.",
                translation = "ಹಲ್ಮಿಡಿ ಶಾಸನ: ಇದು ಕನ್ನಡದ ಅತ್ಯಂತ ಹಳೆಯ ಶಾಸನವಾಗಿದೆ. ಇದು ಕದಂಬ ವಂಶದ ಕಾಕುಸ್ಥವರ್ಮನ ಕಾಲದ್ದೆಂದು ಗುರುತಿಸಲಾಗಿದೆ.",
                englishTranslation = "Halmidi Inscription: This is the oldest known Kannada inscription, dating back to the period of Kakusthavarma of the Kadamba dynasty (450 CE).",
                explanation = "Records a land grant to a warrior named Vijaya Arasa. It signifies the early use of Kannada language in administration and its evolution from Sanskrit.",
                latitude = 13.2044,
                longitude = 75.8953,
                imageUrl = "https://images.unsplash.com/photo-1620619767323-b95a89183081?q=80&w=1000",
                era = "450 CE",
                dynasty = "Kadamba"
            ),
            Inscription(
                id = 2,
                title = "Badami Cliff Inscription",
                description = "The famous Kappe Arabhatta inscription on the cliffs of Badami.",
                translation = "ಕಪ್ಪೆ ಅರಭಟ್ಟನ ಶಾಸನ: ಸಾಧುಗೆ ಸಾಧು ಮಾಧುರ್ಯಂಗೆ ಮಾಧುರ್ಯಂ ಬಾದಿಪ್ಪ ಕಲಿಗೆ ವಿಪರೀತನ್ ಇವನಾರನ್ ಅರಿಪಲು ಸಾಧ್ಯ?",
                englishTranslation = "Kappe Arabhatta Inscription: Gentle to the gentle, sweet to the sweet, a paradox to the wicked - who can know this man?",
                explanation = "Famous for its poetic quality and use of the 'Tripadi' meter. It describes the character of Kappe Arabhatta, a local hero and saint-warrior.",
                latitude = 15.9189,
                longitude = 75.6794,
                imageUrl = "https://images.unsplash.com/photo-1600661653561-629509216228?q=80&w=1000",
                era = "700 CE",
                dynasty = "Chalukya"
            ),
            Inscription(
                id = 3,
                title = "Belur Inscription",
                description = "Hoysala architectural record at the Chennakesava Temple.",
                translation = "ಬೇಲೂರು ಚೆನ್ನಕೇಶವ ದೇವಸ್ಥಾನದ ಶಾಸನ: ಇದು ಹೊಯ್ಸಳ ವಿಷ್ಣುವರ್ಧನನ ವಿಜಯದ ಸಂಕೇತವಾಗಿ ನಿರ್ಮಿಸಲಾದ ದೇಗುಲದ ಬಗ್ಗೆ ವಿವರಿಸುತ್ತದೆ.",
                englishTranslation = "Belur Inscription: This records the construction of the Chennakesava temple as a symbol of Hoysala Vishnuvardhana's victory over the Cholas.",
                explanation = "Detailed inscriptions describe the master craftsmen, the cost of construction, and the religious philosophy of the Hoysala Empire.",
                latitude = 13.1624,
                longitude = 75.8596,
                imageUrl = "https://images.unsplash.com/photo-1548013146-72479768bbaa?q=80&w=1000",
                era = "1117 CE",
                dynasty = "Hoysala"
            ),
            Inscription(
                id = 4,
                title = "Atakur Inscription",
                description = "A unique Hero stone (Viragal) dedicated to a brave dog.",
                translation = "ಅತಕೂರು ಶಾಸನ: ಇದು ಕಾಳಿಂಗ ಎಂಬ ನಾಯಿಯ ಶೌರ್ಯದ ನೆನಪಿಗಾಗಿ ಹಾಕಲಾದ ಶಾಸನ. ಇದು ತನ್ನ ಧಣಿಗಾಗಿ ಹೋರಾಡಿ ಮಡಿದ ನಾಯಿಯ ಬಗ್ಗೆ ತಿಳಿಸುತ್ತದೆ.",
                englishTranslation = "Atakur Inscription: This is a unique memorial set up in memory of the bravery of a dog named Kalinga who died fighting a wild boar.",
                explanation = "Shows the cultural values of loyalty and the practice of setting up 'Viragals' even for animals that showed extraordinary courage.",
                latitude = 12.5684,
                longitude = 77.0347,
                imageUrl = "https://images.unsplash.com/photo-1599341671302-3f1df2156294?q=80&w=1000",
                era = "949 CE",
                dynasty = "Ganga"
            ),
            Inscription(
                id = 5,
                title = "Talagunda Inscription",
                description = "Oldest Sanskrit inscription of Karnataka on a stone pillar.",
                translation = "ತಾಳಗುಂದ ಶಾಸನ: ಇದು ಕದಂಬರ ಮೂಲದ ಬಗ್ಗೆ ಮತ್ತು ಅವರ ಶೈಕ್ಷಣಿಕ ಕೇಂದ್ರಗಳ ಬಗ್ಗೆ ತಿಳಿಸುತ್ತದೆ. ಪ್ರಣವೇಶ್ವರ ದೇವಸ್ಥಾನದ ಆವರಣದಲ್ಲಿದೆ.",
                englishTranslation = "Talagunda Inscription: This pillar inscription provides information about the origins of the Kadambas and their famous educational center (Ghatika).",
                explanation = "Written in poetic Sanskrit, it explains how Mayurasharma founded the dynasty after being insulted at a university in Kanchi.",
                latitude = 14.4214,
                longitude = 75.2536,
                imageUrl = "https://images.unsplash.com/photo-1582510003544-4d00b7f74220?q=80&w=1000",
                era = "450 CE",
                dynasty = "Kadamba"
            ),
            Inscription(
                id = 6,
                title = "Aihole Inscription",
                description = "The Meguti Temple inscription recording the glory of Pulakeshin II.",
                translation = "ಐಹೊಳೆ ಶಾಸನ: ಇಮ್ಮಡಿ ಪುಲಿಕೇಶಿಯ ದಿಗ್ವಿಜಯಗಳ ಬಗ್ಗೆ ರವಿಕೀರ್ತಿ ಬರೆದ ಮಹಾಕಾವ್ಯ. ಇದು ಹರ್ಷವರ್ಧನನ ಸೋಲನ್ನು ಉಲ್ಲೇಖಿಸುತ್ತದೆ.",
                englishTranslation = "Aihole Inscription: An epic poem by Ravikirti documenting the military achievements of Pulakeshin II, including his victory over Emperor Harsha.",
                explanation = "A masterpiece of Sanskrit literature. It also mentions the date of the Mahabharata war and the Kalidasa/Bharavi poets.",
                latitude = 16.0189,
                longitude = 75.8828,
                imageUrl = "https://images.unsplash.com/photo-1621259021950-845244e59174?q=80&w=1000",
                era = "634 CE",
                dynasty = "Chalukya"
            )
        )
        // Using upsert (insert or update) to fix null image fields
        initialData.forEach { repository.insert(it) }
    }

    fun shareInscription(inscription: Inscription) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Discovering History: ${inscription.title}\n\nKannada: ${inscription.translation}\n\nEnglish: ${inscription.englishTranslation}\n\nExplore more on Namma-Shasana app!")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        getApplication<Application>().startActivity(shareIntent)
    }

    fun reportDamage(id: Long, notes: String) {
        viewModelScope.launch { repository.reportDamage(id, notes) }
    }

    fun addInscription(inscription: Inscription) {
        viewModelScope.launch { repository.insert(inscription) }
    }

    fun markAsVisited(id: Long, visited: Boolean) {
        viewModelScope.launch { repository.setVisited(id, visited) }
    }
}
