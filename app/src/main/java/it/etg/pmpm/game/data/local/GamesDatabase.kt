package it.etg.pmpm.game.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GamesEntity::class], version = 1)
abstract class GamesDatabase: RoomDatabase() {
    abstract fun gamesDao(): GamesDao
}