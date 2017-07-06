import org.scalajs.dom.{CanvasRenderingContext2D, document}
import org.scalajs.dom.html.Canvas

object Hello {
  def main(args: Array[String]): Unit = {
    val canvas = document.createElement("canvas").asInstanceOf[Canvas]
    document.body.appendChild(canvas)

    val ctx = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    def drawPoint(x: Double, y: Double) =
      ctx.fillRect(x, y, 5, 5)

    ctx.fillStyle = "red"
    drawPoint(10, 10)
    drawPoint(20, 20)

    ()
  }
}
