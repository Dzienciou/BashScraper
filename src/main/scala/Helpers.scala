import java.io.{File, FileOutputStream, OutputStreamWriter, PrintWriter}

import scala.concurrent.duration.{Duration, NANOSECONDS}

object Helpers {
  def printToFile(filename: String, content: String) = {
    val file = new FileOutputStream(new File(filename))
    val printWriter = new PrintWriter(new OutputStreamWriter(file, "UTF-8"))
    try {
      printWriter.print(content)
    } finally {
      printWriter.close()
    }
  }

  def formatTime(time: Double) = Duration(time, NANOSECONDS).toMillis
}
