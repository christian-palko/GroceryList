package com.revature.grocerylistapp.dao

import com.revature.grocerylistapp.util.ConnectionUtil
import scala.util.Using
import com.revature.grocerylistapp.model.Item
import scala.collection.mutable.ArrayBuffer
import com.revature.grocerylistapp.cli.GroceryListCLI


object ItemDao {

  def getAll(): Seq[Item] = {
      val conn = ConnectionUtil.getConnection();
      Using.Manager { use =>
          val stmt = use(conn.prepareStatement("SELECT item, dept_name FROM userlist LEFT JOIN itemlist ON userlist.item = itemlist.item_name order by dept_name ASC, item ASC;"))
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
        val stmt = use(conn.prepareStatement("INSERT INTO userlist(item) VALUES (?);"))
        stmt.setString(1, item)
        stmt.execute()
        stmt.getUpdateCount() > 0
    }.getOrElse(false)
  }

  def deleteFromDB(item : String) : Boolean = {
    val conn = ConnectionUtil.getConnection();
    Using.Manager { use =>
        val stmt = use(conn.prepareStatement("DELETE FROM userlist WHERE item=?;"))
        stmt.setString(1, item)
        stmt.execute()
        stmt.getUpdateCount() > 0
    }.getOrElse(false)
  }

    def clearAllFromDB() : Boolean = {
    val conn = ConnectionUtil.getConnection();
    Using.Manager { use =>
        val stmt = use(conn.prepareStatement("DELETE FROM userlist;"))
        stmt.execute()
        stmt.getUpdateCount() > 0
    }.getOrElse(false)
  }

  def changeFromDB(item : String, item2 : String) : Boolean = {
    val conn = ConnectionUtil.getConnection();
    Using.Manager { use =>
        val stmt = use(conn.prepareStatement("UPDATE userlist SET item=? WHERE item=?;"))
        stmt.setString(1, item)
        stmt.setString(2, item2)
        stmt.execute()
        stmt.getUpdateCount() > 0
    }.getOrElse(false)
  }
}