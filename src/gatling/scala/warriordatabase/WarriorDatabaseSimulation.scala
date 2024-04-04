package warriordatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import java.util.concurrent.ThreadLocalRandom

/**
 * This sample is based on our official tutorials:
 *
 *   - [[https://gatling.io/docs/gatling/tutorials/quickstart Gatling quickstart tutorial]]
 *   - [[https://gatling.io/docs/gatling/tutorials/advanced Gatling advanced tutorial]]
 */
class WarriorDatabaseSimulation extends Simulation {

  val httpProtocol =
    http.baseUrl("http://127.0.0.1:3000")
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .acceptEncodingHeader("gzip, deflate")
      .userAgentHeader(
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/119.0"
      )

  val scn = scenario("Test URL scenario").exec(
    http("Warrior list").get("/warrior"),
    pause(1),
    http("Single Warrior").get("/warrior/2"),
    pause(1),
    http("Create warrior")
      .post("/warrior")
      .body(StringBody("""{"id": "1", "name": "Warrior 1", "dob": "1901-03-04", "fight_skills": ["Skill 2", "Skill 3"]}"""))
      .asJson
  );

  {
    setUp(
      scn.inject(rampUsersPerSec(3).to(12).during(120))
    ).protocols(httpProtocol);
  }
}
