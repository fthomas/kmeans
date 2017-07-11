package kmeans

import org.scalajs.dom.ext.Color
import scala.util.Random

object color {
  def random: Color = {
    def rgb = Random.nextInt(256)
    Color(rgb, rgb, rgb)
  }

  val randomStream: Stream[Color] =
    Stream.continually(random)
}
