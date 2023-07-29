import android.content.Context
import android.util.Log
import br.pucpr.filmesapp.database.DatabaseApplication
import br.pucpr.filmesapp.database.dao.FilmesDao
import br.pucpr.filmesapp.model.Filme

object DataStore {

    var filmes: MutableList<Filme> = arrayListOf()
        private set

    private lateinit var dao : FilmesDao

    fun setContext(context: Context) {
        dao  = DatabaseApplication.getInstance(context).filmesDao()
        dao?.let {
            filmes = it.getFilmes().toMutableList()
        }
    }

    fun getFilmes(position: Int): Filme {
        return filmes[position]
    }

    fun addFilme(filme: Filme) {

        dao?.insertFilmes(filme).let {
            filmes.add(filme)
        }

        Log.d("Filmes App", "Operação falhou: inserção")
    }

    fun editFilme(position: Int, filme: Filme) {
        dao?.updateFilmes(filme).let {
            filmes[position] = filme
        }

        Log.d("Filmes App", "Operação falhou: update")
    }

    fun removeFilme(position: Int) {
        dao.deleteFilmes(getFilmes(position)).let {
            filmes.removeAt(position)
        }
    }

    fun filmesCount() : Int {
        return filmes.size
    }
}