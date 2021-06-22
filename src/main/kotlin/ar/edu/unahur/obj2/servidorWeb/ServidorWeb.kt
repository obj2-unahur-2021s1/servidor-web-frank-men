package ar.edu.unahur.obj2.servidorWeb

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

  fun ruta(): String { //para "zafar".
    val urlSeparated = url.split("/").toMutableList()
    var i = 3
    while (i != 0){ urlSeparated.removeFirst();i--}
    return "/"+urlSeparated.joinToString("/")
  }
}



class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido)

class ServidorWeb(val body: String = "",val tiempo: Int = 10){
  val modulosEstablecidos = mutableListOf<Modulo>()

  fun agregarModulo(modulo: Modulo) = modulosEstablecidos.add(modulo)

  fun puedeAtender(pedido: Pedido) = pedido.protocolo() == "http"

  fun atender(pedido: Pedido): Respuesta {
    val codigo = CodigoHttp.NOT_FOUND
    val respuesta = Respuesta(codigo,body,tiempo,pedido)
    if(this.hayModuloPara(pedido)){
      val modulo = this.moduloPara(pedido)!!
      return Respuesta(OK,modulo.body,modulo.tiempo,pedido)
    }
    return respuesta
  }

  fun hayModuloPara(pedido: Pedido) = this.moduloPara(pedido)!= null

  fun moduloPara(pedido: Pedido) = modulosEstablecidos.find { it.soporta(pedido.extension()) }


}
class Modulo(val body: String, val tiempo: Int,val extensionSoportada: List<String>){
  fun soporta(extension: String) = extensionSoportada.contains(extension)

}