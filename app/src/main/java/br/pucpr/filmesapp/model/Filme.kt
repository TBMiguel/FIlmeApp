package br.pucpr.filmesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Filmes")
data class Filme(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
//    @ColumnInfo
    var titulo: String,
    var categoria: String,
    var descricao: String,
    var avaliacao: Int,
)