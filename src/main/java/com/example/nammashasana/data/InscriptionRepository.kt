package com.example.nammashasana.data

import kotlinx.coroutines.flow.Flow

class InscriptionRepository(private val inscriptionDao: InscriptionDao) {
    val allInscriptions: Flow<List<Inscription>> = inscriptionDao.getAllInscriptions()

    fun getInscriptionById(id: Long): Flow<Inscription?> = inscriptionDao.getInscriptionById(id)

    suspend fun insert(inscription: Inscription) = inscriptionDao.insertInscription(inscription)

    suspend fun update(inscription: Inscription) = inscriptionDao.updateInscription(inscription)

    suspend fun reportDamage(id: Long, notes: String) = inscriptionDao.reportDamage(id, notes)

    suspend fun setVisited(id: Long, visited: Boolean) = inscriptionDao.setVisited(id, visited)
}
