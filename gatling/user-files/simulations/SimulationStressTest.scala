
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class SimulationStressTest extends Simulation {

	var info_feeder = csv("info.csv").random

	val httpProtocol = http
		.baseURL("http://localhost:8080")
		.inferHtmlResources()

	//val uri1 = "http://cs.curs.pub.ro/2015"
	
	object Welcome {
		val welcome = exec(http("Welcome Page")
						.get("/")
						)
	}
	
	object Register {
		val register = exec(http("Register Page GET")
								.get("/register"))
						.pause(1)		
						.exec(http("Register Page POST")
								.post("/register")
								//.formParam("url", uri1)
								.formParam("url", "${url}")
						.check(regex("""urlShortText">([a-f0-9]*)""").find.saveAs("SHORT_URL"))
						)
						/*
						.exec {
							session =>
								println(session)
								session
						}
						*/
	}
	
	object Browse {
		val browse = exec(http("Browse Page")
						.get("/browse"))
						.pause(1)
						.exec(http("Browse Page POST")
						.post("/browse")
						.formParam("url","${SHORT_URL}"))
	}
	
	val scn = scenario("SimulationStressTest")
				.feed(info_feeder)
				.exec(Welcome.welcome)
				.pause(2)
				.exec(Register.register)
				.pause(5)
				.exec(Browse.browse)
				
	val usersCount: Int = Integer.getInteger("users", 0).toInt
	val usersInBatch: Int = Integer.getInteger("usersB", 0).toInt
	val overSecs: Int = Integer.getInteger("overSecs", 0).toInt
	val pauseSecs: Int = Integer.getInteger("pauseSecs", 1).toInt
		
	// set JAVA_OPTS=-Dusers=4 -DusersB=2 -DoverSecs=10 -DpauseSecs=7	
		
	//setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
	setUp(scn.inject(splitUsers(usersCount) into (rampUsers(usersInBatch) over(overSecs seconds)) separatedBy(pauseSecs seconds))).protocols(httpProtocol)
	
}