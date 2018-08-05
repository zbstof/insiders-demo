package services

import javax.inject.Singleton

import scala.collection.mutable
import scala.io.Source

@Singleton
class BoostingService {
  private val boostingsFileName = "boostings.txt"
  private var keywordsByIds = scala.collection.mutable.Map[Int, mutable.Set[String]]()

  def currentBoostings: mutable.Map[Int, mutable.Set[String]] = keywordsByIds

  val linesIterator: Iterator[String] = Source.fromFile(boostingsFileName).getLines

  for (l <- linesIterator) {
    val keyValues: Array[String] = l.split("=")
    val ids: Array[Int] = keyValues(1).split(",").map(_.trim.toInt)
    val key: String = keyValues(0).trim

    for (id <- ids) keywordsByIds.getOrElseUpdate(id, mutable.Set[String](key)) += key
  }
  //promoted listings
}
