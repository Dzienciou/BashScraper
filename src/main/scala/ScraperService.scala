import nl.grons.metrics4.scala.DefaultInstrumented
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.ExecutionContext
import scala.jdk.CollectionConverters._

object ScraperService extends DefaultInstrumented {

  val loading = metrics.timer("loading")

  def fetchPage(i: Int, ws: StandaloneAhcWSClient, url: String)(implicit ex: ExecutionContext) = {
    loading.timeFuture(
      ws.url(url).withQueryStringParameters("page" -> i.toString).get().map(_.body)
    )
  }

  def parsePage(page: String) = {
    Jsoup.parse(page).select("div.post").iterator.asScala.toSeq.map(parsePost) collect {
      case Some(p) => p
    }
  }

  def parsePost(post: Element) = {
    for {
      id     <- post.select("a.qid").first.text.drop(1).toLongOption
      points <- post.select("span.points").first.text.toLongOption
    } yield {
      val a = post.select("div.post-content").first.html()
      Post(id, points, a)
    }
  }
}
