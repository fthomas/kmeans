import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.{Canvas, Document}
import scala.util.Random

package object kmeans {
  implicit class DocumentOps(val self: Document) extends AnyVal {
    def createCanvas: Canvas =
      self.createElement("canvas").asInstanceOf[Canvas]
  }

  implicit class CanvasOps(val self: Canvas) extends AnyVal {
    def getContext2d: CanvasRenderingContext2D =
      self.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    def isVisible(p: Point2D): Boolean =
      p.x >= 0.0 && p.y >= 0.0 && p.x <= self.width && p.y <= self.height

    def randomPoint(): Point2D =
      Point2D(Random.nextInt(self.width).toDouble,
              Random.nextInt(self.height).toDouble)

    def randomCluster(numberOfPoints: Int): List[Point2D] = {
      val scale = math.min(self.width, self.height) * 0.14
      def offset() = scale * Random.nextGaussian()

      val center = randomPoint()
      def point() = Point2D(center.x + offset(), center.y + offset())
      List.fill(numberOfPoints)(point()).filter(isVisible)
    }
  }

  implicit class CanvasRenderingContext2DOps(
      val self: CanvasRenderingContext2D)
      extends AnyVal {
    def clearCanvas(): Unit = {
      // https://stackoverflow.com/questions/2142535/how-to-clear-the-canvas-for-redrawing
      self.clearRect(0.0,
                     0.0,
                     self.canvas.width.toDouble,
                     self.canvas.height.toDouble)
    }

    def fillCircle(center: Point2D, radius: Double, color: String): Unit = {
      self.beginPath()
      self.arc(center.x, center.y, radius, 0.0, 2.0 * math.Pi)
      self.fillStyle = color
      self.fill()
      self.closePath()
    }
  }
}
