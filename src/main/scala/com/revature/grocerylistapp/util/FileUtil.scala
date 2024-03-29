package com.revature.grocerylistapp.util

import scala.io.BufferedSource
import scala.io.Source
import java.io.File
import java.io.FileNotFoundException
import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn.readLine

object FileUtil {

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

  