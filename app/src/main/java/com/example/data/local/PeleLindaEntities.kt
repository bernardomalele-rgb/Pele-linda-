package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "primary_user",
    val nome: String = "",
    val idade: Int = 22,
    val telemovel: String = "",
    val isVerified: Boolean = false,
    val termosAceites: Boolean = false,
    val tipoPele: String = "Mista",
    val exposicaoSol: String = "Moderada",
    val qualidadeSono: String = "6 a 8h",
    val consumoAgua: String = "1L a 2L",
    val produtosAtuais: String = "Sabonete facial simples",
    val dataRegisto: Long = System.currentTimeMillis(),
    val diasAtivosExpiracao: Long = System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000),
    val estadoConta: String = "EM_TESTE",
    val isBlocked: Boolean = false,
    val emailNotificado: Boolean = false
)

@Entity(tableName = "routine_history")
data class RoutineHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateString: String, // e.g. "02/11/2026"
    val timestamp: Long = System.currentTimeMillis(),
    val taskId: String,
    val taskTitle: String,
    val period: String, // "MANHA" ou "NOITE"
    val stepNumber: Int,
    val completed: Boolean = true
)

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipeEntity(
    @PrimaryKey val recipeId: String,
    val title: String,
    val subtitle: String,
    val budgetRange: String,
    val estimatedCostKz: Int,
    val frequency: String,
    val recommendedSkinType: String,
    val mainBenefit: String,
    val addedTimestamp: Long = System.currentTimeMillis()
)
