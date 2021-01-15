package com.revature.grocerylistapp

import java.io.File
import com.revature.grocerylistapp.cli.GroceryListCLI
import java.sql.Connection

object Runner extends App {
  val glist = new GroceryListCLI()
  glist.useList()
}

