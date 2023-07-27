package br.pucpr.filmesapp.model

data class Filme(
    var titulo: String,
    var categoria: String,
    var descricao: String,
    var avaliacao: Int,
) {
    var id: Long = -1
    constructor(): this("", "", "", 0)
    constructor(titulo: String, categoria: String, descricao: String) : this(titulo, categoria, descricao, 0)
}