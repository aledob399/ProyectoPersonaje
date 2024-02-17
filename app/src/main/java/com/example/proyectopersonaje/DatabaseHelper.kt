package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
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
        private const val COLUMN_SALUD = "salud"
        private const val COLUMN_EXPERIENCIA = "experiencia"
        private const val COLUMN_NIVEL = "nivel"
        private const val COLUMN_CLASE = "clase"
        private const val COLUMN_ESTADOVITAL = "estadoVital"
        private const val COLUMN_TIPO_ARTICULO = "tipoArticulo"
        private const val COLUMN_NOMBRE_ARTICULO = "nombre"
        private const val COLUMN_PESO = "peso"
        private const val COLUMN_UNIDADES = "unidades"
        private const val COLUMN_URL = "url"
        private const val COLUMN_PRECIO = "precio"
        private const val COLUMN_ID_PERSONAJE = "id_personaje"
        private const val COLUMN_ID_ARTICULO = "id_articulo"
        private const val COLUMN_ID_USUARIO_AUTH = "idUsuarioAuth"
        private const val COLUMN_ID_USUARIO_AUTH_ARTICULOS = "idUsuarioAuthArticulos"
        private const val TABLE_MASCOTAS = "mascotas"
        private const val COLUMN_ID_MASCOTA = "id_mascota"
        private const val COLUMN_NOMBRE_MASCOTA = "nombre_mascota"
        private const val COLUMN_ATRIBUTO_MASCOTA = "atributo_mascota"
        private const val COLUMN_SALUD_MASCOTA = "salud_mascota"
        private const val COLUMN_ATAQUE_MASCOTA = "ataque_mascota"
        private const val COLUMN_EXPERIENCIA_MASCOTA = "experiencia_mascota"
        private const val COLUMN_NIVEL_MASCOTA = "nivel_mascota"
        private const val COLUMN_POTENCIAL_MASCOTA = "potencial_mascota"
        private const val COLUMN_DEFENSA_MASCOTA = "defensa_mascota"
        private const val COLUMN_ID_PERSONAJE_MASCOTA = "id_personaje_mascota"

        private const val CREATE_TABLE_MASCOTAS =
            "CREATE TABLE $TABLE_MASCOTAS (" +
                    "$COLUMN_ID_MASCOTA INTEGER PRIMARY KEY," +
                    "$COLUMN_ID_PERSONAJE_MASCOTA INTEGER," +
                    "$COLUMN_NOMBRE_MASCOTA TEXT," +
                    "$COLUMN_ATRIBUTO_MASCOTA TEXT," +
                    "$COLUMN_SALUD_MASCOTA INTEGER," +
                    "$COLUMN_ATAQUE_MASCOTA INTEGER," +
                    "$COLUMN_EXPERIENCIA_MASCOTA INTEGER," +
                    "$COLUMN_NIVEL_MASCOTA INTEGER," +
                    "$COLUMN_POTENCIAL_MASCOTA INTEGER," +
                    "$COLUMN_DEFENSA_MASCOTA INTEGER," +
                    "FOREIGN KEY($COLUMN_ID_PERSONAJE_MASCOTA) REFERENCES $TABLE_PERSONAJE($COLUMN_ID))"


        private const val CREATE_TABLE_PERSONAJE =
            "CREATE TABLE $TABLE_PERSONAJE (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_ID_USUARIO_AUTH TEXT," +
                    "$COLUMN_NOMBRE TEXT," +
                    "$COLUMN_SALUD INTEGER," +
                    "$COLUMN_NIVEL INTEGER," +
                    "$COLUMN_EXPERIENCIA INTEGER," +
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
                    "$COLUMN_ID_USUARIO_AUTH TEXT,"+
                    "$COLUMN_NOMBRE_ARTICULO TEXT," +
                    "$COLUMN_TIPO_ARTICULO TEXT," +
                    "$COLUMN_PESO INTEGER,"+
                    "$COLUMN_PRECIO INTEGER,"+
                    "$COLUMN_UNIDADES INTEGER,"+
                    "$COLUMN_URL INT)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_PERSONAJE)
        db.execSQL(CREATE_TABLE_MOCHILA)
        db.execSQL(CREATE_TABLE_ARTICULOS)
        db.execSQL(CREATE_TABLE_MASCOTAS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PERSONAJE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOCHILA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ARTICULOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MASCOTAS")
        onCreate(db)
    }
    fun insertarMascotas(mascotas: ArrayList<Mascota>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            // Eliminar todas las mascotas existentes
            db.delete(TABLE_MASCOTAS, null, null)

            // Insertar las nuevas mascotas
            for (mascota in mascotas) {
                val values = ContentValues().apply {
                    put(COLUMN_ID_PERSONAJE_MASCOTA, 1)
                    put(COLUMN_NOMBRE_MASCOTA, mascota.getNombre())
                    put(COLUMN_ATRIBUTO_MASCOTA, mascota.getAtributo().name)
                    put(COLUMN_SALUD_MASCOTA, mascota.getSalud())
                    put(COLUMN_ATAQUE_MASCOTA, mascota.getAtaque())
                    put(COLUMN_EXPERIENCIA_MASCOTA, mascota.getExperiencia())
                    put(COLUMN_NIVEL_MASCOTA, mascota.getNivel())
                    put(COLUMN_POTENCIAL_MASCOTA, mascota.getPotencial())
                    put(COLUMN_DEFENSA_MASCOTA, mascota.getDefensa())
                }
                db.insert(TABLE_MASCOTAS, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }


    fun obtenerMascotas(): ArrayList<Mascota> {
        val mascotas = ArrayList<Mascota>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_MASCOTAS WHERE $COLUMN_ID_PERSONAJE_MASCOTA = ? AND $COLUMN_ID_USUARIO_AUTH= ?"
        val cursor = db.rawQuery(query, arrayOf("1",FirebaseAuth.getInstance().uid)) // Cambia "1" por el valor correcto del ID del personaje
        cursor.use { cursor ->
            while (cursor.moveToNext()) {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_MASCOTA))
                val atributoString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ATRIBUTO_MASCOTA))
                val atributo = Mascota.tipoMascota.valueOf(atributoString)
                val salud = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SALUD_MASCOTA))
                val ataque = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ATAQUE_MASCOTA))
                val experiencia = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXPERIENCIA_MASCOTA))
                val nivel = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NIVEL_MASCOTA))
                val potencial = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POTENCIAL_MASCOTA))
                val defensa = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DEFENSA_MASCOTA))
                val mascota=Mascota(nombre, atributo)
                mascota.setNivel(nivel)
                mascota.setPotencial(potencial)
                mascota.setExperiencia(experiencia)
                mascotas.add(mascota)
            }
        }
        return mascotas
    }

    fun insertarPersonaje(personaje: Personaje,idUsuarioAuth: String) {
        val db = writableDatabase

        val articulos = personaje.getMochila().getContenido()

        // Begin transaction
        db.beginTransaction()
        try {
            // Delete the character with ID 1
            db.delete(TABLE_PERSONAJE, "$COLUMN_ID_USUARIO_AUTH = ?  ", arrayOf("$idUsuarioAuth"))

            // Insert the new character
            val valuesPersonaje = ContentValues().apply {
                put(COLUMN_ID_USUARIO_AUTH, idUsuarioAuth)
                put(COLUMN_NOMBRE, personaje.getNombre())
                put(COLUMN_RAZA, personaje.getRaza().name)
                put(COLUMN_CLASE, personaje.getClase().name)
                put(COLUMN_ESTADOVITAL, personaje.getEstadoVital().name)
                /*
                put(COLUMN_NIVEL,personaje.getNivel())
                put(COLUMN_SALUD,personaje.getSalud())
                put(COLUMN_EXPERIENCIA,personaje.getExperiencia())
                */

            }
            val idPersonaje = db.insert(TABLE_PERSONAJE, null, valuesPersonaje)
            Log.d("DatabaseHelper", "Personaje insertado con ID: $idPersonaje")
            //insertarArticulos(personaje.getMochila().getContenido(),idUsuarioAuth)
            /*
            // Insert articles into the mochila
            for (articulo in articulos) {
                val valuesArticulo = ContentValues().apply {
                    put(COLUMN_ARTICULO_NOMBRE, articulo.getNombre().name)
                    put(COLUMN_ARTICULO_TIPO, articulo.getTipoArticulo().name)
                    put(COLUMN_ARTICULO_PESO, articulo.getPeso())
                }
                val idArticulo = db.insert(TABLE_ARTICULOS, null, valuesArticulo)

                val valuesMochila = ContentValues().apply {
                    put(COLUMN_ID_PERSONAJE, idPersonaje)
                    put(COLUMN_ID_ARTICULO, idArticulo)
                }
                db.insert(TABLE_MOCHILA, null, valuesMochila)


            }
             */

            // Transaction successful
            db.setTransactionSuccessful()
        } finally {
            // End transaction
            db.endTransaction()
        }
        // Close the database connection
        db.close()

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
            /*
            val nivel=cursor.getInt(cursor.getColumnIndex(COLUMN_NIVEL))
            val salud=cursor.getInt(cursor.getColumnIndex(COLUMN_SALUD))
            val experiencia=cursor.getInt(cursor.getColumnIndex(COLUMN_EXPERIENCIA))

             */
            val razaFinal: Personaje.Raza = obtenerRazaEnum(raza)
            val estadoVitalFinal: Personaje.EstadoVital = obtenerEstadoVitalEnum(estadoVital)
            val claseFinal: Personaje.Clase = obtenerClaseEnum(clase)

            personaje = Personaje(nombre, razaFinal, claseFinal, estadoVitalFinal)
          //  personaje.getMochila().setContenido(obtenerArticulos(idUsuarioAuth).getContenido())
/*
            personaje.setExperiencia(experiencia)
            personaje.setSalud(salud)
            personaje.setNivel(nivel)
*/

            /*
            if(cargarMochila(1)==null){


            }else {
                cargarMochila(1)?.let { personaje.getMochila().setContenido(it) }
            }

             */

        }

        cursor.close()
        return personaje
    }
    fun insertarArticulos(articulos: ArrayList<Articulo>, idUsuarioAuth: String) {
        val db = this.writableDatabase
       // articulos.add(Articulo(Articulo.TipoArticulo.ARMA, Articulo.Nombre.ESPADA, 2, 2, R.drawable.moneda, 1))
        db.beginTransaction()
        if (articulos.isEmpty()) {
            Log.d("DatabaseHelper", "No hay articulos para insertar")
        } else {
            try {
                for (articulo in articulos) {
                    val values = ContentValues().apply {
                        put(COLUMN_NOMBRE_ARTICULO, articulo.getNombre().name)
                        put(COLUMN_TIPO_ARTICULO, articulo.getTipoArticulo().name)
                        put(COLUMN_PESO, articulo.getPeso())
                        put(COLUMN_PRECIO, articulo.getPrecio())
                        put(COLUMN_URL, articulo.getUrl())
                        put(COLUMN_UNIDADES, articulo.getUnidades())
                    }
                    val rowId = db.insert(TABLE_ARTICULOS, null, values)
                    Log.d("DatabaseHelper", "Artículo insertado con ID: $rowId")
                }
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
                db.close()
            }
        }
    }


    fun obtenerArticulos(idUsuario: String): Mochila {
        val mochila=Mochila(100)
        val articulos = ArrayList<Articulo>()
        val db = readableDatabase

        val query = "SELECT * FROM $TABLE_ARTICULOS WHERE $COLUMN_ID_USUARIO_AUTH= ? ORDER BY $COLUMN_ID"


        val cursor = db.rawQuery(query, arrayOf(idUsuario))


        if(cursor.moveToNext()){
            cursor.use { cursor ->
                while (cursor.moveToNext()) {
                    val nombreArticuloString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_ARTICULO))
                    val tipoArticuloString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO_ARTICULO))
                    val nombreArticulo = obtenerNombreArticuloEnum(nombreArticuloString)
                    val tipoArticulo = obtenerTipoArticuloEnum(tipoArticuloString)
                    val peso = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PESO))
                    val precio = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRECIO))
                    val unidades = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UNIDADES))
                    val url = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_URL))
                  //  val articulo = Articulo( tipoArticulo,nombreArticulo, peso, precio, unidades, url)
                 //   mochila.addArticulo(articulo)

                }
            }

        }
        if(mochila.getContenido().isEmpty()){
            return Mochila(100)
        }else return mochila

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
    private fun obtenerNombreArticuloEnum(nombre: String): Articulo.Nombre{
        return when (nombre) {
            "ARMADURA" -> Articulo.Nombre.ARMADURA
            "BASTON" -> Articulo.Nombre.BASTON
            "DAGA" -> Articulo.Nombre.DAGA
            "ESCUDO" -> Articulo.Nombre.ESCUDO
            "ESPADA" -> Articulo.Nombre.ESPADA
            "GARRAS" -> Articulo.Nombre.GARRAS
            "IRA" -> Articulo.Nombre.IRA
            "MARTILLO" -> Articulo.Nombre.MARTILLO
            "MONEDA" -> Articulo.Nombre.MONEDA
            "POCION" -> Articulo.Nombre.POCION
            else -> Articulo.Nombre.BASTON
        }
    }
    private fun obtenerTipoArticuloEnum(tipo: String): Articulo.TipoArticulo{
        return when (tipo) {
            "ARMA" -> Articulo.TipoArticulo.ARMA
            "OBJETO" -> Articulo.TipoArticulo.OBJETO
            "ORO" -> Articulo.TipoArticulo.ORO
            "PROTECCION" -> Articulo.TipoArticulo.PROTECCION
            else ->  Articulo.TipoArticulo.PROTECCION
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






    fun contienePersonaje(): Boolean {
        val db = readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_PERSONAJE"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count > 0
    }






}
