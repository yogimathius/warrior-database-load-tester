package warriordatabase

import scala.concurrent.duration._

import scala.util.Random

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import java.util.concurrent.ThreadLocalRandom

class WarriorDatabaseSimulation extends Simulation {

  val httpProtocol =
    http.baseUrl("http://127.0.0.1:4000")
    .userAgentHeader("Chaos Agent - Eng. Labs 3")

  val createAndLookUpWarriors = scenario("Creation and Lookup for Warriors")
    .feed(tsv("warriors-payloads.tsv").circular())
    .exec(
      http("creation")
      .post("/warrior").body(StringBody("#{payload}"))
      .header("content-type", "application/json")
      .check(status.in(201, 422, 400))
     .check(status.saveAs("httpStatus"))
      .checkIf(session => session("httpStatus").as[String] == "201") {
        header("Location").saveAs("location")
      }
    )
    .pause(1.milliseconds, 30.milliseconds)
    .doIf(session => session.contains("location")) {
      exec(
        http("look up")
        .get("#{location}")
      )
    }

  val searchWarriors = scenario("Valid Warrior Look up")
    .feed(tsv("search-terms.tsv").circular())
    .exec(
      http("valid look up")
      .get("/warrior?t=#{t}")
    )

  val searchInvalidWarriors = scenario("Invalid Warrior Look up")
    .exec(
      http("invalid look up")
      .get("/warrior")
      .check(status.is(400))
    )

  setUp(
    createAndLookUpWarriors.inject(
      constantUsersPerSec(2).during(10.seconds),
      constantUsersPerSec(5).during(15.seconds).randomized,

      rampUsersPerSec(500).to(600).during(25.seconds)
    ),
    searchWarriors.inject(
      constantUsersPerSec(2).during(25.seconds),

      rampUsersPerSec(90).to(100).during(25.seconds)
    ),
    searchInvalidWarriors.inject(
      constantUsersPerSec(2).during(25.seconds),

      rampUsersPerSec(35).to(40).during(25.seconds)
    )
  ).protocols(httpProtocol)
}
