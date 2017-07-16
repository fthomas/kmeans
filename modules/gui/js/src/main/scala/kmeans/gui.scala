package kmeans

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

    def drawPoint(point: Point2D): Unit =
      ctx.fillRect(point.x, point.y, 8.0, 8.0)

    def drawMean(mean: Mean[Point2D]): Unit =
      ctx.fillCircle(mean.value, 8.0, "black")

    def drawClusters(clusters: List[Cluster[Point2D]]): Unit =
      clusters.zip(color.randomStream).foreach {
        case (cluster, color) =>
          ctx.fillStyle = color.toString
          cluster.points.foreach(drawPoint)
          drawMean(cluster.mean)
      }

    def animateClusters(steps: Stream[List[Cluster[Point2D]]]): Unit = {
      if (steps.nonEmpty) {
        ctx.clearCanvas()
        drawClusters(steps.head)
        timers.setTimeout(100.millisecond)(animateClusters(steps.tail))
        ()
      }
    }

    implicit val point2dOrdering: Ordering[Point2D] =
      Ordering[(Double, Double)].on(m => (m.x, m.y))

    val count = 2 + Random.nextInt(7)
    val points = List.fill(count)(canvas.randomCluster(1100)).flatten
    val kMeans2D = new KMeans2D
    val steps = kMeans2D.runSteps(count, points).map(_.sortBy(_.mean.value))
    animateClusters(steps)
  }
}
