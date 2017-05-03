import cats.data.NonEmptyList
import cats.kernel.Eq

import scala.annotation.tailrec

trait KMeans[T, R] {

  def distance(t1: T, t2: T): R

  def centroid(ts: NonEmptyList[T]): T

  implicit def eqT: Eq[T]

  implicit def orderingR: Ordering[R]

  final case class Mean(value: T)

  type Means = NonEmptyList[Mean]

  final case class Cluster(mean: Mean, ts: NonEmptyList[T])

  final def eqMean: Eq[Mean] =
    Eq.by(_.value)

  final def assignOne(t: T, means: Means): Mean = {
    val (mean, _) = means.map(m => (m, distance(t, m.value))).toList.minBy {
      case (_, dist) => dist
    }
    mean
  }

  final def assignMany(ts: List[T], means: Means): List[Cluster] =
    ts.groupBy(t => assignOne(t, means)).toList.collect {
      case (mean, head :: tail) => Cluster(mean, NonEmptyList(head, tail))
    }

  final def computeMean(cluster: Cluster): Mean =
    Mean(centroid(cluster.ts))

  @tailrec
  final def iterate(ts: List[T], means: Means): List[Cluster] = {
    val result = assignMany(ts, means)
    val oldAndNewMeans = result.map(c => (c.mean, computeMean(c)))
    val stable = oldAndNewMeans.forall((eqMean.eqv _).tupled)
    if (stable) result
    else {
      NonEmptyList.fromList(oldAndNewMeans.map(_._2)) match {
        case Some(newMeans) => iterate(ts, newMeans)
        case None => result
      }
    }
  }
}
