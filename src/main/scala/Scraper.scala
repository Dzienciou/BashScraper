import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import com.typesafe.config.ConfigFactory
import play.api.libs.json.Json

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object Scraper {

  implicit val context      = ExecutionContext.global
  implicit val system       = ActorSystem()
  implicit val materializer = Materializer.matFromSystem
  val ws                    = StandaloneAhcWSClient()
  val outputPath            = ConfigFactory.load("application.conf").getString("output.path")
  val url                   = ConfigFactory.load("application.conf").getString("input.url")

  def main(args: Array[String]) = {

    val pagesCount: Int = args.headOption.flatMap(_.toIntOption).getOrElse(1)
    val fetchedPosts    = Future.traverse(1 to pagesCount)(i => ScraperService.fetchPage(i, ws, url)
      .map(ScraperService.parsePage))

    val posts = Await.result(fetchedPosts, Duration.Inf)
    Helpers.printToFile(outputPath, Json.prettyPrint(Json.toJson(posts.flatten)))

    val time       = ScraperService.loading.mean
    val totalPosts = posts.flatten.length

    println(s"""
         |number of posts: $totalPosts
         |time per page: ${Helpers.formatTime(time)} ms
         |time per post: ${Helpers.formatTime(time * pagesCount / totalPosts)} ms
         |""".stripMargin)

  }
}
