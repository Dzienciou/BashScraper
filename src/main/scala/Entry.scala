import play.api.libs.json.Json

case class Entry(
    id: Long,
    points: Long,
    content: String
)

object Entry {
  implicit val entryFormat = Json.format[Entry]
}
