package example

import scala.io.BufferedSource
import scala.io.Source
import java.io.File

object FileUtil {


def getCsvContent(filename: String, sep: " "): String = {
  var openedFile : BufferedSource = null
  
  try{
    openedFile = Source.fromFile(filename)
    openedFile.getLines().mkString(sep)
    
  } finally {
    if (openedFile != null) openedFile.close()
  }
  



}
  
}

