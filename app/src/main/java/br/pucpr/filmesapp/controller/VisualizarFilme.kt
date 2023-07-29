package br.pucpr.filmesapp.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.pucpr.filmesapp.databinding.ActivityEditarFilmeBinding
import br.pucpr.filmesapp.databinding.ActivityVisualizarFilmeBinding

class VisualizarFilme : AppCompatActivity() {

    private var position = -1
    private var id: Long = 0

    private val binding : ActivityVisualizarFilmeBinding by lazy {
        ActivityVisualizarFilmeBinding.inflate(layoutInflater)
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
        binding.botaoVoltar.setOnClickListener {
            finish()
        }
    }

    private fun setData(position: Int) {
        DataStore.getFilmes(position).run {
            this@VisualizarFilme.id = this.id
            binding.tituloFilme.setText(this.titulo)
            binding.categoriaFilme.setText(this.categoria)
            binding.avaliacaoFilme.setText(this.avaliacao.toString())
            binding.descricaoFilme.setText(this.descricao.uppercase())
        }
    }
}