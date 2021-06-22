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
  val moduloTexto = Modulo("modulo texto",5, listOf("docx","odt"))
  val moduloImagen = Modulo("modulo imagen",5, listOf("jpg","png","gif"))
  describe("Un pedido:"){
    it("Tiene protocolo:"){
      val pedidoHTML = Pedido("127.0.0.1","http://pepito.com.ar/documentos/doc1.html", LocalDateTime.now())
      pedidoHTML.protocolo().shouldBe("http")
    }
    it("Tiene ruta:"){
      val pedidoHTML = Pedido("127.0.0.1","http://pepito.com.ar/documentos/doc1.html", LocalDateTime.now())
      pedidoHTML.ruta().shouldBe("/documentos/doc1.html")
    }
    it("Tiene extension:"){
      val pedidoHTML = Pedido("127.0.0.1","http://pepito.com.ar/documentos/doc1.html", LocalDateTime.now())
      pedidoHTML.extension().shouldBe("html")
    }
  }
  describe("Un servidor web") {
    it("puede atender pedidos"){
      servidor.puedeAtender(pedido1).shouldBe(false)
      servidor.puedeAtender(pedido2).shouldBe(true)
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
      it("Servidor atender pedidos"){
        servidor.hayModuloPara(pedido2).shouldBeTrue()
        servidor.atender(pedido2).codigo.shouldBe(CodigoHttp.OK)
        servidor.atender(pedido2).body.shouldBe("modulo texto")
        servidor.atender(pedido2).tiempo.shouldBe(5)
        servidor.atender(pedido2).pedido.shouldBe(pedido2)
      }
    }
  }
})
