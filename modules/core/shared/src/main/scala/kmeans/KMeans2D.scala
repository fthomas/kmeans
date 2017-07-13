package kmeans

final case class Point2D(x: Double, y: Double) {
  def add(other: Point2D): Point2D =
    Point2D(x + other.x, y + other.y)
}

final class KMeans2D extends KMeans[Point2D, Double] {
  override def distance(p1: Point2D, p2: Point2D): Double =
    math.sqrt(math.pow(p1.x - p2.x, 2.0) + math.pow(p1.y - p2.y, 2.0))

  override def calculateMean(points: List[Point2D]): Mean[Point2D] = {
    val scale = 1.0 / points.size
    val sum = points.reduce[Point2D]((p1, p2) => p1.add(p2))
    Mean(Point2D(sum.x * scale, sum.y * scale))
  }

  override def distanceOrdering: Ordering[Double] =
    implicitly
}
