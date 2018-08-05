package services

import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsArray, JsValue, Json}

import scala.collection.mutable
import scala.io.Source

@Singleton
class BoostingService @Inject()(val elasticService: ElasticService) {
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

  private val jsonArrayKeywordsById: mutable.Map[Int, JsValue] = for ((k,v) <- keywordsByIds) yield (k, Json.toJson(v))
  elasticService.putPromotedListings(jsonArrayKeywordsById)
}
