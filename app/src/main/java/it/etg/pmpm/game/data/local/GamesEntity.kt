package it.etg.pmpm.game.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GamesEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val genre: String,
    val platform: String
)
