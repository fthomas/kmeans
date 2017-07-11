import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.{Canvas, Document}

package object kmeans {
  implicit class DocumentOps(val self: Document) extends AnyVal {
    def createCanvas: Canvas =
      self.createElement("canvas").asInstanceOf[Canvas]
  }

  implicit class CanvasOps(val self: Canvas) extends AnyVal {
    def getContext2d: CanvasRenderingContext2D =
      self.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
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
