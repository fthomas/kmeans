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
}
