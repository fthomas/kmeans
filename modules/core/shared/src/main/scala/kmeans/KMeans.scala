package kmeans

final case class Mean[Point](value: Point) extends AnyVal

final case class Cluster[Point](mean: Mean[Point], points: List[Point])

trait KMeans[Point, Distance] {

  def distance(p1: Point, p2: Point): Distance

  def calculateMean(points: List[Point]): Mean[Point]

  def distanceOrdering: Ordering[Distance]

  final def nearestMean(point: Point, means: Set[Mean[Point]]): Mean[Point] =
    means.minBy(mean => distance(point, mean.value))(distanceOrdering)

  final def calculateCluster(points: List[Point],
                             means: Set[Mean[Point]]): List[Cluster[Point]] =
    points.groupBy(point => nearestMean(point, means)).toList.map {
      case (mean, pointsByMean) => Cluster(mean, pointsByMean)
    }

  final def iterate(points: List[Point],
                    means: Set[Mean[Point]]): Stream[List[Cluster[Point]]] = {
    val cluster = calculateCluster(points, means)
    cluster #:: {
      val newMeans = cluster.map(c => calculateMean(c.points)).toSet
      if (means == newMeans) Stream.empty else iterate(points, newMeans)
    }
  }

  def initialMeans(k: Int, points: List[Point]): Set[Mean[Point]] =
    points.take(k).map(Mean.apply).toSet

  final def runSteps(k: Int,
                     points: List[Point]): Stream[List[Cluster[Point]]] =
    iterate(points, initialMeans(k, points))

  final def runLast(k: Int, points: List[Point]): List[Cluster[Point]] =
    runSteps(k, points).last
}
