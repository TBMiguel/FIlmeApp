package br.pucpr.filmesapp.database.dao

import androidx.room.*
import br.pucpr.filmesapp.model.Filme

@Dao
interface FilmesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilmes(filme: Filme)

    @Delete
    fun deleteFilmes(filme: Filme)

    @Update
    fun updateFilmes(filme: Filme)

    @Query("SELECT * FROM filmes WHERE id = :id")
    fun loadFilmeById(id: Long): Filme

    @Query("SELECT * FROM Filmes")
    fun getFilmes() : List<Filme>
}