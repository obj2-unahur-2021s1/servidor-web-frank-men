package ar.edu.unahur.obj2.servidorWeb


abstract class Analizador {
    abstract val modulos: MutableSet<Modulo>

    fun agregarModulo(modulo: Modulo){ modulos.add(modulo) }

    fun listaDeRespuestasDel(modulo: Modulo): List<Respuesta> {
        val listaRespuestas = mutableListOf<Respuesta>()
        if (modulos.contains(modulo)) { listaRespuestas.addAll(modulo.historialDeRespuestas) }
        return listaRespuestas
    }
    fun listaDeRespuestasTotal(): List<Respuesta>{
        val respuestasTotales = mutableListOf<Respuesta>()
        for(mod in modulos){respuestasTotales.addAll(listaDeRespuestasDel(mod))}
        return respuestasTotales
    }

    fun listaDePedidosDel(modulo: Modulo): List<Pedido>{
        val listaPedidos = mutableListOf<Pedido>()
        if (modulos.contains(modulo)) {
            for(resp in listaDeRespuestasDel(modulo)){ listaPedidos.add(resp.pedido) }
        }
        return listaPedidos
    }
    fun listaDePedidosTotal(): List<Pedido>{
        val pedidosTotales = mutableListOf<Pedido>()
        for(mod in modulos){pedidosTotales.addAll(listaDePedidosDel(mod))}
        return pedidosTotales
    }
}

class AnalizadorDemora(val demoraMinima: Int): Analizador(){
    override val modulos = mutableSetOf<Modulo>()

    fun demoraDe(modulo: Modulo): Int {
        val respuestasDelModulo = listaDeRespuestasDel(modulo)
        return respuestasDelModulo.count { resp -> resp.tiempo > demoraMinima }
    }
}

class AnalizadorIpSospechosa(val ipSospechosas: List<String>): Analizador(){
    override val modulos = mutableSetOf<Modulo>()
    val pedidos = mutableListOf<Pedido>()

    fun pedidosDeLaIpSospechosa(ipSospechosa: String) = (listaDePedidosTotal().count { p -> p.ip == ipSospechosa })

    fun pedidosDeLaIpAlModulo(modulo: Modulo,ipSospechosa: String): Int{
        var pedidos = 0
        if (modulos.contains(modulo)){
            val pedidosDelModulo = listaDePedidosDel(modulo)
            for (pedido in pedidosDelModulo){ if (pedido.ip == ipSospechosa){pedidos++} }
        }
        return pedidos }

    fun moduloMasConsultadoPorIpSospechosas(): Modulo{
        lateinit var moduloMasRequisitado: Modulo
        var pedidoMaximo = 0
        for(modulo in modulos){
            var pedidosAlModulo = 0
            //por cada ip: agregarle los pedidos de esa ip a ese modulo.
            for(ip in ipSospechosas) {
                pedidosAlModulo += pedidosDeLaIpAlModulo(modulo,ip)
            }
            //si la cantidad de pedidos al modulo es mayor a maxima actual, que el modulo mas
            // requisitado sea la de este, y que el pedido maximo sea la maxima actual.
            if(pedidosAlModulo > pedidoMaximo){moduloMasRequisitado = modulo;pedidoMaximo = pedidosAlModulo}
        }
        return moduloMasRequisitado
    }

    fun sospechosasEnRuta(ruta: String): List<String>{
        TODO("Todavia no")
    }
}
