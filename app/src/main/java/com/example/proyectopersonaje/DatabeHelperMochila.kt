package com.example.proyectopersonaje
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelperMochila(context: Context) : SQLiteOpenHelper(context, DATABASE, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE = "OBJETOS_MOCHILA.db"
        private const val TABLA_MOCHILA = "mochila"
        private const val COLUMN_ID_USUARIO_AUTH = "idUsuarioAuth"
        private const val COLUMN_ARTICULO_URL = "url"
        private const val COLUMN_ARTICULO_PRECIO = "precio"
        private const val COLUMN_ARTICULO_NOMBRE = "nombre"
        private const val COLUMN_ARTICULO_UNIDADES = "unidades"
        private const val COLUMN_ARTICULO_PESO = "peso"
        private const val COLUMN_ARTICULO_TIPO = "tipo"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLA_MOCHILA ($COLUMN_ID_USUARIO_AUTH TEXT, $COLUMN_ARTICULO_NOMBRE TEXT, $COLUMN_ARTICULO_UNIDADES INTEGER,$COLUMN_ARTICULO_PESO INTEGER,$COLUMN_ARTICULO_TIPO TEXT,$COLUMN_ARTICULO_URL , PRIMARY KEY (ID_USUARIO_AUTH, COLUMN_ARTICULO_NOMBRE))"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLA_MOCHILA")
        onCreate(db)
    }

    fun insertarContenido(mochila: Mochila, uid:String) {
        val db = this.writableDatabase
        val c = mochila.getContenido()
        for (i in 0..c.size){
            val values = ContentValues().apply {
                put(COLUMN_ID_USUARIO_AUTH, uid)
                put(COLUMN_ARTICULO_NOMBRE, c[i].getNombre().name)
                put(COLUMN_ARTICULO_TIPO, c[i].getTipoArticulo().name)
                put(COLUMN_ARTICULO_UNIDADES, c[i].getUnidades())
                put(COLUMN_ARTICULO_URL, c[i].getUnidades())
                put(COLUMN_ARTICULO_PRECIO, c[i].getPrecio())
                put(COLUMN_ARTICULO_PESO, c[i].getPeso())
            }
        }
        db.close()
    }
    @SuppressLint("Range")
    fun getContenidoMochila(uid: String):ArrayList<Articulo>{
        val selectQuery = "SELECT * FROM $TABLA_MOCHILA WHERE $COLUMN_ID_USUARIO_AUTH=$uid"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        val contenido =ArrayList<Articulo>()
        if (cursor.moveToFirst()) {
            do {
                val nombre = obtenerNombreArticuloEnum(cursor.getString(cursor.getColumnIndex(COLUMN_ARTICULO_NOMBRE)))
                val tipo=obtenerTipoArticuloEnum(cursor.getString(cursor.getColumnIndex(COLUMN_ARTICULO_TIPO)))
                val unidades = cursor.getInt(cursor.getColumnIndex(COLUMN_ARTICULO_UNIDADES))
                val peso = cursor.getInt(cursor.getColumnIndex(COLUMN_ARTICULO_PESO))
                val precio = cursor.getInt(cursor.getColumnIndex(COLUMN_ARTICULO_PRECIO))
                val url = cursor.getInt(cursor.getColumnIndex(COLUMN_ARTICULO_URL))
               // val arti = Articulo(tipo,nombre,peso,precio,url,unidades)
              //  contenido.add(arti)

            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return contenido
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
}