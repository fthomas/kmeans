package kmeans

final case class Point2D(x: Double, y: Double) {
  def add(other: Point2D): Point2D =
    Point2D(x + other.x, y + other.y)
}

final class KMeans2D extends KMeans {
  override type Point = Point2D
  override type Distance = Double

  override def distance(p1: Point, p2: Point): Distance =
    math.sqrt(math.pow(p1.x - p2.x, 2.0) + math.pow(p1.y - p2.y, 2.0))

  override def calculateMean(points: List[Point]): Mean = {
    val scale = 1.0 / points.size
    val sum = points.reduce[Point]((p1, p2) => p1.add(p2))
    Mean(Point2D(sum.x * scale, sum.y * scale))
  }

  override def distanceOrdering: Ordering[Distance] =
    implicitly
}
