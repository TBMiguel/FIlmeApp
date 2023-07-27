package br.pucpr.filmesapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.filmesapp.databinding.FilmeItemBinding
import br.pucpr.filmesapp.model.Filme

class FilmesAdapter(var filmes: MutableList<Filme>):
    RecyclerView.Adapter<FilmesAdapter.FilmeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        FilmeItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false).apply {
            return FilmeViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        // exibe itens criados no onCreate
        filmes[position].apply {
            holder.binding.tituloFilme.text = this.titulo
            holder.binding.categoriaFilme.text = this.categoria
            holder.binding.avaliacaoFilme.text = this.avaliacao.toString()
        }
    }

    override fun getItemCount() = filmes.size

    inner class FilmeViewHolder(var binding: FilmeItemBinding): RecyclerView.ViewHolder(binding.root) {
        var tituloFilme = binding.tituloFilme
        var categoriaFilme = binding.categoriaFilme
        var avaliacaoFilme = binding.avaliacaoFilme
    }
}