package kmeans

import cats.kernel.Monoid

trait Monoidal extends KMeans {
  def monoid: Monoid[Point]

  def times(p: Point, scalar: Double): Point

  final override def calculateMean(points: List[Point]): Mean = {
    val scale = 1.0 / points.size
    Mean(times(monoid.combineAll(points), scale))
  }
}
