package br.pucpr.filmesapp.model

import android.content.Context
import android.util.Log

object DataStore {

    var filmes: MutableList<Filme> = arrayListOf()
        private set
    private var database: Database? = null

    fun setContext(context: Context) {
        database = Database(context)
        database?.let {
            filmes = it.getAllFilmes()
        }
    }

    // Métodos auxiliares para CRUD dos filmes
    fun getFilme(position: Int): Filme {
        return filmes[position]
    }

    fun adicionarFilme(filme: Filme) {
        val id = database?.addFilme(filme) ?: return

        if (id > 0) {
            filme.id = id
            filmes.add(filme)
        }
        else {
            Log.d("Filmes App", "Operação falhou ao criar filme.")
        }
    }

    fun editarFilme(position: Int, filme: Filme) {
        filme.id = getFilme(position).id
        val count = database?.editFilme(filme) ?: return

        if (count > 0) {
            filmes[position] = filme
        }
        else {
            Log.d("Filmes App", "Operação falhou ao atualizar filme.")
        }
    }

    fun removerFilme(position: Int) {
        val count = database?.removeFilme(getFilme(position)) ?: return

        if (count > 0) {
            filmes.removeAt(position)
        }
        else {
            Log.d("Filmes App", "Operação falhou ao remover filme.")
        }
    }
}