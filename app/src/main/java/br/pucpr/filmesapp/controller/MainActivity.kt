package br.pucpr.filmesapp.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.pucpr.filmesapp.databinding.ActivityMainBinding
import br.pucpr.filmesapp.model.DataStore
import br.pucpr.filmesapp.model.Filme
import br.pucpr.filmesapp.view.FilmesAdapter
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var adapterFilmes: FilmesAdapter
    private val listaFilmes: MutableList<Filme> = mutableListOf()

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        DataStore.setContext(this)
        leadRecyclerView()
        configurarFloatActionButton()
    }

    private fun leadRecyclerView() {
        LinearLayoutManager(this).apply {
            this.orientation = LinearLayoutManager.HORIZONTAL
            binding.filmesRecyclerView.layoutManager = this
            adapterFilmes = FilmesAdapter(DataStore.filmes).apply {
                binding.filmesRecyclerView.adapter = this
            }
        }
    }

    private fun configurarFloatActionButton() {
        binding.inputButton.setOnClickListener {
            adicionarNovoFilme()
        }
    }

    private fun adicionarNovoFilme() {

        Intent(this, AdicionarEditarNovoFilme::class.java).run {
            addFilmeForResult.launch(this)
        }
    }

    private val addFilmeForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { intent ->
                Snackbar.make(
                    this,
                    binding.layout,
                    "Filme ${intent.getStringExtra("filme")} adicionada com sucesso!",
                    Snackbar.LENGTH_LONG
                ).show()
                adapterFilmes.notifyDataSetChanged()
            }
        }
    }

    private fun filmesDaLista() {
        val filme1 = Filme("Harry Potter", "Aventura","descrição",3)
        val filme2 = Filme("Velosos e Furioso", "Ação", "descrição",5)
        val filme3 = Filme("SpiderMan", "Fantasia", "descrição",5)

        listaFilmes.add(filme1)
        listaFilmes.add(filme2)
        listaFilmes.add(filme3)
    }
}