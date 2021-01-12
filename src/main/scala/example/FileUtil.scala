package example

import scala.io.BufferedSource
import scala.io.Source
import java.io.File
import java.io.FileNotFoundException
import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn.readLine

object FileUtil {

// def importFile(file: String): Array[String] = {
//   var res: String = Source.fromFile(file).mkString("")
//   var res2 = res.split(",")
//     for (item <- res2) {
//       println(item)
//     } 

//   println(res2)
//   return res2
  
// }

def importFile(file: String, sep: String = " "): String = {

  var openedFile : BufferedSource = null

  try {
    openedFile = Source.fromFile(file)  
    openedFile.getLines().mkString(sep)
    } finally {
      if (openedFile != null) openedFile.close()
    }

}
  
}

  