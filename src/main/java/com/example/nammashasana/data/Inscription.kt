package com.example.nammashasana.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inscriptions")
data class Inscription(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val translation: String, // Modern Kannada
    val englishTranslation: String, // Professional addition: English translation for tourism
    val explanation: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null,
    val era: String,
    val dynasty: String,
    val isDamageReported: Boolean = false,
    val damageNotes: String? = null,
    val isVisited: Boolean = false
)
