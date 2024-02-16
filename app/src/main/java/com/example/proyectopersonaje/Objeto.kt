package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class Objeto : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_objeto)
        val btnRecoger = findViewById<Button>(R.id.recoger)
        val btnContinuar = findViewById<Button>(R.id.continuar)
        val personaje=intent.getParcelableExtra<Personaje>("personaje")
        val mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")
        val dbHelper = DatabaseHelperMercader(this)
        val objeto1=Articulo(Articulo.TipoArticulo.ARMA,Articulo.Nombre.DAGA,2,34,R.drawable.objeto,1,Articulo.Rareza.COMUN)
        val objeto2=Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,3,24,R.drawable.objetodos,1,Articulo.Rareza.COMUN)
        val objeto3=Articulo(Articulo.TipoArticulo.PROTECCION,Articulo.Nombre.ARMADURA,2,34,R.drawable.objetotres,1,Articulo.Rareza.COMUN)
        val objeto4=Articulo(Articulo.TipoArticulo.ARMA,Articulo.Nombre.BASTON,5,42,R.drawable.objetocinco,1,Articulo.Rareza.COMUN)
        val objeto5=Articulo(Articulo.TipoArticulo.ARMA,Articulo.Nombre.GARRAS,7,74,R.drawable.objetocuatro,1,Articulo.Rareza.COMUN)
        val objeto6=Articulo(Articulo.TipoArticulo.ARMA,Articulo.Nombre.DAGA,9,94,R.drawable.objetoseis,1,Articulo.Rareza.COMUN)
        val objeto7=Articulo(Articulo.TipoArticulo.OBJETO,Articulo.Nombre.IRA,1,32,R.drawable.objetosiete,1,Articulo.Rareza.COMUN)
        val objeto8=Articulo(Articulo.TipoArticulo.PROTECCION,Articulo.Nombre.ESCUDO,3,36,R.drawable.objetoocho,1,Articulo.Rareza.COMUN)
        val objeto9=Articulo(Articulo.TipoArticulo.ARMA,Articulo.Nombre.MARTILLO,2,83,R.drawable.objetodos,1,Articulo.Rareza.COMUN)
        val objeto10=Articulo(Articulo.TipoArticulo.ARMA,Articulo.Nombre.GARRAS,4,34,R.drawable.objetotres,1,Articulo.Rareza.COMUN)

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

        val mochilaDb=Mochila(10)
        val numRand = (1..9).random()
        mochilaDb.setContenido(dbHelper.getArticulos())
        mochilaDb.getContenido().get(numRand).getUrl()


        val imagenObjeto=findViewById<ImageView>(R.id.objeto)
        imagenObjeto.setImageResource(mochilaDb.getContenido().get(numRand).getUrl())


        btnRecoger.setOnClickListener {

            if(personaje!!.getMochila().getPesoMochila()>mochilaDb.getContenido().get(numRand).getPeso()){
                personaje!!.getMochila().addArticulo(mochilaDb.getContenido().get(numRand))
                Toast.makeText(this, "Objeto recogido correctamente", Toast.LENGTH_SHORT).show()
            }else Toast.makeText(this, "El objeto no se puede recoger", Toast.LENGTH_SHORT).show()
        }
        btnContinuar.setOnClickListener {
            val intent=Intent(this,Aventura::class.java)
            intent.putExtra("personaje",personaje)
            intent.putParcelableArrayListExtra("mascotas", mascotas)
            startActivity(intent)
        }
    }

    class DatabaseHelperMercader(context: Context) :
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
        fun insertarArticulo(articulo: Articulo){
            val db=this.writableDatabase
            var values= ContentValues()
            when (articulo.getTipoArticulo()) {
                Articulo.TipoArticulo.ARMA -> {
                    when (articulo.getNombre()) {
                        Articulo.Nombre.BASTON, Articulo.Nombre.ESPADA, Articulo.Nombre.DAGA,
                        Articulo.Nombre.MARTILLO, Articulo.Nombre.GARRAS -> {
                            values= ContentValues().apply{
                                put(COLUMN_NOMBRE,articulo.getNombre().name)
                                put(COLUMN_TIPO_ARTICULO,articulo.getTipoArticulo().name)
                                put(COLUMN_PESO,articulo.getPeso())
                                put(COLUMN_PRECIO,articulo.getPrecio())
                                put(COLUMN_URL,articulo.getUrl())
                            }
                        }
                        else -> println("Nombre del artículo no válido para el tipo ARMA.")
                    }
                }
                Articulo.TipoArticulo.OBJETO -> {
                    when (articulo.getNombre()) {
                        Articulo.Nombre.POCION, Articulo.Nombre.IRA -> {
                            values= ContentValues().apply{
                                put(COLUMN_NOMBRE,articulo.getNombre().name)
                                put(COLUMN_TIPO_ARTICULO,articulo.getTipoArticulo().name)
                                put(COLUMN_PESO,articulo.getPeso())
                                put(COLUMN_PRECIO,articulo.getPrecio())
                                put(COLUMN_URL,articulo.getUrl())
                            }
                        }
                        else -> println("Nombre del artículo no válido para el tipo OBJETO.")
                    }
                }
                Articulo.TipoArticulo.PROTECCION -> {
                    when (articulo.getNombre()) {
                        Articulo.Nombre.ESCUDO, Articulo.Nombre.ARMADURA -> {
                            values= ContentValues().apply{
                                put(COLUMN_NOMBRE,articulo.getNombre().name)
                                put(COLUMN_TIPO_ARTICULO,articulo.getTipoArticulo().name)
                                put(COLUMN_PESO,articulo.getPeso())
                                put(COLUMN_PRECIO,articulo.getPrecio())
                                put(COLUMN_URL,articulo.getUrl())
                            }
                        }
                        else -> println("Nombre del artículo no válido para el tipo PROTECCION.")
                    }
                }

                else -> {}
            }

            db.insert(TABLA_ARTICULOS,null,values)
            db.close()
        }
        @SuppressLint("Range")
        fun getArticulos():ArrayList<Articulo>{
            val articulos=ArrayList<Articulo>()
            val selectQuery = "SELECT * FROM $TABLA_ARTICULOS"
            val db=this.readableDatabase
            val cursor=db.rawQuery(selectQuery,null)
            if(cursor.moveToFirst()){
                do{
                    val id=cursor.getInt(cursor.getColumnIndex(KEY_ID))
                    var nom=Articulo.Nombre.ARMADURA
                    nom = when(cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))){
                        "BASTON"-> {
                            Articulo.Nombre.BASTON
                        }
                        "ESPADA"-> {
                            Articulo.Nombre.ESPADA
                        }
                        "DAGA"-> {
                            Articulo.Nombre.DAGA
                        }
                        "MARTILLO"-> {
                            Articulo.Nombre.MARTILLO
                        }
                        "GARRAS"-> {
                            Articulo.Nombre.GARRAS
                        }
                        "POCION"-> {
                            Articulo.Nombre.POCION
                        }
                        "IRA"-> {
                            Articulo.Nombre.IRA
                        }
                        "ESCUDO"-> {
                            Articulo.Nombre.ESCUDO
                        }
                        "ARMADURA"-> {
                            Articulo.Nombre.ARMADURA
                        }

                        else -> {Articulo.Nombre.IRA}
                    }
                    var tipoArticulo=Articulo.TipoArticulo.ARMA

                    tipoArticulo=when(cursor.getString(cursor.getColumnIndex(COLUMN_TIPO_ARTICULO))){
                        "ARMA"-> {
                            Articulo.TipoArticulo.ARMA
                        }
                        "OBJETO"-> {
                            Articulo.TipoArticulo.OBJETO
                        }
                        "PROTECCION"-> {
                            Articulo.TipoArticulo.PROTECCION
                        }

                        else -> {Articulo.TipoArticulo.PROTECCION}
                    }
                    val peso=cursor.getInt(cursor.getColumnIndex(COLUMN_PESO))
                    val precio=cursor.getInt(cursor.getColumnIndex(COLUMN_PESO))
                    val url=cursor.getInt(cursor.getColumnIndex(COLUMN_URL))
                    val unidades=cursor.getInt(cursor.getColumnIndex(COLUMN_UNIDADES))

                    articulos.add(Articulo(tipoArticulo,nom,peso,precio,url,1,Articulo.Rareza.COMUN))

                }while (cursor.moveToNext())




            }
            cursor.close()
            db.close()
            return articulos
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
                    db.delete(TABLA_ARTICULOS, whereClause, whereArgs)
                } else {
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