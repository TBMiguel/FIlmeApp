package br.pucpr.filmesapp.controller

import DataStore
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.filmesapp.database.DatabaseApplication
import br.pucpr.filmesapp.database.dao.FilmesDao
import br.pucpr.filmesapp.databinding.ActivityMainBinding
import br.pucpr.filmesapp.model.Filme
import br.pucpr.filmesapp.view.FilmesAdapter
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var adapterFilmes: FilmesAdapter

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var gesture: GestureDetector

    private val getResultActivity =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                it.data?.let { intent ->
                    Snackbar.make(
                        this,
                        binding.layout,
                        "Filme ${intent.getStringExtra("filme")} adicionada com sucesso!",
                        Snackbar.LENGTH_LONG
                    ).show()

                    adapterFilmes.notifyDataSetChanged()
                    binding.quantidadeFilmes.text = DataStore.filmesCount().toString()
                }
            }
        }

    private val getResultActivityForEditarFilme =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                it.data?.let { intent ->
                    Snackbar.make(
                        this,
                        binding.layout,
                        "Filme ${intent.getStringExtra("filme")} alterado com sucesso!",
                        Snackbar.LENGTH_LONG
                    ).show()

                    adapterFilmes.notifyDataSetChanged()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        DataStore.setContext(this)

        binding.quantidadeFilmes.text = DataStore.filmesCount().toString()
        leadRecyclerView()
        configurarFloatActionButton()
        configureGesture()
        configureRecycleViewEvents()
    }


    private fun configureGesture() {

        gesture = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                binding.filmesRecyclerView.findChildViewUnder(e.x, e.y).run {
                    this?.let { child ->
                        binding.filmesRecyclerView.getChildAdapterPosition(child).apply {
                            Intent(this@MainActivity, EditarFilme::class.java).run {
                                putExtra("position", this@apply)
                                getResultActivityForEditarFilme.launch(this)
                            }
                        }
                    }
                }
                return super.onSingleTapConfirmed(e)
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                binding.filmesRecyclerView.findChildViewUnder(e.x, e.y).run {
                    this?.let { child ->
                        binding.filmesRecyclerView.getChildAdapterPosition(child).apply {
                            Intent(this@MainActivity, VisualizarFilme::class.java).run {
                                putExtra("position", this@apply)
                                getResultActivityForEditarFilme.launch(this)
                            }
                        }
                    }
                }
                return super.onDoubleTap(e)
            }

            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)

                binding.filmesRecyclerView.findChildViewUnder(e.x, e.y).run {
                    this?.let { child ->
                        binding.filmesRecyclerView.getChildAdapterPosition(child).apply {
                            val filme = DataStore.getFilmes(this)
                            AlertDialog.Builder(this@MainActivity).run {
                                setMessage("Tem certeza que deseja remover esta cidade?")
                                setPositiveButton("Excluir") { _, _ ->
                                    DataStore.removeFilme(this@apply)
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Filme ${filme.titulo} excluído com sucesso!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    binding.quantidadeFilmes.text = DataStore.filmesCount().toString()
                                    adapterFilmes.notifyDataSetChanged()
                                }
                                setNegativeButton("Cancelar", null)
                                show()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun configureRecycleViewEvents() {

        binding.filmesRecyclerView.addOnItemTouchListener(object: RecyclerView.OnItemTouchListener {

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                rv.findChildViewUnder(e.x, e.y).apply {
                    return (this != null && gesture.onTouchEvent(e))
                }
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
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
        Intent(this, AdicionarNovoFilme::class.java).run {
            getResultActivity.launch(this)
        }
    }
}