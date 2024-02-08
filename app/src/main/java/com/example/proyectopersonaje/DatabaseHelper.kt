package com.example.proyectopersonaje

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "personajes.db"
        private const val TABLE_PERSONAJE = "personaje"
        private const val TABLE_MOCHILA = "mochila"
        private const val TABLE_ARTICULOS = "articulos"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_SALUD = "salud"
        private const val COLUMN_ATAQUE = "ataque"
        private const val COLUMN_EXPERIENCIA = "experiencia"
        private const val COLUMN_NIVEL = "nivel"
        private const val COLUMN_SUERTE = "suerte"
        private const val COLUMN_DEFENSA = "defensa"
        private const val COLUMN_ARTICULO_ID = "articulo_id"
        private const val COLUMN_ARTICULO_NOMBRE = "nombre"
        private const val COLUMN_ARTICULO_TIPO = "tipo"
        private const val COLUMN_ARTICULO_PESO = "peso"
        private const val COLUMN_ID_PERSONAJE = "id_personaje"
        private const val COLUMN_ID_ARTICULO = "id_articulo"

        private const val CREATE_TABLE_PERSONAJE =
            "CREATE TABLE $TABLE_PERSONAJE (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_NOMBRE TEXT," +
                    "$COLUMN_SALUD INTEGER," +
                    "$COLUMN_ATAQUE INTEGER," +
                    "$COLUMN_EXPERIENCIA INTEGER," +
                    "$COLUMN_NIVEL INTEGER," +
                    "$COLUMN_SUERTE INTEGER," +
                    "$COLUMN_DEFENSA INTEGER)"

        private const val CREATE_TABLE_MOCHILA =
            "CREATE TABLE $TABLE_MOCHILA (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_ID_PERSONAJE INTEGER," +
                    "$COLUMN_ID_ARTICULO INTEGER," +
                    "FOREIGN KEY($COLUMN_ID_PERSONAJE) REFERENCES $TABLE_PERSONAJE($COLUMN_ID)," +
                    "FOREIGN KEY($COLUMN_ID_ARTICULO) REFERENCES $TABLE_ARTICULOS($COLUMN_ID))"


        private const val CREATE_TABLE_ARTICULOS =
            "CREATE TABLE $TABLE_ARTICULOS (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_ARTICULO_NOMBRE TEXT," +
                    "$COLUMN_ARTICULO_TIPO TEXT," +
                    "$COLUMN_ARTICULO_PESO INTEGER)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_PERSONAJE)
        db.execSQL(CREATE_TABLE_MOCHILA)
        db.execSQL(CREATE_TABLE_ARTICULOS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PERSONAJE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOCHILA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ARTICULOS")
        onCreate(db)
    }

    fun insertarPersonaje(personaje: Personaje) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, personaje.getNombre())
            put(COLUMN_SALUD, personaje.getSalud())
            put(COLUMN_ATAQUE, personaje.getAtaque())
            put(COLUMN_EXPERIENCIA, personaje.getExperiencia())
            put(COLUMN_NIVEL, personaje.getNivel())
            put(COLUMN_SUERTE, personaje.getSalud())
            put(COLUMN_DEFENSA, personaje.getSalud())
        }
        db.insert(TABLE_PERSONAJE, null, values)
        db.close()
    }
    fun contienePersonaje(): Boolean {
        val db = readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_PERSONAJE"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count > 0
    }
    fun actualizarPersonaje(personaje: Personaje) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, personaje.getNombre())
            put(COLUMN_SALUD, personaje.getSalud())
            put(COLUMN_ATAQUE, personaje.getAtaque())
            put(COLUMN_EXPERIENCIA, personaje.getExperiencia())
            put(COLUMN_NIVEL, personaje.getNivel())
            put(COLUMN_SUERTE, personaje.getSalud())
            put(COLUMN_DEFENSA, personaje.getSalud())
        }
        db.update(TABLE_PERSONAJE, values, "$COLUMN_NOMBRE=?", arrayOf(personaje.getNombre()))
        db.close()
    }



    fun insertarArticulo(articulo: Articulo) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ARTICULO_NOMBRE, articulo.getNombre().name)
            put(COLUMN_ARTICULO_TIPO, articulo.getTipoArticulo().name)
            put(COLUMN_ARTICULO_PESO, articulo.getPeso())
        }
        db.insert(TABLE_ARTICULOS, null, values)
        db.close()
    }

    fun insertarEnMochila(idPersonaje: Long, idArticulo: Long) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID_PERSONAJE, idPersonaje)
            put(COLUMN_ID_ARTICULO, idArticulo)
        }
        db.insert(TABLE_MOCHILA, null, values)
        db.close()
    }
}
