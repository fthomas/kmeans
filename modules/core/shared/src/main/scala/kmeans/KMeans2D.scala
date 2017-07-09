package kmeans
import cats.kernel.Monoid

final case class Point2D(x: Double, y: Double)

class KMeans2D extends Monoidal {
  override type Point = Point2D
  override type Distance = Double

  override def distanceOrdering: Ordering[Distance] =
    implicitly

  override def distance(p1: Point, p2: Point): Double =
    math.sqrt(math.pow(p1.x - p2.x, 2) + math.pow(p1.y - p2.y, 2))

  override def monoid: Monoid[Point2D] =
    new Monoid[Point2D] {
      override def empty: Point2D =
        Point2D(0.0, 0.0)

      override def combine(p1: Point2D, p2: Point2D): Point2D =
        Point2D(p1.x + p2.x, p1.y + p2.y)
    }

  override def times(p: Point2D, scalar: Double): Point2D =
    Point2D(p.x * scalar, p.y * scalar)
}
