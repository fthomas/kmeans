import org.scalajs.dom
import org.scalajs.dom.ext.Color
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.{CanvasRenderingContext2D, document, window}

object Hello {
  def main(args: Array[String]): Unit = {

    val canvas = document.createElement("canvas").asInstanceOf[Canvas]
    canvas.width = window.innerWidth.toInt - 10
    canvas.height = window.innerHeight.toInt - 20
    document.body.appendChild(canvas)

    val ctx = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    def drawPoint(tuple: (Int, Int), color: Color) = {
      val origStyle = ctx.fillStyle
      ctx.fillStyle = color.toString()
      ctx.fillRect(tuple._1.toDouble, tuple._2.toDouble, 8, 8)
      ctx.fillStyle = origStyle
    }
    def drawPoint2(tuple: (Int, Int), color: Color) = {
      val origStyle = ctx.fillStyle
      ctx.fillStyle = color.toString()
      ctx.fillRect(tuple._1.toDouble, tuple._2.toDouble, 16, 16)
      ctx.fillStyle = origStyle
    }

    def randomPoints(): List[(Int, Int)] =
      List.fill(10000)(
        (scala.util.Random.nextInt(600), scala.util.Random.nextInt(600))
      )

    val k = new KmeansImpl()
    // val cluster = k.kMeans(30, randomPoints())
//List((40,40), (10,1), (200,300), (400,400),
    //(30, 150), (500,500),  (50,50), (300, 301), (452,102)).reverse

    val colors = Color.all.filter(_ != Color.White)
    val colors2 = colors ++ colors ++ colors ++ colors ++ colors

    /*cluster.zip(colors2).foreach { case (c, color) =>
      c.points.foreach(drawPoint(_, color))
      drawPoint2(c.mean.value, Color.Black)
    }*/

    //println(cluster)

    val cluster2 = k.kMeans2(3, randomPoints())

    cluster2.foreach { cluster =>
      println(cluster)

      ctx.clearRect(0.0, 0.0, 2000.0, 2000.0)
      cluster.zip(colors2).foreach {
        case (c, color) =>
          c.points.foreach(drawPoint(_, color))
          drawPoint2(c.mean.value, Color.Black)
      }

    }
    ()
  }
}
