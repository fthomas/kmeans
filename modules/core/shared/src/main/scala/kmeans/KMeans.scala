package kmeans

abstract class KMeans {
  type Point

  type Distance

  def distance(x: Point, y: Point): Distance

  def calculateMean(points: List[Point]): Mean

  def distanceOrdering: Ordering[Distance]

  case class Mean(value: Point)

  case class Cluster(mean: Mean, points: List[Point])

  final def nearestMean(point: Point, means: List[Mean]): Mean =
    means.minBy(mean => distance(point, mean.value))(distanceOrdering)

  final def calculateCluster(points: List[Point], means: List[Mean]): List[Cluster] =
    points.groupBy(point => nearestMean(point, means)).toList.map {
      case (mean, pointsByMean) => Cluster(mean, pointsByMean)
    }

  final def iterate(points: List[Point], means: List[Mean]): Stream[List[Cluster]] = {
    val cluster = calculateCluster(points, means)
    cluster #:: {
      val newMeans = cluster.map(c => calculateMean(c.points))
      if (means == newMeans) Stream.empty else iterate(points, newMeans)
    }
  }

  def initialMeans(k: Int, points: List[Point]): List[Mean] =
    points.take(k).map(Mean)

  final def run(k: Int, points: List[Point]): List[Cluster] =
    iterate(points, initialMeans(k, points)).last

  final def runWithIntermediateSteps(k: Int, points: List[Point]): Stream[List[Cluster]] =
    iterate(points, initialMeans(k, points))
}

class KMeans2D extends KMeans {
  override type Point = (Int, Int)
  override type Distance = Double

  implicit def numericPoint: Numeric[Point] = new Numeric[Point] {
    override def plus(x: (Int, Int), y: (Int, Int)): (Int, Int) =
      (x._1 + y._1, x._2 + y._2)

    override def minus(x: (Int, Int), y: (Int, Int)): (Int, Int) = ???
    override def times(x: (Int, Int), y: (Int, Int)): (Int, Int) = ???
    override def negate(x: (Int, Int)): (Int, Int) = ???
    override def fromInt(x: Int): (Int, Int) = (x, x)
    override def toInt(x: (Int, Int)): Int = ???
    override def toLong(x: (Int, Int)): Long = ???
    override def toFloat(x: (Int, Int)): Float = ???
    override def toDouble(x: (Int, Int)): Double = ???
    override def compare(x: (Int, Int), y: (Int, Int)): Int = ???
  }

  override def distanceOrdering: Ordering[Distance] =
    implicitly

  override def distance(x: Point, y: Point): Double =
    math.sqrt(math.pow((x._1 - y._1).toDouble, 2) + math.pow((x._2 - y._2).toDouble, 2))

  override def calculateMean(points: List[Point]): Mean = {
    val size = points.size
    val s = points.sum
    Mean((s._1 / size, s._2 / size))
  }
}
