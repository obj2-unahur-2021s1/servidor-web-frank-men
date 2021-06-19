package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({
  val servidor = ServidorWeb()
  val pedido1 = Pedido("89.128.99.15","https://pepito.com.ar/documentos/doc1.html", LocalDateTime.now())
  val pedido2 = Pedido("89.12.88.125","http://pepito.com.ar/documentos/doc1.docx", LocalDateTime.now())
  val pedido3 = Pedido("89.128.99.14","https://pepito.com.ar/documentos/doc1.html", LocalDateTime.now())
  val moduloTexto = Modulo("modulo texto",5)
  moduloTexto.agragaExtension("docx")
  moduloTexto.agragaExtension("odt")
  val moduloImagen = Modulo("modulo imagen",5)
  moduloImagen.agragaExtension("jpg")
  moduloImagen.agragaExtension("png")
  moduloImagen.agragaExtension("gif")
  describe("Un servidor web") {
    it("atiende pedidos"){
      servidor.atiende(pedido1).shouldBe(CodigoHttp.NOT_IMPLEMENTED)
      servidor.atiende(pedido2).shouldBe(CodigoHttp.OK)
    }
  }
  describe("Agregar Modulos al servidor"){
    it("Agrgar Modulo"){
      servidor.modulosEstablecidos.add(moduloTexto)
      servidor.modulosEstablecidos.add(moduloImagen)
      servidor.modulosEstablecidos.size.shouldBe(2)
    }

  }
})
