package kmeans

trait KMeans {
  type Point
  type Distance

  def distance(p1: Point, p2: Point): Distance

  def calculateMean(points: List[Point]): Mean

  def distanceOrdering: Ordering[Distance]

  case class Mean(value: Point)

  case class Cluster(mean: Mean, points: List[Point])

  final def nearestMean(point: Point, means: Set[Mean]): Mean =
    means.minBy(mean => distance(point, mean.value))(distanceOrdering)

  final def calculateCluster(points: List[Point], means: Set[Mean]): List[Cluster] =
    points.groupBy(point => nearestMean(point, means)).toList.map {
      case (mean, pointsByMean) => Cluster(mean, pointsByMean)
    }

  final def iterate(points: List[Point], means: Set[Mean]): Stream[List[Cluster]] = {
    val cluster = calculateCluster(points, means)
    cluster #:: {
      val newMeans = cluster.map(c => calculateMean(c.points)).toSet
      if (means == newMeans) Stream.empty else iterate(points, newMeans)
    }
  }

  def initialMeans(k: Int, points: List[Point]): Set[Mean] =
    points.take(k).map(Mean).toSet

  final def runLog(k: Int, points: List[Point]): Stream[List[Cluster]] =
    iterate(points, initialMeans(k, points))

  final def runLast(k: Int, points: List[Point]): List[Cluster] =
    runLog(k, points).last
}
