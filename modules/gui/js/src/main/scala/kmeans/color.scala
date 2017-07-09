package kmeans

import org.scalajs.dom.ext.Color
import scala.util.Random

object color {
  val random: Stream[Color] = {
    def rgb = Random.nextInt(256)
    Stream.continually(Color(rgb, rgb, rgb))
  }
}
