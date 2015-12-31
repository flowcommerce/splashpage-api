package controllers

import io.flow.splashpage.v0.{Authorization, Client}
import io.flow.splashpage.v0.errors.{ErrorsResponse, UnitResponse}
import io.flow.splashpage.v0.models.{Geo, GeoForm, Publication, Subscription, SubscriptionForm}
import io.flow.user.v0.models.User
import io.flow.play.clients.{MockAuthorizationClient, MockUserClient}
import java.util.UUID
import scala.util.{Failure, Success, Try}

import play.api.libs.ws._
import play.api.test._

class SubscriptionsSpec extends PlaySpecification with MockClient {

  import scala.concurrent.ExecutionContext.Implicits.global

  "POST /subscriptions" in new WithServer(port=port) {
    val form = createSubscriptionForm()
    val subscription = await(anonClient.subscriptions.post(form))
    subscription.email must beEqualTo(form.email)
    subscription.publication must beEqualTo(form.publication)
  }

  "POST /subscriptions validates empty email" in new WithServer(port=port) {
    expectErrors {
      anonClient.subscriptions.post(createSubscriptionForm().copy(email = "  "))
    }.errors.map(_.message) must beEqualTo(Seq("Email address cannot be empty"))
  }

  "POST /subscriptions validates invalid email" in new WithServer(port=port) {
    expectErrors {
      anonClient.subscriptions.post(createSubscriptionForm().copy(email = "test"))
    }.errors.map(_.message) must beEqualTo(Seq("Please enter a valid email address"))
  }

  "POST /subscriptions is idempotent with duplicate email" in new WithServer(port=port) {
    val form = createSubscriptionForm()
    val sub = createSubscription(form)

    anonClient.subscriptions.post(form.copy(email = " " + form.email + " "))
    anonClient.subscriptions.post(form.copy(email = " " + form.email.toUpperCase + " "))
    anonClient.subscriptions.post(form.copy(email = form.email.toLowerCase))

    await(
      identifiedClient.subscriptions.getById(sub.id)
    ).email must beEqualTo(form.email)
  }

  "POST /subscriptions validates publication" in new WithServer(port=port) {
    val form = createSubscriptionForm()

    expectErrors {
      anonClient.subscriptions.post(form.copy(publication = Publication.UNDEFINED("invalid_publication")))
    }.errors.map(_.message) must beEqualTo(Seq("Publication not found"))
  }

  "POST /subscriptions validates longitude" in new WithServer(port=port) {
    val form = createSubscriptionForm().copy(
      geo = Some(
        GeoForm(
          latitude = Some("42.213"),
          longitude = None
        )
      )
    )

    expectErrors {
      anonClient.subscriptions.post(form)
    }.errors.map(_.message) must beEqualTo(Seq("If specifying latitude, must also specify longitude"))
  }

  "GET /subscriptions/:id" in new WithServer(port=port) {
    val sub = createSubscription()
    await(
      identifiedClient.subscriptions.getById(sub.id)
    ) must beEqualTo(sub)
  }

  "GET /subscriptions/:id requires authorization" in new WithServer(port=port) {
    expectNotAuthorized(
      anonClient.subscriptions.getById(UUID.randomUUID.toString)
    )

    val otherClient = new Client(
      s"http://localhost:$port",
      Some(Authorization.Basic(UUID.randomUUID.toString.toString))
    )

    expectNotAuthorized(
      anonClient.subscriptions.getById(UUID.randomUUID.toString)
    )
  }

  "GET /subscriptions/:id w/ invalid id returns 404" in new WithServer(port=port) {
    expectNotFound(
      identifiedClient.subscriptions.getById(UUID.randomUUID.toString)
    )
  }

  "GET /subscriptions by id" in new WithServer(port=port) {
    val sub = createSubscription()
    await(
      identifiedClient.subscriptions.get(id = Some(Seq(sub.id)))
    ) must beEqualTo(Seq(sub))

    await(
        identifiedClient.subscriptions.get(id = Some(Seq(UUID.randomUUID.toString)))
    ) must beEqualTo(Nil)
  }

  "GET /subscriptions by email" in new WithServer(port=port) {
    val sub = createSubscription()
    await(
      identifiedClient.subscriptions.get(email = Some(sub.email))
    ) must beEqualTo(Seq(sub))

    await(
      identifiedClient.subscriptions.get(email = Some(UUID.randomUUID.toString + "@flow.io"))
    ) must beEqualTo(Nil)
  }

  "GET /subscriptions by publication" in new WithServer(port=port) {
    val sub = createSubscription()
    await(
      identifiedClient.subscriptions.get(email = Some(sub.email), publication = Some(sub.publication))
    ) must beEqualTo(Seq(sub))
  }

 "POST /subscriptions validates latitude" in new WithServer(port=port) {
    val form = createSubscriptionForm().copy(
      geo = Some(
        GeoForm(
          latitude = None,
          longitude = Some("42.213")
        )
      )
    )

   expectErrors {
      anonClient.subscriptions.post(form)
    }.errors.map(_.message) must beEqualTo(Seq("If specifying longitude, must also specify latitude"))
  }

  "POST /subscriptions geo info ignores empty string" in new WithServer(port=port) {
    val form = GeoForm(
      ipAddress = Some("  ")
    )

    val sub = createSubscription(createSubscriptionForm().copy(geo = Some(form)))
    sub.geo must beEqualTo(Geo())
  }

  "POST /subscriptions stores geo info" in new WithServer(port=port) {
    val form = GeoForm(
      ipAddress = Some("127.0.0.1"),
      latitude = Some("42.213"),
      longitude = Some("12.213")
    )

    val sub = createSubscription(createSubscriptionForm().copy(geo = Some(form)))
    sub.geo must beEqualTo(
      Geo(
        ipAddress = form.ipAddress,
        latitude = form.latitude,
        longitude = form.longitude
      )
    )
  }

}
