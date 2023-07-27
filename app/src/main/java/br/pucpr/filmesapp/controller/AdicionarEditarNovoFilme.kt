package br.pucpr.filmesapp.controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.pucpr.filmesapp.databinding.ActivityAdicionarEditarFilmeBinding
import br.pucpr.filmesapp.model.DataStore
import br.pucpr.filmesapp.model.Filme

class AdicionarEditarNovoFilme : AppCompatActivity() {

    private val binding : ActivityAdicionarEditarFilmeBinding by lazy {
        ActivityAdicionarEditarFilmeBinding.inflate(layoutInflater)
    }

    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.getIntExtra("filme", -1).apply {
            position = this
            if (position != -1) {
                setData(position)
            }
        }

        binding.botaoSalvar.setOnClickListener {

            getData()?.let { city ->
                saveCity(city)
            } ?: run {
                showMessage("Campos inválidos!")
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

        return Filme(titulo, categoria, descricao, avaliacao)
    }

    private fun verificaCamposNulos(titulo: String, categoria: String, descricao: String, avaliacao: Int) : Boolean {
        if (titulo.isEmpty() || categoria.isEmpty() || descricao.isEmpty() || avaliacao == 0) {
            return true
        }
        return false
    }

    private fun setData(position: Int) {

        DataStore.getFilme(position).run {
            binding.tituloFilme.setText(this.titulo)
            binding.categoriaFilme.setText(this.categoria)
            binding.descricaoFilme.setText(this.descricao)
            binding.avaliacaoFilme.setText(this.avaliacao.toString())
        }
    }

    private fun saveCity(filme: Filme) {

        if (position == -1)
            DataStore.adicionarFilme(filme)
        else
            DataStore.editarFilme(position, filme)
        Intent().run {
            putExtra("filme", filme.titulo)
            setResult(RESULT_OK, this)
        }
        finish()
    }

    fun showMessage(message: String) {

        AlertDialog.Builder(this).run {
            title = "Filmes App"
            setMessage(message)
            setPositiveButton("Confirmar", null)
            show()
        }
    }
}