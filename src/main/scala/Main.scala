import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import com.typesafe.config.ConfigFactory
import play.api.libs.json.Json

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object Main {

  implicit val context      = ExecutionContext.global
  implicit val system       = ActorSystem()
  implicit val materializer = Materializer.matFromSystem
  val ws                    = StandaloneAhcWSClient()
  val outputPath            = ConfigFactory.load("application.conf").getString("output.path")
  val url                   = "http://bash.org.pl/latest"

  def main(args: Array[String]) = {
    import Utils._

    val pagesCount: Int = args.headOption.flatMap(_.toIntOption).getOrElse(1)
    val fetchedPosts    = Future.traverse(1 to pagesCount)(i => fetchPage(i, ws, url).map(parsePage))

    val posts = Await.result(fetchedPosts, Duration.Inf)
    printToFile(outputPath, Json.toJson(posts.flatten).toString)

    val time       = loading.mean
    val totalPosts = posts.flatten.length

    println(s"""
         |number of posts: $totalPosts
         |time per page: ${formatTime(time)} ms
         |time per post: ${formatTime(time * pagesCount / totalPosts)} ms
         |""".stripMargin)

  }
}
