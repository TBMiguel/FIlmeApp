package br.pucpr.filmesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filmes")
data class Filme(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    var titulo: String,
    var categoria: String,
    var descricao: String,
    var avaliacao: Int,
)