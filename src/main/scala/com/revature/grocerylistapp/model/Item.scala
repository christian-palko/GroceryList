package com.revature.grocerylistapp.model

import java.sql.ResultSet

case class Item(item: String) {
}

object Item {
  def fromResultSet(rs : ResultSet) : Item = {
    apply(
    rs.getString("item")
    )
  }
}