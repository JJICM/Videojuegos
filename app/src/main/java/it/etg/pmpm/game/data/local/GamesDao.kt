package it.etg.pmpm.game.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GamesDao {
    @Query("SELECT id, title, genre, platform FROM games")
    suspend fun getAll(): List<GamesEntity>

    @Query("SELECT id, title, genre, platform FROM games WHERE id= :id")
    suspend fun getById(id: Int): GamesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(games: GamesEntity)
}