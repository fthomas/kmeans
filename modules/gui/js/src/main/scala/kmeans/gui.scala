package kmeans

import org.scalajs.dom.ext.Color
import org.scalajs.dom.{document, window}
import scala.concurrent.duration._
import scala.scalajs.js.timers
import scala.util.Random

object gui {
  def main(args: Array[String]): Unit = {

    val canvas = document.createCanvas
    canvas.width = window.innerWidth.toInt - 10
    canvas.height = window.innerHeight.toInt - 20
    document.body.appendChild(canvas)

    val ctx = canvas.getContext2d

    def drawPoint(p: Point2D, color: String) = {
      ctx.fillStyle = color
      ctx.fillRect(p.x, p.y, 8, 8)
    }
    def drawPoint2(p: Point2D, color: Color) = {
      ctx.fillCircle(p, 8.0, color.toString())
    }

    def randomPoints(n: Int): List[Point2D] =
      List.fill(n)(
        Point2D(Random.nextInt(canvas.width).toDouble,
                Random.nextInt(canvas.height).toDouble))

    val k = new KMeans2D

    def drawCluster(cluster: List[k.Cluster]): Unit = {
      cluster.zip(color.randomStream.map(_.toString())).foreach {
        case (c, color) =>
          c.points.foreach(drawPoint(_, color))
          drawPoint2(c.mean.value, Color.Black)
      }
    }

    implicit val meanOrdering: Ordering[k.Mean] =
      Ordering[(Double, Double)].on(m => (m.value.x, m.value.y))

    val steps = k.runLog(9, randomPoints(30000)).map(_.sortBy(_.mean))

    def animate(steps: Stream[List[k.Cluster]]): Unit = {
      steps match {
        case cluster #:: tail =>
          ctx.clearCanvas()
          drawCluster(cluster)
          timers.setTimeout(100.millisecond)(animate(tail))
          ()
        case _ => ()
      }
    }

    animate(steps)
  }
}
