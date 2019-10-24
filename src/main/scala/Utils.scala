import java.io.{File, FileOutputStream, OutputStreamWriter, PrintWriter}

import nl.grons.metrics4.scala.DefaultInstrumented
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import scala.jdk.CollectionConverters._
import scala.concurrent.duration._

object Utils extends DefaultInstrumented {

  val loading = metrics.timer("loading")

  def printToFile(filename: String, content: String) = {
    val file        = new FileOutputStream(new File(filename))
    val printWriter = new PrintWriter(new OutputStreamWriter(file, "UTF-8"))
    try {
      printWriter.print(content)
    } finally {
      printWriter.close()
    }
  }

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
      Entry(id, points, post.select("div.post-content").first.text)
    }
  }

  def formatTime(time: Double) = Duration(time, NANOSECONDS).toMillis
}
