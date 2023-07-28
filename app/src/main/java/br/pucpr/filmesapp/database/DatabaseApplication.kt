package br.pucpr.filmesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.pucpr.filmesapp.database.dao.FilmesDao
import br.pucpr.filmesapp.model.Filme

@Database(
    entities = [
        Filme::class
    ], version = 1, exportSchema = false
)
abstract class DatabaseApplication() : RoomDatabase() {

    abstract fun filmesDao() : FilmesDao

    companion object {
        private var INSTANCE : DatabaseApplication? = null

        @Synchronized
        fun getInstance(context : Context) : DatabaseApplication {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseApplication::class.java,
                    "filmes.db").allowMainThreadQueries().build() //allowMainThreadQueries rodar banco na mesma thread
            }
            return INSTANCE as DatabaseApplication
        }
    }
}

