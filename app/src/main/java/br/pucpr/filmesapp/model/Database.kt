package br.pucpr.filmesapp.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class Database (context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "filmes.db"
        const val DB_VERSION = 1

        const val DB_TABLE_NAME = "filmes"
        const val DB_FIELD_ID = "id"
        const val DB_FIELD_TITULO = "titulo"
        const val DB_FIELD_CATEGORIA = "categoria"
        const val DB_FIELD_AVALIACAO = "avaliacao"
        const val DB_FIELD_DESCRICAO = "descricao"

        const val SqlCreateFilmes = "CREATE TABLE IF NOT EXISTS $DB_TABLE_NAME (" +
                "$DB_FIELD_ID INTEGER PRIMARY KEY, " +
                "$DB_FIELD_TITULO TEXT, " +
                "$DB_FIELD_CATEGORIA TEXT, " +
                "$DB_FIELD_AVALIACAO INTEGER, " +
                "$DB_FIELD_DESCRICAO LONGTEXT);"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val db = db?: return

        db.beginTransaction()
        try {
            db.execSQL(SqlCreateFilmes);
            db.setTransactionSuccessful();
        } catch (e: Exception) {
            Log.d("Filmes: Error - ", e.localizedMessage)
        }

        finally {
            db.endTransaction()
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        super.onDowngrade(db, oldVersion, newVersion)
    }

    fun getAllFilmes() : MutableList <Filme> {
        var filmes = mutableListOf<Filme>()
        val db = readableDatabase
        val cursor = db.query(DB_TABLE_NAME, null, null, null, null, null, DB_NAME)

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(DB_FIELD_ID))
                val titulo = getString(getColumnIndexOrThrow(DB_FIELD_TITULO))
                val categoria = getString(getColumnIndexOrThrow(DB_FIELD_CATEGORIA))
                val avaliacao = getInt(getColumnIndexOrThrow(DB_FIELD_AVALIACAO))
                val descricao = getString(getColumnIndexOrThrow(DB_FIELD_DESCRICAO))
                val filme = Filme(titulo, categoria, descricao, avaliacao)
                filme.id = id
                filmes.add(filme)
            }
        }

        return filmes
    }

    fun addFilme(filme: Filme) : Long {
        var id: Long = 0
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DB_FIELD_TITULO, filme.titulo)
            put(DB_FIELD_CATEGORIA, filme.categoria)
            put(DB_FIELD_AVALIACAO, filme.avaliacao)
            put(DB_FIELD_DESCRICAO, filme.descricao)
        }

        db.beginTransaction()
        try {
            id = db.insert(DB_TABLE_NAME, null, values)
            db.setTransactionSuccessful()
        }

        finally {
            db.endTransaction()
        }

        return id
    }

    fun editFilme(filme: Filme) : Int {
        var contador = 0
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DB_FIELD_TITULO, filme.titulo)
            put(DB_FIELD_CATEGORIA, filme.categoria)
            put(DB_FIELD_AVALIACAO, filme.avaliacao)
            put(DB_FIELD_DESCRICAO, filme.descricao)
        }

        val selection = "$DB_FIELD_ID = ?"
        val selectionArgs = arrayOf(filme.id.toString())
        db.beginTransaction()

        try {
            contador = db.update(DB_TABLE_NAME, values, selection, selectionArgs)
            db.setTransactionSuccessful()
        }
        finally {
            db.endTransaction()
        }

        return contador
    }

    fun removeFilme(filme: Filme) : Int {
        var contador = 0
        val db = writableDatabase

        val selection = "$DB_FIELD_ID = ?"
        val selectionArgs = arrayOf(filme.id.toString())
        db.beginTransaction()

        try {
            contador = db.delete(DB_TABLE_NAME, selection, selectionArgs)
            db.setTransactionSuccessful()
        }

        finally {
            db.endTransaction()
        }

        return contador
    }
}