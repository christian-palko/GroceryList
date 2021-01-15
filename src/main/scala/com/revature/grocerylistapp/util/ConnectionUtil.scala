package com.revature.grocerylistapp.util

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

object ConnectionUtil {
  
  var conn: Connection = null;

  def getConnection() : Connection = {
    if (conn == null || conn.isClosed()) {
      classOf[org.postgresql.Driver].newInstance()

      conn = DriverManager.getConnection(
      "jdbc:postgresql://localhost:5432/grocerylist",
      "postgres",
      "password"


      )
    }
    conn
  }
}