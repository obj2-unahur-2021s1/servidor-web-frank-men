package ar.edu.unahur.obj2.servidorWeb

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
}
class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido)

class ServidorWeb{
  val modulosEstablecidos = mutableListOf<Modulo>()
  fun atiende(pedido: Pedido): CodigoHttp {
    var codigo : CodigoHttp = CodigoHttp.OK
    if(pedido.protocolo() != "http"){
      codigo = CodigoHttp.NOT_IMPLEMENTED
    }
    return codigo
  }

}
class Modulo(val body: String, val tiempo: Int){
  val extensionSoportada = mutableListOf<String>()
  fun agragaExtension(extension: String) = extensionSoportada.add(extension)
  fun soporta(extension: String) = extensionSoportada.contains(extension)

}