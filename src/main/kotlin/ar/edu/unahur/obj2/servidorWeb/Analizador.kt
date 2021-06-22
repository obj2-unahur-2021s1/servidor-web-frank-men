package ar.edu.unahur.obj2.servidorWeb


abstract class Analizador {
    abstract val modulos: MutableSet<Modulo>

    fun agregarModulo(modulo: Modulo){ modulos.add(modulo) }

    fun listaDeRespuestas(): List<Respuesta>{
        val listaRespuestas = mutableListOf<Respuesta>()
        for (mod in modulos){ listaRespuestas.addAll(mod.historialDeRespuestas) }
        return listaRespuestas
    }

    fun listaDePedidos(): List<Pedido>{
        val listaPedidos = mutableListOf<Pedido>()
        for (res in listaDeRespuestas()){ listaPedidos.add(res.pedido) }
        return listaPedidos
    }
}

class AnalizadorDemora(val demoraMinima: Int): Analizador(){
    override val modulos = mutableSetOf<Modulo>()

    fun demoraDe(modulo: Modulo): Int {
        val respuestasDelModulo = modulo.historialDeRespuestas
        return respuestasDelModulo.count { resp -> resp.tiempo > demoraMinima }
    }
}

class AnalizadorIpSospechosa(val ipSospechosas: List<String>): Analizador(){
    override val modulos = mutableSetOf<Modulo>()
    val pedidos = mutableListOf<Pedido>()

    fun pedidosDeLaIpSospechosa(ipSospechosa: String) = (listaDePedidos().count { p -> p.ip == ipSospechosa })

    fun moduloMasConsultado(): Modulo{ //de TODAS las ip.
        TODO("Todavia no")
    }

    fun sospechosasEnRuta(ruta: String): List<String>{
        TODO("Todavia no")
    }
}
