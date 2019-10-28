import play.api.libs.json.{JsNumber, JsString, JsValue, Json, Writes}

case class Post(
    id: Long,
    points: Long,
    content: String
)

object Post {
  implicit val postReads = Json.reads[Post]
  implicit val postWrites = new Writes[Post] {
    def writes(post: Post): JsValue = {
      Json.obj(
        "id"     -> JsNumber(post.id),
        "points" -> JsNumber(post.points),
        "text"   -> JsString(parse(post.content))
      )
    }
  }

  private def parse(text: String) =
    text
      .replace("&gt;", ">")
      .replace("&lt;", "<")
}
