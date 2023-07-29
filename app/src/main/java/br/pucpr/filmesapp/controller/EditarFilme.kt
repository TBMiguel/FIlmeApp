package br.pucpr.filmesapp.controller

import DataStore
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.pucpr.filmesapp.database.DatabaseApplication
import br.pucpr.filmesapp.database.dao.FilmesDao
import br.pucpr.filmesapp.databinding.ActivityEditarFilmeBinding
import br.pucpr.filmesapp.model.Filme

class EditarFilme : AppCompatActivity() {

    private var position = -1
    private var id: Long = 0

    private val binding : ActivityEditarFilmeBinding by lazy {
        ActivityEditarFilmeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.getIntExtra("position", -1).apply {
            position = this

            if (position != -1) {
                setData(position)
            }
        }

        buttonsListenter()
    }

    private fun buttonsListenter() {
        binding.botaoSalvar.setOnClickListener {
            getData()?.let { filme ->
                saveFilme(filme)
            } ?: run {
                showMessage("Os campos informados estão vazios ou inválidos!")
            }
        }

        binding.botaoCancelar.setOnClickListener {
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

        return Filme(id, titulo = titulo, categoria = categoria, descricao = descricao, avaliacao = avaliacao)
    }

    private fun verificaCamposNulos(titulo: String, categoria: String, descricao: String, avaliacao: Int) : Boolean {
        if (titulo.isEmpty() || categoria.isEmpty() || descricao.isEmpty() || avaliacao == 0) {
            return true
        }
        return false
    }

    private fun setData(position: Int) {
        DataStore.getFilmes(position).run {
            this@EditarFilme.id = this.id
            binding.tituloFilme.setText(this.titulo)
            binding.categoriaFilme.setText(this.categoria)
            binding.avaliacaoFilme.setText(this.avaliacao.toString())
            binding.descricaoFilme.setText(this.descricao)
        }
    }

    private fun saveFilme(filme: Filme) {
        DataStore.editFilme(position, filme)
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