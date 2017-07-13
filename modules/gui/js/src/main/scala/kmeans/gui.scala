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

    val clusterCnt = 2 + Random.nextInt(5)

    def randomPoints(n: Int): List[Point2D] = {
      Random.shuffle(List
        .fill(clusterCnt)(canvas.randomPoint())
        .flatMap(p =>
          List
            .fill(1000)(
              Point2D(p.x + canvas.width / 10 * Random.nextGaussian(),
                      p.y + canvas.height / 10 * Random.nextGaussian()))
            .filter(p =>
              p.x < canvas.width && p.y < canvas.height && p.x > 0 && p.y > 0)))
    }

    def drawCluster(cluster: List[Cluster[Point2D]]): Unit = {
      cluster.zip(color.randomStream.map(_.toString())).foreach {
        case (c, color) =>
          c.points.foreach(drawPoint(_, color))
          drawPoint2(c.mean.value, Color.Black)
      }
    }

    implicit val meanOrdering: Ordering[Mean[Point2D]] =
      Ordering[(Double, Double)].on(m => (m.value.x, m.value.y))

    def animate(steps: Stream[List[Cluster[Point2D]]]): Unit = {
      if (steps.nonEmpty) {
        ctx.clearCanvas()
        drawCluster(steps.head)
        timers.setTimeout(100.millisecond)(animate(steps.tail))
        ()
      }
    }

    val k = new KMeans2D
    val steps =
      k.runSteps(clusterCnt, randomPoints(30000)).map(_.sortBy(_.mean))
    animate(steps)
  }
}
