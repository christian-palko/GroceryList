package com.revature.grocerylistapp.dao

import com.revature.grocerylistapp.util.ConnectionUtil
import scala.util.Using
import com.revature.grocerylistapp.model.Item
import scala.collection.mutable.ArrayBuffer


object ItemDao {

  def getAll(): Seq[Item] = {
      val conn = ConnectionUtil.getConnection();
      Using.Manager { use =>
      
          val stmt = use(conn.prepareStatement("SELECT * FROM list;"))
          stmt.execute()
          val rs = use(stmt.getResultSet())
          val allItems: ArrayBuffer[Item] = ArrayBuffer()
          while (rs.next()) {
              
              allItems.addOne(Item.fromResultSet(rs))
          }
          allItems.toList
      }.get
  }

  def addNew(item : String) : Boolean = {

    val conn = ConnectionUtil.getConnection();
    Using.Manager { use =>

        val stmt = use(conn.prepareStatement("INSERT INTO list(item) VALUES (?);"))
        stmt.setString(1, item)
        stmt.execute()
        stmt.getUpdateCount() > 0
    }.getOrElse(false)



  }



}