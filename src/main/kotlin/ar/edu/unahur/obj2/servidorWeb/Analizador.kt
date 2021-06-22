package ar.edu.unahur.obj2.servidorWeb


abstract class Analizador {
    val modulos = mutableListOf<Modulo>()
    val respuestas = mutableListOf<Respuesta>()
    
    fun agregarModulo(modulo: Modulo){ modulos.add(modulo) }

    fun agregarRespuesta(respuesta: Respuesta){ respuestas.add(respuesta) }

    fun listaDeRespuestasDel(modulo: Modulo): List<Respuesta> {
        val listaRespuestas = mutableListOf<Respuesta>()
        if (modulos.contains(modulo)) { listaRespuestas.addAll(modulo.historialDeRespuestas) }
        return listaRespuestas
    }

    fun listaDePedidosTotal() = respuestas.map{it.pedido}
}

class AnalizadorDemora(val demoraMinima: Int): Analizador(){

    fun cantidadDeRespDemoradas(modulo: Modulo): Int {
        val respuestasDelModulo = listaDeRespuestasDel(modulo)
        return respuestasDelModulo.count { it.tiempo > demoraMinima }
    }
}

class AnalizadorIpSospechosa(): Analizador(){
    val ipSospechosas = mutableListOf<String>()

    fun pedidosDeIpSospechosa(ipSospechosa: String) = this.listaDePedidosTotal().count { it.ip == ipSospechosa}
    fun cantidadDePedidosDeModuloPorIpSospechosa(modulo: Modulo, ipSospechosa: String) =
        modulo.cantidadDeRespuestasParaIp(ipSospechosa)
    fun cantidadTotalparaModulo(modulo:Modulo) =
        ipSospechosas.sumBy { this.cantidadDePedidosDeModuloPorIpSospechosa(modulo,it) }


    fun moduloMasConsultadoPorIpSospechosas() = modulos.maxByOrNull { this.cantidadTotalparaModulo(it) }


}
