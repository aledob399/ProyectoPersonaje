package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


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
        private const val COLUMN_RAZA = "raza"
        private const val COLUMN_CLASE = "clase"
        private const val COLUMN_ESTADOVITAL = "estadoVital"
        private const val COLUMN_ARTICULO_ID = "articulo_id"
        private const val COLUMN_ARTICULO_NOMBRE = "nombre"
        private const val COLUMN_ARTICULO_TIPO = "tipo"
        private const val COLUMN_ARTICULO_PESO = "peso"
        private const val COLUMN_ID_PERSONAJE = "id_personaje"
        private const val COLUMN_ID_ARTICULO = "id_articulo"
        private const val COLUMN_ID_USUARIO_AUTH = "idUsuarioAuth"

        private const val CREATE_TABLE_PERSONAJE =
            "CREATE TABLE $TABLE_PERSONAJE (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_ID_USUARIO_AUTH TEXT," +
                    "$COLUMN_NOMBRE TEXT," +
                    "$COLUMN_RAZA TEXT," +
                    "$COLUMN_CLASE TEXT," +
                    "$COLUMN_ESTADOVITAL TEXT)"


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

    @SuppressLint("Range")
    fun obtenerPersonaje(idUsuarioAuth: String): Personaje? {
        val db = this.readableDatabase
        var personaje: Personaje? = null

        val query = "SELECT * FROM $TABLE_PERSONAJE WHERE $COLUMN_ID_USUARIO_AUTH = ? ORDER BY $COLUMN_ID DESC LIMIT 1"

        val cursor = db.rawQuery(query, arrayOf(idUsuarioAuth))

        if (cursor.moveToFirst()) {
            val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))
            val raza = cursor.getString(cursor.getColumnIndex(COLUMN_RAZA))
            val clase = cursor.getString(cursor.getColumnIndex(COLUMN_CLASE))
            val estadoVital = cursor.getString(cursor.getColumnIndex(COLUMN_ESTADOVITAL))

            val razaFinal: Personaje.Raza = obtenerRazaEnum(raza)
            val estadoVitalFinal: Personaje.EstadoVital = obtenerEstadoVitalEnum(estadoVital)
            val claseFinal: Personaje.Clase = obtenerClaseEnum(clase)

            personaje = Personaje(nombre, razaFinal, claseFinal, estadoVitalFinal)
        }

        cursor.close()
        return personaje
    }

    // Métodos auxiliares para convertir String a Enum
    private fun obtenerRazaEnum(raza: String): Personaje.Raza {
        return when (raza) {
            "Elfo" -> Personaje.Raza.Elfo
            "Enano" -> Personaje.Raza.Enano
            "Humano" -> Personaje.Raza.Humano
            "Maldito" -> Personaje.Raza.Maldito
            else -> Personaje.Raza.Maldito
        }
    }

    private fun obtenerEstadoVitalEnum(estadoVital: String): Personaje.EstadoVital {
        return when (estadoVital) {
            "Adulto" -> Personaje.EstadoVital.Adulto
            "Joven" -> Personaje.EstadoVital.Joven
            "Anciano" -> Personaje.EstadoVital.Anciano
            else -> Personaje.EstadoVital.Adulto
        }
    }

    private fun obtenerClaseEnum(clase: String): Personaje.Clase {
        return when (clase) {
            "Brujo" -> Personaje.Clase.Brujo
            "Guerrero" -> Personaje.Clase.Guerrero
            "Mago" -> Personaje.Clase.Mago
            else -> Personaje.Clase.Mago
        }
    }

    fun insertarPersonaje(personaje: Personaje) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID_USUARIO_AUTH, FirebaseAuth.getInstance().uid.toString())
            put(COLUMN_NOMBRE, personaje.getNombre())
            put(COLUMN_RAZA, personaje.getRaza().name)
            put(COLUMN_CLASE, personaje.getClase().name)
            put(COLUMN_ESTADOVITAL, personaje.getEstadoVital().name)
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
