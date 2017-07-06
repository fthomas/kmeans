import org.scalajs.dom
import dom.document

object Hello {
  def main(args: Array[String]): Unit = {
    val p = document.createElement("p")
    val t = document.createTextNode("Hello, world!")
    p.appendChild(t)
    document.body.appendChild(p)

    println("Hello, world!")
    println("EOF")
  }
}
