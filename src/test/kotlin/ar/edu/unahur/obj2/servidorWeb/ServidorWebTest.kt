package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({
  val fecha = LocalDateTime.of(2021,6,22,2,15)
  val servidor = ServidorWeb()
  val pedido1 = Pedido("89.128.99.15","https://pepito.com.ar/documentos/doc1.html", fecha)
  val pedido2 = Pedido("89.12.88.125","http://pepito.com.ar/documentos/doc1.docx", fecha)
  val pedido3 = Pedido("89.128.99.14","https://pepito.com.ar/documentos/doc1.html", fecha)
  val moduloTexto = Modulo("modulo texto",5, listOf("docx","odt"))
  val moduloImagen = Modulo("modulo imagen",5, listOf("jpg","png","gif"))
  //-----------------------------------------------------------------------PEDIDOS
  describe("Un pedido:"){
    it("Tiene protocolo:"){
      val pedidoHTML = Pedido("127.0.0.1","http://pepito.com.ar/documentos/doc1.html", fecha)
      pedidoHTML.protocolo().shouldBe("http")
    }
    it("Tiene ruta:"){
      val pedidoHTML = Pedido("127.0.0.1","http://pepito.com.ar/documentos/doc1.html", fecha)
      pedidoHTML.ruta().shouldBe("/documentos/doc1.html")
    }
    it("Tiene extension:"){
      val pedidoHTML = Pedido("127.0.0.1","http://pepito.com.ar/documentos/doc1.html", fecha)
      pedidoHTML.extension().shouldBe("html")
    }
  }
  //-----------------------------------------------------------------------SERVIDOR
  describe("Un servidor web") {
    it("puede atender pedidos"){
      servidor.puedeAtender(pedido1).shouldBe(false)
      servidor.puedeAtender(pedido2).shouldBe(true)
    }
  }
  //-----------------------------------------------------------------------MODULOS
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
  //-----------------------------------------------------------------------ANALIZADOR
  describe("Un analizador:"){
    it("Un analizador de demora"){
      val sv = ServidorWeb()
      val moduloTextos = Modulo("qwe",5, listOf("txt","docx","odt"));sv.agregarModulo(moduloTextos)
      val moduloFotos = Modulo("asd",15, listOf("jpg","png","gif"));sv.agregarModulo(moduloFotos)
      val moduloVideos = Modulo("zxc",25, listOf("avi","mp4","dvd"));sv.agregarModulo(moduloVideos)
      val analizaDemoras = AnalizadorDemora(10);sv.agregarAnalizador(analizaDemoras)
      sv.atender(Pedido("1.1.1.1","http://hola/asd/qwe/perrito.jpg",fecha))
      sv.atender(Pedido("1.1.1.1","http://hola/asd/qwe/gato.gif",fecha))
      sv.atender(Pedido("1.1.1.1","http://hola/asd/qwe/sapito.txt",fecha))
      sv.atender(Pedido("1.1.1.1","http://hola/asd/video/guau.mp4",fecha))
      sv.atender(Pedido("1.1.1.1","http://hola/asd/video/miau.avi",fecha))
      analizaDemoras.cantidadDeRespDemoradas(moduloTextos).shouldBe(0)
      analizaDemoras.cantidadDeRespDemoradas(moduloFotos).shouldBe(2)
      analizaDemoras.cantidadDeRespDemoradas(moduloVideos).shouldBe(2)
    }
    describe("Un analizador de ip's sospechosas"){
      it("Puede ver los pedidos totales de una ip sospechosa:"){
        val sv = ServidorWeb()
        val moduloTextos = Modulo("qwe",5, listOf("txt","docx","odt"));sv.agregarModulo(moduloTextos)
        val moduloFotos = Modulo("asd",15, listOf("jpg","png","gif"));sv.agregarModulo(moduloFotos)
        val moduloVideos = Modulo("zxc",25, listOf("avi","mp4","dvd"));sv.agregarModulo(moduloVideos)
        val analizaIpes = AnalizadorIpSospechosa();sv.agregarAnalizador(analizaIpes)
        sv.atender(Pedido("1.1.1.1","http://hola/asd/perrito.jpg",fecha))
        sv.atender(Pedido("2.2.2.2","http://hola/asd/gato.gif",fecha))
        sv.atender(Pedido("2.2.2.2","http://hola/asd/sapito.txt",fecha))
        sv.atender(Pedido("4.1.1.1","http://hola/asd/guau.mp4",fecha))
        sv.atender(Pedido("5.1.1.1","http://hola/asd/miau.avi",fecha))
        sv.atender(Pedido("2.2.2.2","http://hola/asd/croak.odt",fecha))
        sv.atender(Pedido("2.2.2.2","http://hola/asd/prrr.docx",fecha))
        analizaIpes.pedidosDeIpSospechosa("1.1.1.1").shouldBe(1)
        analizaIpes.pedidosDeIpSospechosa("2.2.2.2").shouldBe(4)
        analizaIpes.pedidosDeIpSospechosa("7.7.7.7").shouldBe(0)
      }
      it("Puede ver cual es el modulo mas consultado por ip's sospechosas:"){
        val sv = ServidorWeb()
        val moduloTextos = Modulo("qwe",5, listOf("txt","docx","odt"));sv.agregarModulo(moduloTextos)
        val moduloFotos = Modulo("asd",15, listOf("jpg","png","gif"));sv.agregarModulo(moduloFotos)
        val moduloVideos = Modulo("zxc",25, listOf("avi","mp4","dvd"));sv.agregarModulo(moduloVideos)
        val analizaIpes = AnalizadorIpSospechosa();sv.agregarAnalizador(analizaIpes)
        analizaIpes.ipSospechosas.add("2.2.2.2")
        analizaIpes.ipSospechosas.add("0.0.0.2")
        sv.atender(Pedido("1.1.1.1","http://hola/asd/perrito.jpg",fecha))
        sv.atender(Pedido("2.2.2.2","http://hola/asd/gato.gif",fecha))
        sv.atender(Pedido("2.2.2.2","http://hola/asd/sapito.txt",fecha))
        sv.atender(Pedido("4.1.1.1","http://hola/asd/guau.mp4",fecha))
        sv.atender(Pedido("5.1.1.1","http://hola/asd/miau.avi",fecha))
        sv.atender(Pedido("2.2.2.2","http://hola/asd/croak.odt",fecha))
        sv.atender(Pedido("2.2.2.2","http://hola/asd/prrr.docx",fecha))
        analizaIpes.moduloMasConsultadoPorIpSospechosas().shouldBe(moduloTextos)
      }
    }
  }
})
