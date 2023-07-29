package br.pucpr.filmesapp.controller

import DataStore
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.pucpr.filmesapp.database.DatabaseApplication
import br.pucpr.filmesapp.database.dao.FilmesDao
import br.pucpr.filmesapp.databinding.ActivityAdicionarFilmeBinding
import br.pucpr.filmesapp.model.Filme

class AdicionarNovoFilme : AppCompatActivity() {

    private val binding : ActivityAdicionarFilmeBinding by lazy {
        ActivityAdicionarFilmeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.botaoSalvar.setOnClickListener {
            getData()?.let { filme ->
                saveFilme(filme)
            } ?: run {
                showMessage("Os campos informados estão vazios ou inválidos!")
            }
        }

        binding.botaoCancelar.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun getData(): Filme? {
        val titulo = binding.tituloFilme.text.toString()
        val categoria = binding.categoriaFilme.text.toString()
        val descricao = binding.descricaoFilme.text.toString()
        val avaliacao = binding.avaliacaoFilme.text.toString().toIntOrNull() ?: 0

        if (verificaCamposNulos(titulo, categoria, descricao, avaliacao))
            return null

        return Filme(0, titulo = titulo, categoria = categoria, descricao = descricao, avaliacao = avaliacao)
    }

    private fun verificaCamposNulos(titulo: String, categoria: String, descricao: String, avaliacao: Int) : Boolean {
        if (titulo.isEmpty() || categoria.isEmpty() || descricao.isEmpty() || avaliacao == 0) {
            return true
        }
        return false
    }

    private fun saveFilme(filme: Filme) {
        DataStore.addFilme(filme)
        Intent().run {
            putExtra("filme", filme.titulo)
            setResult(RESULT_OK, this)
        }
        finish()
    }

    fun showMessage(message: String) {
        AlertDialog.Builder(this).run {
            title = "Filmes App"
            setIcon(resources.getDrawable(android.R.drawable.ic_dialog_alert, theme))
            setTitle(title)
            setMessage(message)
            setPositiveButton("Confirmar", null)
            show()
        }
    }
}