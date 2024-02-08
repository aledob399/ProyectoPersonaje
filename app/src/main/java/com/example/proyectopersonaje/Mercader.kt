package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class Mercader : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mercader)
        val personaje = intent.getParcelableExtra<Personaje>("personaje")
        val btnComerciar = findViewById<Button>(R.id.comerciar)
        val btnContinuar = findViewById<Button>(R.id.continuar)
        val btnComprar = findViewById<Button>(R.id.comprar)
        val btnVender = findViewById<Button>(R.id.vender)
        val btnCancelar = findViewById<Button>(R.id.cancelar)
        val precio = findViewById<TextView>(R.id.precioTextView)
        val btnIzquierda = findViewById<Button>(R.id.izquierda)
        val btnDerecha = findViewById<Button>(R.id.derecha)
        var posicionObjeto = 0
        val objetoPersonaje = findViewById<ImageView>(R.id.objetoPersonaje)
        val dbHelper = Objeto.DatabaseHelper(this)
        val objeto1 =
            Articulo(Articulo.TipoArticulo.ARMA, Articulo.Nombre.DAGA, 2, 34, R.drawable.objeto)
        val objeto2 =
            Articulo(Articulo.TipoArticulo.ORO, Articulo.Nombre.MONEDA, 3, 24, R.drawable.objetodos)
        val objeto3 = Articulo(
            Articulo.TipoArticulo.PROTECCION,
            Articulo.Nombre.ARMADURA,
            2,
            34,
            R.drawable.objetotres
        )
        val objeto4 = Articulo(
            Articulo.TipoArticulo.ARMA,
            Articulo.Nombre.BASTON,
            5,
            42,
            R.drawable.objetocinco
        )
        val objeto5 = Articulo(
            Articulo.TipoArticulo.ARMA,
            Articulo.Nombre.GARRAS,
            7,
            74,
            R.drawable.objetocuatro
        )
        val objeto6 =
            Articulo(Articulo.TipoArticulo.ARMA, Articulo.Nombre.DAGA, 9, 94, R.drawable.objetoseis)
        val objeto7 = Articulo(
            Articulo.TipoArticulo.OBJETO,
            Articulo.Nombre.IRA,
            1,
            32,
            R.drawable.objetosiete
        )
        val objeto8 = Articulo(
            Articulo.TipoArticulo.PROTECCION,
            Articulo.Nombre.ESCUDO,
            3,
            36,
            R.drawable.objetoocho
        )
        val objeto9 = Articulo(
            Articulo.TipoArticulo.ARMA,
            Articulo.Nombre.MARTILLO,
            2,
            83,
            R.drawable.objetodos
        )
        val objeto10 = Articulo(
            Articulo.TipoArticulo.ARMA,
            Articulo.Nombre.GARRAS,
            4,
            34,
            R.drawable.objetotres
        )

        personaje?.getMochila()?.addArticulo(objeto1)


        personaje?.getMochila()?.addArticulo(objeto2)

        dbHelper.insertarArticulo(objeto1)
        dbHelper.insertarArticulo(objeto2)
        dbHelper.insertarArticulo(objeto3)
        dbHelper.insertarArticulo(objeto4)
        dbHelper.insertarArticulo(objeto5)
        dbHelper.insertarArticulo(objeto6)
        dbHelper.insertarArticulo(objeto7)
        dbHelper.insertarArticulo(objeto8)
        dbHelper.insertarArticulo(objeto9)
        dbHelper.insertarArticulo(objeto10)


        val mochilaDb = Mochila(10)
        val max = dbHelper.getArticulos().size
        val numRand = (1..max).random()

        mochilaDb.setContenido(dbHelper.getArticulos())

        val articuloActual = dbHelper.getArticulos().get(numRand)
        btnComerciar.setOnClickListener {

            btnComerciar.visibility = View.GONE
            btnContinuar.visibility = View.GONE
            precio.visibility = View.VISIBLE

            btnComprar.visibility = View.VISIBLE
            btnVender.visibility = View.VISIBLE
            btnCancelar.visibility = View.VISIBLE
            cambiarImagenYMostrarPrecio(articuloActual!!)

        }
        btnContinuar.setOnClickListener {
            val intent = Intent(this, Aventura::class.java)
            intent.putExtra("personaje", personaje)
            startActivity(intent)
        }
        btnComprar.setOnClickListener {
            if (articuloActual != null && personaje!!.misMonedas() > articuloActual.getPrecio()) {

                personaje.restarMonedas(articuloActual)
                
                Toast.makeText(this, "Objeto comprado correctamente", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "No puedes comprar el objeto", Toast.LENGTH_SHORT).show()
            }
        }

        btnVender.setOnClickListener {
            if (personaje?.getMochila()?.getContenido()?.isNotEmpty() == true) {

                btnComprar.visibility = View.GONE
                btnVender.visibility = View.GONE
                btnIzquierda.visibility = View.VISIBLE
                btnDerecha.visibility = View.VISIBLE
                objetoPersonaje.setImageResource(
                    personaje.getMochila().getContenido().get(0).getUrl()
                )

                cambiarImagenYMostrarPrecio(
                    personaje?.getMochila()?.getContenido()!!.get(posicionObjeto)
                )
                personaje?.getMochila()?.getContenido()?.removeAt(posicionObjeto)


            } else {
                Toast.makeText(
                    this,
                    "No tienes objetos en la mochila para vender",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        btnIzquierda.setOnClickListener {
            if (posicionObjeto == 0) {
                posicionObjeto = personaje!!.getMochila().getContenido().size
            } else {
                posicionObjeto--
            }
            cambiarImagenYMostrarPrecio(
                personaje?.getMochila()?.getContenido()!!.get(posicionObjeto)
            )
        }

        btnDerecha.setOnClickListener {
            if (posicionObjeto == personaje!!.getMochila().getContenido().size - 1) {
                posicionObjeto = 0
            } else {
                posicionObjeto++
            }
            cambiarImagenYMostrarPrecio(
                personaje?.getMochila()?.getContenido()!!.get(posicionObjeto)
            )
        }


        btnCancelar.setOnClickListener {

            onBackPressed()
        }

    }

    private fun cambiarImagenYMostrarPrecio(articulo: Articulo) {
        val imagenObjeto = findViewById<ImageView>(R.id.mercader)

        imagenObjeto.setImageResource(articulo.getUrl())

        val precioTextView = findViewById<TextView>(R.id.precioTextView)

        precioTextView.text = "Precio: ${articulo.getPrecio()}"
        precioTextView.visibility = View.VISIBLE


    }

    class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE, null, DATABASE_VERSION) {
        companion object {
            private const val DATABASE_VERSION = 1
            private const val DATABASE = "articulos.db"
            private const val TABLA_ARTICULOS = "articulos"
            private const val KEY_ID = "_id"
            private const val COLUMN_TIPO_ARTICULO = "tipoArticulo"
            private const val COLUMN_NOMBRE = "nombre"
            private const val COLUMN_PESO = "peso"
            private const val COLUMN_UNIDADES = "unidades"
            private const val COLUMN_URL = "url"
            private const val COLUMN_PRECIO = "precio"

        }

        override fun onCreate(db: SQLiteDatabase) {
            val createTable = "CREATE TABLE $TABLA_ARTICULOS ($KEY_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_NOMBRE TEXT, $COLUMN_TIPO_ARTICULO TEXT, $COLUMN_UNIDADES INTEGER," +
                    "$COLUMN_PESO INTEGER,$COLUMN_URL INTEGER,$COLUMN_PRECIO INTEGER)"
            db.execSQL(createTable)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLA_ARTICULOS")
            onCreate(db)
        }

        fun insertarArticulo(articulo: Articulo) {
            val db = this.writableDatabase
            var values = ContentValues()
            when (articulo.getTipoArticulo()) {
                Articulo.TipoArticulo.ARMA -> {
                    when (articulo.getNombre()) {
                        Articulo.Nombre.BASTON, Articulo.Nombre.ESPADA, Articulo.Nombre.DAGA,
                        Articulo.Nombre.MARTILLO, Articulo.Nombre.GARRAS -> {
                            values = ContentValues().apply {
                                put(COLUMN_NOMBRE, articulo.getNombre().name)
                                put(COLUMN_TIPO_ARTICULO, articulo.getTipoArticulo().name)
                                put(COLUMN_PESO, articulo.getPeso())
                                put(COLUMN_PRECIO, articulo.getPrecio())
                                put(COLUMN_URL, articulo.getUrl())
                            }
                        }

                        else -> println("Nombre del artículo no válido para el tipo ARMA.")
                    }
                }

                Articulo.TipoArticulo.OBJETO -> {
                    when (articulo.getNombre()) {
                        Articulo.Nombre.POCION, Articulo.Nombre.IRA -> {
                            values = ContentValues().apply {
                                put(COLUMN_NOMBRE, articulo.getNombre().name)
                                put(COLUMN_TIPO_ARTICULO, articulo.getTipoArticulo().name)
                                put(COLUMN_PESO, articulo.getPeso())
                                put(COLUMN_PRECIO, articulo.getPrecio())
                                put(COLUMN_URL, articulo.getUrl())
                            }
                        }

                        else -> println("Nombre del artículo no válido para el tipo OBJETO.")
                    }
                }

                Articulo.TipoArticulo.PROTECCION -> {
                    when (articulo.getNombre()) {
                        Articulo.Nombre.ESCUDO, Articulo.Nombre.ARMADURA -> {
                            values = ContentValues().apply {
                                put(COLUMN_NOMBRE, articulo.getNombre().name)
                                put(COLUMN_TIPO_ARTICULO, articulo.getTipoArticulo().name)
                                put(COLUMN_PESO, articulo.getPeso())
                                put(COLUMN_PRECIO, articulo.getPrecio())
                                put(COLUMN_URL, articulo.getUrl())
                            }
                        }

                        else -> println("Nombre del artículo no válido para el tipo PROTECCION.")
                    }
                }

                else -> {}
            }

            db.insert(TABLA_ARTICULOS, null, values)
            db.close()
        }

        @SuppressLint("Range")
        fun getArticulos(): ArrayList<Articulo> {
            val articulos = ArrayList<Articulo>()
            val selectQuery = "SELECT * FROM $TABLA_ARTICULOS"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                    var nom = Articulo.Nombre.ARMADURA
                    nom = when (cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))) {
                        "BASTON" -> {
                            Articulo.Nombre.BASTON
                        }

                        "ESPADA" -> {
                            Articulo.Nombre.ESPADA
                        }

                        "DAGA" -> {
                            Articulo.Nombre.DAGA
                        }

                        "MARTILLO" -> {
                            Articulo.Nombre.MARTILLO
                        }

                        "GARRAS" -> {
                            Articulo.Nombre.GARRAS
                        }

                        "POCION" -> {
                            Articulo.Nombre.POCION
                        }

                        "IRA" -> {
                            Articulo.Nombre.IRA
                        }

                        "ESCUDO" -> {
                            Articulo.Nombre.ESCUDO
                        }

                        "ARMADURA" -> {
                            Articulo.Nombre.ARMADURA
                        }

                        else -> {
                            Articulo.Nombre.IRA
                        }
                    }
                    var tipoArticulo = Articulo.TipoArticulo.ARMA

                    tipoArticulo =
                        when (cursor.getString(cursor.getColumnIndex(COLUMN_TIPO_ARTICULO))) {
                            "ARMA" -> {
                                Articulo.TipoArticulo.ARMA
                            }

                            "OBJETO" -> {
                                Articulo.TipoArticulo.OBJETO
                            }

                            "PROTECCION" -> {
                                Articulo.TipoArticulo.PROTECCION
                            }

                            else -> {
                                Articulo.TipoArticulo.PROTECCION
                            }
                        }
                    val peso = cursor.getInt(cursor.getColumnIndex(COLUMN_PESO))
                    val precio = cursor.getInt(cursor.getColumnIndex(COLUMN_PESO))
                    val url = cursor.getInt(cursor.getColumnIndex(COLUMN_URL))

                    articulos.add(Articulo(tipoArticulo, nom, peso, precio, url))

                } while (cursor.moveToNext())


            }
            cursor.close()
            db.close()
            return articulos
        }

        @SuppressLint("Range")
        fun contieneObjeto(articulo: Articulo): Boolean {

            val selectQuery = "SELECT * FROM $TABLA_ARTICULOS"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            var flag = false
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(cursor.getColumnIndex(COLUMN_TIPO_ARTICULO)) == articulo.getTipoArticulo().name && cursor.getString(
                            cursor.getColumnIndex(
                                COLUMN_NOMBRE
                            )
                        ) == articulo.getNombre().name && cursor.getInt(
                            cursor.getColumnIndex(
                                COLUMN_URL
                            )
                        ) == articulo.getUrl() && cursor.getInt(
                            cursor.getColumnIndex(
                                COLUMN_PESO
                            )
                        ) == articulo.getPeso()
                    ) {
                        flag = true

                    }

                } while (cursor.moveToNext())


            }
            cursor.close()
            db.close()
            return flag

        }

        @SuppressLint("Range")
        fun eliminarUnidad(articulo: Articulo) {
            val db = this.writableDatabase
            val whereClause =
                "$COLUMN_TIPO_ARTICULO = ? AND $COLUMN_NOMBRE = ? AND $COLUMN_URL = ? AND $COLUMN_PESO = ?"

            val whereArgs = arrayOf(
                articulo.getTipoArticulo().name,
                articulo.getNombre().name,
                articulo.getUrl().toString(),
                articulo.getPeso().toString()
            )

            val cursor = db.query(
                TABLA_ARTICULOS, null, whereClause, whereArgs,
                null, null, null
            )

            if (cursor.moveToFirst()) {
                val unidades = cursor.getInt(cursor.getColumnIndex(COLUMN_UNIDADES))
                if (unidades <= 1) {
                    // Si hay 1 unidad o menos, eliminamos el artículo
                    db.delete(TABLA_ARTICULOS, whereClause, whereArgs)
                } else {
                    // Si hay más de 1 unidad, disminuimos el número de unidades en 1
                    val updatedUnits = unidades - 1
                    val contentValues = ContentValues().apply {
                        put(COLUMN_UNIDADES, updatedUnits)
                    }
                    db.update(TABLA_ARTICULOS, contentValues, whereClause, whereArgs)
                }
            }
            cursor.close()
            db.close()
        }

    }
}
