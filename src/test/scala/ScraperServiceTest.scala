import org.jsoup.Jsoup
import org.specs2.mutable.Specification

class ScraperServiceTest extends Specification {

  import Fixture._

  "Scraper Service" should {
    "parse html post to case class" in {
      val post = ScraperService.parsePost(Jsoup.parse(postStr))
      post should beSome(Post(4863215, 696, "line1\n<br> line2"))
    }

    "parse simple html formatted page" in {
      val parsed = ScraperService.parsePage(page)
      parsed should haveLength(3)
    }

  }

}

object Fixture {
  val page =
    """
      |<html>
      |<div>
      | <div class="post"> Some text with <> 1 </div>
      | <div class="post"> Some text with <> 2 </div>
      | <div class="post"> Some text with <> 3 </div>
      |</div>
      |</html>
      |""".stripMargin
    val postStr =
      """
        |<div id="d4863215" class="q post">
        |			<div class="bar">
        |				<a class="qid click" href="/4863215/">#4863215</a>
        |				<a class="click votes rox" rel="nofollow" href="/rox/4863215/">+</a>
        |				<span class=" points" style="visibility: hidden;">696</span>
        |				<span class="msg">&nbsp;</span>
        |			</div>
        |			<div class="quote post-content post-body">
        |				line1<br> line2
        |			</div>
        |		</div>
        |""".stripMargin

}
