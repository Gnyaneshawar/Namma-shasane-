package com.example.nammashasana.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface InscriptionDao {
    @Query("SELECT * FROM inscriptions")
    fun getAllInscriptions(): Flow<List<Inscription>>

    @Query("SELECT * FROM inscriptions WHERE id = :id")
    fun getInscriptionById(id: Long): Flow<Inscription?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInscription(inscription: Inscription)

    @Update
    suspend fun updateInscription(inscription: Inscription)

    @Delete
    suspend fun deleteInscription(inscription: Inscription)

    @Query("UPDATE inscriptions SET isDamageReported = 1, damageNotes = :notes WHERE id = :id")
    suspend fun reportDamage(id: Long, notes: String)

    @Query("UPDATE inscriptions SET isVisited = :visited WHERE id = :id")
    suspend fun setVisited(id: Long, visited: Boolean)
}
