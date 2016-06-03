/**
 * Generated by apidoc - http://www.apidoc.me
 * Service version: 0.1.31
 * apidoc:0.11.27 http://www.apidoc.me/flow/splashpage/0.1.31/anorm_2_x_parsers
 */
package io.flow.splashpage.v0.anorm.conversions {

  import anorm.{Column, MetaDataItem, TypeDoesNotMatch}
  import play.api.libs.json.{JsArray, JsObject, JsValue}
  import scala.util.{Failure, Success, Try}

  /**
    * Conversions to collections of objects using JSON.
    */
  object Util {

    def parser[T](
      f: play.api.libs.json.JsValue => T
    ) = anorm.Column.nonNull { (value, meta) =>
      val MetaDataItem(qualified, nullable, clazz) = meta
      value match {
        case json: org.postgresql.util.PGobject => {
          Try {
            f(
              play.api.libs.json.Json.parse(
                json.getValue
              )
            )
          } match {
            case Success(result) => Right(result)
            case Failure(ex) => Left(
              TypeDoesNotMatch(
                s"Column[$qualified] error parsing json $value: $ex"
              )
            )
          }
        }
        case _=> {
          Left(
            TypeDoesNotMatch(
              s"Column[$qualified] error converting $value: ${value.asInstanceOf[AnyRef].getClass} to Json"
            )
          )
        }


      }
    }

  }

  object Types {
    import io.flow.splashpage.v0.models.json._
    implicit val columnToSeqSplashpagePublication: Column[Seq[_root_.io.flow.splashpage.v0.models.Publication]] = Util.parser { _.as[Seq[_root_.io.flow.splashpage.v0.models.Publication]] }
    implicit val columnToMapSplashpagePublication: Column[Map[String, _root_.io.flow.splashpage.v0.models.Publication]] = Util.parser { _.as[Map[String, _root_.io.flow.splashpage.v0.models.Publication]] }
    implicit val columnToSeqSplashpageGeo: Column[Seq[_root_.io.flow.splashpage.v0.models.Geo]] = Util.parser { _.as[Seq[_root_.io.flow.splashpage.v0.models.Geo]] }
    implicit val columnToMapSplashpageGeo: Column[Map[String, _root_.io.flow.splashpage.v0.models.Geo]] = Util.parser { _.as[Map[String, _root_.io.flow.splashpage.v0.models.Geo]] }
    implicit val columnToSeqSplashpageGeoForm: Column[Seq[_root_.io.flow.splashpage.v0.models.GeoForm]] = Util.parser { _.as[Seq[_root_.io.flow.splashpage.v0.models.GeoForm]] }
    implicit val columnToMapSplashpageGeoForm: Column[Map[String, _root_.io.flow.splashpage.v0.models.GeoForm]] = Util.parser { _.as[Map[String, _root_.io.flow.splashpage.v0.models.GeoForm]] }
    implicit val columnToSeqSplashpageSubscription: Column[Seq[_root_.io.flow.splashpage.v0.models.Subscription]] = Util.parser { _.as[Seq[_root_.io.flow.splashpage.v0.models.Subscription]] }
    implicit val columnToMapSplashpageSubscription: Column[Map[String, _root_.io.flow.splashpage.v0.models.Subscription]] = Util.parser { _.as[Map[String, _root_.io.flow.splashpage.v0.models.Subscription]] }
    implicit val columnToSeqSplashpageSubscriptionForm: Column[Seq[_root_.io.flow.splashpage.v0.models.SubscriptionForm]] = Util.parser { _.as[Seq[_root_.io.flow.splashpage.v0.models.SubscriptionForm]] }
    implicit val columnToMapSplashpageSubscriptionForm: Column[Map[String, _root_.io.flow.splashpage.v0.models.SubscriptionForm]] = Util.parser { _.as[Map[String, _root_.io.flow.splashpage.v0.models.SubscriptionForm]] }
  }

  object Standard {
    implicit val columnToJsObject: Column[play.api.libs.json.JsObject] = Util.parser { _.as[play.api.libs.json.JsObject] }
    implicit val columnToSeqBoolean: Column[Seq[Boolean]] = Util.parser { _.as[Seq[Boolean]] }
    implicit val columnToMapBoolean: Column[Map[String, Boolean]] = Util.parser { _.as[Map[String, Boolean]] }
    implicit val columnToSeqDouble: Column[Seq[Double]] = Util.parser { _.as[Seq[Double]] }
    implicit val columnToMapDouble: Column[Map[String, Double]] = Util.parser { _.as[Map[String, Double]] }
    implicit val columnToSeqInt: Column[Seq[Int]] = Util.parser { _.as[Seq[Int]] }
    implicit val columnToMapInt: Column[Map[String, Int]] = Util.parser { _.as[Map[String, Int]] }
    implicit val columnToSeqLong: Column[Seq[Long]] = Util.parser { _.as[Seq[Long]] }
    implicit val columnToMapLong: Column[Map[String, Long]] = Util.parser { _.as[Map[String, Long]] }
    implicit val columnToSeqLocalDate: Column[Seq[_root_.org.joda.time.LocalDate]] = Util.parser { _.as[Seq[_root_.org.joda.time.LocalDate]] }
    implicit val columnToMapLocalDate: Column[Map[String, _root_.org.joda.time.LocalDate]] = Util.parser { _.as[Map[String, _root_.org.joda.time.LocalDate]] }
    implicit val columnToSeqDateTime: Column[Seq[_root_.org.joda.time.DateTime]] = Util.parser { _.as[Seq[_root_.org.joda.time.DateTime]] }
    implicit val columnToMapDateTime: Column[Map[String, _root_.org.joda.time.DateTime]] = Util.parser { _.as[Map[String, _root_.org.joda.time.DateTime]] }
    implicit val columnToSeqBigDecimal: Column[Seq[BigDecimal]] = Util.parser { _.as[Seq[BigDecimal]] }
    implicit val columnToMapBigDecimal: Column[Map[String, BigDecimal]] = Util.parser { _.as[Map[String, BigDecimal]] }
    implicit val columnToSeqJsObject: Column[Seq[_root_.play.api.libs.json.JsObject]] = Util.parser { _.as[Seq[_root_.play.api.libs.json.JsObject]] }
    implicit val columnToMapJsObject: Column[Map[String, _root_.play.api.libs.json.JsObject]] = Util.parser { _.as[Map[String, _root_.play.api.libs.json.JsObject]] }
    implicit val columnToSeqString: Column[Seq[String]] = Util.parser { _.as[Seq[String]] }
    implicit val columnToMapString: Column[Map[String, String]] = Util.parser { _.as[Map[String, String]] }
    implicit val columnToSeqUUID: Column[Seq[_root_.java.util.UUID]] = Util.parser { _.as[Seq[_root_.java.util.UUID]] }
    implicit val columnToMapUUID: Column[Map[String, _root_.java.util.UUID]] = Util.parser { _.as[Map[String, _root_.java.util.UUID]] }
  }

}