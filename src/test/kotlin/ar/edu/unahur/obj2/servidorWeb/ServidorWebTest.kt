package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
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
    it("puede atender pedidos"){
      servidor.puedeAtender(pedido1).shouldBe(CodigoHttp.NOT_IMPLEMENTED)
      servidor.puedeAtender(pedido2).shouldBe(CodigoHttp.OK)
    }
  }
  describe("Modulos del servidor"){
    describe("Agrgar Modulo al servidor"){
      servidor.agregarModulo(moduloTexto)
      servidor.agregarModulo(moduloImagen)
      servidor.modulosEstablecidos.size.shouldBe(2)
      it("modulo soporta extension"){
        moduloTexto.soporta("docx").shouldBeTrue()
      }
      it("Servidor atiende pedidos"){
        servidor.hayModuloPara(pedido2).shouldBeTrue()
        servidor.atiende(pedido2).codigo.shouldBe(CodigoHttp.OK)
        servidor.atiende(pedido2).body.shouldBe("modulo texto")
        servidor.atiende(pedido2).tiempo.shouldBe(5)
        servidor.atiende(pedido2).pedido.shouldBe(pedido2)
      }
    }
  }
})
