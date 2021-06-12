package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({
  describe("Un servidor web") {
    val servidor = ServidorWeb()
    val pedido1 = Pedido("89.128.99.15","https://pepito.com.ar/documentos/doc1.html", LocalDateTime.now())
    val pedido2 = Pedido("89.12.88.125","http://pepito.com.ar/documentos/doc1.html", LocalDateTime.now())
    it("atiende pedidos"){
      servidor.atiende(pedido1).codigo.shouldBe(CodigoHttp.NOT_IMPLEMENTED)
      servidor.atiende(pedido2).codigo.shouldBe(CodigoHttp.OK)
    }
  }
})
