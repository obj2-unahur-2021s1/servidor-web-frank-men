package ar.edu.unahur.obj2.servidorWeb


abstract class Analizador {
    abstract val modulos: MutableList<Modulo>

    fun agregarModulo(modulo: Modulo){ modulos.add(modulo) }

}

class AnalizadorDemora(val demoraMinima: Int): Analizador(){
    override val modulos = mutableListOf<Modulo>()

    fun demoraDe(modulo: Modulo): Int {
        val listaDeRespuestas = modulo.historialDeRespuestas
        return listaDeRespuestas.count { resp -> resp.tiempo > demoraMinima }
    }
}

class AnalizadorIpSospechosa(val ipSospechosas: MutableList<String>): Analizador(){
    override val modulos = mutableListOf<Modulo>()

    fun pedidosDeLaIpSospechosa(ipSospechosas: Int): Int{
        TODO("Todavia no")
    }

    fun moduloMasConsultado(): Modulo{ //de TODAS las ip.
        TODO("Todavia no")
    }

    fun sospechosasEnRuta(ruta: String): List<String>{
        TODO("Todavia no")
    }
}
