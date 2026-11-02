package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 'primary_user' LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 'primary_user' LIMIT 1")
    suspend fun getUserProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("DELETE FROM user_profile")
    suspend fun clearProfile()
}

@Dao
interface RoutineHistoryDao {
    @Query("SELECT * FROM routine_history ORDER BY timestamp DESC")
    fun getAllRoutineHistoryFlow(): Flow<List<RoutineHistoryEntity>>

    @Query("SELECT * FROM routine_history WHERE dateString = :dateString")
    fun getHistoryForDateFlow(dateString: String): Flow<List<RoutineHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(item: RoutineHistoryEntity)

    @Query("DELETE FROM routine_history WHERE dateString = :dateString AND taskId = :taskId")
    suspend fun deleteHistoryItem(dateString: String, taskId: String)

    @Query("DELETE FROM routine_history")
    suspend fun clearAllHistory()
}

@Dao
interface FavoriteRecipeDao {
    @Query("SELECT * FROM favorite_recipes ORDER BY addedTimestamp DESC")
    fun getAllFavoritesFlow(): Flow<List<FavoriteRecipeEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE recipeId = :recipeId)")
    fun isFavoriteFlow(recipeId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE recipeId = :recipeId)")
    suspend fun isFavorite(recipeId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteRecipeEntity)

    @Query("DELETE FROM favorite_recipes WHERE recipeId = :recipeId")
    suspend fun removeFavorite(recipeId: String)

    @Query("DELETE FROM favorite_recipes")
    suspend fun clearAllFavorites()
}
