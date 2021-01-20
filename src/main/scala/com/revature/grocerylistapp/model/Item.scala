package com.revature.grocerylistapp.model

import java.sql.ResultSet

case class Item(item: String, dept_name: String) {}

object Item {
  def fromResultSet(rs : ResultSet) : Item = {
    apply(
    rs.getString("item"),
    rs.getString("dept_name")
    )
  }
}