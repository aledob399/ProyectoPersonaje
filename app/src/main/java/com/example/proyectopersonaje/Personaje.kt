package com.example.proyectopersonaje

import android.os.Parcel
import android.os.Parcelable

class Personaje(
    private var nombre: String?,
    private val raza: Raza,
    private var clase: Clase,
    private var estadoVital: EstadoVital
) : Parcelable {
    private var salud: Int = 0
    private var ataque: Int = 0
    private var experiencia: Int
    private var nivel: Int
    private var suerte: Int
    private var defensa: Int = 0
    private val libro: Libro = Libro(4, 4, 4, 4) // 4 páginas de cada tipo de magia
    private var mana: Int = 0

    // Enumeración para el tipo de raza y clase
    enum class Raza { Humano, Elfo, Enano, Maldito }
    enum class Clase { Brujo, Mago, Guerrero }
    enum class EstadoVital{Anciano, Joven, Adulto}

    private val mochila = Mochila(10) // Ejemplo de peso máximo de la mochila
    // Atributos para el equipo del personaje
    private var arma: Articulo? = null
    private var proteccion: Articulo? = null


    // Inicialización de los atributos tras la construcción del objeto Personaje
    init {
        calcularSalud()
        calcularAtaque()
        calcularDefensa()
        calcularMana()
        experiencia = 0
        nivel = 1
        suerte = (0..10).random() // Asigna un valor de suerte aleatorio entre 0 y 10
    }

    fun getNombre(): String? {
        return nombre
    }
    fun setNombre(nuevoNombre: String) {
        nombre = nuevoNombre
    }
    fun getRaza(): Raza {
        return raza
    }
    fun getSalud(): Int {
        return salud
    }
    fun setSalud(nuevaSalud: Int) {
        salud = nuevaSalud
    }
    fun getAtaque(): Int {
        return ataque
    }
    fun setAtaque(nuevoAtaque: Int) {
        ataque = nuevoAtaque
    }
    fun getClase(): Clase {
        return clase
    }
    fun setClase(nuevaClase: Clase) {
        clase = nuevaClase
    }
    fun getEstadoVital(): EstadoVital {
        return estadoVital
    }
    fun setEstadoVital(nuevoEstadoVital: EstadoVital) {
        estadoVital = nuevoEstadoVital
    }

    fun getExperiencia(): Int {
        return experiencia
    }
    fun setExperiencia(experienciaGanada: Int) {
        experiencia += experienciaGanada
        while (experiencia >= 1000) {
            subirNivel()
            experiencia -= 1000 // Reducir la experiencia en 1000 al subir de nivel
        }
    }
    fun getNivel(): Int {
        return nivel
    }
    fun setNivel(nuevoNivel: Int) {
        nivel = nuevoNivel
    }
    fun getMana(): Int {
        return mana
    }

    fun setMana(nuevoMana: Int) {
        mana = nuevoMana
    }
    fun setM(nuevoMana: Int) {
        mana = nuevoMana
    }
    fun subirNivel() {
        if (nivel < 10) { // Limitar el nivel a 10
            nivel++
            calcularSalud() // Calcular el nuevo valor de salud al subir de nivel
            calcularAtaque() // Calcular el nuevo valor de ataque al subir de nivel
            calcularDefensa()
            calcularMana()
        }
    }
    private fun calcularSalud() {
        salud = when (nivel) {
            1 -> 100
            2 -> 200
            3 -> 300
            4 -> 450
            5 -> 600
            6 -> 800
            7 -> 1000
            8 -> 1250
            9 -> 1500
            10 -> 2000
            else -> 100 // Valor por defecto si el nivel está fuera del rango especificado
        }
    }

    private fun calcularAtaque() {
        ataque = when (nivel) {
            1 -> 10
            2 -> 20
            3 -> 25
            4 -> 30
            5 -> 40
            6 -> 100
            7 -> 200
            8 -> 350
            9 -> 400
            10 -> 450
            else -> 10 // Valor por defecto si el nivel está fuera del rango especificado
        }
    }
    private fun calcularDefensa() {
        defensa = when (nivel) {
            1 -> 4
            2 -> 9
            3 -> 14
            4 -> 19
            5 -> 49
            6 -> 59
            7 -> 119
            8 -> 199
            9 -> 349
            10 -> 399
            else -> 4 // Valor por defecto si el nivel está fuera del rango especificado
        }
    }
    private fun calcularMana() {
        mana = when (nivel) {
            1 -> 50
            2 -> 100
            3 -> 130
            4 -> 150
            5 -> 180
            6 -> 200
            7 -> 220
            8 -> 250
            9 -> 280
            10 -> 300
            else -> 50 // Valor por defecto si el nivel está fuera del rango especificado
        }
    }
    private fun calcularSaludMaxima(): Int {
        return when (nivel) {
            1 -> 100
            2 -> 200
            3 -> 300
            4 -> 450
            5 -> 600
            6 -> 800
            7 -> 1000
            8 -> 1250
            9 -> 1500
            10 -> 2000
            else -> 100 // Valor por defecto si el nivel está fuera del rango especificado
        }
    }
    fun getLibro(): Libro {
        return libro
    }
    fun getArma(): Articulo? {
        return arma
    }
    fun getProteccion(): Articulo? {
        return proteccion
    }


    fun usarMagia(nombre: Magia.Nombre): Int {
        val magiaEncontrada = libro.buscarMagia(nombre)
        if (magiaEncontrada != -1) {
            val magia = libro.getContenido()[magiaEncontrada]
            val tipoMagia = magia.getTipoMagia()
            val manaNecesario = magia.getMana()

            if (mana >= manaNecesario) {
                mana -= manaNecesario
                libro.getContenido().removeAt(magiaEncontrada)

                val resultado: Int = when (tipoMagia) {
                    Magia.TipoMagia.AIRE, Magia.TipoMagia.FUEGO, Magia.TipoMagia.TIERRA -> magia.getMagiaNegra()
                    Magia.TipoMagia.BLANCA -> {
                        salud += magia.getMagiaBlanca()
                        // Asegurar que la salud no supere el máximo según el nivel
                        if (salud > calcularSaludMaxima()) {
                            salud = calcularSaludMaxima()
                        }
                        0 // Devolver 0 porque no es un ataque
                    }
                    else -> -1 // Valor por defecto si no se puede usar la magia
                }

                if (resultado != -1) {
                    return resultado
                }
            } else {
                println("No tienes suficiente maná para utilizar esta magia.")
            }
        } else {
            println("No se encontró la magia en el grimorio.")
        }
        return 0 // Devolver 0 si no se pudo usar la magia
    }

    /*
        fun pelear(): Int {
            println("Turno del Personaje:")
            val nombreMagia = pedirNombreMagia()
            val incremento = usarMagia(nombreMagia)

            // Realizar ataque normal o incrementar la vida según el resultado de usarMagia
            if (incremento == 0) {
                // Ataque normal
                println("Atacando al Monstruo...")
                return ataque
            } else {
                // Incremento de vida
                println("Incrementando vida...")
                return incremento
            }
        }

     */







    fun habilidad() {
        when (clase) {
            Clase.Mago -> {
                calcularSalud() // Subir la salud al límite del nivel
                println("$nombre utiliza su habilidad de Mago para restaurar su salud.")
            }
            Clase.Brujo -> {
                ataque *= 2 // Duplicar el ataque
                println("$nombre utiliza su habilidad de Brujo para duplicar su ataque.")
            }
            Clase.Guerrero -> {
                suerte *= 2 // Duplicar la suerte
                println("$nombre utiliza su habilidad de Guerrero para duplicar su suerte.")
            }
        }
    }
    fun entrenar(tiempoDeEntrenamiento: Int) {
        val factorExperienciaPorHora = 5
        val experienciaGanada = tiempoDeEntrenamiento * factorExperienciaPorHora

        setExperiencia(experienciaGanada)

        println("$nombre ha entrenado durante $tiempoDeEntrenamiento horas y ha ganado $experienciaGanada de experiencia.")
    }
    fun realizarMision(tipoMision: String, dificultad: String) {
        val probabilidadExito = when (dificultad) {
            "Fácil" -> if (nivel >= 5) 8 else 6
            "Normal" -> if (nivel >= 3) 6 else 4
            "Difícil" -> if (nivel >= 7) 4 else 2
            else -> 0 // En caso de dificultad no reconocida
        }

        val resultado = (1..10).random() // Valor aleatorio entre 1 y 10

        if (resultado <= probabilidadExito) {
            val experienciaGanada = when (tipoMision) {
                "Caza" -> 1000
                "Búsqueda" -> 500
                "Asedio" -> 2000
                "Destrucción" -> 5000
                else -> 0 // En caso de tipo de misión no reconocido
            }

            val multiplicadorExperiencia = when (dificultad) {
                "Fácil" -> 0.5
                "Normal" -> 1.0
                "Difícil" -> 2.0
                else -> 0.0 // En caso de dificultad no reconocida
            }

            val experienciaFinal = (experienciaGanada * multiplicadorExperiencia).toInt()
            setExperiencia(experienciaFinal)
            println("$nombre ha completado la misión de $tipoMision ($dificultad) con éxito y gana $experienciaFinal de experiencia.")
        } else {
            println("$nombre ha fracasado en la misión de $tipoMision ($dificultad) y no recibe ninguna recompensa.")
        }
    }
    fun cifrado(mensaje : String, ROT : Int) : String{
        val abecedario : ArrayList<Char> = "abcdefghijklmnñopqrstuvwxyz".toList() as ArrayList<Char>
        var stringInv = ""
        var indice = 0

        for(i in mensaje.lowercase().toList() as ArrayList<Char>){
            if(!i.isLetter() || i.isWhitespace()){
                stringInv += i
            } else{
                indice = abecedario.indexOf(i) + ROT
                if (indice >= abecedario.size) {
                    indice -= abecedario.size
                    stringInv += abecedario[indice]
                } else
                    stringInv += abecedario[indice]
            }
        }
        return stringInv.filter { !it.isWhitespace() && it.isLetter() }
    }
    fun comunicacion(mensaje:String){
        var respuesta=""
        when(estadoVital){
            EstadoVital.Adulto->{
                if (mensaje.equals("¿Como estas?"))
                    respuesta="En la flor de la vida, pero me empieza a doler la espalda"
                else
                    if ((mensaje.contains('?') || mensaje.contains('¿')) && mensaje == mensaje.uppercase())
                        respuesta="Estoy buscando la mejor solución"
                    else
                        if (mensaje == mensaje.uppercase())
                            respuesta="No me levantes la voz mequetrefe"
                        else
                            if (mensaje == nombre)
                                respuesta="¿Necesitas algo?"
                            else
                                if(mensaje == "Tienes algo equipado?"){
                                    if (arma != null || proteccion != null) {
                                        val equipamiento = mutableListOf<String>()
                                        if (arma != null) {
                                            equipamiento.add(arma!!.getNombre().name)
                                        }
                                        if (proteccion != null) {
                                            equipamiento.add(proteccion!!.getNombre().name)
                                        }
                                        println("Tengo equipado: ${equipamiento.joinToString(", ")}")
                                    } else {
                                        println("Ligero como una pluma.")
                                    }
                                }
                                else
                                    respuesta="No sé de qué me estás hablando"
            }
            EstadoVital.Joven->{
                if (mensaje.equals("¿Como estas?"))
                    respuesta="De lujo"
                else
                    if ((mensaje.contains('?') || mensaje.contains('¿')) && mensaje == mensaje.uppercase())
                        respuesta="Tranqui se lo que hago"
                    else
                        if (mensaje == mensaje.uppercase())
                            respuesta="Eh relájate"
                        else
                            if (mensaje == nombre)
                                respuesta="Qué pasa?"
                            else
                                if(mensaje == "Tienes algo equipado?"){
                                    if (arma != null || proteccion != null) {
                                        println("No llevo nada, agente, se lo juro.")
                                    } else {
                                        println("Regístrame si quieres.")
                                    }
                                }
                                else
                                    respuesta="Yo que se"

            }
            EstadoVital.Anciano->{
                if (mensaje.equals("¿Como estas?"))
                    respuesta="No me puedo mover"
                else
                    if ((mensaje.contains('?') || mensaje.contains('¿')) && mensaje == mensaje.uppercase())
                        respuesta="Que no te escucho!"
                    else
                        if (mensaje == mensaje.uppercase())
                            respuesta="Háblame más alto que no te escucho"
                        else
                            if (mensaje == nombre)
                                respuesta="Las 5 de la tarde"
                            else
                                if(mensaje == "Tienes algo equipado?"){
                                    println("A ti que te importa nini!")
                                }
                                else
                                    respuesta="En mis tiempos esto no pasaba"
            }
        }
        when(raza){
            Raza.Elfo-> println(cifrado(respuesta, 1))
            Raza.Enano-> println(respuesta.uppercase())
            Raza.Maldito-> println(cifrado(respuesta, 1))
            else -> println(respuesta)
        }
    }
    fun equipar(articulo: Articulo) {
        when (articulo.getTipoArticulo()) {
            Articulo.TipoArticulo.ARMA -> {
                if (articulo.getNombre() in Articulo.Nombre.BASTON..Articulo.Nombre.GARRAS) {
                    arma = articulo
                    // Aumentar el ataque del personaje según el nombre del arma
                    ataque += articulo.getAumentoAtaque()
                    println("Has equipado el arma: $articulo")
                    mochila.getContenido().remove(articulo)
                } else {
                    println("No se puede equipar el artículo. Tipo de arma no válido.")
                }
            }
            Articulo.TipoArticulo.PROTECCION -> {
                when (articulo.getNombre()) {
                    Articulo.Nombre.ESCUDO, Articulo.Nombre.ARMADURA -> {
                        proteccion = articulo
                        // Aumentar la defensa del personaje solo si la protección es un escudo o una armadura
                        defensa += articulo.getAumentoDefensa()
                        println("Has equipado la protección: $articulo")
                        mochila.getContenido().remove(articulo)
                    }
                    else -> {
                        println("No se puede equipar el artículo. Tipo de protección no válido.")
                    }
                }
            }
            else -> {
                println("No se puede equipar el artículo. Tipo de artículo no válido.")
            }
        }
    }
    fun usarObjeto(articulo: Articulo) {
        when (articulo.getTipoArticulo()) {
            Articulo.TipoArticulo.OBJETO -> {
                when (articulo.getNombre()) {
                    Articulo.Nombre.POCION -> {
                        // Aumentar la vida del personaje al usar una poción
                        salud += articulo.getAumentoVida()
                        println("Has usado la poción y aumentado tu vida. Vida actual: $salud")
                        mochila.getContenido().remove(articulo)
                    }
                    Articulo.Nombre.IRA -> {
                        // Aumentar el ataque del personaje al usar un objeto de ira
                        ataque += articulo.getAumentoAtaque()
                        println("Has canalizado tu ira y aumentado tu ataque. Ataque actual: $ataque")
                        mochila.getContenido().remove(articulo)
                    }
                    else -> {
                        println("No se puede usar el objeto. Tipo de objeto no válido.")
                    }
                }
            }
            else -> {
                println("No se puede usar el artículo. Tipo de artículo no válido.")
            }
        }
    }
    fun misMonedas():Int{
        var monedas=0
        repeat(getMochila().getContenido().size){
            if(getMochila().getContenido()[it].getNombre()==Articulo.Nombre.MONEDA){
                monedas += getMochila().getContenido()[it].getPrecio()
            }
        }
        return monedas
    }
    fun restarMonedas(articuloComprar:Articulo){
        var moneda=Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,0,15,R.drawable.moneda,1,Articulo.Rareza.COMUN)
        var monedasGastar=articuloComprar.getPrecio()
        var monedas=misMonedas()

        if(monedas>monedasGastar){
            monedas -= monedasGastar
            removerArticuloNombre(Articulo.Nombre.MONEDA)
        }
        while(monedas>=15){
            if(monedas>=15){
                getMochila().addArticulo(moneda)
            }
            monedas -= 15
        }
        if(monedas>0){
            getMochila().addArticulo(Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,0,monedas,R.drawable.moneda,1,Articulo.Rareza.COMUN))
        }


    }
    fun removerArticuloNombre(nombre:Articulo.Nombre){
        repeat(getMochila().getContenido().size){
            if(getMochila().getContenido().get(it).getNombre()==nombre){
                getMochila().getContenido().removeAt(it)
            }
        }
    }
    fun getMochila(): Mochila {
        return this.mochila
    }

    override fun toString(): String {

        return "Personaje: Nombre: $nombre, Nivel: $nivel, Salud: $salud, Ataque: $ataque, Defensa: $defensa, Suerte: $suerte, Raza: $raza, Clase: $clase, Estado Vital: $estadoVital, Mana: $mana, Mochila: ${mochila.toString()}  Grimorio: $libro.toString()"
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readSerializable() as Raza,
        parcel.readSerializable() as Clase,
        parcel.readSerializable() as EstadoVital
    ) {
        salud = parcel.readInt()
        ataque = parcel.readInt()
        experiencia = parcel.readInt()
        nivel = parcel.readInt()
        suerte = parcel.readInt()
        defensa = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeSerializable(raza)
        parcel.writeSerializable(clase)
        parcel.writeSerializable(estadoVital)
        parcel.writeInt(salud)
        parcel.writeInt(ataque)
        parcel.writeInt(experiencia)
        parcel.writeInt(nivel)
        parcel.writeInt(suerte)
        parcel.writeInt(defensa)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Personaje> {
        override fun createFromParcel(parcel: Parcel): Personaje {
            return Personaje(parcel)
        }

        override fun newArray(size: Int): Array<Personaje?> {
            return arrayOfNulls(size)
        }
    }

}
/***********************************************************************************************************************
 *  CLASE: Mochila
 *  CONSTRUCTOR:
 *      pesoMochila      - > Peso máximo que puede soportar la mochila (Int)
 *
 *  METODOS
 *      getPesoMochila()        - > Devuelve el peso máximo como Int
 *      addArticulo()           - > Añade un artículo (clase Articulo) a la mochila, comprobando que el peso del
 *                                  artículo sumado al peso total del resto de artículos de la Mochila no supere el
 *                                  límite (pesoMochila)
 *      getContenido()          - > Devuelve el ArrayList de artículos que contiene la mochila
 *      findObjeto(nombre)      - > Devuelve la posición del artículo que cuyo nombre (Articulo.Nombre) pasamos como
 *                                  entrada o -1 si no lo encuentra
 *
 **********************************************************************************************************************/
class Mochila(private var pesoMochila: Int){
    private var contenido=ArrayList<Articulo>()

    fun getPesoMochila(): Int {
        var suma=0
        repeat(contenido.size){
            suma=contenido.get(it).getPeso()
        }
        return pesoMochila-suma
    }
    fun setContenido(nuevaMochila:ArrayList<Articulo>) {
        contenido = nuevaMochila
    }
    fun addArticulo(articulo: Articulo) {
        if (articulo.getPeso() <= pesoMochila) {
            when (articulo.getTipoArticulo()) {
                Articulo.TipoArticulo.ARMA -> {
                    when (articulo.getNombre()) {
                        Articulo.Nombre.BASTON, Articulo.Nombre.ESPADA, Articulo.Nombre.DAGA,
                        Articulo.Nombre.MARTILLO, Articulo.Nombre.GARRAS -> {
                            contenido.add(articulo)
                            this.pesoMochila -= articulo.getPeso()
                            println("${articulo.getNombre()} ha sido añadido a la mochila.")
                        }
                        else -> println("Nombre del artículo no válido para el tipo ARMA.")
                    }
                }
                Articulo.TipoArticulo.OBJETO -> {
                    when (articulo.getNombre()) {
                        Articulo.Nombre.POCION, Articulo.Nombre.IRA -> {
                            contenido.add(articulo)
                            this.pesoMochila -= articulo.getPeso()
                            println("${articulo.getNombre()} ha sido añadido a la mochila.")
                        }
                        else -> println("Nombre del artículo no válido para el tipo OBJETO.")
                    }
                }
                Articulo.TipoArticulo.PROTECCION -> {
                    when (articulo.getNombre()) {
                        Articulo.Nombre.ESCUDO, Articulo.Nombre.ARMADURA -> {
                            contenido.add(articulo)
                            this.pesoMochila -= articulo.getPeso()
                            println("${articulo.getNombre()} ha sido añadido a la mochila.")
                        }
                        else -> println("Nombre del artículo no válido para el tipo PROTECCION.")
                    }
                }

                else -> {}
            }
        } else {
            println("El peso del artículo excede el límite de la mochila.")
        }
    }
    fun getContenido(): ArrayList<Articulo> {
        if(contenido.isEmpty()){
            return ArrayList<Articulo>()
        }else return contenido

    }
    fun findObjeto(nombre: Articulo.Nombre): Int {
        return contenido.indexOfFirst { it.getNombre() == nombre }
    }
    override fun toString(): String {
        return if (contenido.isEmpty()) {
            "Mochila vacía"
        } else {
            "Artículos en la mochila: ${contenido.joinToString("\n")}"
        }
    }
}
/***********************************************************************************************************************
 *  CLASE: Articulo
 *  CONSTRUCTOR:
 *      tipoArticulo  - > Enumeración con valores ARMA, OBJETO, PROTECCION
 *      nombre        - > Enumeración con valores BASTON, ESPADA, DAGA, MARTILLO, GARRAS, POCION, IRA, ESCUDO, ARMADURA
 *      peso          - > Peso del artículo
 *
 *  METODOS
 *      getPeso()           - > Devuelve el peso como Int
 *      getNombre()         - > Devuelve el nombre del artículo
 *      getTipoArticulo()   - > Devuelve el tipo del artículo
 *      toString()          - > Sobreescribimos el método toString para darle formato a la visualización de los
 *                              artículos
 *      getAumentoAtaque()  - > Devuelve el aumento de ataque según el nombre del arma o si el objeto es IRA
 *      getAumentoDefensa() - > Devuelve el aumento de defensa según el nombre de la proteccion
 *      getAumentoVida()    - > Devuelve el aumento de vida si el objeto es POCION
 *
 *
 **********************************************************************************************************************/

class Articulo(
    private var tipoArticulo: TipoArticulo,
    private var nombre: Nombre,
    private var peso: Int,
    private var precio: Int,
    private var url: Int,
    private var unidades: Int,

    private var rareza: Rareza
) {

    enum class TipoArticulo { ARMA, OBJETO, PROTECCION, ORO }
    enum class Nombre { BASTON, ESPADA, DAGA, MARTILLO, GARRAS, POCION, IRA, ESCUDO, ARMADURA, MONEDA }
    enum class Rareza { COMUN, RARO, EPICO, LEGENDARIO }
    private var durabilidad: Int = 0
    init {
        durabilidad=when(rareza){
            Articulo.Rareza.COMUN->50
            Articulo.Rareza.RARO->100
            Articulo.Rareza.EPICO->150
            Articulo.Rareza.LEGENDARIO->200
            else -> 501
        }
    }
    fun getPeso(): Int {
        return peso
    }

    fun getPrecio(): Int {
        return precio
    }

    fun getNombre(): Nombre {
        return nombre
    }

    fun getUrl(): Int {
        return url
    }

    fun getTipoArticulo(): TipoArticulo {
        return tipoArticulo
    }

    fun getUnidades(): Int {
        return unidades
    }

    fun getDurabilidad(): Int {
        return durabilidad
    }

    fun setDurabilidad(durabilidad: Int) {
        this.durabilidad = durabilidad
    }

    fun getRareza(): Rareza {
        return rareza
    }

    fun setRareza(rareza: Rareza) {
        this.rareza = rareza
    }

    fun getAumentoAtaque(): Int {
        return when (nombre) {
            Nombre.BASTON -> 10
            Nombre.ESPADA -> 20
            Nombre.DAGA -> 15
            Nombre.MARTILLO -> 25
            Nombre.GARRAS -> 30
            Nombre.IRA -> 80
            else -> 0 // Para otros tipos de armas no especificados
        }
    }

    fun getAumentoDefensa(): Int {
        return when (nombre) {
            Nombre.ESCUDO -> 10
            Nombre.ARMADURA -> 20
            else -> 0 // Para otros tipos de protecciones no especificados
        }
    }

    fun getAumentoVida(): Int {
        return when (nombre) {
            Nombre.POCION -> 100
            else -> 0 // Para otros tipos de objetos no especificados
        }
    }

    override fun toString(): String {
        return "[Tipo Artículo:$tipoArticulo, Nombre:$nombre, Peso:$peso, Rareza:$rareza]"
    }
}

/***********************************************************************************************************************
 *  CLASE: Mascota
 *  CONSTRUCTOR:
 *      nombre      - > Nombre de tu mascota (String)
 *      tipo        - >(Enum{Fuego,Agua,Planta,Luz,Oscuridad})
 *
 *  METODOS
 *      getPesoMochila()        - > Devuelve el peso máximo como Int
 *      addArticulo()           - > Añade un artículo (clase Articulo) a la mochila, comprobando que el peso del
 *                                  artículo sumado al peso total del resto de artículos de la Mochila no supere el
 *                                  límite (pesoMochila)
 *      getContenido()          - > Devuelve el ArrayList de artículos que contiene la mochila
 *      findObjeto(nombre)      - > Devuelve la posición del artículo que cuyo nombre (Articulo.Nombre) pasamos como
 *                                  entrada o -1 si no lo encuentra
 *
 **********************************************************************************************************************/


class Mascota(
    private var nombre:String?,
    private var atributo: tipoMascota
) : Parcelable {
    enum class tipoMascota { FUEGO, AGUA, PLANTA, LUZ, OSCURIDAD }

    private var salud: Int = 0
    private var ataque: Int = 0
    private var experiencia: Int = 0
    private var nivel: Int = 1
    private var potencial: Int = (1..20).random()
    private var defensa: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        tipoMascota.valueOf(parcel.readString() ?: "FUEGO")
    ) {
        salud = parcel.readInt()
        ataque = parcel.readInt()
        experiencia = parcel.readInt()
        nivel = parcel.readInt()
        potencial = parcel.readInt()
        defensa = parcel.readInt()
    }

    init {
        calcularSalud()
        calcularAtaque()
        calcularDefensa()
    }

    fun subirNivel() {
        if (nivel < 10) {
            nivel++
            calcularSalud()
            calcularAtaque()
            calcularDefensa()
        }
    }

    private fun calcularSalud() {
        salud = when (nivel) {
            1 -> 100
            2 -> 200
            3 -> 300
            4 -> 450
            5 -> 600
            6 -> 800
            7 -> 1000
            8 -> 1250
            9 -> 1500
            10 -> 2000
            else -> 100
        }
        salud *= potencial
    }
    override fun toString(): String {
        return "Mascota(nombre=$nombre, atributo=$atributo, salud=$salud, ataque=$ataque, experiencia=$experiencia, nivel=$nivel, potencial=$potencial, defensa=$defensa)"
    }

    private fun calcularAtaque() {
        ataque = when (nivel) {
            1 -> 10
            2 -> 20
            3 -> 25
            4 -> 30
            5 -> 40
            6 -> 100
            7 -> 200
            8 -> 350
            9 -> 400
            10 -> 450
            else -> 10
        }
        ataque *= potencial
    }

    private fun calcularDefensa() {
        defensa = when (nivel) {
            1 -> 4
            2 -> 9
            3 -> 14
            4 -> 19
            5 -> 49
            6 -> 59
            7 -> 119
            8 -> 199
            9 -> 349
            10 -> 399
            else -> 4
        }
        defensa *= potencial
    }

    // Getters para las propiedades privadas
    fun getNombre(): String? {
        return nombre
    }

    fun getAtributo(): tipoMascota {
        return atributo
    }

    fun getSalud(): Int {
        return salud
    }

    fun getAtaque(): Int {
        return ataque
    }

    fun getExperiencia(): Int {
        return experiencia
    }

    fun getNivel(): Int {
        return nivel
    }

    fun getPotencial(): Int {
        return potencial
    }

    fun getDefensa(): Int {
        return defensa
    }

    fun setNivel(newNivel: Int) {
        nivel = newNivel
        calcularSalud() // Recalcular la salud cuando se cambia el nivel
        calcularAtaque() // Recalcular el ataque cuando se cambia el nivel
        calcularDefensa() // Recalcular la defensa cuando se cambia el nivel
    }

    fun setPotencial(newPotencial: Int) {
        potencial = newPotencial
        calcularSalud() // Recalcular la salud cuando se cambia el potencial
        calcularAtaque() // Recalcular el ataque cuando se cambia el potencial
        calcularDefensa() // Recalcular la defensa cuando se cambia el potencial
    }

    fun setExperiencia(newExperiencia: Int) {
        experiencia = newExperiencia
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(atributo.name)
        parcel.writeInt(salud)
        parcel.writeInt(ataque)
        parcel.writeInt(experiencia)
        parcel.writeInt(nivel)
        parcel.writeInt(potencial)
        parcel.writeInt(defensa)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mascota> {
        override fun createFromParcel(parcel: Parcel): Mascota {
            return Mascota(parcel)
        }

        override fun newArray(size: Int): Array<Mascota?> {
            return arrayOfNulls(size)
        }
    }
}



class Mazmorra(
    private var dificultad: Int,
    private var condicion: TipoCondicion
) : Parcelable {
    enum class TipoCondicion { MENOSVIDA, MENOSATAQUE }
    enum class TipoMaldicion { VIDA, ATAQUE, DEFENSA, MASCOTA }
    enum class TipoBendicion { VIDA, ATAQUE, DEFENSA, ATAQUEDOBLE }

    private var bendiciones: ArrayList<TipoBendicion> = ArrayList()
    private var maldiciones: ArrayList<TipoMaldicion> = ArrayList()
    private var enemigos: ArrayList<Enemigo> = ArrayList()

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readSerializable() as TipoCondicion
    ) {
        parcel.readList(bendiciones, TipoBendicion::class.java.classLoader)
        parcel.readList(maldiciones, TipoMaldicion::class.java.classLoader)
        parcel.readList(enemigos, Enemigo::class.java.classLoader)
    }

    init{
        repeat((1..3).random()*dificultad){
            enemigos.add(Enemigo((1..10).random()))
        }
    }

    fun agregarMaldicion(maldicion: TipoMaldicion) {
        maldiciones.add(maldicion)
    }

    fun agregarBendicion(bendicion: TipoBendicion) {
        bendiciones.add(bendicion)
    }

    fun getDificultad(): Int {
        return dificultad
    }

    fun getCondicion(): TipoCondicion {
        return condicion
    }

    fun getEnemigos(): ArrayList<Enemigo> {
        return enemigos
    }

    fun obtenerMaldiciones(): ArrayList<TipoMaldicion> {
        return maldiciones
    }

    fun obtenerBendiciones(): ArrayList<TipoBendicion> {
        return bendiciones
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(dificultad)
        parcel.writeSerializable(condicion)
        parcel.writeList(bendiciones)
        parcel.writeList(maldiciones)
        parcel.writeList(enemigos)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mazmorra> {
        override fun createFromParcel(parcel: Parcel): Mazmorra {
            return Mazmorra(parcel)
        }

        override fun newArray(size: Int): Array<Mazmorra?> {
            return arrayOfNulls(size)
        }
    }
}




class Enemigo(private var nivel: Int) : Parcelable {
    private var salud: Int = 0
    private var ataque: Int = 0
    private var defensa: Int = 0
    private var potencial: Int = 0

    constructor(parcel: Parcel) : this(parcel.readInt()) {
        salud = parcel.readInt()
        ataque = parcel.readInt()
        defensa = parcel.readInt()
        potencial = parcel.readInt()
    }

    init {
        potencial = (1..10).random()
        calcularSalud()
        calcularAtaque()
        calcularDefensa()
    }

    private fun calcularSalud() {
        salud = when (nivel) {
            1 -> 10
            2 -> 20
            3 -> 30
            4 -> 45
            5 -> 60
            6 -> 80
            7 -> 100
            8 -> 125
            9 -> 150
            10 -> 200
            else -> 10
        }
        salud *= potencial
    }

    private fun calcularAtaque() {
        ataque = when (nivel) {
            1 -> 1
            2 -> 2
            3 -> 2
            4 -> 3
            5 -> 4
            6 -> 10
            7 -> 20
            8 -> 35
            9 -> 40
            10 -> 450
            else -> 10
        }
        ataque *= potencial
    }

    private fun calcularDefensa() {
        defensa = when (nivel) {
            1 -> 4
            2 -> 9
            3 -> 14
            4 -> 19
            5 -> 49
            6 -> 59
            7 -> 119
            8 -> 199
            9 -> 349
            10 -> 399
            else -> 4
        }
        defensa *= potencial
    }

    // Getters y Setters
    fun getNivel(): Int {
        return nivel
    }

    fun getSalud(): Int {
        return salud
    }

    fun getAtaque(): Int {
        return ataque
    }

    fun getDefensa(): Int {
        return defensa
    }

    fun getPotencial(): Int {
        return potencial
    }

    fun setNivel(nuevoNivel: Int) {
        nivel = nuevoNivel
        calcularSalud()
        calcularAtaque()
        calcularDefensa()
    }

    fun setPotencial(nuevoPotencial: Int) {
        potencial = nuevoPotencial
        calcularSalud()
        calcularAtaque()
        calcularDefensa()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(nivel)
        parcel.writeInt(salud)
        parcel.writeInt(ataque)
        parcel.writeInt(defensa)
        parcel.writeInt(potencial)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Enemigo> {
        override fun createFromParcel(parcel: Parcel): Enemigo {
            return Enemigo(parcel)
        }

        override fun newArray(size: Int): Array<Enemigo?> {
            return arrayOfNulls(size)
        }
    }
}



class Magia(private val tipoMagia: TipoMagia, private val nombre: Nombre, private val mana: Int) {
    enum class TipoMagia {
        AIRE,
        FUEGO,
        TIERRA,
        BLANCA
    }

    enum class Nombre {
        TORNADO,
        VENDAVAL,
        HURACAN,
        ALIENTO,
        DESCARGA,
        PROPULSION,
        FATUO,
        MURO,
        TEMBLOR,
        TERREMOTO,
        SANAR,
        CURAR
    }
    fun getMana(): Int {
        return mana
    }

    fun getNombre(): Nombre {
        return nombre
    }

    fun getTipoMagia(): TipoMagia {
        return tipoMagia
    }

    fun getMagiaNegra(): Int {
        var valorAtaque = 0
        when (nombre) {
            Nombre.TORNADO -> if (tipoMagia == TipoMagia.AIRE) valorAtaque = 100
            Nombre.VENDAVAL -> if (tipoMagia == TipoMagia.AIRE) valorAtaque = 70
            Nombre.HURACAN -> if (tipoMagia == TipoMagia.AIRE) valorAtaque = 150
            Nombre.ALIENTO -> if (tipoMagia == TipoMagia.FUEGO) valorAtaque = 50
            Nombre.DESCARGA -> if (tipoMagia == TipoMagia.FUEGO) valorAtaque = 70
            Nombre.PROPULSION -> if (tipoMagia == TipoMagia.FUEGO) valorAtaque = 100
            Nombre.FATUO -> if (tipoMagia == TipoMagia.FUEGO) valorAtaque = 200
            Nombre.MURO -> if (tipoMagia == TipoMagia.TIERRA) valorAtaque = 20
            Nombre.TEMBLOR -> if (tipoMagia == TipoMagia.TIERRA) valorAtaque = 80
            Nombre.TERREMOTO -> if (tipoMagia == TipoMagia.TIERRA) valorAtaque = 300
            else -> {valorAtaque = 300}
        }
        return valorAtaque
    }

    fun getMagiaBlanca(): Int {
        var aumentoVida = 0
        when (nombre) {
            Nombre.SANAR -> if (tipoMagia == TipoMagia.BLANCA) aumentoVida = 100
            Nombre.CURAR -> if (tipoMagia == TipoMagia.BLANCA) aumentoVida = 200
            else -> {aumentoVida = 200}
        }
        return aumentoVida
    }

    override fun toString(): String {
        return "Magia: ${nombre.name} - Tipo: ${tipoMagia.name} - Mana: $mana"
    }
}

class Libro(
    private val paginasLibroVerde: Int,
    private val paginasLibroRojo: Int,
    private val paginasLibroMarron: Int,
    private val paginasLibroBlanco: Int
) {
    private var contenido: ArrayList<Magia> = ArrayList()

    fun getPaginasGrimorioVerde(): Double {
        return (paginasLibroVerde * 2 - contenido.count { it.getTipoMagia() == Magia.TipoMagia.AIRE }).toDouble()
    }

    fun getPaginasGrimorioRojo(): Double {
        return (paginasLibroRojo * 2 - contenido.count { it.getTipoMagia() == Magia.TipoMagia.FUEGO }).toDouble()
    }

    fun getPaginasGrimorioMarron(): Double {
        return (paginasLibroMarron * 2 - contenido.count { it.getTipoMagia() == Magia.TipoMagia.TIERRA }).toDouble()
    }

    fun getPaginasGrimorioBlanco(): Double {
        return (paginasLibroBlanco * 2 - contenido.count { it.getTipoMagia() == Magia.TipoMagia.BLANCA }).toDouble()
    }

    fun aprenderMagia(nuevaMagia: Magia) {
        val tipoMagia = nuevaMagia.getTipoMagia()
        val nombreMagia = nuevaMagia.getNombre()

        val paginasDisponibles = when (tipoMagia) {
            Magia.TipoMagia.AIRE -> getPaginasGrimorioVerde()
            Magia.TipoMagia.FUEGO -> getPaginasGrimorioRojo()
            Magia.TipoMagia.TIERRA -> getPaginasGrimorioMarron()
            Magia.TipoMagia.BLANCA -> getPaginasGrimorioBlanco()
        }

        if (paginasDisponibles >= 1 && tipoMagia == Magia.TipoMagia.BLANCA &&
            (nombreMagia == Magia.Nombre.SANAR || nombreMagia == Magia.Nombre.CURAR)) {
            contenido.add(nuevaMagia)
            println("¡Magia aprendida y añadida al grimorio!")
        } else {
            println("No se puede aprender la magia o no hay suficientes páginas disponibles en el grimorio.")
        }
    }

    fun getContenido(): ArrayList<Magia> {
        return contenido
    }
    fun setContenido(nuevoContenido:ArrayList<Magia>) {
        contenido=nuevoContenido
    }


    fun buscarMagia(nombre: Magia.Nombre): Int {
        return contenido.indexOfFirst { it.getNombre() == nombre }
    }

    override fun toString(): String {
        val contenidoString = StringBuilder()
        contenidoString.append("Contenido del Grimorio:\n")
        contenido.forEachIndexed { index, magia ->
            contenidoString.append("${index + 1}. ${magia.getNombre()} - Tipo: ${magia.getTipoMagia()} - Mana: ${magia.getMana()}\n")
        }
        return contenidoString.toString()
    }
}



