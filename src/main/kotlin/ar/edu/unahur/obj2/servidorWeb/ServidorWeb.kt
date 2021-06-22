package ar.edu.unahur.obj2.servidorWeb

import ar.edu.unahur.obj2.servidorWeb.CodigoHttp.NOT_FOUND
import ar.edu.unahur.obj2.servidorWeb.CodigoHttp.OK
import java.time.LocalDateTime

// Para no tener los códigos "tirados por ahí", usamos un enum que le da el nombre que corresponde a cada código
// La idea de las clases enumeradas es usar directamente sus objetos: CodigoHTTP.OK, CodigoHTTP.NOT_IMPLEMENTED, etc
enum class CodigoHttp(val codigo: Int) {
  OK(200),
  NOT_IMPLEMENTED(501),
  NOT_FOUND(404),
}

class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime){
  fun protocolo() = url.substringBefore(":")
  fun extension() = url.substringAfterLast(".")
  fun ruta(): String {
    val urlSeparated = url.split("/").toMutableList()
    var i = 0
    while (i != 3){ urlSeparated.removeFirst();i++}
    return "/"+urlSeparated.joinToString("/")
  }
}


class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido)

class ServidorWeb(val body: String = "",val tiempo: Int = 10){
  val modulosEstablecidos = mutableListOf<Modulo>()
  val analizadoresEstablecidos = mutableListOf<Analizador>()


  fun agregarModulo(modulo: Modulo) = modulosEstablecidos.add(modulo)
  fun agregarAnalizador(analizador: Analizador) = analizadoresEstablecidos.add(analizador)

  fun puedeAtender(pedido: Pedido) = pedido.protocolo() == "http"


  fun atender(pedido: Pedido): Respuesta {
    var respuesta = Respuesta(NOT_FOUND,"",10,pedido) // es lo que debe devolver cuando hay error
    val modulo = this.moduloPara(pedido)!!
    if(this.hayModuloPara(pedido)){
      respuesta = Respuesta(OK,modulo.body,modulo.tiempo,pedido)
      modulo.agregaRespuesta(respuesta)
    }
    if(hayAnalizadores()){
      analizadoresEstablecidos.map { it.agregarModulo(modulo) }
      analizadoresEstablecidos.map { it.agregarRespuesta(respuesta) }
    }
    return respuesta
  }

  fun hayAnalizadores() = this.analizadoresEstablecidos.isNotEmpty()

  fun hayModuloPara(pedido: Pedido) = this.moduloPara(pedido)!= null

  fun moduloPara(pedido: Pedido) = modulosEstablecidos.find { it.soporta(pedido.extension()) }


}
class Modulo(val body: String, val tiempo: Int,val extensionSoportada: List<String>){
  val historialDeRespuestas = mutableListOf<Respuesta>()
  fun agregaRespuesta(respuesta: Respuesta) = historialDeRespuestas.add(respuesta)
  fun soporta(extension: String) = extensionSoportada.contains(extension)
  fun cantidadDeRespuestasParaIp(ip: String) = pedidosDelModulo().count { it.ip == ip }
  fun pedidosDelModulo() = historialDeRespuestas.map{it.pedido}

}