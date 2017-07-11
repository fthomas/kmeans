package kmeans

import org.scalajs.dom.ext.Color
import org.scalajs.dom.{document, window}

import scala.util.Random

object gui {
  def main(args: Array[String]): Unit = {

    val canvas = document.createCanvas
    canvas.width = window.innerWidth.toInt - 10
    canvas.height = window.innerHeight.toInt - 20
    document.body.appendChild(canvas)

    val ctx = canvas.getContext2d

    def drawPoint(tuple: Point2D, color: String) = {
      val origStyle = ctx.fillStyle
      ctx.fillStyle = color
      ctx.fillRect(tuple.x, tuple.y, 12, 12)
      ctx.fillStyle = origStyle
    }
    def drawPoint2(tuple: Point2D, color: Color) = {
      val origStyle = ctx.fillStyle
      ctx.fillStyle = color.toString()
      ctx.fillRect(tuple.x, tuple.y, 16, 16)
      ctx.fillStyle = origStyle
    }

    def randomPoints(n: Int): List[Point2D] =
      List.fill(n)(
        Point2D(Random.nextInt(canvas.width).toDouble,
                Random.nextInt(canvas.height).toDouble))

    val k = new KMeans2D()
    val cluster = k.runLast(7, randomPoints(10000))

    cluster.zip(color.random.map(_.toString())).foreach {
      case (c, color) =>
        c.points.foreach(drawPoint(_, color))
        drawPoint2(c.mean.value, Color.Black)
    }

    //println(cluster)

    //scalajs.js.timers.set
    /*
    val cluster2 = k.runWithIntermediateSteps(3, randomPoints())

    cluster2.foreach { cluster =>
      println(cluster)

      ctx.clearRect(0.0, 0.0, 2000.0, 2000.0)
      cluster.zip(colors2).foreach {
        case (c, color) =>
          c.points.foreach(drawPoint(_, color))
          drawPoint2(c.mean.value, Color.Black)
      }

    }*/
    ()
  }
}
