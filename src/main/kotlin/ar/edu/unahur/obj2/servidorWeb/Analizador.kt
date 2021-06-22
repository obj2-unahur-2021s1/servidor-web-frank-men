package ar.edu.unahur.obj2.servidorWeb


/*se agregan al servidor.
son una mutablelist dentro del Sv
por cada pedido del servidor, le manda respuesta y modulo al analizador.
despues cada analizador maneja la respuesta como quiere.
.*/

abstract class Analizador(){
    abstract val modulos: MutableList<Modulo>

    fun agregarModulo(modulo: Modulo){ modulos.add(modulo) }

    //abstract fun analizar(modulo: Modulo)
}

class AnalizadorDemora(val demoraMinima: Int): Analizador(){
    override val modulos = mutableListOf<Modulo>()

    fun demoraDe(modulo: Modulo): Int {
        val listaDeRespuestas = modulo.historialDeRespuestas
        return listaDeRespuestas.count { resp -> resp.tiempo > demoraMinima }
    }
}
