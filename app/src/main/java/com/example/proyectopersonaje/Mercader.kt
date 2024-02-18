package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class Mercader : AppCompatActivity(),TextToSpeech.OnInitListener {
    private lateinit var textToSpeech:TextToSpeech
    private var cadena: String? =null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mercader)
        val personaje = intent.getParcelableExtra<Personaje>("personaje")
        val mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")
        val btnComerciar = findViewById<Button>(R.id.comerciar)
        val idUsuario=FirebaseAuth.getInstance().uid.toString()
        val btnContinuar = findViewById<Button>(R.id.continuar)
        val btnComprar = findViewById<Button>(R.id.comprar)
        val btnVender = findViewById<Button>(R.id.vender)
        val btnCancelar = findViewById<Button>(R.id.cancelar)
        val btnMas1 = findViewById<Button>(R.id.btnmas1)
        val btnMenos1 = findViewById<Button>(R.id.btnmenos1)
        val btnIzquierda = findViewById<Button>(R.id.izquierda)
        var unidadestextView = findViewById<TextView>(R.id.unidadesTextView)
        val btnDerecha = findViewById<Button>(R.id.derecha)
        Log.d("DatosPersonaje", "$personaje")
        Log.d("DatosMascota 1", "${mascotas!![0]}")
        var posicionObjeto = 0
        var posiciondb = 0
        val objetoPersonaje = findViewById<ImageView>(R.id.objetoPersonaje)
        val dbHelper = DatabaseHelper(this)
        val btnLeerTexto = findViewById<ImageButton>(R.id.btnLeerTexto)

        textToSpeech = TextToSpeech(this){status->
            if (status == TextToSpeech.SUCCESS){
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA||result == TextToSpeech.LANG_NOT_SUPPORTED){
                    Toast.makeText(this,"lenguaje no soportado",Toast.LENGTH_LONG).show()
                }
            }
        }
        btnLeerTexto.setOnClickListener {
            leerDatos(cadena!!)
        }


        val objeto1 =
            Articulo(
                Articulo.TipoArticulo.ARMA,
                Articulo.Nombre.DAGA,
                2,
                34,
                R.drawable.objeto,
                1,
                Articulo.Rareza.COMUN
            )
        val objeto2 =
            Articulo(
                Articulo.TipoArticulo.ORO,
                Articulo.Nombre.MONEDA,
                3,
                15,
                R.drawable.moneda,
                1,
                Articulo.Rareza.COMUN
            )
        val objeto3 = Articulo(
            Articulo.TipoArticulo.PROTECCION,
            Articulo.Nombre.ARMADURA,
            2,
            34,
            R.drawable.objetotres, 6, Articulo.Rareza.COMUN
        )
        val objeto4 = Articulo(
            Articulo.TipoArticulo.ARMA,
            Articulo.Nombre.BASTON,
            5,
            42,
            R.drawable.objetocinco, 5, Articulo.Rareza.COMUN
        )
        val objeto5 = Articulo(
            Articulo.TipoArticulo.ARMA,
            Articulo.Nombre.GARRAS,
            7,
            74,
            R.drawable.objetocuatro, 3, Articulo.Rareza.COMUN
        )
        val objeto6 =
            Articulo(
                Articulo.TipoArticulo.ARMA,
                Articulo.Nombre.DAGA,
                9,
                94,
                R.drawable.objetoseis,
                1,
                Articulo.Rareza.COMUN
            )
        val objeto7 = Articulo(
            Articulo.TipoArticulo.OBJETO,
            Articulo.Nombre.IRA,
            1,
            32,
            R.drawable.objetosiete, 3, Articulo.Rareza.COMUN
        )
        val objeto8 = Articulo(
            Articulo.TipoArticulo.PROTECCION,
            Articulo.Nombre.ESCUDO,
            3,
            36,
            R.drawable.objetoocho, 4, Articulo.Rareza.COMUN
        )
        val objeto9 = Articulo(
            Articulo.TipoArticulo.ARMA,
            Articulo.Nombre.MARTILLO,
            2,
            83,
            R.drawable.objetodos, 1, Articulo.Rareza.COMUN
        )
        val objeto10 = Articulo(
            Articulo.TipoArticulo.ARMA,
            Articulo.Nombre.GARRAS,
            4,
            34,
            R.drawable.objetotres, 10, Articulo.Rareza.COMUN
        )
        cadena="El mercader tiene estos objetos $objeto1 $objeto2 $objeto3 $objeto4  $objeto6 $objeto6 $objeto7 $objeto8 $objeto9  $objeto10"
        dbHelper.eliminarArticulos(idUsuario)

        dbHelper.insertarArticulo(objeto1,idUsuario)
        dbHelper.insertarArticulo(objeto2,idUsuario)
        dbHelper.insertarArticulo(objeto3,idUsuario)
        dbHelper.insertarArticulo(objeto4,idUsuario)
        dbHelper.insertarArticulo(objeto5,idUsuario)
        dbHelper.insertarArticulo(objeto6,idUsuario)
        dbHelper.insertarArticulo(objeto7,idUsuario)
        dbHelper.insertarArticulo(objeto8,idUsuario)
        dbHelper.insertarArticulo(objeto9,idUsuario)
        dbHelper.insertarArticulo(objeto10,idUsuario)

        val imgMercader = findViewById<ImageView>(R.id.mercader)
        val popupView = layoutInflater.inflate(R.layout.popup_recompensa, null)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        val textViewRecompensa = popupView.findViewById<TextView>(R.id.textViewRecompensa)
        val mochilaDb = Mochila(10)
        val max = dbHelper.getArticulos(idUsuario).size
        var numRand = (1..max).random()
        val linearLayoutMercader = findViewById<LinearLayout>(R.id.objetosMercader)
        val linearLayoutPersonaje = findViewById<LinearLayout>(R.id.objetosPersonaje)
        val scroolViewMercader = findViewById<HorizontalScrollView>(R.id.scroolViewMercader)
        val scroolViewPersonaje = findViewById<HorizontalScrollView>(R.id.scroolViewPersonaje)
        mochilaDb.setContenido(dbHelper.getArticulos(idUsuario))
        var moneda = Articulo(
            Articulo.TipoArticulo.ORO,
            Articulo.Nombre.MONEDA,
            0,
            15,
            R.drawable.moneda,
            1,
            Articulo.Rareza.COMUN
        )
        personaje!!.getMochila()!!.addArticulo(moneda)
        personaje!!.getMochila()!!.addArticulo(moneda)
        personaje!!.getMochila()!!.addArticulo(moneda)
        personaje!!.getMochila()!!.addArticulo(moneda)
        personaje!!.getMochila()!!.addArticulo(moneda)
        personaje!!.getMochila()!!.addArticulo(moneda)
        personaje!!.getMochila()!!.addArticulo(moneda)
        var articuloActual = dbHelper.getArticulos(idUsuario).get(numRand)
        val btnGacha = findViewById<Button>(R.id.gacha)
        val btnContrato = findViewById<Button>(R.id.Contrato)
        val btnAceptar = findViewById<Button>(R.id.btnAceptar)
        val musica: MediaPlayer = MediaPlayer.create(this, R.raw.musica)
        val btnMusica: ImageButton = findViewById(R.id.btnMusica)

        var pos=intent.getIntExtra("posicion",0)
        musica.seekTo(pos)
        musica.start()
        musica.isLooping = true
        btnMusica.setOnClickListener {
            if(musica.isPlaying){
                pos = musica.currentPosition
                musica.pause()
                musica.isLooping = false
                btnMusica.setImageResource(android.R.drawable.ic_media_pause)
            }else{
                btnMusica.setImageResource(android.R.drawable.ic_media_play)
                musica.seekTo(pos)
                musica.start()
                musica.isLooping = true
            }
        }
        btnComerciar.setOnClickListener {
            btnContrato.visibility = View.GONE
            btnGacha.visibility = View.GONE
            btnComerciar.visibility = View.GONE
            btnContinuar.visibility = View.GONE
            // precio.visibility = View.VISIBLE

            btnComprar.visibility = View.VISIBLE
            btnVender.visibility = View.VISIBLE
            btnCancelar.visibility = View.VISIBLE
            // cambiarImagenYMostrarPrecio(articuloActual!!)

        }
        btnContinuar.setOnClickListener {
            textToSpeech.stop()
            val intent = Intent(this, Aventura::class.java)
            pos = musica.currentPosition
            intent.putExtra("personaje", personaje)
            intent.putParcelableArrayListExtra("mascotas", mascotas)
            intent.putExtra("posicion",pos)
            startActivity(intent)
        }

        btnContrato.visibility = View.VISIBLE
        btnGacha.visibility = View.VISIBLE
        btnGacha.setOnClickListener {
            val intent = Intent(this, Gacha::class.java)
            intent.putExtra("personaje", personaje)
            intent.putParcelableArrayListExtra("mascotas", mascotas)
            intent.putExtra("posicion",pos)
            startActivity(intent)

        }
        btnContrato.setOnClickListener {
            val intent = Intent(this, Contrato::class.java)
            intent.putExtra("personaje", personaje)
            intent.putParcelableArrayListExtra("mascotas", mascotas)
            intent.putExtra("posicion",pos)
            if(musica.isPlaying){
                musica.stop()
            }
            startActivity(intent)
        }
        btnComprar.setOnClickListener {
            btnVender.visibility=View.GONE
            btnComprar.visibility = View.GONE
            imgMercader.visibility = View.GONE
            btnContrato.visibility = View.GONE
            btnGacha.visibility = View.GONE
            scroolViewMercader.visibility = View.VISIBLE
            linearLayoutMercader.visibility = View.VISIBLE
           // val btnConfirmarCompra=findViewById<Button>(R.id.btnConfirmarCompra)

            for (articulo in mochilaDb.getContenido()) {

                val button = Button(this)
                button.setBackgroundResource(articulo.getUrl())
                button.setOnClickListener {
                    var unidades = 1
                    btnMas1.visibility = View.VISIBLE
                    btnMenos1.visibility = View.VISIBLE
                    btnMas1.setOnClickListener {
                        if (personaje!!.misMonedas() > articulo.getPrecio() * unidades && articulo.getUnidades() > unidades) {
                            unidades = unidades + 1
                            unidadestextView.text = "$unidades"
                        } else {
                            popupWindow.dismiss()
                            if (articulo.getUnidades() > unidades) {
                                textViewRecompensa.text = "Unidades maximas"
                            } else {
                                textViewRecompensa.text = "No tienes suficinente dinero o espacio"
                            }

                            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
                            Handler().postDelayed({
                                popupWindow.dismiss()
                                textViewRecompensa.text = articulo.toString()
                                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
                            }, 2000)
                        }

                    }
                    btnMenos1.setOnClickListener {
                        if (unidades > 1) {
                            unidades = unidades - 1
                            unidadestextView.text = "$unidades"
                        }

                    }
                    unidadestextView.visibility = View.VISIBLE
                    unidadestextView.text = "$unidades"

                    textViewRecompensa.text = articulo.toString()
                    popupWindow.width = LinearLayout.LayoutParams.WRAP_CONTENT
                    popupWindow.height = LinearLayout.LayoutParams.WRAP_CONTENT

                    popupWindow.isOutsideTouchable = true

                    popupWindow.contentView = popupView

                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
                    Handler().postDelayed({
                                    popupWindow.dismiss()

                    }, 2000)
                    btnAceptar.visibility=View.VISIBLE
                    btnAceptar.setOnClickListener {
                        if (personaje!!.misMonedas() > articulo.getPrecio() && personaje.getMochila()!!
                                .getPesoMochila() > articulo.getPeso() * unidades
                        ) {
                            popupWindow.dismiss()
                            button.visibility = View.GONE
                            personaje.restarMonedas(articulo.getPrecio())
                            personaje.getMochila()!!.addArticulo(articulo)
                            Log.d("DatosPersonaje", "$personaje")
                            Log.d("DatosMascota 1", "${mascotas!![0]}")

                        }


                    }
                    Handler().postDelayed({


                    }, 3000)


                }

                linearLayoutMercader.addView(button)
            }

            btnCancelar.setOnClickListener {
                btnContrato.visibility = View.VISIBLE
                btnComerciar.visibility= View.VISIBLE
                btnGacha.visibility = View.VISIBLE
                scroolViewMercader.visibility = View.GONE
                linearLayoutMercader.visibility = View.GONE
                unidadestextView.visibility = View.GONE
                btnMas1.visibility = View.GONE
                btnMenos1.visibility = View.GONE
                imgMercader.visibility=View.VISIBLE
                btnAceptar.visibility=View.GONE
                btnAceptar.text="Aceptar"

            }


        }

        btnVender.setOnClickListener {
            imgMercader.visibility=View.GONE
            btnComprar.visibility=View.GONE
            btnVender.visibility=View.GONE
            scroolViewPersonaje.visibility = View.VISIBLE
            linearLayoutPersonaje.visibility=View.VISIBLE
            linearLayoutPersonaje.removeAllViews()


            for(articulo in  personaje?.getMochila()!!.getContenido()) {
                if(articulo.getNombre()!=Articulo.Nombre.MONEDA){
                    val button = Button(this)
                    button.setBackgroundResource(articulo.getUrl())
                    button.setOnClickListener {
                        textViewRecompensa.text = articulo.toString()
                        popupWindow.width = LinearLayout.LayoutParams.WRAP_CONTENT
                        popupWindow.height = LinearLayout.LayoutParams.WRAP_CONTENT

                        popupWindow.isOutsideTouchable = true

                        popupWindow.contentView = popupView

                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
                        Handler().postDelayed({
                            popupWindow.dismiss()

                        }, 2000)
                        btnAceptar.visibility=View.VISIBLE
                        btnAceptar.text="Aceptar venta"
                        btnAceptar.setOnClickListener {
                            button.visibility=View.GONE
                            btnAceptar.visibility=View.GONE
                            personaje.getMochila()!!.borrarArticulo(articulo)
                            personaje.sumarMonedas(articulo.getPrecio())
                            Log.d("DatosPersonaje", "$personaje")
                            Log.d("DatosMascota 1", "${mascotas!![0]}")
                        }
                    }
                    linearLayoutPersonaje.addView(button)
                 }
                }
            btnCancelar.setOnClickListener {
                linearLayoutMercader.removeAllViews()
                linearLayoutPersonaje.removeAllViews()
                btnContrato.visibility = View.VISIBLE
                btnComerciar.visibility= View.VISIBLE
                btnGacha.visibility = View.VISIBLE
                scroolViewMercader.visibility = View.GONE
                linearLayoutMercader.visibility = View.GONE
                unidadestextView.visibility = View.GONE
                btnMas1.visibility = View.GONE
                btnMenos1.visibility = View.GONE
                imgMercader.visibility=View.VISIBLE
                btnAceptar.visibility=View.GONE
                btnAceptar.text="Aceptar"
                onBackPressed()

            }

        }
        btnCancelar.setOnClickListener {
            linearLayoutMercader.removeAllViews()
            linearLayoutPersonaje.removeAllViews()
            btnContrato.visibility = View.VISIBLE
            btnComerciar.visibility= View.VISIBLE
            btnGacha.visibility = View.VISIBLE
            scroolViewMercader.visibility = View.GONE
            linearLayoutMercader.visibility = View.GONE
            unidadestextView.visibility = View.GONE
            btnMas1.visibility = View.GONE
            btnMenos1.visibility = View.GONE
            imgMercader.visibility=View.VISIBLE
            btnAceptar.visibility=View.GONE
            btnAceptar.text="Aceptar"

        }


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.getDefault())
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Idioma no disponible, establecer un idioma de respaldo
                val backupLocale = Locale.US
                val backupResult = textToSpeech.setLanguage(backupLocale)
                if (backupResult == TextToSpeech.LANG_MISSING_DATA || backupResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Si el idioma de respaldo también está ausente o no es compatible, mostrar un mensaje de error
                    Toast.makeText(this, "El idioma no es compatible con Text-to-Speech", Toast.LENGTH_SHORT).show()
                } else {
                    // Si el idioma de respaldo está configurado correctamente, leer los datos del personaje
                    leerDatos(cadena!!)
                }
            } else {
                // Si el TextToSpeech se inicializa correctamente con el idioma predeterminado, leer los datos del personaje
                leerDatos(cadena!!)
            }
        } else {
            Toast.makeText(this, "Error al inicializar Text-to-Speech", Toast.LENGTH_SHORT).show()
        }


    }




    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun leerDatos(cadena:String) {
        // Obtener los datos del personaje y las mascotas
        val textoParaLeer = "Hola"

        // Verificar si el idioma español está disponible
        val localeSpanish = Locale("es", "ES")
        val result = textToSpeech.setLanguage(localeSpanish)

        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Si el idioma español no está disponible, mostrar un mensaje de error
            //  Toast.makeText(this, "El idioma español no está disponible para Text-to-Speech", Toast.LENGTH_SHORT).show()
        } else {
            // Si el idioma español está disponible, leer el texto proporcionado en español
            textToSpeech.speak(cadena.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onDestroy() {
        // Liberar los recursos del TextToSpeech
        if (textToSpeech != null) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }





    class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE, null, DATABASE_VERSION) {

        companion object {
            private const val DATABASE_VERSION = 2
            private const val DATABASE = "articulos.db"
            private const val TABLA_ARTICULOS = "articulos"
            private const val TABLA_PERSONAJE = "personaje"
            private const val TABLA_MOCHILA = "mochila"
            private const val KEY_ID = "_id"
            private const val COLUMN_TIPO_ARTICULO = "tipoArticulo"
            private const val COLUMN_NOMBRE = "nombre"
            private const val COLUMN_PESO = "peso"
            private const val COLUMN_UNIDADES = "unidades"
            private const val COLUMN_URL = "url"
            private const val COLUMN_PRECIO = "precio"
            private const val COLUMN_SALUD = "salud"
            private const val COLUMN_ID_USUARIO = "idUsuario"
        }

        override fun onCreate(db: SQLiteDatabase) {
            val createTableArticulos =
                "CREATE TABLE $TABLA_ARTICULOS ($KEY_ID INTEGER PRIMARY KEY," +
                        "$COLUMN_NOMBRE TEXT, $COLUMN_TIPO_ARTICULO TEXT, $COLUMN_UNIDADES INTEGER," +
                        "$COLUMN_PESO INTEGER, $COLUMN_URL INTEGER, $COLUMN_PRECIO INTEGER, $COLUMN_ID_USUARIO INTEGER)"
            db.execSQL(createTableArticulos)

            val createTableMochila = "CREATE TABLE $TABLA_MOCHILA ($KEY_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_NOMBRE TEXT, $COLUMN_PESO INTEGER, $COLUMN_UNIDADES INTEGER)"
            db.execSQL(createTableMochila)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLA_ARTICULOS")
            db.execSQL("DROP TABLE IF EXISTS $TABLA_PERSONAJE")
            db.execSQL("DROP TABLE IF EXISTS $TABLA_MOCHILA")
            onCreate(db)
        }

        fun insertarArticulo(articulo: Articulo, idUsuario:String) {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_NOMBRE, articulo.getNombre().name)
                put(COLUMN_TIPO_ARTICULO, articulo.getTipoArticulo().name)
                put(COLUMN_UNIDADES, articulo.getUnidades())
                put(COLUMN_PESO, articulo.getPeso())
                put(COLUMN_URL, articulo.getUrl())
                put(COLUMN_PRECIO, articulo.getPrecio())
                put(COLUMN_ID_USUARIO, idUsuario)
            }

            db.insert(TABLA_ARTICULOS, null, values)
            db.close()
        }

        @SuppressLint("Range")
        fun getArticulos(idUsuario:String): ArrayList<Articulo> {
            val articulos = ArrayList<Articulo>()
            val selectQuery = "SELECT * FROM $TABLA_ARTICULOS WHERE $COLUMN_ID_USUARIO = ?"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, arrayOf(idUsuario))
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
                    val precio = cursor.getInt(cursor.getColumnIndex(COLUMN_PRECIO))
                    val url = cursor.getInt(cursor.getColumnIndex(COLUMN_URL))
                    val unidades = cursor.getInt(cursor.getColumnIndex(COLUMN_UNIDADES))

                    articulos.add(
                        Articulo(
                            tipoArticulo,
                            nom,
                            peso,
                            precio,
                            url,
                            unidades,
                            Articulo.Rareza.COMUN
                        )
                    )

                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return articulos
        }

        @SuppressLint("Range")
        fun eliminarUnidad(articulo: Articulo) {
            val db = this.writableDatabase
            val whereClause =
                "$COLUMN_TIPO_ARTICULO = ? AND $COLUMN_NOMBRE = ? AND $COLUMN_URL = ? AND $COLUMN_PRECIO = ?"

            val whereArgs = arrayOf(
                articulo.getTipoArticulo().name,
                articulo.getNombre().name,
                articulo.getUrl().toString(),
                articulo.getPrecio().toString()
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
        fun eliminarArticulos(idUsuario:String) {
            val db = this.writableDatabase
            val whereClause = "$COLUMN_ID_USUARIO = ?"
            val whereArgs = arrayOf(idUsuario.toString())

            db.delete(TABLA_ARTICULOS, whereClause, whereArgs)
            db.close()
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
                                COLUMN_PRECIO
                            )
                        ) == articulo.getPrecio()
                    ) {
                        flag = true
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return flag
        }
    }
}
